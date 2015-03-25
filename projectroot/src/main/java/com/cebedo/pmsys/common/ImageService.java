package com.cebedo.pmsys.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.photo.model.Photo;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.project.service.ProjectService;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.service.StaffService;
import com.cebedo.pmsys.systemconfiguration.service.SystemConfigurationService;

@Controller
@RequestMapping(ImageService.SERVICE_NAME)
public class ImageService {

	public static final String SERVICE_NAME = "image";
	public static final String PARAM_FILENAME = "filename";

	private AuthHelper authHelper = new AuthHelper();
	private FileHelper fileHelper = new FileHelper();

	private SystemConfigurationService configService;
	private ProjectService projectService;
	private StaffService staffService;

	@Autowired(required = true)
	@Qualifier(value = "systemConfigurationService")
	public void setFieldService(SystemConfigurationService ps) {
		this.configService = ps;
	}

	@Autowired(required = true)
	@Qualifier(value = "staffService")
	public void setStaffService(StaffService ps) {
		this.staffService = ps;
	}

	@Autowired(required = true)
	@Qualifier(value = "projectService")
	public void setProjectService(ProjectService ps) {
		this.projectService = ps;
	}

	/**
	 * Gets the staff's profile picture.
	 * 
	 * @param staffID
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(SystemConstants.REQUEST_DISPLAY_STAFF_PROFILE)
	public ResponseEntity<byte[]> displayStaffProfile(
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID)
			throws IOException {

		Staff staff = this.staffService.getByID(staffID);

		InputStream imgStream = new FileInputStream(staff.getThumbnailURL());
		byte[] imgBytes = IOUtils.toByteArray(imgStream);
		imgStream.close();

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(imgBytes, headers, HttpStatus.CREATED);
	}

	@RequestMapping(SystemConstants.REQUEST_DISPLAY_PROJECT_PROFILE)
	public ResponseEntity<byte[]> displayProjectProfile(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID)
			throws IOException {

		Project proj = this.projectService.getByID(projectID);

		InputStream imgStream = new FileInputStream(proj.getThumbnailURL());
		byte[] imgBytes = IOUtils.toByteArray(imgStream);
		imgStream.close();

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(imgBytes, headers, HttpStatus.CREATED);
	}

	@RequestMapping(SystemConstants.REQUEST_DISPLAY)
	public ResponseEntity<byte[]> display(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(PARAM_FILENAME) String fileName) throws IOException {
		// If not authorized to view this image, return.
		// TODO Handle this, don't let it crash.
		Project proj = this.projectService.getByID(projectID);
		if (!this.authHelper.isActionAuthorized(proj)) {
			return null;
		}

		// Otherwise, user proj company, over user company.
		// If both are not existing, set to zero.
		String sysHome = this.configService.getValueByName("SYS_HOME");
		Company projCompany = proj.getCompany();
		Company userCompany = this.authHelper.getAuth().getCompany();
		String fileURI = this.fileHelper.constructSysHomeFileURI(
				sysHome,
				projCompany == null ? userCompany == null ? 0 : userCompany
						.getId() : projCompany.getId(), Project.class
						.getSimpleName(), projectID, Photo.class
						.getSimpleName(), fileName);

		// Convert to bytes.
		InputStream imgStream = new FileInputStream(fileURI);
		byte[] imgBytes = IOUtils.toByteArray(imgStream);
		imgStream.close();

		// Send it back to user.
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(imgBytes, headers, HttpStatus.CREATED);
	}
}
