package org.project.company.greenhomes.common.util;

import org.project.company.greenhomes.common.util.EnergyRatings.EnergyRating;

import java.security.InvalidParameterException;

public class MathsHelper {

	public static Float getPercentFromStrings (String val1, String val2) {
		if (null == val1 || val1.length() < 1 || null == val2 || val2.length() < 1) {
			String message = "Invalid number: val1 is" + val1 + ", val2:" + val2;
			throw new InvalidParameterException(message);
		}
		try {
			Float pot = new Float(val1);
			Float cur = new Float(val2);
			return pot / cur;
		} catch (NumberFormatException e) {
			String message = "Number format exception: val1 is" + val1 + ", val2:" + val2;
			throw new InvalidParameterException(message);
		}
	}

	public static Float getPercentValue (Float val1, Float val2) {
		if (null == val1 || null == val2) {
			String message = "Invalid number: val1 is" + val1 + ", val2:" + val2;
			throw new InvalidParameterException(message);
		}
		return val1 * val2;
	}

	public static Integer getLowestRatingValue (String ratings[]) {
		int lowestValue = 100;
		for (int i = 0; i < ratings.length; i++) {
			// get the rating
			EnergyRating rating = EnergyRatings.getEnergyRating(ratings[i]);
			// got the relevant rating, now compare
			if (rating.getStart() <= lowestValue) {
				lowestValue = rating.getStart();
			}
		}
		return lowestValue;
	}

	public static Integer getHighestRatingValue (String ratings[]) {
		int value = 0;
		for (int i = 0; i < ratings.length; i++) {
			// get the rating
			EnergyRating rating = EnergyRatings.getEnergyRating(ratings[i]);
			// got the relevant rating, now compare
			if (rating.getEnd() >= value) {
				value = rating.getEnd();
			}
		}
		return value;
	}

	public static String getTimeTakenFromMillis (long millis) {
		long time = millis / 1000;
		String seconds = Integer.toString((int)(time % 60));
		String minutes = Integer.toString((int)((time % 3600) / 60));
		String hours = Integer.toString((int)(time / 3600));
		for (int i = 0; i < 2; i++) {
			if (seconds.length() < 2) {
				seconds = "0" + seconds;
			}
			if (minutes.length() < 2) {
				minutes = "0" + minutes;
			}
			if (hours.length() < 2) {
				hours = "0" + hours;

			}

		}
		return "hours:+" + hours + ", minutes:" + minutes + ", seconds:" + seconds;
	}

}
