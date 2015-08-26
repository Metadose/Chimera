package com.cebedo.pmsys.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.constants.ConstantsSystem;
import com.cebedo.pmsys.constants.RegistryJSPPath;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.pojo.FormStaffAssignment;
import com.cebedo.pmsys.service.ProjectService;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.service.TaskService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

/**
 * TODO All task-related functions should be under ProjectController.
 */
@Deprecated
@Controller
@SessionAttributes(

value = { TaskController.ATTR_TASK, TaskController.ATTR_STAFF_ASSIGNMENT,
	TaskController.ATTR_TEAM_ASSIGNMENT }

)
@RequestMapping(Task.OBJECT_NAME)
public class TaskController {

    public static final String ATTR_TEAM_LIST = "teamList";
    public static final String ATTR_FIELD_LIST = "fieldList";
    public static final String ATTR_STAFF_LIST = "staffList";
    public static final String ATTR_LIST = "taskList";
    public static final String ATTR_TASK = Task.OBJECT_NAME;
    public static final String ATTR_STAFF_ASSIGNMENT = "staffAssignment";
    public static final String ATTR_TEAM_ASSIGNMENT = "teamAssignment";
    public static final String ATTR_ASSIGN_PROJECT_ID = "assignProjectID";
    public static final String ATTR_ASSIGN_STAFF_ID = "assignStaffID";

    private AuthHelper authHelper = new AuthHelper();

    private TaskService taskService;
    private StaffService staffService;
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
    @Qualifier(value = "staffService")
    public void setStaffService(StaffService s) {
	this.staffService = s;
    }

    /**
     * Create or update a new task.
     * 
     * @param task
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_CREATE + "/" + Project.OBJECT_NAME, method = RequestMethod.POST)
    public String createWithProject(@ModelAttribute(ATTR_TASK) Task task,
	    @RequestParam(value = ConstantsSystem.ORIGIN, required = false) String origin,
	    @RequestParam(value = ConstantsSystem.ORIGIN_ID, required = false) long originID,
	    RedirectAttributes redirectAttrs) {

	AlertBoxGenerator alertFactory = AlertBoxGenerator.SUCCESS;

	// If the task is not here yet,
	// Create it.
	if (task.getId() == 0) {
	    this.taskService.createWithProject(task, originID);
	    alertFactory.setMessage("Successfully <b>created</b> task <b>" + task.getTitle() + "</b>.");
	} else {
	    this.taskService.merge(task);
	    alertFactory.setMessage("Successfully <b>updated</b> task <b>" + task.getTitle() + "</b>.");
	}

	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());

	return ConstantsSystem.CONTROLLER_REDIRECT + origin + "/" + ConstantsSystem.REQUEST_EDIT + "/"
		+ originID;
    }

    /**
     * Redirect to assign a new task for a staff.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_ASSIGN + "/" + Staff.OBJECT_NAME + "/{"
	    + Staff.COLUMN_PRIMARY_KEY + "}")
    public String redirectAssignStaff(@PathVariable(Staff.COLUMN_PRIMARY_KEY) int id, Model model) {
	// Redirect to an edit page with an empty task object
	// And ID.
	model.addAttribute(ATTR_TASK, new Task());
	model.addAttribute(ATTR_ASSIGN_STAFF_ID, id);
	return RegistryJSPPath.JSP_EDIT_TASK;
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
    @RequestMapping(value = { ConstantsSystem.REQUEST_ASSIGN + "/" + ConstantsSystem.NEW + "/"
	    + Staff.OBJECT_NAME + "/{" + Staff.COLUMN_PRIMARY_KEY + "}" }, method = RequestMethod.POST)
    public ModelAndView assignTaskToStaff(@ModelAttribute(ATTR_TASK) Task task,
	    @PathVariable(Staff.COLUMN_PRIMARY_KEY) int staffID, RedirectAttributes redirectAttrs) {

	// Construct the object from the ID.
	// Attach the object to the task.
	// Create the task.
	Staff staff = this.staffService.getByID(staffID);
	task.assignStaff(staff);
	this.taskService.merge(task);

	AlertBoxGenerator alertFactory = AlertBoxGenerator.SUCCESS;
	alertFactory.setMessage("Successfully <b>assigned</b> task <b>" + task.getTitle() + "</b>.");
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());

	// Redirect to staff edit.
	return new ModelAndView(ConstantsSystem.CONTROLLER_REDIRECT + Staff.OBJECT_NAME + "/"
		+ ConstantsSystem.REQUEST_EDIT + "/" + staffID);
    }

    /**
     * User assigns a new task for a project.<br>
     * Called when user clicks a create button from the edit project page.
     * 
     * @param projectID
     * @param model
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_CREATE + "/" + ConstantsSystem.FROM + "/"
	    + Project.OBJECT_NAME)
    public String redirectAssignProject(Model model, HttpSession session) {
	Project proj = (Project) session.getAttribute(ProjectController.ATTR_PROJECT);

	// Redirect to an edit page with an empty task object
	// And project ID.
	model.addAttribute(ATTR_TASK, new Task(proj));
	model.addAttribute(ConstantsSystem.ORIGIN, Project.OBJECT_NAME);
	model.addAttribute(ConstantsSystem.ORIGIN_ID, proj.getId());

	return RegistryJSPPath.JSP_EDIT_TASK;
    }

    /**
     * End state if the user is editing a task.
     * 
     * @param model
     * @param id
     * @return
     */
    public String endStateEditTask(Model model, long id) {
	// Else, get the object from DB
	// then populate the fields in JSP.
	Company co = this.authHelper.getAuth().getCompany();
	Long coID = co == null ? null : co.getId();

	// Task object.
	Task task = this.taskService.getByIDWithAllCollections(id);

	// List unassigned teams/staff,
	// don't list them all.
	List<Staff> staffList = this.staffService.listExcept(coID, task.getStaff());
	model.addAttribute(ATTR_STAFF_LIST, staffList);

	model.addAttribute(ATTR_STAFF_ASSIGNMENT, new FormStaffAssignment());
	model.addAttribute(ATTR_TASK, task);
	return RegistryJSPPath.JSP_EDIT_TASK;
    }

