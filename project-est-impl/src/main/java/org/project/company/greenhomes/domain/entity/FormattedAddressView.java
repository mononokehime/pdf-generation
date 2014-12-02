package org.project.company.greenhomes.domain.entity;

import java.io.Serializable;

public class FormattedAddressView implements Serializable {

	private String address;
	private String addressKey;
	private String postCodeIncode;
	private String postCodeOutcode;

	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append("address:" + address);
		sb.append("\naddressKey: " + addressKey);
		sb.append("\npostCodeIncode:" + postCodeIncode);
		sb.append("\npostCodeOutcode:" + postCodeOutcode);
		return sb.toString();
	}

	public String getAddress () {
		return address;
	}

	public void setAddress (String address) {
		this.address = address;
	}

	public String getAddressKey () {
		return addressKey;
	}

	public void setAddressKey (String addressKey) {
		this.addressKey = addressKey;
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
