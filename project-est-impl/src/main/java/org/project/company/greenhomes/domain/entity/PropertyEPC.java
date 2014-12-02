package org.project.company.greenhomes.domain.entity;

import org.project.company.greenhomes.common.enums.PropertyAttributeNames;
import org.project.company.greenhomes.common.util.EnergyRatings;
import org.project.company.greenhomes.common.util.EnergyRatings.EnergyRating;

import java.io.Serializable;
import java.util.*;

public class PropertyEPC implements PropertyAddress, Cloneable, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * used for listing possible address matches when there is a problem
	 */
	List<FormattedAddressView> possibleAddressMatches;
	//private String propertySaleId;
	private int rownum;
	private String rating;
	private Date inspectionDate;
	/*
	 * Used for display at front end only
	 */
	private Date saleDate;
	private Set<PropertyAttribute> propertyAddressAttributeSet;
	private String propertyAddressId;
	private Set<PropertyMeasure> measuresSet;
	private Long uploadId;
	private String postcodeIncode;
	private String postcodeOutcode;
	private String addressLine1;
	private String addressLine2;
	private String consumerSegment;
	private String addressLine3;
	private String town;
	private String district;
	private String county;
	private String country;
	private String countryName;
	private Integer ESTAC;
	private String ESTACName;
	private String localAuthority;
	private String localAuthorityName;
	private String addressKey;
	private Long scheduleId;
	private String workFlowStatus;
	//private String consumerSegment;
	private String locality;
	//private EnergyRating energyRating;
	private Integer energyRatingCurrent;
	//private EnergyRating potentialEnergyRating;
	private Integer energyRatingPotential;
	private String rrn;
	private Centre centre;
	public PropertyEPC () {
	}
	public PropertyEPC (UUID propertyAddressId) {
		this.propertyAddressId = propertyAddressId.toString();
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

	public Centre getCentre () {
		return centre;
	}

	public void setCentre (Centre centre) {
		this.centre = centre;
	}

	//private String firstName;
	public String getRrn () {
		return rrn;
	}

	public void setRrn (String rrn) {
		this.rrn = rrn;
	}

	public Date getInspectionDate () {
		return inspectionDate;
	}

	public void setInspectionDate (Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public Integer getEnergyRatingCurrent () {
		return energyRatingCurrent;
	}

	public void setEnergyRatingCurrent (Integer energyRatingCurrent) {
		this.energyRatingCurrent = energyRatingCurrent;
	}

	public Integer getEnergyRatingPotential () {
		return energyRatingPotential;
	}

	public void setEnergyRatingPotential (Integer energyRatingPotential) {
		this.energyRatingPotential = energyRatingPotential;
	}

	public PropertyAttribute getNamedAttribute (PropertyAttributeNames name) {
		for (PropertyAttribute attr : propertyAddressAttributeSet) {
			if (attr.getName().equalsIgnoreCase(name.getValue())) {
				return attr;
			}
		}
		return null;
	}

	/**
	 * Returns a list of property measures from the propertyMeasures Set based
	 * on the improvement category id i.e. low, high, further or 1,2,3 respectively
	 *
	 * @param improvementCategoryId
	 * @return never returns null
	 */
	public List<PropertyMeasure> getNamedMeasureType (Integer categoryId) {
		List<PropertyMeasure> list = new ArrayList<PropertyMeasure>();
		for (PropertyMeasure attr : measuresSet) {
			if (attr.getCategoryId().intValue() == categoryId.intValue()) {
				list.add(attr);
			}
		}
		return list;
	}

	public String getRating () {

		EnergyRating rating = EnergyRatings.getRating(getEnergyRatingCurrent());
		return rating.getRating();
	}

	public void setRating (String rating) {
		this.rating = rating;
	}

	//	public String getPropertySaleId() {
	//		return propertySaleId;
	//	}
	//	public void setPropertySaleId(String propertySaleId) {
	//		this.propertySaleId = propertySaleId;
	//	}
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

	public String getCountryName () {
		return countryName;
	}

	public void setCountryName (String countryName) {
		this.countryName = countryName;
	}

	public String getESTACName () {
		return ESTACName;
	}

	public void setESTACName (String name) {
		ESTACName = name;
	}

	public String getLocalAuthorityName () {
		return localAuthorityName;
	}

	public void setLocalAuthorityName (String localAuthorityName) {
		this.localAuthorityName = localAuthorityName;
	}

	public String getLocality () {
		return this.locality;
	}

	public void setLocality (String locality) {
		this.locality = locality;

	}

	public Set<PropertyMeasure> getMeasuresSet () {
		return measuresSet;
	}

	public void setMeasuresSet (Set<PropertyMeasure> measuresSet) {
		this.measuresSet = measuresSet;
	}

	public void addPropertyMeasure (PropertyMeasure attr) {
		if (null == measuresSet) {
			measuresSet = new HashSet<PropertyMeasure>();
		}
		attr.setPropertyAddress(this);
		attr.setPropertyAddressId(this.getPropertyAddressId());
		measuresSet.add(attr);
	}

	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append("date:" + inspectionDate);
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
		sb.append("\n rrn:" + rrn);
		sb.append("\n energyRatingCurrent:" + energyRatingCurrent);
		sb.append("\n energyRatingPotential:" + energyRatingPotential);
		return sb.toString();
	}

	public String getConsumerSegment () {
		PropertyAttribute attr = getNamedAttribute(PropertyAttributeNames.estsegmentdescription);
		if (null != attr) {
			return attr.getValue();
		}
		return "";
	}

	public void setConsumerSegment (String consumerSegment) {
		this.consumerSegment = consumerSegment;
	}

	public int getRownum () {
		return rownum;
	}

	public void setRownum (int rownum) {
		this.rownum = rownum;
	}

	public Date getSaleDate () {
		return saleDate;
	}

	public void setSaleDate (Date saleDate) {
		this.saleDate = saleDate;
	}
}
