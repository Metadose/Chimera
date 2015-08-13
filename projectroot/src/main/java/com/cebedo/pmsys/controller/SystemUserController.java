package com.cebedo.pmsys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.constants.ConstantsSystem;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.service.CompanyService;
import com.cebedo.pmsys.service.SystemUserService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Controller
@SessionAttributes(value = { SystemUserController.ATTR_SYSTEM_USER }, types = { SystemUser.class })
@RequestMapping(SystemUser.OBJECT_NAME)
public class SystemUserController {

    public static final String ATTR_LIST = "systemUserList";
    public static final String ATTR_SYSTEM_USER = SystemUser.OBJECT_NAME;
    public static final String ATTR_COMPANY_LIST = Company.OBJECT_NAME + "List";
    public static final String JSP_LIST = SystemUser.OBJECT_NAME + "/systemUserList";
    public static final String JSP_EDIT = SystemUser.OBJECT_NAME + "/systemUserEdit";
    public static final String JSP_CHANGE_PASSWORD = SystemUser.OBJECT_NAME + "/changePassword";

    public static final String PARAM_OLD_PASS = "password";
    public static final String PARAM_OLD_PASS_RETYPE = "password_retype";
    public static final String PARAM_NEW_PASS = "password_new";

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
    @RequestMapping(value = { ConstantsSystem.REQUEST_ROOT, ConstantsSystem.REQUEST_LIST }, method = RequestMethod.GET)
    public String listSystemUsers(Model model) {
	model.addAttribute(ATTR_LIST, this.systemUserService.list());
	return JSP_LIST;
    }

    @RequestMapping(value = ConstantsSystem.REQUEST_CREATE, method = RequestMethod.POST)
    public String create(@ModelAttribute(ATTR_SYSTEM_USER) SystemUser systemUser, SessionStatus status,
	    RedirectAttributes redirectAttrs) {

	AlertBoxGenerator alertFactory = new AlertBoxGenerator();

	// If the passwords provided were not equal.
	if (!systemUser.getPassword().equals(systemUser.getRetypePassword())) {
	    alertFactory.setStatus(ConstantsSystem.UI_STATUS_DANGER);
	    alertFactory.setMessage("The passwords you entered were not the same.");
	    redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());
	    status.setComplete();
	    return ConstantsSystem.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
		    + ConstantsSystem.REQUEST_EDIT + "/" + systemUser.getId();
	}

	// If request is to create new user.
	alertFactory.setStatus(ConstantsSystem.UI_STATUS_SUCCESS);
	if (systemUser.getId() == 0) {
	    try {
		@SuppressWarnings("unused")
		SystemUser user = this.systemUserService.searchDatabase(systemUser.getUsername());
		alertFactory.setStatus(ConstantsSystem.UI_STATUS_DANGER);
		alertFactory
			.setMessage("<b>Username</b> provided is <b>no longer available</b>. Please pick a different one.");
		redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT,
			alertFactory.generateHTML());
		status.setComplete();
		return ConstantsSystem.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
			+ ConstantsSystem.REQUEST_EDIT + "/" + systemUser.getId();
	    } catch (Exception e) {
		this.systemUserService.create(systemUser);

		// Redirect back to list page.
		alertFactory.setMessage("Successfully <b>created</b> user <b>"
			+ systemUser.getUsername() + "</b>.");
		redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT,
			alertFactory.generateHTML());
		status.setComplete();
		return ConstantsSystem.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
			+ ConstantsSystem.REQUEST_LIST;
	    }
	}

	// If request is to update user.
	this.systemUserService.update(systemUser);

	// Redirect back to the edit page.
	alertFactory.setMessage("Successfully <b>updated</b> user <b>" + systemUser.getUsername()
		+ "</b>.");
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());
	status.setComplete();
	return ConstantsSystem.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
		+ ConstantsSystem.REQUEST_EDIT + "/" + systemUser.getId();
    }

    @RequestMapping(value = { ConstantsSystem.REQUEST_CHANGE_PASSWORD })
    public String redirectChangePassword() {
	return JSP_CHANGE_PASSWORD;
    }

    @RequestMapping(value = ConstantsSystem.REQUEST_CHANGE_PASSWORD + "/" + ConstantsSystem.EXECUTE, method = RequestMethod.POST)
    public String changePassword(@RequestParam(PARAM_OLD_PASS) String passwordOld,
	    @RequestParam(PARAM_OLD_PASS_RETYPE) String passwordOldRetype,
	    @RequestParam(PARAM_NEW_PASS) String passwordNew,
	    @RequestParam(SystemUser.COLUMN_PRIMARY_KEY) long userID, Model model) {

	AlertBoxGenerator alertFactory = new AlertBoxGenerator();

	// Check if the passwords typed are equal.
	if (passwordOld.equals(passwordOldRetype)) {
	    SystemUser user = this.systemUserService.getByID(userID);

	    // If the password passed is valid.
	    if (this.authHelper.isPasswordValid(passwordOld, user)) {

		// TODO Move this inside the service class.
		String encPassword = this.authHelper.encodePassword(passwordNew, user);
		user.setPassword(encPassword);
		this.systemUserService.update(user);
		alertFactory.setStatus(ConstantsSystem.UI_STATUS_SUCCESS);
		alertFactory.setMessage("Successfully changed your password.");
	    } else {
		// Construct error alert. Password is not valid.
		alertFactory.setStatus(ConstantsSystem.UI_STATUS_DANGER);
		alertFactory.setMessage("Incorrect password. Please try again.");
	    }
	} else {
	    // Construct error alert. Old passwords are not equal.
	    alertFactory.setStatus(ConstantsSystem.UI_STATUS_DANGER);
	    alertFactory
		    .setMessage("The old passwords you entered were not the same. Please try again.");
	}
	model.addAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());
	// Redirect back to change pass page.
	return JSP_CHANGE_PASSWORD;
    }

    @RequestMapping(value = ConstantsSystem.REQUEST_DELETE + "/{" + SystemUser.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.POST)
    public String delete(@PathVariable(SystemUser.COLUMN_PRIMARY_KEY) int id) {
	this.systemUserService.delete(id);
	return ConstantsSystem.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
		+ ConstantsSystem.REQUEST_LIST;
    }

    @RequestMapping(value = ConstantsSystem.REQUEST_EDIT + "/{" + SystemUser.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.GET)
    public String editSystemUser(@PathVariable(SystemUser.COLUMN_PRIMARY_KEY) int id, Model model) {

	// Only super admins can change company,
	// view list of all companies.
	if (this.authHelper.getAuth().isSuperAdmin()) {
	    model.addAttribute(ATTR_COMPANY_LIST, this.companyService.list());
	}

	// If id is zero,
	// return with an empty object.
	if (id == 0) {
	    model.addAttribute(ATTR_SYSTEM_USER, new SystemUser());
	    return JSP_EDIT;
	}

	SystemUser resultUser = this.systemUserService.getWithSecurityByID(id);
	// FIXME Why do this?
	resultUser
		.setCompanyID(resultUser.getCompany() == null ? null : resultUser.getCompany().getId());

	model.addAttribute(ATTR_SYSTEM_USER, resultUser);
	return JSP_EDIT;
    }
}