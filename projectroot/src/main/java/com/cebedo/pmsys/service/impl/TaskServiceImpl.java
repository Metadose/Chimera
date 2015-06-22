package com.cebedo.pmsys.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.TaskDAO;
import com.cebedo.pmsys.dao.TeamDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.TaskStaffAssignment;
import com.cebedo.pmsys.model.assignment.TaskTeamAssignment;
import com.cebedo.pmsys.service.TaskService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class TaskServiceImpl implements TaskService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private LogHelper logHelper = new LogHelper();
    private static Logger logger = Logger.getLogger(Task.OBJECT_NAME);

    private TaskDAO taskDAO;
    private ProjectDAO projectDAO;
    private StaffDAO staffDAO;
    private TeamDAO teamDAO;

    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public void setTeamDAO(TeamDAO teamDAO) {
	this.teamDAO = teamDAO;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
	this.staffDAO = staffDAO;
    }

    public void setTaskDAO(TaskDAO taskDAO) {
	this.taskDAO = taskDAO;
    }

    /**
     * Create a new task.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#task.getProject().getId()", condition = "#task.getProject() != null")
    @Override
    @Transactional
    public String create(Task task) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Company authCompany = auth.getCompany();
	task.setCompany(authCompany);
	if (this.authHelper.isActionAuthorized(task)) {
	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.CREATE, task);

	    // Do service.
	    this.taskDAO.create(task);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateCreate(Task.OBJECT_NAME,
		    task.getTitle());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE,
		Task.OBJECT_NAME, task.getId(), task.getTitle()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateCreate(Task.OBJECT_NAME,
		task.getTitle());
    }

    /**
     * Get an object given id.
     */
    @Override
    @Transactional
    public Task getByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Task task = this.taskDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(task)) {
	    // Log the get.
	    logger.info(this.logHelper.logGetObject(auth, Task.OBJECT_NAME, id,
		    task.getTitle()));

	    // Return obj.
	    return task;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE,
		Task.OBJECT_NAME, id, task.getTitle()));

	// Return empty.
	return new Task();
    }

    /**
     * A task object has many relationships to other objects. Merging this with
     * an existing object would be safer than Updating the existing one.
     */
    @Override
    @Transactional
    public String update(Task task) {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (this.authHelper.isActionAuthorized(task)) {
	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.UPDATE, task);

	    // Do service.
	    this.taskDAO.update(task);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(Task.OBJECT_NAME,
		    task.getTitle());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		Task.OBJECT_NAME, task.getId(), task.getTitle()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(Task.OBJECT_NAME,
		task.getTitle());
    }

    /**
     * Delete a task.
     */
    @Override
    @Transactional
    public String delete(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Task task = this.taskDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(task)) {
	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.DELETE, task);

	    // Do service.
	    this.taskDAO.delete(id);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateDelete(Task.OBJECT_NAME,
		    task.getTitle());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE,
		Task.OBJECT_NAME, id, task.getTitle()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateDelete(Task.OBJECT_NAME,
		task.getTitle());
    }

    /**
     * List all tasks.
     */
    @Override
    @Transactional
    public List<Task> list() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    // Log info super admin.
	    logger.info(this.logHelper.logListAsSuperAdmin(token,
		    Task.OBJECT_NAME));

	    // Return list.
	    return this.taskDAO.list(null);
	}

	// Log warn.
	Company company = token.getCompany();
	logger.info(this.logHelper.logListFromCompany(token, Task.OBJECT_NAME,
		company));

	// Return list.
	return this.taskDAO.list(company.getId());
    }

    /**
     * List all tasks with all collections.
     */
    @Override
    @Transactional
    public List<Task> listWithAllCollections() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    // Log info super admin.
	    logger.info(this.logHelper.logListWithCollectionsAsSuperAdmin(
		    token, Task.OBJECT_NAME));

	    // Return list.
	    return this.taskDAO.listWithAllCollections(null);
	}

	// Log info non-super admin.
	Company company = token.getCompany();
	logger.info(this.logHelper.logListWithCollectionsFromCompany(token,
		Task.OBJECT_NAME, company));

	// Return list.
	return this.taskDAO.listWithAllCollections(company.getId());
    }

    /**
     * Set the task to the status specified.
     */
    @Override
    @Transactional
    public String mark(long taskID, int status) {
	// Get the task.
	AuthenticationToken auth = this.authHelper.getAuth();
	Task task = this.taskDAO.getByID(taskID);

	// Set the status and update, if authorized.
	if (this.authHelper.isActionAuthorized(task)) {

	    // Log and notify.
	    TaskStatus taskStatus = TaskStatus.of(status);
	    this.messageHelper
		    .sendMarkAs(AuditAction.MARK_AS, taskStatus, task);

	    // Do service.
	    task.setStatus(status);
	    this.taskDAO.update(task);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateMarkAs(Task.OBJECT_NAME,
		    task.getTitle());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.MARK_AS,
		Task.OBJECT_NAME, task.getId(), task.getTitle()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateMarkAs(Task.OBJECT_NAME,
		task.getTitle());
    }

    /**
     * Assign a task under a staff.
     */
    @Override
    @Transactional
    public String assignStaffTask(long taskID, long staffID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Task task = this.taskDAO.getByID(taskID);
	Staff staff = this.staffDAO.getByID(staffID);

	if (this.authHelper.isActionAuthorized(task)
		&& this.authHelper.isActionAuthorized(staff)) {

	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.ASSIGN, staff,
		    task);

	    // Do service.
	    TaskStaffAssignment taskStaffAssign = new TaskStaffAssignment();
	    taskStaffAssign.setTaskID(taskID);
	    taskStaffAssign.setStaffID(staffID);
	    this.taskDAO.assignStaffTask(taskStaffAssign);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateAssign(Staff.OBJECT_NAME,
		    staff.getFullName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Task.OBJECT_NAME, task.getId(), task.getTitle()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Staff.OBJECT_NAME, staff.getId(), staff.getFullName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateAssign(Staff.OBJECT_NAME,
		staff.getFullName());
    }

    /**
     * Assign task to a team.
     */
    @Override
    @Transactional
    public String assignTeamTask(long taskID, long teamID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Task task = this.taskDAO.getByID(taskID);
	Team team = this.teamDAO.getByID(teamID);

	if (this.authHelper.isActionAuthorized(task)
		&& this.authHelper.isActionAuthorized(team)) {

	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.ASSIGN, team,
		    task);

	    // Do service.
	    TaskTeamAssignment taskTeamAssign = new TaskTeamAssignment();
	    taskTeamAssign.setTaskID(taskID);
	    taskTeamAssign.setTeamID(teamID);
	    this.taskDAO.assignTeamTask(taskTeamAssign);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateAssign(Team.OBJECT_NAME,
		    team.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Task.OBJECT_NAME, task.getId(), task.getTitle()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Team.OBJECT_NAME, team.getId(), team.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateAssign(Team.OBJECT_NAME,
		team.getName());
    }

    /**
     * Get object with all collections.
     */
    @Override
    @Transactional
    public Task getByIDWithAllCollections(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Task task = this.taskDAO.getByIDWithAllCollections(id);

	if (this.authHelper.isActionAuthorized(task)) {
	    // Log info.
	    logger.info(this.logHelper.logGetObjectWithAllCollections(auth,
		    Task.OBJECT_NAME, id, task.getTitle()));

	    // Return obj.
	    return task;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.GET_WITH_COLLECTIONS, Task.OBJECT_NAME, id,
		task.getTitle()));

	// Return empty.
	return new Task();
    }

    /**
     * Unassign a team under task.
     */
    @Override
    @Transactional
    public String unassignTeamTask(long taskID, long teamID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Task task = this.taskDAO.getByID(taskID);
	Team team = this.teamDAO.getByID(teamID);

	if (this.authHelper.isActionAuthorized(task)
		&& this.authHelper.isActionAuthorized(team)) {

	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.UNASSIGN, team,
		    task);

	    // Do service.
	    this.taskDAO.unassignTeamTask(taskID, teamID);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateAssign(Task.OBJECT_NAME,
		    task.getTitle());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Task.OBJECT_NAME, task.getId(), task.getTitle()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.ASSIGN,
		Team.OBJECT_NAME, team.getId(), team.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateAssign(Task.OBJECT_NAME,
		task.getTitle());
    }

    /**
     * Unassign all teams assigned to a task.
     */
    @Override
    @Transactional
    public String unassignAllTeamsInTask(long taskID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Task task = this.taskDAO.getByID(taskID);

	if (this.authHelper.isActionAuthorized(task)) {

	    // Log and notify.
	    this.messageHelper.sendUnassignAll(Team.OBJECT_NAME, task);

	    // Do service.
	    this.taskDAO.unassignAllTeamTasks(taskID);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS
		    .generateUnassignAll(Team.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.UNASSIGN_ALL, Task.OBJECT_NAME, task.getId(),
		task.getTitle()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUnassignAll(Team.OBJECT_NAME);
    }

    /**
     * Unassign a staff from a task. TODO Pass actual objects rather than ID's
     * only.
     */
    @Override
    @Transactional
    public String unassignStaffTask(long taskID, long staffID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Task task = this.taskDAO.getByID(taskID);
	Staff staff = this.staffDAO.getByID(staffID);

	if (this.authHelper.isActionAuthorized(task)
		&& this.authHelper.isActionAuthorized(staff)) {

	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.UNASSIGN, staff,
		    task);

	    // Do service.
	    this.taskDAO.unassignStaffTask(taskID, staffID);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUnassign(Staff.OBJECT_NAME,
		    staff.getFullName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		Task.OBJECT_NAME, task.getId(), task.getTitle()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		Staff.OBJECT_NAME, staff.getId(), staff.getFullName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUnassign(Staff.OBJECT_NAME,
		staff.getFullName());
    }

    /**
     * Unassign all staff linked to a task.
     */
    @Override
    @Transactional
    public String unassignAllStaffTasks(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Task task = this.taskDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(task)) {
	    // Log and notify.
	    this.messageHelper.sendUnassignAll(Staff.OBJECT_NAME, task);

	    // Do service.
	    this.taskDAO.unassignAllStaffTasks(id);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS
		    .generateUnassignAll(Staff.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.UNASSIGN_ALL, Task.OBJECT_NAME, id, task.getTitle()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUnassignAll(Staff.OBJECT_NAME);
    }

    /**
     * Delete all tasks given a project.
     */
    @Override
    @Transactional
    public String deleteAllTasksByProject(long projectID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(projectID);

	if (this.authHelper.isActionAuthorized(project)) {

	    // Log and notify.
	    this.messageHelper.sendDeleteAll(Task.OBJECT_NAME, project);

	    // Do service.
	    this.taskDAO.deleteAllTasksByProject(projectID);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateDeleteAll(Task.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.DELETE_ALL, Project.OBJECT_NAME, project.getId(),
		project.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateDeleteAll(Task.OBJECT_NAME);
    }

    /**
     * Create a task with a linked project.
     */
    @Override
    @Transactional
    public String createWithProject(Task task, long projectID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project proj = this.projectDAO.getByID(projectID);

	if (this.authHelper.isActionAuthorized(proj)) {
	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.CREATE, task);

	    // Do service.
	    task.setProject(proj);
	    Company authCompany = auth.getCompany();
	    task.setCompany(authCompany);
	    this.taskDAO.create(task);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateCreate(Task.OBJECT_NAME,
		    task.getTitle());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE,
		Task.OBJECT_NAME, task.getId(), task.getTitle()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateCreate(Task.OBJECT_NAME,
		task.getTitle());
    }

    /**
     * Update a task.
     */
    @Override
    @Transactional
    public String merge(Task task) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Task oldTask = this.taskDAO.getByIDWithAllCollections(task.getId());

	if (this.authHelper.isActionAuthorized(oldTask)) {

	    // Prepare object.
	    task.setCompany(oldTask.getCompany());
	    task.setFields(oldTask.getFields());
	    if (task.getProject() == null) {
		task.setProject(oldTask.getProject());
	    }
	    if (task.getStaff() == null) {
		task.setStaff(oldTask.getStaff());
	    }
	    task.setTeams(oldTask.getTeams());

	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.UPDATE, oldTask);

	    // Do service.
	    this.taskDAO.merge(task);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(Task.OBJECT_NAME,
		    task.getTitle());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		Task.OBJECT_NAME, task.getId(), task.getTitle()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(Task.OBJECT_NAME,
		task.getTitle());
    }

    /**
     * Get task title given an id.
     */
    @Override
    @Transactional
    public String getTitleByID(long taskID) {
	return this.taskDAO.getTitleByID(taskID);
    }

    /**
     * Unassign all tasks in a project.
     */
    @Override
    @Transactional
    public String unassignAllTasksByProject(long projectID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(projectID);

	if (this.authHelper.isActionAuthorized(project)) {
	    // Log and notify.
	    this.messageHelper.sendUnassignAll(Task.OBJECT_NAME, project);

	    // Do service.
	    this.taskDAO.unassignAllTasksByProject(project);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS
		    .generateUnassignAll(Task.OBJECT_NAME);
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.UNASSIGN_ALL, Project.OBJECT_NAME, project.getId(),
		project.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUnassignAll(Task.OBJECT_NAME);
    }

    /**
     * Unassign a task in a project.
     */
    @Override
    @Transactional
    public String unassignTaskByProject(long taskID, long projectID) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(projectID);
	Task task = this.taskDAO.getByID(taskID);

	if (this.authHelper.isActionAuthorized(project)
		&& this.authHelper.isActionAuthorized(task)) {

	    // Log and notify.
	    this.messageHelper.sendAssignUnassign(AuditAction.UNASSIGN,
		    project, task);

	    // Do service.
	    this.taskDAO.unassignTaskByProject(taskID, project);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUnassign(Task.OBJECT_NAME,
		    task.getTitle());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		Project.OBJECT_NAME, project.getId(), project.getName()));
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UNASSIGN,
		Task.OBJECT_NAME, task.getId(), task.getTitle()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUnassign(Task.OBJECT_NAME,
		task.getTitle());
    }
}
