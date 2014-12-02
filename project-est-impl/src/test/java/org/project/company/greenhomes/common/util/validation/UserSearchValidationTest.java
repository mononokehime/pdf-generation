package org.project.company.greenhomes.common.util.validation;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springmodules.validation.valang.ValangValidator;
import org.project.company.greenhomes.domain.entity.User;
import org.project.company.greenhomes.test.common.ErrorsVerifier;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

/**
 * Commented out tests are for future functionality.
 *
 * @author fmacder
 */
public class UserSearchValidationTest extends BaseSpringTestCase {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private ValangValidator userSearchValangValidator;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT, SPRING_MVC_CONTEXT };
	}

	public void onSetUp () throws Exception {
		userSearchValangValidator = (ValangValidator)applicationContext.getBean("userSearchValangValidator");
		request = new MockHttpServletRequest();
		request.setMethod("GET");
		response = new MockHttpServletResponse();

	}

	/**
	 * Tests to see if validation is ok
	 *
	 * @throws Exception
	 */
	public void testFieldsPass () throws Exception {
		String username = "blaher";
		String emailAddress = "hello@hello.com";
		User user = new User();
		user.setUserName(username);
		user.setEmailAddress(emailAddress);
		BindException errors = new BindException(user, "user");
		userSearchValangValidator.validate(user, errors);
		System.out.println(errors);
		assertEquals(0, errors.getErrorCount());
	}

	/**
	 * Tests to see if validation fails
	 *
	 * @throws Exception
	 */
	public void testFieldsEmpty () throws Exception {
		String username = "";
		String emailAddress = "";
		User user = new User();
		user.setUserName(username);
		user.setEmailAddress(emailAddress);
		BindException errors = new BindException(user, "user");
		userSearchValangValidator.validate(user, errors);
		assertEquals(1, errors.getErrorCount());

		new ErrorsVerifier(errors).forProperty("userName").hasErrorCode("user.search.field.req");
	}

	/**
	 * Tests to see if validation fails
	 *
	 * @throws Exception
	 */
	public void testFieldsNotEmail () throws Exception {
		String username = "";
		String emailAddress = "gdfgdfgdf";
		User user = new User();
		user.setUserName(username);
		user.setEmailAddress(emailAddress);
		BindException errors = new BindException(user, "user");
		userSearchValangValidator.validate(user, errors);
		assertEquals(1, errors.getErrorCount());

		new ErrorsVerifier(errors).forProperty("emailAddress").hasErrorCode("email.invalid");
	}

	/**
	 * Tests to see if validation fails
	 *
	 * @throws Exception
	 */
	public void testFieldsInvalidName () throws Exception {
		String username = "TTBBDD%";
		String emailAddress = null;
		User user = new User();
		user.setUserName(username);
		user.setEmailAddress(emailAddress);
		BindException errors = new BindException(user, "user");
		userSearchValangValidator.validate(user, errors);
		assertEquals(1, errors.getErrorCount());
		System.out.println(errors);
		new ErrorsVerifier(errors).forProperty("userName").hasErrorCode("userName.invalid");
	}
}
