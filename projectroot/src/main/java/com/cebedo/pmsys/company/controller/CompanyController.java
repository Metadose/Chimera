package com.cebedo.pmsys.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.company.service.CompanyService;

@Controller
@RequestMapping(Company.OBJECT_NAME)
public class CompanyController {

	public static final String ATTR_LIST = "companyList";
	public static final String ATTR_COMPANY = Company.OBJECT_NAME;
	public static final String JSP_LIST = "companyList";
	public static final String JSP_EDIT = "companyEdit";

	private CompanyService companyService;

	@Autowired(required = true)
	@Qualifier(value = "companyService")
	public void setCompanyService(CompanyService ps) {
		this.companyService = ps;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listCompanies(Model model) {
		model.addAttribute(ATTR_LIST, this.companyService.list());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_COMPANY) Company company) {
		if (company.getId() == 0) {
			this.companyService.create(company);
		} else {
			this.companyService.update(company);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_COMPANY + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping(SystemConstants.REQUEST_DELETE + "/{"
			+ Company.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(Company.COLUMN_PRIMARY_KEY) int id) {
		this.companyService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_COMPANY + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping(SystemConstants.REQUEST_EDIT + "/{"
			+ Company.COLUMN_PRIMARY_KEY + "}")
	public String editCompany(@PathVariable(Company.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		if (id == 0) {
			model.addAttribute(ATTR_COMPANY, new Company());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_COMPANY, this.companyService.getByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}

}