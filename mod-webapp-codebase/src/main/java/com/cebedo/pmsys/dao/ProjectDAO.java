package com.cebedo.pmsys.dao;

import java.util.List;
import java.util.Set;

import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Project;

public interface ProjectDAO {

    public void create(Project project);

    public Project getByID(long id);

    public void update(Project project);

    public void delete(long id);

    public List<Project> list(Long companyID);

    public List<Project> listWithAllCollections(Long companyID);

    public Project getByIDWithAllCollections(long id);

    public List<Project> listWithTasks(Long id);

    public String getNameByID(long projectID);

    public void merge(Project project);

    public Set<AuditLog> logs(long projID);

}