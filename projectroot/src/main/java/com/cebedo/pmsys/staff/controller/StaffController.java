package com.cebedo.pmsys.staff.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.field.controller.FieldController;
import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.field.service.FieldService;
import com.cebedo.pmsys.project.service.ProjectService;
import com.cebedo.pmsys.security.securityrole.model.SecurityRole;
import com.cebedo.pmsys.staff.model.ManagerAssignment;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.model.StaffTeamAssignment;
import com.cebedo.pmsys.staff.service.StaffService;
import com.cebedo.pmsys.system.constants.SystemConstants;
import com.cebedo.pmsys.system.ui.AlertBoxFactory;
import com.cebedo.pmsys.team.controller.TeamController;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.service.TeamService;

@Controller
@SessionAttributes(value = { StaffController.ATTR_STAFF }, types = { Staff.class })
@RequestMapping(Staff.OBJECT_NAME)
public class StaffController {

	public static final String ATTR_LIST = "staffList";
	public static final String ATTR_STAFF = Staff.OBJECT_NAME;
	public static final String JSP_LIST = "staffList";
	public static final String JSP_EDIT = "staffEdit";

	private StaffService staffService;
	private TeamService teamService;
	private FieldService fieldService;
	private ProjectService projectService;

	@Autowired(required = true)
	@Qualifier(value = "projectService")
	public void setProjectService(ProjectService s) {
		this.projectService = s;
	}

	@Autowired(required = true)
	@Qualifier(value = "fieldService")
	public void setFieldService(FieldService s) {
		this.fieldService = s;
	}

	@Autowired(required = true)
	@Qualifier(value = "staffService")
	public void setStaffService(StaffService s) {
		this.staffService = s;
	}

