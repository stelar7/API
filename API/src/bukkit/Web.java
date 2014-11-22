package bukkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import div.Internet;

public class Web extends Internet
{

    /**
     * Downloads the specified skin from the servers
     * 
     * @param player
     *            the players skin to download
     * @param output
     *            the output folder
     * @return the downloaded skin
     * @throws IOException
     */
    public static File downloadSkin(final String player, final File output) throws IOException
    {
        final String url = "http://s3.amazonaws.com/MinecraftSkins/" + player + ".png";
        final File returnme = new File(output.getAbsolutePath(), player + ".png");
        download(url, returnme);
        return returnme;
    }

    /**
     * Checks if a player is currently on a Premium account
     * 
     * @param player
     *            the player to check
     * @return true if the player is on a Premium account
     */
    public static boolean isPremium(final String player)
    {
        try
        {
            final URL url = new URL("http://www.minecraft.net/haspaid.jsp?user=");
            final String checkurl = url + player;
            return Boolean.parseBoolean(new BufferedReader(new InputStreamReader(new URL(checkurl).openStream())).readLine());
        } catch (final Exception e)
        {
            return false;
        }
    }
}
