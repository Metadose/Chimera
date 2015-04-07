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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.common.ui.AlertBoxFactory;
import com.cebedo.pmsys.field.controller.FieldController;
import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.field.service.FieldService;
import com.cebedo.pmsys.project.controller.ProjectController;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.project.service.ProjectService;
import com.cebedo.pmsys.security.securityrole.model.SecurityRole;
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

	/**
	 * Create a team from the origin.
	 * 
	 * @param staff
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE + "/"
			+ SystemConstants.FROM + "/" + SystemConstants.ORIGIN, method = RequestMethod.POST)
	public String createFromOrigin(@ModelAttribute(ATTR_TEAM) Team team,
			@RequestParam(value = SystemConstants.ORIGIN) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID) String originID,
			RedirectAttributes redirectAttrs) {
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		if (team.getId() == 0) {
			alertFactory.setMessage("Successfully <b>created<b/> team <b>"
					+ team.getName() + "</b>.");
			this.teamService.create(team);
		} else {
			alertFactory.setMessage("Successfully <b>updated<b/> team <b>"
					+ team.getName() + "</b>.");
			this.teamService.update(team);
		}
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		return SystemConstants.CONTROLLER_REDIRECT + origin + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + originID;
	}

	@PreAuthorize("hasRole('" + SecurityRole.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_TEAM) Team team,
			RedirectAttributes redirectAttrs) {
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		if (team.getId() == 0) {
			alertFactory.setMessage("Successfully <b>created<b/> team <b>"
					+ team.getName() + "</b>.");
			this.teamService.create(team);
		} else {
			alertFactory.setMessage("Successfully <b>updated<b/> team <b>"
					+ team.getName() + "</b>.");
			this.teamService.update(team);
		}
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_TEAM + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@PreAuthorize("hasRole('" + SecurityRole.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE + "/{"
			+ Team.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.POST)
	public String delete(@PathVariable(Team.COLUMN_PRIMARY_KEY) int id,
			RedirectAttributes redirectAttrs) {
		String teamName = this.teamService.getNameByID(id);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>deleted<b/> team <b>"
				+ teamName + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		this.teamService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_TEAM + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/"
			+ SystemConstants.FROM + "/" + SystemConstants.ORIGIN)
	public String editTeamFromOrigin(
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long id,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long originID,
			Model model) {

		// Add origin details.
		model.addAttribute(SystemConstants.ORIGIN, origin);
		model.addAttribute(SystemConstants.ORIGIN_ID, originID);

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

	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
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
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
			+ Project.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView assignProjectTeam(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long originID,
			RedirectAttributes redirectAttrs) {
		String teamName = this.teamService.getNameByID(teamID);
		this.teamService.assignProjectTeam(projectID, teamID);

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>assigned<b/> team <b>"
				+ teamName + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

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
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Project.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView unassignProjectTeam(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long originID,
			RedirectAttributes redirectAttrs) {
		String teamName = this.teamService.getNameByID(teamID);
		this.teamService.unassignProjectTeam(projectID, teamID);

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>unassigned<b/> team <b>"
				+ teamName + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

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
	 * Delete all team assignments of the specified team.
	 * 
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ SystemConstants.ALL + "/" + Project.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView unassignAllTeamsFromProject(
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID,
			RedirectAttributes redirectAttrs) {
		String teamName = this.teamService.getNameByID(teamID);
		this.teamService.unassignAllTeamsFromProject(teamID);

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>unassigned " + teamName
				+ "</b> from all projects.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Team.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ teamID);
	}

	/**
	 * Unassign all teams inside a project.
	 * 
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Project.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllProjectTeams(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			RedirectAttributes redirectAttrs) {
		this.teamService.unassignAllProjectTeams(projectID);

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>unassigned all</b> teams.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

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
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Staff.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllMembers(
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID,
			RedirectAttributes redirectAttrs) {
		String teamName = this.teamService.getNameByID(teamID);
		this.teamService.unassignAllMembers(teamID);

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory
				.setMessage("Successfully <b>unassigned all</b> team members of <b>"
						+ teamName + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Team.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ teamID);
	}
}