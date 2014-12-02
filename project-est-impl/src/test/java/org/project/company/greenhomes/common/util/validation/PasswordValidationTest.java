package org.project.company.greenhomes.common.util.validation;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springmodules.validation.valang.ValangValidator;
import org.project.company.greenhomes.domain.entity.User;
import org.project.company.greenhomes.domain.model.PasswordModel;
import org.project.company.greenhomes.test.common.ErrorsVerifier;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

/**
 * Commented out tests are for future functionality.
 *
 * @author fmacder
 */
public class PasswordValidationTest extends BaseSpringTestCase {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private ValangValidator userNameValangValidator;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT, SPRING_MVC_CONTEXT };
	}

	public void onSetUp () throws Exception {
		userNameValangValidator = (ValangValidator)applicationContext.getBean("passwordValangValidator");
		request = new MockHttpServletRequest();
		request.setMethod("GET");
		response = new MockHttpServletResponse();

	}

	/**
	 * Tests to see if validation is ok
	 *
	 * @throws Exception
	 */
	public void testOldPasswordPassTooLong () throws Exception {
		String username = "blah";
		User user = new User();
		user.setUserId(1l);
		PasswordModel password = new PasswordModel();
		password.setOldPassword("passwordsdfssdfd");
		password.setNewPassword("password");
		password.setNewPasswordConfirm("password");
		user.setPasswordModel(password);
		user.setUserName(username);
		BindException errors = new BindException(user, "user");
		userNameValangValidator.validate(user, errors);
		assertEquals(1, errors.getErrorCount());
		System.out.println("error" + errors);
		new ErrorsVerifier(errors).forProperty("passwordModel.oldPassword").hasErrorCode("password.newPassword.short");
	}

	/**
	 * Tests to see if validation is ok
	 *
	 * @throws Exception
	 */
	public void testPasswordPassTooLong () throws Exception {
		String username = "blah";
		User user = new User();
		user.setUserId(1l);
		PasswordModel password = new PasswordModel();
		password.setOldPassword("password");
		password.setNewPassword("passwpasswpassw");
		password.setNewPasswordConfirm("passwpasswpassw");
		user.setPasswordModel(password);
		user.setUserName(username);
		BindException errors = new BindException(user, "user");
		userNameValangValidator.validate(user, errors);
		assertEquals(1, errors.getErrorCount());
		System.out.println("error" + errors);
		new ErrorsVerifier(errors).forProperty("passwordModel.newPassword").hasErrorCode("password.newPassword.short");
	}

	/**
	 * Tests to see if validation is ok
	 *
	 * @throws Exception
	 */
	public void testPasswordPassTooChort () throws Exception {
		String username = "blah";
		User user = new User();
		user.setUserId(1l);
		PasswordModel password = new PasswordModel();
		password.setOldPassword("password");
		password.setNewPassword("passw");
		password.setNewPasswordConfirm("passw");
		user.setPasswordModel(password);
		user.setUserName(username);
		BindException errors = new BindException(user, "user");
		userNameValangValidator.validate(user, errors);
		assertEquals(1, errors.getErrorCount());
		System.out.println("error" + errors);
		new ErrorsVerifier(errors).forProperty("passwordModel.newPassword").hasErrorCode("password.newPassword.short");
	}

	/**
	 * Tests to see if validation is ok
	 *
	 * @throws Exception
	 */
	public void testPasswordPass () throws Exception {
		String username = "blah";
		User user = new User();
		user.setUserId(1l);
		PasswordModel password = new PasswordModel();
		password.setOldPassword("password");
		password.setNewPassword("password2");
		password.setNewPasswordConfirm("password2");
		user.setPasswordModel(password);
		user.setUserName(username);
		BindException errors = new BindException(user, "user");
		userNameValangValidator.validate(user, errors);
		assertEquals(0, errors.getErrorCount());
	}

}
