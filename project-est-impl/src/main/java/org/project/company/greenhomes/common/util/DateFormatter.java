package org.project.company.greenhomes.common.util;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Class for various date operations
 *
 *
 */
public class DateFormatter {

	public static Date getFormattedDateFromLandReg (String str) throws ParseException {
		if (null == str || str.length() < 1) {
			throw new InvalidParameterException("Date is null.");
		}
		// we need to ensure the time is the same
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setLenient(true);
		return getTimeForPropertyInsert(format.parse(str));
	}

	public static Date getFormattedDateFromLandMark (String str) throws ParseException {
		if (null == str || str.length() < 1) {
			throw new InvalidParameterException("Date is null.");
		}
		// we need to ensure the time is the same
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy HH.mm");
		format.setLenient(true);
		return getTimeForPropertyInsert(format.parse(str));
	}

	/**
	 * The date format used by landmark. Set to lenient <code>dd-MMM-yy HH.mm</code>
	 *
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String getFormattedDateForLandMarkData (Date date) throws ParseException {

		// we need to ensure the time is the same
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy HH.mm");
		format.setLenient(true);

		return format.format(date);
	}

	private static Date getTimeForPropertyInsert (Date date) {
		if (null == date) {
			throw new InvalidParameterException("Date is null.");
		}
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		return cal.getTime();
	}

	public static Date getFormattedDate (String str) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		format.setLenient(false);
		return format.parse(str);
	}
	//	/**
	//	 * Dates for scheduling to ensure the finder works
	//	 * @param str
	//	 * @return
	//	 * @throws ParseException
	//	 */
	//	public static Date getFormattedDateForSchedule(String str) throws ParseException
	//	{
	//		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	//		return format.parse(str);
	//	}

	/**
	 * Date format used when renaming the files that have been processed
	 *
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String getFormattedStringForFileRename (Date date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-kk-mm");
		return format.format(date);
	}

	/**
	 * Format of the date printed in the pdf
	 *
	 * @param date
	 * @return
	 */
	public static String getFormattedStringForPDF (Date date) {
		SimpleDateFormat format = new SimpleDateFormat("MMMMM dd yyyy");
		return format.format(date);
	}

	/**
	 * Date that can be used for searching the schedules
	 *
	 * @return
	 */
	public static Date getDateForScheduleSearch () {
		Calendar cal = Calendar.getInstance();
		// roll the cal forward on one day so we get anything for today
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}
}
