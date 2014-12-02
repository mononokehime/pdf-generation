package org.project.company.greenhomes.domain.model;

public class SearchParameters {

	private String templateId;
	private Long scheduleId;
	private String rating;
	private String treatment;
	private String country;
	private String localAuthority;
	private String ESTAC;
	private String consumerSegment;
	private String fromDate;
	private String toDate;
	private String workflowStatus;

	public String getWorkflowStatus () {
		return workflowStatus;
	}

	public void setWorkflowStatus (String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public String getRating () {
		return rating;
	}

	public void setRating (String rating) {
		this.rating = rating;
	}

	public String getTreatment () {
		return treatment;
	}

	public void setTreatment (String treatment) {
		this.treatment = treatment;
	}

	public String getCountry () {
		return country;
	}

	public void setCountry (String country) {
		this.country = country;
	}

	public String getESTAC () {
		return ESTAC;
	}

	public void setESTAC (String estac) {
		ESTAC = estac;
	}

	public String getConsumerSegment () {
		return consumerSegment;
	}

	public void setConsumerSegment (String consumerSegment) {
		this.consumerSegment = consumerSegment;
	}

	public String getFromDate () {
		return fromDate;
	}

	public void setFromDate (String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate () {
		return toDate;
	}

	public void setToDate (String toDate) {
		this.toDate = toDate;
	}

	public String getLocalAuthority () {
		return localAuthority;
	}

	public void setLocalAuthority (String localAuthority) {
		this.localAuthority = localAuthority;
	}

	public Long getScheduleId () {
		return scheduleId;
	}

	public void setScheduleId (Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getTemplateId () {
		return templateId;
	}

	public void setTemplateId (String templateId) {
		this.templateId = templateId;
	}

}
