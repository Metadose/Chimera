package com.cebedo.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	/**
	 * Get the form.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getHome() {
		return "home1";
	}

	/**
	 * Get the form.
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public String home(Model model) {

		Staff staff1 = new Staff(1, "Project Manager");
		Staff staff2 = new Staff(2, "Project Leader");
		Staff staff3 = new Staff(3, "Technical Staff");

		List<Staff> optionList = new ArrayList<Staff>();
		optionList.add(staff1);
		optionList.add(staff2);
		optionList.add(staff3);

		model.addAttribute("optionList", optionList);
		model.addAttribute("myBean", new StaffAssignmentBean());

		return "home";
	}

	/**
	 * Form request is received after clicking button.
	 */
	@RequestMapping(value = "/sendForm", method = RequestMethod.POST)
	public String sendForm(@ModelAttribute("myBean") StaffAssignmentBean saBean) {
		System.out.println(saBean.getStaffID() + " " + saBean.getPosition());
		return "/";
	}

}
