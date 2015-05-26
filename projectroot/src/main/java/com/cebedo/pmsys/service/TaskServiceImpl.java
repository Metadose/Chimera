package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.TaskDAO;
import com.cebedo.pmsys.dao.TeamDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.TaskStaffAssignment;
import com.cebedo.pmsys.model.assignment.TaskTeamAssignment;
import com.cebedo.pmsys.token.AuthenticationToken;

@Service
public class TaskServiceImpl implements TaskService {

	private AuthHelper authHelper = new AuthHelper();
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

	@CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#task.getProject().getId()", condition = "#task.getProject() != null")
	@Override
	@Transactional
	public void create(Task task) {
		AuthenticationToken auth = this.authHelper.getAuth();
		Company authCompany = auth.getCompany();
		task.setCompany(authCompany);
		this.taskDAO.create(task);
	}

	@Override
	@Transactional
	public Task getByID(long id) {
		Task task = this.taskDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(task)) {
			return task;
		}
		return new Task();
	}

	/**
	 * A task object has many relationships to other objects. Merging this with
	 * an existing object would be safer than Updating the existing one.
	 */
	@Override
	@Transactional
	public void update(Task task) {
		if (this.authHelper.isActionAuthorized(task)) {
			this.taskDAO.update(task);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		Task task = this.taskDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(task)) {
			this.taskDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<Task> list() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.taskDAO.list(null);
		}
		return this.taskDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public List<Task> listWithAllCollections() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.taskDAO.listWithAllCollections(null);
		}
		return this.taskDAO.listWithAllCollections(token.getCompany().getId());
	}

	/**
	 * Set the task to the status specified.
	 */
	@Override
	@Transactional
	public void mark(long taskID, int status) {
		// Get the task.
		Task task = this.taskDAO.getByID(taskID);

		// Set the status and update, if authorized.
		if (this.authHelper.isActionAuthorized(task)) {
			task.setStatus(status);
			this.taskDAO.update(task);
		}
	}

	@Override
	@Transactional
	public void assignStaffTask(long taskID, long staffID) {
		Task task = this.taskDAO.getByID(taskID);
		Staff staff = this.staffDAO.getByID(staffID);
		if (this.authHelper.isActionAuthorized(task)
				&& this.authHelper.isActionAuthorized(staff)) {
			TaskStaffAssignment taskStaffAssign = new TaskStaffAssignment();
			taskStaffAssign.setTaskID(taskID);
			taskStaffAssign.setStaffID(staffID);
			this.taskDAO.assignStaffTask(taskStaffAssign);
		}
	}

	@Override
	@Transactional
	public void assignTeamTask(long taskID, long teamID) {
		Task task = this.taskDAO.getByID(taskID);
		Team team = this.teamDAO.getByID(teamID);
		if (this.authHelper.isActionAuthorized(task)
				&& this.authHelper.isActionAuthorized(team)) {
			TaskTeamAssignment taskTeamAssign = new TaskTeamAssignment();
			taskTeamAssign.setTaskID(taskID);
			taskTeamAssign.setTeamID(teamID);
			this.taskDAO.assignTeamTask(taskTeamAssign);
		}
	}

	@Override
	@Transactional
	public Task getByIDWithAllCollections(long id) {
		Task task = this.taskDAO.getByIDWithAllCollections(id);
		if (this.authHelper.isActionAuthorized(task)) {
			return task;
		}
		return new Task();
	}

	@Override
	@Transactional
	public void unassignTeamTask(long taskID, long teamID) {
		Task task = this.taskDAO.getByID(taskID);
		Team team = this.teamDAO.getByID(teamID);
		if (this.authHelper.isActionAuthorized(task)
				&& this.authHelper.isActionAuthorized(team)) {
			this.taskDAO.unassignTeamTask(taskID, teamID);
		}
	}

	@Override
	@Transactional
	public void unassignAllTeamsInTask(long taskID) {
		Task task = this.taskDAO.getByID(taskID);
		if (this.authHelper.isActionAuthorized(task)) {
			this.taskDAO.unassignAllTeamTasks(taskID);
		}
	}

	/**
	 * Unassign a staff from a task. TODO Pass actual objects rather than ID's
	 * only.
	 */
	@Override
	@Transactional
	public void unassignStaffTask(long taskID, long staffID) {
		Task task = this.taskDAO.getByID(taskID);
		Staff staff = this.staffDAO.getByID(staffID);
		if (this.authHelper.isActionAuthorized(task)
				&& this.authHelper.isActionAuthorized(staff)) {
			this.taskDAO.unassignStaffTask(taskID, staffID);
		}
	}

	@Override
	@Transactional
	public void unassignAllStaffTasks(long id) {
		Task task = this.taskDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(task)) {
			this.taskDAO.unassignAllStaffTasks(id);
		}
	}

	@Override
	@Transactional
	public void deleteAllTasksByProject(long projectID) {
		Project project = this.projectDAO.getByID(projectID);
		if (this.authHelper.isActionAuthorized(project)) {
			this.taskDAO.deleteAllTasksByProject(projectID);
		}
	}

	@Override
	@Transactional
	public void createWithProject(Task task, long projectID) {
		Project proj = this.projectDAO.getByID(projectID);
		if (this.authHelper.isActionAuthorized(proj)) {
			task.setProject(proj);
			AuthenticationToken auth = this.authHelper.getAuth();
			Company authCompany = auth.getCompany();
			task.setCompany(authCompany);
			this.taskDAO.create(task);
		}
	}

	@Override
	@Transactional
	public void merge(Task task) {
		Task oldTask = this.taskDAO.getByIDWithAllCollections(task.getId());
		if (this.authHelper.isActionAuthorized(oldTask)) {
			task.setCompany(oldTask.getCompany());
			task.setFields(oldTask.getFields());
			if (task.getProject() == null) {
				task.setProject(oldTask.getProject());
			}
			if (task.getStaff() == null) {
				task.setStaff(oldTask.getStaff());
			}
			task.setTeams(oldTask.getTeams());
			this.taskDAO.merge(task);
		}
	}

	@Override
	@Transactional
	public String getTitleByID(long taskID) {
		return this.taskDAO.getTitleByID(taskID);
	}

	@Override
	@Transactional
	public void unassignAllTasksByProject(long projectID) {
		Project project = this.projectDAO.getByID(projectID);
		this.taskDAO.unassignAllTasksByProject(project);
	}

	@Override
	@Transactional
	public void unassignTaskByProject(long taskID, long projectID) {
		Project project = this.projectDAO.getByID(projectID);
		this.taskDAO.unassignTaskByProject(taskID, project);
	}
}