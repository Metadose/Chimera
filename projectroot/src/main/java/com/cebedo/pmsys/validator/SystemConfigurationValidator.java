package com.cebedo.pmsys.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.constants.RegistryErrorCodes;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.model.SystemConfiguration;

public class SystemConfigurationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return SystemConfiguration.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	SystemConfiguration systemConfiguration = (SystemConfiguration) target;
	// You cannot create a configuration with an empty name.
	if (StringUtils.isBlank(systemConfiguration.getName())) {
	    errors.reject(RegistryErrorCodes.CONFIG_NAME,
		    RegistryResponseMessage.ERROR_CONFIG_EMPTY_NAME);
	}
    }

}
