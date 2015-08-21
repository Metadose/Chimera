package com.cebedo.pmsys.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.constants.ConstantsSystem;
import com.cebedo.pmsys.constants.RegistryJSPPath;
import com.cebedo.pmsys.constants.RegistryURL;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.service.SystemConfigurationService;

@Controller
@RequestMapping(SystemConfiguration.OBJECT_NAME)
@SessionAttributes(

value = { SystemConfigurationController.ATTR_SYSTEM_CONFIGURATION }

)
public class SystemConfigurationController {

    public static final String ATTR_LIST = "systemConfigurationList";
    public static final String ATTR_SYSTEM_CONFIGURATION = SystemConfiguration.OBJECT_NAME;

    private SystemConfigurationService systemConfigurationService;

    @Autowired(required = true)
    @Qualifier(value = "systemConfigurationService")
    public void setSystemConfigurationService(SystemConfigurationService ps) {
	this.systemConfigurationService = ps;
    }

    /**
     * List all system configurations.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { ConstantsSystem.REQUEST_ROOT, ConstantsSystem.REQUEST_LIST }, method = RequestMethod.GET)
    public String listSystemConfigurations(Model model, HttpSession session) {
	model.addAttribute(ATTR_LIST, this.systemConfigurationService.list());
	session.removeAttribute(ProjectController.ATTR_FROM_PROJECT);
	return RegistryJSPPath.JSP_LIST_SYS_CONFIG;
    }

    /**
     * Create or update a system configuration object.
     * 
     * @param systemConfiguration
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_CREATE, method = RequestMethod.POST)
    public String create(
	    @ModelAttribute(ATTR_SYSTEM_CONFIGURATION) SystemConfiguration systemConfiguration,
	    RedirectAttributes redirectAttrs, SessionStatus status) {

	String response = "";
	if (systemConfiguration.getId() == 0) {
	    response = this.systemConfigurationService.create(systemConfiguration);
	} else {
	    response = this.systemConfigurationService.update(systemConfiguration);
	}

	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, response);
	status.setComplete();
	return editPage(systemConfiguration.getId());
    }

    /**
     * Return to edit page.
     * 
     * @param id
     * @return
     */
    private String editPage(long id) {
	return String.format(RegistryURL.REDIRECT_EDIT_SYS_CONFIG, id);
    }

    /**
     * Delete a configuration.
     * 
     * @param id
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_DELETE + "/{"
	    + SystemConfiguration.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.GET)
    public String delete(@PathVariable(SystemConfiguration.COLUMN_PRIMARY_KEY) int id,
	    RedirectAttributes redirectAttrs, SessionStatus status) {
	String response = this.systemConfigurationService.delete(id);
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, response);
	return listPage(status);
    }

    /**
     * Redirect to list page.
     * 
     * @param status
     * 
     * @return
     */
    private String listPage(SessionStatus status) {
	status.setComplete();
	return RegistryURL.REDIRECT_LIST_SYS_CONFIG;
    }

    /**
     * Edit a system configuration.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_EDIT + "/{" + SystemConfiguration.COLUMN_PRIMARY_KEY
	    + "}")
    public String editSystemConfiguration(@PathVariable(SystemConfiguration.COLUMN_PRIMARY_KEY) int id,
	    Model model) {

	// If empty config.
	if (id == 0) {
	    model.addAttribute(ATTR_SYSTEM_CONFIGURATION, new SystemConfiguration());
	    return RegistryJSPPath.JSP_EDIT_SYS_CONFIG;
	}

	// If config already exists.
	model.addAttribute(ATTR_SYSTEM_CONFIGURATION, this.systemConfigurationService.getByID(id));
	return RegistryJSPPath.JSP_EDIT_SYS_CONFIG;
    }

}