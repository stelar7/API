package div;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import com.google.gson.*;

public class TV2Downloader
{
    static class Segment
    {
        public String chunk;

        public long   start = 0;

        /**
         * A single chunk of data
         *
         * @param path
         */
        public Segment(final String path)
        {
            this.chunk = path;
        }

        @Override
        public String toString()
        {
            return this.chunk;
        }
    }

    public static class Tv2Episode
    {
        /**
         * The name of the finished file
         */
        public String title;

        /**
         * The episode id
         */
        public String id;
    }

    /**
     * @see link to a single episode: http://sumo.tv2.no/programmer/serier/hotel-caesar/sesong-1/hotel- caesar-1-episode-1-846502.html 846502 is the id
     * @see link to a season: https://sumo.tv2.no/rest/shows/91275/episodes
     * @see link to all seasons: https://sumo.tv2.no/rest/shows/91275/menu
     * @see link to search: https://sumo.tv2.no/rest/categories/90000/search/grouped?text=hotel&size=3
     */
    public static boolean downloadFromTV2(final Tv2Episode episode, final Path outputPath, final Path ffmpeg)
    {
        try
        {
            episode.title = episode.title.replaceAll("[\\/:*?\"<>|]", "_").trim();

            final Path finalPath = outputPath.resolve(episode.title + ".mp4");

            if (java.nio.file.Files.exists(finalPath))
            {
                System.err.println("Episode " + episode.title + " already exists");
                return true;
            }

            final Path tempdir = new File("temp").toPath();
            final Path epdir = tempdir.resolve(episode.id);
            final Path videodir = epdir.resolve("video");
            final Path audiodir = epdir.resolve("audio");

            // This will fail when the id is not the same as the .smil file...
            // There is a way to get it, but it requires a JSESSION id..
            final String path = "http://tv2-stream-od.online.no/dashvod/_definst_/amlst:{id}.smil/manifest.mpd".replace("{id}", episode.id);

            String xml = "";
            try
            {
                xml = Internet.getPageSource(path);
            } catch (final java.io.FileNotFoundException e)
            {
                System.err.println("Cant download " + episode.title);
                System.err.println("Manifest not in expected location?");
                return true;
            } catch (final IOException e)
            {
                System.err.println(episode.id + ": " + e.getMessage());
                return false;
            }

            final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
            final Element MPD = doc.getDocumentElement();
            MPD.normalize();

            if (doc.getElementsByTagName("ContentProtection").getLength() > 0)
            {
                System.err.println("Cant download " + episode.title);
                System.err.println("DRM handling not supported");
                return true;
            }

            Files.createDirectories(audiodir);
            Files.createDirectories(videodir);

            final Element vidSegTemp = (Element) doc.getElementsByTagName("SegmentTemplate").item(0);
            final String videoMediaPATH = vidSegTemp.getAttribute("media");
            final String videoInitPATH = vidSegTemp.getAttribute("initialization");
            final Element videoTimeline = (Element) doc.getElementsByTagName("SegmentTimeline").item(0);

            final Element audSegTemp = (Element) doc.getElementsByTagName("SegmentTemplate").item(1);
            final String audioMediaPATH = audSegTemp.getAttribute("media");
            final String audioInitPATH = audSegTemp.getAttribute("initialization");
            final Element audioTimeline = (Element) doc.getElementsByTagName("SegmentTimeline").item(1);

            Element bestVideo = null;
            Element bestAudio = null;

            final NodeList qualities = doc.getElementsByTagName("Representation");
            long maxV = Long.MIN_VALUE;
            long maxA = Long.MIN_VALUE;

            for (int i = 0; i < qualities.getLength(); i++)
            {
                final Element item = (Element) qualities.item(i);
                final long bw = Long.parseLong(item.getAttribute("bandwidth"));

                if (item.hasAttribute("width"))
                {
                    if (maxV < bw)
                    {
                        maxV = bw;
                        bestVideo = item;
                    }
                } else
                {
                    if (maxA < bw)
                    {
                        maxA = bw;
                        bestAudio = item;
                    }
                }
            }

            final String videoId = bestVideo.getAttribute("id");
            final String audioId = bestAudio.getAttribute("id");

            final String videoMediaPropper = videoMediaPATH.replace("$RepresentationID$", videoId);
            final String videoInitPropper = videoInitPATH.replace("$RepresentationID$", videoId);

            final String audioMediaPropper = audioMediaPATH.replace("$RepresentationID$", audioId);
            final String audioInitPropper = audioInitPATH.replace("$RepresentationID$", audioId);

            final NodeList videoSegsList = videoTimeline.getElementsByTagName("S");
            final NodeList audioSegsList = audioTimeline.getElementsByTagName("S");

            final Segment vidInit = new Segment(videoInitPropper);
            final Segment audInit = new Segment(audioInitPropper);

            final List<Segment> videoSegments = TV2Downloader.parseSegments(vidInit, videoSegsList, videoMediaPropper);
            final List<Segment> audioSegments = TV2Downloader.parseSegments(audInit, audioSegsList, audioMediaPropper);

            final Path finalVideo = epdir.resolve("video.mp4");
            final Path finalAudio = epdir.resolve("audio.mp4");

            if (Files.exists(finalVideo))
            {
                Files.delete(finalVideo);
            }

            if (Files.exists(finalAudio))
            {
                Files.delete(finalAudio);
            }

            System.out.println("downloading segments...");

            final int parallelism = Runtime.getRuntime().availableProcessors();
            final ForkJoinPool downloadPool = new ForkJoinPool(parallelism);
            videoSegments.parallelStream().forEach(seg -> downloadPool.submit(() -> TV2Downloader.downloadSegment(episode, seg, videodir)));
            audioSegments.parallelStream().forEach(seg -> downloadPool.submit(() -> TV2Downloader.downloadSegment(episode, seg, audiodir)));

            downloadPool.shutdown();
            downloadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

            TV2Downloader.mergeAndDelete(epdir.resolve("video.mp4"), videodir);
            TV2Downloader.mergeAndDelete(epdir.resolve("audio.mp4"), audiodir);

            Files.delete(videodir);
            Files.delete(audiodir);

            final List<String> flagList = new ArrayList<String>();
            flagList.add(ffmpeg.toString());
            flagList.add("-i");
            flagList.add(finalVideo.toString());
            flagList.add("-i");
            flagList.add(finalAudio.toString());
            // THIS MAKES IT WAY FAST; BUT FILE SIZE IS 2x
            // flagList.add("-acodec");
            // flagList.add("copy");
            // flagList.add("-vcodec");
            // flagList.add("copy");
            flagList.add(tempdir.resolve(episode.title + ".mp4").toString());

            if (Runtime.getRuntime().availableProcessors() < 4)
            {
                // Limit to only one ffmpeg instance if we are on a "bad" machine
                while (div.Files.isRunning("ffmpeg.exe"))
                {
                    System.out.println("Sleeping due to lack of processing power");
                    Thread.sleep(2000);
                }
            }

            final Process process = new ProcessBuilder(flagList).inheritIO().start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> process.destroy()));
            process.waitFor();

