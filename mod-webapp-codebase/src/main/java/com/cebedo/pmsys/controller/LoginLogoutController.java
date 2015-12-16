/**
 * 
 */
package com.cebedo.pmsys.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.constants.ConstantsSystem;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.factory.AlertBoxFactory;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.service.FieldService;
import com.cebedo.pmsys.service.SystemConfigurationService;
import com.cebedo.pmsys.service.SystemUserService;

/**
 * Handles and retrieves the login or denied page depending on the URI template
 */
@Controller
@RequestMapping(LoginLogoutController.MAPPING_CONTROLLER)
public class LoginLogoutController {

    protected static Logger logger = Logger.getLogger(ConstantsSystem.LOGGER_LOGIN);
    public static final String MAPPING_CONTROLLER = "auth";
    public static final String JSP_LOGIN = MAPPING_CONTROLLER + "/login";

    public static String cdnUrl = null;
    public static Boolean cdn = null;
    public static Boolean initWebApp = null;
    public boolean initFields = false;

    private SystemConfigurationService configService;
    private SystemUserService systemUserService;
    private FieldService fieldService;

    @Autowired(required = true)
    @Qualifier(value = "fieldService")
    public void setFieldService(FieldService fieldService) {
	this.fieldService = fieldService;
    }

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
	model.addAttribute(ConstantsSystem.UI_PARAM_ALERT,
		AlertBoxFactory.FAILED.generateHTML(RegistryResponseMessage.ERROR_AUTH_LOGIN_GENERIC));
	return getLoginPage(model);
    }

    @RequestMapping(value = "/logout/company/update", method = RequestMethod.GET)
    public String loginCompanyUpdate(Model model, HttpSession session) {
	SecurityContextHolder.getContext().setAuthentication(null);
	model.addAttribute(ConstantsSystem.UI_PARAM_ALERT, AlertBoxFactory.SUCCESS
		.generateHTML(RegistryResponseMessage.SUCCESS_AUTH_LOGIN_GENERIC));
	return getLoginPage(model);
    }

    /**
     * Handles and retrieves the login JSP page.
     * 
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(Model model) {
	initWebApp();
	initFields();
	if (cdn == null) {
	    cdn = this.configService.getCdn();
	}
	if (cdnUrl == null) {
	    cdnUrl = this.configService.getCdnUrl();
	}
	model.addAttribute("cdn", cdn);
	model.addAttribute("cdnUrl", cdnUrl);
	return JSP_LOGIN;
    }

    private void initFields() {

	// If no value, get value.
	if (!this.initFields) {

	    // Get from database.
	    String fieldName = Field.FIELD_TEXTFIELD_NAME;
	    Field textField = this.fieldService.getByName(fieldName);

	    // If the field is not found,
	    // create a new field, set to true.
	    if (textField == null) {
		Field newField = new Field(fieldName);
		this.fieldService.create(newField);
		this.initFields = true;
	    }
	    // If found, set to true.
	    else {
		this.initFields = true;
	    }
	}
    }

    private void initWebApp() {

	// If no value, get value.
	if (initWebApp == null) {
	    String valStr = this.configService.getValueByName(ConstantsSystem.CONFIG_ROOT_INIT, true);

	    // If the configuration is not found,
	    // app init is false, and create a new config.
	    if (valStr == null) {
		initWebApp = false;
		SystemConfiguration config = new SystemConfiguration();
		config.setName(ConstantsSystem.CONFIG_ROOT_INIT);
		config.setValue("1");
		this.configService.create(config, null);
	    }

	    // If found, and already init,
	    // set flag to true.
	    else if (valStr.equalsIgnoreCase("1") || valStr.equalsIgnoreCase("Yes")
		    || valStr.equalsIgnoreCase("True")) {
		initWebApp = true;
	    }

	    // If found, but not yet init,
	    // set config, and set flag to false.
	    else {
		initWebApp = false;
		SystemConfiguration config = this.configService
			.getByName(ConstantsSystem.CONFIG_ROOT_INIT, true);
		config.setValue("1");
		this.configService.merge(config, null);
	    }
	}

	// If not yet init. Init it.
	if (!initWebApp) {
	    this.systemUserService.initRoot();
	}
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