package com.cebedo.pmsys.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cebedo.pmsys.constants.ConstantsSystem;
import com.cebedo.pmsys.constants.RegistryURL;

@Controller
@RequestMapping(DashboardController.REQUEST_MAPPING)
public class DashboardController {
    public static final String REQUEST_MAPPING = "dashboard";
    public static final String JSP_DASHBOARD = REQUEST_MAPPING + "/dashboard";

    /**
     * TODO Implement a company-wide dashboard.
     * 
     * @param session
     * @return
     */
    @RequestMapping(value = { "", ConstantsSystem.REQUEST_ROOT })
    public String view(HttpSession session) {
	session.removeAttribute(ProjectController.ATTR_FROM_PROJECT);
	return RegistryURL.REDIRECT_LIST_PROJECT;
    }
}