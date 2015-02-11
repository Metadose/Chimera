package com.cebedo.pmsys.staff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.service.StaffService;

@Controller
@RequestMapping(Staff.OBJECT_NAME)
public class StaffController {

	public static final String ATTR_LIST = "staffList";
	public static final String ATTR_STAFF = Staff.OBJECT_NAME;
	public static final String JSP_LIST = "staffList";
	public static final String JSP_EDIT = "staffEdit";

	private StaffService staffService;

	@Autowired(required = true)
	@Qualifier(value = "staffService")
	public void setStaffService(StaffService ps) {
		this.staffService = ps;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listStaffs(Model model) {
		model.addAttribute(ATTR_LIST, this.staffService.list());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_STAFF) Staff staff) {
		if (staff.getId() == 0) {
			this.staffService.create(staff);
		} else {
			this.staffService.update(staff);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_STAFF + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_DELETE + "/{"
			+ Staff.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(Staff.COLUMN_PRIMARY_KEY) int id) {
		this.staffService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_STAFF + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_EDIT + "/{"
			+ Staff.COLUMN_PRIMARY_KEY + "}")
	public String editStaff(@PathVariable(Staff.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		if (id == 0) {
			model.addAttribute(ATTR_STAFF, new Staff());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_STAFF, this.staffService.getByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}
}