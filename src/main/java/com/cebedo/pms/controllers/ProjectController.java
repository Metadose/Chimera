package com.cebedo.pms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("project")
public class ProjectController {

	@RequestMapping(value = { "/", "/list" })
	public String getProjectList() {
		return "projectsList";
	}

	@RequestMapping(value = "edit")
	public String editProject() {
		return "projectEdit";
	}
}