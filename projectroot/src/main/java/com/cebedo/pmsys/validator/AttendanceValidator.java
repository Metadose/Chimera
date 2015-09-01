package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.domain.Attendance;

@Component
public class AttendanceValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return Attendance.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	Attendance targetObj = (Attendance) target;
	double wage = targetObj.getWage(); // < Zero
	if (wage < 0) {
	    errors.reject("invalid.attendance.wage",
		    RegistryResponseMessage.ERROR_PROJECT_STAFF_ATTENDANCE_NEGATIVE_WAGE);
	}
    }

}
