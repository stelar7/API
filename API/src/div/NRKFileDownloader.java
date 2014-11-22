package div;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class NRKFileDownloader
{

    /**
     * http://tv.nrk.no/serie/side-om-side/MUHH47000214/sesong-2/episode-2 id = MUHH47000214
     * 
     * @param ids
     * @throws Exception
     */
    public static void get(String... ids) throws Exception
    {
        for (String id : ids)
        {
            String s = getPageSource(id);
            s = s.substring(s.indexOf("data-hls-media") + 16);
            s = s.substring(0, s.indexOf("\""));
            File temp = File.createTempFile("master", "m3u8");
            temp.deleteOnExit();
            Internet.download(s, temp);
            List<String> data = java.nio.file.Files.readAllLines(temp.toPath());
            for (String line : data)
            {
                if (line.contains("index_4"))
                {
                    ProcessBuilder p = new ProcessBuilder("ffmpeg", "-i", line, "-c", "copy", id + ".mkv");
                    p.inheritIO();
                    p.redirectErrorStream(true);
                    Process p2 = p.start();
                    p2.waitFor();
                }
            }
        }
    }

    private static String getPageSource(String id) throws Exception
    {
        String returnString = "";
        int x = -1;
        final URL url = new URL("http://tv.nrk.no/serie/a/" + id);
        URLConnection con = url.openConnection();
        con.connect();
        final Reader r = new InputStreamReader(con.getInputStream());
        while ((x = r.read()) > 0)
        {
            returnString += (char) x;
        }
        return returnString;
    }
}