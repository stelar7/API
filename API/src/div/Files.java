package div;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.MappedByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class Files
{

    /**
     * Browse to a file on the system
     *
     * @param file
     *            the file to browse to
     * @return true if action is supported by OS
     * @throws IOException
     *
     */
    public static boolean browse(final File file) throws IOException
    {
        if (!Desktop.isDesktopSupported())
        {
            return false;
        } else
        {
            Desktop.getDesktop().browse(file.toURI());
        }
        return true;
    }

    /**
     * Extracts all files from a .jar
     *
     * @param jarFile
     *            the file to extract from
     * @param destDir
     *            the dir to save files to
     * @throws Exception
     */
    public static void extractJar(final File jarFile, final File destDir)
    {
        try (JarFile jar = new JarFile(jarFile))
        {
            final Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements())
            {
                final JarEntry file = entries.nextElement();
                try (InputStream inputStream = jar.getInputStream(file))
                {
                    final File f = new File(destDir + File.separator + file.getName());
                    f.getParentFile().mkdirs();
                    try (FileOutputStream f2 = new FileOutputStream(f))
                    {
                        final byte[] b = new byte[1024];
                        int bytes;
                        while ((bytes = inputStream.read(b)) > 0)
                        {
                            f2.write(b, 0, bytes);
                        }
                    }
                    inputStream.close();
                } catch (final Exception e)
                {
                    e.printStackTrace();
                }
            }
            jar.close();
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Gets all classes in the given package
     *
     * @param jarfile
     *            the .jar-file
     * @param packageName
     *            the package
     * @return List<Class<?>> list of all the classes in that package
     *
     */

    public static List<Class<?>> getClassNamesInPackage(final File jarfile, String packageName)
    {
        final List<Class<?>> arrayList = new ArrayList<Class<?>>();
        packageName = packageName.replaceAll("\\.", "/");
        JarEntry jarEntry;
        while (true)
        {
            try (JarInputStream jarFile = new JarInputStream(new FileInputStream(jarfile)))
            {
                jarEntry = jarFile.getNextJarEntry();
                if (jarEntry == null)
                {
                    break;
                }
                if ((jarEntry.getName().startsWith(packageName)) && (jarEntry.getName().endsWith(".class")))
                {
                    arrayList.add(jarEntry.getClass());
                }
                jarFile.close();
            } catch (final Exception e)
            {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    /**
     * Gets the location of a Jar
     *
     * @param c
     *            a class in the Jar to get the location from
     * @return the location of the Jar
     * @throws URISyntaxException
     * @throws Exception
     *
     **/
    public static String getJarLocation(final Class<?> c) throws URISyntaxException, UnsupportedEncodingException
    {
        final String temp = c.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        return URLDecoder.decode(temp, "UTF-8");
    }

    public static void listFilesInFolderAndSubFolders(final File folder)
    {
        final Path origin = folder.toPath();
        final FileVisitor<Path> visitor = new SimpleFileVisitor<Path>()
        {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes exc)
            {
                if (dir.getFileName() == null)
                {
                    return FileVisitResult.CONTINUE;
                }
                if ((dir.getParent() == null) || dir.getParent().equals(origin))
                {
                    System.out.format("%s%n", dir.getFileName());
                }
                return FileVisitResult.CONTINUE;
            }
        };
        try
        {
            java.nio.file.Files.walkFileTree(origin, visitor);
        } catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Loads a Object from the specified path
     *
     * @param file
     *            the path to load from
     * @return the object that has been loaded
     * @throws IOException
     * @throws ClassNotFoundException
     **/
    public static Object load(final File file)
    {
        Object result = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
        {
            result = ois.readObject();
            ois.close();
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Searches a MappedByteBuffer for a string
     *
     * @param buffer
     *            the buffer to seach
     * @param search
     *            the string to seach
     * @return if the string was found
     */
    public static boolean lookFor(final MappedByteBuffer buffer, final String search)
    {
        final byte[] lookup = search.getBytes(Charset.defaultCharset());
        final int datalength = lookup.length;
        buffer.rewind();
        outer: while (buffer.hasRemaining())
        {
            final byte b = buffer.get();
            if (b == lookup[0])
            {
                final byte[] data = new byte[datalength];
                buffer.position(buffer.position() - 1);
                buffer.get(data, 0, datalength);
                final String found = new String(data);
                if (!found.equals(search))
                {
                    continue outer;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Reads from the given file
     *
     * @param file
     *            the file to read from
     * @return the read string
     * @throws IOException
     *
     **/
    public static String read(final File file)
    {
        String returnString = "";
        try (final BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            int x;
            while ((x = br.read()) != -1)
            {
                returnString = returnString + (char) x;
            }
            br.close();
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
        return returnString;
    }

    /**
     * Renames a file to the specified name
     *
     * @param file
     *            the file to rename
     * @param s
     *            the new filename
     *
     **/
    public static void rename(final File file, final String s)
    {
        file.renameTo(new File(file.getAbsoluteFile().getParent(), s));
    }

    /**
     * Saves a object to the specified path
     *
     * @param obj
     *            the object to save
     * @param file
     *            the path to save to
     * @throws IOException
     **/
    public static void save(final Object obj, final File file) throws IOException
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file)))
        {
            oos.writeObject(obj);
            oos.flush();
            oos.close();
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Writes to the given file
     *
     * @param file
     *            the file to write to
     * @param string
     *            the string to write
     * @param append
     *            whether to append or rewrite the file
     * @throws IOException
     *
     **/
    public static void write(final File file, final String string, final boolean append)
    {
        try (final FileWriter br = new FileWriter(file, append))
        {
            br.write(string);
            br.flush();
            br.close();
        } catch (final Exception e)
        {
            e.printStackTrace();
        }
    }
}
