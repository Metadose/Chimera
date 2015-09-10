package com.cebedo.pmsys.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String view(HttpSession session, Model model, RedirectAttributes redirectAttrs) {
	// TODO Will be removed if implementing company-wide dashboard.
	String errorMsg = (String) model.asMap().get(ConstantsSystem.UI_PARAM_ALERT);
	if (errorMsg != null && !errorMsg.isEmpty()) {
	    redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, errorMsg);
	}

	session.removeAttribute(ProjectController.ATTR_FROM_PROJECT);
	return RegistryURL.REDIRECT_LIST_PROJECT;
    }
}