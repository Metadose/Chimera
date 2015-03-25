package com.cebedo.pmsys.projectfile.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.projectfile.model.ProjectFile;
import com.cebedo.pmsys.projectfile.service.ProjectFileService;
import com.cebedo.pmsys.systemconfiguration.service.SystemConfigurationService;

@Controller
@RequestMapping(ProjectFile.OBJECT_NAME)
public class ProjectFileController {

	public static final String ATTR_LIST = "projectFileList";
	public static final String ATTR_PROJECTFILE = ProjectFile.OBJECT_NAME;
	public static final String JSP_LIST = "projectFileList";
	public static final String JSP_EDIT = "projectFileEdit";

	private ProjectFileService projectFileService;
	private SystemConfigurationService configService;
	private String sysHome;

	public String getSysHome() {
		if (sysHome == null) {
			sysHome = this.configService.getValueByName("SYS_HOME");
		}
		return sysHome;
	}

	@Autowired(required = true)
	@Qualifier(value = "projectFileService")
	public void setProjectFileService(ProjectFileService ps) {
		this.projectFileService = ps;
	}

	@Autowired(required = true)
	@Qualifier(value = "systemConfigurationService")
	public void setFieldService(SystemConfigurationService ps) {
		this.configService = ps;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listProjectFiles(Model model) {
		model.addAttribute(ATTR_LIST,
				this.projectFileService.listWithAllCollections());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECTFILE_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(
			@ModelAttribute(ATTR_PROJECTFILE) ProjectFile projectFile,
			RedirectAttributes redirectAttrs) {
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		if (projectFile.getId() == 0) {
			// TODO Create function?
			// Do we need this?
			// this.projectFileService.create(projectFile);
			;
		} else {
			this.projectFileService.update(projectFile);
			alertFactory.setMessage("Successfully <b>updated</b> file <b>"
					+ projectFile.getName() + "<b/>.");
		}
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECTFILE + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	/**
	 * Make a generalized version of this function. Implementation of Origins.
	 * 
	 * @param id
	 * @param projectID
	 * @param redirectAttrs
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE + "/"
			+ SystemConstants.FROM_PROJECT, method = RequestMethod.POST)
	public String deleteFromProject(
			@RequestParam(ProjectFile.COLUMN_PRIMARY_KEY) int id,
			@RequestParam(Project.COLUMN_PRIMARY_KEY) int projectID,
			RedirectAttributes redirectAttrs) {

		String fileName = this.projectFileService.getNameByID(id);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>deleted</b> file <b>"
				+ fileName + "<b/>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		this.projectFileService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + projectID;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECTFILE_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE + "/{"
			+ ProjectFile.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.POST)
	public String delete(@PathVariable(ProjectFile.COLUMN_PRIMARY_KEY) int id,
			RedirectAttributes redirectAttrs) {

		String fileName = this.projectFileService.getNameByID(id);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>deleted</b> file <b>"
				+ fileName + "<b/>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		this.projectFileService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECTFILE + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	/**
	 * Create new or open an existing one from origin.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/"
			+ SystemConstants.FROM + "/" + SystemConstants.ORIGIN, method = RequestMethod.POST)
	public String editProjectFileFromOrigin(
			@RequestParam(ProjectFile.COLUMN_PRIMARY_KEY) int id,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long originID,
			Model model) {
		model.addAttribute(SystemConstants.ORIGIN, origin);
		model.addAttribute(SystemConstants.ORIGIN_ID, originID);
		if (id == 0) {
			model.addAttribute(ATTR_PROJECTFILE, new ProjectFile());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_PROJECTFILE,
				this.projectFileService.getByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}

	/**
	 * Create new or open an existing one.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
			+ ProjectFile.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.POST)
	public String editProjectFile(
			@PathVariable(ProjectFile.COLUMN_PRIMARY_KEY) int id, Model model) {
		if (id == 0) {
			model.addAttribute(ATTR_PROJECTFILE, new ProjectFile());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_PROJECTFILE,
				this.projectFileService.getByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UPLOAD_FILE_TO_PROJECT, method = RequestMethod.POST)
	public ModelAndView uploadFileToProject(
			@RequestParam(ProjectFile.PARAM_FILE) MultipartFile file,
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(ProjectFile.COLUMN_DESCRIPTION) String description,
			RedirectAttributes redirectAttrs) throws IOException {
		AlertBoxFactory alertFactory = new AlertBoxFactory();
		// If file is not empty.
		if (!file.isEmpty()) {
			this.projectFileService.create(file, projectID, description);
			alertFactory.setStatus(SystemConstants.UI_STATUS_SUCCESS);
			alertFactory.setMessage("Successfully <b>uploaded</b> file <b>"
					+ file.getOriginalFilename() + "</b>.");
		} else {
			alertFactory.setStatus(SystemConstants.UI_STATUS_DANGER);
			alertFactory.setMessage("Failed to <b>upload</b> empty file <b>"
					+ file.getOriginalFilename() + "</b>.");
		}
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	/**
	 * TODO Put security here. TODO Create version where request can come from
	 * "origin" with "originID". TODO Put notification after download.
	 * 
	 * @param fileID
	 * @param response
	 * @param redirectAttrs
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_DOWNLOAD, method = RequestMethod.POST)
	public void downloadFile(
			@RequestParam(ProjectFile.COLUMN_PRIMARY_KEY) long fileID,
			HttpServletResponse response) {

		File actualFile = this.projectFileService.getPhysicalFileByID(fileID);
		// AlertBoxFactory alertFactory = new AlertBoxFactory();
		try {
			// alertFactory.setStatus(SystemConstants.UI_STATUS_INFO);
			// alertFactory.setMessage("<b>Downloading</b> file <b>"
			// + actualFile.getName() + "<b/>.");

			FileInputStream iStream = new FileInputStream(actualFile);
			response.setContentType("application/octet-stream");
			response.setContentLength((int) actualFile.length());
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ actualFile.getName() + "\"");
			IOUtils.copy(iStream, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			// alertFactory.setStatus(SystemConstants.UI_STATUS_DANGER);
			// alertFactory.setMessage("Failed to <b>download<b/> file <b>"
			// + actualFile.getName() + "<b/>.");

			e.printStackTrace();
		}
		// redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		// alertFactory.generateHTML());
		// return SystemConstants.CONTROLLER_REDIRECT + ProjectFile.OBJECT_NAME
		// + "/" + SystemConstants.REQUEST_LIST;
	}

	/**
	 * Download a file from Project. TODO Need work on security url here.
	 * 
	 * @param projectID
	 * @param fileID
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_DOWNLOAD + "/"
			+ SystemConstants.FROM_PROJECT, method = RequestMethod.POST)
	public void downloadFileFromProject(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(ProjectFile.COLUMN_PRIMARY_KEY) long fileID,
			HttpServletResponse response) {

		File actualFile = this.projectFileService.getPhysicalFileByID(fileID);
		// AlertBoxFactory alertFactory = new AlertBoxFactory();
		try {
			// alertFactory.setStatus(SystemConstants.UI_STATUS_INFO);
			// alertFactory.setMessage("<b>Downloading</b> file <b>"
			// + actualFile.getName() + "<b/>.");

			FileInputStream iStream = new FileInputStream(actualFile);
			response.setContentType("application/octet-stream");
			response.setContentLength((int) actualFile.length());
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ actualFile.getName() + "\"");
			IOUtils.copy(iStream, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			// alertFactory.setStatus(SystemConstants.UI_STATUS_DANGER);
			// alertFactory.setMessage("Failed to <b>download<b/> file <b>"
			// + actualFile.getName() + "<b/>.");
			e.printStackTrace();
		}

		// redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		// alertFactory.generateHTML());
		//
		// return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME +
		// "/"
		// + SystemConstants.REQUEST_EDIT + "/" + projectID;
	}

	// TODO Create another role which is not a Project File Editor but can
	// upload files.
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECTFILE_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UPLOAD_FILE, method = RequestMethod.POST)
	public ModelAndView handleFileUpload(
			@RequestParam(ProjectFile.PARAM_FILE) MultipartFile file,
			@RequestParam(ProjectFile.COLUMN_DESCRIPTION) String description,
			RedirectAttributes redirectAttrs) throws IOException {

		AlertBoxFactory alertFactory = new AlertBoxFactory();
		// If file is not empty.
		if (!file.isEmpty()) {
			this.projectFileService.createForStaff(file, description);
			alertFactory.setStatus(SystemConstants.UI_STATUS_SUCCESS);
			alertFactory.setMessage("Successfully <b>uploaded</b> file <b>"
					+ file.getOriginalFilename() + "<b/>.");
		} else {
			alertFactory.setStatus(SystemConstants.UI_STATUS_DANGER);
			alertFactory.setMessage("Failed to <b>upload</b> empty file <b>"
					+ file.getOriginalFilename() + "<b/>.");
		}
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ ProjectFile.OBJECT_NAME + "/" + SystemConstants.REQUEST_LIST);
	}

	/**
	 * Update a description of a file.
	 * 
	 * @param fileID
	 * @param description
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UPDATE, method = RequestMethod.POST)
	public ModelAndView updateDescription(
			@RequestParam(ProjectFile.COLUMN_PRIMARY_KEY) long fileID,
			@RequestParam(ProjectFile.COLUMN_DESCRIPTION) String description,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long originID,
			RedirectAttributes redirectAttrs) {

		this.projectFileService.updateDescription(fileID, description);
		String fileName = this.projectFileService.getNameByID(fileID);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory
				.setMessage("Successfully <b>updated description</b> of file <b>"
						+ fileName + "<b/>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		if (!(origin == null || originID == 0)) {
			return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
					+ origin + "/" + SystemConstants.REQUEST_EDIT + "/"
					+ originID);
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ ProjectFile.OBJECT_NAME + "/" + SystemConstants.REQUEST_LIST);
	}
}