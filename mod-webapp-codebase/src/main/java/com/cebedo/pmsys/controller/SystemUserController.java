package com.cebedo.pmsys.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.constants.ConstantsAuthority.AuthorizedAction;
import com.cebedo.pmsys.constants.ConstantsAuthority.AuthorizedModule;
import com.cebedo.pmsys.constants.ConstantsSystem;
import com.cebedo.pmsys.constants.RegistryJSPPath;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.constants.RegistryURL;
import com.cebedo.pmsys.domain.UserAux;
import com.cebedo.pmsys.factory.AlertBoxFactory;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.service.CompanyService;
import com.cebedo.pmsys.service.SystemUserService;

@Controller
@SessionAttributes(

value = { SystemUserController.ATTR_SYSTEM_USER, SystemUserController.ATTR_USER_AUX }

)
@RequestMapping(SystemUser.OBJECT_NAME)
public class SystemUserController {

    public static final String ATTR_LIST = "systemUserList";
    public static final String ATTR_SYSTEM_USER = SystemUser.OBJECT_NAME;
    public static final String ATTR_COMPANY_LIST = Company.OBJECT_NAME + "List";

    // Authorizations.
    public static final String ATTR_USER_AUX = "userAux";
    public static final String ATTR_MODULE_LIST = "moduleList";
    public static final String ATTR_ACTION_LIST = "actionList";

    private AuthHelper authHelper = new AuthHelper();
    private SystemUserService systemUserService;
    private CompanyService companyService;

    @Autowired(required = true)
    @Qualifier(value = "companyService")
    public void setCompanyService(CompanyService ps) {
	this.companyService = ps;
    }

    @Autowired(required = true)
    @Qualifier(value = "systemUserService")
    public void setSystemUserService(SystemUserService ps) {
	this.systemUserService = ps;
    }

    /**
     * List all system users.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { ConstantsSystem.REQUEST_ROOT,
	    ConstantsSystem.REQUEST_LIST }, method = RequestMethod.GET)
    public String listSystemUsers(Model model, HttpSession session) {
	model.addAttribute(ATTR_LIST, this.systemUserService.list());
	session.removeAttribute(ProjectController.ATTR_FROM_PROJECT);
	return RegistryJSPPath.JSP_LIST_SYSTEM_USER;
    }

    /**
     * Update authorities of a user.
     * 
     * @param userAux
     * @param status
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = RegistryURL.AUTHORIZE, method = RequestMethod.POST)
    public String updateAuthority(@ModelAttribute(ATTR_USER_AUX) UserAux userAux, SessionStatus status,
	    RedirectAttributes redirectAttrs) {
	SystemUser systemUser = userAux.getUser();
	String response = this.systemUserService.updateAuthority(userAux);
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, response);
	status.setComplete();
	return editPage(systemUser.getId());
    }

    @RequestMapping(value = ConstantsSystem.REQUEST_CREATE, method = RequestMethod.POST)
    public String create(@ModelAttribute(ATTR_SYSTEM_USER) SystemUser systemUser, SessionStatus status,
	    RedirectAttributes redirectAttrs, BindingResult result) {

	// If request is to create new user.
	if (systemUser.getId() == 0) {

	    // If there is already an existing user with that user name.
	    // AlertBoxFactory here is ok, special case.
	    try {
		@SuppressWarnings("unused")
		SystemUser user = this.systemUserService.searchDatabase(systemUser.getUsername());
		redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, AlertBoxFactory.FAILED
			.generateHTML(RegistryResponseMessage.ERROR_AUTH_USERNAME_NOT_AVAILABLE));
		status.setComplete();
		return editPage(systemUser.getId());

	    }
	    // If everything is ok, create.
	    catch (Exception e) {
		String response = this.systemUserService.create(systemUser, result);
		redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, response);
		status.setComplete();
		return editPage(systemUser.getId());
	    }
	}

	// If request is to update user.
	// Redirect back to the edit page.
	String response = this.systemUserService.update(systemUser, result);
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, response);
	status.setComplete();
	return editPage(systemUser.getId());
    }

    /**
     * Return back to the list page.
     * 
     * @param status
     * 
     * @return
     */
    private String listPage(SessionStatus status) {
	status.setComplete();
	return RegistryURL.REDIRECT_LIST_SYSTEM_USER;
    }

    /**
     * Return to edit page.
     * 
     * @param id
     * @return
     */
    private String editPage(long id) {
	return String.format(RegistryURL.REDIRECT_EDIT_SYSTEM_USER, id);
    }

    /**
     * Delete a user account.
     * 
     * @param id
     * @param status
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_DELETE + "/{" + SystemUser.COLUMN_PRIMARY_KEY
	    + "}", method = RequestMethod.GET)
    public String delete(@PathVariable(SystemUser.COLUMN_PRIMARY_KEY) int id, SessionStatus status,
	    RedirectAttributes redirectAttrs) {
	String response = this.systemUserService.delete(id);
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, response);
	return listPage(status);
    }

    @RequestMapping(value = ConstantsSystem.REQUEST_EDIT + "/{" + SystemUser.COLUMN_PRIMARY_KEY
	    + "}", method = RequestMethod.GET)
    public String editSystemUser(@PathVariable(SystemUser.COLUMN_PRIMARY_KEY) int id, Model model,
	    HttpSession session) {

	session.removeAttribute(ProjectController.ATTR_FROM_PROJECT);

	// Only super admins can change company,
	// view list of all companies.
	if (this.authHelper.getAuth().isSuperAdmin()) {
	    model.addAttribute(ATTR_COMPANY_LIST, this.companyService.list());
	}

	// If id is zero,
	// return with an empty object.
	if (id == 0) {
	    model.addAttribute(ATTR_SYSTEM_USER, new SystemUser());
	    return RegistryJSPPath.JSP_EDIT_SYSTEM_USER;
	}

	SystemUser resultUser = this.systemUserService.getWithSecurityByID(id);
	// FIXME Why do this?
	resultUser
		.setCompanyID(resultUser.getCompany() == null ? null : resultUser.getCompany().getId());

	// Get the UserAux from database.
	UserAux userAux = this.systemUserService.getUserAux(resultUser);
	model.addAttribute(ATTR_USER_AUX, userAux);
	model.addAttribute(ATTR_MODULE_LIST, AuthorizedModule.values());
	model.addAttribute(ATTR_ACTION_LIST, AuthorizedAction.values());
	model.addAttribute(ATTR_SYSTEM_USER, resultUser);
	return RegistryJSPPath.JSP_EDIT_SYSTEM_USER;
    }
}