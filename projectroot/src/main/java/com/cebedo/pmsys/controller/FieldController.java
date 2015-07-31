package com.cebedo.pmsys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.SecurityRole;
import com.cebedo.pmsys.service.FieldService;

@Controller
@RequestMapping(Field.OBJECT_NAME)
public class FieldController {

    public static final String ATTR_LIST = "fieldList";
    public static final String ATTR_FIELD = Field.OBJECT_NAME;
    public static final String JSP_LIST = Field.OBJECT_NAME + "/fieldList";
    public static final String JSP_EDIT = Field.OBJECT_NAME + "/fieldEdit";

    private FieldService fieldService;

    @Autowired(required = true)
    @Qualifier(value = "fieldService")
    public void setFieldService(FieldService ps) {
	this.fieldService = ps;
    }

    @RequestMapping(value = { SystemConstants.REQUEST_ROOT, SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String listField(Model model) {
	model.addAttribute(ATTR_LIST, this.fieldService.listWithAllCollections());
	return JSP_LIST;
    }

    @PreAuthorize("hasRole('" + SecurityRole.ROLE_FIELD_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
    public String create(@ModelAttribute(ATTR_FIELD) Field field) {
	if (field.getId() == 0) {
	    this.fieldService.create(field);
	} else {
	    this.fieldService.update(field);
	}
	return SystemConstants.CONTROLLER_REDIRECT + ATTR_FIELD + "/" + SystemConstants.REQUEST_LIST;
    }

    @PreAuthorize("hasRole('" + SecurityRole.ROLE_FIELD_EDITOR + "')")
    @RequestMapping(value = "/" + SystemConstants.REQUEST_DELETE + "/{" + Field.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.POST)
    public String delete(@PathVariable(Field.COLUMN_PRIMARY_KEY) int id) {
	this.fieldService.delete(id);
	return SystemConstants.CONTROLLER_REDIRECT + ATTR_FIELD + "/" + SystemConstants.REQUEST_LIST;
    }

    /**
     * Open a view for a create new or update an existing one.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/" + SystemConstants.REQUEST_EDIT + "/{" + Field.COLUMN_PRIMARY_KEY + "}")
    public String editField(@PathVariable(Field.COLUMN_PRIMARY_KEY) int id, Model model) {
	if (id == 0) {
	    model.addAttribute(ATTR_FIELD, new Field());
	    return JSP_EDIT;
	}
	model.addAttribute(ATTR_FIELD, this.fieldService.getByID(id));
	return JSP_EDIT;
    }
}