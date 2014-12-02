package org.project.company.greenhomes.common.filefilters;

import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

public class LandmarkMeasuresFileFilterTest extends BaseSpringTestCase {

	//	private ExcelPropertySalesReader controller;
	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT };
	}

	public void onSetUp () throws Exception {
		//	controller = (ExcelPropertySalesReader)applicationContext.getBean(BeanNames.EXCEL_PROPERTY_SALES_READER.getValue());

	}

	public void testAccept () {
		String name = "RdSAP-Recommendations.txt";
		LandMarkMeasuresFileFilter filter = new LandMarkMeasuresFileFilter();
		Boolean result = filter.accept(null, name);
		assertTrue(result);
	}

	public void testAcceptNull () {
		String name = "RdSAP-Recommendations.txt";
		LandMarkMeasuresFileFilter filter = new LandMarkMeasuresFileFilter();
		Boolean result = filter.accept(null, null);
		assertFalse(result);
	}

	public void testAcceptWrong () {
		String name = "RdSAP-Reons.txt";
		LandMarkMeasuresFileFilter filter = new LandMarkMeasuresFileFilter();
		Boolean result = filter.accept(null, name);
		assertFalse(result);
	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data

	}

}
