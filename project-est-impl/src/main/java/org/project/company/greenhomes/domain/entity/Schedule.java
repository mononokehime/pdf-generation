package org.project.company.greenhomes.domain.entity;

import java.io.Serializable;
import java.util.Date;

public class Schedule implements Serializable {

	private Long scheduleId;
	private Long scheduleById;
	private String scheduledByName;
	private Date requestDate;
	private Date startDate;
	private Date endDate;
	private String scheduleSummary;
	private String templateId;
	private String templateVersion;

	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append("scheduleId:" + scheduleId);
		sb.append("\nrequired: scheduleById:" + scheduleById);
		sb.append("\nrequired: requestDate:" + requestDate);
		sb.append("\nrequired: startDate:" + startDate);
		sb.append("\nendDate:" + endDate);
		sb.append("\nscheduleSummary:" + scheduleSummary);
		sb.append("\nrequired: templateId:" + templateId);

		return sb.toString();
	}

	public Long getScheduleId () {
		return scheduleId;
	}

	public void setScheduleId (Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Long getScheduleById () {
		return scheduleById;
	}

	public void setScheduleById (Long scheduleById) {
		this.scheduleById = scheduleById;
	}

	public Date getRequestDate () {
		return requestDate;
	}

	public void setRequestDate (Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getStartDate () {
		return startDate;
	}

	public void setStartDate (Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate () {
		return endDate;
	}

	public void setEndDate (Date endDate) {
		this.endDate = endDate;
	}

	public String getScheduleSummary () {
		return scheduleSummary;
	}

	public void setScheduleSummary (String scheduleSummary) {
		this.scheduleSummary = scheduleSummary;
	}

	public String getTemplateId () {
		return templateId;
	}

	public void setTemplateId (String templateId) {
		this.templateId = templateId;
	}

	public String getScheduledByName () {
		return scheduledByName;
	}

	public void setScheduledByName (String scheduledByName) {
		this.scheduledByName = scheduledByName;
	}

	public String getTemplateVersion () {
		return templateVersion;
	}

	public void setTemplateVersion (String templateVersion) {
		this.templateVersion = templateVersion;
	}

}
