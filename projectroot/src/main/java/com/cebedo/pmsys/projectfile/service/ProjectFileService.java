package com.cebedo.pmsys.projectfile.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.projectfile.model.ProjectFile;

public interface ProjectFileService {

	public void create(ProjectFile projectFile, MultipartFile file,
			String fileLocation) throws IOException;

	public ProjectFile getByID(long id);

	public void update(ProjectFile projectFile);

	public void delete(long id);

	public List<ProjectFile> list();

	public List<ProjectFile> listWithAllCollections();

	public void updateDescription(long fileID, String description);
}
