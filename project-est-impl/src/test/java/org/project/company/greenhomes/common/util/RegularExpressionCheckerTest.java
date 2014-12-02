package org.project.company.greenhomes.common.util;

import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

public class RegularExpressionCheckerTest extends BaseSpringTestCase {

	public void onSetUp () throws Exception {
		//	controller = (ExcelPropertySalesReader)applicationContext.getBean(BeanNames.EXCEL_PROPERTY_SALES_READER.getValue());

	}

	public void testIsValidRRN () {
		String str = "1234-1234-1234-1234-1234";
		assertTrue(RegularExpressionChecker.isValidRRN(str));
	}

	public void testIsValidRRNNull () {
		String str = null;
		assertFalse(RegularExpressionChecker.isValidRRN(str));
	}

	public void testIsValidRRNEmpty () {
		String str = "";
		assertFalse(RegularExpressionChecker.isValidRRN(str));
	}

	public void testIsValidRRNFail () {
		String str = "1234-1234-1234-12341234";
		assertFalse(RegularExpressionChecker.isValidRRN(str));
	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data

	}

}
