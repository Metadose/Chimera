package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.RegistryErrorCodes;
import com.cebedo.pmsys.constants.RegistryResponseMessage;

@Component
public class MultipartFileValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return MultipartFile.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	MultipartFile multipartFile = (MultipartFile) target;
	// TODO multipartFile.getOriginalFilename();
	// Check allowed file extensions.
	if (multipartFile == null || multipartFile.isEmpty()) {
	    errors.reject(RegistryErrorCodes.COMMON_FILE,
		    RegistryResponseMessage.ERROR_COMMON_EMPTY_FILE);
	}
    }

}
