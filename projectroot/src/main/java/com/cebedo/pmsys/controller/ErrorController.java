package com.cebedo.pmsys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("fix")
public class ErrorController {

	@RequestMapping(value = { "", "/" })
	public String error500() {
		return "error/500";
	}

	@RequestMapping(value = { "/404" })
	public String error404() {
		return "error/404";
	}
}
