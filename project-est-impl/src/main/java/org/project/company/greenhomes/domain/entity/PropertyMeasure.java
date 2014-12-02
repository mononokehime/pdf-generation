package org.project.company.greenhomes.domain.entity;

import java.io.Serializable;

/**
 *
 */
public class PropertyMeasure implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	private Integer categoryId;
	private String propertyAddressId;
	private String heading;
	private String description;
	private Integer sortOder;
	private Integer typicalSaving;

	private PropertyAddress propertyAddress;

	public Integer getCategoryId () {
		return categoryId;
	}

	public void setCategoryId (Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getPropertyAddressId () {
		return propertyAddressId;
	}

	public void setPropertyAddressId (String propertyAddressId) {
		this.propertyAddressId = propertyAddressId;
	}

	public String getHeading () {
		return heading;
	}

	public void setHeading (String heading) {
		this.heading = heading;
	}

	public String getDescription () {
		return description;
	}

	public void setDescription (String description) {
		this.description = description;
	}

	public Integer getSortOder () {
		return sortOder;
	}

	public void setSortOder (Integer sortOder) {
		this.sortOder = sortOder;
	}

	public PropertyAddress getPropertyAddress () {
		return propertyAddress;
	}

	public void setPropertyAddress (PropertyAddress propertyAddress) {
		this.propertyAddress = propertyAddress;
	}

	@Override
	public int hashCode () {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((categoryId == null) ? 0 : categoryId.hashCode());
		result = prime * result
				+ ((sortOder == null) ? 0 : sortOder.hashCode());
		return result;
	}

	@Override
	public boolean equals (Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PropertyMeasure other = (PropertyMeasure)obj;
		if (categoryId == null) {
			if (other.categoryId != null) {
				return false;
			}
		} else if (!categoryId.equals(other.categoryId)) {
			return false;
		}
		if (sortOder == null) {
			if (other.sortOder != null) {
				return false;
			}
		} else if (!sortOder.equals(other.sortOder)) {
			return false;
		}
		return true;
	}

	public Integer getTypicalSaving () {
		return typicalSaving;
	}

	public void setTypicalSaving (Integer typicalSaving) {
		this.typicalSaving = typicalSaving;
	}

}
