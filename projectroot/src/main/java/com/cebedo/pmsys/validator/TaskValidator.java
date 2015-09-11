package com.cebedo.pmsys.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.model.Task;

@Component
public class TaskValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return Task.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	Task task = (Task) target;
	// You cannot create a task with an empty title.
	String title = task.getTitle();
	if (StringUtils.isBlank(title)) {
	    errors.reject("", RegistryResponseMessage.ERROR_TASK_EMPTY_TITLE);
	}

	// Task duration must be greater than zero.
	if (task.getDuration() <= 0) {
	    errors.reject("", RegistryResponseMessage.ERROR_TASK_DURATION_LTE_ZERO);
	}
    }

}
