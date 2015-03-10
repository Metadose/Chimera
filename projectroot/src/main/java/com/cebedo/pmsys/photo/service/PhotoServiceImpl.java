package com.cebedo.pmsys.photo.service;

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
import com.cebedo.pmsys.photo.dao.PhotoDAO;
import com.cebedo.pmsys.photo.model.Photo;
import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.dao.StaffDAO;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.systemconfiguration.dao.SystemConfigurationDAO;

@Service
public class PhotoServiceImpl implements PhotoService {

	private PhotoDAO photoDAO;
	private ProjectDAO projectDAO;
	private StaffDAO staffDAO;
	private SystemConfigurationDAO systemConfigurationDAO;
	private String sysHome;

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
			sysHome = this.systemConfigurationDAO.getValueByName("SYS_HOME");
		}
		return sysHome;
	}

	@Override
	@Transactional
	public void deleteProjectProfile(long projectID) {
		Project proj = this.projectDAO.getByID(projectID);
		if (AuthUtils.isActionAuthorized(proj)) {
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
		if (!AuthUtils.isActionAuthorized(staff)) {
			return;
		}
		AuthenticationToken auth = AuthUtils.getAuth();
		Company staffCompany = staff.getCompany();

		String fileLocation = "";
		if (auth.isSuperAdmin()) {
			// If the staff company is null, get it from the user.
			// If the user has no company, set it to zero.
			Company userCompany = auth.getCompany();
			fileLocation = FileUtils.constructSysHomeFileURI(
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
			fileLocation = FileUtils.constructSysHomeFileURI(
					getSysHome(),
					staffCompany == null ? auth.getCompany().getId()
							: staffCompany.getId(),
					Staff.class.getSimpleName(),
					staffID,
					Staff.SUB_MODULE_PROFILE + "/"
							+ Photo.class.getSimpleName(), file
							.getOriginalFilename());
		}

		// Update the staff obj with the new profile pic.
		staff.setThumbnailURL(fileLocation);
		FileUtils.fileUpload(file, fileLocation);
		this.staffDAO.update(staff);
	}

	/**
	 * Upload a profile picture for the project.
	 */
	@Override
	@Transactional
	public void uploadProjectProfile(MultipartFile file, long projectID)
			throws IOException {
		// If the user is not authorized in this project,
		// return.
		Project proj = this.projectDAO.getByID(projectID);
		if (!AuthUtils.isActionAuthorized(proj)) {
			return;
		}
		Company projCompany = proj.getCompany();
		AuthenticationToken auth = AuthUtils.getAuth();

		String fileLocation = "";
		if (auth.isSuperAdmin()) {
			// If the project company is null, get it from the user.
			// If the user has no company, set it to zero.
			Company userCompany = auth.getCompany();
			fileLocation = FileUtils.constructSysHomeFileURI(
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
			fileLocation = FileUtils.constructSysHomeFileURI(
					getSysHome(),
					projCompany == null ? auth.getCompany().getId()
							: projCompany.getId(),
					Project.class.getSimpleName(),
					projectID,
					Project.SUB_MODULE_PROFILE + "/"
							+ Photo.class.getSimpleName(), file
							.getOriginalFilename());
		}

		// Upload and
		// Update the project entry.
		proj.setThumbnailURL(fileLocation);
		FileUtils.fileUpload(file, fileLocation);
		this.projectDAO.update(proj);
	}

	/**
	 * Insert a photo to the project.
	 */
	@Override
	@Transactional
	public void create(MultipartFile file, long projectID, String description)
			throws IOException {
		// If not authorized, return.
		Project proj = this.projectDAO.getByID(projectID);
		if (!AuthUtils.isActionAuthorized(proj)) {
			return;
		}

		AuthenticationToken auth = AuthUtils.getAuth();
		Company projCompany = proj.getCompany();

		// Construct the photo URI.
		String fileLocation = "";
		if (auth.isSuperAdmin()) {
			Company userCompany = auth.getCompany();
			// If the project has no company, use the user's company.
			// If the user has no company, set it to zero.
			// If the user has no staff, user the sysuser details.
			fileLocation = FileUtils.constructSysHomeFileURI(
					getSysHome(),
					projCompany == null ? userCompany == null ? 0 : userCompany
							.getId() : projCompany.getId(), Project.class
							.getSimpleName(), projectID, Photo.class
							.getSimpleName(), file.getOriginalFilename());
		} else {
			// If has no company, use the user's company.
			fileLocation = FileUtils.constructSysHomeFileURI(getSysHome(),
					projCompany == null ? auth.getCompany().getId()
							: projCompany.getId(), Project.class
							.getSimpleName(), projectID, Photo.class
							.getSimpleName(), file.getOriginalFilename());
		}

		// Fetch some details and set.
		long size = file.getSize();
		Date dateUploaded = new Date(System.currentTimeMillis());
		Photo photo = new Photo(auth.getStaff(), fileLocation, proj,
				file.getOriginalFilename(), description, size, dateUploaded);

		// Do the actual upload.
		FileUtils.fileUpload(file, fileLocation);
		this.photoDAO.create(photo);
	}

	@Override
	@Transactional
	public Photo getByID(long id) {
		Photo photo = this.photoDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(photo)) {
			return photo;
		}
		return new Photo();
	}

	@Override
	@Transactional
	public void update(Photo photo) {
		if (AuthUtils.isActionAuthorized(photo)) {
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
		if (AuthUtils.isActionAuthorized(photo)) {
			File photoFile = new File(photo.getLocation());
			photoFile.delete();
			this.photoDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<Photo> list() {
		// TODO
		return this.photoDAO.list();
	}
}
