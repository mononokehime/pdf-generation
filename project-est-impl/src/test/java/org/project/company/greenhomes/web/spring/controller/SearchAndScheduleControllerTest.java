package org.project.company.greenhomes.web.spring.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.project.company.greenhomes.common.enums.BeanNames;
import org.project.company.greenhomes.common.util.DateFormatter;
import org.project.company.greenhomes.domain.model.SearchParameters;
import org.project.company.greenhomes.service.query.QueryHolder;
import org.project.company.greenhomes.service.query.QueryHolder.DBFieldNames;
import org.project.company.greenhomes.test.common.SqlPlusRunner;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.*;

/**
 * Commented out tests are for future functionality.
 *
 * @author fmacder
 */
public class SearchAndScheduleControllerTest extends BaseSpringTestCase {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private HttpSession session;
	private SearchAndScheduleController controller;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT, SPRING_MVC_CONTEXT };
	}

	public void onSetUp () throws Exception {
		controller = (SearchAndScheduleController)applicationContext
				.getBean(BeanNames.SEARCH_AND_SCHEDULE_CONTROLLER.getValue());
		request = new MockHttpServletRequest();
		request.setMethod("GET");
		session = request.getSession();
		response = new MockHttpServletResponse();
		//insertTestData();
	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data
		//deleteData(getClass().getClassLoader().getResourceAsStream("partial.xml"));	
		//deleteTestData();
	}

	/**
	 * @throws Exception
	 */
	public void testViewPDF () throws Exception {
		//viewPDF(HttpServletResponse response,@RequestParam("propertyAddressId") String propertyAddressId  ) {

		deleteTestData();
		insertScheduledTestData();
		String propertyId = "4043";
		//BindException errors = new BindException(searchParameters, "searchParameters");		
		controller.viewPDF(response, propertyId);
		String contentType = response.getContentType();
		assertEquals("application/pdf", contentType);
		File f = new File("my.pdf");
		FileOutputStream fop = new FileOutputStream(f);
		fop.write(response.getContentAsByteArray());
		fop.flush();
		fop.close();

		System.out.println("finished");
		//deleteTestData();
		//endTransaction();
	}
	//	/**
	//	 *
	//	 * @throws Exception
	//	 */
	//	public void testScheduleDateFormatIncorrect() throws Exception {
	//		QueryHolder holder = new QueryHolder(1f);
	//
	//		request.getSession().setAttribute("holder", holder);
	//		String templateId = "value";
	//		String startDate = "01-13-2009";
	//		ModelAndView mav = controller.schedule(templateId, startDate, request, session);
	//		ModelMap map = mav.getModelMap();
	//
	//		List<ReferenceData> templates = (List<ReferenceData>)map.get("templates");
	//		assertNotNull(templates);
	//		assertEquals(1, templates.size());
	//		String message = "Please enter the date in format dd-mm-yyyy";
	//		String errMessage = (String)map.get("message");
	//		assertNotNull(errMessage);
	//		assertEquals(message, errMessage);
	//		// send back if this fails
	//		assertEquals("/search/results", mav.getViewName());
	//	}
	//
	//
	//	/**
	//	 *
	//	 * @throws Exception
	//	 */
	//	public void testPagerPageTooHigh() throws Exception {
	//		deleteTestData();
	//		insertTestData();
	//		SearchParameters searchParameters = new SearchParameters();
	//		String rating = "F,G";
	//		String country = "064";
	//		searchParameters.setRating(rating);
	//		searchParameters.setCountry(country);
	//		QueryHolder holder = populateSearchCriteria(searchParameters);
	//		holder.setSearchParameters(searchParameters);
	//		holder.setPropertyKeys(getPropertyKeys());
	//		holder.setTotalPages(3);
	//		holder.setResultsPerPage(3f);
	//		request.getSession().setAttribute("holder", holder);
	//
	//		ModelAndView mav = controller.pager(7, request);
	//		assertNotNull(mav);
	//		ModelMap map = mav.getModelMap();
	//		holder = (QueryHolder)map.get("holder");
	//		assertNotNull(holder);
	//		assertEquals(3, holder.getResults().size());
	//		// the failure url
	//		assertEquals("The returned url is not correct", "/search/results", mav.getViewName());
	//	}
	//	/**
	//	 *
	//	 * @throws Exception
	//	 */
	//	public void testPagerPageTooLow() throws Exception {
	//		deleteTestData();
	//		insertTestData();
	//		SearchParameters searchParameters = new SearchParameters();
	//		String rating = "F,G";
	//		String country = "064";
	//		searchParameters.setRating(rating);
	//		searchParameters.setCountry(country);
	//		QueryHolder holder = populateSearchCriteria(searchParameters);
	//		holder.setSearchParameters(searchParameters);
	//		holder.setPropertyKeys(getPropertyKeys());
	//		holder.setTotalPages(3);
	//		holder.setResultsPerPage(3f);
	//		request.getSession().setAttribute("holder", holder);
	//
	//		ModelAndView mav = controller.pager(0, request);
	//		assertNotNull(mav);
	//		ModelMap map = mav.getModelMap();
	//		holder = (QueryHolder)map.get("holder");
	//		assertNotNull(holder);
	//		assertEquals(3, holder.getResults().size());
	//		// the failure url
	//		assertEquals("The returned url is not correct", "/search/results", mav.getViewName());
	//	}
	//
	//	/**
	//	 *
	//	 * @throws Exception
	//	 */
	//	public void testPager() throws Exception {
	//		deleteTestData();
	//		insertTestData();
	//		SearchParameters searchParameters = new SearchParameters();
	//		String rating = "F,G";
	//		String country = "064";
	//		searchParameters.setRating(rating);
	//		searchParameters.setCountry(country);
	//		QueryHolder holder = populateSearchCriteria(searchParameters);
	//		holder.setSearchParameters(searchParameters);
	//		holder.setPropertyKeys(getPropertyKeys());
	//		holder.setTotalPages(3);
	//		holder.setResultsPerPage(3f);
	//		request.getSession().setAttribute("holder", holder);
	//
	//		ModelAndView mav = controller.pager(1, request);
	//		assertNotNull(mav);
	//		ModelMap map = mav.getModelMap();
	//		holder = (QueryHolder)map.get("holder");
	//		assertNotNull(holder);
	//		assertEquals(3, holder.getResults().size());
	//		// the failure url
	//		assertEquals("The returned url is not correct", "/search/results", mav.getViewName());
	//	}
	//
	//	/**
	//	 *
	//	 * @throws Exception
	//	 */
	//	public void testViewSchedule() throws Exception {
	//		deleteTestData();
	//		insertScheduledTestData();
	//		SearchParameters searchParameters = new SearchParameters();
	//		String rating = "F,G";
	//		String country = "064";
	//		searchParameters.setRating(rating);
	//		searchParameters.setCountry(country);
	//		searchParameters.setWorkflowStatus("scheduled");
	//		BindException errors = new BindException(searchParameters, "searchParameters");
	//		ModelAndView mav = controller.searchHistory(searchParameters, errors,request);
	//		assertNotNull(mav);
	//		ModelMap map = mav.getModelMap();
	//		QueryHolder holder = (QueryHolder)map.get("holder");
	//		assertNotNull(holder);
	//		assertEquals(14, holder.getPropertyKeys().size());
	//		System.out.println("size:"+holder.getResults().size());
	//		assertEquals(14, holder.getResults().size());
	//		// the failure url
	//		assertEquals("The returned url is not correct", "/search/history-results", mav.getViewName());
	//	}
	//	/**
	//	 *
	//	 * @throws Exception
	//	 */
	//	public void testSearchHistory() throws Exception {
	////		deleteTestData();
	////		insertScheduledTestData();
	//		SearchParameters searchParameters = new SearchParameters();
	//		String rating = "F,G";
	//		String country = "064";
	//		searchParameters.setRating(rating);
	//		searchParameters.setCountry(country);
	//		searchParameters.setWorkflowStatus("scheduled");
	//		BindException errors = new BindException(searchParameters, "searchParameters");
	//		ModelAndView mav = controller.searchHistory(searchParameters, errors,request);
	//		assertNotNull(mav);
	//		ModelMap map = mav.getModelMap();
	//		QueryHolder holder = (QueryHolder)map.get("holder");
	//		assertNotNull(holder);
	//		assertEquals(14, holder.getPropertyKeys().size());
	//		System.out.println("size:"+holder.getResults().size());
	//		assertEquals(14, holder.getResults().size());
	//		// the failure url
	//		assertEquals("The returned url is not correct", "/search/history-results", mav.getViewName());
	//	}
	//
	//
	//
	//
	//	/**
	//	 *
	//	 * @throws Exception
	//	 */
	//	public void testSearch() throws Exception {
	//		deleteTestData();
	//		insertTestData();
	//		SearchParameters searchParameters = new SearchParameters();
	//		String rating = "G";
	//		String country = "064";
	//		searchParameters.setRating(rating);
	//		searchParameters.setCountry(country);
	//		BindException errors = new BindException(searchParameters, "searchParameters");
	//		ModelAndView mav = controller.search(searchParameters, errors, request);
	//		assertNotNull(mav);
	//		ModelMap map = mav.getModelMap();
	//		QueryHolder holder = (QueryHolder)map.get("holder");
	//		assertNotNull(holder);
	//		assertEquals(5, holder.getPropertyKeys().size());
	//		// the failure url
	//		assertEquals("The returned url is not correct", "/search/results", mav.getViewName());
	//	}
	//
	//	public void testSearchFieldsPopulated() throws Exception {
	//		deleteTestData();
	//		insertTestData();
	//		SearchParameters searchParameters = new SearchParameters();
	//		String rating = "G";
	//		String country = "064";
	//		searchParameters.setRating(rating);
	//		searchParameters.setCountry(country);
	//		BindException errors = new BindException(searchParameters, "searchParameters");
	//		ModelAndView mav = controller.search(searchParameters, errors, request);
	//		assertNotNull(mav);
	//		ModelMap map = mav.getModelMap();
	//		QueryHolder holder = (QueryHolder)map.get("holder");
	//		assertNotNull(holder);
	//		assertNotNull(holder.getResults());
	//		assertEquals(5, holder.getPropertyKeys().size());
	//		// the failure url
	//		assertEquals("The returned url is not correct", "/search/results", mav.getViewName());
	//		// iterate through the collection and make sure the key bits are populated
	//		for (PropertyEPC epc: holder.getResults())
	//		{
	//			assertNotNull(epc);
	//			assertNotNull(epc.getAddressKey());
	//			assertNotNull(epc.getAddressLine1());
	//			assertNotNull(epc.getESTAC());
	//			assertNotNull(epc.getESTACName());
	//			assertNotNull(epc.getCountry());
	//			assertNotNull(epc.getCountryName());
	//			assertNotNull(epc.getRating());
	//			assertNotNull(epc.getInspectionDate());
	//		}
	//	}
	//

	private QueryHolder populateSearchCriteria (SearchParameters searchParameters) throws ParseException {
		QueryHolder holder = new QueryHolder((float)5);
		Map<DBFieldNames, Object> query = new HashMap<DBFieldNames, Object>();
		if (StringUtils.isNotBlank(searchParameters.getConsumerSegment())) {
			query.put(DBFieldNames.SEGMENTATION_VALUE, searchParameters.getConsumerSegment());
		}
		if (StringUtils.isNotBlank(searchParameters.getCountry())) {
			query.put(DBFieldNames.COUNTRY, searchParameters.getCountry());
		}
		if (StringUtils.isNotBlank(searchParameters.getESTAC())) {
			query.put(DBFieldNames.ESTAC, searchParameters.getESTAC());
		}
		if (StringUtils.isNotBlank(searchParameters.getRating())) {
			query.put(DBFieldNames.EPC_RATING, searchParameters.getRating());
		}
		if (StringUtils.isNotBlank(searchParameters.getWorkflowStatus())) {
			query.put(DBFieldNames.WORKFLOW, searchParameters.getWorkflowStatus());
		}
		//		if (StringUtils.isNotBlank(searchParameters.getTreatment()))
		//		{
		//			query.put(DBFieldNames.TREATMENT, searchParameters.getTreatment());
		//		}
		// now check for date parameters
		if (StringUtils.isNotBlank(searchParameters.getFromDate())) {
			query.put(DBFieldNames.FROM_DATE, DateFormatter.getFormattedDate(searchParameters.getFromDate()));
			// if the to date is null here, put in a fake one
			if (null == searchParameters.getToDate()) {
				query.put(DBFieldNames.TO_DATE, new Date());
			}
		}
		// if the from date is not null, then we populate anyway
		if (StringUtils.isNotBlank(searchParameters.getToDate())) {
			query.put(DBFieldNames.TO_DATE, DateFormatter.getFormattedDate(searchParameters.getToDate()));
			// now do other way round. If from date was null, then 
			// start from beginning of application date.
			if (null == searchParameters.getFromDate()) {
				query.put(DBFieldNames.FROM_DATE, DateFormatter.getFormattedDate("01/01/2007"));
			}

		}
		holder.setGenericQuery(query);
		return holder;
	}

	private List<String> getPropertyKeys () {
		List<String> keys = new ArrayList<String>();
		keys.add("4043");
		keys.add("4045");
		keys.add("4047");
		keys.add("4049");
		keys.add("4051");
		keys.add("4063");
		return keys;
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

	private void insertTestData () {
		SqlPlusRunner.runSQLPlusFile("user.sql");
		SqlPlusRunner.runSQLPlusFile("upload_summary.sql");
		SqlPlusRunner.runSQLPlusFile("property_address.sql");
		SqlPlusRunner.runSQLPlusFile("property_address_attr.sql");
		SqlPlusRunner.runSQLPlusFile("property_epc.sql");
		SqlPlusRunner.runSQLPlusFile("property_measures.sql");
		SqlPlusRunner.runSQLPlusFile("property_sale.sql");

	}

	private void deleteTestData () {
		SqlPlusRunner.runSQLPlusFile("truncate.sql");
	}
}
