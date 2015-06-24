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
import com.cebedo.pmsys.domain.ConcreteProportion;
import com.cebedo.pmsys.domain.Unit;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.ConcreteProportionService;
import com.cebedo.pmsys.service.UnitService;

@Controller
@RequestMapping(RedisConstants.OBJECT_CONCRETE_PROPORTION)
@SessionAttributes(value = { RedisConstants.OBJECT_CONCRETE_PROPORTION }, types = { ConcreteProportion.class })
public class ConcreteProportionController {

    private static final String ATTR_UNIT_LIST = "unitList";
    private static final String ATTR_CONCRETE_PROPORTION_LIST = "concreteProportionList";
    private static final String ATTR_CONCRETE_PROPORTION = RedisConstants.OBJECT_CONCRETE_PROPORTION;

    private AuthHelper authHelper = new AuthHelper();
    private ConcreteProportionService concreteProportionService;
    private UnitService unitService;

    @Autowired(required = true)
    @Qualifier(value = "unitService")
    public void setUnitService(UnitService unitService) {
	this.unitService = unitService;
    }

    @Autowired(required = true)
    @Qualifier(value = "concreteProportionService")
    public void setConcreteProportionService(
	    ConcreteProportionService concreteProportionService) {
	this.concreteProportionService = concreteProportionService;
    }

    /**
     * Create a unit.
     * 
     * @param unit
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createProportion(
	    @ModelAttribute(ATTR_CONCRETE_PROPORTION) ConcreteProportion ratio,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service.
	// Get response.
	String response = this.concreteProportionService.set(ratio);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Set completed.
	// Back to list page.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT
		+ RedisConstants.OBJECT_CONCRETE_PROPORTION + "/"
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
	    + RedisConstants.OBJECT_CONCRETE_PROPORTION + "}-end" }, method = RequestMethod.GET)
    public String editProportion(
	    @PathVariable(RedisConstants.OBJECT_CONCRETE_PROPORTION) String key,
	    Model model) {

	// Get list of units.
	List<Unit> unitList = this.unitService.list();
	model.addAttribute(ATTR_UNIT_LIST, unitList);

	// If we're creating.
	if (key.equals("0")) {
	    Company company = this.authHelper.getAuth().getCompany();
	    model.addAttribute(ATTR_CONCRETE_PROPORTION,
		    new ConcreteProportion(company));
	    return RedisConstants.JSP_CONCRETE_PROPORTION_EDIT;
	}

	ConcreteProportion ratio = this.concreteProportionService.get(key);
	model.addAttribute(ATTR_CONCRETE_PROPORTION, ratio);
	return RedisConstants.JSP_CONCRETE_PROPORTION_EDIT;
    }

    /**
     * List all.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {

	List<ConcreteProportion> conreteMixingRatioList = this.concreteProportionService
		.list();

	model.addAttribute(ATTR_CONCRETE_PROPORTION_LIST,
		conreteMixingRatioList);

	return RedisConstants.JSP_CONCRETE_PROPORTION_LIST;
    }
}