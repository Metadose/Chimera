package com.cebedo.pmsys.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.FieldDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.TaskDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.model.assignment.StaffFieldAssignment;
import com.cebedo.pmsys.model.assignment.TaskFieldAssignment;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxFactory;

@Service
public class FieldServiceImpl implements FieldService {

    private static Logger logger = Logger.getLogger(Field.OBJECT_NAME);
    private AuthHelper authHelper = new AuthHelper();
    private LogHelper logHelper = new LogHelper();
    private MessageHelper messageHelper = new MessageHelper();

    private FieldDAO fieldDAO;
    private ProjectDAO projectDAO;
    private TaskDAO taskDAO;
    private StaffDAO staffDAO;

    public void setStaffDAO(StaffDAO staffDAO) {
	this.staffDAO = staffDAO;
    }

    public void setTaskDAO(TaskDAO taskDAO) {
	this.taskDAO = taskDAO;
    }

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
	    this.messageHelper.sendAction(AuditAction.CREATE, field);

	    // Do service.
	    this.fieldDAO.create(field);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateCreate(Field.OBJECT_NAME,
		    field.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE,
		Field.OBJECT_NAME, field.getId(), field.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateCreate(Field.OBJECT_NAME,
		field.getName());
    }

    /**
     * Get a field given an id.
     */
    @Override
    @Transactional
    public Field getByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Field field = this.fieldDAO.getByID(id);
	logger.info(this.logHelper.logGetObject(auth, Field.OBJECT_NAME, id,
		field.getName()));
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
	    this.messageHelper.sendAction(AuditAction.UPDATE, field);

