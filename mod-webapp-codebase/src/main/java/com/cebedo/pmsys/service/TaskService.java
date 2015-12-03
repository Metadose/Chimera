package com.cebedo.pmsys.service;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.RegistryCache;
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
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#task.project.id")
    public String create(Task task, BindingResult result);

    public Task getByID(long id);

    /**
     * Update a task.
     * 
     * @param task
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#task.project.id")
    public String update(Task task, BindingResult result);

    /**
     * Delete a task.
     * 
     * @param id
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String delete(long id, long projectId);

    /**
     * Set the task to the status specified.
     * 
     * @param taskID
     * @param status
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String mark(long taskID, int status, long projectId);

    /**
     * Assign a staff under task.
     * 
     * @param taskID
     * @param staffID
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String assignStaffTask(long taskID, long staffID, long projectId);

    /**
     * 
     * @param taskID
     * @param staffID
     * @return
     */

    public Task getByIDWithAllCollections(long id);

    /**
     * Unassign a staff from a task.
     * 
     * @param taskID
     * @param staffID
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String unassignStaffTask(long taskID, long staffID, long projectId);

    /**
     * Unassign all staff from a task.
     * 
     * @param taskID
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String unassignAllStaffUnderTask(long taskID, long projectId);

    /**
     * Delete all tasks given a project.
     * 
     * @param projectID
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String deleteAllTasksByProject(long projectID);

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#project.id")
    public String createMassTasks(Project project, List<Task> tasks, BindingResult result);

    public List<Task> convertExcelToTaskList(MultipartFile multipartFile, Project project);

}