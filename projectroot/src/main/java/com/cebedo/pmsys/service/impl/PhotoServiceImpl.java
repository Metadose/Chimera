package com.cebedo.pmsys.service.impl;

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
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.PhotoDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.FileHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Photo;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.service.PhotoService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class PhotoServiceImpl implements PhotoService {

    private static Logger logger = Logger.getLogger(Photo.OBJECT_NAME);
    private AuthHelper authHelper = new AuthHelper();
    private FileHelper fileHelper = new FileHelper();
    private LogHelper logHelper = new LogHelper();
    private MessageHelper messageHelper = new MessageHelper();

    private PhotoDAO photoDAO;
    private ProjectDAO projectDAO;
    private SystemConfigurationDAO systemConfigurationDAO;
    private CompanyDAO companyDAO;

    private String sysHome;

    public void setCompanyDAO(CompanyDAO companyDAO) {
	this.companyDAO = companyDAO;
    }

    public void setSystemConfigurationDAO(SystemConfigurationDAO systemConfigurationDAO) {
	this.systemConfigurationDAO = systemConfigurationDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public void setPhotoDAO(PhotoDAO photoDAO) {
	this.photoDAO = photoDAO;
    }

    private String getSysHome() {
	if (sysHome == null) {
	    sysHome = this.systemConfigurationDAO.getValueByName(SystemConstants.CONFIG_SYS_HOME);
	}
	return sysHome;
    }

    /**
     * Insert a photo to the project.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String uploadPhotoToProject(MultipartFile file, long projectID, String description)
	    throws IOException {

	AuthenticationToken auth = this.authHelper.getAuth();

	// If not authorized, return.
	Project proj = this.projectDAO.getByID(projectID);
	if (!this.authHelper.isActionAuthorized(proj)) {

	    // Log warn.
	    logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE, Project.OBJECT_NAME,
		    proj.getId(), proj.getName()));
	    logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE, Photo.OBJECT_NAME,
		    file.getName(), file.getOriginalFilename()));

	    // Return fail.
	    return AlertBoxGenerator.FAILED
		    .generateUpload(Photo.OBJECT_NAME, file.getOriginalFilename());
	}

	// Log and notify.
	this.messageHelper.sendActionWithUpload(AuditAction.UPLOAD, proj, Photo.OBJECT_NAME, file);

	// Do service.
	Company projCompany = proj.getCompany();

	// Construct the photo URI.
	String fileLocation = "";
	if (auth.isSuperAdmin()) {
	    Company userCompany = auth.getCompany();
	    // If the project has no company, use the user's company.
	    // If the user has no company, set it to zero.
	    // If the user has no staff, user the sysuser details.
	    fileLocation = this.fileHelper.constructSysHomeFileURI(
		    getSysHome(),
		    projCompany == null ? userCompany == null ? 0 : userCompany.getId() : projCompany
			    .getId(), Project.class.getSimpleName(), projectID, Photo.class
			    .getSimpleName(), file.getOriginalFilename());
	} else {
	    // If has no company, use the user's company.
	    fileLocation = this.fileHelper.constructSysHomeFileURI(getSysHome(),
		    projCompany == null ? auth.getCompany().getId() : projCompany.getId(),
		    Project.class.getSimpleName(), projectID, Photo.class.getSimpleName(),
		    file.getOriginalFilename());
	}

	File fileTest = new File(fileLocation);

	if (fileTest.exists()) {
	    // If exists,
	    // Return fail.
	    AlertBoxGenerator alertFactory = new AlertBoxGenerator();
	    alertFactory = AlertBoxGenerator.FAILED;
	    alertFactory.setMessage("<b>" + file.getOriginalFilename()
		    + " already exists</b> in project <b>" + proj.getName() + "</b>.");

	    return alertFactory.generateHTML();
	}

	// Fetch some details and set.
	long size = file.getSize();
	Date dateUploaded = new Date(System.currentTimeMillis());
	Photo photo = new Photo(auth.getStaff(), fileLocation, proj, file.getOriginalFilename(),
		description, size, dateUploaded, projCompany);

	// Do the actual upload.
	this.fileHelper.fileUpload(file, fileLocation);
	this.photoDAO.create(photo);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUpload(Photo.OBJECT_NAME, file.getOriginalFilename());
    }

    /**
     * Get photo given an id.
     */
    @Override
    @Transactional
    public Photo getByID(long id) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Photo photo = this.photoDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(photo)) {

	    // Log info.
	    logger.info(this.logHelper.logGetObject(auth, Photo.OBJECT_NAME, id, photo.getName()));

	    // Return the obj.
	    return photo;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET, Photo.OBJECT_NAME, id,
		photo.getName()));

	// Return empty.
	return new Photo();
    }

    /**
     * Update a photo.
     */
    @Override
    @Transactional
    public String update(Photo photo) {

	AuthenticationToken auth = this.authHelper.getAuth();
	if (this.authHelper.isActionAuthorized(photo)) {

	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.UPDATE, photo);

	    // Do service.
	    this.photoDAO.update(photo);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(Photo.OBJECT_NAME, photo.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE, Photo.OBJECT_NAME,
		photo.getId(), photo.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(Photo.OBJECT_NAME, photo.getName());
    }

    /**
     * Delete actual physical file and delete photo record.
     */
    @SuppressWarnings("deprecation")
    @Override
    @Transactional
    public String delete(long id) {

	AuthenticationToken auth = this.authHelper.getAuth();
	Photo photo = this.photoDAO.getByID(id);
	Company company = this.companyDAO.getCompanyByObjID(Photo.TABLE_NAME, Photo.COLUMN_PRIMARY_KEY,
		photo.getId());
	photo.setCompany(company);

	if (this.authHelper.isActionAuthorized(photo)) {

	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.DELETE, photo);

	    // Do service.
	    File photoFile = new File(photo.getLocation());
	    photoFile.delete();
	    this.photoDAO.delete(id);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateDelete(Photo.OBJECT_NAME, photo.getName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE, Photo.OBJECT_NAME, id,
		photo.getName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateDelete(Photo.OBJECT_NAME, photo.getName());
    }

    /**
     * List all photos.
     */
    @Override
    @Transactional
    public List<Photo> list() {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (auth.isSuperAdmin()) {
	    // Log info.
	    logger.info(this.logHelper.logListAsSuperAdmin(auth, Photo.OBJECT_NAME));

	    // Return list.
	    return this.photoDAO.list(null);
	}

	// Log info.
	Company co = auth.getCompany();
	Long companyID = co.getId();
	logger.info(this.logHelper.logListFromCompany(auth, Photo.OBJECT_NAME, co));

	// Return list.
	return this.photoDAO.list(companyID);
    }

    /**
     * Get name by ID.
     */
    @Override
    @Transactional
    public String getNameByID(long id) {
	// Log.
	AuthenticationToken auth = this.authHelper.getAuth();
	Photo photo = this.photoDAO.getByID(id);
	logger.info(this.logHelper.logGetProperty(auth, Photo.OBJECT_NAME, Photo.PROPERTY_NAME, id,
		photo.getName()));

	// Return.
	return this.photoDAO.getNameByID(id);
    }
}
