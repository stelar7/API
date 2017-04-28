package div;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.time.LocalTime;

public final class NRKSubDownloader
{
    private NRKSubDownloader()
    {
    }
    
    private static File download(final String url, final File output) throws IOException
    {
        final byte[]        buffer = new byte[1024];
        int                 read   = -1;
        final URLConnection uc     = new URL(url).openConnection();
        uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        uc.setRequestProperty("Content-Language", "en-US");
        uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
        uc.setUseCaches(false);
        uc.setDoInput(true);
        uc.setDoOutput(true);
        try (InputStream in = uc.getInputStream(); OutputStream out = new FileOutputStream(output))
        {
            while ((read = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, read);
            }
            out.flush();
        } catch (final FileNotFoundException e)
        {
            return null;
        }
        return output;
    }
    
    private static String fixTime(final LocalTime time)
    {
        final StringBuilder sb     = new StringBuilder();
        final DecimalFormat format = new DecimalFormat("#00");
        sb.append(format.format(time.getHour())).append(":");
        sb.append(format.format(time.getMinute())).append(":");
        sb.append(format.format(time.getSecond())).append(",");
        int nano = time.getNano();
        while (nano > 1000)
        {
            nano /= 10;
        }
        sb.append(format.format(nano));
        return sb.toString();
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
            System.out.println("starting " + id);
            final File temp = new File(id + ".xml");
            File       xml  = NRKSubDownloader.download("https://tv.nrk.no/programsubtitles/" + id, temp);
            if (xml != null)
            {
                String lines = new String(Files.readAllBytes(xml.toPath()), StandardCharsets.UTF_8);
                if (lines.isEmpty())
                {
                    xml = NRKSubDownloader.download("https://tv.nrk.no/programsubtitles/" + id + "AA", temp);
                    if (xml != null)
                    {
                        lines = new String(Files.readAllBytes(xml.toPath()), StandardCharsets.UTF_8);
                    }
                }
                lines = lines.substring(lines.indexOf("<div>") + 7, lines.indexOf("</div>"));
                final StringBuilder sb   = new StringBuilder();
                final String[]      text = lines.split("\n");
                for (int i = 0; i < text.length; i++)
                {
                    final String s = text[i].trim();
                    try
                    {
                        if (s.isEmpty())
                        {
                            continue;
                        }
                        if (!(s.contains("</p>") && s.contains("<p begin")))
                        {
                            continue;
                        }
                        final String  begin = s.substring(s.indexOf("begin=") + 7, s.indexOf('\"', s.indexOf("begin=") + 8));
                        final Integer bh    = Integer.parseInt(begin.split(":")[0]);
                        final Integer bm    = Integer.parseInt(begin.split(":")[1]);
                        final Integer bs    = Integer.parseInt(begin.split(":")[2].split("\\.")[0]);
                        final Integer bms   = Integer.parseInt(begin.split(":")[2].split("\\.")[1]);
                        if (bh > 24)
                        {
                            continue;
                        }
                        final LocalTime begining = LocalTime.of(bh, bm, bs, bms);
                        
                        final String    durs   = s.substring(s.indexOf("dur=") + 5, s.indexOf('\"', s.indexOf("dur=") + 6));
                        final Integer   eh     = Integer.parseInt(durs.split(":")[0]);
                        final Integer   em     = Integer.parseInt(durs.split(":")[1]);
                        final Integer   es     = Integer.parseInt(durs.split(":")[2].split("\\.")[0]);
                        final Integer   ems    = Integer.parseInt(durs.split(":")[2].split("\\.")[1]);
                        final LocalTime ending = begining.plusHours(eh).plusMinutes(em).plusSeconds(es).plusNanos(ems);
                        
                        final String subdata = s.substring(s.indexOf("\">") + 2, s.indexOf("</p>")).replace("<br />", "\n");
                        
                        sb.append(i + 1).append("\n");
                        sb.append(NRKSubDownloader.fixTime(begining)).append(" --> ").append(NRKSubDownloader.fixTime(ending)).append("\n");
                        sb.append(subdata).append("\n\n");
                    } catch (final StringIndexOutOfBoundsException e)
                    {
                        System.out.println(s);
                        e.printStackTrace();
                    }
                }
            }
            final File sub = new File(id + ".srt");
            Files.write(sub.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            temp.delete();
        }
    }
}
