package org.project.company.greenhomes.test.common;

import junit.framework.TestCase;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

public class ErrorsVerifier {
	private Errors errors;
	private String cursor; // one of successive properties

	public ErrorsVerifier (BindException errors) {
		this.errors = errors;
	}

	public ErrorsVerifier forProperty (String property) {
		cursor = property;
		return this;
	}

	public ErrorsVerifier hasErrorCode (String errorCode) {
		TestCase.assertEquals(errorCode, errors.getFieldError(cursor).getCode());
		return this;
	}

	public ErrorsVerifier doesNotHaveErrorCode (String errorCode) {
		FieldError error = errors.getFieldError(cursor);
		if (error == null) {
			TestCase.assertTrue(true);
		} else {
			TestCase.assertFalse(errorCode.equals(errors.getFieldError(cursor).getCode()));
		}
		return this;
	}
}
