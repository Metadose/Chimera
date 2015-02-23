package com.cebedo.pmsys.task.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.task.dao.TaskDAO;
import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.task.model.TaskStaffAssignment;
import com.cebedo.pmsys.task.model.TaskTeamAssignment;

@Service
public class TaskServiceImpl implements TaskService {

	private TaskDAO taskDAO;
	private ProjectDAO projectDAO;

	public void setTaskDAO(TaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	public void setProjectDAO(ProjectDAO projDAO) {
		this.projectDAO = projDAO;
	}

	@Override
	@Transactional
	public void create(Task task) {
		this.taskDAO.create(task);
	}

	@Override
	@Transactional
	public Task getByID(long id) {
		return this.taskDAO.getByID(id);
	}

	@Override
	@Transactional
	public void update(Task task) {
		this.taskDAO.update(task);
	}

	@Override
	@Transactional
	public void delete(long id) {
		this.taskDAO.delete(id);
	}

	@Override
	@Transactional
	public List<Task> list() {
		return this.taskDAO.list();
	}

	@Override
	@Transactional
	public List<Task> listWithAllCollections() {
		return this.taskDAO.listWithAllCollections();
	}

	@Override
	@Transactional
	public Project getProjectByID(int id) {
		return this.projectDAO.getByID(id);
	}

	/**
	 * Set the task to the status specified.
	 */
	@Override
	@Transactional
	public void mark(long taskID, int status) {
		// Get the task.
		Task task = this.taskDAO.getByID(taskID);

		// Set the status and update.
		task.setStatus(status);
		this.taskDAO.update(task);
	}

	@Override
	@Transactional
	public void assignStaffTask(long taskID, long staffID) {
		TaskStaffAssignment taskStaffAssign = new TaskStaffAssignment();
		taskStaffAssign.setTaskID(taskID);
		taskStaffAssign.setStaffID(staffID);
		this.taskDAO.assignStaffTask(taskStaffAssign);
	}

	@Override
	@Transactional
	public void assignTeamTask(long taskID, long teamID) {
		TaskTeamAssignment taskTeamAssign = new TaskTeamAssignment();
		taskTeamAssign.setTaskID(taskID);
		taskTeamAssign.setTeamID(teamID);
		this.taskDAO.assignTeamTask(taskTeamAssign);
	}

	@Override
	@Transactional
	public Task getByIDWithAllCollections(int id) {
		return this.taskDAO.getByIDWithAllCollections(id);
	}

}
