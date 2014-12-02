package org.project.company.greenhomes.domain.entity;

import java.io.Serializable;

public class Experian implements Serializable {

	private String ESTSegmentDescription;
	private String postCodeIncode;
	private String postCodeOutcode;

	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append("\neSTSegmentDescription: " + ESTSegmentDescription);
		sb.append("\npostCodeIncode:" + postCodeIncode);
		sb.append("\npostCodeOutcode:" + postCodeOutcode);
		return sb.toString();
	}

	public String getESTSegmentDescription () {
		return ESTSegmentDescription;
	}

	public void setESTSegmentDescription (String segmentDescription) {
		ESTSegmentDescription = segmentDescription;
	}

	public String getPostCodeIncode () {
		return postCodeIncode;
	}

	public void setPostCodeIncode (String postCodeIncode) {
		this.postCodeIncode = postCodeIncode;
	}

	public String getPostCodeOutcode () {
		return postCodeOutcode;
	}

	public void setPostCodeOutcode (String postCodeOutcode) {
		this.postCodeOutcode = postCodeOutcode;
	}

}
