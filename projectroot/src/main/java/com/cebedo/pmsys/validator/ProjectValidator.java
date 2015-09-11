package com.cebedo.pmsys.validator;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.enums.ProjectStatus;
import com.cebedo.pmsys.model.Project;

@Component
public class ProjectValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return Project.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	Project project = (Project) target;
	// Please provide a valid name.
	String name = project.getName();
	if (StringUtils.isBlank(name)) {
	    errors.reject("", RegistryResponseMessage.ERROR_COMMON_INVALID_NAME);
	}

	Date start = project.getDateStart();
	Date endTarget = project.getTargetCompletionDate();
	Date endActual = project.getActualCompletionDate();

	// Start date must be before the completion date.
	if (start.after(endTarget)
		|| (project.getStatus() == ProjectStatus.COMPLETED.id() && start.after(endActual))) {
	    errors.reject("", RegistryResponseMessage.ERROR_PROJECT_START_DATE_GT_COMPLETION_DATE);
	}
    }

}
