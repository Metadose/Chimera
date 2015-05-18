package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.model.Task;

public interface TaskService {

	public void create(Task task);

	public Task getByID(long id);

	public void update(Task task);

	public void delete(long id);

	public List<Task> list();

	public List<Task> listWithAllCollections();

	public void mark(long taskID, int status);

	public void assignStaffTask(long taskID, long staffID);

	public void assignTeamTask(long taskID, long teamID);

	public Task getByIDWithAllCollections(long id);

	public void unassignTeamTask(long taskID, long teamID);

	/**
	 * Unassign all teams in a given task.
	 * 
	 * @param taskID
	 */
	public void unassignAllTeamsInTask(long taskID);

	/**
	 * Unassign a staff from a task.
	 * 
	 * @param taskID
	 * @param staffID
	 */
	public void unassignStaffTask(long taskID, long staffID);

	/**
	 * Unassign all staff from a task.
	 * 
	 * @param taskID
	 */
	public void unassignAllStaffTasks(long taskID);

	public void deleteAllTasksByProject(long projectID);

	public void createWithProject(Task task, long projectID);

	public void merge(Task task);

	public String getTitleByID(long taskID);

	public void unassignAllTasksByProject(long projectID);

	public void unassignTaskByProject(long taskID, long projectID);
}
