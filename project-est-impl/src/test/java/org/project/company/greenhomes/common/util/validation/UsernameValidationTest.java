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
public class UsernameValidationTest extends BaseSpringTestCase {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private ValangValidator userNameValangValidator;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT, SPRING_MVC_CONTEXT };
	}

	public void onSetUp () throws Exception {
		userNameValangValidator = (ValangValidator)applicationContext.getBean("userNameValangValidator");
		request = new MockHttpServletRequest();
		request.setMethod("GET");
		response = new MockHttpServletResponse();

	}

	/**
	 * Tests to see if validation is ok
	 *
	 * @throws Exception
	 */
	public void testUserNamePass () throws Exception {
		String username = "blah";
		User user = new User();
		user.setUserName(username);
		BindException errors = new BindException(user, "user");
		userNameValangValidator.validate(user, errors);
		assertEquals(0, errors.getErrorCount());
	}

	/**
	 * Tests to see if validation is ok
	 *
	 * @throws Exception
	 */
	public void testUserNameFailEmpty () throws Exception {
		String username = "";
		User user = new User();
		user.setUserName(username);
		BindException errors = new BindException(user, "user");
		userNameValangValidator.validate(user, errors);
		assertEquals(1, errors.getErrorCount());
		new ErrorsVerifier(errors).forProperty("userName").hasErrorCode("userName.req");
	}

	/**
	 * Tests to see if validation is ok
	 *
	 * @throws Exception
	 */
	public void testUserNamePassOddCharacter () throws Exception {
		String username = "blah_john";
		User user = new User();
		user.setUserName(username);
		BindException errors = new BindException(user, "user");
		userNameValangValidator.validate(user, errors);
		assertEquals(0, errors.getErrorCount());
	}

	/**
	 * Tests to see if validation is ok
	 *
	 * @throws Exception
	 */
	public void testUserNameFailOddCharacter () throws Exception {
		String username = "blah_john*";
		User user = new User();
		user.setUserName(username);
		BindException errors = new BindException(user, "user");
		userNameValangValidator.validate(user, errors);
		assertEquals(1, errors.getErrorCount());
		new ErrorsVerifier(errors).forProperty("userName").hasErrorCode("userName.invalid");
	}
}
