package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.helper.ValidationHelper;

@Component
public class ExpenseValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return Expense.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	Expense targetObj = (Expense) target;
	String name = targetObj.getName();

	// Name is not blank.
	if (this.validationHelper.stringIsBlank(name)) {
	    this.validationHelper.rejectInvalidProperty(errors, "name");
	}
	// Name length = 64
	if (this.validationHelper.stringLengthIsGreaterThanMax(name, 64)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "name", 64);
	}
	// Cost is not negative
	if (this.validationHelper.numberIsNegative(targetObj.getCost())) {
	    this.validationHelper.rejectNegativeNumber(errors, "cost");
	}
    }

}
