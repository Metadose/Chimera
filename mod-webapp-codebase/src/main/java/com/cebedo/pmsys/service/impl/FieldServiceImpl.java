package com.cebedo.pmsys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.dao.FieldDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.factory.AlertBoxFactory;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.service.FieldService;
import com.cebedo.pmsys.validator.FieldAssignmentValidator;

// TODO Transfer all below functions to Project Service.
@Service
public class FieldServiceImpl implements FieldService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    private FieldDAO fieldDAO;
    private ProjectDAO projectDAO;

    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public void setFieldDAO(FieldDAO fieldDAO) {
	this.fieldDAO = fieldDAO;
    }

    @Autowired
    FieldAssignmentValidator fieldAssignmentValidator;

    /**
     * Assign a field to a project.
     */
    @Override
    @Transactional
    public String assignField(FieldAssignment fieldAssignment, long fieldID, long projectID,
	    BindingResult result) {

	Project proj = this.projectDAO.getByID(projectID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxFactory.ERROR;
	}

	// Service layer form validation.
	this.fieldAssignmentValidator.validate(fieldAssignment, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Log.
	this.messageHelper.auditableKey(AuditAction.ACTION_ASSIGN, Project.OBJECT_NAME, proj.getId(),
		Field.OBJECT_NAME, fieldAssignment.getLabel(), proj, fieldAssignment.getLabel());

	// Do service.
	Field field = this.fieldDAO.getByID(fieldID);
	fieldAssignment.setField(field);
	fieldAssignment.setProject(proj);
	this.fieldDAO.assignFieldToProject(fieldAssignment);

	// Return success.
	return AlertBoxFactory.SUCCESS.generateAssign(Field.OBJECT_NAME, fieldAssignment.getLabel());
    }

    /**
     * Unassign a field from a project.
     */
    @Override
    @Transactional
    public String unassignField(long fieldID, long projectID, String label, String value) {
	Project proj = this.projectDAO.getByID(projectID);
	FieldAssignment fieldAssignment = this.fieldDAO.getFieldByKeys(projectID, fieldID, label, value);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxFactory.ERROR;
	}
	// Log.
	this.messageHelper.auditableKey(AuditAction.ACTION_UNASSIGN, Project.OBJECT_NAME, proj.getId(),
		Field.OBJECT_NAME, fieldAssignment.getLabel(), proj, fieldAssignment.getLabel());

	// Do service.
	this.fieldDAO.unassignFieldFromProject(fieldID, projectID, label, value);

	// Return success.
	return AlertBoxFactory.SUCCESS.generateUnassign(Field.OBJECT_NAME, fieldAssignment.getLabel());
    }

    /**
     * Unassign all fields from a project.
     */
    @Override
    @Transactional
    public String unassignAllFields(long projectID) {
	Project proj = this.projectDAO.getByID(projectID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxFactory.ERROR;
	}
	// Log.
	this.messageHelper.auditableKey(AuditAction.ACTION_UNASSIGN_ALL, Project.OBJECT_NAME,
		proj.getId(), Field.OBJECT_NAME, "All", proj, "All");

	// Do service.
	this.fieldDAO.unassignAllFieldsFromProject(projectID);

	// Return success.
	return AlertBoxFactory.SUCCESS.generateUnassignAll(Field.OBJECT_NAME);
    }

    /**
     * Update an assign project field.
     */
    @Override
    @Transactional
    public String updateField(long projectID, long fieldID, String oldLabel, String oldValue,
	    String label, String value, BindingResult result) {

	Project proj = this.projectDAO.getByID(projectID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxFactory.ERROR;
	}

	FieldAssignment fieldAssignment = this.fieldDAO.getFieldByKeys(projectID, fieldID, oldLabel,
		oldValue);

	// Service layer form validation.
	this.fieldAssignmentValidator.validate(fieldAssignment, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Log.
	this.messageHelper.auditableKey(AuditAction.ACTION_UPDATE, Project.OBJECT_NAME, proj.getId(),
		Field.OBJECT_NAME, label, proj, label);

	// Do service.
	this.fieldDAO.updateAssignedField(FieldAssignment.TABLE_NAME, Project.COLUMN_PRIMARY_KEY,
		projectID, fieldID, oldLabel, oldValue, label, value);

	// Return success.
	return AlertBoxFactory.SUCCESS.generateUpdate(FieldAssignment.OBJECT_LABEL, label);
    }

    @Transactional
    @Override
    public Field getByName(String name) {
	// TODO Add validations.
	return this.fieldDAO.getByName(name);
    }

    @Transactional
    @Override
    public void create(Field newField) {
	// TODO Add validations.
	this.fieldDAO.create(newField);
    }

}
