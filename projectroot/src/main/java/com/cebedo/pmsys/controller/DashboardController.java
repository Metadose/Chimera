package com.cebedo.pmsys.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cebedo.pmsys.constants.ConstantsSystem;

@Controller
@RequestMapping(DashboardController.REQUEST_MAPPING)
public class DashboardController {
    public static final String REQUEST_MAPPING = "dashboard";
    public static final String JSP_DASHBOARD = REQUEST_MAPPING + "/dashboard";

    @RequestMapping(value = { "", ConstantsSystem.REQUEST_ROOT })
    public String view(HttpSession session) {
	session.removeAttribute(ProjectController.ATTR_FROM_PROJECT);
	return JSP_DASHBOARD;
    }
}