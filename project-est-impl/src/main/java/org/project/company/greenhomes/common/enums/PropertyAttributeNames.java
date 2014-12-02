package org.project.company.greenhomes.common.enums;

/**
 * Enum class for references to property attributes
 *
 * @author fmacder
 */
public enum PropertyAttributeNames {

	/**
	 * Constructor for user TYPICAL_SAVING value
	 */
	TYPICAL_SAVING("Typical-Saving"),
	/**
	 * Constructor for user lighting cost current value
	 */
	LIGHTING_COST_CURRENT("Lighting-Cost-Current"),
	/**
	 * Constructor for user lighting cost potential value
	 */
	LIGHTING_COST_POTENTIAL("Lighting-Cost-Potential"),
	/**
	 * Constructor for user HEATING cost current value
	 */
	HEATING_COST_CURRENT("Heating-Cost-Current"),
	/**
	 * Constructor for user HEATING cost potential value
	 */
	HEATING_COST_POTENTIAL("Heating-Cost-Potential"),
	/**
	 * Constructor for user Hot-Water- cost current value
	 */
	HOT_WATER_COST_CURRENT("Hot-Water-Cost-Current"),
	/**
	 * Constructor for user Hot-Water- cost potential value
	 */
	HOT_WATER_COST_POTENTIAL("Hot-Water-Cost-Potential"),
	/**
	 * Constructor for user CO2-Emissions- cost current value
	 */
	CO2_EMISSIONS_COST_CURRENT("CO2-Emissions-Current"),
	/**
	 * Constructor for user CO2-Emissions- cost potential value
	 */
	CO2_EMISSIONS_COST_POTENTIAL("CO2-Emissions-Potential"),
	estsegmentdescription("estsegmentdescription");
	/**
	 * value variable
	 */
	private final String value;

	PropertyAttributeNames (String serviceName) {
		value = serviceName;
	}

	/**
	 * @return the value
	 */
	public String getValue () {
		return value;
	}
}
