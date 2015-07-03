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
import com.cebedo.pmsys.domain.PlasterMixture;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.ConcreteProportionService;
import com.cebedo.pmsys.service.PlasterMixtureService;

@Controller
@RequestMapping(RedisConstants.OBJECT_PLASTER_MIXTURE)
@SessionAttributes(value = { RedisConstants.OBJECT_PLASTER_MIXTURE }, types = { PlasterMixture.class })
public class PlasterMixtureController {

    private static final String ATTR_PLASTER_MIXTURE_LIST = "plasterMixtureList";
    private static final String ATTR_CONCRETE_PROPORTION_LIST = "concreteProportionList";
    private static final String ATTR_PLASTER_MIXTURE = "plasterMixture";

    private AuthHelper authHelper = new AuthHelper();
    private PlasterMixtureService plasterMixtureService;
    private ConcreteProportionService concreteProportionService;

    @Autowired(required = true)
    @Qualifier(value = "concreteProportionService")
    public void setConcreteProportionService(
	    ConcreteProportionService concreteProportionService) {
	this.concreteProportionService = concreteProportionService;
    }

    @Autowired(required = true)
    @Qualifier(value = "plasterMixtureService")
    public void setPlasterMixtureService(
	    PlasterMixtureService plasterMixtureService) {
	this.plasterMixtureService = plasterMixtureService;
    }

    /**
     * Create a plasterMixture.
     * 
     * @param plasterMixture
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createPlasterMixture(
	    @ModelAttribute(ATTR_PLASTER_MIXTURE) PlasterMixture plasterMixture,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service.
	// Get response.
	String response = this.plasterMixtureService.set(plasterMixture);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Set completed.
	// Back to list page.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT
		+ RedisConstants.OBJECT_PLASTER_MIXTURE + "/"
		+ SystemConstants.REQUEST_LIST;
    }

    /**
     * Create/Edit a plasterMixture entry.
     * 
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/{"
	    + RedisConstants.OBJECT_PLASTER_MIXTURE + "}-end" }, method = RequestMethod.GET)
    public String editPlasterMixture(
	    @PathVariable(RedisConstants.OBJECT_PLASTER_MIXTURE) String key,
	    Model model) {

	// Set the list of concrete proportions.
	List<ConcreteProportion> concreteProportionList = this.concreteProportionService
		.list();
	model.addAttribute(ATTR_CONCRETE_PROPORTION_LIST,
		concreteProportionList);

	// If we're creating.
	if (key.equals("0")) {
	    Company company = this.authHelper.getAuth().getCompany();
	    model.addAttribute(ATTR_PLASTER_MIXTURE,
		    new PlasterMixture(company));
	    return RedisConstants.JSP_PLASTER_MIXTURE_EDIT;
	}

	PlasterMixture plasterMixture = this.plasterMixtureService.get(key);
	model.addAttribute(ATTR_PLASTER_MIXTURE, plasterMixture);
	return RedisConstants.JSP_PLASTER_MIXTURE_EDIT;
    }

    /**
     * List plasterMixtures.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {

	List<PlasterMixture> plasterMixtureList = this.plasterMixtureService
		.list();

	model.addAttribute(ATTR_PLASTER_MIXTURE_LIST, plasterMixtureList);

	return RedisConstants.JSP_PLASTER_MIXTURE_LIST;
    }
}