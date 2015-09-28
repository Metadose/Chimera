package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.bean.EstimateComputationInputBean;
import com.cebedo.pmsys.helper.ValidationHelper;

@Component
public class EstimateInputValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return EstimateComputationInputBean.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	if (errors.hasErrors()) {
	    this.validationHelper.rejectMissingRequiredFields(errors);
	    return;
	}
	EstimateComputationInputBean targetObj = (EstimateComputationInputBean) target;
	MultipartFile file = targetObj.getEstimationFile();
	String name = targetObj.getName();
	String remarks = targetObj.getRemarks();

	// Name is not blank.
	if (this.validationHelper.stringIsBlank(name)) {
	    this.validationHelper.rejectInvalidProperty(errors, "name");
	}
	// Name length = 32
	if (this.validationHelper.stringLengthIsGreaterThanMax(name, 32)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "name", 32);
	}
	// Remarks length = 255
	if (this.validationHelper.stringLengthIsGreaterThanMax(remarks, 255)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "remarks", 255);
	}

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
