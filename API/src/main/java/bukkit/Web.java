package bukkit;

import div.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;

public class Web extends Internet
{
    
    /**
     * Downloads the specified skin from the servers
     *
     * @param player the players skin to download
     * @param output the output folder
     * @return the downloaded skin
     * @throws IOException if the download fails for any reason
     */
    public static File downloadSkin(final String player, final File output) throws IOException
    {
        final String url      = "http://s3.amazonaws.com/MinecraftSkins/" + player + ".png";
        final File   returnme = new File(output.getAbsolutePath(), player + ".png");
        Internet.download(url, returnme);
        return returnme;
    }
    
    /**
     * Checks if a player is currently on a Premium account
     *
     * @param player the player to check
     * @return true if the player is on a Premium account
     */
    public static boolean isPremium(final String player)
    {
        final String url      = "http://www.minecraft.net/haspaid.jsp?user=";
        final String checkurl = url + player;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL(checkurl).openStream(), StandardCharsets.UTF_8)))
        {
            return Boolean.parseBoolean(br.readLine());
        } catch (final IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
