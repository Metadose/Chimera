package com.cebedo.pmsys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cebedo.pmsys.constants.RegistryURL;

@Controller
@RequestMapping("fix")
public class ErrorController {

    @RequestMapping(value = { "", "/" })
    public String error() {
	return RegistryURL.REDIRECT_DASHBOARD;
    }

}
