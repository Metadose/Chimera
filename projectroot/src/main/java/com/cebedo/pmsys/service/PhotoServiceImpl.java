package com.cebedo.pmsys.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.PhotoDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.FileHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Photo;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxFactory;

@Service
public class PhotoServiceImpl implements PhotoService {

	private AuthHelper authHelper = new AuthHelper();
	private FileHelper fileHelper = new FileHelper();

	private PhotoDAO photoDAO;
	private ProjectDAO projectDAO;
	private StaffDAO staffDAO;
	private SystemConfigurationDAO systemConfigurationDAO;
	private CompanyDAO companyDAO;
	private String sysHome;

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setSystemConfigurationDAO(
			SystemConfigurationDAO systemConfigurationDAO) {
		this.systemConfigurationDAO = systemConfigurationDAO;
	}

	public void setStaffDAO(StaffDAO staffDAO) {
		this.staffDAO = staffDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setPhotoDAO(PhotoDAO photoDAO) {
		this.photoDAO = photoDAO;
	}

	private String getSysHome() {
		if (sysHome == null) {
			sysHome = this.systemConfigurationDAO
					.getValueByName(SystemConstants.CONFIG_SYS_HOME);
		}
		return sysHome;
	}

	@Caching(evict = {
			@CacheEvict(value = Project.OBJECT_NAME + ":getNameByID", key = "#projectID"),
			@CacheEvict(value = Project.OBJECT_NAME + ":getByID", key = "#projectID"),
			@CacheEvict(value = Project.OBJECT_NAME
					+ ":getByIDWithAllCollections", key = "#projectID"),
			@CacheEvict(value = Project.OBJECT_NAME + ":listWithTasks", allEntries = true),
			@CacheEvict(value = Project.OBJECT_NAME + ":listWithAllCollections", allEntries = true),
			@CacheEvict(value = Project.OBJECT_NAME + ":list", allEntries = true) })
	@Override
	@Transactional
	public void deleteProjectProfile(long projectID) {
		Project proj = this.projectDAO.getByID(projectID);
		if (this.authHelper.isActionAuthorized(proj)) {
			// Delete the physical file.
			String path = proj.getThumbnailURL();
			File file = new File(path);
			file.delete();

			// Null the record.
			proj.setThumbnailURL(null);
			this.projectDAO.update(proj);
		}
	}

	@Override
	@Transactional
	public void uploadStaffProfile(MultipartFile file, long staffID)
			throws IOException {
		// If not authorized, return.
		Staff staff = this.staffDAO.getByID(staffID);
		if (!this.authHelper.isActionAuthorized(staff)) {
			return;
		}
		AuthenticationToken auth = this.authHelper.getAuth();
		Company staffCompany = staff.getCompany();

		String fileLocation = "";
		if (auth.isSuperAdmin()) {
			// If the staff company is null, get it from the user.
			// If the user has no company, set it to zero.
			Company userCompany = auth.getCompany();
			fileLocation = this.fileHelper.constructSysHomeFileURI(
					getSysHome(),
					staffCompany == null ? userCompany == null ? 0
							: userCompany.getId() : staffCompany.getId(),
					Staff.class.getSimpleName(),
					staffID,
					Staff.SUB_MODULE_PROFILE + "/"
							+ Photo.class.getSimpleName(), file
							.getOriginalFilename());
		} else {
			// If the staff company is null, get it from the user.
			fileLocation = this.fileHelper.constructSysHomeFileURI(
					getSysHome(), staffCompany == null ? auth.getCompany()
							.getId() : staffCompany.getId(), Staff.class
							.getSimpleName(), staffID, Staff.SUB_MODULE_PROFILE
							+ "/" + Photo.class.getSimpleName(), file
							.getOriginalFilename());
		}

		// Update the staff obj with the new profile pic.
		staff.setThumbnailURL(fileLocation);
		this.fileHelper.fileUpload(file, fileLocation);
		this.staffDAO.update(staff);
	}

	/**
	 * Upload a profile picture for the project.
	 */
	@Caching(evict = {
			@CacheEvict(value = Project.OBJECT_NAME + ":getNameByID", key = "#projectID"),
			@CacheEvict(value = Project.OBJECT_NAME + ":getByID", key = "#projectID"),
			@CacheEvict(value = Project.OBJECT_NAME
					+ ":getByIDWithAllCollections", key = "#projectID"),
			@CacheEvict(value = Project.OBJECT_NAME + ":listWithTasks", allEntries = true),
			@CacheEvict(value = Project.OBJECT_NAME + ":listWithAllCollections", allEntries = true),
			@CacheEvict(value = Project.OBJECT_NAME + ":list", allEntries = true) })
	@Override
	@Transactional
	public void uploadProjectProfile(MultipartFile file, long projectID)
			throws IOException {
		// If the user is not authorized in this project,
		// return.
		Project proj = this.projectDAO.getByID(projectID);
		if (!this.authHelper.isActionAuthorized(proj)) {
			return;
		}
		Company projCompany = proj.getCompany();
		AuthenticationToken auth = this.authHelper.getAuth();

		String fileLocation = "";
		if (auth.isSuperAdmin()) {
			// If the project company is null, get it from the user.
			// If the user has no company, set it to zero.
			Company userCompany = auth.getCompany();
			fileLocation = this.fileHelper.constructSysHomeFileURI(
					getSysHome(),
					projCompany == null ? userCompany == null ? 0 : userCompany
							.getId() : projCompany.getId(),
					Project.class.getSimpleName(),
					projectID,
					Project.SUB_MODULE_PROFILE + "/"
							+ Photo.class.getSimpleName(), file
							.getOriginalFilename());
		} else {
			// If the project company is null, get it from the user.
			fileLocation = this.fileHelper.constructSysHomeFileURI(
					getSysHome(),
					projCompany == null ? auth.getCompany().getId()
							: projCompany.getId(),
					Project.class.getSimpleName(),
					projectID,
					Project.SUB_MODULE_PROFILE + "/"
							+ Photo.class.getSimpleName(), file
							.getOriginalFilename());
		}

		// Delete old photo.
		// Upload and
		// Update the project entry.
		String oldPhoto = proj.getThumbnailURL();
		if (oldPhoto != null && !oldPhoto.isEmpty()) {
			File oldPhotoObj = new File(oldPhoto);
			oldPhotoObj.delete();
		}
		proj.setThumbnailURL(fileLocation);
		this.fileHelper.fileUpload(file, fileLocation);
		this.projectDAO.update(proj);
	}

	/**
	 * Insert a photo to the project.
	 */
	@CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
	@Override
	@Transactional
	public AlertBoxFactory create(MultipartFile file, long projectID,
			String description) throws IOException {
		AlertBoxFactory alertFactory = new AlertBoxFactory();

		// If not authorized, return.
		Project proj = this.projectDAO.getByID(projectID);
		if (!this.authHelper.isActionAuthorized(proj)) {
			alertFactory = AlertBoxFactory.FAILED;
			alertFactory
					.setMessage("You are <b>not authorized</b> to <b>upload</b> a file to project <b>"
							+ proj.getName() + "</b>.");
			return alertFactory;
		}

		AuthenticationToken auth = this.authHelper.getAuth();
		Company projCompany = proj.getCompany();

		// Construct the photo URI.
		String fileLocation = "";
		if (auth.isSuperAdmin()) {
			Company userCompany = auth.getCompany();
			// If the project has no company, use the user's company.
			// If the user has no company, set it to zero.
			// If the user has no staff, user the sysuser details.
			fileLocation = this.fileHelper.constructSysHomeFileURI(
					getSysHome(), projCompany == null ? userCompany == null ? 0
							: userCompany.getId() : projCompany.getId(),
					Project.class.getSimpleName(), projectID, Photo.class
							.getSimpleName(), file.getOriginalFilename());
		} else {
			// If has no company, use the user's company.
			fileLocation = this.fileHelper.constructSysHomeFileURI(
					getSysHome(), projCompany == null ? auth.getCompany()
							.getId() : projCompany.getId(), Project.class
							.getSimpleName(), projectID, Photo.class
							.getSimpleName(), file.getOriginalFilename());
		}

		File fileTest = new File(fileLocation);
		if (fileTest.exists()) {
			alertFactory = AlertBoxFactory.FAILED;
			alertFactory.setMessage("<b>" + file.getOriginalFilename()
					+ " already exists</b> in project <b>" + proj.getName()
					+ "</b>.");
			return alertFactory;
		}

		// Fetch some details and set.
		long size = file.getSize();
		Date dateUploaded = new Date(System.currentTimeMillis());
		Photo photo = new Photo(auth.getStaff(), fileLocation, proj,
				file.getOriginalFilename(), description, size, dateUploaded,
				projCompany);

		// Do the actual upload.
		this.fileHelper.fileUpload(file, fileLocation);
		this.photoDAO.create(photo);

		alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>uploaded</b> the photo <b>"
				+ file.getOriginalFilename() + "</b>.");
		return alertFactory;
	}

	@Override
	@Transactional
	public Photo getByID(long id) {
		Photo photo = this.photoDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(photo)) {
			return photo;
		}
		return new Photo();
	}

	@Override
	@Transactional
	public void update(Photo photo) {
		if (this.authHelper.isActionAuthorized(photo)) {
			this.photoDAO.update(photo);
		}
	}

	/**
	 * Delete actual physical file and delete photo record.
	 */
	@Override
	@Transactional
	public void delete(long id) {
		Photo photo = this.photoDAO.getByID(id);
		Company company = this.companyDAO.getCompanyByObjID(Photo.TABLE_NAME,
				Photo.COLUMN_PRIMARY_KEY, photo.getId());
		photo.setCompany(company);

		if (this.authHelper.isActionAuthorized(photo)) {
			File photoFile = new File(photo.getLocation());
			photoFile.delete();
			this.photoDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<Photo> list() {
		AuthenticationToken auth = this.authHelper.getAuth();
		if (auth.isSuperAdmin()) {
			return this.photoDAO.list(null);
		}
		Long companyID = auth.getCompany().getId();
		return this.photoDAO.list(companyID);
	}

	@Override
	@Transactional
	public String getNameByID(long id) {
		return this.photoDAO.getNameByID(id);
	}
}
