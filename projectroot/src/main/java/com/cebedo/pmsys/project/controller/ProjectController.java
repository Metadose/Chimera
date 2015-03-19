package com.cebedo.pmsys.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cebedo.pmsys.common.AuthUtils;
import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.field.controller.FieldController;
import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.field.service.FieldService;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.project.service.ProjectService;
import com.cebedo.pmsys.staff.controller.StaffController;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.service.StaffService;
import com.cebedo.pmsys.team.controller.TeamController;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.service.TeamService;

@Controller
@RequestMapping(Project.OBJECT_NAME)
public class ProjectController {

	public static final String ATTR_LIST = "projectList";
	public static final String ATTR_PROJECT = Project.OBJECT_NAME;
	public static final String JSP_LIST = "projectList";
	public static final String JSP_EDIT = "projectEdit";

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

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_PROJECT) Project project) {
		if (project.getId() == 0) {
			this.projectService.create(project);
		} else {
			this.projectService.update(project);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECT + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping("/" + SystemConstants.REQUEST_DELETE + "/{"
			+ Project.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(Project.COLUMN_PRIMARY_KEY) int id) {
		// TODO Cleanup also the SYS_HOME.
		this.projectService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECT + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_EDIT + "/{"
			+ Project.COLUMN_PRIMARY_KEY + "}")
	public String editProject(@PathVariable(Project.COLUMN_PRIMARY_KEY) int id,
			Model model) {

		// If ID is zero, create new.
		if (id == 0) {
			model.addAttribute(ATTR_PROJECT, new Project());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}

		Project proj = this.projectService.getByIDWithAllCollections(id);
		Long companyID = AuthUtils.getAuth().isSuperAdmin() ? null : proj
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