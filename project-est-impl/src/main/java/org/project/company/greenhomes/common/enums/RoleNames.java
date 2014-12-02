package org.project.company.greenhomes.common.enums;

/**
 * Enum class for references to the roles,
 *
 * @author fmacder
 */
public enum RoleNames {

	/**
	 * Constructor for user ROLE_USER value
	 */
	ROLE_SUPER_USER("ROLE_SUPER_USER"),
	/**
	 * Constructor for user ROLE_USER value
	 */
	ROLE_USER("ROLE_USER");
	/**
	 * value variable
	 */
	private final String value;

	RoleNames (String serviceName) {
		value = serviceName;
	}

	/**
	 * @return the value
	 */
	public String getValue () {
		return value;
	}
}
