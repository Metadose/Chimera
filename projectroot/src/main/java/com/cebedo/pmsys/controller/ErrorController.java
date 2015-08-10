package com.cebedo.pmsys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cebedo.pmsys.constants.URLRegistry;

@Controller
@RequestMapping("fix")
public class ErrorController {

    @RequestMapping(value = { "", "/" })
    public String error() {
	// return "error/error";
	return URLRegistry.REDIRECT_DASHBOARD;
    }

}
