package com.cebedo.pmsys.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.constants.RegistryErrorCodes;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.model.Staff;

public class StaffValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return Staff.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	Staff staff = (Staff) target;
	if (!staff.isNameSet()) {
	    errors.reject(RegistryErrorCodes.COMMON_NAME,
		    RegistryResponseMessage.ERROR_COMMON_INVALID_NAME);
	}
    }

}
