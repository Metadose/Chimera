package com.cebedo.pmsys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.TaskGanttBean;
import com.cebedo.pmsys.controller.ProjectController;
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.domain.Notification;
import com.cebedo.pmsys.enums.MilestoneStatus;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;
import com.cebedo.pmsys.repository.NotificationZSetRepo;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.google.gson.Gson;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static Logger logger = Logger.getLogger(Project.OBJECT_NAME);
    private AuthHelper authHelper = new AuthHelper();
    private LogHelper logHelper = new LogHelper();
    private MessageHelper messageHelper = new MessageHelper();

    private ProjectDAO projectDAO;
    private CompanyDAO companyDAO;
    private NotificationZSetRepo notificationZSetRepo;
    private PayrollService payrollService;

    public void setPayrollService(PayrollService s) {
	this.payrollService = s;
    }

    public void setNotificationZSetRepo(
	    NotificationZSetRepo notificationZSetRepo) {
	this.notificationZSetRepo = notificationZSetRepo;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public void setCompanyDAO(CompanyDAO companyDAO) {
	this.companyDAO = companyDAO;
    }

    @Override
    @Transactional
    @Caching(evict = {
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithTasks", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithAllCollections", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":list", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":search", key = "#project.getCompany() == null ? 0 : #project.getCompany().getId()") })
    public void create(Project project) {
	// Send messages/notifications.
	// Use message brokers as instructions.
	// LIKE send message to logger to log.
	// AND auditor to audit.
	// Fire up the message so that it would go parallel with the service
	// below.
	AuthenticationToken auth = this.authHelper.getAuth();

	String notifTxt = auth.getStaff() == null ? auth.getUser()
		.getUsername() : auth.getStaff().getFullName()
		+ " created a new project " + project.getName();

	Notification notification = new Notification("Test Content", auth
		.getUser().getId());

	this.notificationZSetRepo.add(notification);
	// this.messageHelper.constructSendMessage(project,
	// AuditLog.ACTION_CREATE,
	// "Creating project: " + project.getName());

	// Do service.
	Company authCompany = auth.getCompany();
	project.setCompany(authCompany);
	this.projectDAO.create(project);
    }

    @Override
    @Transactional
    @Caching(evict = {
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithTasks", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithAllCollections", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":list", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":getNameByID", key = "#project.getId()"),
	    @CacheEvict(value = Project.OBJECT_NAME
		    + ":getByIDWithAllCollections", key = "#project.getId()"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":getByID", key = "#project.getId()"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":search", key = "#project.getCompany() == null ? 0 : #project.getCompany().getId()") })
    public void update(Project project) {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (this.authHelper.isActionAuthorized(project)) {
	    // Audit and log.
	    this.messageHelper.constructSendMessage(
		    project,
		    AuditLog.ACTION_UPDATE,
		    "Updating project: " + project.getId() + " = "
			    + project.getName());

	    // Actual service.
	    Company company = this.companyDAO.getCompanyByObjID(
		    Project.TABLE_NAME, Project.COLUMN_PRIMARY_KEY,
		    project.getId());
	    project.setCompany(company);
	    this.projectDAO.update(project);
	} else {
	    logger.warn(this.logHelper.generateLogMessage(auth,
		    "Not authorized to update project: " + project.getId()
			    + " = " + project.getName()));
	}
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":list", unless = "#result.isEmpty()")
    public List<Project> list() {
	AuthenticationToken token = this.authHelper.getAuth();

	if (token.isSuperAdmin()) {
	    logger.info(this.logHelper.generateLogMessage(token,
		    "Listing all projects as super admin."));
	    return this.projectDAO.list(null);
	}
	Company company = token.getCompany();
	logger.info(this.logHelper.generateLogMessage(token,
		"Listing all projects from company: " + company.getId() + " = "
			+ company.getName()));
	return this.projectDAO.list(company.getId());
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":getByID", key = "#id")
    public Project getByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(project)) {
	    logger.info(this.logHelper.generateLogMessage(auth,
		    "Getting project: " + id + " = " + project.getName()));
	    return project;
	}
	logger.warn(this.logHelper.generateLogMessage(
		auth,
		"Not authorized to get project: " + id + " = "
			+ project.getName()));
	return new Project();
    }

    @Override
    @Transactional
    @Caching(evict = {
	    @CacheEvict(value = Project.OBJECT_NAME + ":getNameByID", key = "#id"),
	    @CacheEvict(value = Project.OBJECT_NAME
		    + ":getByIDWithAllCollections", key = "#id"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":getByID", key = "#id"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithTasks", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":listWithAllCollections", allEntries = true),
	    @CacheEvict(value = Project.OBJECT_NAME + ":list", allEntries = true) })
    public void delete(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.constructSendMessage(project,
		    AuditLog.ACTION_DELETE, "Deleting project: " + id + " = "
			    + project.getName());
	    this.projectDAO.delete(id);
	} else {
	    logger.warn(this.logHelper.generateLogMessage(
		    auth,
		    "Not authorized to delete project: " + id + " = "
			    + project.getName()));
	}
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":listWithAllCollections")
    public List<Project> listWithAllCollections() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    logger.info(this.logHelper.generateLogMessage(token,
		    "Listing projects with all collections as super admin."));
	    return this.projectDAO.listWithAllCollections(null);
	}
	Company company = token.getCompany();
	logger.info(this.logHelper.generateLogMessage(token,
		"Listing projects with all collections from company: "
			+ company.getId() + " = " + company.getName()));
	return this.projectDAO.listWithAllCollections(company.getId());
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#id")
    public Project getByIDWithAllCollections(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();

	// TODO 86400000 is 24 hours.
	Set<Notification> notifs = this.notificationZSetRepo.rangeByScore(
		Notification.constructKey(auth.getUser().getId(), false),
		System.currentTimeMillis() - 86400000,
		System.currentTimeMillis());
	Project project = this.projectDAO.getByIDWithAllCollections(id);

	if (this.authHelper.isActionAuthorized(project)) {
	    logger.info(this.logHelper.generateLogMessage(auth,
		    "Getting project with all collections: " + project.getId()
			    + " = " + project.getName()));
	    return project;
	}
	logger.warn(this.logHelper.generateLogMessage(auth,
		"Not authorized to get project with all collections: "
			+ project.getId() + " = " + project.getName()));
	return new Project();
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":listWithTasks")
    public List<Project> listWithTasks() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    logger.info(this.logHelper
		    .generateLogMessage(token,
			    "Listing all projects (initiated with tasks) as super admin."));
	    return this.projectDAO.listWithTasks(null);
	}
	Company company = token.getCompany();
	logger.info(this.logHelper.generateLogMessage(token,
		"Listing all projects (initiated with tasks) from company: "
			+ company.getId() + " = " + company.getName()));
	return this.projectDAO.listWithTasks(company.getId());
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":getNameByID", key = "#projectID", unless = "#result.isEmpty()")
    public String getNameByID(long projectID) {
	AuthenticationToken token = this.authHelper.getAuth();
	String name = this.projectDAO.getNameByID(projectID);
	logger.info(this.logHelper.generateLogMessage(token,
		"Getting name of project: " + projectID + " = " + name));
	return name;
    }

    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public void clearProjectCache(long projectID) {
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
	List<TaskGanttBean> ganttBeanList = new ArrayList<TaskGanttBean>();

	// Add myself.
	TaskGanttBean myGanttBean = new TaskGanttBean(proj);
	ganttBeanList.add(myGanttBean);

	// Add all milestones and included tasks.
	for (Milestone milestone : proj.getMilestones()) {
	    TaskGanttBean milestoneBean = new TaskGanttBean(milestone,
		    myGanttBean);
	    ganttBeanList.add(milestoneBean);

	    for (Task taskInMilestone : milestone.getTasks()) {
		TaskGanttBean ganttBean = new TaskGanttBean(taskInMilestone,
			milestoneBean);
		ganttBeanList.add(ganttBean);
	    }
	}

	// Get the gantt parent data.
	// All tasks without a milestone.
	for (Task task : proj.getAssignedTasks()) {

	    // Add only tasks without a milestone.
	    if (task.getMilestone() == null) {
		TaskGanttBean ganttBean = new TaskGanttBean(task, myGanttBean);
		ganttBeanList.add(ganttBean);
	    }
	}

	return new Gson().toJson(ganttBeanList, ArrayList.class);
    }

    /**
     * Get summary of milestone data.
     */
    @Override
    @Transactional
    public Map<String, Object> getMilestoneSummaryMap(Project proj) {
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
		msStatus = MilestoneStatus.NEW;
	    } else {
		// Else, it's still ongoing.
		msOngoing++;
		msStatus = MilestoneStatus.ONGOING;
	    }

	    // Add collected data for milestone status and
	    // corresponding count.
	    milestoneStatusMap.put("Status", msStatus);
	    milestoneStatusMap.put(MilestoneStatus.NEW.label(), tasksNew);
	    milestoneStatusMap.put(MilestoneStatus.ONGOING.label(),
		    tasksOngoing);
	    milestoneStatusMap.put(MilestoneStatus.DONE.label(), tasksEndState);
	    milestoneCountMap.put(milestone, milestoneStatusMap);

	    // Get number of tasks assigned to milestones.
	    summaryMap.put(keyTotalTasksAssigned,
		    summaryMap.get(keyTotalTasksAssigned) == null ? 1
			    : summaryMap.get(keyTotalTasksAssigned)
				    + milestone.getTasks().size());
	}

	// Add collected data.
	summaryMap.put(keyTotalMsNew, msNys);
	summaryMap.put(keyTotalMsOngoing, msOngoing);
	summaryMap.put(keyTotalMsDone, msDone);

	// Organize the two maps, before returning.
	Map<String, Object> milestoneSummaryMap = new HashMap<String, Object>();
	milestoneSummaryMap.put(
		ProjectController.ATTR_TIMELINE_MILESTONE_SUMMARY_MAP,
		milestoneCountMap);
	milestoneSummaryMap.put(ProjectController.ATTR_TIMELINE_SUMMARY_MAP,
		summaryMap);

	return milestoneSummaryMap;
    }

    /**
     * Get map of payrolls.
     */
    @Override
    @Transactional
    public Map<String, Object> getPayrollMap(Project proj) {
	Map<String, Object> payrollMaps = new HashMap<String, Object>();

	// TODO Speed up performance!
	// FIXME Since beginning of time 'til now.
	Date min = new Date(0);
	Date max = new Date(System.currentTimeMillis());

	// Payroll maps.
	Map<Team, Map<Staff, String>> teamPayrollMap = new HashMap<Team, Map<Staff, String>>();
	Map<ManagerAssignment, String> managerPayrollMap = new HashMap<ManagerAssignment, String>();

	// Wage for teams.
	Map<Long, String> computedMap = new HashMap<Long, String>();

	// Loop through all the teams.
	for (Team team : proj.getAssignedTeams()) {
	    Map<Staff, String> staffPayrollMap = new HashMap<Staff, String>();

	    for (Staff member : team.getMembers()) {
		long memberID = member.getId();

		// If a staff has already been computed before,
		// don't compute again.
		if (computedMap.containsKey(memberID)) {
		    staffPayrollMap.put(member,
			    "Check " + computedMap.get(memberID));
		    continue;
		}

		// Add to map
		// and add to computed list.
		staffPayrollMap.put(member, String.valueOf(this.payrollService
			.getTotalWageOfStaffInRange(member, min, max)));
		computedMap.put(memberID, "Team " + team.getName());
	    }

	    // Add to team list.
	    teamPayrollMap.put(team, staffPayrollMap);
	}

	// Wage for managers.
	for (ManagerAssignment assignment : proj.getManagerAssignments()) {
	    Staff manager = assignment.getManager();
	    long managerID = manager.getId();
	    // If a staff has already been computed before,
	    // don't compute again.
	    if (computedMap.containsKey(managerID)) {
		managerPayrollMap.put(assignment,
			"Check " + computedMap.get(managerID));
		continue;
	    }

	    // Get wage then add to map.
	    managerPayrollMap.put(assignment, String
		    .valueOf(this.payrollService.getTotalWageOfStaffInRange(
			    manager, min, max)));
	    computedMap.put(managerID, assignment.getProjectPosition());
	}

	payrollMaps
		.put(ProjectController.ATTR_PAYROLL_MAP_TEAM, teamPayrollMap);
	payrollMaps.put(ProjectController.ATTR_PAYROLL_MAP_MANAGER,
		managerPayrollMap);

	return payrollMaps;
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
}