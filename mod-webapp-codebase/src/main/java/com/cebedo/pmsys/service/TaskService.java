package com.cebedo.pmsys.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Task;

public interface TaskService {

    public HSSFWorkbook exportXLS(long projID);

    /**
     * Create a task.
     * 
     * @param task
     * @param result
     * @return
     */
    public String create(Task task, BindingResult result);

    public Task getByID(long id);

    /**
     * Update a task.
     * 
     * @param task
     * @return
     */
    public String update(Task task, BindingResult result);

    /**
     * Delete a task.
     * 
     * @param id
     * @return
     */
    public String delete(long id);

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
     * 
     * @param taskID
     * @param staffID
     * @return
     */

    public Task getByIDWithAllCollections(long id);

    /**
     * 
     * @param taskID
     * @return
     */

    /**
     * 
     * @param taskID
     * @return
     */

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
    public String unassignAllStaffUnderTask(long taskID);

    /**
     * Delete all tasks given a project.
     * 
     * @param projectID
     * @return
     */
    public String deleteAllTasksByProject(long projectID);

    public String createMassTasks(Project project, List<Task> tasks, BindingResult result);

    public List<Task> convertExcelToTaskList(MultipartFile multipartFile, Project project);

}