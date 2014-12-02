package org.project.company.greenhomes.domain.model;

import java.io.Serializable;

public class ReferenceData implements Serializable {

	private String referenceDataKey;
	private String shortName;
	private String longName;

	public String getReferenceDataKey () {
		return referenceDataKey;
	}

	public void setReferenceDataKey (String referenceDataKey) {
		this.referenceDataKey = referenceDataKey;
	}

	public String getShortName () {
		return shortName;
	}

	public void setShortName (String shortName) {
		this.shortName = shortName;
	}

	public String getLongName () {
		return longName;
	}

	public void setLongName (String longName) {
		this.longName = longName;
	}

}
