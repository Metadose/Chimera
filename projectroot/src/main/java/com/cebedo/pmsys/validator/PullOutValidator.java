package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.PullOut;

@Component
public class PullOutValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return PullOut.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

	PullOut obj = (PullOut) target;

	Material material = obj.getMaterial();

	// You are not allowed to pull-out
	// if quantity is greater than what is available.
	double quantity = obj.getQuantity();
	double available = material.getAvailable();

	// Error: Pullout more than the available.
	if (quantity > available) {
	    errors.reject("", RegistryResponseMessage.ERROR_PROJECT_PULLOUT_EXCEED);
	}
	// Error: Invalid quantity value.
	if (quantity <= 0) {
	    errors.reject("", RegistryResponseMessage.ERROR_COMMON_INVALID_QUANTITY);
	}
	// Error: Invalid date time.
	if (obj.getDatetime() == null) {
	    errors.reject("", RegistryResponseMessage.ERROR_COMMON_INVALID_DATE_TIME);
	}
	// Error: Pull out date is before the delivery date.
	if (obj.getDatetime().before(obj.getDelivery().getDatetime())) {
	    errors.reject("", RegistryResponseMessage.ERROR_PROJECT_PULLOUT_DATE_BEFORE_DELIVERY);
	}
	// Error: Etc.
	if (available <= 0) {
	    errors.reject("", RegistryResponseMessage.ERROR_COMMON_GENERIC);
	}
    }

}
