package org.project.company.greenhomes.common.enums;

/**
 * Enum class for references to the services,
 *
 * @author fmacder
 */
public enum BeanNames {

	/**
	 * Constructor for user manager service value
	 */
	USER_SERVICE("userService"),
	/**
	 * Constructor for property sales  service value
	 */
	PROPERTY_SALES_SERVICE("propertyService"),
	/**
	 * Constructor for query engine  service value
	 */
	QUERY_ENGINE_SERVICE("queryEngineService"),
	/**
	 * Constructor for query engine  service value
	 */
	BATCH_SUMMARY_SERVICE("batchSummaryService"),
	/**
	 * Constructor for pdf letter builder
	 */
	PDF_LETTER_BUILDER("pdfLetterBuilder"),
	/**
	 * Constructor for mail  service value
	 */
	MAIL_SERVICE("mailService"),
	/**
	 * Constructor for user manager service value
	 */
	REFERENCE_DATA_SERVICE("referenceDataService"),
	/**
	 * Constructor for landRegistryReader
	 */
	LAND_REGISTRY_READER("landRegistryReader"),
	/**
	 * Constructor for landmark
	 */
	LAND_MARK_READER("landMarkReader"),
	//	/**
	//	 * Constructor for propertySalesReader value
	//	 */
	//	EXCEL_PROPERTY_SALES_READER ("excelPropertySalesReader"),
	/**
	 * Constructor for user data source jndi look up value
	 */
	USER_DATA_SOURCE("userDataSource"),
	/**
	 * Constructor for search and schedule controller
	 */
	SEARCH_AND_SCHEDULE_CONTROLLER("searchAndScheduleController"),
	/**
	 * Constructor for file upload controller
	 */
	FILE_UPLOAD_CONTROLLER("fileUploadController"),
	/**
	 * Constructor for template controller
	 */
	TEMPLATE_CONTROLLER("templateController"),
	/**
	 * Constructor for authenticationManager service value
	 */
	AUTHENTICATION_MANAGER("authenticationManager");
	/**
	 * value variable
	 */
	private final String value;

	BeanNames (String serviceName) {
		value = serviceName;
	}

	/**
	 * @return the value
	 */
	public String getValue () {
		return value;
	}
}
