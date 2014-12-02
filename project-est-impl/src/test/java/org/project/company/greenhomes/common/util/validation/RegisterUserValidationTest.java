package org.project.company.greenhomes.common.util.validation;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.project.company.greenhomes.common.validation.RegisterUserValidator;
import org.project.company.greenhomes.domain.entity.User;
import org.project.company.greenhomes.test.common.ErrorsVerifier;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

/**
 * Commented out tests are for future functionality.
 *
 * @author fmacder
 */
public class RegisterUserValidationTest extends BaseSpringTestCase {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private RegisterUserValidator registerUserValidator;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT, SPRING_MVC_CONTEXT };
	}

	public void onSetUp () throws Exception {
		registerUserValidator = (RegisterUserValidator)applicationContext.getBean("registerUserValidator");
		request = new MockHttpServletRequest();
		request.setMethod("GET");
		response = new MockHttpServletResponse();

	}

	/**
	 * Tests to see if validation is ok
	 *
	 * @throws Exception
	 */
	public void testUserPass () throws Exception {
		String username = "blaher";
		String emailAddress = "hello@hello.com";
		String firstName = "fergus";
		String familyName = "macdermot";
		User user = new User();
		user.setUserName(username);
		user.setEmailAddress(emailAddress);
		user.setFirstName(firstName);
		user.setFamilyName(familyName);
		String[] userRoleId = new String[1];
		userRoleId[0] = "SUPUER_USER";
		user.setUserRoleId(userRoleId);
		BindException errors = new BindException(user, "user");
		registerUserValidator.validate(user, errors);
		System.out.println(errors);
		assertEquals(0, errors.getErrorCount());
	}

	/**
	 * Tests to see if validation is ok
	 *
	 * @throws Exception
	 */
	public void testUserRoleFail () throws Exception {
		String username = "blaher";
		String emailAddress = "hello@hello.com";
		String firstName = "fergus";
		String familyName = "macdermot";
		User user = new User();
		user.setUserName(username);
		user.setEmailAddress(emailAddress);
		user.setFirstName(firstName);
		user.setFamilyName(familyName);
		String[] userRoleId = new String[0];
		//userRoleId[0] = "SUPUER_USER";
		user.setUserRoleId(userRoleId);
		BindException errors = new BindException(user, "user");
		registerUserValidator.validate(user, errors);
		System.out.println(errors);
		assertEquals(1, errors.getErrorCount());
		new ErrorsVerifier(errors).forProperty("userRoleId").hasErrorCode("role.req");
	}
}
