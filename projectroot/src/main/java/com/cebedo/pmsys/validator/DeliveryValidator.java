package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.helper.ValidationHelper;

@Component
public class DeliveryValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return Delivery.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	Delivery targetObj = (Delivery) target;
	String name = targetObj.getName();

	// Name is not blank.
	if (this.validationHelper.stringIsBlank(name)) {
	    this.validationHelper.rejectInvalidProperty(errors, "name");
	}
	// Name length = 64
	if (this.validationHelper.stringLengthIsGreaterThanMax(name, 64)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "name", 64);
	}
	// Description length = 255
	if (this.validationHelper.stringLengthIsGreaterThanMax(targetObj.getDescription(), 255)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "description", 255);
	}
    }

}
