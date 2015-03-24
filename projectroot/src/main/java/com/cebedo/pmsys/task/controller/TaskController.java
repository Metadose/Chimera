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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.common.ui.AlertBoxFactory;
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
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long originID,
			RedirectAttributes redirectAttrs) {

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;

		// If the task is not here yet,
		// Create it.
		if (task.getId() == 0) {
			this.taskService.createWithProject(task, originID);
			alertFactory.setMessage("Successfully <b>created</b> task <b>"
					+ task.getTitle() + "</b>.");
		} else {
			this.taskService.merge(task);
			alertFactory.setMessage("Successfully <b>updated</b> task <b>"
					+ task.getTitle() + "</b>.");
		}

		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

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
	public String create(@ModelAttribute(ATTR_TASK) Task task,
			RedirectAttributes redirectAttrs) {

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;

		// If the task is not here yet,
		// Create it.
		// Else, update.
		if (task.getId() == 0) {
			this.taskService.create(task);
			alertFactory.setMessage("Successfully <b>created</b> task <b>"
					+ task.getTitle() + "</b>.");
			redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
					alertFactory.generateHTML());
			return SystemConstants.CONTROLLER_REDIRECT + ATTR_TASK + "/"
					+ SystemConstants.REQUEST_LIST;
		} else {
			this.taskService.merge(task);
			alertFactory.setMessage("Successfully <b>updated</b> task <b>"
					+ task.getTitle() + "</b>.");
			redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
					alertFactory.generateHTML());
			return SystemConstants.CONTROLLER_REDIRECT + ATTR_TASK + "/"
					+ SystemConstants.REQUEST_EDIT + "/" + task.getId();
		}
	}

	/**
	 * Create or update a task from origin.
	 * 
	 * @param task
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_TASK_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE + "/"
			+ SystemConstants.FROM + "/" + SystemConstants.ORIGIN, method = RequestMethod.POST)
	public String createFromOrigin(
			@ModelAttribute(ATTR_TASK) Task task,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long originID,
			RedirectAttributes redirectAttrs) {

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;

		if (task.getId() == 0) {
			this.taskService.create(task);
			alertFactory.setMessage("Successfully <b>created</b> task <b>"
					+ task.getTitle() + "</b>.");
		} else {
			this.taskService.merge(task);
			alertFactory.setMessage("Successfully <b>updated</b> task <b>"
					+ task.getTitle() + "</b>.");
		}
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		return SystemConstants.CONTROLLER_REDIRECT + origin + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + originID;
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
	 * Assign a task to a project. TODO Figure out return ModelAndView vs return
	 * String. TODO Instead of passing the whole Model of Task and using
	 * "merge", pass only the ID, get the object, attach the project and
	 * "update".
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
			@PathVariable(Project.COLUMN_PRIMARY_KEY) int projectID,
			RedirectAttributes redirectAttrs) {

		// Construct the project object from the ID.
		// Attach the project to the task.
		// Create the task.
		Project proj = this.projectService.getByID(projectID);
		task.setProject(proj);
		this.taskService.merge(task);

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>assigned</b> task <b>"
				+ task.getTitle() + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		// Redirect to project edit.
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	/**
	 * Redirect to assign a new task for a staff.
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

	/**
	 * TODO Fix also "merge" to "update" here.
	 * 
	 * @param task
	 * @param staffID
	 * @param redirectAttrs
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = { SystemConstants.REQUEST_ASSIGN + "/"
			+ SystemConstants.NEW + "/" + Staff.OBJECT_NAME + "/{"
			+ Staff.COLUMN_PRIMARY_KEY + "}" }, method = RequestMethod.POST)
	public ModelAndView assignStaff(@ModelAttribute(ATTR_TASK) Task task,
			@PathVariable(Staff.COLUMN_PRIMARY_KEY) int staffID,
			RedirectAttributes redirectAttrs) {

		// Construct the object from the ID.
		// Attach the object to the task.
		// Create the task.
		Staff staff = this.staffService.getByID(staffID);
		task.assignStaff(staff);
		this.taskService.merge(task);

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>assigned</b> task <b>"
				+ task.getTitle() + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		// Redirect to staff edit.
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
	public String delete(@PathVariable(Task.COLUMN_PRIMARY_KEY) long id,
			RedirectAttributes redirectAttrs) {

		String taskTitle = this.taskService.getTitleByID(id);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>deleted</b> task <b>"
				+ taskTitle + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

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
			@RequestParam(Task.COLUMN_STATUS) int status,
			RedirectAttributes redirectAttrs) {

		this.taskService.mark(taskID, status);
		String taskTitle = this.taskService.getTitleByID(taskID);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		// TODO Change the int value of "status" to a text.
		alertFactory.setMessage("Successfully <b>marked</b> task <b>"
				+ taskTitle + "</b> as <b>" + status + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

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
			@RequestParam(Task.COLUMN_STATUS) int status,
			RedirectAttributes redirectAttrs) {

		this.taskService.mark(taskID, status);
		String taskTitle = this.taskService.getTitleByID(taskID);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		// TODO Change the int value of "status" to a text.
		alertFactory.setMessage("Successfully <b>marked</b> task <b>"
				+ taskTitle + "</b> as <b>" + status + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	/**
	 * Open a page with appropriate values from a project object.<br>
	 * May be a Create Page or Edit Page. TODO Add method-level security.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/"
			+ SystemConstants.FROM + "/" + SystemConstants.ORIGIN, method = RequestMethod.POST)
	public String editTaskFromOrigin(
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long originID,
			Model model) {

		// TODO Optimize by getting only name and id.
		// Get list of teams for the selector.
		List<Team> teamList = this.teamService.list();
		List<Staff> staffList = this.staffService.list();
		List<Field> fieldList = this.fieldService.list();
		model.addAttribute(TeamController.JSP_LIST, teamList);
		model.addAttribute(StaffController.JSP_LIST, staffList);
		model.addAttribute(FieldController.JSP_LIST, fieldList);

		// Set origin values.
		model.addAttribute(SystemConstants.ORIGIN, origin);
		model.addAttribute(SystemConstants.ORIGIN_ID, originID);

		// If ID is zero,
		// Open a page with empty values, ready to create.
		if (taskID == 0) {
			model.addAttribute(ATTR_TASK, new Task());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}

		// Else, get the object from DB
		// then populate the fields in JSP.
		model.addAttribute(ATTR_TASK,
				this.taskService.getByIDWithAllCollections(taskID));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
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
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID,
			RedirectAttributes redirectAttrs) {
		this.taskService.assignStaffTask(taskID, staffID);

		String taskTitle = this.taskService.getTitleByID(taskID);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>assigned</b> task <b>"
				+ taskTitle + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

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
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID,
			RedirectAttributes redirectAttrs) {
		this.taskService.assignTeamTask(taskID, teamID);
		String taskTitle = this.taskService.getTitleByID(taskID);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>assigned</b> task <b>"
				+ taskTitle + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ taskID);
	}

	/**
	 * Delete all team assignments in a specific task.
	 * 
	 * @param taskID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Team.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllTeamTasks(
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
			RedirectAttributes redirectAttrs) {
		this.taskService.unassignAllTeamTasks(taskID);
		String taskTitle = this.taskService.getTitleByID(taskID);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>unassigned " + taskTitle
				+ "</b> from all teams.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
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
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID,
			RedirectAttributes redirectAttrs) {
		this.taskService.unassignTeamTask(taskID, teamID);

		String taskTitle = this.taskService.getTitleByID(taskID);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>unassigned</b> task <b>"
				+ taskTitle + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

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
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID,
			RedirectAttributes redirectAttrs) {
		this.taskService.unassignStaffTask(taskID, staffID);
		String taskTitle = this.taskService.getTitleByID(taskID);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>unassigned</b> task <b>"
				+ taskTitle + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ taskID);
	}

	/**
	 * Unassign a task linked to a project.
	 * 
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ SystemConstants.FROM + "/" + Project.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView unassignTaskByProject(
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			RedirectAttributes redirectAttrs) {
		this.taskService.unassignTaskByProject(taskID, projectID);
		String taskName = this.taskService.getTitleByID(taskID);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>unassigned</b> task <b>"
				+ taskName + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	/**
	 * Unassign all tasks linked to a project.
	 * 
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_PROJECT_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Project.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllTasksByProject(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			RedirectAttributes redirectAttrs) {
		this.taskService.unassignAllTasksByProject(projectID);
		String projName = this.projectService.getNameByID(projectID);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory
				.setMessage("Successfully <b>unassigned all</b> tasks assigned to <b>"
						+ projName + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	/**
	 * Delete all tasks linked to a project.
	 * 
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_TASK_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE + "/"
			+ Project.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView deleteAllTasksByProject(
			@RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID,
			RedirectAttributes redirectAttrs) {
		this.taskService.deleteAllTasksByProject(projectID);

		String projName = this.projectService.getNameByID(projectID);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory
				.setMessage("Successfully <b>deleted all</b> tasks assigned to <b>"
						+ projName + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + projectID);
	}

	/**
	 * Unassign all staff assignments given a task ID. Remove all staff linked
	 * to a task.
	 * 
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasRole('" + SystemConstants.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Staff.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllStaffTasks(
			@RequestParam(Task.COLUMN_PRIMARY_KEY) long id,
			RedirectAttributes redirectAttrs) {
		this.taskService.unassignAllStaffTasks(id);

		String taskTitle = this.taskService.getTitleByID(id);
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>unassigned</b> task <b>"
				+ taskTitle + "</b> from all staff members.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Task.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ id);
	}
}