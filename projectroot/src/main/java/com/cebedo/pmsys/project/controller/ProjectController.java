package com.cebedo.pmsys.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.project.service.ProjectService;

@Controller
@RequestMapping(Project.OBJECT_NAME)
public class ProjectController {

	public static final String ATTR_PROJECT = Project.OBJECT_NAME;
	public static final String ATTR_LIST = "projectList";

	public static final String REQUEST_ROOT = "/";
	public static final String REQUEST_LIST = "list";
	public static final String REQUEST_EDIT = "edit";
	public static final String REQUEST_CREATE = "create";

	public static final String JSP_LIST = "projectList";
	public static final String JSP_EDIT = "projectEdit";

	private ProjectService projectService;

	@Autowired(required = true)
	@Qualifier(value = "projectService")
	public void setProjectService(ProjectService ps) {
		this.projectService = ps;
	}

	@RequestMapping(value = "edit")
	public String editProject() {
		return "projectEdit";
	}

	@RequestMapping(value = { REQUEST_ROOT, REQUEST_LIST }, method = RequestMethod.GET)
	public String listProjects(Model model) {
		model.addAttribute(ATTR_PROJECT, new Project());
		model.addAttribute(ATTR_LIST, this.projectService.list());
		return JSP_LIST;
	}

	@RequestMapping(value = REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(Project.OBJECT_NAME) Project project) {
		if (project.getId() == 0) {
			this.projectService.create(project);
		} else {
			this.projectService.update(project);
		}
		return "redirect:/" + REQUEST_LIST;
	}

	@RequestMapping("/delete/{" + Project.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(Project.COLUMN_PRIMARY_KEY) int id) {
		this.projectService.delete(id);
		return "redirect:/" + REQUEST_LIST;
	}

	@RequestMapping("/edit/{" + Project.COLUMN_PRIMARY_KEY + "}")
	public String editProject(@PathVariable(Project.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		model.addAttribute(Project.OBJECT_NAME, this.projectService.getByID(id));
		return JSP_EDIT;
	}
}