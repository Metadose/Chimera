package com.cebedo.pmsys.projectfile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.projectfile.model.ProjectFile;
import com.cebedo.pmsys.projectfile.service.ProjectFileService;

@Controller
@RequestMapping(ProjectFile.OBJECT_NAME)
public class ProjectFileController {

	public static final String ATTR_LIST = "projectFileList";
	public static final String ATTR_PROJECTFILE = ProjectFile.OBJECT_NAME;
	public static final String JSP_LIST = "projectFileList";
	public static final String JSP_EDIT = "projectFileEdit";

	private ProjectFileService projectFileService;

	@Autowired(required = true)
	@Qualifier(value = "projectFileService")
	public void setProjectFileService(ProjectFileService ps) {
		this.projectFileService = ps;
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

	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(
			@ModelAttribute(ATTR_PROJECTFILE) ProjectFile projectFile) {
		if (projectFile.getId() == 0) {
			this.projectFileService.create(projectFile);
		} else {
			this.projectFileService.update(projectFile);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECTFILE + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_DELETE + "/{"
			+ ProjectFile.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(ProjectFile.COLUMN_PRIMARY_KEY) int id) {
		this.projectFileService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECTFILE + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_EDIT + "/{"
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

	/**
	 * Update a description of a file.
	 * 
	 * @param fileID
	 * @param description
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_UPDATE, method = RequestMethod.POST)
	public ModelAndView updateDescription(
			@RequestParam(ProjectFile.COLUMN_PRIMARY_KEY) long fileID,
			@RequestParam(ProjectFile.COLUMN_DESCRIPTION) String description) {
		this.projectFileService.updateDescription(fileID, description);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ ProjectFile.OBJECT_NAME + "/" + SystemConstants.REQUEST_LIST);
	}
}