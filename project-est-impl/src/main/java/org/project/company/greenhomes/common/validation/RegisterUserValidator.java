package org.project.company.greenhomes.common.validation;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.validation.valang.ValangValidator;
import org.project.company.greenhomes.domain.entity.User;

public class RegisterUserValidator implements Validator {

	private ValangValidator registerUserValangValidator;

	public void setRegisterUserValangValidator (
			ValangValidator registerUserValangValidator) {
		this.registerUserValangValidator = registerUserValangValidator;
	}

	@SuppressWarnings("unchecked")
	public boolean supports (Class arg0) {

		return User.class.isAssignableFrom(arg0);
	}

	public void validate (Object arg0, Errors arg1) {
		User model = (User)arg0;
		// first check the basic validation has passed
		BindException errors = new BindException(model, "model");
		registerUserValangValidator.validate(arg0, errors);

		if (errors.hasErrors()) {
			arg1.addAllErrors(errors);
			//			return;
		}
		// now we need to check the role ids

		String[] rolesIds = model.getUserRoleId();
		if (null == rolesIds || rolesIds.length < 1) {
			// throw error
			arg1.rejectValue("userRoleId", "role.req", null, "Role name is required");
		}
	}

}