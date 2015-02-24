package com.cebedo.pmsys.task.dao;

import java.util.List;

import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.task.model.TaskStaffAssignment;
import com.cebedo.pmsys.task.model.TaskTeamAssignment;

public interface TaskDAO {

	public void create(Task task);

	public Task getByID(long id);

	public void update(Task task);

	public void delete(long id);

	public List<Task> list();

	public List<Task> listWithAllCollections();

	public void assignStaffTask(TaskStaffAssignment taskStaffAssign);

	public void assignTeamTask(TaskTeamAssignment taskTeamAssign);

	public Task getByIDWithAllCollections(long id);

	public void unassignTeamTask(long taskID, long teamID);

	public void unassignAllTeamTasks(long taskID);

	public void unassignStaffTask(long taskID, long staffID);

	public void unassignAllStaffTasks(long id);
}
