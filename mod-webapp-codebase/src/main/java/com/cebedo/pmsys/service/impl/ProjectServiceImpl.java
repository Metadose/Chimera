package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.CalendarEventType;
import com.cebedo.pmsys.enums.EstimateCostType;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.pojo.JSONCalendarEvent;
import com.cebedo.pmsys.pojo.JSONTimelineGantt;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.service.EstimateCostService;
import com.cebedo.pmsys.service.ProjectService;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.service.TaskService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DateUtils;
import com.cebedo.pmsys.validator.MultipartFileValidator;
import com.cebedo.pmsys.validator.ProjectValidator;
import com.google.gson.Gson;

@Service
public class ProjectServiceImpl implements ProjectService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    private ProjectDAO projectDAO;
    private CompanyDAO companyDAO;
    private ProjectAuxValueRepo projectAuxValueRepo;
    private StaffService staffService;
    private TaskService taskService;
    private EstimateCostService estimateCostService;

    @Autowired
    @Qualifier(value = "estimateCostService")
    public void setEstimateCostService(EstimateCostService estimateCostService) {
	this.estimateCostService = estimateCostService;
    }

    @Autowired
    @Qualifier(value = "taskService")
    public void setTaskService(TaskService taskService) {
	this.taskService = taskService;
    }

    @Autowired
    @Qualifier(value = "staffService")
    public void setStaffService(StaffService staffService) {
	this.staffService = staffService;
    }

    public void setProjectAuxValueRepo(ProjectAuxValueRepo projectAuxValueRepo) {
	this.projectAuxValueRepo = projectAuxValueRepo;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public void setCompanyDAO(CompanyDAO companyDAO) {
	this.companyDAO = companyDAO;
    }

    @Autowired
    MultipartFileValidator multipartFileValidator;

    @Autowired
    ProjectValidator projectValidator;

    @Override
    @Transactional
    public String uploadExcelCosts(MultipartFile multipartFile, Project project, BindingResult result) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Service layer form validation.
	this.multipartFileValidator.validate(multipartFile, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Do service.
	List<EstimateCost> costs = this.estimateCostService.convertExcelToCostList(multipartFile,
		project);
	if (costs == null) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_FILE_CORRUPT_INVALID);
	}

	List<EstimateCost> includeCosts = new ArrayList<EstimateCost>();
	List<EstimateCost> projectCosts = this.estimateCostService.list(project);

	// Check if this task is already committed.
	for (EstimateCost cost : costs) {

	    String name = cost.getName();
	    double estimatedCost = cost.getCost();
	    double actualCost = cost.getActualCost();
	    EstimateCostType costType = cost.getCostType();
	    boolean include = true;

	    // Check for existing.
	    for (EstimateCost projCost : projectCosts) {
		String projName = projCost.getName();
		double projEstimatedCost = projCost.getCost();
		double projActualCost = projCost.getActualCost();
		EstimateCostType projCostType = projCost.getCostType();

		// If we found a match from the project tasks,
		// break. Don't include to list to commit.
		if (name.equals(projName) && estimatedCost == projEstimatedCost
			&& actualCost == projActualCost && costType == projCostType) {
		    include = false;
		    break;
		}
	    }

	    if (include) {
		includeCosts.add(cost);
	    }
	}

	// Create mass.
	// Returns null if ok.
	String invalid = this.estimateCostService.createMassCosts(includeCosts, result);
	if (invalid != null) {
	    return invalid;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_CREATE_MASS, Project.OBJECT_NAME, project.getId(),
		ConstantsRedis.OBJECT_ESTIMATE_COST);

	return AlertBoxGenerator.SUCCESS.generateCreateEntries(ConstantsRedis.OBJECT_ESTIMATE_COST);
    }

    /**
     * Create Tasks from an Excel file.
     */
    @Override
    @Transactional
    public String uploadExcelTasks(MultipartFile multipartFile, Project project, BindingResult result) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Service layer form validation.
	this.multipartFileValidator.validate(multipartFile, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Do service.
	List<Task> tasks = this.taskService.convertExcelToTaskList(multipartFile, project);
	if (tasks == null) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_FILE_CORRUPT_INVALID);
	}

	List<Task> includeTasks = new ArrayList<Task>();
	Set<Task> projTasks = project.getAssignedTasks();

	// Check if this task is already committed.
	for (Task task : tasks) {

	    String title = task.getTitle();
	    String content = task.getContent();
	    Date start = task.getDateStart();
	    double duration = task.getDuration();
	    boolean include = true;

	    // Check for existing.
	    for (Task projTask : projTasks) {
		String projTitle = projTask.getTitle();
		String projContent = projTask.getContent();
		Date projStart = projTask.getDateStart();
		double projDuration = projTask.getDuration();

		// If we found a match from the project tasks,
		// break. Don't include to list to commit.
		if (title.equals(projTitle) && content.equals(projContent) && start.equals(projStart)
			&& duration == projDuration) {
		    include = false;
		    break;
		}
	    }

	    if (include) {
		includeTasks.add(task);
	    }
	}

	// Create mass.
	// Returns null if ok.
	String invalid = this.taskService.createMassTasks(includeTasks, result);
	if (invalid != null) {
	    return invalid;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_CREATE_MASS, Project.OBJECT_NAME, project.getId(),
		Task.OBJECT_NAME);

	return AlertBoxGenerator.SUCCESS.generateAssignEntries(Task.OBJECT_NAME);
    }

    /**
     * Create a new project.
     */
    @Override
    @Transactional
    public String create(Project project, BindingResult result) {

	// Service layer form validation.
	this.projectValidator.validate(project, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Do service.
	// Set the project aux object.
	AuthenticationToken auth = this.authHelper.getAuth();
	project.setCompany(auth.getCompany());
	this.projectDAO.create(project);
	this.projectAuxValueRepo.set(new ProjectAux(project));

	// Log.
	this.messageHelper.send(AuditAction.ACTION_CREATE, Project.OBJECT_NAME, project.getId());

	// Return success response.
	return AlertBoxGenerator.SUCCESS.generateCreate(Project.OBJECT_NAME, project.getName());
    }

    /**
     * Create Staff members from Excel.
     */
    @Override
    @Transactional
    public String uploadExcelStaff(MultipartFile multipartFile, Project proj, BindingResult result) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Service layer form validation.
	// Service layer form validation.
	this.multipartFileValidator.validate(multipartFile, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Convert excel to staff objects.
	// Commit all in staff list.
	// Assign all staff to project.
	List<Staff> staffList = this.staffService.convertExcelToStaffList(multipartFile,
		proj.getCompany());

	// Invalid file.
	if (staffList == null) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_FILE_CORRUPT_INVALID);
	}

	staffList = this.staffService.createOrGetStaffInList(staffList, result);

	// There was a problem with the list of staff you provided. Please
	// review the list and try again.
	if (staffList == null) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_STAFF_MASS_UPLOAD_GENERIC);
	}

	// If everything's ok.
	assignAllStaffToProject(proj, staffList);

	// Log.
	this.messageHelper.send(AuditAction.ACTION_CREATE_MASS, Project.OBJECT_NAME, proj.getId(),
		Staff.OBJECT_NAME);
	return AlertBoxGenerator.SUCCESS.generateCreateEntries(Staff.OBJECT_NAME);
    }

    /**
     * Assign all staff to project.
     * 
     * @param proj
     * @param staffList
     */
    private void assignAllStaffToProject(Project proj, List<Staff> staffList) {
	Set<Staff> projectStaff = proj.getAssignedStaff();
	projectStaff.addAll(staffList);
	proj.setAssignedStaff(projectStaff);
	this.projectDAO.merge(proj);
    }

    /**
     * Update a project.
     */
    @Override
    @Transactional
    public String update(Project project, BindingResult result) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Service layer form validation.
	if (result != null) {
	    this.projectValidator.validate(project, result);
	    if (result.hasErrors()) {
		return this.validationHelper.errorMessageHTML(result);
	    }
	}

	// Actual service.
	Company company = this.companyDAO.getCompanyByObjID(Project.TABLE_NAME,
		Project.COLUMN_PRIMARY_KEY, project.getId());
	project.setCompany(company);

	this.projectDAO.update(project);

	// Log.
	this.messageHelper.send(AuditAction.ACTION_UPDATE, Project.OBJECT_NAME, project.getId());

	// Response for the user.
	return AlertBoxGenerator.SUCCESS.generateUpdate(Project.OBJECT_NAME, project.getName());
    }

    @Override
    @Transactional
    public List<Project> list() {

	AuthenticationToken token = this.authHelper.getAuth();
	this.messageHelper.send(AuditAction.ACTION_LIST, Project.OBJECT_NAME);

	// List as super admin.
	if (token.isSuperAdmin()) {
	    return this.projectDAO.list(null);
	}

	// List as not a super admin.
	Company company = token.getCompany();
	return this.projectDAO.list(company.getId());
    }

    @Override
    @Transactional
    public Project getByID(long id) {

	// Get.
	Project project = this.projectDAO.getByID(id);

	// Check security.
	// Log and return.
	if (this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.send(AuditAction.ACTION_GET, Project.OBJECT_NAME, id);
	    return project;
	}

	// Log a warning.
	this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	return new Project();
    }

    /**
     * Delete a project.
     */
    @Override
    @Transactional
    public String delete(long id) {

	// Get auth and actual object.
	Project project = this.projectDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(project) && !this.authHelper.isCompanyAdmin()) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// If authorized, do actual service.
	this.projectDAO.delete(id);
	this.projectAuxValueRepo.delete(ProjectAux.constructKey(project));

	// Delete also linked redis objects.
	// company.fk:4:*project:139*
	// company.fk:4:*project.fk:139*
	long companyID = project.getCompany().getId();
	Set<String> keysSet = this.projectAuxValueRepo
		.keys(String.format("company.fk:%s:*project:%s*", companyID, id));
	Set<String> keysSet2 = this.projectAuxValueRepo
		.keys(String.format("company.fk:%s:*project.fk:%s*", companyID, id));
	keysSet.addAll(keysSet2);
	this.projectAuxValueRepo.delete(keysSet);

	// Log.
	this.messageHelper.send(AuditAction.ACTION_DELETE, Project.OBJECT_NAME, project.getId());

	// Success response.
	return AlertBoxGenerator.SUCCESS.generateDelete(Project.OBJECT_NAME, project.getName());
    }

    @Override
    @Transactional
    public List<Project> listWithAllCollections() {
	// Log.
	AuthenticationToken token = this.authHelper.getAuth();
	this.messageHelper.send(AuditAction.ACTION_LIST, Project.OBJECT_NAME);

	if (token.isSuperAdmin()) {
	    return this.projectDAO.listWithAllCollections(null);
	}

	// Return the list.
	Company company = token.getCompany();
	return this.projectDAO.listWithAllCollections(company.getId());
    }

    @Override
    @Transactional
    public Project getByIDWithAllCollections(long id) {

	Project project = this.projectDAO.getByIDWithAllCollections(id);

	// Log and return.
	if (this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.send(AuditAction.ACTION_GET, Project.OBJECT_NAME, id);
	    return project;
	}

	// Log then return empty object.
	this.messageHelper.unauthorized(Project.OBJECT_NAME, id);
	return new Project();
    }

    /**
     * Construct a JSON to be used by the Gantt dhtmlx.
     */
    @Override
    @Transactional
    public String getGanttJSON(Project proj) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return ""; // Returning empty since expecting a JSON.
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET_JSON, Project.OBJECT_NAME, proj.getId(),
		JSONTimelineGantt.class.getName());

	// Construct JSON data for the gantt chart.
	List<JSONTimelineGantt> ganttBeanList = new ArrayList<JSONTimelineGantt>();

	// Add myself.
	JSONTimelineGantt myGanttBean = new JSONTimelineGantt(proj);
	ganttBeanList.add(myGanttBean);

	// Get the gantt parent data.
	// All tasks.
	for (Task task : proj.getAssignedTasks()) {
	    JSONTimelineGantt jSONTimelineGantt = new JSONTimelineGantt(task, myGanttBean);
	    ganttBeanList.add(jSONTimelineGantt);

	    // If task has an actual duration, add it also in the Gantt.
	    Date actualDate = task.getActualDateStart();
	    double actualDuration = task.getActualDuration();
	    if (actualDate != null && actualDuration > 0) {
		JSONTimelineGantt actualGantt = new JSONTimelineGantt(task, jSONTimelineGantt,
			actualDate, actualDuration);
		ganttBeanList.add(actualGantt);
	    }
	}

	return new Gson().toJson(ganttBeanList, ArrayList.class);
    }

    /**
     * Get task status and count map.
     * 
     * @param staff
     * @return
     */
    @Transactional
    @Override
    public Map<TaskStatus, Integer> getTaskStatusCountMap(Project proj) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return new HashMap<TaskStatus, Integer>();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET_MAP, Project.OBJECT_NAME, proj.getId(),
		TaskStatus.class.getName());

	// Get summary of tasks.
	// For each task status, count how many.
	Map<TaskStatus, Integer> taskStatusMap = new HashMap<TaskStatus, Integer>();
	Map<TaskStatus, Integer> taskStatusMapSorted = new LinkedHashMap<TaskStatus, Integer>();

	// Get the tasks (children) of each parent.
	for (Task task : proj.getAssignedTasks()) {
	    int taskStatusInt = task.getStatus();
	    TaskStatus taskStatus = TaskStatus.of(taskStatusInt);
	    Integer statCount = taskStatusMap.get(taskStatus) == null ? 1
		    : taskStatusMap.get(taskStatus) + 1;
	    taskStatusMap.put(taskStatus, statCount);
	}

	// If status count is null,
	// Add it as zero.
	for (TaskStatus status : TaskStatus.class.getEnumConstants()) {
	    Integer count = taskStatusMap.get(status);
	    taskStatusMapSorted.put(status, count == null ? 0 : count);
	}

	return taskStatusMapSorted;
    }

    @Transactional
    @Override
    public String getCalendarJSON(Project proj) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return ""; // Empty, expecting JSON.
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET_JSON, Project.OBJECT_NAME, proj.getId(),
		JSONCalendarEvent.class.getName());

	// Get calendar events.
	List<JSONCalendarEvent> jSONCalendarEvents = new ArrayList<JSONCalendarEvent>();

	// Process all tasks to be included in the calendar.
	for (Task task : proj.getAssignedTasks()) {
	    // Get the start date.
	    Date startDate = task.getDateStart();
	    String start = DateUtils.formatDate(startDate, "yyyy-MM-dd");
	    String name = task.getTitle();

	    // Set values to bean.
	    JSONCalendarEvent event = new JSONCalendarEvent();
	    event.setId(Task.OBJECT_NAME + "-" + start + "-" + StringUtils.remove(name, " "));
	    event.setTitle("(Task) " + name);
	    event.setStart(start);
	    event.setClassName(CalendarEventType.TASK.css());

	    // Get the end date.
	    String end = "";
	    double duration = task.getDuration();
	    if (duration > 1) {
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.DATE, ((Double) Math.ceil(duration)).intValue());
		end = DateUtils.formatDate(c.getTime(), "yyyy-MM-dd");
		event.setEnd(end);
	    }

	    jSONCalendarEvents.add(event);
	}

	return new Gson().toJson(jSONCalendarEvents, ArrayList.class);
    }

    @Override
    @Transactional
    public String clearActualCompletionDate(Project project) {
	project.setActualCompletionDate(null);
	return update(project, null);
    }

    @Override
    @Transactional
    public String mark(long projID, int status) {
	// Get the task.
	Project project = this.projectDAO.getByID(projID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_UPDATE, Project.OBJECT_NAME, project.getId());

	// Do service.
	project.setActualCompletionDate(null);
	project.setStatus(status);
	this.projectDAO.update(project);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateMarkAs(Project.OBJECT_NAME, project.getName());
    }

    @Override
    @Transactional
    public List<AuditLog> logsDesc(long projID) {
	// Get the task.
	Project project = this.projectDAO.getByID(projID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return new ArrayList<AuditLog>();
	}

	List<AuditLog> logs = this.projectDAO.logs(projID, project.getCompany());
	for (AuditLog log : logs) {
	    log.setAuditAction(AuditAction.of(log.getAction()));
	}

	// Sort the list in descending order.
	Collections.sort(logs, new Comparator<AuditLog>() {
	    @Override
	    public int compare(AuditLog aObj, AuditLog bObj) {
		Date aStart = aObj.getDateExecuted();
		Date bStart = bObj.getDateExecuted();

		// To sort in ascending,
		// remove Not's.
		return !(aStart.before(bStart)) ? -1 : !(aStart.after(bStart)) ? 1 : 0;
	    }
	});
	return logs;
    }

}