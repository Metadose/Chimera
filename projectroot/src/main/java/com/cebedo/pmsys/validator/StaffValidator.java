package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.constants.RegistryErrorCodes;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
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
	if (!this.validationHelper.checkLength(pfx, 8)) {
	    this.validationHelper.rejectLength(errors, "prefix", 8);
	}

	// First = 32
	String first = staff.getFirstName();
	if (!this.validationHelper.checkLength(first, 32)) {
	    this.validationHelper.rejectLength(errors, "first name", 32);
	}

	// Middle = 16
	String middle = staff.getMiddleName();
	if (!this.validationHelper.checkLength(middle, 16)) {
	    this.validationHelper.rejectLength(errors, "middle name", 16);
	}

	// Last = 16
	String last = staff.getLastName();
	if (!this.validationHelper.checkLength(last, 16)) {
	    this.validationHelper.rejectLength(errors, "last name", 16);
	}

	// Suffix = 8
	String sfx = staff.getSuffix();
	if (!this.validationHelper.checkLength(sfx, 8)) {
	    this.validationHelper.rejectLength(errors, "suffix", 8);
	}

	// Company Position = 32
	String position = staff.getCompanyPosition();
	if (!this.validationHelper.checkLength(position, 32)) {
	    this.validationHelper.rejectLength(errors, "company position", 32);
	}

	// Salary (Daily) = not <0
	double wage = staff.getWage();
	if (!this.validationHelper.zeroOrPositive(wage)) {
	    this.validationHelper.rejectZeroOrPositive(errors, "wage");
	}

	// E-Mail = 32, TODO pattern
	String email = staff.getEmail();
	if (!this.validationHelper.checkLength(email, 32)) {
	    this.validationHelper.rejectLength(errors, "email", 32);
	}

	// Contact Number = 32
	String nmber = staff.getContactNumber();
	if (!this.validationHelper.checkLength(nmber, 32)) {
	    this.validationHelper.rejectLength(errors, "contact number", 32);
	}

	if (!staff.isNameSet()) {
	    errors.reject(RegistryErrorCodes.COMMON_NAME,
		    RegistryResponseMessage.ERROR_COMMON_INVALID_NAME);
	}
    }

}
