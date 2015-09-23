package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Staff;

@Component
public class StaffValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return Staff.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	Staff staff = (Staff) target;

	// Prefix = 8
	String pfx = staff.getPrefix();
	if (!this.validationHelper.stringLengthIsLessThanMax(pfx, 8)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "prefix", 8);
	}

	// First = 32
	String first = staff.getFirstName();
	if (!this.validationHelper.stringLengthIsLessThanMax(first, 32)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "first name", 32);
	}

	// Middle = 16
	String middle = staff.getMiddleName();
	if (!this.validationHelper.stringLengthIsLessThanMax(middle, 16)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "middle name", 16);
	}

	// Last = 16
	String last = staff.getLastName();
	if (!this.validationHelper.stringLengthIsLessThanMax(last, 16)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "last name", 16);
	}

	// Suffix = 8
	String sfx = staff.getSuffix();
	if (!this.validationHelper.stringLengthIsLessThanMax(sfx, 8)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "suffix", 8);
	}

	// Company Position = 32
	String position = staff.getCompanyPosition();
	if (!this.validationHelper.stringLengthIsLessThanMax(position, 32)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "company position", 32);
	}

	// Salary (Daily) = not <0
	double wage = staff.getWage();
	if (!this.validationHelper.numberIsZeroOrPositive(wage)) {
	    this.validationHelper.rejectNegativeNumber(errors, "wage");
	}

	// E-Mail = 32
	String email = staff.getEmail();
	if (!this.validationHelper.stringLengthIsLessThanMax(email, 32)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "email", 32);
	}

	// E-Mail pattern
	if (!this.validationHelper.stringEmailIsValid(email)) {
	    this.validationHelper.rejectInvalidProperty(errors, "e-mail address");
	}

	// Contact Number = 32
	String nmber = staff.getContactNumber();
	if (!this.validationHelper.stringLengthIsLessThanMax(nmber, 32)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "contact number", 32);
	}

	if (!staff.isNameSet()) {
	    this.validationHelper.rejectInvalidProperty(errors, "name");
	}
    }

}
