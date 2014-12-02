package org.project.company.greenhomes.test.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Class which can be used to load properties. Maintains a cache of loaded
 * Properties objects for faster retrieval once a set of properties have been
 * loaded This class is not thread safe, that is, is suitable only for single
 * user mode. Also, property files cannot be dynamically changed during
 * execution
 *
 * @author Phil Zoio
 */
public class PropertyUtils {

	private static Map cache = new HashMap();

	public PropertyUtils () {
		super();
	}

	/**
	 * Gets the property identified by the key, from a property file found in
	 * the specified file in the default package
	 *
	 * @param fileName the file in which the properties are stored
	 * @param key      the property key
	 */
	public static String getProperty (String fileName, String key) {
		String file = getFileFromResource(null, fileName);
		if (file == null) {
			return null;
		}
		Object cachedProps = cache.get(file);
		if (cachedProps != null) {
			Properties props = (Properties)cachedProps;
			return props.getProperty(key);
		} else {
			Properties props = loadProperties(file, true);
			cache.put(file, props);
			return props.getProperty(key);
		}
	}

	/**
	 * Gets the property identified by the key, from a property file found in
	 * the specified file in the specified package
	 *
	 * @param packageName the package location of the property file
	 * @param fileName    the file in which the properties are stored
	 * @param key         the property key
	 */
	public static String getProperty (String packageName, String fileName, String key) {
		String file = getFileFromResource(packageName, fileName);
		if (file == null) {
			return null;
		}
		Object cachedProps = cache.get(file);
		if (cachedProps != null) {
			Properties props = (Properties)cachedProps;
			return props.getProperty(key);
		} else {
			Properties props = loadProperties(file, true);
			cache.put(file, props);
			return props.getProperty(key);
		}
	}

	/**
	 * Loads the properties existing in a package with the file name. For
	 * example, test.properties in the package com.realsolve.foo is loaded using
	 * loadProperties("com.realsolve.foo", "test.properties");
	 */
	public static Properties loadProperties (String packageName, String fileName) {
		String file = getFileFromResource(packageName, fileName);
		if (file == null) {
			return null;
		}
		Properties props = loadProperties(file, true);
		return props;
	}

	/**
	 * Loads properties from file denoted by absolute file path <BR>
	 * If errorIfNoFile is true, then throws exception if no file is present.
	 * Else, returns null. If file does not end with .properties, then throws
	 * exception.
	 */
	public static Properties loadProperties (String filePath, boolean errorIfNoFile) {

		Properties p = null;

		File file = new File(filePath);
		if (!file.exists()) {
			if (errorIfNoFile) {
				throw new IllegalStateException("File does not exist");
			} else {
				return null;
			}
		}

		FileInputStream stream = null;

		try {
			p = new Properties();
			stream = new FileInputStream(filePath);
			p.load(stream);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return p;

	}

	public static String getFileFromResource (String packageName, String fileName) {

		String resource = null;

		if (packageName != null) {
			String fileLocation = replaceSeparators(packageName, '.', '/');
			resource = fileLocation + "/" + fileName;
		} else {
			resource = fileName;
		}
		URL url = PropertyUtils.class.getClassLoader().getResource(resource);

		if (url == null) {
			System.out.println("Unable to find resource for " + resource);
			return null;
		}

		String file = url.getFile();
		return file;
	}

	/**
	 * Replaces separators in currentString with replaceSeparator. eg.
	 * co.uk.realsolve becomes co/uk/realsolve return null if current String is
	 * null
	 */
	public static final String replaceSeparators (String inputString, char currentSeparator, char replacedSeparator) {

		char[] chars = inputString.toCharArray();
		char[] newChars = new char[chars.length];
		System.arraycopy(chars, 0, newChars, 0, chars.length);

		for (int i = 0; i < chars.length; i++) {
			char aChar = chars[i];
			if (aChar == currentSeparator) {
				newChars[i] = replacedSeparator;
			}
		}

		return new String(newChars);

	}

}