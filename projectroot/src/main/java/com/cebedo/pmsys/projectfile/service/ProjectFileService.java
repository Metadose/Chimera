package com.cebedo.pmsys.projectfile.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.projectfile.model.ProjectFile;

public interface ProjectFileService {

	public void create(MultipartFile file, long projectID, String description)
			throws IOException;

	public void createForStaff(MultipartFile file, String description)
			throws IOException;

	public ProjectFile getByID(long id);

	public void update(ProjectFile projectFile);

	public void delete(long id);

	public List<ProjectFile> list();

	public List<ProjectFile> listWithAllCollections();

	public void updateDescription(long fileID, String description);

	public File getPhysicalFileByID(long fileID);
}
