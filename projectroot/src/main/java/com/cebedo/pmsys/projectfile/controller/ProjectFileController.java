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

import com.cebedo.pmsys.common.SystemConstants;
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
			@ModelAttribute(ATTR_PROJECTFILE) ProjectFile projectFile) {
		if (projectFile.getId() == 0) {
			// TODO Create function?
			// Do we need this?
			// this.projectFileService.create(projectFile);
			;
		} else {
			this.projectFileService.update(projectFile);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECTFILE + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(SystemConstants.REQUEST_DELETE + "/"
			+ SystemConstants.FROM_PROJECT)
	public String deleteFromProject(
			@RequestParam(ProjectFile.COLUMN_PRIMARY_KEY) int id,
			@RequestParam(Project.COLUMN_PRIMARY_KEY) int projectID) {
		this.projectFileService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + projectID;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECTFILE_EDITOR + "')")
	@RequestMapping(SystemConstants.REQUEST_DELETE + "/{"
			+ ProjectFile.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(ProjectFile.COLUMN_PRIMARY_KEY) int id) {
		this.projectFileService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECTFILE + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECTFILE_EDITOR + "')")
	@RequestMapping(SystemConstants.REQUEST_EDIT + "/{"
			+ ProjectFile.COLUMN_PRIMARY_KEY + "}")
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
			@RequestParam(ProjectFile.COLUMN_DESCRIPTION) String description)
			throws IOException {
		// If file is not empty.
		if (!file.isEmpty()) {
			this.projectFileService.create(file, projectID, description);
		} else {
			// TODO Handle this scenario.
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	@RequestMapping(value = SystemConstants.REQUEST_DOWNLOAD, method = RequestMethod.POST)
	public void downloadFile(
			@RequestParam(ProjectFile.COLUMN_PRIMARY_KEY) long fileID,
			HttpServletResponse response) {

		File actualFile = this.projectFileService.getPhysicalFileByID(fileID);
		try {
			FileInputStream iStream = new FileInputStream(actualFile);
			response.setContentType("application/octet-stream");
			response.setContentLength((int) actualFile.length());
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ actualFile.getName() + "\"");
			IOUtils.copy(iStream, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		try {
			FileInputStream iStream = new FileInputStream(actualFile);
			response.setContentType("application/octet-stream");
			response.setContentLength((int) actualFile.length());
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ actualFile.getName() + "\"");
			IOUtils.copy(iStream, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO Create another role which is not a Project File Editor but can
	// upload files.
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECTFILE_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UPLOAD_FILE, method = RequestMethod.POST)
	public ModelAndView handleFileUpload(
			@RequestParam(ProjectFile.PARAM_FILE) MultipartFile file,
			@RequestParam(ProjectFile.COLUMN_DESCRIPTION) String description)
			throws IOException {

		// If file is not empty.
		if (!file.isEmpty()) {
			this.projectFileService.createForStaff(file, description);
		} else {
			// TODO Handle this scenario.
		}
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
			@RequestParam(ProjectFile.COLUMN_DESCRIPTION) String description) {
		this.projectFileService.updateDescription(fileID, description);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ ProjectFile.OBJECT_NAME + "/" + SystemConstants.REQUEST_LIST);
	}
}