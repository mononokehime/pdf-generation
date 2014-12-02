package org.project.company.greenhomes.domain.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UploadSummary implements Serializable {

	private Long uploadSummaryId;
	private String uploadType;
	private Date startTime;
	private Date endTime;
	private Integer errorCount = 0;
	private Boolean fileRenameSucceeded = true;

	private Integer numberOfRows;

	private List<PropertyAddress> propertyAddresses;

	public List<PropertyAddress> getPropertyAddresses () {
		return propertyAddresses;
	}

	public void setPropertyAddresses (List<PropertyAddress> propertyAddresses) {
		this.propertyAddresses = propertyAddresses;
	}

	public Long getUploadSummaryId () {
		return uploadSummaryId;
	}

	public void setUploadSummaryId (Long uploadSummaryId) {
		this.uploadSummaryId = uploadSummaryId;
	}

	public String getUploadType () {
		return uploadType;
	}

	public void setUploadType (String uploadType) {
		this.uploadType = uploadType;
	}

	public Date getStartTime () {
		return startTime;
	}

	public void setStartTime (Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime () {
		return endTime;
	}

	public void setEndTime (Date endTime) {
		this.endTime = endTime;
	}

	public Integer getNumberOfRows () {
		return numberOfRows;
	}

	public void setNumberOfRows (Integer numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public Integer getErrorCount () {
		return errorCount;
	}

	public void setErrorCount (Integer errorCount) {
		this.errorCount = errorCount;
	}

	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append("uploadSummaryId+" + uploadSummaryId);
		sb.append("\nuploadType+" + uploadType);
		sb.append("\nstartTime+" + startTime);
		sb.append("\nendTime+" + endTime);
		sb.append("\nerrorCount+" + errorCount);
		return sb.toString();
	}

	public Boolean getFileRenameSucceeded () {
		return fileRenameSucceeded;
	}

	public void setFileRenameSucceeded (Boolean fileRenameSucceeded) {
		this.fileRenameSucceeded = fileRenameSucceeded;
	}
}
