package org.project.company.greenhomes.domain.entity;

import java.io.Serializable;

public class Measure implements Serializable {

	private Integer measureId;
	private Integer category;
	private String summary;
	private String heading;
	private String description;
	private String estHeading;
	private String estDescription;

	public Integer getMeasureId () {
		return measureId;
	}

	public void setMeasureId (Integer measureId) {
		this.measureId = measureId;
	}

	public Integer getCategory () {
		return category;
	}

	public void setCategory (Integer category) {
		this.category = category;
	}

	public String getSummary () {
		return summary;
	}

	public void setSummary (String summary) {
		this.summary = summary;
	}

	public String getHeading () {
		return heading;
	}

	public void setHeading (String heading) {
		this.heading = heading;
	}

	public String getDescription () {
		return description;
	}

	public void setDescription (String description) {
		this.description = description;
	}

	public String getEstHeading () {
		return estHeading;
	}

	public void setEstHeading (String estHeading) {
		this.estHeading = estHeading;
	}

	public String getEstDescription () {
		return estDescription;
	}

	public void setEstDescription (String estDescription) {
		this.estDescription = estDescription;
	}

}
