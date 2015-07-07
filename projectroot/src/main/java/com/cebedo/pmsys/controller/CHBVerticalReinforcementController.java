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
import com.cebedo.pmsys.domain.CHBVerticalReinforcement;
import com.cebedo.pmsys.enums.CHBReinforcingBarsSpacing;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.CHBVerticalReinforcementService;

@Controller
@RequestMapping(RedisConstants.OBJECT_CHB_VERTICAL_REINFORCEMENT)
@SessionAttributes(value = { RedisConstants.OBJECT_CHB_VERTICAL_REINFORCEMENT }, types = { CHBVerticalReinforcement.class })
public class CHBVerticalReinforcementController {

    private static final String ATTR_CHB_VERTICAL_REINFORCEMENT_LIST = "chbVerticalReinforcementList";
    private static final String ATTR_CHB_VERTICAL_REINFORCEMENT = "chbVerticalReinforcement";
    private static final String ATTR_CHB_BAR_SPACING_LIST = "barSpacingList";

    private AuthHelper authHelper = new AuthHelper();
    private CHBVerticalReinforcementService chbVerticalReinforcementService;

    @Autowired(required = true)
    @Qualifier(value = "chbVerticalReinforcementService")
    public void setCHBVerticalReinforcementService(
	    CHBVerticalReinforcementService chbVerticalReinforcementService) {
	this.chbVerticalReinforcementService = chbVerticalReinforcementService;
    }

    /**
     * Create a chbVerticalReinforcement.
     * 
     * @param chbVerticalReinforcement
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createCHBVerticalReinforcement(
	    @ModelAttribute(ATTR_CHB_VERTICAL_REINFORCEMENT) CHBVerticalReinforcement chbVerticalReinforcement,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service.
	// Get response.
	String response = this.chbVerticalReinforcementService
		.set(chbVerticalReinforcement);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Set completed.
	// Back to list page.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT
		+ RedisConstants.OBJECT_CHB_VERTICAL_REINFORCEMENT + "/"
		+ SystemConstants.REQUEST_LIST;
    }

    /**
     * Create/Edit a chbVerticalReinforcement entry.
     * 
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/{"
	    + RedisConstants.OBJECT_CHB_VERTICAL_REINFORCEMENT + "}-end" }, method = RequestMethod.GET)
    public String editCHBVerticalReinforcement(
	    @PathVariable(RedisConstants.OBJECT_CHB_VERTICAL_REINFORCEMENT) String key,
	    Model model) {

	model.addAttribute(ATTR_CHB_BAR_SPACING_LIST,
		CHBReinforcingBarsSpacing.class.getEnumConstants());

	// If we're creating.
	if (key.equals("0")) {
	    Company company = this.authHelper.getAuth().getCompany();
	    model.addAttribute(ATTR_CHB_VERTICAL_REINFORCEMENT,
		    new CHBVerticalReinforcement(company));
	    return RedisConstants.JSP_CHB_VERTICAL_REINFORCEMENT_EDIT;
	}

	CHBVerticalReinforcement chbVerticalReinforcement = this.chbVerticalReinforcementService
		.get(key);
	model.addAttribute(ATTR_CHB_VERTICAL_REINFORCEMENT,
		chbVerticalReinforcement);
	return RedisConstants.JSP_CHB_VERTICAL_REINFORCEMENT_EDIT;
    }

    /**
     * List chbVerticalReinforcements.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {

	List<CHBVerticalReinforcement> chbVerticalReinforcementList = this.chbVerticalReinforcementService
		.list();

	model.addAttribute(ATTR_CHB_VERTICAL_REINFORCEMENT_LIST,
		chbVerticalReinforcementList);

	return RedisConstants.JSP_CHB_VERTICAL_REINFORCEMENT_LIST;
    }
}