    /**
     * Unassign a staff task.
     * 
     * @param taskID
     * @param staffID
     * @return
     */
    @Deprecated
    @RequestMapping(value = ConstantsSystem.REQUEST_UNASSIGN + "/" + Staff.OBJECT_NAME, method = RequestMethod.POST)
    public ModelAndView unassignTaskFromStaff(@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
	    @RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID, RedirectAttributes redirectAttrs) {
	this.taskService.unassignStaffTask(taskID, staffID);
	String taskTitle = this.taskService.getTitleByID(taskID);
	AlertBoxGenerator alertFactory = AlertBoxGenerator.SUCCESS;
	alertFactory.setMessage("Successfully <b>unassigned</b> task <b>" + taskTitle + "</b>.");
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());
	return new ModelAndView(ConstantsSystem.CONTROLLER_REDIRECT + Task.OBJECT_NAME + "/"
		+ ConstantsSystem.REQUEST_EDIT + "/" + taskID);
    }

    /**
     * Unassign a task linked to a project.
     * 
     * @param projectID
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_UNASSIGN + "/" + ConstantsSystem.FROM + "/"
	    + Project.OBJECT_NAME, method = RequestMethod.POST)
    public ModelAndView unassignTaskByProject(@RequestParam(Task.COLUMN_PRIMARY_KEY) long taskID,
	    @RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID, RedirectAttributes redirectAttrs) {
	this.taskService.unassignTaskByProject(taskID, projectID);
	String taskName = this.taskService.getTitleByID(taskID);
	AlertBoxGenerator alertFactory = AlertBoxGenerator.SUCCESS;
	alertFactory.setMessage("Successfully <b>unassigned</b> task <b>" + taskName + "</b>.");
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());

	return new ModelAndView(ConstantsSystem.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ ConstantsSystem.REQUEST_EDIT + "/" + projectID);
    }

    /**
     * Unassign all tasks linked to a project.
     * 
     * @param projectID
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_UNASSIGN + "/" + Project.OBJECT_NAME + "/"
	    + ConstantsSystem.ALL, method = RequestMethod.POST)
    public ModelAndView unassignAllTasksInProject(
	    @RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID, RedirectAttributes redirectAttrs) {
	this.taskService.unassignAllTasksByProject(projectID);
	String projName = this.projectService.getNameByID(projectID);
	AlertBoxGenerator alertFactory = AlertBoxGenerator.SUCCESS;
	alertFactory.setMessage("Successfully <b>unassigned all</b> tasks assigned to <b>" + projName
		+ "</b>.");
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());

	return new ModelAndView(ConstantsSystem.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ ConstantsSystem.REQUEST_EDIT + "/" + projectID);
    }

    /**
     * Delete all tasks linked to a project.
     * 
     * @param projectID
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_DELETE + "/" + Project.OBJECT_NAME + "/"
	    + ConstantsSystem.ALL, method = RequestMethod.POST)
    public ModelAndView deleteAllTasksByProject(
	    @RequestParam(Project.COLUMN_PRIMARY_KEY) long projectID, RedirectAttributes redirectAttrs) {
	this.taskService.deleteAllTasksByProject(projectID);

	String projName = this.projectService.getNameByID(projectID);
	AlertBoxGenerator alertFactory = AlertBoxGenerator.SUCCESS;
	alertFactory.setMessage("Successfully <b>deleted all</b> tasks assigned to <b>" + projName
		+ "</b>.");
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());

	return new ModelAndView(ConstantsSystem.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ ConstantsSystem.REQUEST_EDIT + "/" + projectID);
    }

    /**
     * Unassign all staff assignments given a task ID. Remove all staff linked
     * to a task.
     * 
     * @param id
     * @return
     */
    @Deprecated
    @RequestMapping(value = ConstantsSystem.REQUEST_UNASSIGN + "/" + Staff.OBJECT_NAME + "/"
	    + ConstantsSystem.ALL, method = RequestMethod.POST)
    public ModelAndView unassignAllTaskStaff(@RequestParam(Task.COLUMN_PRIMARY_KEY) long id,
	    RedirectAttributes redirectAttrs) {
	this.taskService.unassignAllStaffUnderTask(id);

	String taskTitle = this.taskService.getTitleByID(id);
	AlertBoxGenerator alertFactory = AlertBoxGenerator.SUCCESS;
	alertFactory.setMessage("Successfully <b>unassigned</b> task <b>" + taskTitle
		+ "</b> from all staff members.");
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());

	return new ModelAndView(ConstantsSystem.CONTROLLER_REDIRECT + Task.OBJECT_NAME + "/"
		+ ConstantsSystem.REQUEST_EDIT + "/" + id);
    }
}