package com.cebedo.pmsys.team.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.field.controller.FieldController;
import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.field.service.FieldService;
import com.cebedo.pmsys.project.controller.ProjectController;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.project.service.ProjectService;
import com.cebedo.pmsys.staff.controller.StaffController;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.service.StaffService;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.service.TeamService;

@Controller
@RequestMapping(Team.OBJECT_NAME)
public class TeamController {

	public static final String ATTR_LIST = "teamList";
	public static final String ATTR_TEAM = Team.OBJECT_NAME;
	public static final String JSP_LIST = "teamList";
	public static final String JSP_EDIT = "teamEdit";

	private TeamService teamService;
	private FieldService fieldService;
	private StaffService staffService;
	private ProjectService projectService;

	@Autowired(required = true)
	@Qualifier(value = "projectService")
	public void setProjectService(ProjectService s) {
		this.projectService = s;
	}

	@Autowired(required = true)
	@Qualifier(value = "staffService")
	public void setStaffService(StaffService s) {
		this.staffService = s;
	}

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

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listTeams(Model model) {
		model.addAttribute(ATTR_LIST, this.teamService.listWithTasks());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_TEAM) Team team) {
		if (team.getId() == 0) {
			this.teamService.create(team);
		} else {
			this.teamService.update(team);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_TEAM + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_TEAM_EDITOR + "')")
	@RequestMapping("/" + SystemConstants.REQUEST_DELETE + "/{"
			+ Team.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(Team.COLUMN_PRIMARY_KEY) int id) {
		this.teamService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_TEAM + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_EDIT + "/{"
			+ Team.COLUMN_PRIMARY_KEY + "}")
	public String editTeam(@PathVariable(Team.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		List<Field> fieldList = this.fieldService.list();
		List<Staff> staffList = this.staffService.list();
		List<Project> projectList = this.projectService.list();
		model.addAttribute(FieldController.JSP_LIST, fieldList);
		model.addAttribute(StaffController.JSP_LIST, staffList);
		model.addAttribute(ProjectController.JSP_LIST, projectList);
		if (id == 0) {
			model.addAttribute(ATTR_TEAM, new Team());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_TEAM,
				this.teamService.getWithAllCollectionsByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}

	/**
	 * Assign team to a project.
	 * 
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
			+ Project.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView assignProjectTeam(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long originID) {
		this.teamService.assignProjectTeam(projectID, teamID);
		if (!origin.isEmpty()) {
			return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
					+ origin + "/" + SystemConstants.REQUEST_EDIT + "/"
					+ originID);
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	/**
	 * Unassign team from a project.
	 * 
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Project.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView unassignProjectTeam(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long originID) {
		this.teamService.unassignProjectTeam(projectID, teamID);
		if (!origin.isEmpty()) {
			return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
					+ origin + "/" + SystemConstants.REQUEST_EDIT + "/"
					+ originID);
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	/**
	 * Unassign all project teams.
	 * 
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ SystemConstants.ALL + "/" + Project.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView unassignAllTeamsFromProject(
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID) {
		this.teamService.unassignAllTeamsFromProject(teamID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Team.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ teamID);
	}

	/**
	 * Unassign all project teams.
	 * 
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Project.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllProjectTeams(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID) {
		this.teamService.unassignAllProjectTeams(projectID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	/**
	 * Unassign all staff members in this team.
	 * 
	 * @param teamID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Staff.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllMembers(
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID) {
		this.teamService.unassignAllMembers(teamID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Team.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ teamID);
	}
}