package org.project.company.greenhomes.common.util;

import org.springframework.test.annotation.ExpectedException;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.security.InvalidParameterException;

public class MathsHelperTest extends BaseSpringTestCase {

	public void onSetUp () throws Exception {
		//	controller = (ExcelPropertySalesReader)applicationContext.getBean(BeanNames.EXCEL_PROPERTY_SALES_READER.getValue());

	}

	@ExpectedException(InvalidParameterException.class)
	public void testGetPercentFromStringsNull () {
		//fail();
		String val1 = null;
		String val2 = "100";
		Float result = MathsHelper.getPercentFromStrings(val1, val2);
		assertNotNull(result);
		assertEquals("0.5", result + "");
	}

	@ExpectedException(InvalidParameterException.class)
	public void testGetPercentFromStringsInvalidNumber () {
		//fail();
		String val1 = "ddd";
		String val2 = "100";
		Float result = MathsHelper.getPercentFromStrings(val1, val2);
		assertNotNull(result);
		assertEquals("0.5", result + "");
	}

	public void testGetPercentFromStrings () {
		//fail();
		String val1 = "50";
		String val2 = "100";
		Float result = MathsHelper.getPercentFromStrings(val1, val2);
		assertNotNull(result);
		assertEquals("0.5", result + "");
	}

	@ExpectedException(InvalidParameterException.class)
	public void testGetPercentValueNull () {
		//fail();
		Float val1 = null;
		Float val2 = new Float("100");
		Float result = MathsHelper.getPercentValue(val1, val2);
		assertNotNull(result);
		assertEquals("50.0", result + "");
	}

	public void testGetPercentValue () {
		//fail();
		Float val1 = new Float("0.5");
		Float val2 = new Float("100");
		Float result = MathsHelper.getPercentValue(val1, val2);
		assertNotNull(result);
		assertEquals("50.0", result + "");
	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data

	}

}
