package com.cebedo.pmsys.field.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.field.service.FieldService;

@Controller
@RequestMapping(Field.OBJECT_NAME)
public class FieldController {

	public static final String ATTR_LIST = "fieldList";
	public static final String ATTR_FIELD = Field.OBJECT_NAME;
	public static final String JSP_LIST = "fieldList";
	public static final String JSP_EDIT = "fieldEdit";

	private FieldService fieldService;

	@Autowired(required = true)
	@Qualifier(value = "fieldService")
	public void setFieldService(FieldService ps) {
		this.fieldService = ps;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listField(Model model) {
		model.addAttribute(ATTR_LIST,
				this.fieldService.listWithAllCollections());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_FIELD) Field field) {
		if (field.getId() == 0) {
			this.fieldService.create(field);
		} else {
			this.fieldService.update(field);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_FIELD + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_DELETE + "/{"
			+ Field.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(Field.COLUMN_PRIMARY_KEY) int id) {
		this.fieldService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_FIELD + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_EDIT + "/{"
			+ Field.COLUMN_PRIMARY_KEY + "}")
	public String editField(@PathVariable(Field.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		if (id == 0) {
			model.addAttribute(ATTR_FIELD, new Field());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_FIELD, this.fieldService.getByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}
}