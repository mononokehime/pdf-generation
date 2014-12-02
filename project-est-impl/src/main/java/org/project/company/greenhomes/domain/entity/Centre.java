package org.project.company.greenhomes.domain.entity;

import java.io.Serializable;

public class Centre implements Serializable {

	private Integer centreCode;
	private String centreName;
	private String address1;
	private String address2;
	private String address3;
	private String address4;
	private String address5;
	private String postcode;
	private String contactName;
	private String contactJobTitle;

	public Integer getCentreCode () {
		return centreCode;
	}

	public void setCentreCode (Integer centreCode) {
		this.centreCode = centreCode;
	}

	public String getCentreName () {
		return centreName;
	}

	public void setCentreName (String centreName) {
		this.centreName = centreName;
	}

	public String getAddress1 () {
		return address1;
	}

	public void setAddress1 (String address1) {
		this.address1 = address1;
	}

	public String getAddress2 () {
		return address2;
	}

	public void setAddress2 (String address2) {
		this.address2 = address2;
	}

	public String getAddress3 () {
		return address3;
	}

	public void setAddress3 (String address3) {
		this.address3 = address3;
	}

	public String getAddress4 () {
		return address4;
	}

	public void setAddress4 (String address4) {
		this.address4 = address4;
	}

	public String getAddress5 () {
		return address5;
	}

	public void setAddress5 (String address5) {
		this.address5 = address5;
	}

	public String getPostcode () {
		return postcode;
	}

	public void setPostcode (String postcode) {
		this.postcode = postcode;
	}

	public String getContactName () {
		return contactName;
	}

	public void setContactName (String contactName) {
		this.contactName = contactName;
	}

	public String getContactJobTitle () {
		return contactJobTitle;
	}

	public void setContactJobTitle (String contactJobTitle) {
		this.contactJobTitle = contactJobTitle;
	}
}
