package com.cebedo.pmsys.project.service;

import java.util.List;

import com.cebedo.pmsys.project.model.Project;

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

}