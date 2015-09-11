package com.cebedo.pmsys.helper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

public class ValidationHelper {

    private static final String PATTERN_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    private final Pattern patternEmail = Pattern.compile(PATTERN_EMAIL);
    private Matcher matcher;

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
	if (projectPayroll.getEndDate().before(projectPayroll.getStartDate())) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(String.format(
			    RegistryResponseMessage.ERROR_COMMON_X_DATE_BEFORE_Y_DATE, "start date",
			    "end date"));
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
    public boolean checkFile(MultipartFile multipartFile) {
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

    public boolean checkLength(String property, int max) {
	if (property.length() > max) {
	    return false;
	}
	return true;
    }

    public void rejectLength(Errors errors, String propertyName, int len) {
	errors.reject("",
		String.format(RegistryResponseMessage.ERROR_COMMON_MAX_LENGTH, propertyName, len));
    }

    public boolean numberIsZeroOrPositive(double number) {
	if (number < 0) {
	    return false;
	}
	return true;
    }

    public boolean checkEmail(String email) {
	this.matcher = this.patternEmail.matcher(email);
	if (!this.matcher.matches()) {
	    return false;
	}
	return true;
    }

    public void rejectZeroOrPositive(Errors errors, String propertyName) {
	errors.reject(
		"",
		String.format(RegistryResponseMessage.ERROR_COMMON_ZERO_OR_POSITIVE,
			StringUtils.capitalize(propertyName)));
    }

    public void rejectInvalid(Errors errors, String propertyName) {
	// Please provide a valid %s.
	errors.reject("",
		String.format(RegistryResponseMessage.ERROR_COMMON_INVALID_PROPERTY, propertyName));
    }

    public boolean numberIsPositive(double nmber) {
	if (nmber > 0) {
	    return true;
	}
	return false;
    }

    public boolean checkBlank(String str) {
	if (org.apache.commons.lang.StringUtils.isBlank(str)) {
	    return true;
	}
	return false;
    }

    public void rejectDateRange(Errors errors, String string, String string2) {
	errors.reject("", String.format(RegistryResponseMessage.ERROR_COMMON_X_DATE_BEFORE_Y_DATE,
		string, string2));
    }
}
