package com.cebedo.pmsys.project.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.field.controller.FieldController;
import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.field.model.FieldAssignment;
import com.cebedo.pmsys.field.service.FieldService;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.project.service.ProjectService;
import com.cebedo.pmsys.security.securityrole.model.SecurityRole;
import com.cebedo.pmsys.staff.controller.StaffController;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.service.StaffService;
import com.cebedo.pmsys.system.bean.FieldAssignmentBean;
import com.cebedo.pmsys.system.constants.SystemConstants;
import com.cebedo.pmsys.system.helper.AuthHelper;
import com.cebedo.pmsys.system.ui.AlertBoxFactory;
import com.cebedo.pmsys.team.controller.TeamController;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.service.TeamService;

@Controller
@SessionAttributes(value = { Project.OBJECT_NAME }, types = { Project.class })
@RequestMapping(Project.OBJECT_NAME)
public class ProjectController {

	public static final String ATTR_LIST = "projectList";
	public static final String ATTR_PROJECT = Project.OBJECT_NAME;
	public static final String ATTR_FIELD = Field.OBJECT_NAME;
	public static final String JSP_LIST = "projectList";
	public static final String JSP_EDIT = "projectEdit";

	private AuthHelper authHelper = new AuthHelper();
	private ProjectService projectService;
	private StaffService staffService;
	private TeamService teamService;
	private FieldService fieldService;

	@Autowired(required = true)
	@Qualifier(value = "fieldService")
	public void setFieldService(FieldService s) {
		this.fieldService = s;
	}

	@Autowired(required = true)
	@Qualifier(value = "teamService")
	public void setTeamService(TeamService s) {
		this.teamService = s;
	}

	@Autowired(required = true)
	@Qualifier(value = "staffService")
	public void setStaffService(StaffService s) {
		this.staffService = s;
	}

	@Autowired(required = true)
	@Qualifier(value = "projectService")
	public void setProjectService(ProjectService s) {
		this.projectService = s;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listProjects(Model model) {
		model.addAttribute(ATTR_LIST, this.projectService.listWithTasks());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	@PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@Valid Project project,
			RedirectAttributes redirectAttrs, SessionStatus status) {

		// Used for notification purposes.
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;

		// If request is to create a new project.
		if (project.getId() == 0) {
			alertFactory.setMessage("Successfully <b>created</b> project <b>"
					+ project.getName() + "</b>.");
			this.projectService.create(project);
			redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
					alertFactory.generateHTML());
			return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECT + "/"
					+ SystemConstants.REQUEST_LIST;
		}

		// If request is to edit a project.
		alertFactory.setMessage("Successfully <b>updated</b> project <b>"
				+ project.getName() + "</b>.");
		this.projectService.update(project);
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECT + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + project.getId();
	}

	/**
	 * Delete a project.
	 * 
	 * @param id
	 * @param redirectAttrs
	 * @param status
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE + "/{"
			+ Project.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.POST)
	public String delete(@PathVariable(Project.COLUMN_PRIMARY_KEY) int id,
			RedirectAttributes redirectAttrs, SessionStatus status) {
		// Alert result.
		String projectName = this.projectService.getNameByID(id);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>deleted</b> project <b>"
				+ projectName + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		// TODO Cleanup also the SYS_HOME.
		this.projectService.delete(id);
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECT + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	/**
	 * Assign a field to a project.
	 * 
	 * @param fieldAssignment
	 * @param fieldID
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
			+ Field.OBJECT_NAME, method = RequestMethod.POST)
	public String assignField(HttpSession session,
			@ModelAttribute(ATTR_FIELD) FieldAssignmentBean faBean,
			RedirectAttributes redirectAttrs, SessionStatus status) {

		// Get project from session.
		// Construct commit object.
		Project proj = (Project) session
				.getAttribute(ProjectController.ATTR_PROJECT);
		long fieldID = 1;
		FieldAssignment fieldAssignment = new FieldAssignment();
		fieldAssignment.setLabel(faBean.getLabel());
		fieldAssignment.setField(new Field(faBean.getFieldID()));
		fieldAssignment.setValue(faBean.getValue());
		fieldAssignment.setProject(proj);

		// Construct ui notifications.
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory
				.setMessage("Successfully <b>added</b> extra information <b>"
						+ fieldAssignment.getLabel() + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		// Do service.
		this.fieldService.assignProject(fieldAssignment, fieldID, proj.getId());

		// Remove session variables.
		// Evict project cache.
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + proj.getId();
	}

	/**
	 * Open an existing/new project page.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
			+ Project.COLUMN_PRIMARY_KEY + "}")
	public String editProject(@PathVariable(Project.COLUMN_PRIMARY_KEY) int id,
			Model model) {

		// Model for the "Add More Information".
		model.addAttribute(ATTR_FIELD, new FieldAssignmentBean(id, 1));

		// If ID is zero, create new.
		if (id == 0) {
			model.addAttribute(ATTR_PROJECT, new Project());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}

		Project proj = this.projectService.getByIDWithAllCollections(id);
		Long companyID = this.authHelper.getAuth().isSuperAdmin() ? null : proj
				.getCompany().getId();
		model.addAttribute(ATTR_PROJECT, proj);

		// Get list of fields.
		List<Field> fieldList = this.fieldService.list();
		model.addAttribute(FieldController.ATTR_LIST, fieldList);

		// Get list of staff members for manager assignments.
		List<Staff> staffList = this.staffService.listUnassignedInProject(
				companyID, proj);
		model.addAttribute(StaffController.ATTR_LIST, staffList);

		// Get list of teams.
		List<Team> teamList = this.teamService.listUnassignedInProject(
				companyID, proj);
		model.addAttribute(TeamController.ATTR_LIST, teamList);

		// Add the type of action.
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);

		return JSP_EDIT;
	}
}