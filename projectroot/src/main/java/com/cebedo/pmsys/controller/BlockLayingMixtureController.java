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
import com.cebedo.pmsys.domain.BlockLayingMixture;
import com.cebedo.pmsys.domain.CHB;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.BlockLayingMixtureService;
import com.cebedo.pmsys.service.CHBService;

@Controller
@RequestMapping(RedisConstants.OBJECT_BLOCK_LAYING_MIXTURE)
@SessionAttributes(value = { RedisConstants.OBJECT_BLOCK_LAYING_MIXTURE }, types = { BlockLayingMixture.class })
public class BlockLayingMixtureController {

    private static final String ATTR_BLOCK_LAYING_MIXTURE_LIST = "blockLayingMixtureList";
    private static final String ATTR_BLOCK_LAYING_MIXTURE = "blockLayingMixture";
    private static final String ATTR_CHB_LIST = "chbList";

    private AuthHelper authHelper = new AuthHelper();
    private BlockLayingMixtureService blockLayingMixtureService;
    private CHBService chbService;

    @Autowired(required = true)
    @Qualifier(value = "chbService")
    public void setChbService(CHBService chbService) {
	this.chbService = chbService;
    }

    @Autowired(required = true)
    @Qualifier(value = "blockLayingMixtureService")
    public void setBlockLayingMixtureService(
	    BlockLayingMixtureService blockLayingMixtureService) {
	this.blockLayingMixtureService = blockLayingMixtureService;
    }

    /**
     * Create a blockLayingMixture.
     * 
     * @param blockLayingMixture
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createBlockLayingMixture(
	    @ModelAttribute(ATTR_BLOCK_LAYING_MIXTURE) BlockLayingMixture blockLayingMixture,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service.
	// Get response.
	String response = this.blockLayingMixtureService
		.set(blockLayingMixture);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Set completed.
	// Back to list page.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT
		+ RedisConstants.OBJECT_BLOCK_LAYING_MIXTURE + "/"
		+ SystemConstants.REQUEST_LIST;
    }

    /**
     * Create/Edit a blockLayingMixture entry.
     * 
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/{"
	    + RedisConstants.OBJECT_BLOCK_LAYING_MIXTURE + "}-end" }, method = RequestMethod.GET)
    public String editBlockLayingMixture(
	    @PathVariable(RedisConstants.OBJECT_BLOCK_LAYING_MIXTURE) String key,
	    Model model) {

	// Get list of chb for selector.
	// Add to model.
	List<CHB> chbList = this.chbService.list();
	model.addAttribute(ATTR_CHB_LIST, chbList);

	// If we're creating.
	if (key.equals("0")) {
	    Company company = this.authHelper.getAuth().getCompany();
	    model.addAttribute(ATTR_BLOCK_LAYING_MIXTURE,
		    new BlockLayingMixture(company));
	    return RedisConstants.JSP_BLOCK_LAYING_MIXTURE_EDIT;
	}

	BlockLayingMixture blockLayingMixture = this.blockLayingMixtureService
		.get(key);
	model.addAttribute(ATTR_BLOCK_LAYING_MIXTURE, blockLayingMixture);
	return RedisConstants.JSP_BLOCK_LAYING_MIXTURE_EDIT;
    }

    /**
     * List blockLayingMixtures.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {

	List<BlockLayingMixture> blockLayingMixtureList = this.blockLayingMixtureService
		.list();

	model.addAttribute(ATTR_BLOCK_LAYING_MIXTURE_LIST,
		blockLayingMixtureList);

	return RedisConstants.JSP_BLOCK_LAYING_MIXTURE_LIST;
    }
}