package com.cebedo.pmsys.projectfile.dao;

import java.util.List;

import com.cebedo.pmsys.projectfile.model.ProjectFile;

public interface ProjectFileDAO {

	public void create(ProjectFile projectFile);

	public ProjectFile getByID(long id);

	public void update(ProjectFile projectFile);

	public void delete(long id);

	public List<ProjectFile> list();

	public List<ProjectFile> listWithAllCollections();

	public void updateDescription(long fileID, String description);
}
