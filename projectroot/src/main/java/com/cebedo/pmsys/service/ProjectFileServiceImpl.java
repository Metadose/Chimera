package com.cebedo.pmsys.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.ProjectFileDAO;
import com.cebedo.pmsys.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.FileHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.ProjectFile;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class ProjectFileServiceImpl implements ProjectFileService {

    private static Logger logger = Logger.getLogger(ProjectFile.OBJECT_NAME);
    private AuthHelper authHelper = new AuthHelper();
    private LogHelper logHelper = new LogHelper();
    private MessageHelper messageHelper = new MessageHelper();
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

    /**
     * Get the directory of the SYS_HOME.
     * 
     * @return
     */
    private String getSysHome() {
	if (sysHome == null) {
	    sysHome = this.systemConfigurationDAO
		    .getValueByName(SystemConstants.CONFIG_SYS_HOME);
	}
	return sysHome;
    }

    /**
     * Upload a file for the staff.
     */
    @Override
    @Transactional
    public String uploadFileToStaff(MultipartFile file, String description)
	    throws IOException {
	AuthenticationToken auth = this.authHelper.getAuth();

	// Do service.
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

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUpload(ProjectFile.OBJECT_NAME,
		projectFile.getName());
    }

    /**
     * Upload file to project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String uploadFileToProject(MultipartFile file, long projectID,
	    String description) throws IOException {

	AuthenticationToken auth = this.authHelper.getAuth();
	Project proj = this.projectDAO.getByID(projectID);

	if (!this.authHelper.isActionAuthorized(proj)) {

	    // Log warn.
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.UPLOAD, ProjectFile.OBJECT_NAME,
		    file.getName(), file.getOriginalFilename()));
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.UPLOAD, Project.OBJECT_NAME, proj.getId(),
		    proj.getName()));

	    // Return fail.
	    return AlertBoxGenerator.FAILED.generateUpload(
		    ProjectFile.OBJECT_NAME, file.getOriginalFilename());
	}

	// Log and notify.

	// Do service.
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

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUpload(ProjectFile.OBJECT_NAME,
		file.getOriginalFilename());
    }

    /**
     * Get file by id.
     */
    @Override
    @Transactional
    public ProjectFile getByID(long id) {

	AuthenticationToken auth = this.authHelper.getAuth();
	ProjectFile file = this.projectFileDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(file)) {

	    // Log info.
	    logger.info(this.logHelper.logGetObject(auth,
		    ProjectFile.OBJECT_NAME, id, file.getName()));

	    // Return file.
	    return file;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET,
		ProjectFile.OBJECT_NAME, id, file.getName()));

	// Return empty.
	return new ProjectFile();
    }

    /**
     * Update an existing project file.
     */
    @Override
    @Transactional
    public String update(ProjectFile projectFile) {

	AuthenticationToken auth = this.authHelper.getAuth();

	if (this.authHelper.isActionAuthorized(projectFile)) {

	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.UPDATE, projectFile);

	    // Do service.
	    this.projectFileDAO.update(projectFile);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(
		    ProjectFile.OBJECT_NAME, projectFile.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		ProjectFile.OBJECT_NAME, projectFile.getId(),
		projectFile.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(ProjectFile.OBJECT_NAME,
		projectFile.getName());
    }

    /**
     * Deletes the physical file then the file records.
     */
    @Override
    @Transactional
    public String delete(long id) {

	AuthenticationToken auth = this.authHelper.getAuth();
	ProjectFile file = this.projectFileDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(file)) {

	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.DELETE, file);

	    // Do service.
	    String location = file.getLocation();
	    this.fileHelper.deletePhysicalFile(location);
	    this.projectFileDAO.delete(id);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateDelete(
		    ProjectFile.OBJECT_NAME, file.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE,
		ProjectFile.OBJECT_NAME, id, file.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateDelete(ProjectFile.OBJECT_NAME,
		file.getName());
    }

    /**
     * List all project files.
     */
    @Override
    @Transactional
    public List<ProjectFile> list() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    // Log info.
	    logger.info(this.logHelper.logListAsSuperAdmin(token,
		    ProjectFile.OBJECT_NAME));

	    // Return list.
	    return this.projectFileDAO.list(null);
	}

	// Log info.
	Company co = token.getCompany();
	logger.info(this.logHelper.logListFromCompany(token,
		ProjectFile.OBJECT_NAME, co));

	// Return list.
	return this.projectFileDAO.list(co.getId());
    }

    /**
     * List all project files with initialized collections.
     */
    @Override
    @Transactional
    public List<ProjectFile> listWithAllCollections() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    // Log info.
	    logger.info(this.logHelper.logListWithCollectionsAsSuperAdmin(
		    token, ProjectFile.OBJECT_NAME));

	    // Return list.
	    return this.projectFileDAO.listWithAllCollections(null);
	}

	// Log info.
	Company co = token.getCompany();
	logger.info(this.logHelper.logListWithCollectionsFromCompany(token,
		ProjectFile.OBJECT_NAME, co));

	// Return list.
	return this.projectFileDAO.listWithAllCollections(co.getId());
    }

    /**
     * Update a file's description.
     */
    @Override
    @Transactional
    public String updateDescription(long fileID, String description) {

	AuthenticationToken auth = this.authHelper.getAuth();
	ProjectFile file = this.projectFileDAO.getByID(fileID);

	if (this.authHelper.isActionAuthorized(file)) {

	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.UPDATE, file);

	    // Do service.
	    this.projectFileDAO.updateDescription(fileID, description);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(
		    ProjectFile.OBJECT_NAME, file.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		ProjectFile.OBJECT_NAME, file.getId(), file.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(ProjectFile.OBJECT_NAME,
		file.getName());
    }

    /**
     * Get the actual physical file from the directory.
     */
    @Override
    @Transactional
    public File getPhysicalFileByID(long fileID) {

	AuthenticationToken auth = this.authHelper.getAuth();
	ProjectFile file = this.projectFileDAO.getByID(fileID);

	if (this.authHelper.isActionAuthorized(file)) {

	    // Log info.
	    logger.info(this.logHelper.logGetObject(auth,
		    ProjectFile.OBJECT_NAME, file.getId(), file.getName()));

	    // Return file.
	    String fileLocation = file.getLocation();
	    File actualFile = new File(fileLocation);
	    return actualFile;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET,
		ProjectFile.OBJECT_NAME, file.getId(), file.getName()));

	// TODO Don't let it crash.
	// Return an empty file instead.
	return null;
    }

    /**
     * Get name by id.
     */
    @Override
    @Transactional
    public String getNameByID(long fileID) {

	// Log info.
	AuthenticationToken auth = this.authHelper.getAuth();
	ProjectFile file = this.projectFileDAO.getByID(fileID);
	logger.info(this.logHelper.logGetProperty(auth,
		ProjectFile.OBJECT_NAME, ProjectFile.PROPERTY_NAME,
		file.getId(), file.getName()));

	// Do service.
	return file.getName();
    }
}
