package com.cebedo.pmsys.projectfile.service;

import java.util.List;

import com.cebedo.pmsys.projectfile.model.ProjectFile;

public interface ProjectFileService {

	public void create(ProjectFile projectFile);

	public ProjectFile getByID(long id);

	public void update(ProjectFile projectFile);

	public void delete(long id);

	public List<ProjectFile> list();
}
