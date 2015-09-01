package com.cebedo.pmsys.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.constants.RegistryErrorCodes;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.model.assignment.FieldAssignment;

@Component
public class FieldAssignmentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return FieldAssignment.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	FieldAssignment targetObj = (FieldAssignment) target;
	// You cannot set an empty label.
	String label = targetObj.getLabel();
	String value = targetObj.getValue();

	if (StringUtils.isBlank(label) || StringUtils.isBlank(value)) {
	    errors.reject(RegistryErrorCodes.PROJECT_EXTRA_INFO,
		    RegistryResponseMessage.ERROR_PROJECT_EMPTY_EXTRA_INFO);
	}
    }

}
