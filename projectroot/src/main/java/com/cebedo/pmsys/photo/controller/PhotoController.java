package com.cebedo.pmsys.photo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.common.ui.AlertBoxFactory;
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
			sysHome = this.configService
					.getValueByName(SystemConstants.CONFIG_SYS_HOME);
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
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE + "/"
			+ SystemConstants.PROJECT_PROFILE, method = RequestMethod.POST)
	public ModelAndView deleteProjectProfile(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			RedirectAttributes redirectAttrs) throws IOException {
		this.photoService.deleteProjectProfile(projectID);

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory
				.setMessage("Successfully <b>deleted</b> the <b>profile picture</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UPLOAD_TO_STAFF_PROFILE, method = RequestMethod.POST)
	public ModelAndView uploadStaffProfile(
			@RequestParam(ProjectFile.PARAM_FILE) MultipartFile file,
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID)
			throws IOException {

		// If file is not empty.
		if (!file.isEmpty()) {
			this.photoService.uploadStaffProfile(file, staffID);
		} else {
			// TODO Handle this scenario.
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ staffID);
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UPLOAD_TO_PROJECT_PROFILE, method = RequestMethod.POST)
	public ModelAndView uploadProjectProfile(
			@RequestParam(ProjectFile.PARAM_FILE) MultipartFile file,
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID)
			throws IOException {

		// If file is not empty.
		if (!file.isEmpty()) {
			this.photoService.uploadProjectProfile(file, projectID);
		} else {
			// TODO Handle this scenario.
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UPLOAD + "/"
			+ Project.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView uploadFileToProject(
			@RequestParam(ProjectFile.PARAM_FILE) MultipartFile file,
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(ProjectFile.COLUMN_DESCRIPTION) String description,
			RedirectAttributes redirectAttrs) throws IOException {

		// If file is not empty.
		if (!file.isEmpty()) {
			this.photoService.create(file, projectID, description);
		} else {
			// TODO Handle this scenario.
		}

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>uploaded</b> the photo <b>"
				+ file.getOriginalFilename() + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
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

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PHOTO_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_PHOTO) Photo photo,
			RedirectAttributes redirectAttrs) {
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		if (photo.getId() == 0) {
			// TODO
			// this.photoService.create(photo);
		} else {
			alertFactory.setMessage("Successfully <b>updated</b> the photo <b>"
					+ photo.getName() + "</b>.");
			this.photoService.update(photo);
		}
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PHOTO + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE, method = RequestMethod.POST)
	public ModelAndView delete(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(Photo.COLUMN_PRIMARY_KEY) int id) {
		this.photoService.delete(id);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PHOTO_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
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