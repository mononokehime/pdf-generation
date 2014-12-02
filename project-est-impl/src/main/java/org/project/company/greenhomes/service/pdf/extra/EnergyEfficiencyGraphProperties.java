package org.project.company.greenhomes.service.pdf.extra;

import java.awt.*;

/**
 * Class that encapsulates properties for the energy efficiency graphs
 *
 *
 */
public class EnergyEfficiencyGraphProperties {
	public static final float x = 353;
	public static final float y = 615;
	/*
	 * height of the bar
	 */
	public static final float height = 9;
	/*
	 * offset for text
	 */
	public static final float offSet = height + 3;
	/*
	 * how much to grown by from previous one for each rating bar
	 */
	public static final float growBy = 7;
	public static final float startingWidth = 45;
	/*
	 * width of the graph bar
	 */
	private float width;
	/*
	 * color to fill the bar in with
	 */
	private Color color;
	private String ratingRange;
	private String rating;
	private float yOffset;
	/**
	 * Contructor for props
	 *
	 * @param color
	 * @param yOffset
	 * @param width
	 * @param ratingRange
	 * @param rating
	 */
	public EnergyEfficiencyGraphProperties (Color color, float yOffset, float width, String ratingRange,
			String rating) {
		this.color = color;
		this.width = width;
		//	this.height = height;
		this.yOffset = yOffset;
		this.ratingRange = ratingRange;
		this.rating = rating;
	}

	public float getX () {
		return x;
	}

	public float getY () {
		return y;
	}

	public float getWidth () {
		return width;
	}

	public void setWidth (float width) {
		this.width = width;
	}

	public float getHeight () {
		return height;
	}

	public Color getColor () {
		return color;
	}

	public void setColor (Color color) {
		this.color = color;
	}

	public String getRatingRange () {
		return ratingRange;
	}

	public void setRatingRange (String ratingRange) {
		this.ratingRange = ratingRange;
	}

	public String getRating () {
		return rating;
	}

	public void setRating (String rating) {
		this.rating = rating;
	}

	public float getYOffset () {
		return yOffset;
	}

	public void setYOffset (float offset) {
		yOffset = offset;
	}
}


