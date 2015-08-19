package com.cebedo.pmsys.controller;

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
import com.cebedo.pmsys.constants.RegistryURL;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.CompanyService;

@Controller
@SessionAttributes(value = { CompanyController.ATTR_COMPANY }, types = { Company.class })
@RequestMapping(Company.OBJECT_NAME)
public class CompanyController {

    public static final String ATTR_LIST = "companyList";
    public static final String ATTR_COMPANY = Company.OBJECT_NAME;
    public static final String JSP_LIST = Company.OBJECT_NAME + "/companyList";
    public static final String JSP_EDIT = Company.OBJECT_NAME + "/companyEdit";

    private CompanyService companyService;

    @Autowired(required = true)
    @Qualifier(value = "companyService")
    public void setCompanyService(CompanyService ps) {
	this.companyService = ps;
    }

    @RequestMapping(value = { ConstantsSystem.REQUEST_ROOT, ConstantsSystem.REQUEST_LIST }, method = RequestMethod.GET)
    public String listCompanies(Model model) {
	model.addAttribute(ATTR_LIST, this.companyService.list());
	return JSP_LIST;
    }

    /**
     * Create or update a company.
     * 
     * @param company
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_CREATE, method = RequestMethod.POST)
    public String create(@ModelAttribute(ATTR_COMPANY) Company company,
	    RedirectAttributes redirectAttrs, SessionStatus status) {

	String response = "";

	if (company.getId() == 0) {
	    response = this.companyService.create(company);
	} else {
	    response = this.companyService.update(company);
	}
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, response);
	status.setComplete();
	return editPage(company.getId());
    }

    /**
     * Return to the edit page.
     * 
     * @param id
     * @return
     */
    private String editPage(long id) {
	return String.format(RegistryURL.REDIRECT_EDIT_COMPANY, id);
    }

    /**
     * Delete a company.
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = { ConstantsSystem.REQUEST_DELETE + "/{" + Company.COLUMN_PRIMARY_KEY + "}" }, method = RequestMethod.GET)
    public String delete(@PathVariable(Company.COLUMN_PRIMARY_KEY) int id,
	    RedirectAttributes redirectAttrs, SessionStatus status) {
	String response = this.companyService.delete(id);
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, response);
	status.setComplete();
	return listPage();
    }

    /**
     * Return to the list page.
     * 
     * @return
     */
    private String listPage() {
	return RegistryURL.REDIRECT_LIST_COMPANY;
    }

    /**
     * Opening an edit page or create page for a company.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = { ConstantsSystem.REQUEST_EDIT + "/{" + Company.COLUMN_PRIMARY_KEY + "}" })
    public String editCompany(@PathVariable(Company.COLUMN_PRIMARY_KEY) int id, Model model) {
	if (id == 0) {
	    model.addAttribute(ATTR_COMPANY, new Company());
	    return JSP_EDIT;
	}
	model.addAttribute(ATTR_COMPANY, this.companyService.getByID(id));
	return JSP_EDIT;
    }

}