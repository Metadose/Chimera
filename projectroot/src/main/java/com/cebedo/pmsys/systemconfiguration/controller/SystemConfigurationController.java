package com.cebedo.pmsys.systemconfiguration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.systemconfiguration.model.SystemConfiguration;
import com.cebedo.pmsys.systemconfiguration.service.SystemConfigurationService;

@Controller
@RequestMapping(SystemConfiguration.OBJECT_NAME)
public class SystemConfigurationController {

	public static final String ATTR_LIST = "systemConfigurationList";
	public static final String ATTR_SYSTEM_CONFIGURATION = SystemConfiguration.OBJECT_NAME;
	public static final String JSP_LIST = "systemConfigurationList";
	public static final String JSP_EDIT = "systemConfigurationEdit";

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
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

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

	@RequestMapping("/" + SystemConstants.REQUEST_DELETE + "/{"
			+ SystemConfiguration.COLUMN_PRIMARY_KEY + "}")
	public String delete(
			@PathVariable(SystemConfiguration.COLUMN_PRIMARY_KEY) int id) {
		this.systemConfigurationService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_CONFIGURATION
				+ "/" + SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_EDIT + "/{"
			+ SystemConfiguration.COLUMN_PRIMARY_KEY + "}")
	public String editSystemConfiguration(
			@PathVariable(SystemConfiguration.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		if (id == 0) {
			model.addAttribute(ATTR_SYSTEM_CONFIGURATION,
					new SystemConfiguration());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_SYSTEM_CONFIGURATION,
				this.systemConfigurationService.getByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}

}