package com.cebedo.pms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("project")
public class ProjectController {

	@RequestMapping(value = { "/list" })
	public String getProjectList() {
		return "index";
	}

	@RequestMapping(value = { "/index", "wow" })
	public String index() {
		return "index";
	}
}