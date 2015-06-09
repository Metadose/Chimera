package com.cebedo.pmsys.service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import com.cebedo.pmsys.dao.SystemUserDAO;
import com.cebedo.pmsys.domain.Notification;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.CSSClass;
import com.cebedo.pmsys.enums.CalendarEventType;
import com.cebedo.pmsys.enums.MilestoneStatus;
import com.cebedo.pmsys.enums.PayrollStatus;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Delivery;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Reminder;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;
import com.cebedo.pmsys.repository.NotificationZSetRepo;
import com.cebedo.pmsys.repository.ProjectPayrollValueRepo;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxFactory;
import com.cebedo.pmsys.utils.DataStructUtils;
import com.cebedo.pmsys.utils.DateUtils;
import com.cebedo.pmsys.wrapper.ProjectPayrollWrapper;
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
    private ProjectPayrollValueRepo projectPayrollValueRepo;
    private SystemUserDAO systemUserDAO;

    public void setSystemUserDAO(SystemUserDAO systemUserDAO) {
	this.systemUserDAO = systemUserDAO;
    }

    public void setProjectPayrollValueRepo(
	    ProjectPayrollValueRepo projectPayrollValueRepo) {
	this.projectPayrollValueRepo = projectPayrollValueRepo;
    }

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

    /**
     * Construct the JSON needed for the tree grid.
     * 
     * @param payrollMap
     * @param proj
     * @return
     */
    @SuppressWarnings("unchecked")
    private String constructPayrollJSON(Map<String, Object> payrollMap,
	    Project proj) {

	// Currency formatter.
	// Summary groups.
	NumberFormat df = NumberFormat.getCurrencyInstance();
	DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	dfs.setCurrencySymbol("&#8369;");
	dfs.setGroupingSeparator('.');
	dfs.setMonetaryDecimalSeparator('.');
	((DecimalFormat) df).setDecimalFormatSymbols(dfs);
	Map<String, Double> summaryGroup = (Map<String, Double>) payrollMap
		.get(ProjectController.ATTR_PAYROLL_SUMMARY_MAP);

	// Summary details.
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

	// Managers.
	treeGrid = getManagerTreeGrid(payrollMap, summaryGroup, motherPKey,
		randomno, df, treeGrid);

	// Teams.
	treeGrid = getTeamTreeGrid(payrollMap, randomno, summaryGroup, df,
		motherPKey, treeGrid);

	// Tasks.
	treeGrid = getTaskTreeGrid(payrollMap, randomno, summaryGroup, df,
		motherPKey, treeGrid);

	// Deliveries.
	treeGrid = getDeliveryTreeGrid(payrollMap, randomno, summaryGroup, df,
		motherPKey, treeGrid);

	return new Gson().toJson(treeGrid, ArrayList.class);
    }

    /**
     * Get partial tree grid for team.
     * 
     * @param teamPayrollMap
     * @param randomno
     * @param teamGroup
     * @param df
     * @param headerTeamPKey
     * @param treeGrid
     * @return
     */
    @SuppressWarnings({ "unchecked" })
    private List<TreeGridRowBean> getDeliveryTreeGrid(
	    Map<String, Object> payrollMap, Random randomno,
	    Map<String, Double> summaryGroup, NumberFormat df, long motherPKey,
	    List<TreeGridRowBean> treeGrid) {

	// The map.
	Map<Delivery, Map<Staff, String>> thisPayrollMap = (Map<Delivery, Map<Staff, String>>) payrollMap
		.get(ProjectController.ATTR_PAYROLL_MAP_DELIVERY);
	Map<Delivery, Double> thisGroup = (Map<Delivery, Double>) payrollMap
		.get(ProjectController.ATTR_PAYROLL_GROUP_DELIVERY);
	double thisTotal = summaryGroup
		.get(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_DELIVERY);
	String thisTotalStr = df.format(thisTotal);

	// Add the mother key.
	long headerPKey = Math.abs(randomno.nextLong());
	TreeGridRowBean headerBean = new TreeGridRowBean(headerPKey,
		motherPKey, CSSClass.PRIMARY.getSpanHTML("GROUP")
			+ "&nbsp;Deliveries", thisTotalStr);
	treeGrid.add(headerBean);

	// Loop through teams.
	for (Delivery delivery : thisPayrollMap.keySet()) {

	    Map<Staff, String> staffMap = thisPayrollMap.get(delivery);
	    if (staffMap.keySet().isEmpty()) {
		continue;
	    }

	    // Get details.
	    long thisPKey = Math.abs(randomno.nextLong());
	    double thisObjectTotal = thisGroup.get(delivery);
	    String thisObjectTotalStr = df.format(thisObjectTotal);

	    // Add to bean.
	    TreeGridRowBean thisBean = new TreeGridRowBean(thisPKey,
		    headerPKey, CSSClass.INFO.getSpanHTML("DELIVERY")
			    + "&nbsp;" + delivery.getName(), thisObjectTotalStr);
	    treeGrid.add(thisBean);

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
			thisPKey, rowName, rowValue);
		treeGrid.add(rowBean);
	    }
	}

	return treeGrid;
    }

    /**
     * Get partial tree grid for managers.
     * 
     * @param managerPayrollMap
     * @param headerManagerPKey
     * @param randomno
     * @param df
     * @param treeGrid
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<TreeGridRowBean> getManagerTreeGrid(
	    Map<String, Object> payrollMap, Map<String, Double> summaryGroup,
	    long motherPKey, Random randomno, NumberFormat df,
	    List<TreeGridRowBean> treeGrid) {

	Map<ManagerAssignment, String> managerPayrollMap = (Map<ManagerAssignment, String>) payrollMap
		.get(ProjectController.ATTR_PAYROLL_MAP_MANAGER);

	// Manager map.
	double managersTotal = summaryGroup
		.get(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_MANAGER);
	String managersTotalStr = df.format(managersTotal);

	// Add header beans.
	long headerManagerPKey = Math.abs(randomno.nextLong());
	TreeGridRowBean headerManagerBean = new TreeGridRowBean(
		headerManagerPKey, motherPKey,
		CSSClass.PRIMARY.getSpanHTML("GROUP") + "&nbsp;Managers",
		managersTotalStr);
	treeGrid.add(headerManagerBean);

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
	return treeGrid;
    }

    /**
     * Get partial tree grid for team.
     * 
     * @param teamPayrollMap
     * @param randomno
     * @param teamGroup
     * @param df
     * @param headerTeamPKey
     * @param treeGrid
     * @return
     */
    @SuppressWarnings({ "unchecked" })
    private List<TreeGridRowBean> getTaskTreeGrid(
	    Map<String, Object> payrollMap, Random randomno,
	    Map<String, Double> summaryGroup, NumberFormat df, long motherPKey,
	    List<TreeGridRowBean> treeGrid) {

	// The map.
	Map<Task, Map<Staff, String>> thisPayrollMap = (Map<Task, Map<Staff, String>>) payrollMap
		.get(ProjectController.ATTR_PAYROLL_MAP_TASK);
	Map<Task, Double> thisGroup = (Map<Task, Double>) payrollMap
		.get(ProjectController.ATTR_PAYROLL_GROUP_TASK);
	double thisTotal = summaryGroup
		.get(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_TASK);
	String thisTotalStr = df.format(thisTotal);

	// Add the mother key.
	long headerPKey = Math.abs(randomno.nextLong());
	TreeGridRowBean headerBean = new TreeGridRowBean(headerPKey,
		motherPKey, CSSClass.PRIMARY.getSpanHTML("GROUP")
			+ "&nbsp;Tasks", thisTotalStr);
	treeGrid.add(headerBean);

	// Loop through teams.
	for (Task task : thisPayrollMap.keySet()) {

	    Map<Staff, String> staffMap = thisPayrollMap.get(task);
	    if (staffMap.keySet().isEmpty()) {
		continue;
	    }

	    // Get details.
	    long thisPKey = Math.abs(randomno.nextLong());
	    double thisObjectTotal = thisGroup.get(task);
	    String thisObjectTotalStr = df.format(thisObjectTotal);

	    // Add to bean.
	    TreeGridRowBean thisBean = new TreeGridRowBean(thisPKey,
		    headerPKey, CSSClass.INFO.getSpanHTML("TASK") + "&nbsp;"
			    + task.getTitle(), thisObjectTotalStr);
	    treeGrid.add(thisBean);

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
			thisPKey, rowName, rowValue);
		treeGrid.add(rowBean);
	    }
	}

	return treeGrid;
    }

    /**
     * Get partial tree grid for team.
     * 
     * @param teamPayrollMap
     * @param randomno
     * @param teamGroup
     * @param df
     * @param headerTeamPKey
     * @param treeGrid
     * @return
     */
    @SuppressWarnings({ "unchecked" })
    private List<TreeGridRowBean> getTeamTreeGrid(
	    Map<String, Object> payrollMap, Random randomno,
	    Map<String, Double> summaryGroup, NumberFormat df, long motherPKey,
	    List<TreeGridRowBean> treeGrid) {

	// Team map.
	Map<Team, Map<Staff, String>> teamPayrollMap = (Map<Team, Map<Staff, String>>) payrollMap
		.get(ProjectController.ATTR_PAYROLL_MAP_TEAM);
	Map<Team, Double> teamGroup = (Map<Team, Double>) payrollMap
		.get(ProjectController.ATTR_PAYROLL_GROUP_TEAM);
	double teamsTotal = summaryGroup
		.get(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_TEAM);
	String teamsTotalStr = df.format(teamsTotal);

	// Add the mother key.
	long headerTeamPKey = Math.abs(randomno.nextLong());
	TreeGridRowBean headerTeamBean = new TreeGridRowBean(headerTeamPKey,
		motherPKey, CSSClass.PRIMARY.getSpanHTML("GROUP")
			+ "&nbsp;Teams", teamsTotalStr);
	treeGrid.add(headerTeamBean);

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

	return treeGrid;
    }

    /**
     * Get map of payrolls.
     */
    @Override
    @Transactional
    public Map<String, Object> getComputedPayrollMap(Project proj, Date min,
	    Date max, ProjectPayroll projectPayroll) {
	Map<String, Object> payrollMaps = new HashMap<String, Object>();
	List<Long> staffIDsToCompute = DataStructUtils
		.convertArrayToList(projectPayroll.getStaffIDs());

	// Payroll maps.
	Map<String, Double> summaryGroup = new HashMap<String, Double>();

	// Already computed? Add ID to this list.
	Map<Long, String> computedMap = new HashMap<Long, String>();

	// Wage for managers.
	double managersTotal = 0;
	Map<ManagerAssignment, String> managerPayrollMap = new HashMap<ManagerAssignment, String>();
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
	    computedMap.put(managerID, "Manager List");
	}

	// Wage for teams.
	// Loop through all the teams.
	Map<Team, Map<Staff, String>> teamPayrollMap = new HashMap<Team, Map<Staff, String>>();
	Map<Team, Double> teamGroup = new HashMap<Team, Double>();
	double teamsTotal = 0;
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

	// Wage for tasks.
	// Loop through all the teams.
	Map<Task, Map<Staff, String>> taskPayrollMap = new HashMap<Task, Map<Staff, String>>();
	double tasksTotal = 0;
	Map<Task, Double> taskGroup = new HashMap<Task, Double>();

	for (Task task : proj.getAssignedTasks()) {
	    Map<Staff, String> staffPayrollMap = new HashMap<Staff, String>();
	    double thisTaskTotal = 0;

	    for (Staff assignedStaff : task.getStaff()) {
		long staffID = assignedStaff.getId();

		if (!staffIDsToCompute.contains(staffID)) {
		    continue;
		}

		// If a staff has already been computed before,
		// don't compute again.
		if (computedMap.containsKey(staffID)) {
		    staffPayrollMap.put(
			    assignedStaff,
			    IDENTIFIER_ALREADY_EXISTS
				    + computedMap.get(staffID));
		    continue;
		}

		// Add to map
		// and add to computed list.
		double totalWageOfStaff = this.payrollService
			.getTotalWageOfStaffInRange(assignedStaff, min, max);
		thisTaskTotal += totalWageOfStaff;
		staffPayrollMap.put(assignedStaff,
			String.valueOf(totalWageOfStaff));
		computedMap.put(staffID, "Task " + task.getTitle());
	    }

	    // Add to team list.
	    tasksTotal += thisTaskTotal;
	    taskGroup.put(task, thisTaskTotal);
	    taskPayrollMap.put(task, staffPayrollMap);
	}

	// Wage for delivery.
	// Loop through all the deliveries.
	Map<Delivery, Map<Staff, String>> deliveryPayrollMap = new HashMap<Delivery, Map<Staff, String>>();
	double deliveryTotal = 0;
	Map<Delivery, Double> deliveryGroup = new HashMap<Delivery, Double>();

	for (Delivery delivery : proj.getDeliveries()) {
	    Map<Staff, String> staffPayrollMap = new HashMap<Staff, String>();
	    double thisDeliveryTotal = 0;

	    for (Staff assignedStaff : delivery.getStaff()) {
		long staffID = assignedStaff.getId();

		if (!staffIDsToCompute.contains(staffID)) {
		    continue;
		}

		// If a staff has already been computed before,
		// don't compute again.
		if (computedMap.containsKey(staffID)) {
		    staffPayrollMap.put(
			    assignedStaff,
			    IDENTIFIER_ALREADY_EXISTS
				    + computedMap.get(staffID));
		    continue;
		}

		// Add to map
		// and add to computed list.
		double totalWageOfStaff = this.payrollService
			.getTotalWageOfStaffInRange(assignedStaff, min, max);
		thisDeliveryTotal += totalWageOfStaff;
		staffPayrollMap.put(assignedStaff,
			String.valueOf(totalWageOfStaff));
		computedMap.put(staffID, "Delivery " + delivery.getName());
	    }

	    // Add to team list.
	    deliveryTotal += thisDeliveryTotal;
	    deliveryGroup.put(delivery, thisDeliveryTotal);
	    deliveryPayrollMap.put(delivery, staffPayrollMap);
	}

	// Add summary details.
	summaryGroup.put(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_OVERALL,
		teamsTotal + managersTotal + tasksTotal + deliveryTotal);
	summaryGroup.put(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_TEAM,
		teamsTotal);
	summaryGroup.put(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_MANAGER,
		managersTotal);
	summaryGroup.put(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_TASK,
		tasksTotal);
	summaryGroup.put(ProjectController.ATTR_PAYROLL_SUMMARY_TOTAL_DELIVERY,
		deliveryTotal);

	// Groups total.
	payrollMaps.put(ProjectController.ATTR_PAYROLL_GROUP_TEAM, teamGroup);
	payrollMaps.put(ProjectController.ATTR_PAYROLL_GROUP_TASK, taskGroup);
	payrollMaps.put(ProjectController.ATTR_PAYROLL_GROUP_DELIVERY,
		deliveryGroup);

	// Add maps to general map.
	payrollMaps.put(ProjectController.ATTR_PAYROLL_SUMMARY_MAP,
		summaryGroup);
	payrollMaps
		.put(ProjectController.ATTR_PAYROLL_MAP_TEAM, teamPayrollMap);
	payrollMaps
		.put(ProjectController.ATTR_PAYROLL_MAP_TASK, taskPayrollMap);
	payrollMaps.put(ProjectController.ATTR_PAYROLL_MAP_DELIVERY,
		deliveryPayrollMap);
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
	    String start = DateUtils.formatDate(startDate, "yyyy-MM-dd");
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
		end = DateUtils.formatDate(c.getTime(), "yyyy-MM-dd");
		event.setEnd(end);
	    }

	    calendarEvents.add(event);
	}

	// Process all reminders to be included in the calendar.
	for (Reminder reminder : proj.getReminders()) {
	    Date myDate = reminder.getDatetime();
	    String start = DateUtils.formatDate(myDate, "yyyy-MM-dd");
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
	    String start = DateUtils.formatDate(myDate, "yyyy-MM-dd");
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
	List<Date> datesAllowed = DateUtils.getDatesBetweenDates(startDate,
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
    public String getPayrollJSON(Project proj, Date startDate, Date endDate,
	    ProjectPayroll projectPayroll) {
	Map<String, Object> payrollMap = getComputedPayrollMap(proj, startDate,
		endDate, projectPayroll);
	return constructPayrollJSON(payrollMap, proj);
    }

    /**
     * Get all payrolls given a project.
     */
    @Transactional
    @Override
    public List<ProjectPayrollWrapper> getAllPayrolls(Project proj) {

	// Get the needed ID's for the key.
	// Construct the key.
	long companyID = proj.getCompany() == null ? 0 : proj.getCompany()
		.getId();
	String pattern = ProjectPayroll.constructKey(companyID, proj.getId(),
		null, null, null, null, null);

	// Get all keys based on pattern.
	// Multi-get all objects based on keys.
	Set<String> keys = this.projectPayrollValueRepo.keys(pattern);
	List<ProjectPayroll> projectPayrolls = this.projectPayrollValueRepo
		.multiGet(keys);
	List<ProjectPayrollWrapper> wrappedProjectPayrolls = new ArrayList<ProjectPayrollWrapper>();

	// For each resulting object,
	// wrap with wrapper.
	for (ProjectPayroll payroll : projectPayrolls) {

	    // Get objects which corresponding to which ID's.
	    SystemUser approver = this.systemUserDAO.getByID(payroll
		    .getApproverID());
	    SystemUser creator = this.systemUserDAO.getByID(payroll
		    .getCreatorID());
	    Date startDate = payroll.getStartDate();
	    Date endDate = payroll.getEndDate();
	    PayrollStatus status = PayrollStatus.of(payroll.getStatusID());
	    Company co = this.companyDAO.getByID(payroll.getCompanyID());

	    // Construct the wrapped object.
	    // Add object to list.
	    ProjectPayrollWrapper wrappedPayroll = new ProjectPayrollWrapper(
		    approver, creator, startDate, endDate, status, co, proj);
	    wrappedProjectPayrolls.add(wrappedPayroll);
	}

	// Sort the list in descending order.
	Collections.sort(wrappedProjectPayrolls,
		new Comparator<ProjectPayrollWrapper>() {
		    @Override
		    public int compare(ProjectPayrollWrapper aObj,
			    ProjectPayrollWrapper bObj) {
			Date aStart = aObj.getStartDate();
			Date bStart = bObj.getStartDate();

			// To sort in ascending,
			// remove Not's.
			return !(aStart.before(bStart)) ? -1 : !(aStart
				.after(bStart)) ? 1 : 0;
		    }
		});

	return wrappedProjectPayrolls;
    }

}