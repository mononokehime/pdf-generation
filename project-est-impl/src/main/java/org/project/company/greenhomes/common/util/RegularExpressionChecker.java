package org.project.company.greenhomes.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that checks regular expressions for us
 *
 *
 */
public class RegularExpressionChecker {

	/*
	 * Match for st with spaces at end only in case in the beginning of a sentence
	 */
	private static final String ST_NO_LEADING_SPACE_SYNTAX = "^st\\s+[a-z\\s\'-.]*$";
	/*
	 * Match for st with spaces at both ends in case in the middle of a sentence
	 */
	private static final String ST_SPACE_SYNTAX = "^[a-z\'-.]*+\\s+st\\s+[a-z\\s\'-.]*$";

	private static final String RRN_SYNTAX = "[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}";

	/**
	 * Checks the string is in the valid rrn format. Null is ok
	 *
	 * @param str
	 * @return
	 */
	public static Boolean isValidRRN (String str) {
		if (null == str) {
			str = "";
		}
		// more common that st at the beginning
		Pattern pattern = Pattern.compile(RRN_SYNTAX, Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(str);
		if (match.matches()) {
			return true;
		}
		return false;
	}

	public static String replaceStValues (String str) {
		String thePattern = "^st\\s";
		Pattern pattern = Pattern.compile(thePattern, Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(str);
		String val = matcher.replaceAll("St. ");
		// now replace later ones
		thePattern = "\\sst\\s";
		pattern = Pattern.compile(thePattern, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(val);
		//matcher.replaceAll(" St. ");
		return matcher.replaceAll(" St. ");
	}

	/**
	 * Checks to see if a string matches an abbreviation for saint i.e. st. It checks first to
	 * see if at the beginning of the line, in which case there is no leading space, and then if it was in the
	 * middle of the block
	 *
	 * @param str
	 * @return
	 */
	public static Boolean isStMatch (String str) {

		// more common that st at the beginning
		Pattern pattern = Pattern.compile(ST_NO_LEADING_SPACE_SYNTAX, Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(str);
		if (match.matches()) {
			return true;
		}
		// now do as if middle of sentence
		pattern = Pattern.compile(ST_SPACE_SYNTAX, Pattern.CASE_INSENSITIVE);
		match = pattern.matcher(str);
		if (match.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks to see if a string is a digit, i.e. a number
	 *
	 * @param str
	 * @return
	 */
	public static Boolean isDigit (String str) {
		char[] characters = str.toCharArray();
		for (int i = 0; i < characters.length; i++) {
			if (!Character.isDigit(characters[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks to see if a string is a digit, i.e. a number
	 *
	 * @param str
	 * @return
	 */
	public static int numberOfNonDigits (String str) {
		int numberOfNonDigits = 0;
		char[] characters = str.toCharArray();
		for (int i = 0; i < characters.length; i++) {
			if (!Character.isDigit(characters[i])) {
				numberOfNonDigits++;
			}
		}
		return numberOfNonDigits;
	}

	public static Boolean containsInvertedComma (String str) {
		return str.indexOf("'") != -1;
	}

}
