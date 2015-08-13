package com.cebedo.pmsys.controller;

import org.apache.log4j.Logger;
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
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.CompanyService;

@Controller
@SessionAttributes(value = { CompanyController.ATTR_COMPANY }, types = { Company.class })
@RequestMapping(Company.OBJECT_NAME)
public class CompanyController {

    private static Logger logger = Logger.getLogger(Company.OBJECT_NAME);
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

    @RequestMapping(value = { ConstantsSystem.REQUEST_ROOT,
	    ConstantsSystem.REQUEST_LIST }, method = RequestMethod.GET)
    public String listCompanies(Model model) {
	logger.info("Listing all companies.");
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
	// TODO This has a bug.
	// Fix your client-side JSP forms to handle dates.
	String uiAlert = "";
	if (company.getId() == 0) {
	    logger.info("Creating company: " + company.toString());
	    uiAlert = this.companyService.create(company);
	    redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT,
		    uiAlert);
	    status.setComplete();
	    return ConstantsSystem.CONTROLLER_REDIRECT + ATTR_COMPANY + "/"
		    + ConstantsSystem.REQUEST_LIST;
	}
	logger.info("Updating company: " + company.toString());
	uiAlert = this.companyService.update(company);
	redirectAttrs
		.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, uiAlert);
	status.setComplete();
	return ConstantsSystem.CONTROLLER_REDIRECT + ATTR_COMPANY + "/"
		+ ConstantsSystem.REQUEST_EDIT + "/" + company.getId();
    }

    /**
     * Delete a company.
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = { ConstantsSystem.REQUEST_DELETE + "/{"
	    + Company.COLUMN_PRIMARY_KEY + "}" }, method = RequestMethod.GET)
    public String delete(@PathVariable(Company.COLUMN_PRIMARY_KEY) int id,
	    RedirectAttributes redirectAttrs, SessionStatus status) {
	logger.info("Deleting company: " + id);
	String uiAlert = this.companyService.delete(id);
	redirectAttrs
		.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, uiAlert);
	status.setComplete();
	return ConstantsSystem.CONTROLLER_REDIRECT + ATTR_COMPANY + "/"
		+ ConstantsSystem.REQUEST_LIST;
    }

    /**
     * Opening an edit page or create page for a company.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = { ConstantsSystem.REQUEST_EDIT + "/{"
	    + Company.COLUMN_PRIMARY_KEY + "}" })
    public String editCompany(@PathVariable(Company.COLUMN_PRIMARY_KEY) int id,
	    Model model) {
	if (id == 0) {
	    logger.info("Opening new company: " + id);
	    model.addAttribute(ATTR_COMPANY, new Company());
	    return JSP_EDIT;
	}
	logger.info("Editing company: " + id);
	model.addAttribute(ATTR_COMPANY, this.companyService.getByID(id));
	return JSP_EDIT;
    }

}