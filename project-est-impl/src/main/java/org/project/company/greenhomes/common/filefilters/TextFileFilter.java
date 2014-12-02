package org.project.company.greenhomes.common.filefilters;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Makes sure only files ending with .txt are read, ignoring those with <code>recommendation</code> in them.
 *
 *
 */

public class TextFileFilter implements FilenameFilter {

	private final String extension = ".txt";
	/*
	 * this is the part of the file name to ignore as there will also be data files in there
	 * which we don't want to read.
	 */
	private final String ignoreText = "recommendation";

	public boolean accept (File dir, String name) {
		return name.endsWith(extension) && name.toLowerCase().indexOf(ignoreText) == -1;
	}

}
