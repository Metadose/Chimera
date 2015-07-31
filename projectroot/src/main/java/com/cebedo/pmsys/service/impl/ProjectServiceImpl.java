package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.CalendarEventType;
import com.cebedo.pmsys.enums.MilestoneStatus;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;
import com.cebedo.pmsys.repository.ProjectAuxValueRepo;
import com.cebedo.pmsys.service.ProjectService;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DateUtils;
import com.google.gson.Gson;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static Logger logger = Logger.getLogger(Project.OBJECT_NAME);
    private AuthHelper authHelper = new AuthHelper();
    private LogHelper logHelper = new LogHelper();
    private MessageHelper messageHelper = new MessageHelper();

    private ProjectDAO projectDAO;
    private CompanyDAO companyDAO;
    private ProjectAuxValueRepo projectAuxValueRepo;
    private StaffService staffService;

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
	AuthenticationToken auth = this.authHelper.getAuth();

	// Construct and send system message.
	this.messageHelper.sendAction(AuditAction.CREATE, project);

	// Do service.
	Company authCompany = auth.getCompany();
	project.setCompany(authCompany);
	this.projectDAO.create(project);
	this.projectAuxValueRepo.set(new ProjectAux(project));

	// Return success response.
	return AlertBoxGenerator.SUCCESS.generateCreate(Project.OBJECT_NAME, project.getName());
    }

    @Override
    @Transactional
    public String createAllStaffFromExcelAssignToProject(MultipartFile multipartFile, Project proj) {
	// Convert excel to staff objects.
	List<Staff> staffList = this.staffService.convertExcelToStaffList(multipartFile,
		proj.getCompany());

	// Commit all in staff list.
	this.staffService.createAllStaffInList(staffList);

	// Assign all staff to project.
	assignAllStaffToProject(proj, staffList);

	return "TODO";
    }

    private void assignAllStaffToProject(Project proj, List<Staff> staffList) {
	Set<Staff> projectStaff = proj.getAssignedStaff();
	projectStaff.addAll(staffList);
	proj.setAssignedStaff(projectStaff);
	this.projectDAO.update(proj);
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
	AuthenticationToken auth = this.authHelper.getAuth();
	String response = "";

	if (this.authHelper.isActionAuthorized(project)) {

	    // Construct and send system message.
	    this.messageHelper.sendAction(AuditAction.UPDATE, project);

	    // Actual service.
	    Company company = this.companyDAO.getCompanyByObjID(Project.TABLE_NAME,
		    Project.COLUMN_PRIMARY_KEY, project.getId());
	    project.setCompany(company);
	    this.projectDAO.update(project);

	    // Response for the user.
	    response = AlertBoxGenerator.SUCCESS.generateUpdate(Project.OBJECT_NAME, project.getName());
	} else {
	    // Log a warning.
	    logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE, Project.OBJECT_NAME,
		    project.getId(), project.getName()));

	    // Construct failed response
	    response = AlertBoxGenerator.FAILED.generateUpdate(Project.OBJECT_NAME, project.getName());
	}
	return response;
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":list", unless = "#result.isEmpty()")
    public List<Project> list() {
	AuthenticationToken token = this.authHelper.getAuth();

	if (token.isSuperAdmin()) {
	    // List as super admin.
	    logger.info(this.logHelper.logListAsSuperAdmin(token, Project.OBJECT_NAME));
	    return this.projectDAO.list(null);
	}

	// List as not a super admin.
	Company company = token.getCompany();
	logger.info(this.logHelper.logListFromCompany(token, Project.OBJECT_NAME, company));
	return this.projectDAO.list(company.getId());
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":getByID", key = "#id")
    public Project getByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(project)) {
	    // Log the action.
	    logger.info(this.logHelper.logGetObject(auth, Project.OBJECT_NAME, id, project.getName()));
	    return project;
	}

	// Create a warning log.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET, Project.OBJECT_NAME, id,
		project.getName()));
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
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(id);
	String response = "";

	if (this.authHelper.isActionAuthorized(project)) {

	    // Construct and send system message.
	    this.messageHelper.sendAction(AuditAction.DELETE, project);

	    // If authorized, do actual service.
	    this.projectDAO.delete(id);
	    this.projectAuxValueRepo.delete(ProjectAux.constructKey(project));

	    // Success response.
	    response = AlertBoxGenerator.SUCCESS.generateDelete(Project.OBJECT_NAME, project.getName());
	} else {
	    // If not, log as warning.
	    logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE, Project.OBJECT_NAME,
		    id, project.getName()));

	    // Failed response.
	    response = AlertBoxGenerator.FAILED.generateDelete(Project.OBJECT_NAME, project.getName());
	}
	return response;
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":listWithAllCollections")
    public List<Project> listWithAllCollections() {
	AuthenticationToken token = this.authHelper.getAuth();

	if (token.isSuperAdmin()) {
	    // Log the action.
	    // And return the list.
	    logger.info(this.logHelper.logListWithCollectionsAsSuperAdmin(token, Project.OBJECT_NAME));
	    return this.projectDAO.listWithAllCollections(null);
	}

	// Log the action.
	// Return the list.
	Company company = token.getCompany();
	logger.info(this.logHelper
		.logListWithCollectionsFromCompany(token, Project.OBJECT_NAME, company));

	return this.projectDAO.listWithAllCollections(company.getId());
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#id")
    public Project getByIDWithAllCollections(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();

	Long companyID = auth.getCompany() == null ? 0 : auth.getCompany().getId();
	Project project = this.projectDAO.getByIDWithAllCollections(id);

	if (this.authHelper.isActionAuthorized(project)) {
	    // Log the action.
	    // Then do the action.
	    logger.info(this.logHelper.logGetObjectWithAllCollections(auth, Project.OBJECT_NAME, id,
		    project.getName()));
	    return project;
	}

	// Log then return empty object.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET_WITH_COLLECTIONS,
		Project.OBJECT_NAME, id, project.getName()));
	return new Project();
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":listWithTasks")
    public List<Project> listWithTasks() {
	AuthenticationToken token = this.authHelper.getAuth();

	// Initiate with tasks.
	if (token.isSuperAdmin()) {
	    logger.info(this.logHelper.logListPartialCollectionsAsSuperAdmin(token, Project.OBJECT_NAME,
		    Task.OBJECT_NAME));
	    return this.projectDAO.listWithTasks(null);
	}

	// List with partial collections from company.
	Company company = token.getCompany();
	logger.info(this.logHelper.logListPartialCollectionsFromCompany(token, Project.OBJECT_NAME,
		Task.OBJECT_NAME, company));

	return this.projectDAO.listWithTasks(company.getId());
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":getNameByID", key = "#projectID", unless = "#result.isEmpty()")
    public String getNameByID(long projectID) {
	AuthenticationToken token = this.authHelper.getAuth();
	String name = this.projectDAO.getNameByID(projectID);
	logger.info(this.logHelper.logGetProperty(token, Project.OBJECT_NAME, Project.PROPERTY_NAME,
		projectID, name));
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
	    int duration = task.getDuration();
	    if (duration > 1) {
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		c.add(Calendar.DATE, duration);
		end = DateUtils.formatDate(c.getTime(), "yyyy-MM-dd");
		event.setEnd(end);
	    }

	    calendarEvents.add(event);
	}

	return new Gson().toJson(calendarEvents, ArrayList.class);
    }

    @Transactional
    @Override
    public List<Staff> getAllStaff(Project proj) {
	// Get all managers in this project.
	List<Staff> managers = new ArrayList<Staff>();
	for (ManagerAssignment managerAssignment : proj.getManagerAssignments()) {
	    managers.add(managerAssignment.getManager());
	}

	// Get all staff in this project.
	List<Staff> allStaff = new ArrayList<Staff>();
	allStaff.addAll(managers);
	for (Task task : proj.getAssignedTasks()) {
	    allStaff.addAll(task.getStaff());
	}

	for (Team team : proj.getAssignedTeams()) {
	    allStaff.addAll(team.getMembers());
	}
	return allStaff;
    }

    @Transactional
    @Override
    public List<Staff> getAllManagers(Project proj) {
	List<Staff> managers = new ArrayList<Staff>();
	for (ManagerAssignment managerAssignment : proj.getManagerAssignments()) {
	    managers.add(managerAssignment.getManager());
	}
	return managers;
    }

    @Transactional
    @Override
    public List<Staff> getAllManagersWithUsers(Project proj) {
	List<Staff> managers = new ArrayList<Staff>();
	for (ManagerAssignment managerAssignment : proj.getManagerAssignments()) {
	    if (managerAssignment.getManager().getUser() == null) {
		continue;
	    }
	    managers.add(managerAssignment.getManager());
	}
	return managers;
    }

    @Transactional
    @Override
    public Set<ManagerAssignment> getAllManagersAssignmentsWithUsers(Project proj) {
	Set<ManagerAssignment> managers = new HashSet<ManagerAssignment>();
	for (ManagerAssignment managerAssignment : proj.getManagerAssignments()) {
	    if (managerAssignment.getManager().getUser() == null) {
		continue;
	    }
	    managers.add(managerAssignment);
	}
	return managers;
    }

}