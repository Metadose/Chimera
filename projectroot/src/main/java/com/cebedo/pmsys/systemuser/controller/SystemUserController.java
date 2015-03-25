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

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.systemuser.model.SystemUser;
import com.cebedo.pmsys.systemuser.service.SystemUserService;

@Controller
@RequestMapping(SystemUser.OBJECT_NAME)
public class SystemUserController {

	public static final String ATTR_LIST = "systemUserList";
	public static final String ATTR_SYSTEM_USER = SystemUser.OBJECT_NAME;
	public static final String JSP_LIST = "systemUserList";
	public static final String JSP_EDIT = "systemUserEdit";

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
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			String encPassword = encoder.encodePassword(
					systemUser.getPassword(), systemUser.getUsername());
			systemUser.setPassword(encPassword);
			this.systemUserService.create(systemUser);
		} else {
			this.systemUserService.update(systemUser);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_SYSTEM_USER + "/"
				+ SystemConstants.REQUEST_LIST;
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
			+ SystemUser.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.POST)
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