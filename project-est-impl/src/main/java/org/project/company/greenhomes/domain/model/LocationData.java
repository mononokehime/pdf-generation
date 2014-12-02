package org.project.company.greenhomes.domain.model;

import java.io.Serializable;

public class LocationData implements Serializable {

	private String addressKey;
	private String region;
	private Integer centreCode;
	private String estSegmentDescription;
	private String localAuthority;

	public String getAddressKey () {
		return addressKey;
	}

	public void setAddressKey (String addressKey) {
		this.addressKey = addressKey;
	}

	public String getRegion () {
		return region;
	}

	public void setRegion (String region) {
		this.region = region;
	}

	public Integer getCentreCode () {
		return centreCode;
	}

	public void setCentreCode (Integer centreCode) {
		this.centreCode = centreCode;
	}

	public String getEstSegmentDescription () {
		return estSegmentDescription;
	}

	public void setEstSegmentDescription (String estSegmentDescription) {
		this.estSegmentDescription = estSegmentDescription;
	}

	public String getLocalAuthority () {
		return localAuthority;
	}

	public void setLocalAuthority (String localAuthority) {
		this.localAuthority = localAuthority;
	}
}
