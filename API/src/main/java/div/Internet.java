package div;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;

public final class Internet
{
    private static final String USER_AGENT       = "User-Agent";
    private static final String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11";
    
    private Internet()
    {
        
    }
    
    /**
     * Downloads a file from the internet
     *
     * @param url    the url to download from
     * @param output the path to save the file to
     * @throws IOException if the reading failes at some point
     **/
    public static void download(final String url, final File output) throws IOException
    {
        final byte[]        buffer = new byte[1024];
        int                 read;
        final URLConnection uc     = new URL(url).openConnection();
        uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        uc.setRequestProperty("Content-Language", "en-US");
        uc.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
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
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the source in the form of a string
     *
     * @param inUrl the page to get the source from
     * @return String the source
     * @throws IOException if the reading failes at some point
     */
    public static String getPageSource(final String inUrl) throws IOException
    {
        final StringJoiner  joiner = new StringJoiner("\n");
        final URL           outUrl = new URL(inUrl);
        final URLConnection uc     = outUrl.openConnection();
        uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        uc.setRequestProperty("Content-Language", "en-US");
        uc.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
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
    
    public static void sendPost(String url, String data) throws Exception
    {
        URL                obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        
        con.setRequestMethod("POST");
        con.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(data);
        wr.flush();
        wr.close();
    
        BufferedReader in       = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String         inputLine;
        StringBuilder  response = new StringBuilder();
        
        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();
        
        System.out.println(con.getResponseCode());
        System.out.println(response.toString());
        
    }
}
