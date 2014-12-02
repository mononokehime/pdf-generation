package org.project.company.greenhomes.domain.entity;

import java.io.Serializable;
import java.util.*;

public class PropertySale implements PropertyAddress, Cloneable, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * used for listing possible address matches when there is a problem
	 */
	List<FormattedAddressView> possibleAddressMatches;
	private Set<PropertyAttribute> propertyAddressAttributeSet;
	private String propertyAddressId;
	private Centre centre;
	private Long uploadId;
	private String postcodeIncode;
	private String postcodeOutcode;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String town;
	private String district;
	private String county;
	private String country;
	private Integer ESTAC;
	private String localAuthority;
	private String addressKey;
	private Long scheduleId;
	private String workFlowStatus;
	private Date saleDate;
	private String propertySaleId;
	private String propertyType;
	private String locality;
	public PropertySale () {
	}
	public PropertySale (UUID id) {
		this.propertyAddressId = id.toString();
	}

	public void addPropertyAddressAttribute (PropertyAttribute attr) {
		if (null == propertyAddressAttributeSet) {

			propertyAddressAttributeSet = new HashSet<PropertyAttribute>();
		}

		attr.setPropertyAddress(this);
		attr.setPropertyAddressId(this.getPropertyAddressId());
		propertyAddressAttributeSet.add(attr);
	}

	public List<FormattedAddressView> getPossibleAddressMatches () {
		return possibleAddressMatches;
	}

	public void setPossibleAddressMatches (
			List<FormattedAddressView> possibleAddressMatches) {
		this.possibleAddressMatches = possibleAddressMatches;
	}

	public String getLocality () {
		return locality;
	}

	public void setLocality (String locality) {
		this.locality = locality;
	}

	public Date getSaleDate () {
		return saleDate;
	}

	public void setSaleDate (Date saleDate) {
		this.saleDate = saleDate;
	}

	public String getPropertyType () {
		return propertyType;
	}

	public void setPropertyType (String propertyType) {
		this.propertyType = propertyType;
	}

	public String getPropertySaleId () {
		return propertySaleId;
	}

	public void setPropertySaleId (String propertySaleId) {
		this.propertySaleId = propertySaleId;
	}

	public Set<PropertyAttribute> getPropertyAddressAttributeSet () {
		return propertyAddressAttributeSet;
	}

	public void setPropertyAddressAttributeSet (
			Set<PropertyAttribute> propertyAddressAttributeSet) {
		this.propertyAddressAttributeSet = propertyAddressAttributeSet;
	}

	public String getPropertyAddressId () {
		return propertyAddressId;
	}

	public void setPropertyAddressId (String propertyAddressId) {
		this.propertyAddressId = propertyAddressId;
	}

	public Long getUploadId () {
		return uploadId;
	}

	public void setUploadId (Long uploadId) {
		this.uploadId = uploadId;
	}

	public String getPostcodeIncode () {
		return postcodeIncode;
	}

	public void setPostcodeIncode (String postcodeIncode) {
		this.postcodeIncode = postcodeIncode;
	}

	public String getPostcodeOutcode () {
		return postcodeOutcode;
	}

	public void setPostcodeOutcode (String postcodeOutcode) {
		this.postcodeOutcode = postcodeOutcode;
	}

	public String getAddressLine1 () {
		return addressLine1;
	}

	public void setAddressLine1 (String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2 () {
		return addressLine2;
	}

	public void setAddressLine2 (String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3 () {
		return addressLine3;
	}

	public void setAddressLine3 (String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getTown () {
		return town;
	}

	public void setTown (String town) {
		this.town = town;
	}

	public String getDistrict () {
		return district;
	}

	public void setDistrict (String district) {
		this.district = district;
	}

	public String getCounty () {
		return county;
	}

	public void setCounty (String county) {
		this.county = county;
	}

	public String getCountry () {
		return country;
	}

	public void setCountry (String country) {
		this.country = country;
	}

	public Integer getESTAC () {
		return ESTAC;
	}

	public void setESTAC (Integer estac) {
		ESTAC = estac;
	}

	public String getLocalAuthority () {
		return localAuthority;
	}

	public void setLocalAuthority (String localAuthority) {
		this.localAuthority = localAuthority;
	}

	public String getAddressKey () {
		return addressKey;
	}

	public void setAddressKey (String addressKey) {
		this.addressKey = addressKey;
	}

	public Long getScheduleId () {
		return scheduleId;
	}

	public void setScheduleId (Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getWorkFlowStatus () {
		return workFlowStatus;
	}

	public void setWorkFlowStatus (String workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
	}

	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append("date:" + saleDate);
		sb.append("\naddress line 1:" + addressLine1);
		sb.append("\naddress line 2:" + addressLine2);
		sb.append("\naddress line 3:" + addressLine3);
		sb.append("\nlocality:" + locality);
		sb.append("\ntown:" + town);
		sb.append("\ndistrict:" + district);
		sb.append("\ncounty:" + county);
		sb.append("\nin code:" + postcodeIncode);
		sb.append("\n out code:" + postcodeOutcode);
		sb.append("\n uploadId:" + uploadId);
		//	sb.append("\n out code:"+postcodeOutcode);
		return sb.toString();
	}

	public Centre getCentre () {
		return centre;
	}

	public void setCentre (Centre centre) {
		this.centre = centre;
	}

}
