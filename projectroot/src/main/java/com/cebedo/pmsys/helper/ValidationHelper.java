package com.cebedo.pmsys.helper;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.bean.EstimateComputationInputBean;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.pojo.FormMassAttendance;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

public class ValidationHelper {

    private static final String PATTERN_USERNAME = "^[a-z0-9_-]{4,32}$";
    private static final String PATTERN_PASSWORD = "^(?=.*\\d).{8,16}$";

    private final Pattern patternUsername = Pattern.compile(PATTERN_USERNAME);
    private final Pattern patternPassword = Pattern.compile(PATTERN_PASSWORD);
    private Matcher matcher;

    /**
     * Attendance.
     * 
     * @param attendance
     * @return
     */
    public String validate(Attendance attendance) {
	double wage = attendance.getWage(); // < Zero
	if (wage < 0) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_STAFF_ATTENDANCE_NEGATIVE_WAGE);
	}
	return null;
    }

    /**
     * Mass attendance.
     * 
     * @param attendanceMass
     * @return
     */
    public String validate(FormMassAttendance attendanceMass) {
	Date startDate = attendanceMass.getStartDate(); // Start date is after
	Date endDate = attendanceMass.getEndDate(); // End date
	double wage = attendanceMass.getWage(); // < Zero

	// If start date is > end date, error.
	if (startDate.after(endDate)) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_START_DATE_GT_END_DATE);
	}
	if (wage < 0) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_STAFF_ATTENDANCE_NEGATIVE_WAGE);
	}
	return null;
    }

    /**
     * Company.
     * 
     * @param company
     * @return
     */
    public String validate(Company company) {
	Date now = new Date(System.currentTimeMillis());
	Date expire = company.getDateExpiration();
	Date start = company.getDateStarted();

	// Expiration is before now.
	if (expire.before(now)) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMPANY_EXPIRE_DATE_LT_NOW);
	}
	// Expiration is before start.
	if (expire.before(start)) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_START_DATE_GT_END_DATE);
	}
	return null;
    }

    /**
     * Estimation.
     * 
     * @param estimateInput
     * @return
     */
    public String validate(EstimateComputationInputBean estimateInput) {
	MultipartFile file = estimateInput.getEstimationFile();
	// If file is null
	// Or file is empty.
	if (file == null || file.isEmpty()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_ESTIMATION_EMPTY_FILE);
	}
	// TODO Handle case when other file types are uploaded.
	// Filter only Excel files.
	// TODO Test if code works in *.xlsx
	return null;
    }

    /**
     * Project field.
     */
    public String validate(FieldAssignment fieldAssignment) {
	// You cannot set an empty label.
	String label = fieldAssignment.getLabel();
	String value = fieldAssignment.getValue();
	if (label.isEmpty() || label.replaceAll(" ", "").isEmpty() || value.isEmpty()
		|| value.replaceAll(" ", "").isEmpty()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_EMPTY_EXTRA_INFO);
	}
	return null;
    }

    /**
     * Material.
     * 
     * @param obj
     * @return
     */
    public String validate(Material obj) {
	// TODO Check the material object for more validations.
	// Can only choose one unit of measure for each material.
	int unitCount = 0;
	unitCount = obj.getUnitLength() == null ? unitCount : unitCount + 1;
	unitCount = obj.getUnitMass() == null ? unitCount : unitCount + 1;
	unitCount = obj.getUnitVolume() == null ? unitCount : unitCount + 1;
	if (unitCount > 1) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_MATERIAL_MORE_THAN_ONE_UNIT);
	}
	return null;
    }

    /**
     * Project payroll.
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
     * Pull-out.
     * 
     * @param obj
     * @return
     */
    public String validate(PullOut obj) {

	Material material = obj.getMaterial();

	// You are not allowed to pull-out
	// if quantity is greater than what is available.
	double quantity = obj.getQuantity();
	double available = material.getAvailable();

	// Error: Pullout more than the available.
	if (quantity > available) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_PULLOUT_EXCEED);
	}
	// Error: Invalid quantity value.
	if (quantity <= 0) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_INVALID_QUANTITY);
	}
	// Error: Invalid date time.
	if (obj.getDatetime() == null) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_INVALID_DATE_TIME);
	}
	// Error: Pull out date is before the delivery date.
	if (obj.getDatetime().before(obj.getDelivery().getDatetime())) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_PULLOUT_DATE_BEFORE_DELIVERY);
	}
	// Error: Etc.
	if (available <= 0) {
	    return AlertBoxGenerator.ERROR;
	}
	return null;
    }

    /**
     * System configuration.
     * 
     * @param systemConfiguration
     * @return
     */
    public String validate(SystemConfiguration systemConfiguration) {
	// You cannot create a configuration with an empty name.
	if (systemConfiguration.getName().isEmpty()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_CONFIG_EMPTY_NAME);
	}
	return null;
    }

    /**
     * System user.
     * 
     * @param systemUser
     * @return
     */
    public String validate(SystemUser systemUser) {

	String username = systemUser.getUsername();
	String password = systemUser.getPassword();
	String passwordRe = systemUser.getRetypePassword();

	// If the username and password are the same.
	if (username.equals(password)) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_AUTH_USERNAME_PASSWORD_EQUAL);
	}

	// If the password and re-type password are not the same.
	if (!password.equals(passwordRe)) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_AUTH_PASSWORDS_NOT_EQUAL);
	}

	// Check if the user name is valid.
	this.matcher = this.patternUsername.matcher(username);
	if (!this.matcher.matches()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_AUTH_USERNAME_INVALID_PATTERN);
	}

	// Check if the password is valid.
	this.matcher = this.patternPassword.matcher(password);
	if (!this.matcher.matches()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_AUTH_PASSWORD_INVALID_PATTERN);
	}

	return null;
    }

    /**
     * Task.
     * 
     * @param task
     * @return
     */
    public String validate(Task task) {
	// You cannot create a task with an empty title.
	String title = task.getTitle();
	if (title == null || title.isEmpty()) {
	    return AlertBoxGenerator.FAILED.generateHTML(RegistryResponseMessage.ERROR_TASK_EMPTY_TITLE);
	}

	// Task duration must be greater than zero.
	if (task.getDuration() <= 0) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_TASK_DURATION_LTE_ZERO);
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
     * Multi-part file.
     * 
     * @param multipartFile
     * @return
     */
    public String validate(MultipartFile multipartFile) {
	// TODO multipartFile.getOriginalFilename();
	// Check allowed file extensions.
	if (multipartFile == null || multipartFile.isEmpty()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_EMPTY_FILE);
	}
	return null;
    }

    /**
     * Project.
     * 
     * @param project
     * @return
     */
    public String validate(Project project) {

	// Please provide a valid name.
	String name = project.getName();
	if (name == null || name.isEmpty()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_INVALID_NAME);
	}
	return null;
    }

    /**
     * Staff.
     * 
     * @param staff
     * @return
     */
    public String validate(Staff staff) {
	if (!staff.isNameSet()) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_INVALID_NAME);
	}
	return null;
    }
}
