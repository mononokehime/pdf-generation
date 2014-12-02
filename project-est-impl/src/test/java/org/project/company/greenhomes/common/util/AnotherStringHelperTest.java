package org.project.company.greenhomes.common.util;

import junit.framework.TestCase;

public class AnotherStringHelperTest extends TestCase {

	public void onSetUp () throws Exception {
		//	controller = (ExcelPropertySalesReader)applicationContext.getBean(BeanNames.EXCEL_PROPERTY_SALES_READER.getValue());

	}

	public void testRebuildStringWithNoBigSpacesNull () {
		//fail();
		String str = null;

		String result = AnotherStringHelper.rebuildStringWithNoBigSpaces(str);
		assertNotNull(result);
		assertEquals("", result);
	}

	public void testRebuildStringWithNoBigSpacesNoValue () {
		//fail();
		String str = "";
		String result = AnotherStringHelper.rebuildStringWithNoBigSpaces(str);
		assertNotNull(result);
		assertEquals("", result);
	}

	public void testRebuildStringWithNoBigSpaces () {
		//fail();
		String str = "1      ROPEWORKS";
		String result = AnotherStringHelper.rebuildStringWithNoBigSpaces(str);
		assertNotNull(result);
		assertEquals("1 ROPEWORKS", result);
	}

	public void testRebuildStringWithNoBigSpacesTwoBlanks () {
		//fail();
		String str = "28      CRANLEIGH      HOUSE";
		String result = AnotherStringHelper.rebuildStringWithNoBigSpaces(str);
		assertNotNull(result);
		assertEquals("28 CRANLEIGH HOUSE", result);
	}

	public void testRebuildStringWithNoBigSpacesAnotherTest () {
		//fail();
		String str = "28      CRANLEIGH HOUSE";
		String result = AnotherStringHelper.rebuildStringWithNoBigSpaces(str);
		assertNotNull(result);
		assertEquals("28 CRANLEIGH HOUSE", result);
	}

	public void testRebuildStringWithNoBigSpacesNoChange () {
		//fail();
		String str = "28";
		String result = AnotherStringHelper.rebuildStringWithNoBigSpaces(str);
		assertNotNull(result);
		assertEquals("28", result);
	}

}
