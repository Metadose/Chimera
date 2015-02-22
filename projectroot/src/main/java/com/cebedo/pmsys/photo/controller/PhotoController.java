package com.cebedo.pmsys.photo.controller;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.photo.model.Photo;
import com.cebedo.pmsys.photo.service.PhotoService;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.projectfile.model.ProjectFile;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.systemconfiguration.service.SystemConfigurationService;

@Controller
@RequestMapping(Photo.OBJECT_NAME)
public class PhotoController {

	public static final String ATTR_LIST = "photoList";
	public static final String ATTR_PHOTO = Photo.OBJECT_NAME;
	public static final String JSP_LIST = "photoList";
	public static final String JSP_EDIT = "photoEdit";

	private PhotoService photoService;
	private SystemConfigurationService configService;
	private String sysHome;

	public String getSysHome() {
		if (sysHome == null) {
			sysHome = this.configService.getValueByName("SYS_HOME");
		}
		return sysHome;
	}

	@Autowired(required = true)
	@Qualifier(value = "photoService")
	public void setPhotoService(PhotoService ps) {
		this.photoService = ps;
	}

	@Autowired(required = true)
	@Qualifier(value = "systemConfigurationService")
	public void setFieldService(SystemConfigurationService ps) {
		this.configService = ps;
	}

	/**
	 * Delete a project's profile picture.
	 * 
	 * @param projectID
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = SystemConstants.REQUEST_DELETE + "/"
			+ SystemConstants.PROJECT_PROFILE, method = RequestMethod.POST)
	public ModelAndView deleteProjectProfile(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID)
			throws IOException {
		this.photoService.deleteProjectProfile(projectID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	@RequestMapping(value = SystemConstants.REQUEST_UPLOAD_TO_STAFF_PROFILE, method = RequestMethod.POST)
	public ModelAndView uploadStaffProfile(
			@RequestParam(ProjectFile.PARAM_FILE) MultipartFile file,
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID)
			throws IOException {

		// If file is not empty.
		if (!file.isEmpty()) {

			// Upload the file to the server.
			String fileLocation = getSysHome() + "/" + Staff.OBJECT_NAME + "/"
					+ staffID + "/profile/photo/" + file.getOriginalFilename();

			// Fetch some details and set.
			this.photoService.uploadStaffProfile(file, fileLocation, staffID);
		} else {
			// TODO Handle this scenario.
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ staffID);
	}

	@RequestMapping(value = SystemConstants.REQUEST_UPLOAD_TO_PROJECT_PROFILE, method = RequestMethod.POST)
	public ModelAndView uploadProjectProfile(
			@RequestParam(ProjectFile.PARAM_FILE) MultipartFile file,
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID)
			throws IOException {

		// If file is not empty.
		if (!file.isEmpty()) {

			// Upload the file to the server.
			String fileLocation = getSysHome() + "/" + Project.OBJECT_NAME
					+ "/" + projectID + "/profile/photo/"
					+ file.getOriginalFilename();

			// Fetch some details and set.
			this.photoService.uploadProjectProfile(file, fileLocation,
					projectID);
		} else {
			// TODO Handle this scenario.
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	@RequestMapping(value = SystemConstants.REQUEST_UPLOAD_TO_PROJECT, method = RequestMethod.POST)
	public ModelAndView uploadFileToProject(
			@RequestParam(ProjectFile.PARAM_FILE) MultipartFile file,
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(ProjectFile.COLUMN_DESCRIPTION) String description)
			throws IOException {

		// If file is not empty.
		if (!file.isEmpty()) {

			// Upload the file to the server.
			String fileLocation = getSysHome() + "/" + Project.OBJECT_NAME
					+ "/" + projectID + "/photos/" + file.getOriginalFilename();

			// Fetch some details and set.
			long size = file.getSize();
			Date dateUploaded = new Date(System.currentTimeMillis());
			Photo photo = new Photo();

			// TODO Uploader.
			photo.setUploader(new Staff(1));

			photo.setLocation(fileLocation);
			photo.setProject(new Project(projectID));
			photo.setName(file.getOriginalFilename());
			photo.setDescription(description);
			photo.setSize(size);
			photo.setDateUploaded(dateUploaded);
			this.photoService.create(file, fileLocation, photo);
		} else {
			// TODO Handle this scenario.
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listPhotos(Model model) {
		model.addAttribute(ATTR_LIST, this.photoService.list());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_PHOTO) Photo photo) {
		if (photo.getId() == 0) {
			// TODO
			// this.photoService.create(photo);
		} else {
			this.photoService.update(photo);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PHOTO + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping(SystemConstants.REQUEST_DELETE)
	public ModelAndView delete(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(Photo.COLUMN_PRIMARY_KEY) int id) {
		this.photoService.delete(id);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	@RequestMapping("/" + SystemConstants.REQUEST_EDIT + "/{"
			+ Photo.COLUMN_PRIMARY_KEY + "}")
	public String editPhoto(@PathVariable(Photo.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		if (id == 0) {
			model.addAttribute(ATTR_PHOTO, new Photo());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_PHOTO, this.photoService.getByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}
}