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
import com.cebedo.pmsys.domain.CHBHorizontalReinforcement;
import com.cebedo.pmsys.enums.CHBReinforcingBarsLayer;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.CHBHorizontalReinforcementService;

@Controller
@RequestMapping(RedisConstants.OBJECT_CHB_HORIZONTAL_REINFORCEMENT)
@SessionAttributes(value = { RedisConstants.OBJECT_CHB_HORIZONTAL_REINFORCEMENT }, types = { CHBHorizontalReinforcement.class })
public class CHBHorizontalReinforcementController {

    private static final String ATTR_CHB_HORIZONTAL_REINFORCEMENT_LIST = "chbHorizontalReinforcementList";
    private static final String ATTR_CHB_HORIZONTAL_REINFORCEMENT = "chbHorizontalReinforcement";
    private static final String ATTR_CHB_BAR_LAYERS_LIST = "barLayersList";

    private AuthHelper authHelper = new AuthHelper();
    private CHBHorizontalReinforcementService chbHorizontalReinforcementService;

    @Autowired(required = true)
    @Qualifier(value = "chbHorizontalReinforcementService")
    public void setCHBHorizontalReinforcementService(
	    CHBHorizontalReinforcementService chbHorizontalReinforcementService) {
	this.chbHorizontalReinforcementService = chbHorizontalReinforcementService;
    }

    /**
     * Create a chbHorizontalReinforcement.
     * 
     * @param chbHorizontalReinforcement
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createCHBHorizontalReinforcement(
	    @ModelAttribute(ATTR_CHB_HORIZONTAL_REINFORCEMENT) CHBHorizontalReinforcement chbHorizontalReinforcement,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service.
	// Get response.
	String response = this.chbHorizontalReinforcementService
		.set(chbHorizontalReinforcement);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Set completed.
	// Back to list page.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT
		+ RedisConstants.OBJECT_CHB_HORIZONTAL_REINFORCEMENT + "/"
		+ SystemConstants.REQUEST_LIST;
    }

    /**
     * Create/Edit a chbHorizontalReinforcement entry.
     * 
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/{"
	    + RedisConstants.OBJECT_CHB_HORIZONTAL_REINFORCEMENT + "}-end" }, method = RequestMethod.GET)
    public String editCHBHorizontalReinforcement(
	    @PathVariable(RedisConstants.OBJECT_CHB_HORIZONTAL_REINFORCEMENT) String key,
	    Model model) {

	model.addAttribute(ATTR_CHB_BAR_LAYERS_LIST,
		CHBReinforcingBarsLayer.class.getEnumConstants());

	// If we're creating.
	if (key.equals("0")) {
	    Company company = this.authHelper.getAuth().getCompany();
	    model.addAttribute(ATTR_CHB_HORIZONTAL_REINFORCEMENT,
		    new CHBHorizontalReinforcement(company));
	    return RedisConstants.JSP_CHB_HORIZONTAL_REINFORCEMENT_EDIT;
	}

	CHBHorizontalReinforcement chbHorizontalReinforcement = this.chbHorizontalReinforcementService
		.get(key);
	model.addAttribute(ATTR_CHB_HORIZONTAL_REINFORCEMENT,
		chbHorizontalReinforcement);
	return RedisConstants.JSP_CHB_HORIZONTAL_REINFORCEMENT_EDIT;
    }

    /**
     * List chbHorizontalReinforcements.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {

	List<CHBHorizontalReinforcement> chbHorizontalReinforcementList = this.chbHorizontalReinforcementService
		.list();

	model.addAttribute(ATTR_CHB_HORIZONTAL_REINFORCEMENT_LIST,
		chbHorizontalReinforcementList);

	return RedisConstants.JSP_CHB_HORIZONTAL_REINFORCEMENT_LIST;
    }
}