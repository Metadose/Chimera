package com.cebedo.pmsys.staff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.service.StaffService;

@Controller
@RequestMapping(Staff.OBJECT_NAME)
public class StaffController {

	public static final String ATTR_LIST = "staffList";

	public static final String REQUEST_ROOT = "/";
	public static final String REQUEST_LIST = "list";
	public static final String REQUEST_EDIT = "edit";
	public static final String REQUEST_CREATE = "create";

	public static final String JSP_LIST = "staffList";
	public static final String JSP_EDIT = "staffEdit";

	private StaffService staffService;

	@Autowired(required = true)
	@Qualifier(value = "staffService")
	public void setStaffService(StaffService ps) {
		this.staffService = ps;
	}

	@RequestMapping(value = { REQUEST_ROOT, REQUEST_LIST }, method = RequestMethod.GET)
	public String listStaff(Model model) {
		model.addAttribute(ATTR_LIST, this.staffService.list());
		return JSP_LIST;
	}

	@RequestMapping(value = REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(Staff.OBJECT_NAME) Staff staff) {
		if (staff.getId() == 0) {
			this.staffService.create(staff);
		} else {
			this.staffService.update(staff);
		}
		return "redirect:/" + Staff.OBJECT_NAME + "/" + REQUEST_LIST;
	}

	@RequestMapping("/delete/{" + Staff.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(Staff.COLUMN_PRIMARY_KEY) int id) {
		this.staffService.delete(id);
		return "redirect:/" + Staff.OBJECT_NAME + "/" + REQUEST_LIST;
	}

	@RequestMapping("/edit/{" + Staff.COLUMN_PRIMARY_KEY + "}")
	public String editStaff(@PathVariable(Staff.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		if (id == 0) {
			model.addAttribute(Staff.OBJECT_NAME, new Staff());
			return JSP_EDIT;
		}
		model.addAttribute(Staff.OBJECT_NAME, this.staffService.getByID(id));
		return JSP_EDIT;
	}
}