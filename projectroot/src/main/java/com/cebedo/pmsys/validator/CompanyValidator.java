package com.cebedo.pmsys.validator;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.constants.RegistryErrorCodes;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Company;

@Component
public class CompanyValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return Company.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	Company targetObj = (Company) target;
	Date now = new Date(System.currentTimeMillis());
	Date expire = targetObj.getDateExpiration();
	Date start = targetObj.getDateStarted();

	String name = targetObj.getName();
	String description = targetObj.getDescription();

	if (!this.validationHelper.checkLength(name, 64)) {
	    this.validationHelper.rejectLength(errors, "name", 64);
	}
	if (!this.validationHelper.checkLength(description, 255)) {
	    this.validationHelper.rejectLength(errors, "description", 255);
	}

	// Invalid name.
	if (StringUtils.isBlank(targetObj.getName())) {
	    errors.reject(RegistryErrorCodes.COMPANY_EXPIRATION,
		    RegistryResponseMessage.ERROR_COMMON_INVALID_NAME);
	}
	// Expiration is before now.
	if (expire.before(now)) {
	    errors.reject(RegistryErrorCodes.COMPANY_EXPIRATION,
		    RegistryResponseMessage.ERROR_COMPANY_EXPIRE_DATE_LT_NOW);
	}
	// Expiration is before start.
	if (expire.before(start)) {
	    errors.reject(RegistryErrorCodes.COMMON_DATES,
		    RegistryResponseMessage.ERROR_COMMON_START_DATE_GT_END_DATE);
	}
    }

}
