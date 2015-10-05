package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.pojo.FormMultipartFile;

@Component
public class ImageUploadValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return FormMultipartFile.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	if (errors.hasErrors()) {
	    this.validationHelper.rejectMissingRequiredFields(errors);
	    return;
	}

	FormMultipartFile targetObj = (FormMultipartFile) target;
	MultipartFile file = targetObj.getFile();

	if (this.validationHelper.fileIsNullOrEmpty(file)) {
	    this.validationHelper.rejectFileIsNullOrEmpty(errors);
	}

	if (this.validationHelper.fileIsNotImage(file)) {
	    this.validationHelper.rejectFileIsNotImage(errors);
	}
    }

}
