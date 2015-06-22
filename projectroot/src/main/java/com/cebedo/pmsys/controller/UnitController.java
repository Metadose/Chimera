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
import com.cebedo.pmsys.domain.Unit;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.UnitService;

@Controller
@RequestMapping(RedisConstants.OBJECT_UNIT)
@SessionAttributes(value = { RedisConstants.OBJECT_UNIT }, types = { Unit.class })
public class UnitController {

    private static final String ATTR_UNIT_LIST = "unitList";
    private static final String ATTR_UNIT = "unit";

    private AuthHelper authHelper = new AuthHelper();
    private UnitService unitService;

    @Autowired(required = true)
    @Qualifier(value = "unitService")
    public void setUnitService(UnitService unitService) {
	this.unitService = unitService;
    }

    /**
     * Create a unit.
     * 
     * @param unit
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createUnit(@ModelAttribute(ATTR_UNIT) Unit unit,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service.
	// Get response.
	String response = this.unitService.set(unit);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Set completed.
	// Back to list page.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + RedisConstants.OBJECT_UNIT
		+ "/" + SystemConstants.REQUEST_LIST;
    }

    /**
     * Create/Edit a unit entry.
     * 
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/{"
	    + RedisConstants.OBJECT_UNIT + "}-end" }, method = RequestMethod.GET)
    public String editUnit(
	    @PathVariable(RedisConstants.OBJECT_UNIT) String key, Model model) {

	// If we're creating.
	if (key.equals("0")) {
	    Company company = this.authHelper.getAuth().getCompany();
	    model.addAttribute(ATTR_UNIT, new Unit(company));
	    return RedisConstants.JSP_UNIT_EDIT;
	}

	Unit unit = this.unitService.get(key);
	model.addAttribute(ATTR_UNIT, unit);
	return RedisConstants.JSP_UNIT_EDIT;
    }

    /**
     * List units.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {

	List<Unit> unitList = this.unitService.list();

	model.addAttribute(ATTR_UNIT_LIST, unitList);

	return RedisConstants.JSP_UNIT_LIST;
    }
}