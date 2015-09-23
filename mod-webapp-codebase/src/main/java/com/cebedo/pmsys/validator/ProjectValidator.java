package com.cebedo.pmsys.validator;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.enums.ProjectStatus;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Project;

@Component
public class ProjectValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return Project.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	Project project = (Project) target;
	Date start = project.getDateStart();
	Date endTarget = project.getTargetCompletionDate();
	Date endActual = project.getActualCompletionDate();
	double phyTarget = project.getPhysicalTarget();
	String name = project.getName();

	// Name must not be blank.
	if (this.validationHelper.stringIsBlank(name)) {
	    this.validationHelper.rejectInvalidProperty(errors, "name");
	}
	// Name max length = 32
	if (this.validationHelper.stringLengthIsGreaterThanMax(name, 32)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "name", 32);
	}
	// Physical target must be zero or positive.
	if (this.validationHelper.numberIsNegative(phyTarget)) {
	    this.validationHelper.rejectNegativeNumber(errors, "physical target");
	}
	// Location max length = 108
	if (this.validationHelper.stringLengthIsGreaterThanMax(project.getLocation(), 108)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "location", 108);
	}
	// Notes max length = 255
	if (this.validationHelper.stringLengthIsGreaterThanMax(project.getNotes(), 255)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "notes", 255);
	}

	if (endTarget.before(start)) {
	    this.validationHelper.rejectInvalidDateRange(errors, "start date", "target completion date");
	}
	if (project.getStatus() == ProjectStatus.COMPLETED.id() && endActual.before(start)) {
	    this.validationHelper.rejectInvalidDateRange(errors, "start date", "actual completion date");
	}
    }
}
