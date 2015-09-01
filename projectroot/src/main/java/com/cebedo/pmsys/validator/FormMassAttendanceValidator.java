package com.cebedo.pmsys.validator;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.constants.RegistryErrorCodes;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.pojo.FormMassAttendance;

@Component
public class FormMassAttendanceValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return FormMassAttendance.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	FormMassAttendance targetObj = (FormMassAttendance) target;
	Date startDate = targetObj.getStartDate(); // Start date is after
	Date endDate = targetObj.getEndDate(); // End date
	double wage = targetObj.getWage(); // < Zero

	// If start date is > end date, error.
	if (startDate.after(endDate)) {
	    errors.reject(RegistryErrorCodes.COMMON_DATES,
		    RegistryResponseMessage.ERROR_COMMON_START_DATE_GT_END_DATE);
	}
	if (wage < 0) {
	    errors.reject(RegistryErrorCodes.ATTENDANCE_WAGE,
		    RegistryResponseMessage.ERROR_PROJECT_STAFF_ATTENDANCE_NEGATIVE_WAGE);
	}
    }

}
