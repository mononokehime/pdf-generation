package org.project.company.greenhomes.common.filefilters;

import java.io.File;
import java.io.FilenameFilter;

/**
 * The original land registry data came in excel format. Best not to delete just in case, just don't use it!
 *
 *
 */
@Deprecated
public class ExcelFileFilter implements FilenameFilter {

	private final String extension = ".xls";

	public boolean accept (File dir, String name) {

		return name.endsWith(extension);
	}

}
