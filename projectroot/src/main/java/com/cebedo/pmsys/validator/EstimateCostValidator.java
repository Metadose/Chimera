package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.helper.ValidationHelper;

@Component
public class EstimateCostValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return EstimateCost.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	EstimateCost targetObj = (EstimateCost) target;
	String name = targetObj.getName();

	// Name is not blank.
	if (this.validationHelper.stringIsBlank(name)) {
	    this.validationHelper.rejectInvalidProperty(errors, "name");
	}
	// Name length = 64
	if (this.validationHelper.stringLengthIsGreaterThanMax(name, 64)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "name", 64);
	}
	// Cost not negative
	if (this.validationHelper.numberIsNegative(targetObj.getCost())) {
	    this.validationHelper.rejectNegativeNumber(errors, "estimated cost");
	}
	// Actual cost not negative
	if (this.validationHelper.numberIsNegative(targetObj.getActualCost())) {
	    this.validationHelper.rejectNegativeNumber(errors, "actual cost");
	}
    }

}
