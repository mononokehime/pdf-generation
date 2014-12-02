package org.project.company.greenhomes.web.spring.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.project.company.greenhomes.domain.entity.User;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Commented out tests are for future functionality.
 *
 * @author fmacder
 */
public class UserManagementControllerTest extends BaseSpringTestCase {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private UserManagementController controller;
	private ProviderManager authenticationManager;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT, SPRING_MVC_CONTEXT };
	}

	public void onSetUp () throws Exception {

		controller = (UserManagementController)applicationContext.getBean("userManagementController");
		authenticationManager = (ProviderManager)applicationContext.getBean("_authenticationManager");

		request = new MockHttpServletRequest();
		request.setMethod("GET");
		response = new MockHttpServletResponse();
		//SqlPlusRunner.runSQLPlusFile("truncate_users.sql");
		//SqlPlusRunner.runSQLPlusFile("user.sql");			

	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data
		//deleteData(getClass().getClassLoader().getResourceAsStream("partial.xml"));	
		//SqlPlusRunner.runSQLPlusFile("truncate.sql");	
	}

	/**
	 * Tests to see if password reset ok
	 *
	 * @throws Exception
	 */
	public void testDisableUser () throws Exception {

		String userName = "mononoke6";
		String password = "password";
		Authentication auth = new UsernamePasswordAuthenticationToken(userName, password);
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(auth));

		User model = new User();
		//BindException errors = new BindException(model, "model");
		String user = "mono";
		String emailAddress = "";
		model.setUserName(user);
		model.setEmailAddress(emailAddress);
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(model.getEmailAddress())) {
			criteria.put("emailAddress", "%" + model.getEmailAddress() + "%");
		}
		if (StringUtils.isNotBlank(model.getUserName())) {
			criteria.put("userName", "%" + model.getUserName() + "%");
		}
		request.setMethod("POST");
		request.getSession().setAttribute("criteria", criteria);
		Long userId = new Long(69);
		Boolean active = new Boolean(false);
		ModelAndView mav = controller.changeUserStatus(userId, active, request);
		assertEquals("The returned url is not correct", "/user-management/search-users", mav.getViewName());
		assertNotNull(mav.getModelMap());
		ModelMap map = mav.getModelMap();
		List<User> models = (List<User>)map.get("models");
		mav.addObject("models", models);
		assertEquals(7, models.size());
		// now go through and check status has changed
		for (User users : models) {
			if (users.getUserId().longValue() == userId.longValue()) {
				System.out.println("users" + users.getUserId() + ", statuus:" + users.getActive());
				assertFalse("status is wrong", users.getActive());
			}
		}
	}
	//	/**
	//	 * Tests to see if password reset ok
	//	 * @throws Exception
	//	 */
	//	public void testSearchUser() throws Exception {
	//
	//		User model = new User();
	//		BindException errors = new BindException(model, "model");
	//		String userName = "mono";
	//		String emailAddress = "";
	//		model.setUserName(userName);
	//		model.setEmailAddress(emailAddress);
	//    	Map<String, Object> criteria = new HashMap<String, Object>();
	//    	if (StringUtils.isNotBlank(model.getEmailAddress()))
	//    	{
	//    		criteria.put("emailAddress", "%"+model.getEmailAddress()+"%");
	//    	}
	//    	if (StringUtils.isNotBlank(model.getUserName()) )
	//    	{
	//    		criteria.put("userName", "%"+model.getUserName()+"%");
	//    	}
	//    	request.setMethod("POST");
	//    	request.getSession().setAttribute("criteria", criteria);
	//    	ModelAndView mav = controller.searchUsers(model, errors, request);
	//    	assertEquals("The returned url is not correct", "/user-management/search-users", mav.getViewName());
	//		assertNotNull(mav.getModelMap());
	//		ModelMap map = mav.getModelMap();
	//		List<User> models = (List<User>)map.get("models");
	//    	mav.addObject("models",models);
	//    	assertEquals(7, models.size());
	//	}
	//	/**
	//	 * Tests to see if validation fails and correct path returned
	//	 * @throws Exception
	//	 */
	//	@ExpectedException(UsernameNotFoundException.class)
	//	public void testChangePasswordNotMatchNoUser() throws Exception {
	//		// no password should be ok, invalid is not
	//		//Validator validator = (Validator)applicationContext.getBean("registerUserValangValidator");
	//		String oldPassword = "blahasd";
	//		String newPassword = "password";
	//		String newPasswordConfirm = "password";
	//		User model = new User();
	//		model.setUserId((long)125673);
	//		PasswordModel pwdModel = new PasswordModel();
	//		pwdModel.setOldPassword(oldPassword);
	//		pwdModel.setNewPassword(newPassword);
	//		pwdModel.setNewPasswordConfirm(newPasswordConfirm);
	//		model.setPasswordModel(pwdModel);
	//		BindException errors = new BindException(model, "model");
	//
	//		ModelAndView mav = controller.changePassword(model, errors, request, response);
	//		assertNotNull(mav);
	//		// the failure url
	//		assertEquals("The returned url is not correct", "/user-management/change-password", mav.getViewName());
	//		System.out.println("errors:"+errors);
	//		assertEquals(1, errors.getErrorCount());
	//		// check some of the messages
	//
	//		new ErrorsVerifier(errors).forProperty("passwordModel.oldPassword").hasErrorCode("myPassword.oldPassword.does.not.match");
	//	}
	//	/**
	//	 * Tests to see if validation fails and correct path returned
	//	 * @throws Exception
	//	 */
	//	public void testChangePasswordNotMatch() throws Exception {
	//		// no password should be ok, invalid is not
	//		//Validator validator = (Validator)applicationContext.getBean("registerUserValangValidator");
	//		String oldPassword = "blahasd";
	//		String newPassword = "password";
	//		String newPasswordConfirm = "password";
	//		User model = new User();
	//		model.setUserId((long)123);
	//		PasswordModel pwdModel = new PasswordModel();
	//		pwdModel.setOldPassword(oldPassword);
	//		pwdModel.setNewPassword(newPassword);
	//		pwdModel.setNewPasswordConfirm(newPasswordConfirm);
	//		model.setPasswordModel(pwdModel);
	//		BindException errors = new BindException(model, "model");
	//
	//		ModelAndView mav = controller.changePassword(model, errors, request, response);
	//		assertNotNull(mav);
	//		// the failure url
	//		assertEquals("The returned url is not correct", "/user-management/change-password", mav.getViewName());
	//		System.out.println("errors:"+errors);
	//		assertEquals(1, errors.getErrorCount());
	//		// check some of the messages
	//
	//		new ErrorsVerifier(errors).forProperty("passwordModel.oldPassword").hasErrorCode("myPassword.oldPassword.does.not.match");
	//	}
	//
	//	/**
	//	 * Tests to see if validation fails and correct path returned
	//	 * @throws Exception
	//	 */
	//	public void testChangePasswordInvalidConfirm() throws Exception {
	//		// no password should be ok, invalid is not
	//		//Validator validator = (Validator)applicationContext.getBean("registerUserValangValidator");
	//		String oldPassword = "blah";
	//		String newPassword = "blaheere";
	//		String newPasswordConfirm = "blahss";
	//		User model = new User();
	//		model.setUserId((long)1);
	//		PasswordModel pwdModel = new PasswordModel();
	//		pwdModel.setOldPassword(oldPassword);
	//		pwdModel.setNewPassword(newPassword);
	//		pwdModel.setNewPasswordConfirm(newPasswordConfirm);
	//		model.setPasswordModel(pwdModel);
	//		BindException errors = new BindException(model, "model");
	//
	//		ModelAndView mav = controller.changePassword(model, errors, request, response);
	//		assertNotNull(mav);
	//		// the failure url
	//		assertEquals("The returned url is not correct", "/user-management/change-password", mav.getViewName());
	//		//System.out.println("errors:"+errors);
	//		// too short and not the same
	//		assertEquals(1, errors.getErrorCount());
	//		// check some of the messages
	//
	//		new ErrorsVerifier(errors).forProperty("passwordModel.newPasswordConfirm").hasErrorCode("password.newPasswordConfirm.no.match");
	//
	//	}
	//
	//
	//
	//
	//
	//	/**
	//	 * Tests to see if password reset ok
	//	 * @throws Exception
	//	 */
	//	public void testResetPassword() throws Exception {
	//		String userName = "mononoke6";
	//		String password = "password";
	//		Authentication auth = new UsernamePasswordAuthenticationToken(userName, password);
	//		SecurityContextHolder.getContext().setAuthentication(authenticationManager.doAuthentication(auth));
	//
	//		User user = new User();
	//		user.setUserName(userName);
	//		BindException errors = new BindException(user, "user");
	//		ModelAndView mav = controller.resetPassword(user, errors);
	//		assertNotNull(mav);
	//		assertEquals("The returned url is not correct", "/login/login", mav.getViewName());
	//		String message = (String)mav.getModelMap().get("message");
	//		assertNotNull(message);
	//	}
	//	/**
	//	 * Tests to see if validation fails
	//	 * @throws Exception
	//	 */
	//	public void testResetPasswordNoUser() throws Exception {
	//		String userName = "emptynamm";
	//		User user = new User();
	//		user.setUserName(userName);
	//		BindException errors = new BindException(user, "user");
	//		ModelAndView mav = controller.resetPassword(user, errors);
	//		assertNotNull(mav);
	//		assertEquals("The returned url is not correct", "/login/login", mav.getViewName());
	//		assertEquals(1, errors.getErrorCount());
	//		new ErrorsVerifier(errors).forProperty("userName").hasErrorCode("userName.not.found");
	//	}
	//
	//
	//	/**
	//	 * Tests to see if validation fails
	//	 * @throws Exception
	//	 */
	//	public void testResetPasswordFail() throws Exception {
	//		String userName = "mononoke6&";
	//		User user = new User();
	//		user.setUserName(userName);
	//		BindException errors = new BindException(user, "user");
	//		ModelAndView mav = controller.resetPassword(user, errors);
	//		assertNotNull(mav);
	//		assertEquals("The returned url is not correct", "/login/login", mav.getViewName());
	//		assertEquals(1, errors.getErrorCount());
	//		new ErrorsVerifier(errors).forProperty("userName").hasErrorCode("userName.invalid");
	//	}

}
