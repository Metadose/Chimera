package com.cebedo.pmsys.controller;

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

import com.cebedo.pmsys.bean.StaffAssignmentBean;
import com.cebedo.pmsys.bean.TeamAssignmentBean;
import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Expense;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.SecurityRole;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.service.ExpenseService;
import com.cebedo.pmsys.service.FieldService;
import com.cebedo.pmsys.service.ProjectService;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.service.TaskService;
import com.cebedo.pmsys.service.TeamService;
import com.cebedo.pmsys.ui.AlertBoxFactory;

@Controller
@SessionAttributes(value = { TaskController.ATTR_TASK,
	TaskController.ATTR_STAFF_ASSIGNMENT,
	TaskController.ATTR_TEAM_ASSIGNMENT, TaskController.ATTR_EXPENSE }, types = {
	Task.class, StaffAssignmentBean.class, TeamAssignmentBean.class,
	Expense.class })
@RequestMapping(Task.OBJECT_NAME)
public class TaskController {

    public static final String ATTR_LIST = "taskList";
    public static final String ATTR_TASK = Task.OBJECT_NAME;
    public static final String ATTR_EXPENSE = Expense.OBJECT_NAME;
    public static final String ATTR_STAFF_ASSIGNMENT = "staffAssignment";
    public static final String ATTR_TEAM_ASSIGNMENT = "teamAssignment";
    public static final String ATTR_ASSIGN_PROJECT_ID = "assignProjectID";
    public static final String ATTR_ASSIGN_STAFF_ID = "assignStaffID";

    public static final String JSP_LIST = Task.OBJECT_NAME + "/taskList";
    public static final String JSP_EDIT = Task.OBJECT_NAME + "/taskEdit";

    private AuthHelper authHelper = new AuthHelper();

    private TaskService taskService;
    private TeamService teamService;
    private StaffService staffService;
    private FieldService fieldService;
    private ProjectService projectService;
    private ExpenseService expenseService;

