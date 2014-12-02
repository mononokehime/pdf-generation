package org.project.company.greenhomes.service;

import org.project.company.greenhomes.common.enums.BeanNames;
import org.project.company.greenhomes.domain.entity.PropertyAddress;
import org.project.company.greenhomes.domain.entity.PropertySale;
import org.project.company.greenhomes.domain.model.ReferenceData;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.util.List;
import java.util.UUID;

/**
 * @author fmacder
 */
public class ReferenceDataServiceTest extends BaseSpringTestCase {

	private ReferenceDataService service;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT };
	}

	public void onSetUp () throws Exception {
		//DBUtil.insertData(getClass().getResourceAsStream("../../customer-data.xml"), BeanNames.CUSTOMER_DATA_SOURCE.getValue());
		service = (ReferenceDataService)applicationContext.getBean(BeanNames.REFERENCE_DATA_SERVICE.getValue());
	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data
		//deleteData(getClass().getClassLoader().getResourceAsStream("partial.xml"));	
	}

	/**
	 * tests to see if LocationData correctly returned
	 */
	public void testPopulateLocationDataWithInvertedComma () throws Exception {

		PropertyAddress address = new PropertySale(UUID.randomUUID());
		address.setPostcodeOutcode("N16");
		address.setPostcodeIncode("0HP");
		address.setAddressLine1("");
		address.setAddressLine2("51");
		address.setAddressLine3("QUEEN ELIZABETH'S CLOSE");
		PropertyAddress data = service.populateLocationData(address);

		assertNotNull(data);

		assertEquals("71", data.getESTAC().intValue() + "");
		assertEquals("00AM", data.getLocalAuthority());
		assertEquals("APRNL8FBGPK4ATW06R", data.getAddressKey());
	}

	/**
	 * tests to see if a ratings list is returned
	 */
	public void testFindRatingsList () throws Exception {

		List<ReferenceData> results = service.findRatingsList();
		assertNotNull(results);
		assertEquals(8, results.size());
		// make sure the first one is f,g		
		for (ReferenceData data : results) {
			assertEquals("F,G", data.getReferenceDataKey());
			break;
		}
	}
	//	/**
	//	 * tests to see if a treatments list is returned
	//	 */
	//	public void testFindTreatmentsList() throws Exception {
	//
	//		List<ReferenceData> results = service.findTreatmentsList();
	//		assertNotNull(results);
	//		assertEquals(3, results.size());
	//		// make sure the first one is f,g
	//		for(ReferenceData data: results)
	//		{
	//			assertEquals("", data.getReferenceDataKey());
	//			assertEquals("--", data.getLongName());
	//			break;
	//		}
	//	}

	/**
	 * tests to see if a countries list is returned
	 */
	public void testFindCountriesList () throws Exception {

		List<ReferenceData> results = service.findCountriesList();
		assertNotNull(results);
		assertEquals(3, results.size());
		// make sure the first one is f,g		
		for (ReferenceData data : results) {
			assertEquals("064", data.getReferenceDataKey());
			assertEquals("England", data.getLongName());
			break;
		}
	}

	/**
	 * tests to see if a estac list is returned
	 */
	public void testFindESTACList () throws Exception {

		List<ReferenceData> results = service.findESTACListByCountry("064");
		assertNotNull(results);
		assertEquals(30, results.size());
	}

	/**
	 * tests to see if a treatments list is returned
	 */
	public void testFindConsumerSegmentList () throws Exception {

		List<ReferenceData> results = service.findConsumerSegmentList();
		assertNotNull(results);
		assertEquals(12, results.size());
		// make sure the first one is f,g		
		for (ReferenceData data : results) {
			assertEquals("", data.getReferenceDataKey());
			assertEquals("--", data.getLongName());
			break;
		}
	}

	/**
	 * tests to see if a country and ESTAC are correctly returned
	 */
	public void testFindESTACListByCountry () throws Exception {

		String countryKey = "179";

		List<ReferenceData> results = service.findESTACListByCountry(countryKey);
		assertNotNull(results);
		assertEquals(6, results.size());
	}

	/**
	 * tests to see if a country and ESTAC are correctly returned
	 */
	public void testFindLASListByCountry () throws Exception {

		String countryKey = "179";

		List<ReferenceData> results = service.findLAListByCountry(countryKey);
		assertNotNull(results);
		assertEquals(32, results.size());

	}

	/**
	 * tests to see if LocationData correctly returned
	 */
	public void testLocationData () throws Exception {

		PropertyAddress address = new PropertySale();
		address.setPostcodeOutcode("WC1H");
		address.setPostcodeIncode("8JJ");
		address.setAddressLine1("11");
		address.setAddressLine2("Tangmere");

		PropertyAddress data = service.populateLocationData(address);
		System.out.println("address key:" + data.getAddressKey());
		assertNotNull(data);
		assertEquals("064", data.getCountry());
		assertEquals("71", data.getESTAC().intValue() + "");
		System.out.println("location data:" + data.getLocalAuthority());
		assertEquals("00AG", data.getLocalAuthority());
		assertEquals("AP5FHU817KX48UN08R", data.getAddressKey());
	}

	/**
	 * tests to see if LocationData correctly returned
	 */
	public void testLocationDataWithStreet () throws Exception {

		PropertyAddress address = new PropertySale(UUID.randomUUID());
		address.setPostcodeOutcode("DE15");
		address.setPostcodeIncode("9GW");
		address.setAddressLine2("14");
		address.setAddressLine3("WETHEREL ROAD");
		PropertyAddress data = service.populateLocationData(address);
		//		System.out.println("address key:"+data.getAddressKey());
		//		System.out.println("addressest:"+data.getESTAC());
		System.out.println("with street:" + data.getLocalAuthority());
		assertNotNull(data);

		assertEquals("27", data.getESTAC().intValue() + "");
		assertEquals("41UC", data.getLocalAuthority());
		assertEquals("AP4QEB8UVW65BH30BH", data.getAddressKey());
	}

}
