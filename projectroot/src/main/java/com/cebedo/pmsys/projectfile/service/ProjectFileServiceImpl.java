package com.cebedo.pmsys.projectfile.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.projectfile.dao.ProjectFileDAO;
import com.cebedo.pmsys.projectfile.model.ProjectFile;

@Service
public class ProjectFileServiceImpl implements ProjectFileService {

	private ProjectFileDAO projectFileDAO;

	public void setProjectFileDAO(ProjectFileDAO projectFileDAO) {
		this.projectFileDAO = projectFileDAO;
	}

	/**
	 * Upload a file.
	 * 
	 * @param file
	 * @param id
	 * @param objectName
	 * @throws IOException
	 */
	private void fileUpload(MultipartFile file, String fileLocation)
			throws IOException {
		// Prelims.
		byte[] bytes = file.getBytes();
		checkDirectoryExistence(fileLocation);

		// Upload file.
		FileOutputStream oStream = new FileOutputStream(new File(fileLocation));
		BufferedOutputStream stream = new BufferedOutputStream(oStream);
		stream.write(bytes);
		stream.close();
		oStream.close();
	}

	/**
	 * Helper function to create non-existing folders.
	 * 
	 * @param fileLocation
	 */
	private void checkDirectoryExistence(String fileLocation) {
		File file = new File(fileLocation);
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
	}

	@Override
	@Transactional
	public void create(ProjectFile projectFile, MultipartFile file,
			String fileLocation) throws IOException {
		fileUpload(file, fileLocation);
		this.projectFileDAO.create(projectFile);
	}

	@Override
	@Transactional
	public ProjectFile getByID(long id) {
		return this.projectFileDAO.getByID(id);
	}

	@Override
	@Transactional
	public void update(ProjectFile projectFile) {
		this.projectFileDAO.update(projectFile);
	}

	/**
	 * Deletes the physical file then the file records.
	 */
	@Override
	@Transactional
	public void delete(long id) {
		deletePhysicalFile(id);
		this.projectFileDAO.delete(id);
	}

	/**
	 * Deletes the physical file from the file system.
	 * 
	 * @param id
	 */
	private void deletePhysicalFile(long id) {
		ProjectFile file = this.projectFileDAO.getByID(id);
		String location = file.getLocation();
		File phyFile = new File(location);
		phyFile.delete();
	}

	@Override
	@Transactional
	public List<ProjectFile> list() {
		return this.projectFileDAO.list();
	}

	@Override
	@Transactional
	public List<ProjectFile> listWithAllCollections() {
		return this.projectFileDAO.listWithAllCollections();
	}

	@Override
	@Transactional
	public void updateDescription(long fileID, String description) {
		this.projectFileDAO.updateDescription(fileID, description);
	}

}
