package org.project.company.greenhomes.common.util.validation;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.project.company.greenhomes.common.validation.SearchValidator;
import org.project.company.greenhomes.domain.model.SearchParameters;
import org.project.company.greenhomes.test.common.ErrorsVerifier;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

/**
 * Commented out tests are for future functionality.
 *
 * @author fmacder
 */
public class SearchValidationTest extends BaseSpringTestCase {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private SearchValidator searchValidator;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT, SPRING_MVC_CONTEXT };
	}

	public void onSetUp () throws Exception {
		searchValidator = (SearchValidator)applicationContext.getBean("searchValidator");
		request = new MockHttpServletRequest();
		request.setMethod("GET");
		response = new MockHttpServletResponse();

	}

	/**
	 * Tests to see if validation is ok
	 *
	 * @throws Exception
	 */
	public void testIncorrectDateFormat () throws Exception {
		String country = "064";
		String fromDate = "03-30-2008";
		SearchParameters searchParameters = new SearchParameters();
		searchParameters.setCountry(country);
		searchParameters.setFromDate(fromDate);
		BindException errors = new BindException(searchParameters, "searchParameters");
		searchValidator.validate(searchParameters, errors);
		assertEquals(1, errors.getErrorCount());
		System.out.println("error" + errors);
		new ErrorsVerifier(errors).forProperty("fromDate").hasErrorCode("date.format.incorrect");
	}

	/**
	 * Tests to see if validation is ok
	 *
	 * @throws Exception
	 */
	public void testCorrectDateFormat () throws Exception {
		String country = "064";
		String fromDate = "30-03-2008";
		SearchParameters searchParameters = new SearchParameters();
		searchParameters.setCountry(country);
		searchParameters.setFromDate(fromDate);
		BindException errors = new BindException(searchParameters, "searchParameters");
		searchValidator.validate(searchParameters, errors);
		assertEquals(0, errors.getErrorCount());

	}
}
