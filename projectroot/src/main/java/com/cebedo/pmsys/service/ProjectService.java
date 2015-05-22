package com.cebedo.pmsys.service;

import java.util.List;
import java.util.Map;

import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.model.Project;

public interface ProjectService {

    public void create(Project project);

    public Project getByID(long projectID);

    public void update(Project project);

    public void delete(long id);

    public List<Project> list();

    public List<Project> listWithAllCollections();

    public Project getByIDWithAllCollections(long id);

    public List<Project> listWithTasks();

    public String getNameByID(long projectID);

    public void clearProjectCache(long projectID);

    public void clearSearchCache(Long companyID);

    public String getGanttJSON(Project proj);

    public Map<String, Object> getMilestoneSummaryMap(Project proj);

    public Map<String, Object> getPayrollMap(Project proj);

    public Map<TaskStatus, Integer> getTaskStatusCountMap(Project proj);

}