	    // Do service.
	    this.fieldDAO.update(field);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateUpdate(Field.OBJECT_NAME,
		    field.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		Field.OBJECT_NAME, field.getId(), field.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateUpdate(Field.OBJECT_NAME,
		field.getName());
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
	    this.messageHelper.sendAction(AuditAction.DELETE, field);

	    // Do service.
	    this.fieldDAO.delete(id);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateDelete(Field.OBJECT_NAME,
		    field.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE,
		Field.OBJECT_NAME, field.getId(), field.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateDelete(Field.OBJECT_NAME,
		field.getName());
    }

    /**
     * List all fields.
     */
    @Override
    @Transactional
    public List<Field> list() {
	AuthenticationToken auth = this.authHelper.getAuth();
	logger.info(this.logHelper.logListWithoutCompany(auth,
		Field.OBJECT_NAME));
	return this.fieldDAO.list();
    }

    /**
     * List all fields with collections.
     */
    @Override
    @Transactional
    public List<Field> listWithAllCollections() {
	AuthenticationToken auth = this.authHelper.getAuth();
	logger.info(this.logHelper.logListWithoutCompany(auth,
		Field.OBJECT_NAME));
	return this.fieldDAO.listWithAllCollections();
    }

    /**
     * Assign a field to a project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String assignProject(FieldAssignment fieldAssignment, long fieldID,
	    long projectID) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Project proj = this.projectDAO.getByID(projectID);
	Field field = this.fieldDAO.getByID(fieldID);

	if (this.authHelper.isActionAuthorized(proj)) {

	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.ASSIGN, proj,
		    fieldAssignment);

	    // Do service.
	    fieldAssignment.setField(field);
	    fieldAssignment.setProject(proj);
	    this.fieldDAO.assignProject(fieldAssignment);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateAssign(Field.OBJECT_NAME,
		    fieldAssignment.getLabel());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Project.OBJECT_NAME, proj.getId(), proj.getName()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Field.OBJECT_NAME, fieldAssignment.getLabel(),
		fieldAssignment.getValue()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateAssign(Field.OBJECT_NAME,
		fieldAssignment.getLabel());
    }

    /**
     * Unassign a field from a project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String unassignProject(long fieldID, long projectID, String label,
	    String value) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project proj = this.projectDAO.getByID(projectID);
	FieldAssignment fieldAssignment = this.fieldDAO.getFieldByKeys(
		projectID, fieldID, label, value);

	if (this.authHelper.isActionAuthorized(proj)) {
	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.UNASSIGN, proj,
		    fieldAssignment);

	    // Do service.
	    this.fieldDAO.unassignProject(fieldID, projectID, label, value);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateUnassign(Field.OBJECT_NAME,
		    fieldAssignment.getLabel());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		Project.OBJECT_NAME, proj.getId(), proj.getName()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		Field.OBJECT_NAME, fieldAssignment.getLabel(),
		fieldAssignment.getValue()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateUnassign(Field.OBJECT_NAME,
		fieldAssignment.getLabel());
    }

    /**
     * Unassign all fields from a project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String unassignAllProjects(long projectID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project proj = this.projectDAO.getByID(projectID);

	if (this.authHelper.isActionAuthorized(proj)) {
	    // Log and notify.
	    this.messageHelper.sendUnassignAll(Field.OBJECT_NAME, proj);

	    // Do service.
	    this.fieldDAO.unassignAllProjects(projectID);

	    // Return success.
	    return AlertBoxFactory.SUCCESS
		    .generateUnassignAll(Field.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.UNASSIGN_ALL, Project.OBJECT_NAME, proj.getId(),
		proj.getName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateUnassignAll(Field.OBJECT_NAME);
    }

    /**
     * Update an assign project field.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public void updateAssignedProjectField(long projectID, long fieldID,
	    String oldLabel, String oldValue, String label, String value) {
	this.fieldDAO.updateAssignedField(FieldAssignment.TABLE_NAME,
		Project.COLUMN_PRIMARY_KEY, projectID, fieldID, oldLabel,
		oldValue, label, value);
    }

    @Override
    @Transactional
    public void assignTask(TaskFieldAssignment taskField, long fieldID,
	    long taskID) {
	Field field = this.fieldDAO.getByID(fieldID);
	Task task = this.taskDAO.getByID(taskID);
	taskField.setField(field);
	taskField.setTask(task);
	this.fieldDAO.assignTask(taskField);
    }

    @Override
    @Transactional
    public void unassignAllTasks(long taskID) {
	this.fieldDAO.unassignAllTasks(taskID);
    }

    @Override
    @Transactional
    public void unassignTask(long fieldID, long taskID, String label,
	    String value) {
	this.fieldDAO.unassignTask(fieldID, taskID, label, value);
    }

    @Override
    @Transactional
    public void updateAssignedTaskField(long taskID, long fieldID,
	    String oldLabel, String oldValue, String label, String value) {
	// Delete the old version of the field.
	this.fieldDAO.unassignTask(fieldID, taskID, oldLabel, oldValue);

	// Save a new one.
	TaskFieldAssignment newFieldAssignment = new TaskFieldAssignment();
	Field field = this.fieldDAO.getByID(fieldID);
	Task task = this.taskDAO.getByID(taskID);
	newFieldAssignment.setTask(task);
	newFieldAssignment.setField(field);
	newFieldAssignment.setLabel(label);
	newFieldAssignment.setValue(value);
	this.fieldDAO.assignTask(newFieldAssignment);
    }

    @Override
    @Transactional
    public void unassignStaff(long fieldID, long staffID, String label,
	    String value) {
	this.fieldDAO.unassignStaff(fieldID, staffID, label, value);
    }

    @Override
    @Transactional
    public void assignStaff(StaffFieldAssignment fieldAssignment, long fieldID,
	    long staffID) {
	Field field = this.fieldDAO.getByID(fieldID);
	Staff staff = this.staffDAO.getByID(staffID);
	fieldAssignment.setField(field);
	fieldAssignment.setStaff(staff);
	this.fieldDAO.assignStaff(fieldAssignment);
    }

    @Override
    @Transactional
    public void unassignAllStaff(long staffID) {
	this.fieldDAO.unassignAllStaff(staffID);
    }

    @Override
    @Transactional
    public void updateAssignedStaffField(long staffID, long fieldID,
	    String oldLabel, String oldValue, String label, String value) {
	// Delete the old version of the field.
	this.fieldDAO.unassignStaff(fieldID, staffID, oldLabel, oldValue);

	// Save a new one.
	StaffFieldAssignment newFieldAssignment = new StaffFieldAssignment();
	Field field = this.fieldDAO.getByID(fieldID);
	Staff staff = this.staffDAO.getByID(staffID);
	newFieldAssignment.setStaff(staff);
	newFieldAssignment.setField(field);
	newFieldAssignment.setLabel(label);
	newFieldAssignment.setValue(value);
	this.fieldDAO.assignStaff(newFieldAssignment);
    }
}
