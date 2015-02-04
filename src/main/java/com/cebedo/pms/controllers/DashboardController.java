package com.cebedo.pms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dashboard")
public class DashboardController {

	@RequestMapping(value = { "/test" })
	public String view() {
		return "dashboard";
	}

}
