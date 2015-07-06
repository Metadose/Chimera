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
import com.cebedo.pmsys.domain.CHBFootingDimension;
import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.CHBFootingDimensionService;

@Controller
@RequestMapping(RedisConstants.OBJECT_CHB_FOOTING_DIMENSION)
@SessionAttributes(value = { RedisConstants.OBJECT_CHB_FOOTING_DIMENSION }, types = { CHBFootingDimension.class })
public class CHBFootingDimensionController {

    private static final String ATTR_CHB_FOOTING_DIMENSION_LIST = "chbFootingDimensionList";
    private static final String ATTR_CHB_FOOTING_DIMENSION = "chbFootingDimension";
    private static final String ATTR_COMMON_LENGTH_UNIT_LIST = "commonLengthUnitList";

    private AuthHelper authHelper = new AuthHelper();
    private CHBFootingDimensionService chbFootingDimensionService;

    @Autowired(required = true)
    @Qualifier(value = "chbFootingDimensionService")
    public void setCHBFootingDimensionService(
	    CHBFootingDimensionService chbFootingDimensionService) {
	this.chbFootingDimensionService = chbFootingDimensionService;
    }

    /**
     * Create a chbFootingDimension.
     * 
     * @param chbFootingDimension
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createCHBFootingDimension(
	    @ModelAttribute(ATTR_CHB_FOOTING_DIMENSION) CHBFootingDimension chbFootingDimension,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service.
	// Get response.
	String response = this.chbFootingDimensionService
		.set(chbFootingDimension);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Set completed.
	// Back to list page.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT
		+ RedisConstants.OBJECT_CHB_FOOTING_DIMENSION + "/"
		+ SystemConstants.REQUEST_LIST;
    }

    /**
     * Create/Edit a chbFootingDimension entry.
     * 
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/{"
	    + RedisConstants.OBJECT_CHB_FOOTING_DIMENSION + "}-end" }, method = RequestMethod.GET)
    public String editCHBFootingDimension(
	    @PathVariable(RedisConstants.OBJECT_CHB_FOOTING_DIMENSION) String key,
	    Model model) {

	// Add list of common length units.
	model.addAttribute(ATTR_COMMON_LENGTH_UNIT_LIST,
		CommonLengthUnit.class.getEnumConstants());

	// If we're creating.
	if (key.equals("0")) {
	    Company company = this.authHelper.getAuth().getCompany();
	    model.addAttribute(ATTR_CHB_FOOTING_DIMENSION,
		    new CHBFootingDimension(company));
	    return RedisConstants.JSP_CHB_FOOTING_DIMENSION_EDIT;
	}

	CHBFootingDimension chbFootingDimension = this.chbFootingDimensionService
		.get(key);
	model.addAttribute(ATTR_CHB_FOOTING_DIMENSION, chbFootingDimension);
	return RedisConstants.JSP_CHB_FOOTING_DIMENSION_EDIT;
    }

    /**
     * List chbFootingDimensions.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {

	List<CHBFootingDimension> chbFootingDimensionList = this.chbFootingDimensionService
		.list();

	model.addAttribute(ATTR_CHB_FOOTING_DIMENSION_LIST,
		chbFootingDimensionList);

	return RedisConstants.JSP_CHB_FOOTING_DIMENSION_LIST;
    }
}