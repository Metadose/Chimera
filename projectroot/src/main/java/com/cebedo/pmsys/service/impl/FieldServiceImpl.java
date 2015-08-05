package com.cebedo.pmsys.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.FieldDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.service.FieldService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class FieldServiceImpl implements FieldService {

    private static Logger logger = Logger.getLogger(Field.OBJECT_NAME);
    private AuthHelper authHelper = new AuthHelper();
    private LogHelper logHelper = new LogHelper();

    private FieldDAO fieldDAO;
    private ProjectDAO projectDAO;

    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public void setFieldDAO(FieldDAO fieldDAO) {
	this.fieldDAO = fieldDAO;
    }

    /**
     * Create a new field.
     */
    @Override
    @Transactional
    public String create(Field field) {
	AuthenticationToken auth = this.authHelper.getAuth();
	if (auth.isSuperAdmin()) {

	    // Log and notify.

	    // Do service.
	    this.fieldDAO.create(field);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateCreate(Field.OBJECT_NAME, field.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE, Field.OBJECT_NAME,
		field.getId(), field.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateCreate(Field.OBJECT_NAME, field.getName());
    }

    /**
     * Get a field given an id.
     */
    @Override
    @Transactional
    public Field getByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Field field = this.fieldDAO.getByID(id);
	logger.info(this.logHelper.logGetObject(auth, Field.OBJECT_NAME, id, field.getName()));
	return field;
    }

    /**
     * Update a field.
     */
    @Override
    @Transactional
    public String update(Field field) {
	AuthenticationToken auth = this.authHelper.getAuth();
	if (auth.isSuperAdmin()) {

	    // Log and notify.

	    // Do service.
	    this.fieldDAO.update(field);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(Field.OBJECT_NAME, field.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE, Field.OBJECT_NAME,
		field.getId(), field.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(Field.OBJECT_NAME, field.getName());
    }

    /**
     * Delete a field.
     */
    @Override
    @Transactional
    public String delete(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Field field = this.fieldDAO.getByID(id);

	if (auth.isSuperAdmin()) {

	    // Log and notify.

	    // Do service.
	    this.fieldDAO.delete(id);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateDelete(Field.OBJECT_NAME, field.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE, Field.OBJECT_NAME,
		field.getId(), field.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateDelete(Field.OBJECT_NAME, field.getName());
    }

    /**
     * List all fields.
     */
    @Override
    @Transactional
    public List<Field> list() {
	AuthenticationToken auth = this.authHelper.getAuth();
	logger.info(this.logHelper.logListWithoutCompany(auth, Field.OBJECT_NAME));
	return this.fieldDAO.list();
    }

    /**
     * List all fields with collections.
     */
    @Override
    @Transactional
    public List<Field> listWithAllCollections() {
	AuthenticationToken auth = this.authHelper.getAuth();
	logger.info(this.logHelper.logListWithoutCompany(auth, Field.OBJECT_NAME));
	return this.fieldDAO.listWithAllCollections();
    }

    /**
     * Assign a field to a project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String assignFieldToProject(FieldAssignment fieldAssignment, long fieldID, long projectID) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Project proj = this.projectDAO.getByID(projectID);
	Field field = this.fieldDAO.getByID(fieldID);

	if (this.authHelper.isActionAuthorized(proj)) {

	    // Log and notify.

	    // Do service.
	    fieldAssignment.setField(field);
	    fieldAssignment.setProject(proj);
	    this.fieldDAO.assignFieldToProject(fieldAssignment);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateAssign(Field.OBJECT_NAME,
		    fieldAssignment.getLabel());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN, Project.OBJECT_NAME,
		proj.getId(), proj.getName()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN, Field.OBJECT_NAME,
		fieldAssignment.getLabel(), fieldAssignment.getValue()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateAssign(Field.OBJECT_NAME, fieldAssignment.getLabel());
    }

    /**
     * Unassign a field from a project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String unassignFieldFromProject(long fieldID, long projectID, String label, String value) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project proj = this.projectDAO.getByID(projectID);
	FieldAssignment fieldAssignment = this.fieldDAO.getFieldByKeys(projectID, fieldID, label, value);

	if (this.authHelper.isActionAuthorized(proj)) {
	    // Log and notify.

	    // Do service.
	    this.fieldDAO.unassignFieldFromProject(fieldID, projectID, label, value);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUnassign(Field.OBJECT_NAME,
		    fieldAssignment.getLabel());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN, Project.OBJECT_NAME,
		proj.getId(), proj.getName()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN, Field.OBJECT_NAME,
		fieldAssignment.getLabel(), fieldAssignment.getValue()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUnassign(Field.OBJECT_NAME, fieldAssignment.getLabel());
    }

    /**
     * Unassign all fields from a project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String unassignAllFieldsFromProject(long projectID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project proj = this.projectDAO.getByID(projectID);

	if (this.authHelper.isActionAuthorized(proj)) {
	    // Log and notify.

	    // Do service.
	    this.fieldDAO.unassignAllFieldsFromProject(projectID);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUnassignAll(Field.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN_ALL, Project.OBJECT_NAME,
		proj.getId(), proj.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUnassignAll(Field.OBJECT_NAME);
    }

    /**
     * Update an assign project field.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String updateAssignedProjectField(long projectID, long fieldID, String oldLabel,
	    String oldValue, String label, String value) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Project proj = this.projectDAO.getByID(projectID);

	if (this.authHelper.isActionAuthorized(proj)) {

	    // Log and notify.
	    FieldAssignment fieldAssignment = this.fieldDAO.getFieldByKeys(projectID, fieldID, oldLabel,
		    oldValue);

	    // Do service.
	    this.fieldDAO.updateAssignedField(FieldAssignment.TABLE_NAME, Project.COLUMN_PRIMARY_KEY,
		    projectID, fieldID, oldLabel, oldValue, label, value);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(FieldAssignment.OBJECT_LABEL, label);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE, Project.OBJECT_NAME,
		proj.getId(), proj.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(FieldAssignment.OBJECT_LABEL, oldLabel);
    }

}
