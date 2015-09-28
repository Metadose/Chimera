package com.cebedo.pmsys.validator;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.pojo.FormMassAttendance;

@Component
public class FormMassAttendanceValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return FormMassAttendance.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	if (errors.hasErrors()) {
	    this.validationHelper.rejectMissingRequiredFields(errors);
	    return;
	}
	FormMassAttendance targetObj = (FormMassAttendance) target;
	Date startDate = targetObj.getStartDate(); // Start date is after
	Date endDate = targetObj.getEndDate(); // End date
	double wage = targetObj.getWage(); // < Zero

	// If start date is > end date, error.
	if (endDate.before(startDate)) {
	    this.validationHelper.rejectInvalidDateRange(errors, "start date", "end date");
	}
	if (this.validationHelper.numberIsNegative(wage)) {
	    this.validationHelper.rejectNegativeNumber(errors, "wage");
	}
    }

}
