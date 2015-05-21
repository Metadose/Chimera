package com.cebedo.pmsys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cebedo.pmsys.constants.SystemConstants;

@Controller
@RequestMapping(DashboardController.REQUEST_MAPPING)
public class DashboardController {
    public static final String REQUEST_MAPPING = "dashboard";
    public static final String JSP_DASHBOARD = REQUEST_MAPPING + "/dashboard";

    @RequestMapping(value = { SystemConstants.REQUEST_ROOT })
    public String view() {
	return JSP_DASHBOARD;
    }
}