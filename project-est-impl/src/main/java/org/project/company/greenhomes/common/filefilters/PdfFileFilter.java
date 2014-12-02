package org.project.company.greenhomes.common.filefilters;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Makes sure only files ending with .pdf are read.
 *
 *
 */

public class PdfFileFilter implements FilenameFilter {

	public static final String extension = ".pdf";

	public boolean accept (File dir, String name) {
		return name.endsWith(extension);
	}

}
