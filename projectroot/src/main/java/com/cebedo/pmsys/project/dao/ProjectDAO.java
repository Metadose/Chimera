package com.cebedo.pmsys.project.dao;

import java.util.List;

import com.cebedo.pmsys.project.model.Project;

public interface ProjectDAO {

	public void create(Project project);

	public Project getByID(long id);

	public void update(Project project);

	public void delete(long id);

	public List<Project> list();

	public List<Project> listWithAllCollections();

	public Project getByIDWithAllCollections(int id);

}