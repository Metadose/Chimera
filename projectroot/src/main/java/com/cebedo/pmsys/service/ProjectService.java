package com.cebedo.pmsys.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.model.Project;

public interface ProjectService {

    /**
     * Create a new project.
     * 
     * @param project
     * @return
     */
    public String create(Project project);

    public Project getByID(long projectID);

    /**
     * Update a project.
     * 
     * @param project
     * @return
     */
    public String update(Project project);

    /**
     * Delete a project.
     * 
     * @param id
     * @return
     */
    public String delete(long id);

    public List<Project> list();

    public List<Project> listWithAllCollections();

    public Project getByIDWithAllCollections(long id);

    public String getGanttJSON(Project proj);

    public Map<TaskStatus, Integer> getTaskStatusCountMap(Project proj);

    public String getCalendarJSON(Project proj);

    public String createStaffFromExcel(MultipartFile multipartFile, Project proj);

    public String createTasksFromExcel(MultipartFile multipartFile, Project project);

}