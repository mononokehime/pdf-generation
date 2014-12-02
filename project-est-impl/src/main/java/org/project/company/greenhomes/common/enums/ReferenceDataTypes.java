package org.project.company.greenhomes.common.enums;

/**
 * Enum class for references to the reference data so they
 * can be easily pulled out of the storage map
 *
 * @author fmacder
 */
public enum ReferenceDataTypes {
	/**
	 * Constructor for estacs
	 */
	ALL_ESTACS("allEstacs"),
	/**
	 * Constructor for local authorities
	 */
	ALL_LAS("alllas"),
	/**
	 * Constructor for templates
	 */
	TEMPLATES("templates"),
	/**
	 * Constructor for consumer segments
	 */
	CONSUMER_SEGMENTS("consumerSegments"),
	/**
	 * Constructor for countries
	 */
	COUNTRIES("countries"),
	/**
	 * Constructor for ratings value
	 */
	RATINGS("ratings"),
	/**
	 * Constructor for energy ratings value
	 */
	ENERGY_RATINGS("energy_ratings"),
	/**
	 * Constructor for treatments
	 */
	TREATMENTS("treatments");
	/**
	 * value variable
	 */
	private final String value;

	ReferenceDataTypes (String serviceName) {
		value = serviceName;
	}

	/**
	 * @return the value
	 */
	public String getValue () {
		return value;
	}
}
