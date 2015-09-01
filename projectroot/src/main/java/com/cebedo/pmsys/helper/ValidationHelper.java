package com.cebedo.pmsys.helper;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

public class ValidationHelper {

    /**
     * Project payroll. This cannot be translated to a Validator since we have
     * an action flag.
     * 
     * @param projectPayroll
     * @param action
     * @return
     */
    public String validate(ProjectPayroll projectPayroll, AuditAction action) {

	// Start date > end date.
	if (projectPayroll.getStartDate().after(projectPayroll.getEndDate())) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_START_DATE_GT_END_DATE);
	}

	// No staff is checked.
	if (action == AuditAction.ACTION_UPDATE) {
	    long[] staffIDs = projectPayroll.getStaffIDs();
	    if (staffIDs.length == 0) {
		return AlertBoxGenerator.FAILED
			.generateHTML(RegistryResponseMessage.ERROR_PROJECT_PAYROLL_NO_STAFF_CHECK);
	    }
	}

	return null;
    }

    /**
     * Multi-part file.
     * 
     * @param multipartFile
     * @return
     */
    public boolean check(MultipartFile multipartFile) {
	// TODO multipartFile.getOriginalFilename();
	// Check allowed file extensions.
	if (multipartFile == null || multipartFile.isEmpty()) {
	    return false;
	}
	return true;
    }

    /**
     * Generate error messages based on result.
     * 
     * @param result
     * @return
     */
    public String errorMessageHTML(BindingResult result) {
	// Variables used.
	String errorsStr = "";
	AlertBoxGenerator alertBox = AlertBoxGenerator.FAILED;
	// Loop all errors.
	List<ObjectError> errors = result.getAllErrors();
	for (ObjectError error : errors) {
	    errorsStr += alertBox.generateHTML(error.getDefaultMessage());
	}
	return errorsStr;
    }
}
