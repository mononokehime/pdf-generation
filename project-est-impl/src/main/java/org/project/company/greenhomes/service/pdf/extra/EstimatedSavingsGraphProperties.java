package org.project.company.greenhomes.service.pdf.extra;

import java.awt.*;

/**
 * Estimated savings graph on page 2 constant values
 *
 *
 */
public class EstimatedSavingsGraphProperties {

	public static final float height = 13;
	public static final float fullWidth = 228;
	public static final float column1X = 46;
	public static final float column2X = fullWidth + column1X + 49;
	public static final float row1Y = 730;
	public static final float row2Y = 685;
	private float width;
	private String cost;
	private String text;
	private Color color;
	private String headline;
	private float x;
	private float y;
	public EstimatedSavingsGraphProperties (Color color, float width, float x, float y, String text, String cost) {
		this.color = color;
		this.width = width;
		this.x = x;
		this.y = y;
		this.cost = cost;
		this.text = text;
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

	public String getCost () {
		return cost;
	}

	public void setCost (String cost) {
		this.cost = cost;
	}

	public String getText () {
		return text;
	}

	public void setText (String text) {
		this.text = text;
	}

	public String getHeadline () {
		return headline;
	}

	public void setHeadline (String headline) {
		this.headline = headline;
	}
}


