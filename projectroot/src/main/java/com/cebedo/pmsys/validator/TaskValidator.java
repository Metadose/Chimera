package com.cebedo.pmsys.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Task;

@Component
public class TaskValidator implements Validator {

    private ValidationHelper validationHelper = new ValidationHelper();

    @Override
    public boolean supports(Class<?> clazz) {
	return Task.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	Task task = (Task) target;
	String title = task.getTitle();

	// Title is not blank.
	if (this.validationHelper.stringIsBlank(title)) {
	    this.validationHelper.rejectInvalidProperty(errors, "title");
	}
	// Title len = 64
	if (this.validationHelper.stringLengthIsGreaterThanMax(title, 64)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "title", 64);
	}
	// Content len = 255
	if (this.validationHelper.stringLengthIsGreaterThanMax(task.getContent(), 255)) {
	    this.validationHelper.rejectGreaterThanMaxLength(errors, "content", 255);
	}
	// Duration (planned) must be positive.
	if (this.validationHelper.numberIsZeroOrNegative(task.getDuration())) {
	    this.validationHelper.rejectZeroOrNegativeNumber(errors, "planned duration");
	}
	// Duration (actual) must be positive.
	if (task.getStatus() == TaskStatus.COMPLETED.id()
		&& this.validationHelper.numberIsZeroOrNegative(task.getActualDuration())) {
	    this.validationHelper.rejectZeroOrNegativeNumber(errors, "actual duration");
	}
    }

}
