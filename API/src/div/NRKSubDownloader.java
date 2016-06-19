package div;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.Files;
import java.text.*;
import java.time.*;

public class NRKSubDownloader
{

    private static File download(final String url, final File output) throws IOException
    {
        final byte buffer[] = new byte[1024];
        int read = -1;
        final URLConnection uc = new URL(url).openConnection();
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
        final StringBuilder sb = new StringBuilder();
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

    public static void get2(final String data[]) throws Exception
    {
        String name = data[0];
        String url = data[1];

        System.out.println("starting " + url);

        String lines = Internet.getPageSource(url);

        if (!lines.contains("<div>"))
        {
            System.out.println(lines);
            System.out.println("No subs?");
            return;
        }
        lines = lines.substring(lines.indexOf("<div>") + 7, lines.indexOf("</div>"));
        final StringBuilder sb = new StringBuilder();
        final String[] text = lines.split("\n");
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
                final String begin = s.substring(s.indexOf("begin=") + 7, s.indexOf("\"", s.indexOf("begin=") + 8));
                final Integer bh = Integer.parseInt(begin.split(":")[0]);
                final Integer bm = Integer.parseInt(begin.split(":")[1]);
                final Integer bs = Integer.parseInt(begin.split(":")[2].split("\\.")[0]);
                final Integer bms = Integer.parseInt(begin.split(":")[2].split("\\.")[1]);
                if (bh > 24)
                {
                    continue;
                }
                final LocalTime begining = LocalTime.of(bh, bm, bs, bms);

                final String durs = s.substring(s.indexOf("dur=") + 5, s.indexOf("\"", s.indexOf("dur=") + 6));
                final Integer eh = Integer.parseInt(durs.split(":")[0]);
                final Integer em = Integer.parseInt(durs.split(":")[1]);
                final Integer es = Integer.parseInt(durs.split(":")[2].split("\\.")[0]);
                final Integer ems = Integer.parseInt(durs.split(":")[2].split("\\.")[1]);
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
        final File sub = new File("output/" + name + ".srt");
        sub.getParentFile().mkdirs();
        Files.write(sub.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Deprecated
    public static void get(String[] fileDownloadOutput) throws Exception
    {

        String title = fileDownloadOutput[0];
        String id = fileDownloadOutput[1];

        System.out.println("starting " + title + "(" + id + ")");
        final File temp = new File("output/" + id + ".xml");
        temp.deleteOnExit();
        File xml = NRKSubDownloader.download("https://tv.nrk.no/programsubtitles/" + id, temp);
        String lines = new String(Files.readAllBytes(xml.toPath()), StandardCharsets.UTF_8);
        if (lines.isEmpty())
        {
            xml = NRKSubDownloader.download("https://tv.nrk.no/programsubtitles/" + id + "AA", temp);
            lines = new String(Files.readAllBytes(xml.toPath()), StandardCharsets.UTF_8);
        }
        if (lines.isEmpty())
        {
            xml = NRKSubDownloader.download("https://tv.nrk.no/programsubtitles/" + id + "BW", temp);
            lines = new String(Files.readAllBytes(xml.toPath()), StandardCharsets.UTF_8);
        }
        if (!lines.contains("<div>"))
        {
            System.out.println(lines);
            System.out.println("No subs?");
            return;
        }
        lines = lines.substring(lines.indexOf("<div>") + 7, lines.indexOf("</div>"));
        final StringBuilder sb = new StringBuilder();
        final String[] text = lines.split("\n");
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
                final String begin = s.substring(s.indexOf("begin=") + 7, s.indexOf("\"", s.indexOf("begin=") + 8));
                final Integer bh = Integer.parseInt(begin.split(":")[0]);
                final Integer bm = Integer.parseInt(begin.split(":")[1]);
                final Integer bs = Integer.parseInt(begin.split(":")[2].split("\\.")[0]);
                final Integer bms = Integer.parseInt(begin.split(":")[2].split("\\.")[1]);
                if (bh > 24)
                {
                    continue;
                }
                final LocalTime begining = LocalTime.of(bh, bm, bs, bms);

                final String durs = s.substring(s.indexOf("dur=") + 5, s.indexOf("\"", s.indexOf("dur=") + 6));
                final Integer eh = Integer.parseInt(durs.split(":")[0]);
                final Integer em = Integer.parseInt(durs.split(":")[1]);
                final Integer es = Integer.parseInt(durs.split(":")[2].split("\\.")[0]);
                final Integer ems = Integer.parseInt(durs.split(":")[2].split("\\.")[1]);
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
        final File sub = new File("output/" + title + ".srt");
        Files.write(sub.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

}
