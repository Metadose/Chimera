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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.SecurityRole;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.assignment.StaffFieldAssignment;
import com.cebedo.pmsys.model.assignment.TaskFieldAssignment;
import com.cebedo.pmsys.service.FieldService;

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

	/**
	 * Assign a field to a task.
	 * 
	 * @param taskField
	 * @param fieldID
	 * @param taskID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
			+ Task.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView assignTask(
			@ModelAttribute(ATTR_FIELD) TaskFieldAssignment taskField,
			@RequestParam(Field.COLUMN_PRIMARY_KEY) long fieldID,
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID) {
		this.fieldService.assignTask(taskField, fieldID, taskID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ taskID);
	}

	/**
	 * Assign a field to a staff.
	 * 
	 * @param fieldAssignment
	 * @param fieldID
	 * @param staffID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
			+ Staff.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView assignStaff(
			@ModelAttribute(ATTR_FIELD) StaffFieldAssignment fieldAssignment,
			@RequestParam(Field.COLUMN_PRIMARY_KEY) long fieldID,
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID) {
		this.fieldService.assignStaff(fieldAssignment, fieldID, staffID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ staffID);
	}

	/**
	 * Unassign all fields from a staff.
	 * 
	 * @param staffID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Staff.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllStaff(
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID) {
		this.fieldService.unassignAllStaff(staffID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ staffID);
	}

	/**
	 * Unassign all fields in a task.
	 * 
	 * @param taskID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Task.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllTasks(
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID) {
		this.fieldService.unassignAllTasks(taskID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ taskID);
	}

	/**
	 * Update a currently assigned Task field.
	 * 
	 * @param fieldID
	 * @param taskID
	 * @param label
	 * @param value
	 * @param oldLabel
	 * @param oldValue
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UPDATE + "/"
			+ SystemConstants.ASSIGNED + "/" + Task.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView updateAssignedTaskField(
			@RequestParam(Field.COLUMN_PRIMARY_KEY) long fieldID,
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
			@RequestParam(Field.COLUMN_LABEL) String label,
			@RequestParam(Field.COLUMN_VALUE) String value,
			@RequestParam("old_" + Field.COLUMN_LABEL) String oldLabel,
			@RequestParam("old_" + Field.COLUMN_VALUE) String oldValue) {
		this.fieldService.updateAssignedTaskField(taskID, fieldID, oldLabel,
				oldValue, label, value);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ taskID);
	}

	/**
	 * Update an existing staff field.
	 * 
	 * @param fieldID
	 * @param staffID
	 * @param label
	 * @param value
	 * @param oldLabel
	 * @param oldValue
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UPDATE + "/"
			+ SystemConstants.ASSIGNED + "/" + Staff.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView updateAssignedStaffField(
			@RequestParam(Field.COLUMN_PRIMARY_KEY) long fieldID,
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID,
			@RequestParam(Field.COLUMN_LABEL) String label,
			@RequestParam(Field.COLUMN_VALUE) String value,
			@RequestParam("old_" + Field.COLUMN_LABEL) String oldLabel,
			@RequestParam("old_" + Field.COLUMN_VALUE) String oldValue) {
		this.fieldService.updateAssignedStaffField(staffID, fieldID, oldLabel,
				oldValue, label, value);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ staffID);
	}

	/**
	 * Unassign a field from a task.
	 * 
	 * @param fieldID
	 * @param taskID
	 * @param label
	 * @param value
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Task.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView unassignTask(
			@RequestParam(Field.COLUMN_PRIMARY_KEY) long fieldID,
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
			@RequestParam(Field.COLUMN_LABEL) String label,
			@RequestParam(Field.COLUMN_VALUE) String value) {
		this.fieldService.unassignTask(fieldID, taskID, label, value);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ taskID);
	}

	/**
	 * Unassign a field from a task.
	 * 
	 * @param fieldID
	 * @param staffID
	 * @param label
	 * @param value
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Staff.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView unassignStaff(
			@RequestParam(Field.COLUMN_PRIMARY_KEY) long fieldID,
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID,
			@RequestParam(Field.COLUMN_LABEL) String label,
			@RequestParam(Field.COLUMN_VALUE) String value) {
		this.fieldService.unassignStaff(fieldID, staffID, label, value);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ staffID);
	}

	@PreAuthorize("hasRole('" + SecurityRole.ROLE_FIELD_EDITOR + "')")
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

	@PreAuthorize("hasRole('" + SecurityRole.ROLE_FIELD_EDITOR + "')")
	@RequestMapping(value = "/" + SystemConstants.REQUEST_DELETE + "/{"
			+ Field.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.POST)
	public String delete(@PathVariable(Field.COLUMN_PRIMARY_KEY) int id) {
		this.fieldService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_FIELD + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	/**
	 * Open a view for a create new or update an existing one.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/" + SystemConstants.REQUEST_EDIT + "/{"
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