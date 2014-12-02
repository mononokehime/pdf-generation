package org.project.company.greenhomes.common.readers;

import org.project.company.greenhomes.common.enums.BeanNames;
import org.project.company.greenhomes.domain.entity.UploadSummary;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.service.BatchSummaryAndScheduleService;
import org.project.company.greenhomes.test.common.SqlPlusRunner;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.io.File;
import java.io.IOException;

public class LandMarkReaderTest extends BaseSpringTestCase {

	private LandMarkReader controller;
	private BatchSummaryAndScheduleService batchService;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT };
	}

	public void onSetUp () throws Exception {
		controller = (LandMarkReader)applicationContext.getBean(BeanNames.LAND_MARK_READER.getValue());
		batchService = (BatchSummaryAndScheduleService)applicationContext
				.getBean(BeanNames.BATCH_SUMMARY_SERVICE.getValue());
		//SqlPlusRunner.runSQLPlusFile("truncate.sql");
	}

	/**
	 *
	 */
	public void testUploadSameData () {
		// get the test directory so we can copy the file
		SqlPlusRunner.runSQLPlusFile("truncate.sql");
		String testDir = getProperty("data.output.location");
		String fileLocations = testDir + "/landmark-1/";
		File file = new File(fileLocations + "1237390631343EPC-Data_SAMPLE1.csv");
		try {
			controller.setFileUploadLocationLandMark(fileLocations);
			UploadSummary summary = controller.processFile(file);
			assertNotNull(summary);

			assertEquals("Number of rows", 1, summary.getNumberOfRows().intValue());
			assertEquals("Error count", 0, summary.getErrorCount().intValue());
			// should be good
			// now do the same again
			UploadSummary summary2 = controller.processFile(file);
			assertNotNull(summary2);

			assertEquals("Number of rows second", 1, summary2.getNumberOfRows().intValue());
			assertEquals("Error count second", 1, summary2.getErrorCount().intValue());
			// now we need to check that the earlier one is not a duplicate

			Integer results = batchService.findNumberOfResultsByUploadId(summary.getUploadSummaryId());
			assertNotNull(results);
			assertEquals("results", 1, results.intValue());

		} catch (IOException e) {
			fail();
		} catch (InvalidDataException e) {
			fail();
		}
	}
	//	/**
	//	 *
	//	 */
	//	public void testLoadLandMarkDataBadFileException()
	//	{
	//		// get the test directory so we can copy the file
	//
	//		String testDir = getProperty( "data.output.location");
	//		String fileLocations = testDir+"/landmark-bad/";
	//		File file = new File(fileLocations+"1237390631343EPC-Data_SAMPLE1.csv");
	//		try {
	//			controller.setFileUploadLocationLandMark(fileLocations);
	//			UploadSummary summary = controller.processFile(file);
	//			assertNotNull(summary);
	//
	//			assertEquals(11, summary.getNumberOfRows().intValue());
	//			assertEquals(0, summary.getErrorCount().intValue());
	//
	//		} catch (IOException e) {
	//			fail();
	//		}
	//		catch (InvalidDataException e)
	//		{
	//			// then copy the file back from errors directory
	//			File errorLocation  = new File(fileLocations+"errors");
	//			for (File erFile: errorLocation.listFiles()) {
	//				if (!erFile.isDirectory()) {
	//					assertTrue(erFile.renameTo(file));
	//				}
	//			}
	//		}
	//	}
	//	/**
	//	 *
	//	 */
	//	public void testLoadLandmarkData()
	//	{
	//		System.setProperty("PROCESS_INPUTS","true");
	//
	//		// we can over-ride the default value here to test.
	//
	//		String testDir = getProperty("data.output.location")+"/landmark";
	//		controller.setFileUploadLocationLandMark(testDir);
	//		File file = new File(testDir+"/1237390631343EPC-Data_SAMPLE1.csv");
	//		try
	//		{
	//			UploadSummary summary = controller.processFile(file);
	//			assertNotNull(summary);
	//			assertEquals(1+"",summary.getErrorCount()+"");
	//			assertTrue(summary.getFileRenameSucceeded());
	//		}
	//		catch (IOException e)
	//		{
	//		fail();
	//		}
	//		catch (InvalidDataException e)
	//		{
	//		fail();
	//		}
	//
	//	}

	@Override
	protected void onTearDown () throws Exception {
		//SqlPlusRunner.runSQLPlusFile("truncate.sql");

	}

}
