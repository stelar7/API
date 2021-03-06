package div;

import com.google.gson.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public final class NRKFileDownloader
{
    
    private NRKFileDownloader()
    {
        
    }
    
    public static String[] get2(final String id) throws Exception
    {
        String source = getPageSource("https://tv.nrk.no/serie/a/" + id);
        
        String searchFor  = "\"episodenumber\" content=\"";
        String searchFor2 = "\"seriestitle\" content=\"";
        String searchFor3 = "\"seasonid\" content=\"";
        
        String epno = source.substring(source.indexOf(searchFor) + searchFor.length());
        epno = epno.substring(0, epno.indexOf('"'));
        
        String title = source.substring(source.indexOf(searchFor2) + searchFor2.length());
        title = title.substring(0, title.indexOf('"'));
        
        String season = source.substring(source.indexOf(searchFor3) + searchFor3.length());
        season = season.substring(0, season.indexOf('"'));
        
        String fullName = season + "/ " + title + " - " + epno;
        
        String apiPath     = "https://psapi-ne.nrk.no/mediaelement/" + id;
        String apiPathData = getPageSource(apiPath);
        
        JsonParser parser       = new JsonParser();
        JsonObject obj          = parser.parse(apiPathData).getAsJsonObject();
        String     subtitlesUrl = obj.get("subtitlesUrlPath").getAsString();
        
        String mediaUrl = obj.get("mediaUrl").getAsString();
        String start    = mediaUrl.substring(0, mediaUrl.lastIndexOf('/') + 1);
        
        
        final List<String> data = Arrays.asList(getPageSource(mediaUrl).split("\n"));
        
        int    max = 0;
        String url = "";
        
        String bandwidth = "BANDWIDTH";
        
        for (int i = 0; i < data.size(); i++)
        {
            String line = data.get(i);
            
            if (line.contains(bandwidth))
            {
                String band = line.substring(line.indexOf(bandwidth + "=") + (bandwidth.length() + 1));
                int    bw   = Integer.parseInt(band.substring(0, band.indexOf(',')));
                
                if (bw > max)
                {
                    max = bw;
                    url = data.get(i + 1);
                }
            }
        }
        
        String partsPath = start + url;
        
        List<String> parts = Arrays.asList(getPageSource(partsPath).split("\n"));
        parts = parts.stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());
        System.out.println("downloading parts...");
        parts.forEach(s ->
                      {
                          try
                          {
                              String name = s.substring(0, s.indexOf('?'));
                
                              File file = new File(id, name);
                
                              if (file.getParentFile().mkdirs() || file.getParentFile().exists())
                              {
                                  Internet.download(start + s, file);
                              } else
                              {
                                  System.out.println("Failed to download file");
                              }
                
                
                          } catch (Exception e)
                          {
                              e.printStackTrace();
                          }
                      });
        
        File       outFile = new File(id, id + ".ts");
        List<File> files   = Arrays.asList(outFile.getParentFile().listFiles());
        
        System.out.println("merging parts...");
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outFile, true))))
        {
            files.stream().sorted(new NaturalOrderComparator()).forEach(file ->
                                                                        {
                                                                            try
                                                                            {
                                                                                DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
                                                                                int             inb;
                                                                                while ((inb = in.read()) != -1)
                                                                                {
                                                                                    out.write(inb);
                                                                                }
                                                                                in.close();
                                                                            } catch (IOException e)
                                                                            {
                                                                                e.printStackTrace();
                                                                            }
                                                                        });
            out.flush();
            out.close();
        }
        
        File seasonFile = new File("output", season);
        if (seasonFile.mkdirs() || seasonFile.exists())
        {
            ProcessBuilder p = new ProcessBuilder("ffmpeg", "-i", id + "/" + id + ".ts", "-c", "copy", "output/" + fullName + ".mkv");
            p.inheritIO();
            p.redirectErrorStream(true);
            Process p2 = p.start();
            p2.waitFor();
        } else
        {
            System.out.println("File creating failed");
        }
        
        Files.deleteFolderAndItsContent(Paths.get(id));
        
        return new String[]{fullName, subtitlesUrl};
    }
    
    public static String getPageSource(final String inUrl) throws Exception
    {
        
        final StringJoiner  joiner = new StringJoiner("\n");
        final URL           outUrl = new URL(inUrl);
        final URLConnection uc     = outUrl.openConnection();
        uc.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        uc.setRequestProperty("Accept-Language", "nb-NO,nb;q=0.8,no;q=0.6,nn;q=0.4,en-US;q=0.2,en;q=0.2,da;q=0.2,sv;q=0.2");
        uc.setRequestProperty("Cache-Control", "no-cache");
        uc.setRequestProperty("Connection", "keep-alive");
        String cookie = "i00=0000558f386af8380000; NRK_PLAYER_SETTINGS_TV=devicetype=desktop&preferred-player-odm=hlslink&preferred-player-live=hlslink&max-data-rate=3500";
        uc.setRequestProperty("Cookie", cookie);
        uc.setRequestProperty("Host", "tv.nrk.no");
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