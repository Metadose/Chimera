package com.cebedo.pmsys.service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.CalendarEventBean;
import com.cebedo.pmsys.bean.GanttBean;
import com.cebedo.pmsys.bean.TreeGridRowBean;
import com.cebedo.pmsys.controller.ProjectController;
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.domain.Notification;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.CSSClass;
import com.cebedo.pmsys.enums.CalendarEventType;
import com.cebedo.pmsys.enums.MilestoneStatus;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.DataStructHelper;
import com.cebedo.pmsys.helper.DateHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Delivery;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Reminder;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;
import com.cebedo.pmsys.repository.NotificationZSetRepo;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxFactory;
import com.google.gson.Gson;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final String IDENTIFIER_ALREADY_EXISTS = "Check ";
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

	// Return success response.
	return AlertBoxFactory.SUCCESS.generateCreate(Project.OBJECT_NAME,
		project.getName());
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
	    @CacheEvict(value = Project.OBJECT_NAME
		    + ":getByIDWithAllCollections", key = "#project.getId()"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":getByID", key = "#project.getId()"),
	    @CacheEvict(value = Project.OBJECT_NAME + ":search", key = "#project.getCompany() == null ? 0 : #project.getCompany().getId()") })
    public String update(Project project) {
	AuthenticationToken auth = this.authHelper.getAuth();
	String response = "";

	if (this.authHelper.isActionAuthorized(project)) {

	    // Construct and send system message.
	    this.messageHelper.sendAction(AuditAction.UPDATE, project);

	    // Actual service.
	    Company company = this.companyDAO.getCompanyByObjID(
		    Project.TABLE_NAME, Project.COLUMN_PRIMARY_KEY,
		    project.getId());
	    project.setCompany(company);
	    this.projectDAO.update(project);

	    // Response for the user.
	    response = AlertBoxFactory.SUCCESS.generateUpdate(
		    Project.OBJECT_NAME, project.getName());
	} else {
	    // Log a warning.
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.UPDATE, Project.OBJECT_NAME, project.getId(),
		    project.getName()));

	    // Construct failed response
	    response = AlertBoxFactory.FAILED.generateUpdate(
		    Project.OBJECT_NAME, project.getName());
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
	    logger.info(this.logHelper.logListAsSuperAdmin(token,
		    Project.OBJECT_NAME));
	    return this.projectDAO.list(null);
	}

	// List as not a super admin.
	Company company = token.getCompany();
	logger.info(this.logHelper.logListFromCompany(token,
		Project.OBJECT_NAME, company));
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
	    logger.info(this.logHelper.logGetObject(auth, Project.OBJECT_NAME,
		    id, project.getName()));
	    return project;
	}

	// Create a warning log.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET,
		Project.OBJECT_NAME, id, project.getName()));
	return new Project();
    }

    /**
     * Delete a project.
     */
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

	    // Success response.
	    response = AlertBoxFactory.SUCCESS.generateDelete(
		    Project.OBJECT_NAME, project.getName());
	} else {
	    // If not, log as warning.
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.DELETE, Project.OBJECT_NAME, id,
		    project.getName()));

	    // Failed response.
	    response = AlertBoxFactory.FAILED.generateDelete(
		    Project.OBJECT_NAME, project.getName());
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
	    logger.info(this.logHelper.logListWithCollectionsAsSuperAdmin(
		    token, Project.OBJECT_NAME));
	    return this.projectDAO.listWithAllCollections(null);
	}

	// Log the action.
	// Return the list.
	Company company = token.getCompany();
	logger.info(this.logHelper.logListWithCollectionsFromCompany(token,
		Project.OBJECT_NAME, company));

	return this.projectDAO.listWithAllCollections(company.getId());
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#id")
    public Project getByIDWithAllCollections(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();

	// TODO 86400000 is 24 hours.
	Long companyID = auth.getCompany() == null ? 0 : auth.getCompany()
		.getId();
	Set<Notification> notifs = this.notificationZSetRepo.rangeByScore(
		Notification.constructKey(companyID, auth.getUser().getId(),
			false), System.currentTimeMillis() - 86400000, System
			.currentTimeMillis());
	Project project = this.projectDAO.getByIDWithAllCollections(id);

	if (this.authHelper.isActionAuthorized(project)) {
	    // Log the action.
	    // Then do the action.
	    logger.info(this.logHelper.logGetObjectWithAllCollections(auth,
		    Project.OBJECT_NAME, id, project.getName()));
	    return project;
	}

	// Log then return empty object.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.GET_WITH_COLLECTIONS, Project.OBJECT_NAME, id,
		project.getName()));
	return new Project();
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":listWithTasks")
    public List<Project> listWithTasks() {
	AuthenticationToken token = this.authHelper.getAuth();

	// Initiate with tasks.
	if (token.isSuperAdmin()) {
	    logger.info(this.logHelper.logListPartialCollectionsAsSuperAdmin(
		    token, Project.OBJECT_NAME, Task.OBJECT_NAME));
	    return this.projectDAO.listWithTasks(null);
	}

	// List with partial collections from company.
	Company company = token.getCompany();
	logger.info(this.logHelper.logListPartialCollectionsFromCompany(token,
		Project.OBJECT_NAME, Task.OBJECT_NAME, company));

	return this.projectDAO.listWithTasks(company.getId());
    }

    @Override
    @Transactional
    @Cacheable(value = Project.OBJECT_NAME + ":getNameByID", key = "#projectID", unless = "#result.isEmpty()")
    public String getNameByID(long projectID) {
	AuthenticationToken token = this.authHelper.getAuth();
	String name = this.projectDAO.getNameByID(projectID);
	logger.info(this.logHelper.logGetProperty(token, Project.OBJECT_NAME,
		Project.PROPERTY_NAME, projectID, name));
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
		GanttBean ganttBean = new GanttBean(taskInMilestone,
			milestoneBean);
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

    @SuppressWarnings("unchecked")
    private String constructPayrollJSON(Map<String, Object> payrollMap,
	    Project proj) {

	// Team map.
	Map<Team, Map<Staff, String>> teamPayrollMap = (Map<Team, Map<Staff, String>>) payrollMap
		.get(ProjectController.ATTR_PAYROLL_MAP_TEAM);

	// Manager map.
	Map<ManagerAssignment, String> managerPayrollMap = (Map<ManagerAssignment, String>) payrollMap
		.get(ProjectController.ATTR_PAYROLL_MAP_MANAGER);

	// Summary groups.
	Map<Team, Double> teamGroup = (Map<Team, Double>) payrollMap
		.get(ProjectController.ATTR_PAYROLL_GROUP_TEAM);
	Map<String, Double> summaryGroup = (Map<String, Double>) payrollMap
		.get(ProjectController.ATTR_PAYROLL_SUMMARY_MAP);

	// Currency formatter.
	NumberFormat df = NumberFormat.getCurrencyInstance();
	DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	dfs.setCurrencySymbol("&#8369;");
	dfs.setGroupingSeparator('.');
	dfs.setMonetaryDecimalSeparator('.');
	((DecimalFormat) df).setDecimalFormatSymbols(dfs);

	// Summary details.
	double managersTotal = summaryGroup
		.get(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_MANAGER);
	String managersTotalStr = df.format(managersTotal);
	double teamsTotal = summaryGroup
		.get(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_TEAM);
	String teamsTotalStr = df.format(teamsTotal);
	double overallTotal = summaryGroup
		.get(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_OVERALL);
	String overallTotalStr = df.format(overallTotal);

	// Add the mother bean.
	Random randomno = new Random();
	List<TreeGridRowBean> treeGrid = new ArrayList<TreeGridRowBean>();
	long motherPKey = Math.abs(randomno.nextLong());
	TreeGridRowBean motherBean = new TreeGridRowBean(motherPKey, -1,
		CSSClass.SUCCESS.getSpanHTML("PROJECT") + "&nbsp;"
			+ proj.getName(), overallTotalStr);
	treeGrid.add(motherBean);

	// Add header beans.
	long headerManagerPKey = Math.abs(randomno.nextLong());
	TreeGridRowBean headerManagerBean = new TreeGridRowBean(
		headerManagerPKey, motherPKey,
		CSSClass.PRIMARY.getSpanHTML("GROUP") + "&nbsp;Managers",
		managersTotalStr);
	treeGrid.add(headerManagerBean);

	long headerTeamPKey = Math.abs(randomno.nextLong());
	TreeGridRowBean headerTeamBean = new TreeGridRowBean(headerTeamPKey,
		motherPKey, CSSClass.PRIMARY.getSpanHTML("GROUP")
			+ "&nbsp;Teams", teamsTotalStr);
	treeGrid.add(headerTeamBean);

	// Loop through managers.
	for (ManagerAssignment managerAssignment : managerPayrollMap.keySet()) {

	    // Get details.
	    Staff manager = managerAssignment.getManager();
	    long rowPKey = Math.abs(randomno.nextLong());
	    String rowName = CSSClass.DEFAULT.getSpanHTML("STAFF") + "&nbsp;"
		    + manager.getFullName();
	    String value = managerPayrollMap.get(managerAssignment);
	    String rowValue = value.contains(IDENTIFIER_ALREADY_EXISTS) ? "<i>("
		    + value + ")</i>"
		    : df.format(Double.valueOf(value));

	    // Add to bean.
	    TreeGridRowBean rowBean = new TreeGridRowBean(rowPKey,
		    headerManagerPKey, rowName, rowValue);
	    treeGrid.add(rowBean);
	}

	// Loop through teams.
	for (Team team : teamPayrollMap.keySet()) {

	    Map<Staff, String> staffMap = teamPayrollMap.get(team);
	    if (staffMap.keySet().isEmpty()) {
		continue;
	    }

	    // Get details.
	    long teamPKey = Math.abs(randomno.nextLong());
	    double thisTeamTotal = teamGroup.get(team);
	    String thisTeamTotalStr = df.format(thisTeamTotal);

	    // Add to bean.
	    TreeGridRowBean teamBean = new TreeGridRowBean(teamPKey,
		    headerTeamPKey, CSSClass.INFO.getSpanHTML("TEAM")
			    + "&nbsp;" + team.getName(), thisTeamTotalStr);
	    treeGrid.add(teamBean);

	    // Add all staff inside team.
	    for (Staff staff : staffMap.keySet()) {

		// Get details.
		long rowPKey = Math.abs(randomno.nextLong());
		String rowName = CSSClass.DEFAULT.getSpanHTML("STAFF")
			+ "&nbsp;" + staff.getFullName();
		String value = staffMap.get(staff);
		String rowValue = value.contains(IDENTIFIER_ALREADY_EXISTS) ? "<i>("
			+ value + ")</i>"
			: df.format(Double.valueOf(value));

		// Add to bean.
		TreeGridRowBean rowBean = new TreeGridRowBean(rowPKey,
			teamPKey, rowName, rowValue);
		treeGrid.add(rowBean);
	    }
	}

	return new Gson().toJson(treeGrid, ArrayList.class);
    }

    /**
     * Get map of payrolls.
     */
    @Override
    @Transactional
    public Map<String, Object> getComputedPayrollMap(Project proj, Date min,
	    Date max, ProjectPayroll projectPayroll) {
	Map<String, Object> payrollMaps = new HashMap<String, Object>();
	List<Long> staffIDsToCompute = DataStructHelper
		.convertArrayToList(projectPayroll.getStaffIDs());

	// Payroll maps.
	Map<Team, Double> teamGroup = new HashMap<Team, Double>();
	Map<String, Double> summaryGroup = new HashMap<String, Double>();
	Map<Team, Map<Staff, String>> teamPayrollMap = new HashMap<Team, Map<Staff, String>>();
	Map<ManagerAssignment, String> managerPayrollMap = new HashMap<ManagerAssignment, String>();
	double managersTotal = 0;
	double teamsTotal = 0;

	// Wage for teams.
	Map<Long, String> computedMap = new HashMap<Long, String>();

	// Loop through all the teams.
	for (Team team : proj.getAssignedTeams()) {
	    Map<Staff, String> staffPayrollMap = new HashMap<Staff, String>();
	    double thisTeamTotal = 0;

	    for (Staff member : team.getMembers()) {
		long memberID = member.getId();

		if (!staffIDsToCompute.contains(memberID)) {
		    continue;
		}

		// If a staff has already been computed before,
		// don't compute again.
		if (computedMap.containsKey(memberID)) {
		    staffPayrollMap.put(member, IDENTIFIER_ALREADY_EXISTS
			    + computedMap.get(memberID));
		    continue;
		}

		// Add to map
		// and add to computed list.
		double totalWageOfStaff = this.payrollService
			.getTotalWageOfStaffInRange(member, min, max);
		thisTeamTotal += totalWageOfStaff;
		staffPayrollMap.put(member, String.valueOf(totalWageOfStaff));
		computedMap.put(memberID, "Team " + team.getName());
	    }

	    // Add to team list.
	    teamsTotal += thisTeamTotal;
	    teamGroup.put(team, thisTeamTotal);
	    teamPayrollMap.put(team, staffPayrollMap);
	}

	// Wage for managers.
	for (ManagerAssignment assignment : proj.getManagerAssignments()) {
	    Staff manager = assignment.getManager();
	    long managerID = manager.getId();

	    if (!staffIDsToCompute.contains(managerID)) {
		continue;
	    }

	    // If a staff has already been computed before,
	    // don't compute again.
	    if (computedMap.containsKey(managerID)) {
		managerPayrollMap.put(assignment, IDENTIFIER_ALREADY_EXISTS
			+ computedMap.get(managerID));
		continue;
	    }

	    // Get wage then add to map.
	    double managerWageTotal = this.payrollService
		    .getTotalWageOfStaffInRange(manager, min, max);
	    managersTotal += managerWageTotal;
	    managerPayrollMap.put(assignment, String.valueOf(managerWageTotal));
	    computedMap.put(managerID, assignment.getProjectPosition());
	}

	// Add summary details.
	summaryGroup.put(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_OVERALL,
		teamsTotal + managersTotal);
	summaryGroup.put(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_TEAM,
		teamsTotal);
	summaryGroup.put(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_MANAGER,
		managersTotal);

	// Add maps to general map.
	payrollMaps.put(ProjectController.ATTR_PAYROLL_GROUP_TEAM, teamGroup);
	payrollMaps.put(ProjectController.ATTR_PAYROLL_SUMMARY_MAP,
		summaryGroup);
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

    @Transactional
    @Override
    public String getCalendarJSON(Project proj) {
	// Get calendar events.
	List<CalendarEventBean> calendarEvents = new ArrayList<CalendarEventBean>();

	// Process all tasks to be included in the calendar.
	for (Task task : proj.getAssignedTasks()) {
	    // Get the start date.
	    Date startDate = task.getDateStart();
	    String start = DateHelper.formatDate(startDate, "yyyy-MM-dd");
	    String name = task.getTitle();

	    // Set values to bean.
	    CalendarEventBean event = new CalendarEventBean();
	    event.setId(Task.OBJECT_NAME + "-" + start + "-"
		    + StringUtils.remove(name, " "));
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
		end = DateHelper.formatDate(c.getTime(), "yyyy-MM-dd");
		event.setEnd(end);
	    }

	    calendarEvents.add(event);
	}

	// Process all reminders to be included in the calendar.
	for (Reminder reminder : proj.getReminders()) {
	    Date myDate = reminder.getDatetime();
	    String start = DateHelper.formatDate(myDate, "yyyy-MM-dd");
	    String name = reminder.getTitle();

	    CalendarEventBean event = new CalendarEventBean();
	    event.setId(Reminder.OBJECT_NAME + "-" + start + ""
		    + StringUtils.remove(name, " "));
	    event.setStart(start);
	    event.setTitle("(Reminder) " + name);
	    event.setClassName(CalendarEventType.REMINDER.css());
	    calendarEvents.add(event);
	}

	// Process all deliveries to be included in the calendar.
	for (Delivery delivery : proj.getDeliveries()) {
	    Date myDate = delivery.getDatetime();
	    String start = DateHelper.formatDate(myDate, "yyyy-MM-dd");
	    String name = delivery.getName();

	    CalendarEventBean event = new CalendarEventBean();
	    event.setId(Delivery.OBJECT_NAME + "-" + start + "-"
		    + StringUtils.remove(name, " "));
	    event.setStart(start);
	    event.setTitle("(Delivery) " + name);
	    event.setClassName(CalendarEventType.DELIVERY.css());
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

	for (Delivery delivery : proj.getDeliveries()) {
	    allStaff.addAll(delivery.getStaff());
	}
	return allStaff;
    }

    @Transactional
    @Override
    public Map<String, Object> getProjectStructureMap(Project proj,
	    Date startDate, Date endDate) {

	Map<String, Object> projectStructMap = new HashMap<String, Object>();

	// Get all managers in this project.
	List<Staff> managers = new ArrayList<Staff>();
	for (ManagerAssignment managerAssignment : proj.getManagerAssignments()) {
	    managers.add(managerAssignment.getManager());
	}
	projectStructMap.put(ProjectController.KEY_PROJECT_STRUCTURE_MANAGERS,
		managers);

	// Get all staff in this project.
	Map<Team, Set<Staff>> teamStaffMap = new HashMap<Team, Set<Staff>>();
	Map<Task, Set<Staff>> taskStaffMap = new HashMap<Task, Set<Staff>>();
	Map<Delivery, Set<Staff>> deliveryStaffMap = new HashMap<Delivery, Set<Staff>>();
	List<Date> datesAllowed = DateHelper.getDatesBetweenDates(startDate,
		endDate);

	for (Team team : proj.getAssignedTeams()) {
	    teamStaffMap.put(team, team.getMembers());
	}

	for (Task task : proj.getAssignedTasks()) {
	    // Only allow dates that are in range.
	    Date taskStartDate = task.getDateStart();
	    if (datesAllowed.contains(taskStartDate)) {
		taskStaffMap.put(task, task.getStaff());
	    }
	}

	for (Delivery delivery : proj.getDeliveries()) {
	    // Only allow dates that are in range.
	    if (datesAllowed.contains(delivery.getDatetime())) {
		deliveryStaffMap.put(delivery, delivery.getStaff());
	    }
	}

	projectStructMap.put(ProjectController.KEY_PROJECT_STRUCTURE_TEAMS,
		teamStaffMap);
	projectStructMap.put(ProjectController.KEY_PROJECT_STRUCTURE_TASKS,
		taskStaffMap);
	projectStructMap.put(
		ProjectController.KEY_PROJECT_STRUCTURE_DELIVERIES,
		deliveryStaffMap);

	return projectStructMap;
    }

    @Transactional
    @Override
    public List<Staff> getManagers(Project proj) {
	List<Staff> managers = new ArrayList<Staff>();
	for (ManagerAssignment managerAssignment : proj.getManagerAssignments()) {
	    managers.add(managerAssignment.getManager());
	}
	return managers;
    }

    @Transactional
    @Override
    public String getPayrollJSON(Project proj, Date startDate, Date endDate,
	    ProjectPayroll projectPayroll) {
	Map<String, Object> payrollMap = getComputedPayrollMap(proj, startDate,
		endDate, projectPayroll);
	return constructPayrollJSON(payrollMap, proj);
    }

}