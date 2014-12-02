package org.project.company.greenhomes.common.util;

import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class FileIOExtrasTest extends BaseSpringTestCase {

	//	private ExcelPropertySalesReader controller;

	public void onSetUp () throws Exception {
		//	controller = (ExcelPropertySalesReader)applicationContext.getBean(BeanNames.EXCEL_PROPERTY_SALES_READER.getValue());

	}

	/**
	 *
	 */
	public void testReadLandRegistryFileFromFileSystem () {
		// get the test directory so we can copy the file
		String testDir = getProperty("data.output.location");
		//		// first of all copy file over to expected location
		File in = new File(testDir + "/landregistry/new-land-reg-extract.txt");
		File out = new File(testDir + "/landregistry/completed/new-land-reg-extract.txt");
		File outDir = new File(testDir + "/landregistry/completed/");
		File[] filesLeft = null;
		try {
			FileIOExtras.copyFile(in, out);
			filesLeft = outDir.listFiles(new TxtFileFilter());

			assertEquals(1, filesLeft.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			// now delete all if file
			filesLeft = outDir.listFiles();
			for (File file : filesLeft) {
				if (file.isFile()) {
					file.delete();
				}
			}

		}

	}
	//	/**
	//	 * checks to see correct exception thrown when null value passed
	//	 */
	//	@ExpectedException(InvalidParameterException.class)
	//	public void testbuildNewFileNameNulls()
	//	{
	//		//fail();
	//		String dir = null;
	//		File file = new File(dir+"/mytestfile.xls");
	//
	//		File newFile = FileIOExtras.buildNewFileName(file, dir);
	//		assertNotNull(newFile);
	//	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data

	}

	/**
	 * Makes sure only files containing .txt are returned.
	 *
	 *
	 */

	public class TxtFileFilter implements FilenameFilter {

		public boolean accept (File dir, String name) {
			if (null == name) {
				return false;
			}
			return name.endsWith(".txt");
		}

	}

}
