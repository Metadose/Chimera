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
import com.cebedo.pmsys.domain.CHB;
import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.CHBService;

@Controller
@RequestMapping(RedisConstants.OBJECT_CHB)
@SessionAttributes(value = { RedisConstants.OBJECT_CHB }, types = { CHB.class })
public class CHBController {

    private static final String ATTR_COMMON_LENGTH_UNIT_LIST = "commonLengthUnitList";
    private static final String ATTR_CHB_LIST = "chbList";
    private static final String ATTR_CHB = "chb";

    private AuthHelper authHelper = new AuthHelper();
    private CHBService chbService;

    @Autowired(required = true)
    @Qualifier(value = "chbService")
    public void setChbService(CHBService chbService) {
	this.chbService = chbService;
    }

    /**
     * Create a chb.
     * 
     * @param chb
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createCHB(@ModelAttribute(ATTR_CHB) CHB chb,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service.
	// Get response.
	String response = this.chbService.set(chb);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Set completed.
	// Back to list page.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + RedisConstants.OBJECT_CHB
		+ "/" + SystemConstants.REQUEST_LIST;
    }

    /**
     * Create/Edit a chb entry.
     * 
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/{"
	    + RedisConstants.OBJECT_CHB + "}-end" }, method = RequestMethod.GET)
    public String editCHB(@PathVariable(RedisConstants.OBJECT_CHB) String key,
	    Model model) {

	// Add list of common length units.
	model.addAttribute(ATTR_COMMON_LENGTH_UNIT_LIST,
		CommonLengthUnit.class.getEnumConstants());

	// If we're creating.
	if (key.equals("0")) {
	    Company company = this.authHelper.getAuth().getCompany();
	    model.addAttribute(ATTR_CHB, new CHB(company));
	    return RedisConstants.JSP_CHB_EDIT;
	}

	CHB chb = this.chbService.get(key);
	model.addAttribute(ATTR_CHB, chb);
	return RedisConstants.JSP_CHB_EDIT;
    }

    /**
     * List chbs.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {

	List<CHB> chbList = this.chbService.list();

	model.addAttribute(ATTR_CHB_LIST, chbList);

	return RedisConstants.JSP_CHB_LIST;
    }
}