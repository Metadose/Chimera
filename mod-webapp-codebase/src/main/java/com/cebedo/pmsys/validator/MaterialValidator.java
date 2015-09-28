package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.helper.ValidationHelper;

@Component
public class MaterialValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return Material.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	if (errors.hasErrors()) {
	    this.validationHelper.rejectMissingRequiredFields(errors);
	    return;
	}
	Material targetObj = (Material) target;
	String name = targetObj.getName();

	// Name must not be blank.
	if (this.validationHelper.stringIsBlank(name)) {
	    this.validationHelper.rejectInvalidProperty(errors, "name");
	}
	// Name length = 64
	if (this.validationHelper.stringLengthIsGreaterThanMax(name, 64)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "name", 64);
	}
	// Quantity not negative.
	if (this.validationHelper.numberIsNegative(targetObj.getQuantity())) {
	    this.validationHelper.rejectNegativeNumber(errors, "quantity");
	}
	// Cost not negative.
	if (this.validationHelper.numberIsNegative(targetObj.getCostPerUnitMaterial())) {
	    this.validationHelper.rejectNegativeNumber(errors, "cost");
	}
	// Remarks length = 255
	if (this.validationHelper.stringLengthIsGreaterThanMax(targetObj.getRemarks(), 255)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "remarks", 255);
	}
    }
}
