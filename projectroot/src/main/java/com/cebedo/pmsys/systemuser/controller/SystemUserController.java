package com.cebedo.pmsys.systemuser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cebedo.pmsys.common.AuthHelper;
import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.common.ui.AlertBoxFactory;
import com.cebedo.pmsys.systemuser.model.SystemUser;
import com.cebedo.pmsys.systemuser.service.SystemUserService;

@Controller
@RequestMapping(SystemUser.OBJECT_NAME)
public class SystemUserController {

	public static final String ATTR_LIST = "systemUserList";
	public static final String ATTR_SYSTEM_USER = SystemUser.OBJECT_NAME;
	public static final String JSP_LIST = "systemUserList";
	public static final String JSP_EDIT = "systemUserEdit";
	public static final String JSP_CHANGE_PASSWORD = "changePassword";

	public static final String PARAM_OLD_PASS = "password";
	public static final String PARAM_OLD_PASS_RETYPE = "password_retype";
	public static final String PARAM_NEW_PASS = "password_new";

	private Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
	private AuthHelper authHelper = new AuthHelper();
	private SystemUserService systemUserService;

	@Autowired(required = true)
	@Qualifier(value = "systemUserService")
	public void setSystemUserService(SystemUserService ps) {
		this.systemUserService = ps;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listSystemUsers(Model model) {
		model.addAttribute(ATTR_LIST, this.systemUserService.list());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_SYSTEMUSER_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_SYSTEM_USER) SystemUser systemUser) {
		if (systemUser.getId() == 0) {
			String encPassword = this.authHelper.encodePassword(
					systemUser.getPassword(), systemUser);
			systemUser.setPassword(encPassword);
			this.systemUserService.create(systemUser);
		} else {
			this.systemUserService.update(systemUser);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_CHANGE_PASSWORD })
	public String redirectChangePassword() {
		return JSP_CHANGE_PASSWORD;
	}

	@RequestMapping(value = SystemConstants.REQUEST_CHANGE_PASSWORD + "/"
			+ SystemConstants.EXECUTE, method = RequestMethod.POST)
	public String changePassword(
			@RequestParam(PARAM_OLD_PASS) String passwordOld,
			@RequestParam(PARAM_OLD_PASS_RETYPE) String passwordOldRetype,
			@RequestParam(PARAM_NEW_PASS) String passwordNew,
			@RequestParam(SystemUser.COLUMN_PRIMARY_KEY) long userID,
			Model model) {

		AlertBoxFactory alertFactory = new AlertBoxFactory();

		// Check if the passwords typed are equal.
		if (passwordOld.equals(passwordOldRetype)) {
			SystemUser user = this.systemUserService.getByID(userID);

			// If the password passed is valid.
			if (this.authHelper.isPasswordValid(passwordOld, user)) {

				String encPassword = this.authHelper.encodePassword(
						passwordNew, user);
				user.setPassword(encPassword);
				this.systemUserService.update(user);
				alertFactory.setStatus(SystemConstants.UI_STATUS_SUCCESS);
				alertFactory.setMessage("Successfully changed your password.");
			} else {
				// Construct error alert. Password is not valid.
				alertFactory.setStatus(SystemConstants.UI_STATUS_DANGER);
				alertFactory
						.setMessage("Incorrect password. Please try again.");
			}
		} else {
			// Construct error alert. Old passwords are not equal.
			alertFactory.setStatus(SystemConstants.UI_STATUS_DANGER);
			alertFactory
					.setMessage("The old passwords you entered were not the same. Please try again.");
		}
		model.addAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		// Redirect back to change pass page.
		return JSP_CHANGE_PASSWORD;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_SYSTEMUSER_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE + "/{"
			+ SystemUser.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.POST)
	public String delete(@PathVariable(SystemUser.COLUMN_PRIMARY_KEY) int id) {
		this.systemUserService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_SYSTEMUSER_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
			+ SystemUser.COLUMN_PRIMARY_KEY + "}")
	public String editSystemUser(
			@PathVariable(SystemUser.COLUMN_PRIMARY_KEY) int id, Model model) {
		if (id == 0) {
			model.addAttribute(ATTR_SYSTEM_USER, new SystemUser());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_SYSTEM_USER, this.systemUserService.getByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}

}