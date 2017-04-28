package div;

import java.io.File;
import java.util.List;

public final class NRKFileDownloader
{
    
    private NRKFileDownloader()
    {
    }
    
    /**
     * http://tv.nrk.no/serie/side-om-side/MUHH47000214/sesong-2/episode-2 id = MUHH47000214
     *
     * @param ids
     * @throws Exception
     */
    public static void get(final String... ids) throws Exception
    {
        for (final String id : ids)
        {
            String s = Internet.getPageSource("https://tv.nrk.no/serie/a/" + id);
            s = s.substring(s.indexOf("data-hls-media") + 16);
            s = s.substring(0, s.indexOf('\"'));
            final File temp = File.createTempFile("master", "m3u8");
            Internet.download(s, temp);
            final List<String> data = java.nio.file.Files.readAllLines(temp.toPath());
            for (final String line : data)
            {
                if (line.contains("index_4"))
                {
                    final ProcessBuilder p = new ProcessBuilder("ffmpeg", "-i", line, "-c", "copy", id + ".mkv");
                    p.inheritIO();
                    p.redirectErrorStream(true);
                    final Process p2 = p.start();
                    p2.waitFor();
                }
            }
            temp.delete();
        }
    }
}