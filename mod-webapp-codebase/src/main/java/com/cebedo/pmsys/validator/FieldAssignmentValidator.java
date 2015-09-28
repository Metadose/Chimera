package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.assignment.FieldAssignment;

@Component
public class FieldAssignmentValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return FieldAssignment.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	if (errors.hasErrors()) {
	    this.validationHelper.rejectMissingRequiredFields(errors);
	    return;
	}
	FieldAssignment targetObj = (FieldAssignment) target;
	// You cannot set an empty label.
	String label = targetObj.getLabel();
	String value = targetObj.getValue();

	// Label is not blank.
	if (this.validationHelper.stringIsBlank(label)) {
	    this.validationHelper.rejectInvalidProperty(errors, "label");
	}
	// Value is not blank.
	if (this.validationHelper.stringIsBlank(value)) {
	    this.validationHelper.rejectInvalidProperty(errors, "information");
	}
	// Label len = 32
	if (this.validationHelper.stringLengthIsGreaterThanMax(label, 32)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "label", 32);
	}
	// Value len = 255
	if (this.validationHelper.stringLengthIsGreaterThanMax(value, 255)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "information", 255);
	}
    }
}
