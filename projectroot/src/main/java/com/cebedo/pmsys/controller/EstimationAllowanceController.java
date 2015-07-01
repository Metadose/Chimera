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
import com.cebedo.pmsys.domain.EstimationAllowance;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.EstimationAllowanceService;

@Controller
@RequestMapping(RedisConstants.OBJECT_ESTIMATION_ALLOWANCE)
@SessionAttributes(value = { RedisConstants.OBJECT_ESTIMATION_ALLOWANCE }, types = { EstimationAllowance.class })
public class EstimationAllowanceController {

    private static final String ATTR_ESTIMATION_ALLOWANCE_LIST = "estimationAllowanceList";
    private static final String ATTR_ESTIMATION_ALLOWANCE = "estimationAllowance";

    private AuthHelper authHelper = new AuthHelper();
    private EstimationAllowanceService estimationAllowanceService;

    @Autowired(required = true)
    @Qualifier(value = "estimationAllowanceService")
    public void setEstimationAllowanceService(
	    EstimationAllowanceService estimationAllowanceService) {
	this.estimationAllowanceService = estimationAllowanceService;
    }

    /**
     * Create a estimationAllowance.
     * 
     * @param estimationAllowance
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createEstimationAllowance(
	    @ModelAttribute(ATTR_ESTIMATION_ALLOWANCE) EstimationAllowance estimationAllowance,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service.
	// Get response.
	String response = this.estimationAllowanceService
		.set(estimationAllowance);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Set completed.
	// Back to list page.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT
		+ RedisConstants.OBJECT_ESTIMATION_ALLOWANCE + "/"
		+ SystemConstants.REQUEST_LIST;
    }

    /**
     * Create/Edit a estimationAllowance entry.
     * 
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/{"
	    + RedisConstants.OBJECT_ESTIMATION_ALLOWANCE + "}-end" }, method = RequestMethod.GET)
    public String editEstimationAllowance(
	    @PathVariable(RedisConstants.OBJECT_ESTIMATION_ALLOWANCE) String key,
	    Model model) {

	// If we're creating.
	if (key.equals("0")) {
	    Company company = this.authHelper.getAuth().getCompany();
	    model.addAttribute(ATTR_ESTIMATION_ALLOWANCE,
		    new EstimationAllowance(company));
	    return RedisConstants.JSP_ESTIMATION_ALLOWANCE_EDIT;
	}

	EstimationAllowance estimationAllowance = this.estimationAllowanceService
		.get(key);
	model.addAttribute(ATTR_ESTIMATION_ALLOWANCE, estimationAllowance);
	return RedisConstants.JSP_ESTIMATION_ALLOWANCE_EDIT;
    }

    /**
     * List estimationAllowances.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {

	List<EstimationAllowance> estimationAllowanceList = this.estimationAllowanceService
		.list();

	model.addAttribute(ATTR_ESTIMATION_ALLOWANCE_LIST,
		estimationAllowanceList);

	return RedisConstants.JSP_ESTIMATION_ALLOWANCE_LIST;
    }
}