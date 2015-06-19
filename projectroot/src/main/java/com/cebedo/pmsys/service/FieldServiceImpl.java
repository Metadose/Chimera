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
import com.cebedo.pmsys.ui.AlertBoxGenerator;

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
	    return AlertBoxGenerator.SUCCESS.generateCreate(Field.OBJECT_NAME,
		    field.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE,
		Field.OBJECT_NAME, field.getId(), field.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateCreate(Field.OBJECT_NAME,
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
	    return AlertBoxGenerator.SUCCESS.generateUpdate(Field.OBJECT_NAME,
		    field.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		Field.OBJECT_NAME, field.getId(), field.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(Field.OBJECT_NAME,
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
	    return AlertBoxGenerator.SUCCESS.generateDelete(Field.OBJECT_NAME,
		    field.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE,
		Field.OBJECT_NAME, field.getId(), field.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateDelete(Field.OBJECT_NAME,
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
    public String assignFieldToProject(FieldAssignment fieldAssignment,
	    long fieldID, long projectID) {

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
	    this.fieldDAO.assignFieldToProject(fieldAssignment);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateAssign(Field.OBJECT_NAME,
		    fieldAssignment.getLabel());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Project.OBJECT_NAME, proj.getId(), proj.getName()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Field.OBJECT_NAME, fieldAssignment.getLabel(),
		fieldAssignment.getValue()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateAssign(Field.OBJECT_NAME,
		fieldAssignment.getLabel());
    }

    /**
     * Unassign a field from a project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String unassignFieldFromProject(long fieldID, long projectID,
	    String label, String value) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project proj = this.projectDAO.getByID(projectID);
	FieldAssignment fieldAssignment = this.fieldDAO.getFieldByKeys(
		projectID, fieldID, label, value);

	if (this.authHelper.isActionAuthorized(proj)) {
	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.UNASSIGN, proj,
		    fieldAssignment);

	    // Do service.
	    this.fieldDAO.unassignFieldFromProject(fieldID, projectID, label,
		    value);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUnassign(Field.OBJECT_NAME,
		    fieldAssignment.getLabel());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		Project.OBJECT_NAME, proj.getId(), proj.getName()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		Field.OBJECT_NAME, fieldAssignment.getLabel(),
		fieldAssignment.getValue()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUnassign(Field.OBJECT_NAME,
		fieldAssignment.getLabel());
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
	    this.messageHelper.sendUnassignAll(Field.OBJECT_NAME, proj);

	    // Do service.
	    this.fieldDAO.unassignAllFieldsFromProject(projectID);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS
		    .generateUnassignAll(Field.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.UNASSIGN_ALL, Project.OBJECT_NAME, proj.getId(),
		proj.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUnassignAll(Field.OBJECT_NAME);
    }

    /**
     * Update an assign project field.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String updateAssignedProjectField(long projectID, long fieldID,
	    String oldLabel, String oldValue, String label, String value) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Project proj = this.projectDAO.getByID(projectID);

	if (this.authHelper.isActionAuthorized(proj)) {

	    // Log and notify.
	    FieldAssignment fieldAssignment = this.fieldDAO.getFieldByKeys(
		    projectID, fieldID, oldLabel, oldValue);
	    this.messageHelper.sendActionWithField(AuditAction.UPDATE, proj,
		    fieldAssignment);

	    // Do service.
	    this.fieldDAO.updateAssignedField(FieldAssignment.TABLE_NAME,
		    Project.COLUMN_PRIMARY_KEY, projectID, fieldID, oldLabel,
		    oldValue, label, value);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(
		    FieldAssignment.OBJECT_LABEL, label);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		Project.OBJECT_NAME, proj.getId(), proj.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(
		FieldAssignment.OBJECT_LABEL, oldLabel);
    }

    /**
     * Assign field to a task.
     */
    @Override
    @Transactional
    public String assignFieldToTask(TaskFieldAssignment taskField,
	    long fieldID, long taskID) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Field field = this.fieldDAO.getByID(fieldID);
	Task task = this.taskDAO.getByID(taskID);

	if (this.authHelper.isActionAuthorized(task)) {

	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.ASSIGN, task,
		    taskField);

	    // Do service.
	    taskField.setField(field);
	    taskField.setTask(task);
	    this.fieldDAO.assignFieldToTask(taskField);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateAssign(Field.OBJECT_NAME,
		    taskField.getLabel());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Task.OBJECT_NAME, task.getId(), task.getTitle()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateAssign(Field.OBJECT_NAME,
		taskField.getLabel());
    }

    /**
     * Unassign all fields from a task.
     */
    @Override
    @Transactional
    public String unassignAllFieldsFromTask(long taskID) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Task task = this.taskDAO.getByID(taskID);

	if (this.authHelper.isActionAuthorized(task)) {

	    // Log and notify.
	    this.messageHelper.sendUnassignAll(Field.OBJECT_NAME, task);

	    // Do service.
	    this.fieldDAO.unassignAllFieldsFromTask(taskID);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS
		    .generateUnassignAll(Field.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.UNASSIGN_ALL, Task.OBJECT_NAME, task.getId(),
		task.getTitle()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUnassignAll(Field.OBJECT_NAME);
    }

    /**
     * Unassign field from a task.
     */
    @Override
    @Transactional
    public String unassignFieldFromTask(long fieldID, long taskID,
	    String label, String value) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Task task = this.taskDAO.getByID(taskID);

	if (this.authHelper.isActionAuthorized(task)) {

	    // Log and notify.
	    TaskFieldAssignment fieldAssign = new TaskFieldAssignment();
	    Field field = this.fieldDAO.getByID(fieldID);
	    fieldAssign.setLabel(label);
	    fieldAssign.setTask(task);
	    fieldAssign.setValue(value);
	    fieldAssign.setField(field);
	    this.messageHelper.sendAssignUnassign(AuditAction.UNASSIGN, task,
		    fieldAssign);

	    // Do service.
	    this.fieldDAO.unassignFieldFromTask(fieldID, taskID, label, value);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUnassign(Field.OBJECT_NAME,
		    label);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		Task.OBJECT_NAME, task.getId(), task.getTitle()));

	// Return fail.
	return AlertBoxGenerator.FAILED
		.generateUnassign(Field.OBJECT_NAME, label);
    }

    /**
     * Update an assigned task field.
     */
    @Override
    @Transactional
    public String updateAssignedTaskField(long taskID, long fieldID,
	    String oldLabel, String oldValue, String label, String value) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Task task = this.taskDAO.getByID(taskID);

	if (this.authHelper.isActionAuthorized(task)) {

	    // Prepare.
	    // Delete the old version of the field.
	    this.fieldDAO.unassignFieldFromTask(fieldID, taskID, oldLabel,
		    oldValue);

	    // Save a new one.
	    TaskFieldAssignment newFieldAssignment = new TaskFieldAssignment();
	    Field field = this.fieldDAO.getByID(fieldID);
	    newFieldAssignment.setTask(task);
	    newFieldAssignment.setField(field);
	    newFieldAssignment.setLabel(label);
	    newFieldAssignment.setValue(value);

	    // Log and notify.
	    this.messageHelper.sendActionWithField(AuditAction.UPDATE, task,
		    newFieldAssignment);

	    // Do service.
	    this.fieldDAO.assignFieldToTask(newFieldAssignment);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(Field.OBJECT_NAME,
		    label);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		Task.OBJECT_NAME, task.getId(), task.getTitle()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(Field.OBJECT_NAME, label);
    }

    /**
     * Unassign a field from staff.
     */
    @Override
    @Transactional
    public String unassignFieldFromStaff(long fieldID, long staffID,
	    String label, String value) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Staff staff = this.staffDAO.getByID(staffID);

	if (this.authHelper.isActionAuthorized(staff)) {

	    // Log and notify.
	    this.messageHelper.sendActionWithField(AuditAction.UNASSIGN, staff,
		    fieldID, label);

	    // Do service.
	    this.fieldDAO
		    .unassignFieldFromStaff(fieldID, staffID, label, value);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUnassign(Field.OBJECT_NAME,
		    label);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		Staff.OBJECT_NAME, staff.getId(), staff.getFullName()));

	// Return fail.
	return AlertBoxGenerator.FAILED
		.generateUnassign(Field.OBJECT_NAME, label);
    }

    /**
     * Assign a field to a staff.
     */
    @Override
    @Transactional
    public String assignFieldToStaff(StaffFieldAssignment fieldAssignment,
	    long fieldID, long staffID) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Field field = this.fieldDAO.getByID(fieldID);
	Staff staff = this.staffDAO.getByID(staffID);
	String label = fieldAssignment.getLabel();

	if (this.authHelper.isActionAuthorized(staff)) {

	    // Log and notify.
	    this.messageHelper.sendActionWithField(AuditAction.ASSIGN, staff,
		    fieldID, label);

	    // Do service.
	    fieldAssignment.setField(field);
	    fieldAssignment.setStaff(staff);
	    this.fieldDAO.assignFieldToStaff(fieldAssignment);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateAssign(Field.OBJECT_NAME,
		    label);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Staff.OBJECT_NAME, staff.getId(), staff.getFullName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateAssign(Field.OBJECT_NAME, label);
    }

    /**
     * Unassign all fields from a staff.
     */
    @Override
    @Transactional
    public String unassignAllFieldsFromStaff(long staffID) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Staff staff = this.staffDAO.getByID(staffID);

	if (this.authHelper.isActionAuthorized(staff)) {

	    // Log and notify.
	    this.messageHelper.sendUnassignAll(Field.OBJECT_NAME, staff);

	    // Do service.
	    this.fieldDAO.unassignAllFieldsFromStaff(staffID);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS
		    .generateUnassignAll(Field.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.UNASSIGN_ALL, Staff.OBJECT_NAME, staff.getId(),
		staff.getFullName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUnassignAll(Field.OBJECT_NAME);
    }

    /**
     * Update an assigned staff field.
     */
    @Override
    @Transactional
    public String updateAssignedStaffField(long staffID, long fieldID,
	    String oldLabel, String oldValue, String label, String value) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Staff staff = this.staffDAO.getByID(staffID);

	if (this.authHelper.isActionAuthorized(staff)) {

	    // Log and notify.
	    this.messageHelper.sendActionWithField(AuditAction.UPDATE, staff,
		    fieldID, label);

	    // Prepare.
	    // Delete the old version of the field.
	    this.fieldDAO.unassignFieldFromStaff(fieldID, staffID, oldLabel,
		    oldValue);

	    // Save a new one.
	    StaffFieldAssignment newFieldAssignment = new StaffFieldAssignment();
	    Field field = this.fieldDAO.getByID(fieldID);
	    newFieldAssignment.setStaff(staff);
	    newFieldAssignment.setField(field);
	    newFieldAssignment.setLabel(label);
	    newFieldAssignment.setValue(value);

	    // Do service.
	    this.fieldDAO.assignFieldToStaff(newFieldAssignment);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(Field.OBJECT_NAME,
		    label);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		Staff.OBJECT_NAME, staff.getId(), staff.getFullName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(Field.OBJECT_NAME, label);
    }
}
