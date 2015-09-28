package com.cebedo.pmsys.validator;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.helper.ValidationHelper;

@Component
public class PullOutValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return PullOut.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	if (errors.hasErrors()) {
	    this.validationHelper.rejectMissingRequiredFields(errors);
	    return;
	}

	PullOut obj = (PullOut) target;

	Material material = obj.getMaterial();

	// You are not allowed to pull-out
	// if quantity is greater than what is available.
	double quantity = obj.getQuantity();
	double available = material.getAvailable();
	Date deliveryDate = obj.getDelivery().getDatetime();
	Date pullOutDate = obj.getDatetime();
	String remarks = obj.getRemarks();

	// Error: Pullout more than the available.
	if (quantity > available) {
	    this.validationHelper.rejectInvalidNumbers(errors, "available materials",
		    "materials to pull-out");
	}
	// Error: Invalid quantity value.
	if (quantity <= 0) {
	    this.validationHelper.rejectInvalidProperty(errors, "quantity");
	}
	// Error: Pull out date is before the delivery date.
	if (pullOutDate.before(deliveryDate)) {
	    this.validationHelper.rejectInvalidDateRange(errors, "delivery date", "pull-out date");
	}
	// Remarks length = 255
	if (this.validationHelper.stringLengthIsGreaterThanMax(remarks, 255)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "remarks", 255);
	}
    }

}
