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
import com.cebedo.pmsys.domain.ConcreteMixingRatio;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.ConcreteMixingRatioService;

@Controller
@RequestMapping(RedisConstants.OBJECT_CONCRETE_MIXING_RATIO)
@SessionAttributes(value = { RedisConstants.OBJECT_CONCRETE_MIXING_RATIO }, types = { ConcreteMixingRatio.class })
public class ConcreteMixingRatioController {

    private static final String ATTR_CONCRETE_MIXING_RATIO_LIST = "concreteMixingRatioList";
    private static final String ATTR_CONCRETE_MIXING_RATIO = RedisConstants.OBJECT_CONCRETE_MIXING_RATIO;

    private AuthHelper authHelper = new AuthHelper();
    private ConcreteMixingRatioService concreteMixingRatioService;

    @Autowired(required = true)
    @Qualifier(value = "concreteMixingRatioService")
    public void setConcreteMixingRatioService(
	    ConcreteMixingRatioService concreteMixingRatioService) {
	this.concreteMixingRatioService = concreteMixingRatioService;
    }

    /**
     * Create a unit.
     * 
     * @param unit
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createRatio(
	    @ModelAttribute(ATTR_CONCRETE_MIXING_RATIO) ConcreteMixingRatio ratio,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service.
	// Get response.
	String response = this.concreteMixingRatioService.set(ratio);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Set completed.
	// Back to list page.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT
		+ RedisConstants.OBJECT_CONCRETE_MIXING_RATIO + "/"
		+ SystemConstants.REQUEST_LIST;
    }

    /**
     * Create/Edit an entry.
     * 
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/{"
	    + RedisConstants.OBJECT_CONCRETE_MIXING_RATIO + "}-end" }, method = RequestMethod.GET)
    public String editRatio(
	    @PathVariable(RedisConstants.OBJECT_CONCRETE_MIXING_RATIO) String key,
	    Model model) {

	// If we're creating.
	if (key.equals("0")) {
	    Company company = this.authHelper.getAuth().getCompany();
	    model.addAttribute(ATTR_CONCRETE_MIXING_RATIO,
		    new ConcreteMixingRatio(company));
	    return RedisConstants.JSP_CONCRETE_MIXING_RATIO_EDIT;
	}

	ConcreteMixingRatio ratio = this.concreteMixingRatioService.get(key);
	model.addAttribute(ATTR_CONCRETE_MIXING_RATIO, ratio);
	return RedisConstants.JSP_CONCRETE_MIXING_RATIO_EDIT;
    }

    /**
     * List all.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {

	List<ConcreteMixingRatio> conreteMixingRatioList = this.concreteMixingRatioService
		.list();

	model.addAttribute(ATTR_CONCRETE_MIXING_RATIO_LIST,
		conreteMixingRatioList);

	return RedisConstants.JSP_CONCRETE_MIXING_RATIO_LIST;
    }
}