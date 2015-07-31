package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.TaskStaffAssignment;
import com.cebedo.pmsys.model.assignment.TaskTeamAssignment;

public interface TaskDAO {

    public void create(Task task);

    public Task getByID(long id);

    public void update(Task task);

    public void delete(long id);

    public List<Task> list(Long companyID);

    public List<Task> listWithAllCollections(Long companyID);

    public void assignStaffTask(TaskStaffAssignment taskStaffAssign);

    public void assignTeamTask(TaskTeamAssignment taskTeamAssign);

    public Task getByIDWithAllCollections(long id);

    public void unassignTeamTask(long taskID, long teamID);

    public void unassignAllTeamTasks(long taskID);

    public void unassignStaffTask(long taskID, long staffID);

    public void unassignAllStaffTasks(long id);

    public List<Staff> getStaffByTaskID(long taskID);

    public List<Team> getTeamByTaskID(long taskID);

    public void deleteAllTasksByProject(long projectID);

    public void merge(Task task);

    public String getTitleByID(long taskID);

    public void unassignAllTasksByProject(Project project);

    public void unassignTaskByProject(long taskID, Project project);

    public List<Task> listTaskFromCache(Long companyID);

}
