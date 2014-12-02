package org.project.company.greenhomes.common.filefilters;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Makes sure only files containing measures are returned. We look for the text <code>recommendations</code>
 *
 *
 */

public class LandMarkMeasuresFileFilter implements FilenameFilter {

	private final String extension = "recommendations";

	public boolean accept (File dir, String name) {
		if (null == name) {
			return false;
		}
		return name.toLowerCase().indexOf(extension) != -1;
	}

}
