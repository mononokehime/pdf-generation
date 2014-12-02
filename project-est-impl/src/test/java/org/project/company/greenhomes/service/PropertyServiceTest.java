package org.project.company.greenhomes.service;

import org.springframework.test.annotation.ExpectedException;
import org.project.company.greenhomes.common.enums.BeanNames;
import org.project.company.greenhomes.common.enums.WorkFlowStatus;
import org.project.company.greenhomes.common.util.DateFormatter;
import org.project.company.greenhomes.domain.entity.Measure;
import org.project.company.greenhomes.domain.entity.PropertyAddress;
import org.project.company.greenhomes.domain.entity.PropertySale;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.test.common.SqlPlusRunner;
import org.project.company.greenhomes.test.common.mock.PropertyAddressUtil;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.util.List;

/**
 * Commented out tests are for future functionality.
 *
 * @author fmacder
 */
public class PropertyServiceTest extends BaseSpringTestCase {

	private PropertyService service;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT };
	}

	public void onSetUp () throws Exception {
		service = (PropertyService)applicationContext.getBean(BeanNames.PROPERTY_SALES_SERVICE.getValue());
		insertTestData();
	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data
		deleteTestData();

	}

	/**
	 * tests to see if measures returned correctly
	 */
	public void testFindNotValidAddressesByUploadId () throws Exception {

		// this is the uplaod id
		long processId = 4188;
		int startPoint = 0;
		int numberToReturn = 10;

		List<PropertyAddress> addresses = service
				.findNotValidAddressesByUploadId(processId, startPoint, numberToReturn);
		assertNotNull(addresses);
		assertEquals(2, addresses.size());
	}

	/**
	 * tests to see if measures returned correctly
	 */
	public void testFindMeasureByHeadingSummaryBoiler () throws Exception {
		String heading = "Band A condensing boiler";
		String summary = "Replace boiler with Band A condensing boiler";
		Measure measure = service.findMeasureByHeadingSummary(summary, heading);
		assertNotNull(measure);
		assertEquals(heading, measure.getHeading());
		assertEquals(summary, measure.getSummary());
		assertEquals(2, measure.getCategory().intValue());
		assertEquals(20, measure.getMeasureId().intValue());
	}

	/**
	 * tests to see if measures returned correctly
	 */
	public void testFindMeasureByHeadingSummary () throws Exception {
		String heading = "Heating controls (programmer, room thermostat and thermostatic radiator valves)";
		String summary = "Upgrade heating controls";
		Measure measure = service.findMeasureByHeadingSummary(summary, heading);
		assertNotNull(measure);
		assertEquals(heading, measure.getHeading());
		assertEquals(summary, measure.getSummary());
		assertEquals(1, measure.getCategory().intValue());
		assertEquals(11, measure.getMeasureId().intValue());
	}

	/**
	 * tests to see if exception returned correctly
	 */
	@ExpectedException(InvalidDataException.class)
	public void testFindMeasureByHeadingSummaryNull () throws Exception {
		String heading = "";
		String summary = "Replace single glazed windows with low-E double glazing";
		Measure measure = service.findMeasureByHeadingSummary(summary, heading);
		assertNotNull(measure);
		assertEquals(heading, measure.getHeading());
		assertEquals(summary, measure.getSummary());
		assertEquals(3, measure.getCategory().intValue());
		assertEquals(8, measure.getMeasureId().intValue());
	}

	/**
	 * tests to see if batch results returned correctly
	 */
	public void testInsertPropertySale () throws Exception {

		PropertySale sale = createPropertySale();
		PropertyAddress address = (PropertyAddress)sale;
		address = service.insertPropertyAddress(address);

		assertNotNull(address);
		assertNotNull(address.getPropertyAddressId());

	}

	/**
	 * tests to see if duplicate is inserted with duplicate value
	 */
	public void testCreateDuplicateSale () throws Exception {
		//	Long pk = new Long(4049);
		PropertySale address = createPropertySale();
		address.setAddressKey("ABCDEF0");
		// yyyy-MM-dd HH:mm:ss format is
		String dateStr = "2008-07-15 12:00:00";
		address.setSaleDate(DateFormatter.getFormattedDateFromLandReg(dateStr));

		PropertyAddress result = service.insertPropertyAddress(address);
		assertNotNull(result);
		assertEquals(WorkFlowStatus.DUPLICATE.getValue(), result.getWorkFlowStatus());

	}

	private PropertySale createPropertySale () {
		PropertySale address = PropertyAddressUtil.createPropertySale("dfgf", "22");
		//		address.setPostcodeIncode("7TT");
		//		address.setPostcodeOutcode("WC1H");
		//		address.setTown("London");
		//		address.setAddressLine1("an address");
		address.setWorkFlowStatus(WorkFlowStatus.RECEIVED.getValue());
		address.setUploadId(4031l);
		return address;
	}

	private void insertTestData () {
		SqlPlusRunner.runSQLPlusFile("measures.sql");
		SqlPlusRunner.runSQLPlusFile("upload_summary.sql");
		SqlPlusRunner.runSQLPlusFile("property_address.sql");
		SqlPlusRunner.runSQLPlusFile("property_address_attr.sql");
		SqlPlusRunner.runSQLPlusFile("property_epc.sql");
		SqlPlusRunner.runSQLPlusFile("property_measures.sql");
		SqlPlusRunner.runSQLPlusFile("property_sale.sql");
		SqlPlusRunner.runSQLPlusFile("user.sql");
	}

	private void deleteTestData () {
		SqlPlusRunner.runSQLPlusFile("truncate.sql");
	}
}
