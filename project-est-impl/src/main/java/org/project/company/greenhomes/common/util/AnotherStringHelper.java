package org.project.company.greenhomes.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * Another of the ubiquitous string helper classes
 *
 *
 */
public class AnotherStringHelper {

	/**
	 * Many of the land registry entries have extraneous blank spaces in so this method strips them out.
	 *
	 * @param str
	 * @return
	 */
	public static String rebuildStringWithNoBigSpaces (String str) {
		if (null == str || str.length() < 1) {
			str = "";
		}
		StringBuilder sb = new StringBuilder();
		String[] strSplit = StringUtils.split(str, " ");
		for (int i = 0; i < strSplit.length; i++) {
			// for the first one no space before is possible
			if (i == 0) {
				sb.append(strSplit[i]);
			} else {
				// add the space that is between the addresses
				sb.append(" " + strSplit[i]);
			}
		}
		return sb.toString();
	}

}
