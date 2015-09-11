package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.helper.ValidationHelper;

@Component
public class AttendanceValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return Attendance.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	Attendance targetObj = (Attendance) target;
	double wage = targetObj.getWage(); // < Zero
	if (this.validationHelper.numberIsZeroOrPositive(wage)) {
	    this.validationHelper.rejectZeroOrPositive(errors, "wage");
	}
    }

}
