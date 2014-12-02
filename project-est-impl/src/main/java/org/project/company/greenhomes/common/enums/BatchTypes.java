package org.project.company.greenhomes.common.enums;

/**
 * Enum class for references to the batch types this identifies the data import as either epc or property sales
 *
 * @author fmacder
 */
public enum BatchTypes {

	/**
	 * Constructor for user PROPERTY_SALES value
	 */
	PROPERTY_SALES("PROPSALE"),
	/**
	 * Constructor for user PROPERTY_SALES value
	 */
	PDF_GENERATION("PDF_GENERATION"),
	/**
	 * Constructor for user EPC_XML value
	 */
	EPC_REPORT("EPC_REPORT");
	/**
	 * value variable
	 */
	private final String value;

	BatchTypes (String serviceName) {
		value = serviceName;
	}

	/**
	 * @return the value
	 */
	public String getValue () {
		return value;
	}
}
