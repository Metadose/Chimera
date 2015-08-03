package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Task;

public interface TaskService {

    /**
     * Create a task.
     * 
     * @param task
     * @return
     */
    public String create(Task task);

    public Task getByID(long id);

    /**
     * Update a task.
     * 
     * @param task
     * @return
     */
    public String update(Task task);

    /**
     * Delete a task.
     * 
     * @param id
     * @return
     */
    public String delete(long id);

    public List<Task> list();

    public List<Task> listWithAllCollections();

    /**
     * Set the task to the status specified.
     * 
     * @param taskID
     * @param status
     * @return
     */
    public String mark(long taskID, int status);

    /**
     * Assign a staff under task.
     * 
     * @param taskID
     * @param staffID
     * @return
     */
    public String assignStaffTask(long taskID, long staffID);

    /**
     * Assign a team under task.
     * 
     * @param taskID
     * @param staffID
     * @return
     */
    public String assignTeamTask(long taskID, long teamID);

    public Task getByIDWithAllCollections(long id);

    /**
     * Unassign a team under task.
     * 
     * @param taskID
     * @param teamID
     * @return
     */
    public String unassignTeamTask(long taskID, long teamID);

    /**
     * Unassign all teams in a given task.
     * 
     * @param taskID
     * @return
     */
    public String unassignAllTeamsInTask(long taskID);

    /**
     * Unassign a staff from a task.
     * 
     * @param taskID
     * @param staffID
     * @return
     */
    public String unassignStaffTask(long taskID, long staffID);

    /**
     * Unassign all staff from a task.
     * 
     * @param taskID
     * @return
     */
    public String unassignAllStaffTasks(long taskID);

    /**
     * Delete all tasks given a project.
     * 
     * @param projectID
     * @return
     */
    public String deleteAllTasksByProject(long projectID);

    /**
     * Create a task with a linked project.
     * 
     * @param task
     * @param projectID
     * @return
     */
    public String createWithProject(Task task, long projectID);

    /**
     * Update a task.
     * 
     * @param task
     * @return
     */
    public String merge(Task task);

    public String getTitleByID(long taskID);

    /**
     * Unassign all tasks given a project.
     * 
     * @param projectID
     * @return
     */
    public String unassignAllTasksByProject(long projectID);

    /**
     * Unassign a task given a project.
     * 
     * @param taskID
     * @param projectID
     * @return
     */
    public String unassignTaskByProject(long taskID, long projectID);

    public void createMassTasks(List<Task> tasks);

    public List<Task> convertExcelToTaskList(MultipartFile multipartFile, Project project);

}