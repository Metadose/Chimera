package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.SystemUser;

@Component
public class SystemUserValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return SystemUser.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	if (errors.hasErrors()) {
	    this.validationHelper.rejectMissingRequiredFields(errors);
	    return;
	}

	SystemUser systemUser = (SystemUser) target;

	String username = systemUser.getUsername();
	String password = systemUser.getPassword();
	String passwordRe = systemUser.getRetypePassword();

	// Required fields.
	if (this.validationHelper.stringIsBlank(username)) {
	    this.validationHelper.rejectInvalidProperty(errors, "username");
	}

	if (this.validationHelper.stringIsBlank(password)) {
	    this.validationHelper.rejectInvalidProperty(errors, "password");
	}

	if (this.validationHelper.stringIsBlank(passwordRe)) {
	    this.validationHelper.rejectInvalidProperty(errors, "re-type password");
	}

	// If the username and password are the same.
	if (username.equals(password)) {
	    this.validationHelper.rejectEqualStrings(errors, "username", "password");
	}

	// If the password and re-type password are not the same.
	if (!password.equals(passwordRe)) {
	    this.validationHelper.rejectNotEqualStrings(errors, "password", "re-type password");
	}

	// Check if the user name is valid.
	if (!this.validationHelper.stringUsernameIsValid(username)) {
	    this.validationHelper.rejectUsername(errors);
	}

	// Check if the password is valid.
	if (!this.validationHelper.stringPasswordIsValid(password)) {
	    this.validationHelper.rejectPassword(errors);
	}
    }
}
