package org.project.company.greenhomes.web.spring.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.validation.valang.ValangValidator;
import org.project.company.greenhomes.common.util.WebApplicationContextHolder;
import org.project.company.greenhomes.common.validation.RegisterUserValidator;
import org.project.company.greenhomes.domain.entity.User;
import org.project.company.greenhomes.domain.entity.UserRole;
import org.project.company.greenhomes.domain.model.PasswordModel;
import org.project.company.greenhomes.exception.DuplicateUserException;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.exception.PasswordDoesNotMatchException;
import org.project.company.greenhomes.service.MailService;
import org.project.company.greenhomes.service.ReferenceDataService;
import org.project.company.greenhomes.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserManagementController {
	private static final Logger log = LoggerFactory.getLogger(UserManagementController.class);
	private RegisterUserValidator registerUserValidator;

	private ValangValidator passwordValangValidator;
	private ValangValidator userNameValangValidator;
	private ValangValidator userSearchValangValidator;
	private ReferenceDataService referenceDataService;
	/**
	 * the logger
	 */
	//private static final Logger log = LoggerFactory.getLogger(UserManagementController.class);

	private UserService userService;
	private MailService mailService;

	public void setMailService (MailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * Custom handler for the welcome view.
	 * Note that this handler relies on the RequestToViewNameTranslator to
	 * determine the logical view name based on the request URL: "/welcome.do"
	 * -> "welcome".
	 */

	@RequestMapping("/user-management/index")
	public void helloHandler () {

	}

	@RequestMapping("/login/login")
	public ModelAndView loginHandler (HttpSession session) {

		String message = (String)session.getAttribute("loginMessage");
		session.removeAttribute("loginMessage");
		ModelAndView modelView = new ModelAndView("/login/login");
		User model = new User();
		modelView.addObject("model", model);
		modelView.addObject("loginMessage", message);
		return modelView;
	}

	/**
	 * We can use this method for both preparing the adding and amending user
	 * page
	 *
	 * @param request
	 */
	@RequestMapping(value = "/user-management/add-user", method = RequestMethod.GET)
	public ModelAndView addAmendUserPrepare (@RequestParam(value = "userId", required = false) final Long userId,
			HttpServletRequest request) {
		//
		ModelAndView modelView = new ModelAndView("/user-management/add-user");

		User model = new User();
		model.setActive(true);

		// if there is a user id then populate the user
		if (null != userId) {
			model = userService.findUserByUserid(userId);
			// populate the user role ids.

			if (null != model.getUserRoleSet() && model.getUserRoleSet().size() > 0) {
				String[] userRoleId = new String[model.getUserRoleSet().size()];
				//populate the values
				int i = 0;
				for (UserRole role : model.getUserRoleSet()) {

					userRoleId[i] = role.getRoleName() + "";
					i++;
				}
				model.setUserRoleId(userRoleId);

			}

		} else {
			String roleId[] = { "ROLE_USER" };
			model.setUserRoleId(roleId);
		}

		modelView.addObject("model", model);
		modelView.addObject("roleMap", referenceDataService.getRolesMap());
		return modelView;

	}

	/**
	 * We can use this method for both  adding and amending user
	 * page
	 *
	 * @param model
	 * @param result
	 * @throws PasswordDoesNotMatchException
	 */
	@RequestMapping(value = "/user-management/add-user", method = RequestMethod.POST)
	public ModelAndView addAmendUser (@ModelAttribute("model") User model, BindingResult result)
			throws PasswordDoesNotMatchException {
		//
		ModelAndView modelView = null;
		// up to here the null checking is done for us, but we still need to validate
		registerUserValidator.validate(model, result);
		if (result.hasErrors()) {
			modelView = new ModelAndView("/user-management/add-user");
			modelView.addObject("model", model);
			modelView.addObject("roleMap", referenceDataService.getRolesMap());
			return modelView;
		}

		UserRole role = null;
		// sets can only have one in them so set up
		for (int i = 0; i < model.getUserRoleId().length; i++) {
			role = new UserRole();
			role.setRoleName(model.getUserRoleId()[i]);
			model.addUserRole(role);
		}

		// if the user id does not exist create, otherwise edit
		modelView = new ModelAndView("/user-management/add-user-confirm");
		if (null == model.getUserId()) {
			modelView.addObject("message", "created");
			try {
				model = userService.createUser(model);
			} catch (DuplicateUserException e) {
				result.rejectValue("userName", "userName.exists");
				modelView = new ModelAndView("/user-management/add-user");
				modelView.addObject("model", model);
				modelView.addObject("roleMap", referenceDataService.getRolesMap());
				return modelView;
			} catch (InvalidDataException e) {
				result.rejectValue("userName", "role.req");
				modelView = new ModelAndView("/user-management/add-user");
				modelView.addObject("model", model);
				modelView.addObject("roleMap", referenceDataService.getRolesMap());
				return modelView;
			}
		} else {
			modelView.addObject("message", "updated");

			model = userService.updateUser(model, false);
		}

		modelView.addObject("model", model);
		return modelView;

	}

	/**
	 *
	 */
	@RequestMapping(value = "/user-management/change-password", method = RequestMethod.GET)
	public ModelAndView changePassword () {
		// just return them to the view
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//
		User model = new User();
		model.setUserId(user.getUserId());
		PasswordModel myPassword = new PasswordModel();
		model.setPasswordModel(myPassword);
		ModelAndView modelView = new ModelAndView("/user-management/change-password");
		modelView.addObject("model", model);
		return modelView;

	}

	@RequestMapping(value = "/user-management/change-password", method = RequestMethod.POST)
	public ModelAndView changePassword (@ModelAttribute("model") User model, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {
		passwordValangValidator.validate(model, result);
		ModelAndView modelView = null;

		if (result.hasErrors()) {
			modelView = new ModelAndView("/user-management/change-password");
			modelView.addObject("model", model);
			return modelView;
		}
		String newPassword = null;
		try {
			// we need the new unencrypted password for authentication
			newPassword = model.getPasswordModel().getNewPassword();
			model = userService.updateUser(model, true);
		} catch (PasswordDoesNotMatchException e) {

			// in this case send them back with an error message
			result.rejectValue("passwordModel.oldPassword", "myPassword.oldPassword.does.not.match", null,
					"Password supplied does not match.");
			modelView = new ModelAndView("/user-management/change-password");
			modelView.addObject("model", model);
			return modelView;
		}
		// and re-authenticate
		reauthenticate(model, newPassword);

		modelView = new ModelAndView("/user-management/change-password-confirm");
		return modelView;
	}

	/**
	 *
	 */
	@RequestMapping(value = "/reset-password", method = RequestMethod.POST)
	public ModelAndView resetPassword (@ModelAttribute("model") User model, BindingResult result, HttpSession session) {
		// validate user first
		userNameValangValidator.validate(model, result);
		if (result.hasErrors()) {
			// return to the beginning
			ModelAndView modelView = new ModelAndView("/login/login");
			return modelView;
		}
		// resets a users password
		try {

			model = (User)userService.findUserByUserid(1l);
		} catch (UsernameNotFoundException e) {
			result.rejectValue("userName", "userName.not.found");
			ModelAndView modelView = new ModelAndView("/login/login");
			return modelView;
		}
		if (!model.getActive()) {
			result.rejectValue("userName", "invalid.login.credentials");
			ModelAndView modelView = new ModelAndView("/login/login");
			return modelView;
		}
		// now change user password to user name - first 6 character
		String newPwd = model.getUserName().substring(0, 6);
		// check the old and new match
		ShaPasswordEncoder sha = new ShaPasswordEncoder();
		//String pwd = sha.encodePassword(user.getUserName(), null);

		// now set the new password!
		String newPassword = sha.encodePassword(newPwd, null);
		model.setPassword(newPassword);

		// check exists etc
		model = userService.updatePassword(model);
		// now send them an email
		mailService.sendResetPasswordSuccessEmail(model, newPwd);
		session.setAttribute("message", "Your password has been reset.");
		return new ModelAndView("redirect:/login/login.do");

	}

	private void reauthenticate (User user, String newPassword) {

		Authentication auth = new UsernamePasswordAuthenticationToken(user.getUserName(), newPassword);
		ProviderManager authenticationManager = (ProviderManager)WebApplicationContextHolder.getAuthenticationManager();
		//SecurityContextHolder.getContext().
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(auth));

	}

	/**
	 */
	@RequestMapping(value = "/user-management/search-users", method = RequestMethod.GET)
	public void searchUsers () {
		// just return them to the view
	}

	/**
	 * @param request
	 */
	@RequestMapping(value = "/user-management/search-users", method = RequestMethod.POST)
	public ModelAndView searchUsers (@ModelAttribute("model") User model, BindingResult result,
			HttpServletRequest request) {

		ModelAndView mav = new ModelAndView("/user-management/search-users");
		// lets validate
		// validate user first
		userSearchValangValidator.validate(model, result);
		if (result.hasErrors()) {
			// return to the beginning
			mav.addObject("model", model);
			return mav;
		}
		// pull the user out
		// we are only allowing email and username
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(model.getEmailAddress())) {
			criteria.put("emailAddress", "%" + model.getEmailAddress() + "%");
		}
		if (StringUtils.isNotBlank(model.getUserName())) {
			criteria.put("userName", "%" + model.getUserName() + "%");
		}
		// save the criteria for later
		request.getSession().setAttribute("criteria", criteria);
		List<User> models = userService.findUsersByCriteria(criteria);
		mav.addObject("models", models);
		return mav;

	}

	/**
	 * We can use this method for both  adding and amending user
	 * page
	 *
	 * @param request
	 * @throws PasswordDoesNotMatchException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/user-management/change-user-status", method = RequestMethod.GET)
	public ModelAndView changeUserStatus (@RequestParam(value = "userId", required = true) final Long userId,
			@RequestParam(value = "active", required = true) final Boolean active, HttpServletRequest request)
			throws PasswordDoesNotMatchException {

		// pull the user out
		User model = new User();
		model.setUserId(userId);
		model.setActive(active);

		model = userService.updateUserStatus(model);
		// if this is the same user then we need to log them out! hohoho.
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.getUserName().equals(model.getUserName())) {
			request.getSession().setAttribute("message", "Your account has been disabled");
			return new ModelAndView("redirect:/j_spring_security_logout");
		}
		// go back to search results
		Map<String, Object> criteria = (Map<String, Object>)request.getSession().getAttribute("criteria");
		List<User> models = userService.findUsersByCriteria(criteria);
		//log.debug("model:"+models.size());
		ModelAndView mav = new ModelAndView("/user-management/search-users");
		mav.addObject("models", models);
		return mav;

	}

	public void setUserService (UserService userService) {
		this.userService = userService;
	}

	public void setReferenceDataService (ReferenceDataService referenceDataService) {
		this.referenceDataService = referenceDataService;
	}

	public void setPasswordValangValidator (ValangValidator passwordValangValidator) {
		this.passwordValangValidator = passwordValangValidator;
	}

	public void setUserNameValangValidator (ValangValidator userNameValangValidator) {
		this.userNameValangValidator = userNameValangValidator;
	}

	public void setUserSearchValangValidator (
			ValangValidator userSearchValangValidator) {
		this.userSearchValangValidator = userSearchValangValidator;
	}

	public void setRegisterUserValidator (RegisterUserValidator registerUserValidator) {
		this.registerUserValidator = registerUserValidator;
	}

}
