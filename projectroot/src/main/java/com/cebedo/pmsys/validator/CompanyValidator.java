package com.cebedo.pmsys.validator;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Company;

@Component
public class CompanyValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return Company.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

	Company targetObj = (Company) target;
	Date now = new Date(System.currentTimeMillis());
	Date expire = targetObj.getDateExpiration();
	Date start = targetObj.getDateStarted();
	String name = targetObj.getName();
	String description = targetObj.getDescription();

	if (!this.validationHelper.checkLength(name, 64)) {
	    this.validationHelper.rejectLength(errors, "name", 64);
	}
	if (!this.validationHelper.checkLength(description, 255)) {
	    this.validationHelper.rejectLength(errors, "description", 255);
	}

	// Invalid name.
	if (this.validationHelper.checkBlank(name)) {
	    this.validationHelper.rejectInvalid(errors, "name");
	}
	// Expiration is before now.
	if (expire.before(now)) {
	    this.validationHelper.rejectDateRange(errors, "current date", "expiration date");
	}
	// Expiration is before start.
	if (expire.before(start)) {
	    this.validationHelper.rejectDateRange(errors, "start date", "expiration date");
	}
    }

}
