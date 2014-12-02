package org.project.company.greenhomes.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.Date;

/**
 * Class that helps with some common file renaming operations
 *
 *
 */
public class FileIOExtras {
	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(FileIOExtras.class);

	/**
	 * Checks if a file exists, can be read/write from the file system.
	 *
	 * @param file
	 * @return
	 */
	public static boolean isAccessible (File file) {
		if (null == file) {
			throw new InvalidParameterException("File cannot be null");
		}
		return file.exists() && file.canRead() && file.canWrite();
	}

	/**
	 * Builds a new file name for uploaded files with a date stamp
	 *
	 * @param file
	 * @param directory
	 * @return
	 */
	public static File buildNewFileName (File file, String directory) {
		if (null == file || null == directory) {
			throw new InvalidParameterException(
					"File is:" + file + ", and directory is:" + directory + ", both must not be be null!");
		}
		// move!
		String newFileName = null;
		try {
			newFileName = DateFormatter.getFormattedStringForFileRename(new Date()) + file.getName();

		} catch (ParseException e) {
			// highly unlikely but if it does use a default
			log.error("Unable to create a new file name!");
			newFileName = System.currentTimeMillis() + file.getName();
		}
		return new File(directory, newFileName);
	}

	/**
	 * Copies file from a to b
	 *
	 * @param in  original file
	 * @param out destination file
	 * @throws IOException
	 */
	public static void copyFile (File in, File out)
			throws IOException {
		FileChannel inChannel = new
				FileInputStream(in).getChannel();
		FileChannel outChannel = new
				FileOutputStream(out).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(),
					outChannel);
		} catch (IOException e) {
			throw e;
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	}

	/**
	 * Writes the uploaded land registry file to the specified location on the files system.
	 *
	 * @param in       file holder
	 * @param name     name for the file
	 * @param location location of the file
	 * @return
	 */
	public static Boolean writeLandRegistryFileToSystem (MultipartFile in, String name, String location) {
		if (null == in || null == name || null == location) {
			throw new InvalidParameterException(
					"File is:" + in + ", and location is:" + location + ", and name is " + name
							+ ", all must not be be null!");
		}
		// get the file directory
		File folderToRead = new File(location);
		if (FileIOExtras.isAccessible(folderToRead)) {
			File f = new File(location, name);
			try {
				in.transferTo(f);
			} catch (IllegalStateException e) {
				log.error("eek, can't write to file system", e);
				return false;
			} catch (IOException e) {
				log.error("eek, can't write to file system", e);
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

}
