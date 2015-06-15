package com.cebedo.pmsys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.model.SecurityRole;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.service.SystemConfigurationService;

@Controller
@RequestMapping(SystemConfiguration.OBJECT_NAME)
public class SystemConfigurationController {

    public static final String ATTR_LIST = "systemConfigurationList";
    public static final String ATTR_SYSTEM_CONFIGURATION = SystemConfiguration.OBJECT_NAME;
    public static final String JSP_LIST = SystemConfiguration.OBJECT_NAME
	    + "/systemConfigurationList";
    public static final String JSP_EDIT = SystemConfiguration.OBJECT_NAME
	    + "/systemConfigurationEdit";

    private SystemConfigurationService systemConfigurationService;

    @Autowired(required = true)
    @Qualifier(value = "systemConfigurationService")
    public void setSystemConfigurationService(SystemConfigurationService ps) {
	this.systemConfigurationService = ps;
    }

    @RequestMapping(value = { SystemConstants.REQUEST_ROOT,
	    SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String listSystemConfigurations(Model model) {
	model.addAttribute(ATTR_LIST, this.systemConfigurationService.list());
	return JSP_LIST;
    }

    @PreAuthorize("hasRole('" + SecurityRole.ROLE_CONFIG_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
    public String create(
	    @ModelAttribute(ATTR_SYSTEM_CONFIGURATION) SystemConfiguration systemConfiguration) {
	if (systemConfiguration.getId() == 0) {
	    this.systemConfigurationService.create(systemConfiguration);
	} else {
	    this.systemConfigurationService.update(systemConfiguration);
	}
	return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_CONFIGURATION
		+ "/" + SystemConstants.REQUEST_LIST;
    }

    @PreAuthorize("hasRole('" + SecurityRole.ROLE_CONFIG_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_DELETE + "/{"
	    + SystemConfiguration.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.POST)
    public String delete(
	    @PathVariable(SystemConfiguration.COLUMN_PRIMARY_KEY) int id) {
	this.systemConfigurationService.delete(id);
	return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_CONFIGURATION
		+ "/" + SystemConstants.REQUEST_LIST;
    }

    @PreAuthorize("hasRole('" + SecurityRole.ROLE_CONFIG_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
	    + SystemConfiguration.COLUMN_PRIMARY_KEY + "}")
    public String editSystemConfiguration(
	    @PathVariable(SystemConfiguration.COLUMN_PRIMARY_KEY) int id,
	    Model model) {
	if (id == 0) {
	    model.addAttribute(ATTR_SYSTEM_CONFIGURATION,
		    new SystemConfiguration());
	    return JSP_EDIT;
	}
	model.addAttribute(ATTR_SYSTEM_CONFIGURATION,
		this.systemConfigurationService.getByID(id));
	return JSP_EDIT;
    }

}