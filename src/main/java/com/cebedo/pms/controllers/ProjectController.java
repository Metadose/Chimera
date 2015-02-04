package com.cebedo.pms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProjectController {

	@RequestMapping(value = { "/", "/index" })
	public String getProjectList() {
		return "index";
	}

	@RequestMapping(value = { "/index", "wow" })
	public String index() {
		return "index";
	}
}