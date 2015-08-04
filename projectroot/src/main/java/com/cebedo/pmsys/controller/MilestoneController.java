package com.cebedo.pmsys.controller;

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

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.service.MilestoneService;

@Controller
@SessionAttributes(value = { MilestoneController.ATTR_MILESTONE }, types = { Milestone.class })
@RequestMapping(Milestone.OBJECT_NAME)
public class MilestoneController {

    public static final String ATTR_MILESTONE = Milestone.OBJECT_NAME;
    public static final String ATTR_LIST = "list";
    public static final String JSP_EDIT = "milestoneEdit";
    public static final String JSP_LIST = "milestoneList";

    private MilestoneService milestoneService;

    @Autowired
    @Qualifier(value = "milestoneService")
    public void setMilestoneService(MilestoneService milestoneService) {
	this.milestoneService = milestoneService;
    }

    /**
     * Create or update a milestone object.
     * 
     * @param milestone
     * @param status
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
    public String create(@ModelAttribute(ATTR_MILESTONE) Milestone milestone, SessionStatus status) {

	if (milestone.getId() == 0) {
	    this.milestoneService.create(milestone);
	    status.setComplete();
	    return SystemConstants.CONTROLLER_REDIRECT + Milestone.OBJECT_NAME + "/"
		    + SystemConstants.REQUEST_LIST;
	}

	this.milestoneService.update(milestone);
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Milestone.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + milestone.getId();
    }

    /**
     * Open an edit page for an object.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{" + Milestone.OBJECT_NAME + "}", method = RequestMethod.GET)
    public String editMilestone(@PathVariable(Milestone.OBJECT_NAME) int id, Model model) {
	if (id == 0) {
	    model.addAttribute(ATTR_MILESTONE, new Milestone());
	} else {
	    Milestone milestone = this.milestoneService.getByID(id);
	    model.addAttribute(ATTR_MILESTONE, milestone);
	}
	return JSP_EDIT;
    }

    /**
     * List all without company filter.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_ROOT, SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {
	model.addAttribute(ATTR_LIST, this.milestoneService.list());
	return JSP_LIST;
    }

    /**
     * Delete a milestone.
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_DELETE + "/{" + Milestone.OBJECT_NAME + "}", method = RequestMethod.GET)
    public String delete(@PathVariable(Milestone.OBJECT_NAME) int id) {
	this.milestoneService.delete(id);
	return SystemConstants.CONTROLLER_REDIRECT + Milestone.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_LIST;
    }

}
