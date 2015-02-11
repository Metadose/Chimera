package com.cebedo.pmsys.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dashboard")
public class DashboardController {

	@RequestMapping(value = { "/" })
	public String view() {
		return "dashboard";
	}

}
