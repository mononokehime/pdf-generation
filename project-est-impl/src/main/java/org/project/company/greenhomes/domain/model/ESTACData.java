package org.project.company.greenhomes.domain.model;

import java.io.Serializable;

public class ESTACData implements Serializable {

	private String region;
	private Integer centreCode;
	private String localAuthority;

	public ESTACData (String region, Integer centreCode, String localAuthority) {
		super();
		this.region = region;
		this.centreCode = centreCode;
		this.localAuthority = localAuthority;
	}

	@Override
	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append("region:" + region);

		sb.append("\ncentreCode:" + centreCode);

		return sb.toString();
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

	public String getLocalAuthority () {
		return localAuthority;
	}

	public void setLocalAuthority (String localAuthority) {
		this.localAuthority = localAuthority;
	}

}