            Files.copy(tempdir.resolve(episode.title + ".mp4"), finalPath, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(tempdir.resolve(episode.title + ".mp4"));

            Files.delete(finalAudio);
            Files.delete(finalVideo);
            Files.delete(epdir);

            System.out.println("deleting temp files");

            return true;
        } catch (

        final Exception e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    private static boolean downloadSegment(final Tv2Episode episode, final Segment segment, final Path outDir)
    {
        try
        {
            final String chunkPATH = "http://tv2-stream-od.online.no/dashvod/_definst_/amlst:{id}.smil/{chunk}".replace("{id}", episode.id);
            final File outFile = new File(outDir.toFile(), segment.chunk);

            if (outFile.exists())
            {
                return true;
            }

            final String URL = chunkPATH.replace("{chunk}", segment.chunk);

            final byte buffer[] = new byte[1024];
            int read = -1;
            final HttpURLConnection uc = (HttpURLConnection) new URL(URL).openConnection();
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            uc.setRequestProperty("Content-Language", "en-US");
            uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
            uc.setUseCaches(false);
            uc.setDoInput(true);
            uc.setDoOutput(true);

            if (uc.getResponseCode() == 502)
            {
                System.err.println(episode.id + ": failed to get " + segment + ", 502 retrying...");
                Thread.sleep(1000);
                TV2Downloader.downloadSegment(episode, segment, outDir);
                return true;
            }

            if (uc.getResponseCode() == 404)
            {
                System.err.println(episode.id + ": failed to get " + segment + ", file not in expected location?");
                return false;
            }

            try (InputStream in = uc.getInputStream(); FileOutputStream out = new FileOutputStream(outFile))
            {
                while ((read = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, read);
                }
                out.flush();
            } catch (final Exception e)
            {
                e.printStackTrace();
            }

            return true;
        } catch (final Exception e)
        {
            System.out.println("Downloading error...");
            System.out.println(e);
            return false;
        }
    }

    public static void downloadShow(final String showId, final Path outputPath, final Path ffmpeg) throws Exception
    {
        final List<Tv2Episode> episodes = new ArrayList<>();
        final Map<String, List<Tv2Episode>> episodeMapping = new HashMap<>();

        final JsonParser parser = new JsonParser();
        final JsonElement obj = parser.parse(Internet.getPageSource("https://sumo.tv2.no/rest/shows/{id}/menu".replace("{id}", showId)));
        obj.getAsJsonArray().forEach(el -> {
            try
            {
                final String id = el.getAsJsonObject().get("id").getAsString();
                final JsonElement epobj = parser.parse(Internet.getPageSource("https://sumo.tv2.no/rest/shows/{id}/episodes".replace("{id}", id)));

                epobj.getAsJsonObject().get("assets").getAsJsonArray().forEach(ee -> {
                    final Tv2Episode ep = new Tv2Episode();
                    ep.id = ee.getAsJsonObject().get("id").getAsString();
                    ep.title = ee.getAsJsonObject().get("title").getAsString();
                    episodes.add(ep);
                });

                System.out.println("all episodes of season " + id + " loaded");

                episodeMapping.put(id, new ArrayList<Tv2Episode>(episodes));
                episodes.clear();
            } catch (final Exception e)
            {
                e.printStackTrace();
            }
        });

        System.out.format("Total eps: %s%n", episodeMapping.values().stream().mapToInt(list -> list.size()).sum());

        final ForkJoinPool pool = new ForkJoinPool(2);
        episodeMapping.forEach((k, v) -> {
            v.forEach(ep -> {
                try
                {
                    pool.submit(() -> {
                        try
                        {
                            int i = 0;
                            while (!TV2Downloader.downloadFromTV2(ep, outputPath, ffmpeg))
                            {
                                Thread.sleep(2500);
                                if (i++ == 5)
                                {
                                    System.err.println(ep.id + ": ended without downloading");
                                    break;
                                }
                            }

                            System.out.format("Thread done! remaining eps: %s%n", pool.getQueuedSubmissionCount());

                        } catch (final Exception e1)
                        {
                            e1.printStackTrace();
                        }
                    });
                } catch (final Exception e1)
                {
                    e1.printStackTrace();
                }
            });
        });

        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }

    private static void mergeAndDelete(final Path toFile, final Path fromDir)
    {
        final File vFile = toFile.toFile();
        final List<Path> paths = new ArrayList<>();

        for (final File f : fromDir.toFile().listFiles())
        {
            paths.add(f.toPath());
        }

        paths.sort(new NaturalOrderComparator());

        for (final Path f : paths)
        {
            int read = -1;
            final byte buffer[] = new byte[1024];
            try (FileInputStream in = new FileInputStream(f.toFile()); FileOutputStream out = new FileOutputStream(vFile, true))
            {
                while ((read = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, read);
                }
                out.flush();
            } catch (final Exception e)
            {
                e.printStackTrace();
            }

            f.toFile().delete();
        }
    }

    private static List<Segment> parseSegments(final Segment init, final NodeList list, final String mediaPropper)
    {
        final List<Segment> segments = new ArrayList<>();
        final Segment first = new Segment(mediaPropper.replace("$Time$", "0"));
        segments.add(init);
        segments.add(first);
        for (int i = 0; i < (list.getLength() - 1); i++)
        {
            final Node segnode = list.item(i);
            if (segnode.getNodeType() == Node.ELEMENT_NODE)
            {
                final Element segelem = (Element) segnode;
                final Segment last = segments.get(segments.size() - 1);
                final Segment addme = new Segment(mediaPropper.replace("$Time$", "" + (Long.parseLong(segelem.getAttribute("d")) + last.start)));
                addme.start = Long.parseLong(segelem.getAttribute("d")) + last.start;
                segments.add(addme);
            }
        }
        return segments;
    }

}
