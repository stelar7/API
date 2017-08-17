package div;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.Files;
import java.nio.file.*;
import java.text.*;
import java.time.*;

public final class NRKSubDownloader
{
    
    private static final String DIV = "<DIV>";
    
    private NRKSubDownloader()
    {
    
    }
    
    private static String fixTime(final LocalTime time)
    {
        final StringBuilder sb     = new StringBuilder();
        final DecimalFormat format = new DecimalFormat("#00");
        sb.append(format.format(time.getHour())).append(":");
        sb.append(format.format(time.getMinute())).append(":");
        sb.append(format.format(time.getSecond())).append(",");
    
        int thousandNanos = 1_000;
        int tenNanos      = 10;
        int nano          = time.getNano();
    
        while (nano > thousandNanos)
        {
            nano /= tenNanos;
        }
        sb.append(format.format(nano));
        return sb.toString();
    }
    
    public static void get2(final String[] data) throws Exception
    {
        String url = data[1];
        
        System.out.println("starting " + url);
        String lines = Internet.getPageSource(url);
    
        if (!lines.contains(DIV))
        {
            System.out.println(lines);
            System.out.println("No subs?");
            return;
        }
        lines = lines.substring(lines.indexOf(DIV) + (DIV.length() + 2), lines.indexOf(DIV));
        final StringBuilder sb          = new StringBuilder();
        final String[]      text        = lines.split("\n");
        String              beginString = "begin=";
        int                 day         = 24;
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
                final String  begin = s.substring(s.indexOf(beginString) + 7, s.indexOf('"', s.indexOf(beginString) + 8));
                final Integer bh    = Integer.parseInt(begin.split(":")[0]);
                final Integer bm    = Integer.parseInt(begin.split(":")[1]);
                final Integer bs    = Integer.parseInt(begin.split(":")[2].split("\\.")[0]);
                final Integer bms   = Integer.parseInt(begin.split(":")[2].split("\\.")[1]);
                if (bh > day)
                {
                    continue;
                }
                final LocalTime begining = LocalTime.of(bh, bm, bs, bms);
    
                final String    durs   = s.substring(s.indexOf("dur=") + 5, s.indexOf('"', s.indexOf("dur=") + 6));
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
        String     name = data[0];
        final File sub  = new File("output/" + name + ".srt");
        if (sub.getParentFile().mkdirs() || sub.getParentFile().exists())
        {
            Files.write(sub.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } else
        {
            System.out.println("Failed to write file");
        }
        System.out.println("done " + url);
    }
    
}
