package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.bean.CalendarEventBean;
import com.cebedo.pmsys.bean.GanttBean;
import com.cebedo.pmsys.controller.ProjectController;
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.MilestoneDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.CalendarEventType;
import com.cebedo.pmsys.enums.MilestoneStatus;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.service.ProjectService;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.service.TaskService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DateUtils;
import com.google.gson.Gson;

@Service
public class ProjectServiceImpl implements ProjectService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();

    private ProjectDAO projectDAO;
    private CompanyDAO companyDAO;
    private ProjectAuxValueRepo projectAuxValueRepo;
    private StaffService staffService;
    private TaskService taskService;
    private MilestoneDAO milestoneDAO;

    @Autowired
    @Qualifier(value = "milestoneDAO")
    public void setMilestoneDAO(MilestoneDAO milestoneDAO) {
	this.milestoneDAO = milestoneDAO;
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

    /**
     * Create Tasks from an Excel file.
     */
    @Override
    @Transactional
    public String createTasksFromExcel(MultipartFile multipartFile, Project project) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Do service.
	List<Task> tasks = this.taskService.convertExcelToTaskList(multipartFile, project);
	this.taskService.createMassTasks(tasks);

	// Log.
	this.messageHelper.send(AuditAction.ACTION_CREATE_MASS, Project.OBJECT_NAME, project.getId(),
		Task.OBJECT_NAME);

	// TODO
	return "TODO";
    }

    /**
     * Create a new project.
     */
    @Override
    @Transactional
    @Caching(evict = {
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithTasks", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithAllCollections", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":list", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":search", key = "#project.getCompany() == null ? 0 : #project.getCompany().getId()") })
    public String create(Project project) {

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
    public String createStaffFromExcel(MultipartFile multipartFile, Project proj) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Convert excel to staff objects.
	// Commit all in staff list.
	// Assign all staff to project.
	List<Staff> staffList = this.staffService.convertExcelToStaffList(multipartFile,
		proj.getCompany());
	staffList = this.staffService.createOrGetStaffInList(staffList);
	assignAllStaffToProject(proj, staffList);

	// Log.
	this.messageHelper.send(AuditAction.ACTION_CREATE_MASS, Project.OBJECT_NAME, proj.getId(),
		Staff.OBJECT_NAME);

	// TODO
	return "TODO";
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
    @Caching(evict = {
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithTasks", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithAllCollections", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":list", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":getNameByID", key = "#project.getId()"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#project.getId()"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":getByID", key = "#project.getId()"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":search", key = "#project.getCompany() == null ? 0 : #project.getCompany().getId()") })
    public String update(Project project) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
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
    @Cacheable(value = Project.OBJECT_NAME + ":list", unless = "#result.isEmpty()")
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
    @Cacheable(value = Project.OBJECT_NAME + ":getByID", key = "#id")
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
    @Caching(evict = { @CacheEvict(value = Project.OBJECT_NAME + ":getNameByID", key = "#id"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#id"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":getByID", key = "#id"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithTasks", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithAllCollections", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":list", allEntries = true) })
    public String delete(long id) {

	// Get auth and actual object.
	Project project = this.projectDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// If authorized, do actual service.
	this.projectDAO.delete(id);
	this.projectAuxValueRepo.delete(ProjectAux.constructKey(project));

	// Log.
	this.messageHelper.send(AuditAction.ACTION_DELETE, Project.OBJECT_NAME, project.getId());

	// Success response.
	return AlertBoxGenerator.SUCCESS.generateDelete(Project.OBJECT_NAME, project.getName());
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":listWithAllCollections")
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
    @Cacheable(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#id")
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

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":listWithTasks")
    public List<Project> listWithTasks() {

	AuthenticationToken token = this.authHelper.getAuth();
	this.messageHelper.send(AuditAction.ACTION_LIST, Project.OBJECT_NAME);

	// Initiate with tasks.
	if (token.isSuperAdmin()) {
	    return this.projectDAO.listWithTasks(null);
	}

	// List with partial collections from company.
	Company company = token.getCompany();
	return this.projectDAO.listWithTasks(company.getId());
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":getNameByID", key = "#projectID", unless = "#result.isEmpty()")
    public String getNameByID(long projectID) {
	String name = this.projectDAO.getNameByID(projectID);
	this.messageHelper.send(AuditAction.ACTION_GET, Project.OBJECT_NAME, projectID);
	return name;
    }

    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public void clearProjectCache(long projectID) {
	;
    }

    @CacheEvict(value = Project.OBJECT_NAME + ":listWithTasks")
    @Override
    @Transactional
    public void clearListCache() {
	;
    }

    @CacheEvict(value = Project.OBJECT_NAME + ":search", key = "#companyID == null ? 0 : #companyID")
    @Override
    @Transactional
    public void clearSearchCache(Long companyID) {
	;
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
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET_JSON, Project.OBJECT_NAME, proj.getId(),
		GanttBean.class.getName());

	// Construct JSON data for the gantt chart.
	List<GanttBean> ganttBeanList = new ArrayList<GanttBean>();

	// Add myself.
	GanttBean myGanttBean = new GanttBean(proj);
	ganttBeanList.add(myGanttBean);

	// Add all milestones and included tasks.
	for (Milestone milestone : proj.getMilestones()) {
	    GanttBean milestoneBean = new GanttBean(milestone, myGanttBean);
	    ganttBeanList.add(milestoneBean);

	    for (Task taskInMilestone : milestone.getTasks()) {
		GanttBean ganttBean = new GanttBean(taskInMilestone, milestoneBean);
		ganttBeanList.add(ganttBean);
	    }
	}

	// Get the gantt parent data.
	// All tasks without a milestone.
	for (Task task : proj.getAssignedTasks()) {

	    // Add only tasks without a milestone.
	    if (task.getMilestone() == null) {
		GanttBean ganttBean = new GanttBean(task, myGanttBean);
		ganttBeanList.add(ganttBean);
	    }
	}

	return new Gson().toJson(ganttBeanList, ArrayList.class);
    }

    /**
     * Get summary of timeline data.
     */
    @Override
    @Transactional
    public Map<String, Object> getTimelineSummaryMap(Project proj) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return new HashMap<String, Object>();
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET_MAP, Project.OBJECT_NAME, proj.getId(),
		Milestone.class.getName());

	String keyTotalTasks = ProjectController.KEY_SUMMARY_TOTAL_TASKS;
	String keyTotalMilestones = ProjectController.KEY_SUMMARY_TOTAL_MILESTONES;
	String keyTotalTasksAssigned = ProjectController.KEY_SUMMARY_TOTAL_TASKS_ASSIGNED_MILESTONES;
	String keyTotalMsNew = ProjectController.KEY_SUMMARY_TOTAL_MILESTONE_NEW;
	String keyTotalMsOngoing = ProjectController.KEY_SUMMARY_TOTAL_MILESTONE_ONGOING;
	String keyTotalMsDone = ProjectController.KEY_SUMMARY_TOTAL_MILESTONE_DONE;

	// Summary table map.
	Map<String, Integer> summaryMap = new HashMap<String, Integer>();
	Map<Milestone, Map<String, Object>> milestoneCountMap = new HashMap<Milestone, Map<String, Object>>();
	summaryMap.put(keyTotalTasks, proj.getAssignedTasks().size());
	summaryMap.put(keyTotalMilestones, proj.getMilestones().size());

	// Count all milestone statuses.
	int msNys = 0;
	int msOngoing = 0;
	int msDone = 0;

	// Add all milestones and included tasks.
	for (Milestone milestone : proj.getMilestones()) {

	    // Actual adding of tasks under this milestone.
	    Map<String, Object> milestoneStatusMap = new HashMap<String, Object>();
	    int tasksNew = 0;
	    int tasksOngoing = 0;
	    int tasksEndState = 0;

	    for (Task taskInMilestone : milestone.getTasks()) {

		// Check if task is New, Ongoing, or neither (end state).
		int taskStatusId = taskInMilestone.getStatus();
		if (taskStatusId == TaskStatus.NEW.id()) {
		    tasksNew++;
		} else if (taskStatusId == TaskStatus.ONGOING.id()) {
		    tasksOngoing++;
		} else {
		    tasksEndState++;
		}
	    }

	    // Status of this milestone.
	    MilestoneStatus msStatus;

	    // If number of tasks is equal to
	    // number of end state, milestone is finished.
	    int tasksInMilestone = milestone.getTasks().size();
	    if (tasksInMilestone == tasksEndState) {
		msDone++;
		msStatus = MilestoneStatus.DONE;
	    } else if (tasksInMilestone == tasksNew) {
		// Else if task size == new, then milestone is not yet started.
		msNys++;
		msStatus = MilestoneStatus.NOT_YET_STARTED;
	    } else {
		// Else, it's still ongoing.
		msOngoing++;
		msStatus = MilestoneStatus.ONGOING;
	    }

	    // Add collected data for milestone status and
	    // corresponding count.
	    milestoneStatusMap.put("Status", msStatus);
	    milestoneStatusMap.put(MilestoneStatus.NOT_YET_STARTED.label(), tasksNew);
	    milestoneStatusMap.put(MilestoneStatus.ONGOING.label(), tasksOngoing);
	    milestoneStatusMap.put(MilestoneStatus.DONE.label(), tasksEndState);
	    milestoneCountMap.put(milestone, milestoneStatusMap);

	    // Get number of tasks assigned to milestones.
	    summaryMap.put(keyTotalTasksAssigned, summaryMap.get(keyTotalTasksAssigned) == null ? 1
		    : summaryMap.get(keyTotalTasksAssigned) + milestone.getTasks().size());
	}

	// Add collected data.
	summaryMap.put(keyTotalMsNew, msNys);
	summaryMap.put(keyTotalMsOngoing, msOngoing);
	summaryMap.put(keyTotalMsDone, msDone);

	// Organize the two maps, before returning.
	Map<String, Object> milestoneSummaryMap = new HashMap<String, Object>();
	milestoneSummaryMap
		.put(ProjectController.ATTR_TIMELINE_MILESTONE_SUMMARY_MAP, milestoneCountMap);
	milestoneSummaryMap.put(ProjectController.ATTR_TIMELINE_SUMMARY_MAP, summaryMap);

	return milestoneSummaryMap;
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
	    Integer statCount = taskStatusMap.get(taskStatus) == null ? 1 : taskStatusMap
		    .get(taskStatus) + 1;
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
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET_JSON, Project.OBJECT_NAME, proj.getId(),
		CalendarEventBean.class.getName());

	// Get calendar events.
	List<CalendarEventBean> calendarEvents = new ArrayList<CalendarEventBean>();

	// Process all tasks to be included in the calendar.
	for (Task task : proj.getAssignedTasks()) {
	    // Get the start date.
	    Date startDate = task.getDateStart();
	    String start = DateUtils.formatDate(startDate, "yyyy-MM-dd");
	    String name = task.getTitle();

	    // Set values to bean.
	    CalendarEventBean event = new CalendarEventBean();
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

	    calendarEvents.add(event);
	}

	return new Gson().toJson(calendarEvents, ArrayList.class);
    }

    @Transactional
    @Override
    public String deleteProgramOfWorks(Project project) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Do service.
	this.taskService.deleteAllTasksByProject(project.getId());
	this.milestoneDAO.deleteAllByProject(project.getId());

	// Log.
	this.messageHelper.send(AuditAction.ACTION_DELETE_ALL, Project.OBJECT_NAME, project.getId(),
		Task.OBJECT_NAME + "+" + Milestone.OBJECT_NAME);

	// TODO
	return "TODO";
    }

}