package org.project.company.greenhomes.common.filefilters;

import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.io.File;

public class TextFileFilterTest extends BaseSpringTestCase {

	//	private ExcelPropertySalesReader controller;
	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT };
	}

	public void onSetUp () throws Exception {
		//	controller = (ExcelPropertySalesReader)applicationContext.getBean(BeanNames.EXCEL_PROPERTY_SALES_READER.getValue());

	}

	public void testAccept () {
		String folder = "C:/dev/workspace/GreenHome/docs/landmark";
		TextFileFilter filter = new TextFileFilter();
		File folderToRead = new File(folder);
		// need to scan that directory for any files
		// and then move them to prevent any other access
		File[] filesInDir = folderToRead.listFiles(filter);
		System.out.println("filesInDir" + filesInDir.length);

	}

	//	public void testAcceptNull()
	//	{
	//		String name = "RdSAP-Recommendations.txt";
	//		LandMarkMeasuresFileFilter filter = new LandMarkMeasuresFileFilter();
	//		Boolean result = filter.accept(null, null);
	//		assertFalse(result);
	//	}
	//	public void testAcceptWrong()
	//	{
	//		String name = "RdSAP-Reons.txt";
	//		LandMarkMeasuresFileFilter filter = new LandMarkMeasuresFileFilter();
	//		Boolean result = filter.accept(null, name);
	//		assertFalse(result);
	//	}
	@Override
	protected void onTearDown () throws Exception {
		// delete all the data

	}

}
