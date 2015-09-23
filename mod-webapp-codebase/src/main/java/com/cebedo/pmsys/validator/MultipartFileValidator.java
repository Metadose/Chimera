package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.helper.ValidationHelper;

@Component
public class MultipartFileValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return MultipartFile.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	MultipartFile file = (MultipartFile) target;

	// If the file is XLSX.
	if (this.validationHelper.fileIsExcelXLSX(file)) {
	    this.validationHelper.rejectXLSXFile(errors);
	}
	// If file is null, or file is empty.
	// Handle case when other file types are uploaded.
	// Filter only Excel files: "application/vnd.ms-excel"
	else if (this.validationHelper.fileIsNullOrEmpty(file)
		|| this.validationHelper.fileIsNotExcelXLS(file)) {
	    this.validationHelper.rejectInvalidProperty(errors, "Excel (*.xls) file");
	}
    }

}
