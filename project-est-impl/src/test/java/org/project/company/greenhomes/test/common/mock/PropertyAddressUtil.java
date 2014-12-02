package org.project.company.greenhomes.test.common.mock;

import org.project.company.greenhomes.common.enums.PropertyAttributeNames;
import org.project.company.greenhomes.common.enums.WorkFlowStatus;
import org.project.company.greenhomes.domain.entity.PropertyAttribute;
import org.project.company.greenhomes.domain.entity.PropertyEPC;
import org.project.company.greenhomes.domain.entity.PropertySale;

import java.util.Calendar;
import java.util.UUID;

public class PropertyAddressUtil {

	public static PropertySale createPropertySale (String addressKey, String houseNumber) {
		//Date date = new Date();
		Integer myInt = new Integer(houseNumber);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -5);
		cal.add(Calendar.MONTH, -myInt);
		cal.add(Calendar.YEAR, -1);
		PropertySale sale = new PropertySale(UUID.randomUUID());
		sale.setAddressKey(addressKey);
		sale.setAddressLine1(houseNumber + " Sutton Avenue");
		// 220 wales, 179 scotland and 064 (england) are values
		sale.setCountry("179");
		sale.setCounty("Suffolk");
		sale.setESTAC(70);
		sale.setLocalAuthority("BA");
		sale.setPostcodeIncode("AB4");
		sale.setPostcodeOutcode("4EF");
		sale.setSaleDate(cal.getTime());
		sale.setUploadId((long)1);
		sale.setTown("London");
		sale.setWorkFlowStatus(WorkFlowStatus.RECEIVED.getValue());
		PropertyAttribute attr = new PropertyAttribute();
		attr = new PropertyAttribute();
		attr.setName(PropertyAttributeNames.estsegmentdescription.getValue());
		attr.setValue("Little Britain");
		sale.addPropertyAddressAttribute(attr);
		return sale;
	}

	public static PropertyEPC createPropertyEPC (String addressKey, String houseNumber) {

		Integer myInt = new Integer(houseNumber);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -5);
		cal.add(Calendar.MONTH, -myInt);
		cal.add(Calendar.YEAR, -1);
		PropertyEPC sale = new PropertyEPC(UUID.randomUUID());
		sale.setAddressKey(addressKey);
		sale.setAddressLine1(houseNumber + " Sutton Avenue");
		// 220 wales, 179 scotland and 064 (england) are values
		sale.setCountry("179");
		sale.setCounty("Suffolk");
		sale.setESTAC(70);
		sale.setLocalAuthority("BA");
		sale.setPostcodeIncode("AB4");
		sale.setPostcodeOutcode("4EF");

		sale.setUploadId((long)1);
		sale.setTown("London");
		sale.setWorkFlowStatus(WorkFlowStatus.RECEIVED.getValue());
		sale.setRating("A");
		sale.setInspectionDate(cal.getTime());
		PropertyAttribute attr = new PropertyAttribute();
		attr = new PropertyAttribute();
		attr.setName(PropertyAttributeNames.estsegmentdescription.getValue());
		attr.setValue("Little Britain");
		sale.addPropertyAddressAttribute(attr);
		return sale;
	}

}
