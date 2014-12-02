package org.project.company.greenhomes.common.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that finds energy ratings from the energy rating score. Also can work vica versa
 * So an energy rating can return the range of scores for that rating.
 *
 * @author fmacder
 */
public class EnergyRatings {

	private static List<EnergyRating> ratings;

	/**
	 * initialiser just to set the values up
	 */
	static {
		ratings = new ArrayList<EnergyRating>();
		EnergyRatings me = new EnergyRatings();
		EnergyRating e = me.new EnergyRating("A", 92, 100);
		ratings.add(e);
		e = me.new EnergyRating("A", 92, 100);
		ratings.add(e);
		e = me.new EnergyRating("B", 81, 91);
		ratings.add(e);
		e = me.new EnergyRating("C", 69, 80);
		ratings.add(e);
		e = me.new EnergyRating("D", 55, 68);
		ratings.add(e);
		e = me.new EnergyRating("E", 39, 54);
		ratings.add(e);
		e = me.new EnergyRating("F", 21, 38);
		ratings.add(e);
		e = me.new EnergyRating("G", 1, 20);
		ratings.add(e);
		// go through and add a to g	
	}

	/**
	 * Returns the energy rating range based on supplied A-G value
	 *
	 * @param rating
	 * @return
	 */
	public static EnergyRating getEnergyRating (String rating) {
		if ("A".equalsIgnoreCase(rating)) {
			return getRating(95);
		}
		if ("B".equalsIgnoreCase(rating)) {
			return getRating(85);
		}
		if ("C".equalsIgnoreCase(rating)) {
			return getRating(75);
		}
		if ("D".equalsIgnoreCase(rating)) {
			return getRating(65);
		}
		if ("E".equalsIgnoreCase(rating)) {
			return getRating(45);
		}
		if ("F".equalsIgnoreCase(rating)) {
			return getRating(25);
		}
		if ("G".equalsIgnoreCase(rating)) {
			return getRating(10);
		}
		// if we get here then bad number called
		throw new InvalidParameterException("The rating given does not fit any energy rating. Given:" + rating);

	}

	/**
	 * Returns the energy rating A-G based on the number provided
	 *
	 * @param rating
	 * @return
	 */
	public static EnergyRating getRating (int rating) {
		// iterate through the ratings and return the relevant one#
		for (EnergyRating r : ratings) {
			if (rating >= r.getStart() && rating <= r.getEnd()) {
				return r;
			}
		}
		// if we get here then bad number called
		throw new InvalidParameterException("The rating given does not fit any energy rating. Given:" + rating);

	}

	public class EnergyRating {
		private String rating;
		private int start;
		private int end;
		public EnergyRating (String rating, int start, int end) {
			this.rating = rating;
			this.start = start;
			this.end = end;
		}

		public String getRating () {
			return rating;
		}

		public int getStart () {
			return start;
		}

		public int getEnd () {
			return end;
		}

		@Override
		public int hashCode () {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((rating == null) ? 0 : rating.hashCode());
			return result;
		}

		@Override
		public boolean equals (Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final EnergyRating other = (EnergyRating)obj;
			if (rating == null) {
				if (other.rating != null) {
					return false;
				}
			} else if (!rating.equals(other.rating)) {
				return false;
			}
			return true;
		}
	}

}
