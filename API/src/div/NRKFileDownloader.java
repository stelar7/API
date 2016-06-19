package div;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import com.google.common.collect.*;
import com.google.gson.*;

public class NRKFileDownloader
{

    public static String[] get2(final String id) throws Exception
    {
        String source = getPageSource("https://tv.nrk.no/serie/a/" + id);

        String searchFor = "\"episodenumber\" content=\"";
        String searchFor2 = "\"seriestitle\" content=\"";
        String searchFor3 = "\"seasonid\" content=\"";

        String epno = source.substring(source.indexOf(searchFor) + searchFor.length());
        epno = epno.substring(0, epno.indexOf("\""));

        String title = source.substring(source.indexOf(searchFor2) + searchFor2.length());
        title = title.substring(0, title.indexOf("\""));

        String season = source.substring(source.indexOf(searchFor3) + searchFor3.length());
        season = season.substring(0, season.indexOf("\""));

        String fullName = season + "/ " + title + " - " + epno;

        String APiPath = "https://psapi-ne.nrk.no/mediaelement/" + id;
        String apiPathData = getPageSource(APiPath);

        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(apiPathData).getAsJsonObject();
        String subtitlesUrl = obj.get("subtitlesUrlPath").getAsString();

        String mediaUrl = obj.get("mediaUrl").getAsString();
        String start = mediaUrl.substring(0, mediaUrl.lastIndexOf("/") + 1);

        System.out.println(fullName);
        System.out.println(APiPath);
        System.out.println(subtitlesUrl);
        System.out.println(mediaUrl);

        final List<String> data = Lists.newArrayList(getPageSource(mediaUrl).split("\n"));

        int max = 0;
        String url = "";

        for (int i = 0; i < data.size(); i++)
        {
            String line = data.get(i);

            if (line.contains("BANDWIDTH"))
            {
                String band = line.substring(line.indexOf("BANDWIDTH=") + 10);
                int bw = Integer.parseInt(band.substring(0, band.indexOf(",")));

                if (bw > max)
                {
                    max = bw;
                    url = data.get(i + 1);
                }
            }
        }

        String partsPath = start + url;
        System.out.println(partsPath);

        List<String> parts = Lists.newArrayList(getPageSource(partsPath).split("\n"));
        parts = parts.stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());
        parts.forEach(s -> {
            try
            {
                String name = s.substring(0, s.indexOf("?"));

                File file = new File(id, name);
                file.getParentFile().mkdirs();

                Internet.download(start + s, file);

                System.out.println(file);

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        File outFile = new File(id, id + ".ts");
        List<File> files = Lists.newArrayList(outFile.getParentFile().listFiles());

        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outFile, true)));
        files.stream().sorted(new NaturalOrderComparator()).forEach(file -> {
            try
            {
                System.out.println(file.getName());
                DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
                int inb = 0;
                while ((inb = in.read()) != -1)
                {
                    out.write(inb);
                }
                in.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        });
        out.flush();
        out.close();

        new File("output", season).mkdirs();
        ProcessBuilder p = new ProcessBuilder("ffmpeg", "-i", id + "/" + id + ".ts", "-c", "copy", "output/" + fullName + ".mkv");
        p.inheritIO();
        p.redirectErrorStream(true);
        Process p2 = p.start();
        p2.waitFor();

        Files.deleteFolderAndItsContent(Paths.get(id));

        return new String[] { fullName, subtitlesUrl };
    }

    @Deprecated
    public static String[] get(final String id) throws Exception
    {
        String s = Internet.getPageSource("https://tv.nrk.no/serie/a/" + id);

        String searchFor = "\"episodenumber\" content=\"";
        String searchFor2 = "\"seriestitle\" content=\"";
        String searchFor3 = "\"seasonid\" content=\"";
        String searchFor4 = "programsubtitles/";

        String epno = s.substring(s.indexOf(searchFor) + searchFor.length());
        epno = epno.substring(0, epno.indexOf("\""));

        String title = s.substring(s.indexOf(searchFor2) + searchFor2.length());
        title = title.substring(0, title.indexOf("\""));

        String season = s.substring(s.indexOf(searchFor3) + searchFor3.length());
        season = season.substring(0, season.indexOf("\""));

        String subtitle = s.substring(s.indexOf(searchFor4) + searchFor4.length());
        subtitle = subtitle.substring(0, subtitle.indexOf("\""));
        System.out.println(subtitle);

        String fullName = season + "/ " + title + " - " + epno;

        new File("output", fullName.substring(0, fullName.indexOf("/"))).mkdirs();

        s = s.substring(s.indexOf("data-hls-media") + 16);
        s = s.substring(0, s.indexOf("\""));
        final File temp = File.createTempFile("output/master", "m3u8");
        temp.deleteOnExit();

        System.out.println(s);

        Internet.download(s, temp);
        final List<String> data = java.nio.file.Files.readAllLines(temp.toPath());

        int max = 0;
        String url = "";

        for (int i = 0; i < data.size(); i++)
        {
            String line = data.get(i);

            if (line.contains("BANDWIDTH"))
            {
                String band = line.substring(line.indexOf("BANDWIDTH=") + 10);
                int bw = Integer.parseInt(band.substring(0, band.indexOf(",")));

                if (bw > max)
                {
                    max = bw;
                    url = data.get(i + 1);
                }
            }
        }

        final ProcessBuilder p = new ProcessBuilder("ffmpeg", "-i", url, "-c", "copy", "output/" + fullName + ".mkv");
        p.inheritIO();
        p.redirectErrorStream(true);
        final Process p2 = p.start();
        p2.waitFor();

        return new String[] { fullName, subtitle };
    }

    public static String getPageSource(final String URL) throws Exception
    {

        final StringJoiner joiner = new StringJoiner("\n");
        final URL Url = new URL(URL);
        final URLConnection uc = Url.openConnection();
        uc.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        uc.setRequestProperty("Accept-Language", "nb-NO,nb;q=0.8,no;q=0.6,nn;q=0.4,en-US;q=0.2,en;q=0.2,da;q=0.2,sv;q=0.2");
        uc.setRequestProperty("Cache-Control", "no-cache");
        uc.setRequestProperty("Connection", "keep-alive");
        uc.setRequestProperty("Cookie", "i00=0000558f386af8380000; NRK_PLAYER_SETTINGS_TV=devicetype=desktop&preferred-player-odm=hlslink&preferred-player-live=hlslink&max-data-rate=3500");
        uc.setRequestProperty("Host", "psapi-ne.nrk.no");
        uc.setRequestProperty("Pragma", "no-cache");
        uc.setRequestProperty("Upgrade-Insecure-Requests", "1");
        uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
        uc.setUseCaches(false);
        uc.setDoInput(true);
        uc.setDoOutput(true);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8)))
        {
            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                joiner.add(inputLine);
            }
            in.close();
        }

        return joiner.toString();
    }
}