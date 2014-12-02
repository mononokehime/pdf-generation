package org.project.company.greenhomes.common.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.validation.valang.ValangValidator;
import org.project.company.greenhomes.common.util.DateFormatter;
import org.project.company.greenhomes.domain.model.SearchParameters;

import java.text.ParseException;

public class SearchValidator implements Validator {

	private ValangValidator searchValangValidator;

	public void setSearchValangValidator (ValangValidator searchValangValidator) {
		this.searchValangValidator = searchValangValidator;
	}

	@SuppressWarnings("unchecked")
	public boolean supports (Class arg0) {

		return SearchParameters.class.isAssignableFrom(arg0);
	}

	public void validate (Object arg0, Errors arg1) {
		SearchParameters searchParameters = (SearchParameters)arg0;
		// first check the basic validation has passed
		BindException errors = new BindException(searchParameters, "searchParameters");
		searchValangValidator.validate(arg0, errors);
		if (errors.hasErrors()) {
			arg1.addAllErrors(errors);
		}

		//if that's ok, then check the date parameters
		if (StringUtils.isNotBlank(searchParameters.getFromDate())) {
			// check can be parsed in correct format
			try {
				DateFormatter.getFormattedDate(searchParameters.getFromDate());

			} catch (ParseException e) {
				arg1.rejectValue("fromDate", "date.format.incorrect", null,
						"Please enter the date in format dd-mm-yyyy");
			}
		}
		if (StringUtils.isNotBlank(searchParameters.getToDate())) {
			// check can be parsed in correct format
			try {
				DateFormatter.getFormattedDate(searchParameters.getToDate());
			} catch (ParseException e) {
				arg1.rejectValue("toDate", "date.format.incorrect", null, "Please enter the date in format dd-mm-yyyy");
			}
		}

	}

}