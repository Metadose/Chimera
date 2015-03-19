package com.cebedo.pmsys.task.controller;

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
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.project.service.ProjectService;
import com.cebedo.pmsys.staff.controller.StaffController;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.service.StaffService;
import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.task.service.TaskService;
import com.cebedo.pmsys.team.controller.TeamController;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.service.TeamService;

@Controller
@RequestMapping(Task.OBJECT_NAME)
public class TaskController {

	public static final String ATTR_LIST = "taskList";
	public static final String ATTR_TASK = Task.OBJECT_NAME;
	public static final String ATTR_ASSIGN_PROJECT_ID = "assignProjectID";
	public static final String ATTR_ASSIGN_STAFF_ID = "assignStaffID";

	public static final String JSP_LIST = "taskList";
	public static final String JSP_EDIT = "taskEdit";

	private TaskService taskService;
	private TeamService teamService;
	private StaffService staffService;
	private FieldService fieldService;
	private ProjectService projectService;

	@Autowired(required = true)
	@Qualifier(value = "projectService")
	public void setProjectService(ProjectService s) {
		this.projectService = s;
	}

	@Autowired(required = true)
	@Qualifier(value = "taskService")
	public void setTaskService(TaskService s) {
		this.taskService = s;
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

	/**
	 * List all tasks and load all collections.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listTasks(Model model) {
		model.addAttribute(ATTR_LIST, this.taskService.listWithAllCollections());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	/**
	 * Create or update a new task.
	 * 
	 * @param task
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE + "/"
			+ Project.OBJECT_NAME, method = RequestMethod.POST)
	public String createWithProject(
			@ModelAttribute(ATTR_TASK) Task task,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long originID) {

		// If the task is not here yet,
		// Create it.
		this.taskService.createWithProject(task, originID);
		return SystemConstants.CONTROLLER_REDIRECT + origin + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + originID;
	}

	/**
	 * Create or update a new task.
	 * 
	 * @param task
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_TASK_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_TASK) Task task) {

		// If the task is not here yet,
		// Create it.
		// Else, update.
		if (task.getId() == 0) {
			this.taskService.create(task);
			return SystemConstants.CONTROLLER_REDIRECT + ATTR_TASK + "/"
					+ SystemConstants.REQUEST_LIST;
		} else {
			this.taskService.update(task);
			return SystemConstants.CONTROLLER_REDIRECT + ATTR_TASK + "/"
					+ SystemConstants.REQUEST_EDIT + "/" + task.getId();
		}
	}

	/**
	 * User assigns a new task for a project.<br>
	 * Called when user clicks a create button from the edit project page.
	 * 
	 * @param projectID
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
			+ SystemConstants.FROM + "/" + Project.OBJECT_NAME, method = RequestMethod.POST)
	public String redirectAssignProject(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long originID,
			Model model) {

		// Redirect to an edit page with an empty task object
		// And project ID.
		model.addAttribute(ATTR_TASK, new Task());
		model.addAttribute(Project.COLUMN_PRIMARY_KEY, projectID);
		model.addAttribute(SystemConstants.ORIGIN, origin);
		model.addAttribute(SystemConstants.ORIGIN_ID, originID);
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_ASSIGN);
		return JSP_EDIT;
	}

	/**
	 * Assign a task to a project.
	 * 
	 * @param task
	 * @param projectID
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = {
			SystemConstants.REQUEST_ASSIGN_PROJECT,
			SystemConstants.REQUEST_ASSIGN_PROJECT + "/{"
					+ Project.COLUMN_PRIMARY_KEY + "}" }, method = RequestMethod.POST)
	public ModelAndView assignProject(@ModelAttribute(ATTR_TASK) Task task,
			@PathVariable(Project.COLUMN_PRIMARY_KEY) int projectID) {

		// Construct the project object from the ID.
		// Attach the project to the task.
		// Create the task.
		Project proj = this.projectService.getByID(projectID);
		task.setProject(proj);
		this.taskService.create(task);

		// Redirect to project edit.
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	/**
	 * Assign a new task for a staff.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(SystemConstants.REQUEST_ASSIGN + "/" + Staff.OBJECT_NAME
			+ "/{" + Staff.COLUMN_PRIMARY_KEY + "}")
	public String redirectAssignStaff(
			@PathVariable(Staff.COLUMN_PRIMARY_KEY) int id, Model model) {
		// Redirect to an edit page with an empty task object
		// And ID.
		model.addAttribute(ATTR_TASK, new Task());
		model.addAttribute(ATTR_ASSIGN_STAFF_ID, id);
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_ASSIGN);
		return JSP_EDIT;
	}

	@PreAuthorize("hasRole('" + SystemConstants.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = { SystemConstants.REQUEST_ASSIGN + "/"
			+ SystemConstants.NEW + "/" + Staff.OBJECT_NAME + "/{"
			+ Staff.COLUMN_PRIMARY_KEY + "}" }, method = RequestMethod.POST)
	public ModelAndView assignStaff(@ModelAttribute(ATTR_TASK) Task task,
			@PathVariable(Staff.COLUMN_PRIMARY_KEY) int staffID) {

		// Construct the object from the ID.
		// Attach the object to the task.
		// Create the task.
		Staff staff = this.staffService.getByID(staffID);
		task.assignStaff(staff);
		this.taskService.create(task);

		// Redirect to project edit.
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ staffID);
	}

	/**
	 * Delete a specific task.
	 * 
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_TASK_EDITOR + "')")
	@RequestMapping("/" + SystemConstants.REQUEST_DELETE + "/{"
			+ Task.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(Task.COLUMN_PRIMARY_KEY) int id) {
		this.taskService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_TASK + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	/**
	 * Mark the status based on param passed.
	 * 
	 * @param taskID
	 * @param status
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_TASK_EDITOR + "')")
	@RequestMapping(SystemConstants.REQUEST_MARK)
	public ModelAndView mark(
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
			@RequestParam(Task.COLUMN_STATUS) int status) {
		this.taskService.mark(taskID, status);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_LIST);
	}

	/**
	 * Set the project task to the status specified.
	 * 
	 * @param projectID
	 * @param taskID
	 * @param status
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(SystemConstants.REQUEST_MARK + "/" + Project.OBJECT_NAME)
	public ModelAndView markProject(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
			@RequestParam(Task.COLUMN_STATUS) int status) {

		this.taskService.mark(taskID, status);

		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	/**
	 * Open a page with appropriate values.<br>
	 * May be a Create Page or Edit Page.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/" + SystemConstants.REQUEST_EDIT + "/{"
			+ Task.COLUMN_PRIMARY_KEY + "}")
	public String editTask(@PathVariable(Task.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		// TODO Optimize by getting only name and id.
		// Get list of teams for the selector.
		List<Team> teamList = this.teamService.list();
		List<Staff> staffList = this.staffService.list();
		List<Field> fieldList = this.fieldService.list();
		model.addAttribute(TeamController.JSP_LIST, teamList);
		model.addAttribute(StaffController.JSP_LIST, staffList);
		model.addAttribute(FieldController.JSP_LIST, fieldList);

		// If ID is zero,
		// Open a page with empty values, ready to create.
		if (id == 0) {
			model.addAttribute(ATTR_TASK, new Task());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}

		// Else, get the object from DB
		// then populate the fields in JSP.
		model.addAttribute(ATTR_TASK,
				this.taskService.getByIDWithAllCollections(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}

	/**
	 * Assign a new task to a staff.
	 * 
	 * @param taskID
	 * @param staffID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
			+ Staff.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView assignStaffTask(
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID) {
		this.taskService.assignStaffTask(taskID, staffID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ taskID);
	}

	/**
	 * Assign a new task to a team.
	 * 
	 * @param taskID
	 * @param staffID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
			+ Team.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView assignTeamTask(
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID) {
		this.taskService.assignTeamTask(taskID, teamID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ taskID);
	}

	/**
	 * Unassign all team tasks.
	 * 
	 * @param taskID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Team.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllTeamTasks(
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID) {
		this.taskService.unassignAllTeamTasks(taskID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ taskID);
	}

	/**
	 * Unassign a task from a team.
	 * 
	 * @param taskID
	 * @param teamID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Team.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView unassignTeamTask(
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID) {
		this.taskService.unassignTeamTask(taskID, teamID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ taskID);
	}

	/**
	 * Unassign a staff task.
	 * 
	 * @param taskID
	 * @param staffID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Staff.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView unassignStaffTask(
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID) {
		this.taskService.unassignStaffTask(taskID, staffID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ taskID);
	}

	/**
	 * Unassign all project tasks.
	 * 
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Project.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllProjectTasks(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID) {
		this.taskService.unassignAllProjectTasks(projectID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	/**
	 * Unassign all staff tasks.
	 * 
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Staff.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllStaffTasks(
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long id) {
		this.taskService.unassignAllStaffTasks(id);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ id);
	}
}