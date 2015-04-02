/**
 * 
 */
package com.cebedo.pmsys.login.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.systemconfiguration.service.SystemConfigurationService;
import com.cebedo.pmsys.systemuser.service.SystemUserService;

/**
 * Handles and retrieves the login or denied page depending on the URI template
 */
@Controller
@RequestMapping("/auth")
public class LoginLogoutController {

	protected static Logger logger = Logger
			.getLogger(SystemConstants.LOGGER_LOGIN);
	public static final String JSP_LOGIN = "login";
	public static final String JSP_INIT = "init";
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

	/**
	 * Handles and retrieves the login JSP page.
	 * 
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLoginPage() {
		// If no value, get value.
		if (appInit == null) {
			String valStr = this.configService
					.getValueByName(SystemConstants.CONFIG_ROOT_INIT);
			appInit = Boolean.valueOf(valStr);

			if (appInit == null) {
				// TODO Crash then log.
			}
		}

		// If not yet init. Init it.
		if (!appInit) {
			this.systemUserService.initRoot();
		}

		// If init already.

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