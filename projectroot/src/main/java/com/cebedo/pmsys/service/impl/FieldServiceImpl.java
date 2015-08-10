package com.cebedo.pmsys.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.FieldDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.service.FieldService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

// TODO Transfer all below functions to Project Service.
@Deprecated
@Service
public class FieldServiceImpl implements FieldService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();

    private FieldDAO fieldDAO;
    private ProjectDAO projectDAO;

    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public void setFieldDAO(FieldDAO fieldDAO) {
	this.fieldDAO = fieldDAO;
    }

    /**
     * Assign a field to a project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String assignFieldToProject(FieldAssignment fieldAssignment, long fieldID, long projectID) {

	Project proj = this.projectDAO.getByID(projectID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_ASSIGN, Project.OBJECT_NAME, proj.getId(),
		Field.OBJECT_NAME, fieldAssignment.getLabel());

	Field field = this.fieldDAO.getByID(fieldID);

	// Log and notify.

	// Do service.
	fieldAssignment.setField(field);
	fieldAssignment.setProject(proj);
	this.fieldDAO.assignFieldToProject(fieldAssignment);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateAssign(Field.OBJECT_NAME, fieldAssignment.getLabel());
    }

    /**
     * Unassign a field from a project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String unassignFieldFromProject(long fieldID, long projectID, String label, String value) {
	Project proj = this.projectDAO.getByID(projectID);
	FieldAssignment fieldAssignment = this.fieldDAO.getFieldByKeys(projectID, fieldID, label, value);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_UNASSIGN, Project.OBJECT_NAME, proj.getId(),
		Field.OBJECT_NAME, fieldAssignment.getLabel());

	// Log and notify.

	// Do service.
	this.fieldDAO.unassignFieldFromProject(fieldID, projectID, label, value);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUnassign(Field.OBJECT_NAME, fieldAssignment.getLabel());
    }

    /**
     * Unassign all fields from a project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String unassignAllFieldsFromProject(long projectID) {
	Project proj = this.projectDAO.getByID(projectID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_UNASSIGN_ALL, Project.OBJECT_NAME, proj.getId(),
		Field.OBJECT_NAME);

	// Do service.
	this.fieldDAO.unassignAllFieldsFromProject(projectID);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUnassignAll(Field.OBJECT_NAME);
    }

    /**
     * Update an assign project field.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String updateAssignedProjectField(long projectID, long fieldID, String oldLabel,
	    String oldValue, String label, String value) {

	Project proj = this.projectDAO.getByID(projectID);
	FieldAssignment fieldAssignment = this.fieldDAO.getFieldByKeys(projectID, fieldID, oldLabel,
		oldValue);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_UPDATE, Project.OBJECT_NAME, proj.getId(),
		Field.OBJECT_NAME, fieldAssignment.getLabel());

	// Do service.
	this.fieldDAO.updateAssignedField(FieldAssignment.TABLE_NAME, Project.COLUMN_PRIMARY_KEY,
		projectID, fieldID, oldLabel, oldValue, label, value);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUpdate(FieldAssignment.OBJECT_LABEL, label);
    }

}
