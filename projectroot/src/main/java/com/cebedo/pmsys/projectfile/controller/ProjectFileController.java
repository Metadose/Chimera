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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.project.service.ProjectService;
import com.cebedo.pmsys.projectfile.model.ProjectFile;
import com.cebedo.pmsys.projectfile.service.ProjectFileService;
import com.cebedo.pmsys.security.securityrole.model.SecurityRole;
import com.cebedo.pmsys.system.constants.SystemConstants;
import com.cebedo.pmsys.system.ui.AlertBoxFactory;
import com.cebedo.pmsys.systemconfiguration.service.SystemConfigurationService;

@Controller
@SessionAttributes(value = { ProjectFileController.ATTR_PROJECTFILE }, types = { ProjectFile.class })
@RequestMapping(ProjectFile.OBJECT_NAME)
public class ProjectFileController {

	public static final String ATTR_LIST = "projectFileList";
	public static final String ATTR_PROJECTFILE = ProjectFile.OBJECT_NAME;
	public static final String JSP_LIST = "projectFileList";
	public static final String JSP_EDIT = "projectFileEdit";

	private ProjectFileService projectFileService;
	private SystemConfigurationService configService;
	private ProjectService projectService;
	private String sysHome;

	public String getSysHome() {
		if (sysHome == null) {
			sysHome = this.configService
					.getValueByName(SystemConstants.CONFIG_SYS_HOME);
		}
		return sysHome;
	}

	@Autowired(required = true)
	@Qualifier(value = "projectService")
	public void setProjectService(ProjectService s) {
		this.projectService = s;
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

	@PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECTFILE_EDITOR + "')")
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
					+ projectFile.getName() + "</b>.");
		}
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECTFILE + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	/**
	 * Delete a file.
	 * 
	 * @param id
	 * @param redirectAttrs
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECTFILE_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE + "/{"
			+ ProjectFile.OBJECT_NAME + "}", method = RequestMethod.GET)
	public String delete(@PathVariable(ProjectFile.OBJECT_NAME) int id,
			RedirectAttributes redirectAttrs) {

		String fileName = this.projectFileService.getNameByID(id);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>deleted</b> file <b>"
				+ fileName + "</b>.");
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
	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
			+ ProjectFile.OBJECT_NAME + "}/" + SystemConstants.FROM + "/{"
			+ SystemConstants.ORIGIN + "}/{" + SystemConstants.ORIGIN_ID + "}", method = RequestMethod.GET)
	public String editProjectFileFromOrigin(
			@PathVariable(ProjectFile.OBJECT_NAME) int id,
			@PathVariable(SystemConstants.ORIGIN) String origin,
			@PathVariable(SystemConstants.ORIGIN_ID) long originID, Model model) {
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
			+ ProjectFile.OBJECT_NAME + "}", method = RequestMethod.GET)
	public String editProjectFile(
			@PathVariable(ProjectFile.OBJECT_NAME) int id, Model model) {
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
			// + actualFile.getName() + "</b>.");

			FileInputStream iStream = new FileInputStream(actualFile);
			response.setContentType("application/octet-stream");
			response.setContentLength((int) actualFile.length());
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ actualFile.getName() + "\"");
			IOUtils.copy(iStream, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			// alertFactory.setStatus(SystemConstants.UI_STATUS_DANGER);
			// alertFactory.setMessage("Failed to <b>download</b> file <b>"
			// + actualFile.getName() + "</b>.");

			e.printStackTrace();
		}
		// redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		// alertFactory.generateHTML());
		// return SystemConstants.CONTROLLER_REDIRECT + ProjectFile.OBJECT_NAME
		// + "/" + SystemConstants.REQUEST_LIST;
	}

	// TODO Create another role which is not a Project File Editor but can
	// upload files.
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECTFILE_EDITOR + "')")
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
					+ file.getOriginalFilename() + "</b>.");
		} else {
			alertFactory.setStatus(SystemConstants.UI_STATUS_DANGER);
			alertFactory.setMessage("Failed to <b>upload</b> empty file <b>"
					+ file.getOriginalFilename() + "</b>.");
		}
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ ProjectFile.OBJECT_NAME + "/" + SystemConstants.REQUEST_LIST);
	}

	/**
	 * Update a description of a file from origin.
	 * 
	 * @param fileID
	 * @param description
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_UPDATE + "/"
			+ SystemConstants.FROM + "/{" + SystemConstants.ORIGIN + "}/{"
			+ SystemConstants.ORIGIN_ID + "}", method = RequestMethod.POST)
	public String updateProjectFileFromOrigin(
			@ModelAttribute(ATTR_PROJECTFILE) ProjectFile pFile,
			@PathVariable(SystemConstants.ORIGIN) String origin,
			@PathVariable(SystemConstants.ORIGIN_ID) long originID,
			SessionStatus status, RedirectAttributes redirectAttrs) {
		long fileID = pFile.getId();
		String description = pFile.getDescription();

		this.projectFileService.updateDescription(fileID, description);

		// If the origin is from a project, clear the cache of that project.
		if (origin.equals(Project.OBJECT_NAME)) {
			this.projectService.clearProjectCache(originID);
		}

		String fileName = this.projectFileService.getNameByID(fileID);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory
				.setMessage("Successfully <b>updated description</b> of file <b>"
						+ fileName + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + origin + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + originID;
	}

	/**
	 * Update a description of a file.
	 * 
	 * @param fileID
	 * @param description
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UPDATE, method = RequestMethod.POST)
	public String updateProjectFile(
			@ModelAttribute(ATTR_PROJECTFILE) ProjectFile pFile,
			SessionStatus status, RedirectAttributes redirectAttrs) {
		long fileID = pFile.getId();
		String description = pFile.getDescription();

		this.projectFileService.updateDescription(fileID, description);
		String fileName = this.projectFileService.getNameByID(fileID);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory
				.setMessage("Successfully <b>updated description</b> of file <b>"
						+ fileName + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ProjectFile.OBJECT_NAME
				+ "/" + SystemConstants.REQUEST_EDIT + "/" + fileID;
	}
}