	@Autowired(required = true)
	@Qualifier(value = "teamService")
	public void setTeamService(TeamService s) {
		this.teamService = s;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listStaff(Model model) {
		model.addAttribute(ATTR_LIST,
				this.staffService.listWithAllCollections());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	/**
	 * Commit function that would create/update staff.
	 * 
	 * @param staff
	 * @param redirectAttrs
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_STAFF) Staff staff,
			SessionStatus status, RedirectAttributes redirectAttrs) {
		// Add ui notifications.
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;

		// Create staff.
		if (staff.getId() == 0) {
			this.staffService.create(staff);
			alertFactory.setMessage("Successfully <b>created</b> staff <b>"
					+ staff.getFullName() + "</b>.");

			// Add redirs attrs.
			redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
					alertFactory.generateHTML());
			status.setComplete();
			return SystemConstants.CONTROLLER_REDIRECT + ATTR_STAFF + "/"
					+ SystemConstants.REQUEST_LIST;
		}
		// Update staff.
		this.staffService.update(staff);
		alertFactory.setMessage("Successfully <b>updated</b> staff <b>"
				+ staff.getFullName() + "</b>.");

		// Add redirs attrs.
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_STAFF + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + staff.getId();
	}

	/**
	 * Create a staff from the origin.
	 * 
	 * @param staff
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE + "/"
			+ SystemConstants.FROM + "/{" + SystemConstants.ORIGIN + "}/{"
			+ SystemConstants.ORIGIN_ID + "}", method = RequestMethod.POST)
	public String createFromOrigin(@ModelAttribute(ATTR_STAFF) Staff staff,
			@PathVariable(value = SystemConstants.ORIGIN) String origin,
			@PathVariable(value = SystemConstants.ORIGIN_ID) String originID,
			SessionStatus status, RedirectAttributes redirectAttrs) {
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		if (staff.getId() == 0) {
			this.staffService.createFromOrigin(staff, origin, originID);

			alertFactory.setMessage("Successfully <b>created</b> staff <b>"
					+ staff.getFullName() + "</b>.");
		} else {
			alertFactory.setMessage("Successfully <b>updated</b> staff <b>"
					+ staff.getFullName() + "</b>.");

			this.staffService.update(staff);

			// Update all associated project.
			for (ManagerAssignment assignment : staff.getAssignedManagers()) {
				if (assignment.getProject() == null) {
					continue;
				}
				this.projectService.clearProjectCache(assignment.getProject()
						.getId());
			}
		}
		status.setComplete();
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		return SystemConstants.CONTROLLER_REDIRECT + origin + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + originID;
	}

	/**
	 * Delete coming from the staff list page.
	 * 
	 * @param id
	 * @param redirectAttrs
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE + "/{"
			+ Staff.OBJECT_NAME + "}", method = RequestMethod.GET)
	public String delete(@PathVariable(Staff.OBJECT_NAME) long id,
			SessionStatus status, RedirectAttributes redirectAttrs) {

		Staff staff = this.staffService.getByID(id);

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>deleted</b> staff <b>"
				+ staff.getFullName() + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		this.staffService.delete(id);
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_STAFF + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	/**
	 * Delete coming from the staff edit page.
	 * 
	 * @param status
	 * @param session
	 * @param redirectAttrs
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE, method = RequestMethod.POST)
	public String delete(SessionStatus status, HttpSession session,
			RedirectAttributes redirectAttrs) {

		Staff staff = (Staff) session.getAttribute(ATTR_STAFF);
		if (staff == null) {
			AlertBoxFactory alertFactory = AlertBoxFactory.FAILED;
			alertFactory
					.setMessage("Error occured when you tried to <b>delete</b> staff. Please try again.");
			redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
					alertFactory.generateHTML());
			status.setComplete();
			return SystemConstants.CONTROLLER_REDIRECT + ATTR_STAFF + "/"
					+ SystemConstants.REQUEST_LIST;
		}

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>deleted</b> staff <b>"
				+ staff.getFullName() + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		this.staffService.delete(staff.getId());
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_STAFF + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	/**
	 * If the Create/Edit Staff request is coming from the project.
	 * 
	 * @param staffID
	 * @param projectID
	 * @param model
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
			+ Staff.OBJECT_NAME + "}/" + SystemConstants.FROM + "/{"
			+ SystemConstants.ORIGIN + "}/{" + SystemConstants.ORIGIN_ID + "}", method = RequestMethod.GET)
	public String editStaffFromOrigin(
			@PathVariable(Staff.OBJECT_NAME) long staffID,
			@PathVariable(value = SystemConstants.ORIGIN) String origin,
			@PathVariable(value = SystemConstants.ORIGIN_ID) long originID,
			Model model) {
		// Add origin details.
		model.addAttribute(SystemConstants.ORIGIN, origin);
		model.addAttribute(SystemConstants.ORIGIN_ID, originID);

		// If new, create it.
		if (staffID == 0) {
			Staff stf = new Staff();
			model.addAttribute(ATTR_STAFF, stf);
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}

		// Else if not new, edit it.
		model.addAttribute(ATTR_STAFF,
				this.staffService.getWithAllCollectionsByID(staffID));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}

	/**
	 * Open a view page where the user can edit the staff.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
			+ Staff.COLUMN_PRIMARY_KEY + "}")
	public String editStaff(@PathVariable(Staff.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		List<Team> teamList = this.teamService.list();
		List<Field> fields = this.fieldService.list();
		model.addAttribute(TeamController.JSP_LIST, teamList);
		model.addAttribute(FieldController.JSP_LIST, fields);
		if (id == 0) {
			model.addAttribute(ATTR_STAFF, new Staff());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_STAFF,
				this.staffService.getWithAllCollectionsByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}

	/**
	 * Unassign a staff from a team.
	 * 
	 * @param teamID
	 * @param staffID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Team.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView unassignTeam(
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID,
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin) {
		this.staffService.unassignTeam(teamID, staffID);
		if (origin.equals(Team.OBJECT_NAME)) {
			return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
					+ Team.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
					+ "/" + teamID);
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ staffID);
	}

	/**
	 * Unassign all teams from a staff.
	 * 
	 * @param teamID
	 * @param staffID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Team.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllTeams(
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID) {
		this.staffService.unassignAllTeams(staffID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ staffID);
	}

	/**
	 * Assign a team to a staff.
	 * 
	 * @param staffID
	 * @param teamID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
			+ Team.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView assignTeam(
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID,
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) String originID) {
		StaffTeamAssignment stAssign = new StaffTeamAssignment();
		stAssign.setStaffID(staffID);
		stAssign.setTeamID(teamID);
		this.staffService.assignTeam(stAssign);
		if (!origin.isEmpty()) {
			return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
					+ origin + "/" + SystemConstants.REQUEST_EDIT + "/"
					+ originID);
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ staffID);
	}
}
