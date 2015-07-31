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
import com.cebedo.pmsys.domain.Shape;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.service.ShapeService;

@Controller
@RequestMapping(RedisConstants.OBJECT_SHAPE)
@SessionAttributes(value = { RedisConstants.OBJECT_SHAPE }, types = { Shape.class })
public class ShapeController {

    private static final String ATTR_LIST = "shapeList";
    private static final String JSP_LIST = RedisConstants.OBJECT_SHAPE + "/shapeList";
    private static final String JSP_EDIT = RedisConstants.OBJECT_SHAPE + "/shapeEdit";

    private AuthHelper authHelper = new AuthHelper();
    private ShapeService shapeService;

    @Autowired(required = true)
    @Qualifier(value = "shapeService")
    public void setFormulaService(ShapeService s) {
	this.shapeService = s;
    }

    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {
	List<Shape> formulaList = this.shapeService.list();
	model.addAttribute(ATTR_LIST, formulaList);
	return JSP_LIST;
    }

    /**
     * Create or update a formula.
     * 
     * @param shape
     * @param status
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createShape(@ModelAttribute(RedisConstants.OBJECT_SHAPE) Shape shape,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service and get response.
	String response = this.shapeService.set(shape);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Clear session.
	status.setComplete();

	return SystemConstants.CONTROLLER_REDIRECT + RedisConstants.OBJECT_SHAPE + "/"
		+ SystemConstants.REQUEST_LIST;
    }

    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/{" + RedisConstants.OBJECT_SHAPE
	    + "}-end" }, method = RequestMethod.GET)
    public String editShape(Model model, @PathVariable(RedisConstants.OBJECT_SHAPE) String key) {

	if (key.equals("0")) {
	    model.addAttribute(RedisConstants.OBJECT_SHAPE, new Shape(this.authHelper.getAuth()
		    .getCompany()));
	    return JSP_EDIT;
	}

	Shape shape = this.shapeService.get(key);
	model.addAttribute(RedisConstants.OBJECT_SHAPE, shape);
	return JSP_EDIT;
    }

}