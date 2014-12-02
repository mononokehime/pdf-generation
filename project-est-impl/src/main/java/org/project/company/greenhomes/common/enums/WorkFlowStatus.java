package org.project.company.greenhomes.common.enums;

/**
 * Enum class for work flow status
 *
 * @author fmacder
 */
public enum WorkFlowStatus {

	/**
	 * Constructor for received - the first status
	 */
	RECEIVED("received"),
	/**
	 * Constructor for completed
	 */
	COMPLETED("completed"),
	/**
	 * Constructor for duplicates
	 */
	DUPLICATE("duplicate"),
	/**
	 * Constructor for duplicates
	 */
	INVALID("invalid"),
	/**
	 * Constructor for failures
	 */
	FAILED("failed"),
	/**
	 * Constructor for successful ftp
	 */
	FTP_SUCCESS("ftp-success"),
	/**
	 * Constructor for failed ftp
	 */
	FTP_FAILED("ftp-failed"),
	/**
	 * Constructor for scheduled
	 */
	SCHEDULED("scheduled");
	/**
	 * value variable
	 */
	private final String value;

	WorkFlowStatus (String serviceName) {
		value = serviceName;
	}

	/**
	 * @return the value
	 */
	public String getValue () {
		return value;
	}
}
