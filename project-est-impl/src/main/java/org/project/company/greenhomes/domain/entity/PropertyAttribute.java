package org.project.company.greenhomes.domain.entity;

import java.io.Serializable;

public class PropertyAttribute implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	private Long propertyAddressAttributeId;
	private String propertyAddressId;
	private String name;
	private String value;
	private PropertyAddress propertyAddress;

	public Long getPropertyAddressAttributeId () {
		return propertyAddressAttributeId;
	}

	public void setPropertyAddressAttributeId (Long propertyAddressAttributeId) {
		this.propertyAddressAttributeId = propertyAddressAttributeId;
	}

	//	public Long getPropertySaleId() {
	//		return propertySaleId;
	//	}
	//	public void setPropertySaleId(Long propertySaleId) {
	//		this.propertySaleId = propertySaleId;
	//	}
	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getValue () {
		return value;
	}

	public void setValue (String value) {
		this.value = value;
	}

	public PropertyAddress getPropertyAddress () {
		return propertyAddress;
	}

	public void setPropertyAddress (PropertyAddress propertyAddress) {
		this.propertyAddress = propertyAddress;
	}

	public String getPropertyAddressId () {
		return propertyAddressId;
	}

	public void setPropertyAddressId (String propertyAddressId) {
		this.propertyAddressId = propertyAddressId;
	}

}
