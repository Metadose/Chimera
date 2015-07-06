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
import com.cebedo.pmsys.domain.CHBFootingMixture;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.CHBFootingDimensionService;
import com.cebedo.pmsys.service.CHBFootingMixtureService;
import com.cebedo.pmsys.service.ConcreteProportionService;

@Controller
@RequestMapping(RedisConstants.OBJECT_CHB_FOOTING_MIXTURE)
@SessionAttributes(value = { RedisConstants.OBJECT_CHB_FOOTING_MIXTURE }, types = { CHBFootingMixture.class })
public class CHBFootingMixtureController {

    private static final String ATTR_CHB_FOOTING_MIXTURE_LIST = "chbFootingMixtureList";
    private static final String ATTR_CONCRETE_PROPORTION_LIST = "concreteProportionList";
    private static final String ATTR_CHB_FOOTING_DIMENSION_LIST = "chbFootingDimensionList";
    private static final String ATTR_CHB_FOOTING_MIXTURE = "chbFootingMixture";

    private AuthHelper authHelper = new AuthHelper();
    private CHBFootingMixtureService chbFootingMixtureService;
    private CHBFootingDimensionService chbFootingDimensionService;
    private ConcreteProportionService concreteProportionService;

    @Autowired(required = true)
    @Qualifier(value = "concreteProportionService")
    public void setConcreteProportionService(
	    ConcreteProportionService concreteProportionService) {
	this.concreteProportionService = concreteProportionService;
    }

    @Autowired(required = true)
    @Qualifier(value = "chbFootingDimensionService")
    public void setChbFootingDimensionService(
	    CHBFootingDimensionService chbFootingDimensionService) {
	this.chbFootingDimensionService = chbFootingDimensionService;
    }

    @Autowired(required = true)
    @Qualifier(value = "chbFootingMixtureService")
    public void setCHBFootingMixtureService(
	    CHBFootingMixtureService chbFootingMixtureService) {
	this.chbFootingMixtureService = chbFootingMixtureService;
    }

    /**
     * Create a chbFootingMixture.
     * 
     * @param chbFootingMixture
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createCHBFootingMixture(
	    @ModelAttribute(ATTR_CHB_FOOTING_MIXTURE) CHBFootingMixture chbFootingMixture,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service.
	// Get response.
	String response = this.chbFootingMixtureService.set(chbFootingMixture);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Set completed.
	// Back to list page.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT
		+ RedisConstants.OBJECT_CHB_FOOTING_MIXTURE + "/"
		+ SystemConstants.REQUEST_LIST;
    }

    /**
     * Create/Edit a chbFootingMixture entry.
     * 
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/{"
	    + RedisConstants.OBJECT_CHB_FOOTING_MIXTURE + "}-end" }, method = RequestMethod.GET)
    public String editCHBFootingMixture(
	    @PathVariable(RedisConstants.OBJECT_CHB_FOOTING_MIXTURE) String key,
	    Model model) {

	// Set lists.
	model.addAttribute(ATTR_CONCRETE_PROPORTION_LIST,
		this.concreteProportionService.list());
	model.addAttribute(ATTR_CHB_FOOTING_DIMENSION_LIST,
		this.chbFootingDimensionService.list());

	// If we're creating.
	if (key.equals("0")) {
	    Company company = this.authHelper.getAuth().getCompany();
	    model.addAttribute(ATTR_CHB_FOOTING_MIXTURE, new CHBFootingMixture(
		    company));
	    return RedisConstants.JSP_CHB_FOOTING_MIXTURE_EDIT;
	}

	CHBFootingMixture chbFootingMixture = this.chbFootingMixtureService
		.get(key);
	model.addAttribute(ATTR_CHB_FOOTING_MIXTURE, chbFootingMixture);
	return RedisConstants.JSP_CHB_FOOTING_MIXTURE_EDIT;
    }

    /**
     * List chbFootingMixtures.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {

	List<CHBFootingMixture> chbFootingMixtureList = this.chbFootingMixtureService
		.list();

	model.addAttribute(ATTR_CHB_FOOTING_MIXTURE_LIST, chbFootingMixtureList);

	return RedisConstants.JSP_CHB_FOOTING_MIXTURE_LIST;
    }
}