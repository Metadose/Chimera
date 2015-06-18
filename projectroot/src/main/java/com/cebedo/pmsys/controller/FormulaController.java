package com.cebedo.pmsys.controller;

import java.util.List;

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

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.domain.Formula;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.service.FormulaService;

@Controller
@RequestMapping(RedisConstants.OBJECT_FORMULA)
@SessionAttributes(value = { RedisConstants.OBJECT_FORMULA }, types = { Formula.class })
public class FormulaController {

    private static final String ATTR_LIST = "formulaList";
    private static final String JSP_LIST = RedisConstants.OBJECT_FORMULA
	    + "/formulaList";
    private static final String JSP_EDIT = RedisConstants.OBJECT_FORMULA
	    + "/formulaEdit";

    private AuthHelper authHelper = new AuthHelper();
    private FormulaService formulaService;

    @Autowired(required = true)
    @Qualifier(value = "formulaService")
    public void setFormulaService(FormulaService formulaService) {
	this.formulaService = formulaService;
    }

    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {
	List<Formula> formulaList = this.formulaService.list();
	model.addAttribute(ATTR_LIST, formulaList);
	return JSP_LIST;
    }

    /**
     * Test a formula.
     * 
     * @param formula
     * @param status
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_TEST }, method = RequestMethod.POST)
    public String testFormula(
	    @ModelAttribute(RedisConstants.OBJECT_FORMULA) Formula formula,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service and get response.
	String response = this.formulaService.test(formula);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_FORMULA_TEST,
		response);

	// Clear session.
	status.setComplete();

	return SystemConstants.CONTROLLER_REDIRECT
		+ RedisConstants.OBJECT_FORMULA + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + formula.getKey()
		+ "-end";
    }

    /**
     * Create or update a formula.
     * 
     * @param formula
     * @param status
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createFormula(
	    @ModelAttribute(RedisConstants.OBJECT_FORMULA) Formula formula,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service and get response.
	String response = this.formulaService.set(formula);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Clear session.
	status.setComplete();

	return SystemConstants.CONTROLLER_REDIRECT
		+ RedisConstants.OBJECT_FORMULA + "/"
		+ SystemConstants.REQUEST_LIST;
    }

    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/{"
	    + RedisConstants.UUID + "}-end" }, method = RequestMethod.GET)
    public String editFormula(Model model,
	    @PathVariable(RedisConstants.UUID) String key) {

	if (key.equals("0")) {
	    model.addAttribute(RedisConstants.OBJECT_FORMULA, new Formula(
		    this.authHelper.getAuth().getCompany()));
	    return JSP_EDIT;
	}

	Formula formula = this.formulaService.get(key);
	model.addAttribute(RedisConstants.OBJECT_FORMULA, formula);
	return JSP_EDIT;
    }

}