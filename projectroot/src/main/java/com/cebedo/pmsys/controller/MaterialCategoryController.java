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
import com.cebedo.pmsys.domain.MaterialCategory;
import com.cebedo.pmsys.domain.Unit;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.MaterialCategoryService;
import com.cebedo.pmsys.service.UnitService;

@Controller
@RequestMapping(RedisConstants.OBJECT_MATERIAL_CATEGORY)
@SessionAttributes(value = { RedisConstants.OBJECT_MATERIAL_CATEGORY }, types = { MaterialCategory.class })
public class MaterialCategoryController {

    private static final String ATTR_UNIT_LIST = "unitList";
    private static final String ATTR_MATERIAL_CATEGORY_LIST = "materialCategoryList";
    private static final String ATTR_MATERIAL_CATEGORY = "materialcategory";

    private AuthHelper authHelper = new AuthHelper();
    private MaterialCategoryService materialCategoryService;
    private UnitService unitService;

    @Autowired(required = true)
    @Qualifier(value = "unitService")
    public void setUnitService(UnitService unitService) {
	this.unitService = unitService;
    }

    @Autowired(required = true)
    @Qualifier(value = "materialCategoryService")
    public void setMaterialCategoryService(
	    MaterialCategoryService materialCategoryService) {
	this.materialCategoryService = materialCategoryService;
    }

    /**
     * Create a materialCategory.
     * 
     * @param materialCategory
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE }, method = RequestMethod.POST)
    public String createMaterialCategory(
	    @ModelAttribute(ATTR_MATERIAL_CATEGORY) MaterialCategory materialCategory,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Do service.
	// Get response.
	String response = this.materialCategoryService.set(materialCategory);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Set completed.
	// Back to list page.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT
		+ RedisConstants.OBJECT_MATERIAL_CATEGORY + "/"
		+ SystemConstants.REQUEST_LIST;
    }

    /**
     * Create/Edit a materialCategory entry.
     * 
     * @param key
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/{"
	    + RedisConstants.OBJECT_MATERIAL_CATEGORY + "}-end" }, method = RequestMethod.GET)
    public String editMaterialCategory(
	    @PathVariable(RedisConstants.OBJECT_MATERIAL_CATEGORY) String key,
	    Model model) {

	List<Unit> unitList = this.unitService.list();
	model.addAttribute(ATTR_UNIT_LIST, unitList);

	// If we're creating.
	if (key.equals("0")) {
	    Company company = this.authHelper.getAuth().getCompany();
	    model.addAttribute(ATTR_MATERIAL_CATEGORY, new MaterialCategory(
		    company));
	    return RedisConstants.JSP_MATERIAL_CATEGORY_EDIT;
	}

	MaterialCategory materialCategory = this.materialCategoryService
		.get(key);
	model.addAttribute(ATTR_MATERIAL_CATEGORY, materialCategory);
	return RedisConstants.JSP_MATERIAL_CATEGORY_EDIT;
    }

    /**
     * List Material Categories.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String list(Model model) {

	List<MaterialCategory> materialCategoryList = this.materialCategoryService
		.list();

	model.addAttribute(ATTR_MATERIAL_CATEGORY_LIST, materialCategoryList);

	return RedisConstants.JSP_MATERIAL_CATEGORY_LIST;
    }
}