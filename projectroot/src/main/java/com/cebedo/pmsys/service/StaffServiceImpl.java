package com.cebedo.pmsys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.CalendarEventBean;
import com.cebedo.pmsys.bean.GanttBean;
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.SystemUserDAO;
import com.cebedo.pmsys.dao.TeamDAO;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.DateHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.ProjectFile;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;
import com.cebedo.pmsys.model.assignment.StaffTeamAssignment;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxFactory;
import com.cebedo.pmsys.wrapper.StaffWrapper;
import com.google.gson.Gson;

@Service
public class StaffServiceImpl implements StaffService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private LogHelper logHelper = new LogHelper();
    private static Logger logger = Logger.getLogger(Staff.OBJECT_NAME);

    private StaffDAO staffDAO;
    private ProjectDAO projectDAO;
    private TeamDAO teamDAO;
    private SystemUserDAO systemUserDAO;
    private CompanyDAO companyDAO;
    private ProjectFileService projectFileService;

    public void setCompanyDAO(CompanyDAO companyDAO) {
	this.companyDAO = companyDAO;
    }

    public void setSystemUserDAO(SystemUserDAO systemUserDAO) {
	this.systemUserDAO = systemUserDAO;
    }

    @Autowired(required = true)
    @Qualifier(value = "projectFileService")
    public void setProjectFileService(ProjectFileService ps) {
	this.projectFileService = ps;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
	this.staffDAO = staffDAO;
    }

    public void setTeamDAO(TeamDAO teamDAO) {
	this.teamDAO = teamDAO;
    }

    /**
     * Create a new staff.
     */
    @Override
    @Transactional
    public String create(Staff staff) {
	AuthenticationToken auth = this.authHelper.getAuth();

	// Log and notify.
	this.messageHelper.sendAction(AuditAction.CREATE, staff);

	// Do service.
	// Create the staff first since to attach it's relationship
	// with the company.
	Company authCompany = auth.getCompany();
	staff.setCompany(authCompany);
	this.staffDAO.create(staff);

	// Return success.
	return AlertBoxFactory.SUCCESS.generateCreate(Staff.OBJECT_NAME,
		staff.getFullName());
    }

    /**
     * Get staff by id.
     */
    @Override
    @Transactional
    public Staff getByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Staff stf = this.staffDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(stf)) {
	    // Log info.
	    logger.info(this.logHelper.logGetObject(auth, Staff.OBJECT_NAME,
		    id, stf.getFullName()));

	    // Return obj.
	    return stf;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET,
		Staff.OBJECT_NAME, stf.getId(), stf.getFullName()));

	// Return empty.
	return new Staff();
    }

    /**
     * Update a staff.
     */
    @Override
    @Transactional
    public String update(Staff staff) {
	AuthenticationToken auth = this.authHelper.getAuth();

	if (this.authHelper.isActionAuthorized(staff)) {
	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.UPDATE, staff);

	    // Do service.
	    this.staffDAO.update(staff);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateUpdate(Staff.OBJECT_NAME,
		    staff.getFullName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE,
		Staff.OBJECT_NAME, staff.getId(), staff.getFullName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateUpdate(Staff.OBJECT_NAME,
		staff.getFullName());
    }

    /**
     * Delete a staff.
     */
    @Override
    @Transactional
    public String delete(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Staff stf = this.staffDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(stf)) {
	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.DELETE, stf);

	    // Do service.
	    // Check if the staff has any project files.
	    Hibernate.initialize(stf.getFiles());
	    for (ProjectFile file : stf.getFiles()) {

		// If not owned by a project, delete it.
		if (file.getProject() == null) {
		    this.projectFileService.delete(file.getId());
		    continue;
		}
		// If the file is owned by a project,
		// remove it's association with the staff.
		file.setUploader(null);
		this.projectFileService.update(file);
	    }
	    this.staffDAO.delete(id);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateDelete(Staff.OBJECT_NAME,
		    stf.getFullName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE,
		Staff.OBJECT_NAME, stf.getId(), stf.getFullName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateDelete(Staff.OBJECT_NAME,
		stf.getFullName());
    }

    /**
     * List all staff.
     */
    @Override
    @Transactional
    public List<Staff> list() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    // Log info.
	    logger.info(this.logHelper.logListAsSuperAdmin(token,
		    Staff.OBJECT_NAME));

	    // Return list.
	    return this.staffDAO.list(null);
	}

	// Log info.
	Company co = token.getCompany();
	logger.info(this.logHelper.logListFromCompany(token, Staff.OBJECT_NAME,
		co));

	// Return non-super list.
	return this.staffDAO.list(co.getId());
    }

    /**
     * List all staff with all collections.
     */
    @Override
    @Transactional
    public List<Staff> listWithAllCollections() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    // Log info.
	    logger.info(this.logHelper.logListWithCollectionsAsSuperAdmin(
		    token, Staff.OBJECT_NAME));

	    // Return list.
	    return this.staffDAO.listWithAllCollections(null);
	}

	// Log info.
	Company co = token.getCompany();
	logger.info(this.logHelper.logListWithCollectionsFromCompany(token,
		Staff.OBJECT_NAME, co));

	// Return list.
	return this.staffDAO.listWithAllCollections(co.getId());
    }

    /**
     * Assign a staff to a project as manager.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String assignProjectManager(long projectID, long staffID,
	    String position) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(projectID);
	Staff staff = this.staffDAO.getByID(staffID);

	// If this action is not authorized,
	// return.
	if (!this.authHelper.isActionAuthorized(staff)
		|| !this.authHelper.isActionAuthorized(project)) {

	    // Log warn.
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.ASSIGN, Project.OBJECT_NAME, project.getId(),
		    project.getName()));
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.ASSIGN, Staff.OBJECT_NAME, staff.getId(),
		    staff.getFullName()));

	    // Return fail.
	    return AlertBoxFactory.FAILED.generateAssign(Staff.OBJECT_NAME,
		    staff.getFullName());
	}

	// Log and notify.
	this.messageHelper.sendAssignUnassign(AuditAction.ASSIGN, project,
		staff);

	// Do service.
	ManagerAssignment assignment = new ManagerAssignment();
	assignment.setProject(project);
	assignment.setManager(staff);
	assignment.setProjectPosition(position);
	this.staffDAO.assignProjectManager(assignment);

	// Return success.
	return AlertBoxFactory.SUCCESS.generateAssign(Staff.OBJECT_NAME,
		staff.getFullName());
    }

    /**
     * Unassign a project manager.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String unassignProjectManager(long projectID, long staffID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(projectID);
	Staff staff = this.staffDAO.getByID(staffID);

	if (!this.authHelper.isActionAuthorized(staff)
		|| !this.authHelper.isActionAuthorized(project)) {

	    // Log warn.
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.UNASSIGN, Project.OBJECT_NAME, project.getId(),
		    project.getName()));
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.UNASSIGN, Staff.OBJECT_NAME, staff.getId(),
		    staff.getFullName()));

	    // Return fail.
	    return AlertBoxFactory.FAILED.generateUnassign(Staff.OBJECT_NAME,
		    staff.getFullName());
	}

	// Log and notify.
	this.messageHelper.sendAssignUnassign(AuditAction.UNASSIGN, project,
		staff);

	// Do service.
	// If authorized, continue with the action.
	this.staffDAO.unassignProjectManager(projectID, staffID);

	// Return success.
	return AlertBoxFactory.SUCCESS.generateUnassign(Staff.OBJECT_NAME,
		staff.getFullName());
    }

    /**
     * Unassign all project managers.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public String unassignAllProjectManagers(long projectID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectDAO.getByID(projectID);

	if (!this.authHelper.isActionAuthorized(project)) {
	    // Log warn.
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.UNASSIGN_ALL, Project.OBJECT_NAME,
		    project.getId(), project.getName()));

	    // Return fail.
	    return AlertBoxFactory.FAILED
		    .generateUnassignAll(ManagerAssignment.OBJECT_LABEL);
	}

	// Log and notify.
	this.messageHelper.sendUnassignAll(ManagerAssignment.OBJECT_LABEL,
		project);

	// Do service.
	this.staffDAO.unassignAllProjectManagers(projectID);

	// Return success.
	return AlertBoxFactory.SUCCESS
		.generateUnassignAll(ManagerAssignment.OBJECT_LABEL);
    }

    /**
     * Get with all collections and id.
     */
    @Override
    @Transactional
    public Staff getWithAllCollectionsByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Staff stf = this.staffDAO.getWithAllCollectionsByID(id);

	if (this.authHelper.isActionAuthorized(stf)) {

	    // Log info.
	    logger.info(this.logHelper.logGetObjectWithAllCollections(auth,
		    Staff.OBJECT_NAME, id, stf.getFullName()));

	    // Return obj.
	    return stf;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth,
		AuditAction.GET_WITH_COLLECTIONS, Staff.OBJECT_NAME, id,
		stf.getFullName()));

	// Return empty.
	return new Staff();
    }

    /**
     * Unassign a team from a staff.
     */
    @Override
    @Transactional
    public String unassignTeam(long teamID, long staffID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Team team = this.teamDAO.getByID(teamID);
	Staff staff = this.staffDAO.getByID(staffID);

	if (!this.authHelper.isActionAuthorized(staff)
		|| !this.authHelper.isActionAuthorized(team)) {
	    // Log warn.
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.UNASSIGN, Staff.OBJECT_NAME, staff.getId(),
		    staff.getFullName()));
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.UNASSIGN, Team.OBJECT_NAME, team.getId(),
		    team.getName()));

	    // Return fail.
	    return AlertBoxFactory.FAILED.generateUnassign(Team.OBJECT_NAME,
		    team.getName());
	}

	// Log and notify.
	this.messageHelper
		.sendAssignUnassign(AuditAction.UNASSIGN, staff, team);

	// Do service.
	this.staffDAO.unassignTeam(teamID, staffID);

	// Return success.
	return AlertBoxFactory.SUCCESS.generateUnassign(Team.OBJECT_NAME,
		team.getName());
    }

    /**
     * Unassign all teams linked to a staff.
     */
    @Override
    @Transactional
    public String unassignAllTeams(long staffID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Staff staff = this.staffDAO.getByID(staffID);

	if (!this.authHelper.isActionAuthorized(staff)) {
	    // Log warn.
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.UNASSIGN_ALL, Staff.OBJECT_NAME, staff.getId(),
		    staff.getFullName()));

	    // Return fail.
	    return AlertBoxFactory.FAILED.generateUnassignAll(Team.OBJECT_NAME);
	}

	// Log and notify.
	this.messageHelper.sendUnassignAll(Team.OBJECT_NAME, staff);

	// Do service.
	this.staffDAO.unassignAllTeams(staffID);

	// Log success.
	return AlertBoxFactory.SUCCESS.generateUnassignAll(Team.OBJECT_NAME);
    }

    /***
     * Assign a team for the staff.
     */
    @Override
    @Transactional
    public String assignTeam(StaffTeamAssignment stAssign) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Staff staff = this.staffDAO.getByID(stAssign.getStaffID());
	Team team = this.teamDAO.getByID(stAssign.getTeamID());

	if (!this.authHelper.isActionAuthorized(staff)
		|| !this.authHelper.isActionAuthorized(team)) {

	    // Log warn.
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.ASSIGN, Staff.OBJECT_NAME, staff.getId(),
		    staff.getFullName()));
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.ASSIGN, Team.OBJECT_NAME, team.getId(),
		    team.getName()));

	    // Return fail.
	    return AlertBoxFactory.FAILED.generateAssign(Team.OBJECT_NAME,
		    team.getName());
	}

	// Log and notify.
	this.messageHelper.sendAssignUnassign(AuditAction.ASSIGN, staff, team);

	// Do service.
	this.staffDAO.assignTeam(stAssign);

	// Return success.
	return AlertBoxFactory.SUCCESS.generateAssign(Team.OBJECT_NAME,
		team.getName());
    }

    /**
     * List all staff.
     */
    @Override
    @Transactional
    public List<Staff> list(Long companyID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	if (auth.isSuperAdmin()) {
	    // Log info.
	    logger.info(this.logHelper.logListAsSuperAdmin(auth,
		    Staff.OBJECT_NAME));

	    // Return list.
	    return this.staffDAO.list(null);
	}

	// Log info.
	Company co = this.companyDAO.getByID(companyID);
	logger.info(this.logHelper.logListFromCompany(auth, Staff.OBJECT_NAME,
		co));

	// Return list.
	return this.staffDAO.list(companyID);
    }

    /**
     * List unassigned staff given a project.
     */
    @Override
    @Transactional
    public List<Staff> listUnassignedInProject(Long companyID, Project project) {
	if (this.authHelper.isActionAuthorized(project)) {
	    List<Staff> companyStaffList = this.staffDAO.list(companyID);
	    List<StaffWrapper> wrappedStaffList = StaffWrapper
		    .wrap(companyStaffList);
	    List<StaffWrapper> assignedStaffList = StaffWrapper.wrap(project
		    .getManagerAssignments());
	    wrappedStaffList.removeAll(assignedStaffList);
	    return StaffWrapper.unwrap(StaffWrapper
		    .removeEmptyNames(wrappedStaffList));
	}
	return new ArrayList<Staff>();
    }

    /**
     * Get name given an id.
     */
    @Override
    @Transactional
    public String getNameByID(long staffID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Staff staff = this.staffDAO.getByID(staffID);
	logger.info(this.logHelper.logGetProperty(auth, Staff.OBJECT_NAME,
		Staff.PROPERTY_TRANSIENT_FULL_NAME, staff.getId(),
		staff.getFullName()));
	return this.staffDAO.getNameByID(staffID);
    }

    /**
     * Create a staff from an origin.
     */
    @Override
    @Transactional
    public String createFromOrigin(Staff staff, String origin, String originID) {
	AuthenticationToken auth = this.authHelper.getAuth();

	// If coming from the system user page.
	if (origin.equals(SystemUser.OBJECT_NAME)) {
	    SystemUser user = this.systemUserDAO.getByID(Long
		    .parseLong(originID));

	    if (user == null) {

		// Get the company from the executor.
		staff.setCompany(auth.getCompany());

		if (this.authHelper.isActionAuthorized(staff)) {

		    // Log and notify.
		    this.messageHelper.sendAction(AuditAction.CREATE, staff);

		    // Do service.
		    this.staffDAO.create(staff);

		    // Return success.
		    return AlertBoxFactory.SUCCESS.generateCreate(
			    Staff.OBJECT_NAME, staff.getFullName());
		}
	    } else {
		// Get the company from the user.
		staff.setCompany(user.getCompany());

		if (this.authHelper.isActionAuthorized(staff)) {

		    // Log and notify.
		    this.messageHelper.sendAction(AuditAction.CREATE, staff);

		    // Do service.
		    // Update the staff.
		    this.staffDAO.create(staff);

		    // If coming from the system user,
		    // attach relationship with user.
		    user.setStaff(staff);
		    this.systemUserDAO.update(user);

		    // Return success.
		    return AlertBoxFactory.SUCCESS.generateCreate(
			    Staff.OBJECT_NAME, staff.getFullName());
		}
	    }

	    // Log warn.
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.CREATE, Staff.OBJECT_NAME, staff.getId(),
		    staff.getFullName()));

	    // Return fail.
	    return AlertBoxFactory.FAILED.generateCreate(Staff.OBJECT_NAME,
		    staff.getFullName());
	}

	// Create the staff first since to attach it's relationship
	// with the company.
	Company authCompany = auth.getCompany();
	staff.setCompany(authCompany);
	if (this.authHelper.isActionAuthorized(staff)) {

	    // Log and notify.
	    this.messageHelper.sendAction(AuditAction.CREATE, staff);

	    // Do service.
	    this.staffDAO.create(staff);

	    // Return success.
	    return AlertBoxFactory.SUCCESS.generateCreate(Staff.OBJECT_NAME,
		    staff.getFullName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE,
		Staff.OBJECT_NAME, staff.getId(), staff.getFullName()));

	// Return fail.
	return AlertBoxFactory.FAILED.generateCreate(Staff.OBJECT_NAME,
		staff.getFullName());
    }

    /**
     * Get the JSON for the calendar.
     * 
     * @param staff
     * @param attendanceList
     * @return
     */
    @Transactional
    @Override
    public String getCalendarJSON(Staff staff, Set<Attendance> attendanceList) {
	// Get calendar events.
	List<CalendarEventBean> calendarEvents = new ArrayList<CalendarEventBean>();

	for (Attendance attendance : attendanceList) {

	    Date myDate = attendance.getTimestamp();
	    String start = DateHelper.formatDate(myDate, "yyyy-MM-dd");
	    AttendanceStatus attnStat = AttendanceStatus.of(attendance
		    .getStatusID());

	    // Construct the event bean for this attendance.
	    CalendarEventBean event = new CalendarEventBean();
	    event.setStart(start);
	    event.setTitle(attnStat.name());
	    event.setId(start);
	    event.setClassName(attnStat.css());
	    event.setAttendanceStatus(String.valueOf(attendance.getStatusID()));
	    event.setAttendanceWage(String.valueOf(attendance.getWage()));
	    if (attnStat == AttendanceStatus.OVERTIME) {
		event.setBorderColor("Red");
	    }
	    calendarEvents.add(event);
	}

	return new Gson().toJson(calendarEvents, ArrayList.class);
    }

    /**
     * Get the JSON for the staff.
     * 
     * @param staff
     * @return
     */
    @Transactional
    @Override
    public String getGanttJSON(Staff staff) {
	// Get gantt-data.
	List<GanttBean> ganttBeanList = new ArrayList<GanttBean>();

	// Add myself.
	GanttBean myGanttBean = new GanttBean(staff);
	ganttBeanList.add(myGanttBean);

	// Get the gantt parent data.
	for (ManagerAssignment assigns : staff.getAssignedManagers()) {

	    // Add all projects.
	    Project proj = assigns.getProject();
	    GanttBean projectBean = new GanttBean(proj, myGanttBean);
	    ganttBeanList.add(projectBean);

	    // For each milestone in this project, add.
	    for (Milestone milestone : proj.getMilestones()) {
		GanttBean milestoneBean = new GanttBean(milestone, projectBean);
		ganttBeanList.add(milestoneBean);
	    }
	}

	// Get the tasks (children) of each parent.
	for (Task task : staff.getTasks()) {
	    // Get the data for the gantt chart.
	    // Get the parent of this task.
	    String parentId = "";
	    Project proj = task.getProject();
	    if (task.getMilestone() != null) {
		parentId = Milestone.OBJECT_NAME + "-"
			+ task.getMilestone().getId();
	    } else if (proj != null) {
		parentId = Project.OBJECT_NAME + "-" + proj.getId();
	    } else {
		parentId = Staff.OBJECT_NAME + "-" + staff.getId();
	    }

	    GanttBean ganttBean = new GanttBean(task, parentId);
	    ganttBeanList.add(ganttBean);
	}

	return new Gson().toJson(ganttBeanList, ArrayList.class);
    }

    /**
     * Get map of task status with corresponding count.
     * 
     * @param staff
     * @return
     */
    @Transactional
    @Override
    public Map<TaskStatus, Integer> getTaskStatusCountMap(Staff staff) {
	// Get summary of tasks.
	// For each task status, count how many.
	Map<TaskStatus, Integer> taskStatusMap = new HashMap<TaskStatus, Integer>();
	Map<TaskStatus, Integer> taskStatusMapSorted = new LinkedHashMap<TaskStatus, Integer>();

	// Get the tasks (children) of each parent.
	for (Task task : staff.getTasks()) {
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

    /**
     * Get attendance status with corresponding count.
     * 
     * @param staff
     * @param attendanceList
     * @return
     */
    @Transactional
    @Override
    public Map<AttendanceStatus, Map<String, Double>> getAttendanceStatusCountMap(
	    Set<Attendance> attendanceList) {
	String statusCount = "statusCount";
	String equivalentWage = "equivalentWage";

	// And count number per status.
	Map<AttendanceStatus, Map<String, Double>> attendanceStatusMap = new HashMap<AttendanceStatus, Map<String, Double>>();
	Map<AttendanceStatus, Map<String, Double>> attendanceStatusSorted = new LinkedHashMap<AttendanceStatus, Map<String, Double>>();

	for (Attendance attendance : attendanceList) {

	    AttendanceStatus attnStat = AttendanceStatus.of(attendance
		    .getStatusID());

	    // Get and set status count.
	    Double statCount = attendanceStatusMap.get(attnStat) == null ? 1
		    : attendanceStatusMap.get(attnStat).get(statusCount) + 1;
	    Map<String, Double> breakdown = new HashMap<String, Double>();
	    breakdown.put(statusCount, statCount);
	    double value = attnStat == AttendanceStatus.ABSENT ? 0 : statCount
		    * attendance.getWage();
	    breakdown.put(equivalentWage, value);
	    attendanceStatusMap.put(attnStat, breakdown);
	}

	// If status count is null,
	// Add it as zero.
	for (AttendanceStatus status : AttendanceStatus.class
		.getEnumConstants()) {
	    if (status == AttendanceStatus.DELETE) {
		continue;
	    }
	    Map<String, Double> breakdown = attendanceStatusMap.get(status);
	    if (breakdown == null) {
		breakdown = new HashMap<String, Double>();
		breakdown.put(statusCount, (double) 0);
		breakdown.put(equivalentWage, (double) 0);
		attendanceStatusSorted.put(status, breakdown);
	    }
	    attendanceStatusSorted.put(status, breakdown);
	}

	return attendanceStatusSorted;
    }
}
