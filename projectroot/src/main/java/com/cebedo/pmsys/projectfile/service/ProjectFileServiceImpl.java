package com.cebedo.pmsys.projectfile.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.common.AuthUtils;
import com.cebedo.pmsys.common.FileUtils;
import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.projectfile.dao.ProjectFileDAO;
import com.cebedo.pmsys.projectfile.model.ProjectFile;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.systemconfiguration.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.systemuser.model.SystemUser;

@Service
public class ProjectFileServiceImpl implements ProjectFileService {

	private ProjectFileDAO projectFileDAO;
	private SystemConfigurationDAO systemConfigurationDAO;
	private String sysHome;

	public void setProjectFileDAO(ProjectFileDAO projectFileDAO) {
		this.projectFileDAO = projectFileDAO;
	}

	public void setSystemConfigurationDAO(
			SystemConfigurationDAO systemConfigurationDAO) {
		this.systemConfigurationDAO = systemConfigurationDAO;
	}

	private String getSysHome() {
		if (sysHome == null) {
			sysHome = this.systemConfigurationDAO.getValueByName("SYS_HOME");
		}
		return sysHome;
	}

	@Override
	@Transactional
	public void createForStaff(MultipartFile file, String description)
			throws IOException {
		AuthenticationToken auth = AuthUtils.getAuth();

		// Upload the file to the server.
		// file://SYS_HOME/"company"/[id]/"staff|project|team"/[id]/files/file.getOriginalFilename();
		// Fetch some details and set.
		String fileLocation = "";
		long size = file.getSize();
		Date dateUploaded = new Date(System.currentTimeMillis());

		if (auth.isSuperAdmin()) {
			fileLocation = FileUtils.constructSysHomeFileURI(getSysHome(), 0,
					SystemUser.class.getSimpleName(), auth.getUser().getId(),
					file.getOriginalFilename());
		} else {
			fileLocation = FileUtils.constructSysHomeFileURI(getSysHome(), auth
					.getCompany().getId(), Staff.class.getSimpleName(), auth
					.getStaff().getId(), file.getOriginalFilename());
		}
		// Set the properties.
		ProjectFile projectFile = new ProjectFile(auth.getStaff(),
				fileLocation, file.getOriginalFilename(), description, size,
				dateUploaded);

		// Do actual upload.
		FileUtils.fileUpload(file, fileLocation);
		this.projectFileDAO.create(projectFile);

		// Update the entry for company details.
		Company authCompany = auth.getCompany();
		if (AuthUtils.notNullObjNotSuperAdmin(authCompany)) {
			projectFile.setCompany(authCompany);
			this.projectFileDAO.update(projectFile);
		}
	}

	@Override
	@Transactional
	public void create(MultipartFile file, long projectID, String description)
			throws IOException {
		AuthenticationToken auth = AuthUtils.getAuth();

		// Upload the file to the server.
		// file://SYS_HOME/"company"/[id]/"staff|project|team"/[id]/files/file.getOriginalFilename();
		// Fetch some details and set.
		String fileLocation = FileUtils.constructSysHomeFileURI(getSysHome(),
				auth.getCompany().getId(), Project.class.getSimpleName(),
				projectID, file.getOriginalFilename());
		long size = file.getSize();
		Date dateUploaded = new Date(System.currentTimeMillis());

		// Set the properties.
		ProjectFile projectFile = new ProjectFile(auth.getStaff(),
				fileLocation, new Project(projectID),
				file.getOriginalFilename(), description, size, dateUploaded);

		// Do actual upload.
		FileUtils.fileUpload(file, fileLocation);
		this.projectFileDAO.create(projectFile);

		// Update the entry for company details.
		Company authCompany = auth.getCompany();
		if (AuthUtils.notNullObjNotSuperAdmin(authCompany)) {
			projectFile.setCompany(authCompany);
			this.projectFileDAO.update(projectFile);
		}
	}

	@Override
	@Transactional
	public ProjectFile getByID(long id) {
		ProjectFile file = this.projectFileDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(file)) {
			return file;
		}
		return new ProjectFile();
	}

	@Override
	@Transactional
	public void update(ProjectFile projectFile) {
		if (AuthUtils.isActionAuthorized(projectFile)) {
			this.projectFileDAO.update(projectFile);
		}
	}

	/**
	 * Deletes the physical file then the file records.
	 */
	@Override
	@Transactional
	public void delete(long id) {
		ProjectFile file = this.projectFileDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(file)) {
			String location = file.getLocation();
			FileUtils.deletePhysicalFile(location);
			this.projectFileDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<ProjectFile> list() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.projectFileDAO.list(null);
		}
		return this.projectFileDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public List<ProjectFile> listWithAllCollections() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.projectFileDAO.listWithAllCollections(null);
		}
		return this.projectFileDAO.listWithAllCollections(token.getCompany()
				.getId());
	}

	@Override
	@Transactional
	public void updateDescription(long fileID, String description) {
		ProjectFile file = this.projectFileDAO.getByID(fileID);
		if (AuthUtils.isActionAuthorized(file)) {
			this.projectFileDAO.updateDescription(fileID, description);
		}
	}

	/**
	 * Get the actual physical file from the directory.
	 */
	@Override
	@Transactional
	public File getPhysicalFileByID(long fileID) {
		ProjectFile file = this.projectFileDAO.getByID(fileID);
		if (AuthUtils.isActionAuthorized(file)) {
			String fileLocation = file.getLocation();
			File actualFile = new File(fileLocation);
			return actualFile;
		}
		// TODO Don't let it crash.
		// Return an empty file instead.
		return null;
	}
}
