package com.cebedo.pmsys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cebedo.pmsys.constants.ConstantsSystem;

@Controller
@RequestMapping(ConstantsSystem.REQUEST_ROOT)
public class MainController {

	private static final String JSP_HOME = "home";

	@RequestMapping(value = { ConstantsSystem.REQUEST_ROOT })
	public String view() {
		return ConstantsSystem.SYSTEM + "/" + JSP_HOME;
	}
}