    @Autowired(required = true)
    @Qualifier(value = "expenseService")
    public void setExpenseService(ExpenseService s) {
	this.expenseService = s;
    }

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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
    public String create(@ModelAttribute(ATTR_TASK) Task task,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;

	// If the task is not here yet,
	// Create it.
	// Else, update.
	if (task.getId() == 0) {
	    this.taskService.create(task);
	    status.setComplete();
	    alertFactory.setMessage("Successfully <b>created</b> task <b>"
		    + task.getTitle() + "</b>.");
	    redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		    alertFactory.generateHTML());
	    return SystemConstants.CONTROLLER_REDIRECT + ATTR_TASK + "/"
		    + SystemConstants.REQUEST_LIST;
	} else {
	    this.taskService.merge(task);
	    status.setComplete();
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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_CREATE + "/"
	    + SystemConstants.FROM + "/{" + SystemConstants.ORIGIN + "}/{"
	    + SystemConstants.ORIGIN_ID + "}", method = RequestMethod.POST)
    public String createFromOrigin(@ModelAttribute(ATTR_TASK) Task task,
	    SessionStatus status,
	    @PathVariable(SystemConstants.ORIGIN) String origin,
	    @PathVariable(SystemConstants.ORIGIN_ID) long originID,
	    RedirectAttributes redirectAttrs) {

	AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;

	if (task.getId() == 0) {
	    this.taskService.create(task);
	    status.setComplete();
	    alertFactory.setMessage("Successfully <b>created</b> task <b>"
		    + task.getTitle() + "</b>.");
	} else {
	    this.taskService.merge(task);
	    status.setComplete();
	    alertFactory.setMessage("Successfully <b>updated</b> task <b>"
		    + task.getTitle() + "</b>.");
	}
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		alertFactory.generateHTML());
	return SystemConstants.CONTROLLER_REDIRECT + origin + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + originID;
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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
	    + Staff.OBJECT_NAME + "/{" + Staff.COLUMN_PRIMARY_KEY + "}")
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
    @Deprecated
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
    @RequestMapping(value = { SystemConstants.REQUEST_ASSIGN + "/"
	    + SystemConstants.NEW + "/" + Staff.OBJECT_NAME + "/{"
	    + Staff.COLUMN_PRIMARY_KEY + "}" }, method = RequestMethod.POST)
    public ModelAndView assignTaskToStaff(@ModelAttribute(ATTR_TASK) Task task,
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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_DELETE + "/{"
	    + Task.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.POST)
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
     * Mark the task status based on param passed. The method is GET since the
     * mark action is done via a href.
     * 
     * @param taskID
     * @param status
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_MARK, method = RequestMethod.GET)
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
     * Set the project task to the status specified. The method is GET since the
     * mark action is done via a href.
     * 
     * @param projectID
     * @param taskID
     * @param status
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_MARK + "/"
	    + Project.OBJECT_NAME, method = RequestMethod.GET)
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
     * User assigns a new task for a project.<br>
     * Called when user clicks a create button from the edit project page.
     * 
     * @param projectID
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_CREATE + "/"
	    + SystemConstants.FROM + "/" + Project.OBJECT_NAME)
    public String redirectAssignProject(Model model, HttpSession session) {
	Project proj = (Project) session
		.getAttribute(ProjectController.ATTR_PROJECT);

	// Redirect to an edit page with an empty task object
	// And project ID.
	model.addAttribute(ATTR_TASK, new Task(proj));
	model.addAttribute(SystemConstants.ORIGIN, Project.OBJECT_NAME);
	model.addAttribute(SystemConstants.ORIGIN_ID, proj.getId());
	model.addAttribute(SystemConstants.ATTR_ACTION,
		SystemConstants.ACTION_ASSIGN);

	return TaskController.JSP_EDIT;
    }

    /**
     * Open a page with appropriate values.<br>
     * May be a Create Page or Edit Page.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/"
	    + Expense.OBJECT_NAME + "/{" + Expense.OBJECT_NAME + "}")
    public String editExpense(
	    @PathVariable(Expense.OBJECT_NAME) long expenseId, Model model,
	    HttpSession session) {
	Task task = (Task) session.getAttribute(ATTR_TASK);
	if (expenseId == 0) {
	    model.addAttribute(ATTR_EXPENSE, new Expense());
	    return ExpenseController.JSP_EDIT;
	}
	Expense expense = this.expenseService.getByID(expenseId);
	model.addAttribute(ATTR_EXPENSE, expense);
	model.addAttribute(SystemConstants.ORIGIN, Task.OBJECT_NAME);
	model.addAttribute(SystemConstants.ORIGIN_ID, task.getId());
	return ExpenseController.JSP_EDIT;
    }

    /**
     * Open a page with appropriate values from a project object.<br>
     * May be a Create Page or Edit Page. TODO Add method-level security.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = SystemConstants.ACTION_EDIT + "/{"
	    + Task.OBJECT_NAME + "}/" + SystemConstants.FROM + "/{"
	    + SystemConstants.ORIGIN + "}/{" + SystemConstants.ORIGIN_ID + "}", method = RequestMethod.GET)
    public String editTaskFromOrigin(
	    @PathVariable(Task.OBJECT_NAME) long taskID,
	    @PathVariable(SystemConstants.ORIGIN) String origin,
	    @PathVariable(SystemConstants.ORIGIN_ID) long originID, Model model) {

	// TODO Faster performance by getting only name and id.
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

	// TODO Merge this block if condition with function
	// redirectAssignProject.
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
     * Unassign all staff from a task.
     * 
     * @param projectID
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Staff.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.GET)
    public String unassignAllTaskStaff(HttpSession session,
	    SessionStatus status, RedirectAttributes redirectAttrs) {
	Task task = (Task) session.getAttribute(ATTR_TASK);

	// Error handling if staff was not set properly.
	if (task == null) {
	    AlertBoxFactory alertFactory = AlertBoxFactory.FAILED;
	    alertFactory
		    .setMessage("Error occured when you tried to <b>unassign all staff</b>. Please try again.");
	    redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		    alertFactory.generateHTML());
	    status.setComplete();
	    return SystemConstants.CONTROLLER_REDIRECT + Task.OBJECT_NAME + "/"
		    + SystemConstants.REQUEST_LIST;
	}

	long taskID = task.getId();
	this.taskService.unassignAllStaffTasks(taskID);

	AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
	alertFactory.setMessage("Successfully <b>unassigned all</b> managers.");
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		alertFactory.generateHTML());

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Task.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + taskID;
    }

    /**
     * Unassign all teams inside a task.
     * 
     * @param projectID
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Team.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.GET)
    public String unassignAllTaskTeams(HttpSession session,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Get IDs.
	Task task = (Task) session.getAttribute(ATTR_TASK);
	long taskID = task.getId();

	// Do service.
	this.taskService.unassignAllTeamsInTask(taskID);

	// Construct response.
	AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
	alertFactory.setMessage("Successfully <b>unassigned all</b> teams.");
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		alertFactory.generateHTML());

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Task.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + taskID;
    }

    /**
     * Unassign a staff from a task.
     * 
     * @param projectID
     * @param staffID
     * @param position
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Staff.OBJECT_NAME + "/{" + Staff.OBJECT_NAME + "}", method = RequestMethod.GET)
    public String unassignTaskStaff(HttpSession session, SessionStatus status,
	    @PathVariable(Staff.OBJECT_NAME) long staffID,
	    RedirectAttributes redirectAttrs) {

	// Get the object from the session.
	Task task = (Task) session.getAttribute(ATTR_TASK);

	// Error handling if staff was not set properly.
	if (task == null) {
	    AlertBoxFactory alertFactory = AlertBoxFactory.FAILED;
	    alertFactory
		    .setMessage("Error occured when you tried to <b>unassign</b> a staff. Please try again.");
	    redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		    alertFactory.generateHTML());
	    status.setComplete();
	    return SystemConstants.CONTROLLER_REDIRECT + Task.OBJECT_NAME + "/"
		    + SystemConstants.REQUEST_LIST;
	}

	AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
	alertFactory.setMessage("TOODOO Successfully <b>unassigned "
		+ task.getTitle() + "</b>.");
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		alertFactory.generateHTML());

	this.taskService.unassignStaffTask(task.getId(), staffID);
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Task.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + task.getId();
    }

    /**
     * Unassign team from a project.
     * 
     * @param projectID
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Team.OBJECT_NAME + "/{" + Team.OBJECT_NAME + "}", method = RequestMethod.GET)
    public String unassignTaskTeam(HttpSession session, SessionStatus status,
	    @PathVariable(Team.OBJECT_NAME) long teamID,
	    RedirectAttributes redirectAttrs) {

	// Get the IDs.
	Task task = (Task) session.getAttribute(ATTR_TASK);
	long taskID = task.getId();

	// Do service.
	this.taskService.unassignTeamTask(taskID, teamID);

	// Construct response.
	String teamName = this.teamService.getNameByID(teamID);
	AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
	alertFactory.setMessage("Successfully <b>unassigned</b> team <b>"
		+ teamName + "</b>.");
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		alertFactory.generateHTML());
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Task.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + taskID;
    }

    /**
     * Assign a staff to a task.
     * 
     * @param projectID
     * @param staffID
     * @param staffAssignment
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
	    + Staff.OBJECT_NAME, method = RequestMethod.POST)
    public String assignTaskStaff(
	    HttpSession session,
	    SessionStatus status,
	    @ModelAttribute(ATTR_STAFF_ASSIGNMENT) StaffAssignmentBean staffAssignment,
	    RedirectAttributes redirectAttrs) {

	Task task = (Task) session.getAttribute(ATTR_TASK);

	// Error handling if staff was not set properly.
	if (task == null) {
	    AlertBoxFactory alertFactory = AlertBoxFactory.FAILED;
	    alertFactory
		    .setMessage("Error occured when you tried to <b>assign</b> a staff. Please try again.");
	    redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		    alertFactory.generateHTML());
	    status.setComplete();
	    return SystemConstants.CONTROLLER_REDIRECT + Task.OBJECT_NAME + "/"
		    + SystemConstants.REQUEST_LIST;
	}

	// Fetch staff name, construct ui notifs.
	AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;

	// FIXME
	alertFactory.setMessage("TODOOOOO Successfully <b>assigned "
		+ task.getTitle() + "</b> as <b></b>.");
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		alertFactory.generateHTML());

	// Do service, clear session.
	// Then redirect.
	this.taskService.assignStaffTask(task.getId(),
		staffAssignment.getStaffID());
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Task.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + task.getId();
    }

    /**
     * Open a page with appropriate values.<br>
     * May be a Create Page or Edit Page.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
	    + Task.COLUMN_PRIMARY_KEY + "}")
    public String editTask(@PathVariable(Task.COLUMN_PRIMARY_KEY) int id,
	    Model model) {
	// TODO Optimize by getting only name and id.
	// Get list of teams for the selector.
	// FIXME Team and field list? Do we need this?
	List<Field> fieldList = this.fieldService.list();
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
	Company co = this.authHelper.getAuth().getCompany();
	Long coID = co == null ? null : co.getId();

	// FIXME List unassigned teams/staff,
	// don't list them all.
	List<Team> teamList = this.teamService.list(coID);
	List<Staff> staffList = this.staffService.list(coID);
	model.addAttribute(TeamController.JSP_LIST, teamList);
	model.addAttribute(StaffController.JSP_LIST, staffList);
	model.addAttribute(ATTR_STAFF_ASSIGNMENT, new StaffAssignmentBean());
	model.addAttribute(ATTR_TEAM_ASSIGNMENT, new TeamAssignmentBean());
	model.addAttribute(ATTR_TASK,
		this.taskService.getByIDWithAllCollections(id));
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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
	    + Team.OBJECT_NAME, method = RequestMethod.POST)
    public String assignTaskTeam(
	    @ModelAttribute(ATTR_TEAM_ASSIGNMENT) TeamAssignmentBean taBean,
	    HttpSession session, SessionStatus status,
	    RedirectAttributes redirectAttrs) {
	// Get the IDs.
	Task task = (Task) session.getAttribute(ATTR_TASK);
	long teamID = taBean.getTeamID();
	long taskID = task.getId();

	// Do the service.
	this.taskService.assignTeamTask(taskID, teamID);

	// Construct response.
	String teamName = this.teamService.getNameByID(teamID);
	AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
	alertFactory.setMessage("Successfully <b>assigned</b> team <b>"
		+ teamName + "</b>.");
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		alertFactory.generateHTML());
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Task.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + taskID;
    }

    /**
     * Delete all team assignments in a specific task.
     * 
     * @param taskID
     * @return
     */
    @Deprecated
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_TEAM_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Team.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
    public ModelAndView unassignAllTaskTeams(
	    @RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
	    RedirectAttributes redirectAttrs) {
	this.taskService.unassignAllTeamsInTask(taskID);
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
    @Deprecated
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_TEAM_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Team.OBJECT_NAME, method = RequestMethod.POST)
    public ModelAndView unassignTaskFromTeam(
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
    @Deprecated
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Staff.OBJECT_NAME, method = RequestMethod.POST)
    public ModelAndView unassignTaskFromStaff(
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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Project.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
    public ModelAndView unassignAllTasksInProject(
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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_TASK_EDITOR + "')")
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
    @Deprecated
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Staff.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
    public ModelAndView unassignAllTaskStaff(
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