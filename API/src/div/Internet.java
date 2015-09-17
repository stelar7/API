package div;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

public class Internet
{

    /**
     * Downloads a file from the internet
     *
     * @param url
     *            the url to download from
     * @param output
     *            the path to save the file to
     *
     * @return true if writing was successful.
     * @throws IOException
     **/
    public static boolean download(final String url, final File output) throws IOException
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
            return false;
        }
        return true;
    }

    /**
     * Returns the source in the form of a string
     *
     * @param URL
     *            the page to get the source from
     * @return String the source
     * @throws IOException
     */
    public static String getPageSource(final String URL) throws IOException
    {
        String returnString = "";
        final URL url = new URL(URL);
        int x = -1;
        final Reader r = new InputStreamReader(url.openConnection().getInputStream());
        while ((x = r.read()) > 0)
        {
            returnString += (char) x;
        }
        return returnString;
    }
}
