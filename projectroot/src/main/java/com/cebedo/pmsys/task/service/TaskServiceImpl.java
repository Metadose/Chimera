package com.cebedo.pmsys.task.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthUtils;
import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.dao.StaffDAO;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.task.dao.TaskDAO;
import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.task.model.TaskStaffAssignment;
import com.cebedo.pmsys.task.model.TaskTeamAssignment;
import com.cebedo.pmsys.team.dao.TeamDAO;
import com.cebedo.pmsys.team.model.Team;

@Service
public class TaskServiceImpl implements TaskService {

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

	@Override
	@Transactional
	public void create(Task task) {
		this.taskDAO.create(task);
		AuthenticationToken auth = AuthUtils.getAuth();
		Company authCompany = auth.getCompany();
		if (AuthUtils.notNullObjNotSuperAdmin(authCompany)) {
			task.setCompany(authCompany);
			this.taskDAO.update(task);
		}
	}

	@Override
	@Transactional
	public Task getByID(long id) {
		Task task = this.taskDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(task)) {
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
		if (AuthUtils.isActionAuthorized(task)) {
			this.taskDAO.update(task);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		Task task = this.taskDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(task)) {
			this.taskDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<Task> list() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.taskDAO.list(null);
		}
		return this.taskDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public List<Task> listWithAllCollections() {
		AuthenticationToken token = AuthUtils.getAuth();
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
		if (AuthUtils.isActionAuthorized(task)) {
			task.setStatus(status);
			this.taskDAO.update(task);
		}
	}

	@Override
	@Transactional
	public void assignStaffTask(long taskID, long staffID) {
		Task task = this.taskDAO.getByID(taskID);
		Staff staff = this.staffDAO.getByID(staffID);
		if (AuthUtils.isActionAuthorized(task)
				&& AuthUtils.isActionAuthorized(staff)) {
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
		if (AuthUtils.isActionAuthorized(task)
				&& AuthUtils.isActionAuthorized(team)) {
			TaskTeamAssignment taskTeamAssign = new TaskTeamAssignment();
			taskTeamAssign.setTaskID(taskID);
			taskTeamAssign.setTeamID(teamID);
			this.taskDAO.assignTeamTask(taskTeamAssign);
		}
	}

	@Override
	@Transactional
	public Task getByIDWithAllCollections(int id) {
		Task task = this.taskDAO.getByIDWithAllCollections(id);
		if (AuthUtils.isActionAuthorized(task)) {
			return task;
		}
		return new Task();
	}

	@Override
	@Transactional
	public void unassignTeamTask(long taskID, long teamID) {
		Task task = this.taskDAO.getByID(taskID);
		Team team = this.teamDAO.getByID(teamID);
		if (AuthUtils.isActionAuthorized(task)
				&& AuthUtils.isActionAuthorized(team)) {
			this.taskDAO.unassignTeamTask(taskID, teamID);
		}
	}

	@Override
	@Transactional
	public void unassignAllTeamTasks(long taskID) {
		Task task = this.taskDAO.getByID(taskID);
		if (AuthUtils.isActionAuthorized(task)) {
			this.taskDAO.unassignAllTeamTasks(taskID);
		}
	}

	@Override
	@Transactional
	public void unassignStaffTask(long taskID, long staffID) {
		Task task = this.taskDAO.getByID(taskID);
		Staff staff = this.staffDAO.getByID(staffID);
		if (AuthUtils.isActionAuthorized(task)
				&& AuthUtils.isActionAuthorized(staff)) {
			this.taskDAO.unassignStaffTask(taskID, staffID);
		}
	}

	@Override
	@Transactional
	public void unassignAllStaffTasks(long id) {
		Task task = this.taskDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(task)) {
			this.taskDAO.unassignAllStaffTasks(id);
		}
	}

	@Override
	@Transactional
	public void unassignAllProjectTasks(long projectID) {
		Project project = this.projectDAO.getByID(projectID);
		if (AuthUtils.isActionAuthorized(project)) {
			this.taskDAO.unassignAllProjectTasks(projectID);
		}
	}

	@Override
	@Transactional
	public void createWithProject(Task task, long projectID) {
		Project proj = this.projectDAO.getByID(projectID);
		if (AuthUtils.isActionAuthorized(proj)) {
			task.setProject(proj);
			this.taskDAO.create(task);

			AuthenticationToken auth = AuthUtils.getAuth();
			Company authCompany = auth.getCompany();
			if (AuthUtils.notNullObjNotSuperAdmin(authCompany)) {
				task.setCompany(authCompany);
				this.taskDAO.update(task);
			}
		}
	}

	@Override
	@Transactional
	public void merge(Task task) {
		Task oldTask = this.taskDAO.getByIDWithAllCollections(task.getId());
		if (AuthUtils.isActionAuthorized(oldTask)) {
			task.setCompany(oldTask.getCompany());
			task.setFields(oldTask.getFields());
			task.setProject(oldTask.getProject());
			task.setStaff(oldTask.getStaff());
			task.setTeams(oldTask.getTeams());
			this.taskDAO.merge(task);
		}
	}
}
