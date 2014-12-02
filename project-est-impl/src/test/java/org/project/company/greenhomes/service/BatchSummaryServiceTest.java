package org.project.company.greenhomes.service;

import org.project.company.greenhomes.common.enums.BeanNames;
import org.project.company.greenhomes.domain.entity.Schedule;
import org.project.company.greenhomes.test.common.SqlPlusRunner;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Commented out tests are for future functionality.
 *
 * @author fmacder
 */
public class BatchSummaryServiceTest extends BaseSpringTestCase {

	private BatchSummaryAndScheduleService service;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT };
	}

	public void onSetUp () throws Exception {
		service = (BatchSummaryAndScheduleService)applicationContext
				.getBean(BeanNames.BATCH_SUMMARY_SERVICE.getValue());

	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data

	}

	public void testMarkScheduleComplete () throws Exception {
		insertTestData();
		Long scheduleId = 2l;
		String comment = "all done";
		service.markScheduleComplete(scheduleId, comment);
		endTransaction();
		Schedule schedule = service.findScheduleByid(scheduleId);
		assertNotNull(schedule);
		assertEquals(comment, schedule.getScheduleSummary());
		assertNotNull(schedule.getEndDate());
		deleteTestData();
	}
	//
	//	public void testFindLastXSummariesByType() throws Exception {
	//		insertTestData();
	//
	//
	//		//createScheduleAndUpdateProperties(List<Long> ids, Schedule schedule)
	//		List<UploadSummary> excelResults = service.findLastXSummariesByType(BatchTypes.PROPERTY_SALES, new Integer(5));
	//		assertNotNull(excelResults);
	//		assertEquals(5, excelResults.size());
	//		for (UploadSummary summary: excelResults)
	//		{
	//			assertEquals("2008-07-15 14:21:46.0", summary.getStartTime()+"");
	//
	//		}
	//		deleteTestData();
	//	}
	//	public void testCreateScheduleAndUpdateProperties() throws Exception {
	//		insertTestData();
	//		Schedule schedule = new Schedule();
	//		schedule.setRequestDate(new Date());
	//		schedule.setScheduleById(123l);
	//		schedule.setStartDate(new Date());
	//		schedule.setTemplateId(11+"");
	//
	//		//createScheduleAndUpdateProperties(List<Long> ids, Schedule schedule)
	//		service.createScheduleAndUpdateProperties(generatePropertyIdsList(), schedule);
	//		endTransaction();
	//	}
	//
	//	/**
	//	 * tests to see if batch results returned correctly
	//	 */
	//	public void testFindExcelBatchSummaries() throws Exception {
	//		Integer numberToReturn = new Integer(5);
	//		List<UploadSummary> results = service.findLastXSummariesByType(BatchTypes.PROPERTY_SALES, numberToReturn);
	//		assertNotNull(results);
	//		assertEquals(numberToReturn.intValue(), results.size());
	//		for (UploadSummary sum: results)
	//		{
	//			assertNotNull(sum);
	//		}
	//	}
	//
	//	/**
	//	 * tests to see if number of batch results returned correctly
	//	 */
	//	public void testFindNumberOfResultsByUploadId() throws Exception {
	//		long uploadId = 4042;
	//		Integer results = service.findNumberOfResultsByUploadId(uploadId);
	//		assertNotNull(results);
	//		//System.out.println("****************************results:"+results);
	//		assertEquals(5,results.intValue());
	//	}
	//	/**
	//	 * tests to see if number of schedules returned correctly
	//	 */
	//	public void testfindSchedulesToRun() throws Exception {
	//		List<Schedule> scheds = service.findSchedulesToRun();
	//		assertNotNull(scheds);
	//		//System.out.println("****************************results:"+results);
	//		assertEquals(1,scheds.size());
	//		deleteTestData();
	//	}

	private List<String> generatePropertyIdsList () {
		List<String> list = new ArrayList<String>();
		list.add(4043l + "");
		list.add(4045l + "");
		list.add(4047l + "");
		list.add(4049l + "");
		list.add(4051l + "");
		list.add(4063l + "");
		list.add(4065l + "");
		list.add(4067l + "");
		list.add(4127l + "");
		list.add(4129l + "");
		list.add(4131l + "");
		list.add(4133l + "");
		list.add(4135l + "");
		list.add(4147l + "");
		list.add(4149l + "");
		list.add(4151l + "");
		list.add(4153l + "");
		list.add(4167l + "");
		list.add(4169l + "");
		list.add(4171l + "");
		list.add(4173l + "");
		list.add(4175l + "");
		list.add(4189l + "");
		list.add(4191l + "");
		list.add(4193l + "");
		list.add(4195l + "");
		list.add(4197l + "");
		return list;
	}

	private void insertTestData () {
		SqlPlusRunner.runSQLPlusFile("upload_summary.sql");
		SqlPlusRunner.runSQLPlusFile("property_address.sql");
		SqlPlusRunner.runSQLPlusFile("property_address_attr.sql");
		//SqlPlusRunner.runSQLPlusFile("property_epc.sql");
		SqlPlusRunner.runSQLPlusFile("property_sale.sql");
		SqlPlusRunner.runSQLPlusFile("user.sql");
		SqlPlusRunner.runSQLPlusFile("schedule.sql");
	}

	private void deleteTestData () {
		SqlPlusRunner.runSQLPlusFile("truncate.sql");
	}
}
