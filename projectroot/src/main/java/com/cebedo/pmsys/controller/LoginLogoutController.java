/**
 * 
 */
package com.cebedo.pmsys.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.service.SystemConfigurationService;
import com.cebedo.pmsys.service.SystemUserService;

/**
 * Handles and retrieves the login or denied page depending on the URI template
 */
@Controller
@RequestMapping(LoginLogoutController.MAPPING_CONTROLLER)
public class LoginLogoutController {

    protected static Logger logger = Logger.getLogger(SystemConstants.LOGGER_LOGIN);
    public static final String MAPPING_CONTROLLER = "auth";
    public static final String JSP_LOGIN = MAPPING_CONTROLLER + "/login";
    public static Boolean appInit = null;
    private SystemConfigurationService configService;
    private SystemUserService systemUserService;

    @Autowired(required = true)
    @Qualifier(value = "systemUserService")
    public void setSystemUserService(SystemUserService ps) {
	this.systemUserService = ps;
    }

    @Autowired(required = true)
    @Qualifier(value = "systemConfigurationService")
    public void setFieldService(SystemConfigurationService ps) {
	this.configService = ps;
    }

    @RequestMapping(value = "/login/error", method = RequestMethod.GET)
    public String loginError(Model model) {
	model.addAttribute(SystemConstants.UI_PARAM_ALERT, "Login failed");
	return getLoginPage();
    }

    /**
     * Handles and retrieves the login JSP page.
     * 
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
	// If no value, get value.
	if (appInit == null) {
	    String valStr = this.configService.getValueByName(SystemConstants.CONFIG_ROOT_INIT, true);

	    // If the configuration is not found,
	    // app init is false, and create a new config.
	    if (valStr == null) {
		appInit = false;
		SystemConfiguration config = new SystemConfiguration();
		config.setName(SystemConstants.CONFIG_ROOT_INIT);
		config.setValue("1");
		this.configService.create(config);
	    }

	    // If found, and already init,
	    // set flag to true.
	    else if (valStr.equalsIgnoreCase("1") || valStr.equalsIgnoreCase("Yes")
		    || valStr.equalsIgnoreCase("True")) {
		appInit = true;
	    }

	    // If found, but not yet init,
	    // set config, and set flag to false.
	    else {
		appInit = false;
		SystemConfiguration config = this.configService
			.getByName(SystemConstants.CONFIG_ROOT_INIT);
		config.setValue("1");
		this.configService.merge(config);
	    }
	}

	// If not yet init. Init it.
	if (!appInit) {
	    this.systemUserService.initRoot();
	}
	return JSP_LOGIN;
    }

    /**
     * Handles and retrieves the denied JSP page. This is shown whenever a
     * regular user tries to access an admin only page.
     * 
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/denied", method = RequestMethod.GET)
    public String getDeniedPage() {
	// TODO
	// logger.debug("Received request to show denied page");
	return "deniedpage";
    }
}