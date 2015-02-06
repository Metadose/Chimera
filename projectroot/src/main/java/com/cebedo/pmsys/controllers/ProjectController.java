package com.cebedo.pmsys.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("project")
public class ProjectController {

	@RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)
	public String getProjectList(ModelMap model) {
		model.addAttribute("message", "Spring 3 MVC Hello World");
		return "projectList";
	}

	@RequestMapping(value = "edit")
	public String editProject() {
		return "projectEdit";
	}
}