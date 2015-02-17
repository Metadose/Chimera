package com.cebedo.pmsys.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cebedo.pmsys.common.SystemConstants;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.task.service.TaskService;

@Controller
@RequestMapping(Task.OBJECT_NAME)
public class TaskController {

	public static final String ATTR_LIST = "taskList";
	public static final String ATTR_TASK = Task.OBJECT_NAME;
	public static final String ATTR_ASSIGN_PROJECT_ID = "assignProjectID";

	public static final String JSP_LIST = "taskList";
	public static final String JSP_EDIT = "taskEdit";

	private TaskService taskService;

	@Autowired(required = true)
	@Qualifier(value = "taskService")
	public void setTaskService(TaskService ps) {
		this.taskService = ps;
	}

	// @InitBinder
	// public void initBinder(WebDataBinder binder) {
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	// sdf.setLenient(true);
	// binder.registerCustomEditor(Calendar.class, new CustomDateEditor(sdf,
	// true));
	// }

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listTasks(Model model) {
		model.addAttribute(ATTR_LIST, this.taskService.listWithAllCollections());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_TASK) Task task) {
		if (task.getId() == 0) {
			this.taskService.create(task);
		} else {
			this.taskService.update(task);
		}
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_TASK + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_REDIRECT_ASSIGN_PROJECT
			+ "/{" + Project.COLUMN_PRIMARY_KEY + "}")
	public String redirectAssignProject(
			@PathVariable(Project.COLUMN_PRIMARY_KEY) int id, Model model) {
		model.addAttribute(ATTR_TASK, new Task());
		model.addAttribute(ATTR_ASSIGN_PROJECT_ID, id);
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_REDIRECT_ASSIGN_PROJECT);
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
	@RequestMapping(value = {
			SystemConstants.REQUEST_ASSIGN_PROJECT,
			"/" + SystemConstants.REQUEST_REDIRECT_ASSIGN_PROJECT + "/{"
					+ Project.COLUMN_PRIMARY_KEY + "}" }, method = RequestMethod.POST)
	public ModelAndView assignProject(@ModelAttribute(ATTR_TASK) Task task,
			@PathVariable(Project.COLUMN_PRIMARY_KEY) int projectID) {

		// Construct the project object from the ID.
		// Attach the project to the task.
		// Create the task.
		Project proj = this.taskService.getProjectByID(projectID);
		task.setProject(proj);
		this.taskService.create(task);

		// Redirect to project edit.
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
				+ "/" + "{" + projectID + "}");
	}

	@RequestMapping("/" + SystemConstants.REQUEST_DELETE + "/{"
			+ Task.COLUMN_PRIMARY_KEY + "}")
	public String delete(@PathVariable(Task.COLUMN_PRIMARY_KEY) int id) {
		this.taskService.delete(id);
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_TASK + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	@RequestMapping("/" + SystemConstants.REQUEST_EDIT + "/{"
			+ Task.COLUMN_PRIMARY_KEY + "}")
	public String editTask(@PathVariable(Task.COLUMN_PRIMARY_KEY) int id,
			Model model) {
		if (id == 0) {
			model.addAttribute(ATTR_TASK, new Task());
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}
		model.addAttribute(ATTR_TASK, this.taskService.getByID(id));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}
}