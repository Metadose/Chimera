package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.ProjectFile;

public interface ProjectFileDAO {

	public void create(ProjectFile projectFile);

	public ProjectFile getByID(long id);

	public void update(ProjectFile projectFile);

	public void delete(long id);

	public List<ProjectFile> list(Long companyID);

	public List<ProjectFile> listWithAllCollections(Long companyID);

	public void updateDescription(long fileID, String description);

	public String getNameByID(long fileID);
}
