package com.cebedo.pmsys.task.service;

import java.util.List;

import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.task.model.Task;

public interface TaskService {

	public void create(Task task);

	public Task getByID(long id);

	public void update(Task task);

	public void delete(long id);

	public List<Task> list();

	public List<Task> listWithAllCollections();

	public Project getProjectByID(int id);

	public void mark(long taskID, int status);

	public void assignStaffTask(long taskID, long staffID);

	public void assignTeamTask(long taskID, long teamID);

	public Task getByIDWithAllCollections(int id);

	public void unassignTeamTask(long taskID, long teamID);

	public void unassignAllTeamTasks(long taskID);

	public void unassignStaffTask(long taskID, long staffID);

	public void unassignAllStaffTasks(long id);
}
