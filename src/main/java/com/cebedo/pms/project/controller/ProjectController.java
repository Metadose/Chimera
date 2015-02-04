package com.cebedo.pms.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("project")
public class ProjectController {

	@RequestMapping(value = { "/index", "wow" })
	public String index() {
		return "index";
	}
}