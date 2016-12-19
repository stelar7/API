package div;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.List;
import java.util.jar.*;

public class Files
{
	
	/**
	 * Deletes Folder with all of its content
	 *
	 * @param folder path to folder which should be deleted
	 */
	public static void deleteFolderAndItsContent(final Path folder) throws IOException
	{
		java.nio.file.Files.walkFileTree(folder, new SimpleFileVisitor<Path>()
		{
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
			{
				java.nio.file.Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
			{
				if (exc != null)
				{
					throw exc;
				}
				// Somehow  the visitFile doesnt find all the files...?
				java.nio.file.Files.list(dir).forEach(f ->
				                                      {
					                                      try
					                                      {
						                                      java.nio.file.Files.delete(f);
					                                      } catch (IOException e)
					                                      {
						                                      e.printStackTrace();
					                                      }
				                                      });
				java.nio.file.Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	/**
	 * Deletes Folder with all of its content
	 *
	 * @param file path to folder which should be deleted
	 */
	public static void deleteDir(File file)
	{
		File[] contents = file.listFiles();
		if (contents != null)
		{
			for (File f : contents)
			{
				deleteDir(f);
			}
		}
		file.delete();
	}
	
	/**
	 * Browse to a file on the system
	 *
	 * @param file the file to browse to
	 * @return true if action is supported by OS
	 * @throws IOException if the file is null?
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
	 * @param jarFile the file to extract from
	 * @param destDir the dir to save files to
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
					if (f.getParentFile().mkdirs())
					{
						try (FileOutputStream f2 = new FileOutputStream(f))
						{
							final byte[] b = new byte[1024];
							int          bytes;
							while ((bytes = inputStream.read(b)) > 0)
							{
								f2.write(b, 0, bytes);
							}
						}
						inputStream.close();
					}
				} catch (final IOException e)
				{
					e.printStackTrace();
				}
			}
			jar.close();
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets all classes in the given package
	 *
	 * @param jarfile     the .jar-file
	 * @param packageName the package
	 * @return {@code List<Class<?>>} list of all the classes in that package
	 */
	
	public static List<Class<?>> getClassNamesInPackage(final File jarfile, String packageName)
	{
		final List<Class<?>> arrayList = new ArrayList<>();
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
			} catch (final IOException e)
			{
				e.printStackTrace();
			}
		}
		return arrayList;
	}
	
	/**
	 * Gets the location of a Jar
	 *
	 * @param c a class in the Jar to get the location from
	 * @return the location of the Jar
	 **/
	public static String getJarLocation(final Class<?> c) throws URISyntaxException, UnsupportedEncodingException
	{
		final String temp = c.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		return URLDecoder.decode(temp, "UTF-8");
	}
	
	public static boolean isRunning(final String string)
	{
		boolean found = false;
		try
		{
			final Process            proc    = Runtime.getRuntime().exec("wmic.exe");
			final BufferedReader     input   = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			final OutputStreamWriter oStream = new OutputStreamWriter(proc.getOutputStream());
			oStream.write(String.format("process where name='%s'", string));
			oStream.flush();
			oStream.close();
			while (input.readLine() != null)
			{
				if (found)
				{
					// return if more than 2 lines
					return true;
				}
				found = true;
			}
			input.close();
		} catch (final IOException ioe)
		{
			ioe.printStackTrace();
		}
		return false;
	}
	
	public static void listFilesInFolderAndSubFolders(final File folder)
	{
		final Path origin = folder.toPath();
		final FileVisitor<Path> visitor = new SimpleFileVisitor<Path>()
		{
			@Override
			public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes exc)
			{
				if (dir.getFileName() != null)
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
	 * @param file the path to load from
	 * @return the object that has been loaded
	 **/
	public static Object load(final File file)
	{
		Object result;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
		{
			result = ois.readObject();
			ois.close();
			return result;
		} catch (final IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Searches a MappedByteBuffer for a string
	 *
	 * @param buffer the buffer to seach
	 * @param search the string to seach
	 * @return if the string was found
	 */
	public static boolean lookFor(final MappedByteBuffer buffer, final String search)
	{
		final byte[] lookup     = search.getBytes(Charset.defaultCharset());
		final int    datalength = lookup.length;
		buffer.rewind();
		while (buffer.hasRemaining())
		{
			final byte b = buffer.get();
			if (b == lookup[0])
			{
				final byte[] data = new byte[datalength];
				buffer.position(buffer.position() - 1);
				buffer.get(data, 0, datalength);
				final String found = new String(data, StandardCharsets.UTF_8);
				if (!found.equals(search))
				{
					continue;
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Reads from the given file
	 *
	 * @param file the file to read from
	 * @return the read string
	 **/
	public static String read(final File file) throws IOException
	{
		final StringJoiner joiner = new StringJoiner("\n");
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)))
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
	
	/**
	 * Saves a object to the specified path
	 *
	 * @param obj  the object to save
	 * @param file the path to save to
	 **/
	public static void save(final Object obj, final File file)
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file)))
		{
			oos.writeObject(obj);
			oos.flush();
			oos.close();
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes to the given file
	 *
	 * @param file   the file to write to
	 * @param string the string to write
	 * @param append whether to append or rewrite the file
	 **/
	public static void write(final File file, final String string, final boolean append)
	{
		try (final OutputStreamWriter br = new OutputStreamWriter(new FileOutputStream(file, append), StandardCharsets.UTF_8))
		{
			br.write(string);
			br.flush();
			br.close();
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
	}
}
