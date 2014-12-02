package org.project.company.greenhomes.domain.entity;

import java.util.List;
import java.util.Set;

public interface PropertyAddress {

	String getPropertyAddressId ();

	void setPropertyAddressId (String propertyAddressId);

	Long getUploadId ();

	void setUploadId (Long uploadId);

	String getPostcodeIncode ();

	void setPostcodeIncode (String postcodeIncode);

	String getPostcodeOutcode ();

	void setPostcodeOutcode (String postcodeOutcode);

	String getAddressLine1 ();

	void setAddressLine1 (String addressLine1);

	String getAddressLine2 ();

	void setAddressLine2 (String addressLine2);

	String getAddressLine3 ();

	void setAddressLine3 (String addressLine3);

	String getTown ();

	void setTown (String town);

	String getDistrict ();

	void setDistrict (String district);

	String getCounty ();

	void setCounty (String county);

	String getCountry ();

	void setCountry (String country);

	Integer getESTAC ();

	void setESTAC (Integer estac);

	String getLocalAuthority ();

	void setLocalAuthority (String localAuthority);

	String getAddressKey ();

	void setAddressKey (String addressKey);

	Long getScheduleId ();

	void setScheduleId (Long scheduleId);

	String getWorkFlowStatus ();

	void setWorkFlowStatus (String workFlowStatus);

	String getLocality ();

	void setLocality (String locality);

	Set<PropertyAttribute> getPropertyAddressAttributeSet ();

	void setPropertyAddressAttributeSet (
			Set<PropertyAttribute> propertyAddressAttributeSet);

	void addPropertyAddressAttribute (PropertyAttribute attr);

	List<FormattedAddressView> getPossibleAddressMatches ();

	void setPossibleAddressMatches (List<FormattedAddressView> possibleAddressMatches);

	Centre getCentre ();

	void setCentre (Centre centre);

}
