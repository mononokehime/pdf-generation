package org.project.company.greenhomes.service.query;

import org.project.company.greenhomes.common.enums.BeanNames;
import org.project.company.greenhomes.common.enums.WorkFlowStatus;
import org.project.company.greenhomes.common.util.DateFormatter;
import org.project.company.greenhomes.domain.entity.PropertyEPC;
import org.project.company.greenhomes.domain.entity.PropertySale;
import org.project.company.greenhomes.service.query.QueryHolder.DBFieldNames;
import org.project.company.greenhomes.test.common.SqlPlusRunner;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.util.*;

/**
 * Commented out tests are for future functionality.
 *
 * @author fmacder
 */
public class QueryEngineServiceTest extends BaseSpringTestCase {

	List<PropertyEPC> epcs = new ArrayList<PropertyEPC>();
	List<PropertySale> sales = new ArrayList<PropertySale>();
	private QueryEngineService service;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT };
	}

	public void onSetUp () throws Exception {
		service = (QueryEngineService)applicationContext.getBean(BeanNames.QUERY_ENGINE_SERVICE.getValue());
		//salesService = (PropertyService)applicationContext.getBean(BeanNames.PROPERTY_SALES_SERVICE.getValue());
		//summaryService = (BatchSummaryAndScheduleService)applicationContext.getBean(BeanNames.BATCH_SUMMARY_SERVICE.getValue());
		//deleteTestData();
		insertTestData();
	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data
		deleteTestData();
	}

	/**
	 * tests to see if property records can be returned, just with the country and estac
	 */
	public void testFindPropertiesForPDFsScheduleId () throws Exception {
		deleteTestData();
		insertScheduledTestData();
		QueryHolder holder = new QueryHolder((float)20);
		Map<DBFieldNames, Object> query = new HashMap<DBFieldNames, Object>();
		//		query.put(DBFieldNames.POSTCODE_INCODE, "AB4");
		//		query.put(DBFieldNames.POSTCODE_OUTCODE, "4EF");
		query.put(DBFieldNames.SCHEDULE_ID, "295747");

		holder.setGenericQuery(query);
		holder = service.findPropertiesForPDFsByCriteria(holder);
		assertNotNull(holder);
		assertNotNull(holder.getPropertyKeys());
		assertEquals(14, holder.getPropertyKeys().size());
		assertNotNull(holder.getResults());
		assertEquals(14, holder.getResults().size());
	}

	/**
	 * tests to see if property records can be returned, just with the country and estac
	 */
	public void testFindPropertiesForPDFsByCountryESTACDate () throws Exception {
		QueryHolder holder = new QueryHolder((float)20);
		Map<DBFieldNames, Object> query = new HashMap<DBFieldNames, Object>();
		//		query.put(DBFieldNames.POSTCODE_INCODE, "AB4");
		//		query.put(DBFieldNames.POSTCODE_OUTCODE, "4EF");
		query.put(DBFieldNames.COUNTRY, "220");
		query.put(DBFieldNames.ESTAC, new Long(71));

		//10/03/2008 14:21:47 10/03/2008 14:26:10
		//("dd-MMM-yy HH.mm")
		String myTime = "09-MAR-2008 12.12";
		Date time = DateFormatter.getFormattedDateFromLandMark(myTime);
		System.out.println("from date:" + time);
		query.put(DBFieldNames.FROM_DATE, time);
		myTime = "11-MAR-2008 12.12";
		time = DateFormatter.getFormattedDateFromLandMark(myTime);
		query.put(DBFieldNames.TO_DATE, time);
		query.put(DBFieldNames.EPC_RATING, "F,G");
		holder.setGenericQuery(query);
		holder = service.findPropertiesForPDFsByCriteria(holder);
		assertNotNull(holder);
		assertNotNull(holder.getPropertyKeys());
		assertEquals(1, holder.getPropertyKeys().size());
		assertNotNull(holder.getResults());
		assertEquals(1, holder.getResults().size());
	}

	/**
	 * tests to see if property records can be returned, just with the country and estac
	 */
	public void testFindPropertiesForPDFsByLocalAuthority () throws Exception {
		QueryHolder holder = new QueryHolder((float)20);
		Map<DBFieldNames, Object> query = new HashMap<DBFieldNames, Object>();
		//		query.put(DBFieldNames.POSTCODE_INCODE, "AB4");
		//		query.put(DBFieldNames.POSTCODE_OUTCODE, "4EF");
		query.put(DBFieldNames.COUNTRY, "064");
		query.put(DBFieldNames.LOCAL_AUTHORITY, "24UF");
		holder.setGenericQuery(query);
		holder = service.findPropertiesForPDFsByCriteria(holder);
		assertNotNull(holder);
		assertNotNull(holder.getPropertyKeys());
		assertEquals(14, holder.getPropertyKeys().size());
		assertNotNull(holder.getResults());
		assertEquals(14, holder.getResults().size());
		for (PropertyEPC epc : holder.getResults()) {
			System.out.println("sold:" + epc.getSaleDate());
		}
	}

	/**
	 * tests to see if property records can be returned, just with the country and estac
	 */
	public void testFindPropertiesForPDFsByCountryESTAC () throws Exception {
		QueryHolder holder = new QueryHolder((float)20);
		Map<DBFieldNames, Object> query = new HashMap<DBFieldNames, Object>();
		query.put(DBFieldNames.COUNTRY, "064");
		query.put(DBFieldNames.EPC_RATING, "F,G");
		query.put(DBFieldNames.WORKFLOW, WorkFlowStatus.RECEIVED.getValue());
		holder.setGenericQuery(query);
		holder = service.findPropertiesForPDFsByCriteria(holder);
		assertNotNull(holder);
		assertNotNull(holder.getPropertyKeys());
		assertEquals(14, holder.getPropertyKeys().size());
		//		for (String keys: holder.getPropertyKeys())
		//		{
		//			System.out.println("keys:"+keys);
		//		}
		//System.out.println("holder.getResults():"+holder.getResults().size());
		assertNotNull(holder.getResults());
		assertEquals(14, holder.getResults().size());
		//		List<PropertyEPC> results = holder.getResults();
		//		for (PropertyEPC epc: results)
		//		{
		//			assertNotNull(epc.getPropertyAddressAttributeSet());
		//		}
		//		//assertEquals(5, holder.getPropertyKeys().size());
		//		assertNotNull(holder.getResults());
		//		assertEquals(5, holder.getResults().size());
		//		// iterate results to make sure no lazy init session
		//		List<PropertyEPC> results = holder.getResults();
		//		for (PropertyEPC epc: results)
		//		{
		//			assertNotNull(epc.getPropertyAddressAttributeSet());
		//		}
	}

	/**
	 * tests to see if property records can be returned, just with the country and estac, and epc
	 */
	public void testFindPropertiesForPDFsByCountryESTACRating () throws Exception {
		//distinct(pa.PROPERTY_ADDRESS_ID)  from property_address pa , 
		//property_epc epc where   epc.PROPERTY_ADDRESS_ID = pa.PROPERTY_ADDRESS_ID  
		//epc.RATING in ( ?,? )  
		//and  pa.WORK_FLOW_STATUS = ?  and  pa.ESTAC = ?  and  pa.COUNTRY = ? ];
		QueryHolder holder = new QueryHolder((float)20);
		Map<DBFieldNames, Object> query = new HashMap<DBFieldNames, Object>();
		query.put(DBFieldNames.COUNTRY, "064");
		query.put(DBFieldNames.ESTAC, "72");
		query.put(DBFieldNames.EPC_RATING, "F,G");
		query.put(DBFieldNames.WORKFLOW, WorkFlowStatus.RECEIVED.getValue());
		holder.setGenericQuery(query);
		holder = service.findPropertiesForPDFsByCriteria(holder);
		assertNotNull(holder);
		assertNotNull(holder.getPropertyKeys());
		assertEquals(5, holder.getPropertyKeys().size());
		assertNotNull(holder.getResults());
		assertEquals(5, holder.getResults().size());
	}

	/**
	 * tests to see if property records can be returned, just with the country and estac
	 */
	public void testFindPropertiesForPDFsByCountryESTACSegmentation () throws Exception {
		QueryHolder holder = new QueryHolder((float)20);
		Map<DBFieldNames, Object> query = new HashMap<DBFieldNames, Object>();
		//		query.put(DBFieldNames.POSTCODE_INCODE, "AB4");
		//		query.put(DBFieldNames.POSTCODE_OUTCODE, "4EF");
		query.put(DBFieldNames.COUNTRY, "064");
		query.put(DBFieldNames.ESTAC, new Long(72));
		query.put(DBFieldNames.SEGMENTATION_VALUE, "Little Britain");
		holder.setGenericQuery(query);
		holder = service.findPropertiesForPDFsByCriteria(holder);
		assertNotNull(holder);
		assertNotNull(holder.getPropertyKeys());
		assertEquals(5, holder.getPropertyKeys().size());
		assertNotNull(holder.getResults());
		assertEquals(5, holder.getResults().size());
	}

	/**
	 * tests to see if property records can be returned, just with the country and estac
	 */
	public void testFindPropertiesForPDFsByCountryESTACSegmentationRating () throws Exception {
		QueryHolder holder = new QueryHolder((float)20);
		Map<DBFieldNames, Object> query = new HashMap<DBFieldNames, Object>();
		//		query.put(DBFieldNames.POSTCODE_INCODE, "AB4");
		//		query.put(DBFieldNames.POSTCODE_OUTCODE, "4EF");
		query.put(DBFieldNames.COUNTRY, "064");
		query.put(DBFieldNames.ESTAC, new Long(71));
		query.put(DBFieldNames.SEGMENTATION_VALUE, "Environmentally Mature");
		query.put(DBFieldNames.EPC_RATING, "F,G");
		holder.setGenericQuery(query);
		holder = service.findPropertiesForPDFsByCriteria(holder);
		assertNotNull(holder);
		assertNotNull(holder.getPropertyKeys());
		assertEquals(5, holder.getPropertyKeys().size());
		assertNotNull(holder.getResults());
		assertEquals(5, holder.getResults().size());
	}

	private void insertTestData () {
		SqlPlusRunner.runSQLPlusFile("upload_summary.sql");
		SqlPlusRunner.runSQLPlusFile("property_address.sql");
		SqlPlusRunner.runSQLPlusFile("property_address_attr.sql");
		SqlPlusRunner.runSQLPlusFile("property_epc.sql");
		SqlPlusRunner.runSQLPlusFile("property_sale.sql");
	}

	private void deleteTestData () {
		SqlPlusRunner.runSQLPlusFile("truncate.sql");
	}

	private void insertScheduledTestData () {
		SqlPlusRunner.runSQLPlusFile("upload_summary.sql");
		SqlPlusRunner.runSQLPlusFile("user.sql");
		SqlPlusRunner.runSQLPlusFile("property_address_scheduled.sql");
		SqlPlusRunner.runSQLPlusFile("property_address_attr.sql");
		SqlPlusRunner.runSQLPlusFile("property_epc.sql");
		SqlPlusRunner.runSQLPlusFile("property_measures_scheduled.sql");
		SqlPlusRunner.runSQLPlusFile("property_sale.sql");
	}
}
