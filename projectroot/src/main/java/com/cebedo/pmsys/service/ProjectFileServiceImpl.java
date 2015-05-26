package com.cebedo.pmsys.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.ProjectFileDAO;
import com.cebedo.pmsys.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.FileHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.ProjectFile;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;

@Service
public class ProjectFileServiceImpl implements ProjectFileService {

	private AuthHelper authHelper = new AuthHelper();
	private FileHelper fileHelper = new FileHelper();

	private ProjectFileDAO projectFileDAO;
	private ProjectDAO projectDAO;
	private SystemConfigurationDAO systemConfigurationDAO;
	private String sysHome;

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setProjectFileDAO(ProjectFileDAO projectFileDAO) {
		this.projectFileDAO = projectFileDAO;
	}

	public void setSystemConfigurationDAO(
			SystemConfigurationDAO systemConfigurationDAO) {
		this.systemConfigurationDAO = systemConfigurationDAO;
	}

	private String getSysHome() {
		if (sysHome == null) {
			sysHome = this.systemConfigurationDAO
					.getValueByName(SystemConstants.CONFIG_SYS_HOME);
		}
		return sysHome;
	}

	@Override
	@Transactional
	public void createForStaff(MultipartFile file, String description)
			throws IOException {
		AuthenticationToken auth = this.authHelper.getAuth();

		// Upload the file to the server.
		// file://SYS_HOME/"company"/[id]/"staff|project|team"/[id]/files/file.getOriginalFilename();
		// Fetch some details and set.
		String fileLocation = "";
		long size = file.getSize();
		Date dateUploaded = new Date(System.currentTimeMillis());

		// If user is super admin, and has company? and has staff?
		// If user is not super admin, has company, has a staff.
		if (auth.isSuperAdmin()) {
			Company userCompany = auth.getCompany();
			Staff userStaff = auth.getStaff();
			fileLocation = this.fileHelper.constructSysHomeFileURI(
					getSysHome(),
					userCompany == null ? 0 : userCompany.getId(),
					userStaff == null ? SystemUser.class.getSimpleName()
							: Staff.class.getSimpleName(),
					userStaff == null ? auth.getUser().getId() : userStaff
							.getId(), ProjectFile.class.getSimpleName(), file
							.getOriginalFilename());
		} else {
			fileLocation = this.fileHelper.constructSysHomeFileURI(
					getSysHome(), auth.getCompany().getId(),
					Staff.class.getSimpleName(), auth.getStaff().getId(),
					ProjectFile.class.getSimpleName(),
					file.getOriginalFilename());
		}
		// Set the properties.
		ProjectFile projectFile = new ProjectFile(auth.getStaff(),
				fileLocation, file.getOriginalFilename(), description, size,
				dateUploaded);

		// Do actual upload.
		this.fileHelper.fileUpload(file, fileLocation);
		Company authCompany = auth.getCompany();
		projectFile.setCompany(authCompany);
		this.projectFileDAO.create(projectFile);
	}

	@CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
	@Override
	@Transactional
	public void create(MultipartFile file, long projectID, String description)
			throws IOException {
		AuthenticationToken auth = this.authHelper.getAuth();
		Project proj = this.projectDAO.getByID(projectID);
		Company projCompany = proj.getCompany();

		// If user is super admin, has company? and has project.
		// Get the company from the project.
		// If user is not super admin, has company, and has project.
		String fileLocation = "";
		if (auth.isSuperAdmin()) {
			fileLocation = this.fileHelper.constructSysHomeFileURI(
					getSysHome(),
					projCompany == null ? 0 : projCompany.getId(),
					Project.class.getSimpleName(), projectID,
					ProjectFile.class.getSimpleName(),
					file.getOriginalFilename());
		} else {
			// file://SYS_HOME/"company"/[id]/"staff|project|team"/[id]/files/file.getOriginalFilename();
			// Fetch some details and set.
			// If project's company is not defined, use the user's company.
			fileLocation = this.fileHelper.constructSysHomeFileURI(
					getSysHome(), projCompany == null ? auth.getCompany()
							.getId() : projCompany.getId(), Project.class
							.getSimpleName(), projectID, ProjectFile.class
							.getSimpleName(), file.getOriginalFilename());
		}

		long size = file.getSize();
		Date dateUploaded = new Date(System.currentTimeMillis());

		// Set the properties.
		ProjectFile projectFile = new ProjectFile(auth.getStaff(),
				fileLocation, new Project(projectID),
				file.getOriginalFilename(), description, size, dateUploaded);

		// Do actual upload.
		this.fileHelper.fileUpload(file, fileLocation);
		Company authCompany = auth.getCompany();
		projectFile.setCompany(authCompany);
		this.projectFileDAO.create(projectFile);
	}

	@Override
	@Transactional
	public ProjectFile getByID(long id) {
		ProjectFile file = this.projectFileDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(file)) {
			return file;
		}
		return new ProjectFile();
	}

	@Override
	@Transactional
	public void update(ProjectFile projectFile) {
		if (this.authHelper.isActionAuthorized(projectFile)) {
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
		if (this.authHelper.isActionAuthorized(file)) {
			String location = file.getLocation();
			this.fileHelper.deletePhysicalFile(location);
			this.projectFileDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<ProjectFile> list() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.projectFileDAO.list(null);
		}
		return this.projectFileDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public List<ProjectFile> listWithAllCollections() {
		AuthenticationToken token = this.authHelper.getAuth();
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
		if (this.authHelper.isActionAuthorized(file)) {
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
		if (this.authHelper.isActionAuthorized(file)) {
			String fileLocation = file.getLocation();
			File actualFile = new File(fileLocation);
			return actualFile;
		}
		// TODO Don't let it crash.
		// Return an empty file instead.
		return null;
	}

	@Override
	@Transactional
	public String getNameByID(long fileID) {
		return this.projectFileDAO.getNameByID(fileID);
	}
}