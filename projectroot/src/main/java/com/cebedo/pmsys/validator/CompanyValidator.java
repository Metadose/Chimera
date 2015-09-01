package com.cebedo.pmsys.validator;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.constants.RegistryErrorCodes;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.model.Company;

@Component
public class CompanyValidator implements Validator {

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
