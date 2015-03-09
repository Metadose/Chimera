package com.cebedo.pmsys.project.service;

import java.util.List;

import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.project.model.Project;

public interface ProjectService {

	public void create(Project project);

	public Project getByID(long projectID);

	public void update(Project project);

	public void delete(int id);

	public List<Project> list();

	public List<Project> listWithAllCollections();

	public Project getByIDWithAllCollections(int id);

}