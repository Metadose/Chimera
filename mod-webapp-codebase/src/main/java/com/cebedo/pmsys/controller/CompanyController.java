package com.cebedo.pmsys.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.constants.ConstantsSystem;
import com.cebedo.pmsys.constants.RegistryJSPPath;
import com.cebedo.pmsys.constants.RegistryURL;
import com.cebedo.pmsys.enums.Theme;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.CompanyService;

@Controller
@SessionAttributes(

value = { CompanyController.ATTR_COMPANY }

)
@RequestMapping(Company.OBJECT_NAME)
public class CompanyController {

    public static final String ATTR_LIST = "companyList";
    public static final String ATTR_LOGS = "logs";
    public static final String ATTR_THEMES = "themes";
    public static final String ATTR_COMPANY = Company.OBJECT_NAME;

    private CompanyService companyService;

    @Autowired(required = true)
    @Qualifier(value = "companyService")
    public void setCompanyService(CompanyService ps) {
	this.companyService = ps;
    }

    @RequestMapping(value = { ConstantsSystem.REQUEST_ROOT,
	    ConstantsSystem.REQUEST_LIST }, method = RequestMethod.GET)
    public String listCompanies(Model model, HttpSession session) {
	model.addAttribute(ATTR_LIST, this.companyService.list());
	session.removeAttribute(ProjectController.ATTR_FROM_PROJECT);
	return RegistryJSPPath.JSP_LIST_COMPANY;
    }

    /**
     * Create or update a company.
     * 
     * @param company
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_CREATE, method = RequestMethod.POST)
    public String create(@ModelAttribute(ATTR_COMPANY) Company company, RedirectAttributes redirectAttrs,
	    SessionStatus status, BindingResult result) {
	String response = "";
	if (company.getId() == 0) {
	    response = this.companyService.create(company, result);
	} else {
	    response = this.companyService.update(company, result);
	}
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, response);

	AuthHelper authHelper = new AuthHelper();
	if (authHelper.isSuperAdmin()) {
	    return editPage(company.getId(), status);
	}
	return RegistryURL.REDIRECT_LOGOUT_COMPANY_UPDATE;
    }

    /**
     * Return to the edit page.
     * 
     * @param id
     * @param status
     * @return
     */
    private String editPage(long id, SessionStatus status) {
	status.setComplete();
	return String.format(RegistryURL.REDIRECT_EDIT_COMPANY, id);
    }

    /**
     * Delete a company.
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = { ConstantsSystem.REQUEST_DELETE + "/{" + Company.COLUMN_PRIMARY_KEY
	    + "}" }, method = RequestMethod.GET)
    public String delete(@PathVariable(Company.COLUMN_PRIMARY_KEY) int id,
	    RedirectAttributes redirectAttrs, SessionStatus status) {
	String response = this.companyService.delete(id);
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, response);
	return listPage(status);
    }

    /**
     * Return to the list page.
     * 
     * @param status
     * 
     * @return
     */
    private String listPage(SessionStatus status) {
	status.setComplete();
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
	model.addAttribute(ATTR_THEMES, Theme.values());
	if (id == 0) {
	    model.addAttribute(ATTR_COMPANY, new Company());
	    return RegistryJSPPath.JSP_EDIT_COMPANY;
	}
	model.addAttribute(ATTR_COMPANY, this.companyService.getByID(id));
	return RegistryJSPPath.JSP_EDIT_COMPANY;
    }

    @RequestMapping(value = RegistryURL.SETTINGS)
    public String settings(Model model) {
	model.addAttribute(ATTR_THEMES, Theme.values());
	model.addAttribute(ATTR_COMPANY, this.companyService.settings());
	return RegistryJSPPath.JSP_EDIT_COMPANY;
    }

    /**
     * View all logs in this project.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = RegistryURL.LOGS)
    public String logs(Model model, HttpSession session) {
	session.removeAttribute(ProjectController.ATTR_FROM_PROJECT);
	List<AuditLog> logs = this.companyService.logs();
	model.addAttribute(ATTR_LOGS, logs);
	return RegistryJSPPath.JSP_LOGS_COMPANY;
    }

}