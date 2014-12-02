package org.project.company.greenhomes.domain.entity;

import java.io.Serializable;

public class ExceptionReport implements Serializable {

	private Long exceptionReportId;
	private String message;
	private String functionalArea;
	private String exception;
	private String processId;

	public String getProcessId () {
		return processId;
	}

	public void setProcessId (String processId) {
		this.processId = processId;
	}

	public Long getExceptionReportId () {
		return exceptionReportId;
	}

	public void setExceptionReportId (Long exceptionReportId) {
		this.exceptionReportId = exceptionReportId;
	}

	public String getMessage () {
		return message;
	}

	public void setMessage (String message) {
		this.message = message;
	}

	public String getFunctionalArea () {
		return functionalArea;
	}

	public void setFunctionalArea (String functionalArea) {
		this.functionalArea = functionalArea;
	}

	public String getException () {
		return exception;
	}

	public void setException (String exception) {
		this.exception = exception;
	}

}
