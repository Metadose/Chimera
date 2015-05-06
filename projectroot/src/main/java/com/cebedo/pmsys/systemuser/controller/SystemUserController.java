package com.cebedo.pmsys.systemuser.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.company.service.CompanyService;
import com.cebedo.pmsys.security.securityaccess.model.SecurityAccess;
import com.cebedo.pmsys.security.securityrole.model.SecurityRole;
import com.cebedo.pmsys.system.bean.UserSecAccessBean;
import com.cebedo.pmsys.system.bean.UserSecRoleBean;
import com.cebedo.pmsys.system.constants.SystemConstants;
import com.cebedo.pmsys.system.helper.AuthHelper;
import com.cebedo.pmsys.system.ui.AlertBoxFactory;
import com.cebedo.pmsys.systemuser.model.SystemUser;
import com.cebedo.pmsys.systemuser.service.SystemUserService;

@Controller
@SessionAttributes(value = { SystemUserController.ATTR_SYSTEM_USER,
		SystemUserController.ATTR_SEC_ACCESS,
		SystemUserController.ATTR_SEC_ROLE }, types = { SystemUser.class,
		UserSecAccessBean.class, UserSecRoleBean.class })
@RequestMapping(SystemUser.OBJECT_NAME)
public class SystemUserController {

	public static final String ATTR_LIST = "systemUserList";
	public static final String ATTR_SEC_ACCESS = SecurityAccess.OBJECT_NAME;
	public static final String ATTR_SEC_ROLE = SecurityRole.OBJECT_NAME;
	public static final String ATTR_SYSTEM_USER = SystemUser.OBJECT_NAME;
	public static final String ATTR_COMPANY_LIST = Company.OBJECT_NAME + "List";
	public static final String JSP_LIST = "systemUserList";
	public static final String JSP_EDIT = "systemUserEdit";
	public static final String JSP_CHANGE_PASSWORD = "changePassword";

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
	 * Assign a security access to a user.
	 * 
	 * @param secAccBean
	 * @param session
	 * @param status
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_SYSTEMUSER_EDITOR + "')")
	@RequestMapping(value = { SystemConstants.REQUEST_ASSIGN + "/"
			+ SecurityAccess.OBJECT_NAME }, method = RequestMethod.POST)
	public String assignSecurityAccess(
			@ModelAttribute(ATTR_SEC_ACCESS) UserSecAccessBean secAccBean,
			HttpSession session, SessionStatus status, Model model) {

		// Get the user.
		SystemUser user = (SystemUser) session.getAttribute(ATTR_SYSTEM_USER);
		this.systemUserService.assignSecurityAccess(user, secAccBean);

		// FIXME Fix this notification.
		model.addAttribute(SystemConstants.UI_PARAM_ALERT,
				AlertBoxFactory.SUCCESS.generateCreate(
						SecurityAccess.OBJECT_NAME,
						"ASSIGN " + secAccBean.toString()));
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + user.getId();
	}

	/**
	 * Assign a security role to a user.
	 * 
	 * @param secAccBean
	 * @param session
	 * @param status
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_SYSTEMUSER_EDITOR + "')")
	@RequestMapping(value = { SystemConstants.REQUEST_ASSIGN + "/"
			+ SecurityRole.OBJECT_NAME }, method = RequestMethod.POST)
	public String assignSecurityRole(
			@ModelAttribute(ATTR_SEC_ROLE) UserSecRoleBean secRoleBean,
			HttpSession session, SessionStatus status, Model model) {

		// Get the user.
		SystemUser user = (SystemUser) session.getAttribute(ATTR_SYSTEM_USER);
		this.systemUserService.assignSecurityRole(user, secRoleBean);

		// FIXME Fix this notification.
		model.addAttribute(SystemConstants.UI_PARAM_ALERT,
				AlertBoxFactory.SUCCESS.generateCreate(
						SecurityAccess.OBJECT_NAME,
						"ASSIGN " + secRoleBean.toString()));
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + user.getId();
	}

	/**
	 * Unassign a security access from a user.
	 * 
	 * @param secAccID
	 * @param session
	 * @param status
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_SYSTEMUSER_EDITOR + "')")
	@RequestMapping(value = { SystemConstants.REQUEST_UNASSIGN + "/"
			+ SecurityAccess.OBJECT_NAME + "/{" + SecurityAccess.OBJECT_NAME
			+ "}" }, method = RequestMethod.GET)
	public String unassignSecurityAccess(
			@PathVariable(SecurityAccess.OBJECT_NAME) long secAccID,
			HttpSession session, SessionStatus status, Model model) {

		// Get the user.
		// And unassign the access from the user.
		SystemUser user = (SystemUser) session.getAttribute(ATTR_SYSTEM_USER);
		this.systemUserService.unassignSecurityAccess(user, secAccID);

		// FIXME Fix this notification.
		model.addAttribute(SystemConstants.UI_PARAM_ALERT,
				AlertBoxFactory.SUCCESS.generateCreate(
						SecurityAccess.OBJECT_NAME, "UNASSIGN " + secAccID));
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + user.getId();
	}

	/**
	 * Unassign a security role from a user.
	 * 
	 * @param secRoleID
	 * @param session
	 * @param status
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_SYSTEMUSER_EDITOR + "')")
	@RequestMapping(value = { SystemConstants.REQUEST_UNASSIGN + "/"
			+ SecurityRole.OBJECT_NAME + "/{" + SecurityRole.OBJECT_NAME + "}" }, method = RequestMethod.GET)
	public String unassignSecurityRole(
			@PathVariable(SecurityRole.OBJECT_NAME) long secRoleID,
			HttpSession session, SessionStatus status, Model model) {

		// Get the user.
		// And unassign the role from the user.
		SystemUser user = (SystemUser) session.getAttribute(ATTR_SYSTEM_USER);
		this.systemUserService.unassignSecurityRole(user, secRoleID);

		// FIXME Fix this notification.
		model.addAttribute(SystemConstants.UI_PARAM_ALERT,
				AlertBoxFactory.SUCCESS
						.generateCreate(SecurityAccess.OBJECT_NAME,
								"UNASSIGN role" + secRoleID));
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + user.getId();
	}

	/**
	 * Unassign all security access assigned to the user.
	 * 
	 * @param session
	 * @param status
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_SYSTEMUSER_EDITOR + "')")
	@RequestMapping(value = { SystemConstants.REQUEST_UNASSIGN + "/"
			+ SecurityAccess.OBJECT_NAME + "/" + SystemConstants.ALL }, method = RequestMethod.GET)
	public String unassignAllSecurityAccess(HttpSession session,
			SessionStatus status, Model model) {

		// Get the user.
		// Unassign all the access from the user.
		SystemUser user = (SystemUser) session.getAttribute(ATTR_SYSTEM_USER);
		this.systemUserService.unassignAllSecurityAccess(user);

		// FIXME Fix this notification.
		model.addAttribute(SystemConstants.UI_PARAM_ALERT,
				AlertBoxFactory.SUCCESS.generateCreate(
						SecurityAccess.OBJECT_NAME, "UNASSIGN ALL"));
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + user.getId();
	}

	/**
	 * Unassign all security roles assigned to the user.
	 * 
	 * @param session
	 * @param status
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_SYSTEMUSER_EDITOR + "')")
	@RequestMapping(value = { SystemConstants.REQUEST_UNASSIGN + "/"
			+ SecurityRole.OBJECT_NAME + "/" + SystemConstants.ALL }, method = RequestMethod.GET)
	public String unassignAllSecurityRoles(HttpSession session,
			SessionStatus status, Model model) {

		// Get the user.
		// Unassign all the access from the user.
		SystemUser user = (SystemUser) session.getAttribute(ATTR_SYSTEM_USER);
		this.systemUserService.unassignAllSecurityRoles(user);

		// FIXME Fix this notification.
		model.addAttribute(SystemConstants.UI_PARAM_ALERT,
				AlertBoxFactory.SUCCESS.generateCreate(
						SecurityAccess.OBJECT_NAME, "UNASSIGN ALL"));
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + user.getId();
	}

	/**
	 * List all system users.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listSystemUsers(Model model) {
		model.addAttribute(ATTR_LIST, this.systemUserService.list());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	@PreAuthorize("hasRole('" + SecurityRole.ROLE_SYSTEMUSER_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(
			@ModelAttribute(ATTR_SYSTEM_USER) SystemUser systemUser,
			SessionStatus status, RedirectAttributes redirectAttrs) {

		AlertBoxFactory alertFactory = new AlertBoxFactory();

		// If the passwords provided were not equal.
		if (!systemUser.getPassword().equals(systemUser.getRetypePassword())) {
			alertFactory.setStatus(SystemConstants.UI_STATUS_DANGER);
			alertFactory
					.setMessage("The passwords you entered were not the same.");
			redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
					alertFactory.generateHTML());
			status.setComplete();
			return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
					+ SystemConstants.REQUEST_EDIT + "/" + systemUser.getId();
		}

		// If request is to create new user.
		alertFactory.setStatus(SystemConstants.UI_STATUS_SUCCESS);
		if (systemUser.getId() == 0) {
			try {
				@SuppressWarnings("unused")
				SystemUser user = this.systemUserService
						.searchDatabase(systemUser.getUsername());
				alertFactory.setStatus(SystemConstants.UI_STATUS_DANGER);
				alertFactory
						.setMessage("<b>Username</b> provided is <b>no longer available</b>. Please pick a different one.");
				redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
						alertFactory.generateHTML());
				status.setComplete();
				return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER
						+ "/" + SystemConstants.REQUEST_EDIT + "/"
						+ systemUser.getId();
			} catch (Exception e) {
				this.systemUserService.create(systemUser);

				// Redirect back to list page.
				alertFactory.setMessage("Successfully <b>created</b> user <b>"
						+ systemUser.getUsername() + "</b>.");
				redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
						alertFactory.generateHTML());
				status.setComplete();
				return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER
						+ "/" + SystemConstants.REQUEST_LIST;
			}
		}

		// If request is to update user.
		this.systemUserService.update(systemUser);

		// Redirect back to the edit page.
		alertFactory.setMessage("Successfully <b>updated</b> user <b>"
				+ systemUser.getUsername() + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + systemUser.getId();
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

				// TODO Move this inside the service class.
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

	@PreAuthorize("hasRole('" + SecurityRole.ROLE_SYSTEMUSER_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE + "/{"
			+ SystemUser.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.POST)
	public String delete(@PathVariable(SystemUser.COLUMN_PRIMARY_KEY) int id) {
		this.systemUserService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@PreAuthorize("hasRole('" + SecurityRole.ROLE_SYSTEMUSER_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
			+ SystemUser.COLUMN_PRIMARY_KEY + "}")
	public String editSystemUser(
			@PathVariable(SystemUser.COLUMN_PRIMARY_KEY) int id, Model model) {

		model.addAttribute(ATTR_SEC_ACCESS, new UserSecAccessBean(id));
		model.addAttribute(ATTR_SEC_ROLE, new UserSecRoleBean(id));

		// Only super admins can change company,
		// view list of all companies.
		if (this.authHelper.getAuth().isSuperAdmin()) {
			model.addAttribute(ATTR_COMPANY_LIST, this.companyService.list());
		}

		// If id is zero,
		// return with an empty object.
		if (id == 0) {
			model.addAttribute(ATTR_SYSTEM_USER, new SystemUser());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}

		SystemUser resultUser = this.systemUserService.getByID(id);
		// FIXME Why do this?
		resultUser.setCompanyID(resultUser.getCompany() == null ? null
				: resultUser.getCompany().getId());
		model.addAttribute(ATTR_SYSTEM_USER, resultUser);
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}

}