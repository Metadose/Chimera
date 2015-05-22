package com.cebedo.pmsys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.CalendarEventBean;
import com.cebedo.pmsys.bean.TaskGanttBean;
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.SystemUserDAO;
import com.cebedo.pmsys.dao.TeamDAO;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.enums.Status;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.DateHelper;
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
import com.cebedo.pmsys.wrapper.StaffWrapper;
import com.google.gson.Gson;

@Service
public class StaffServiceImpl implements StaffService {

    private AuthHelper authHelper = new AuthHelper();
    private StaffDAO staffDAO;
    private ProjectDAO projectDAO;
    private TeamDAO teamDAO;
    private CompanyDAO companyDAO;
    private SystemUserDAO systemUserDAO;
    private ProjectFileService projectFileService;

    public void setSystemUserDAO(SystemUserDAO systemUserDAO) {
	this.systemUserDAO = systemUserDAO;
    }

    @Autowired(required = true)
    @Qualifier(value = "projectFileService")
    public void setProjectFileService(ProjectFileService ps) {
	this.projectFileService = ps;
    }

    public void setCompanyDAO(CompanyDAO companyDAO) {
	this.companyDAO = companyDAO;
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

    @Override
    @Transactional
    public void create(Staff staff) {
	// Create the staff first since to attach it's relationship
	// with the company.
	AuthenticationToken auth = this.authHelper.getAuth();
	Company authCompany = auth.getCompany();
	staff.setCompany(authCompany);
	this.staffDAO.create(staff);
    }

    @Override
    @Transactional
    public Staff getByID(long id) {
	Staff stf = this.staffDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(stf)) {
	    return stf;
	}
	return new Staff();
    }

    @Override
    @Transactional
    public void update(Staff staff) {
	if (this.authHelper.isActionAuthorized(staff)) {
	    this.staffDAO.update(staff);
	}
    }

    @Override
    @Transactional
    public void delete(long id) {
	Staff stf = this.staffDAO.getByID(id);
	if (this.authHelper.isActionAuthorized(stf)) {
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
	}
    }

    @Override
    @Transactional
    public List<Staff> list() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    return this.staffDAO.list(null);
	}
	return this.staffDAO.list(token.getCompany().getId());
    }

    @Override
    @Transactional
    public List<Staff> listWithAllCollections() {
	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    return this.staffDAO.listWithAllCollections(null);
	}
	return this.staffDAO.listWithAllCollections(token.getCompany().getId());
    }

    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public void assignProjectManager(long projectID, long staffID,
	    String position) {
	Project project = this.projectDAO.getByID(projectID);
	Staff staff = this.staffDAO.getByID(staffID);

	// If this action is not authorized,
	// return.
	if (!this.authHelper.isActionAuthorized(staff)
		|| !this.authHelper.isActionAuthorized(project)) {
	    return;
	}

	ManagerAssignment assignment = new ManagerAssignment();
	assignment.setProject(project);
	assignment.setManager(staff);
	assignment.setProjectPosition(position);
	this.staffDAO.assignProjectManager(assignment);
    }

    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public void unassignProjectManager(long projectID, long staffID) {
	Project project = this.projectDAO.getByID(projectID);
	Staff staff = this.staffDAO.getByID(staffID);
	if (!this.authHelper.isActionAuthorized(staff)
		|| !this.authHelper.isActionAuthorized(project)) {
	    return;
	}
	// If authorized, continue with the action.
	this.staffDAO.unassignProjectManager(projectID, staffID);
    }

    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
    @Override
    @Transactional
    public void unassignAllProjectManagers(long projectID) {
	Project project = this.projectDAO.getByID(projectID);
	if (!this.authHelper.isActionAuthorized(project)) {
	    return;
	}
	this.staffDAO.unassignAllProjectManagers(projectID);
    }

    @Override
    @Transactional
    public Staff getWithAllCollectionsByID(long id) {
	Staff stf = this.staffDAO.getWithAllCollectionsByID(id);
	if (this.authHelper.isActionAuthorized(stf)) {
	    return stf;
	}
	return new Staff();
    }

    @Override
    @Transactional
    public void unassignTeam(long teamID, long staffID) {
	Team team = this.teamDAO.getByID(teamID);
	Staff staff = this.staffDAO.getByID(staffID);
	if (!this.authHelper.isActionAuthorized(staff)
		|| !this.authHelper.isActionAuthorized(team)) {
	    return;
	}
	this.staffDAO.unassignTeam(teamID, staffID);
    }

    @Override
    @Transactional
    public void unassignAllTeams(long staffID) {
	Staff staff = this.staffDAO.getByID(staffID);
	if (!this.authHelper.isActionAuthorized(staff)) {
	    return;
	}
	this.staffDAO.unassignAllTeams(staffID);
    }

    @Override
    @Transactional
    public void assignTeam(StaffTeamAssignment stAssign) {
	Staff staff = this.staffDAO.getByID(stAssign.getStaffID());
	Team team = this.teamDAO.getByID(stAssign.getTeamID());
	if (!this.authHelper.isActionAuthorized(staff)
		|| !this.authHelper.isActionAuthorized(team)) {
	    return;
	}
	this.staffDAO.assignTeam(stAssign);
    }

    @Override
    @Transactional
    public List<Staff> list(Long companyID) {
	return this.staffDAO.list(companyID);
    }

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

    @Override
    @Transactional
    public String getNameByID(long staffID) {
	return this.staffDAO.getNameByID(staffID);
    }

    @Override
    @Transactional
    public void createFromOrigin(Staff staff, String origin, String originID) {
	if (origin.equals(SystemUser.OBJECT_NAME)) {
	    SystemUser user = this.systemUserDAO.getByID(Long
		    .parseLong(originID));

	    if (user == null) {
		staff.setCompany(this.authHelper.getAuth().getCompany());
		this.staffDAO.create(staff);
	    } else {
		// Get the company from the user.
		// Update the staff.
		staff.setCompany(user.getCompany());
		this.staffDAO.create(staff);

		// If coming from the system user,
		// attach relationship with user.
		user.setStaff(staff);
		this.systemUserDAO.update(user);
	    }

	    return;
	}

	// Create the staff first since to attach it's relationship
	// with the company.
	AuthenticationToken auth = this.authHelper.getAuth();
	Company authCompany = auth.getCompany();
	staff.setCompany(authCompany);
	this.staffDAO.create(staff);
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
	    Status attnStat = attendance.getStatus();

	    // Construct the event bean for this attendance.
	    CalendarEventBean event = new CalendarEventBean();
	    event.setStart(start);
	    event.setTitle(attnStat.name());
	    event.setId(start);
	    event.setClassName(attnStat.css());
	    event.setAttendanceStatus(String.valueOf(attendance.getStatus()
		    .id()));
	    event.setAttendanceWage(String.valueOf(attendance.getWage()));
	    if (attnStat == Status.OVERTIME) {
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
	List<TaskGanttBean> ganttBeanList = new ArrayList<TaskGanttBean>();

	// Add myself.
	TaskGanttBean myGanttBean = new TaskGanttBean(staff);
	ganttBeanList.add(myGanttBean);

	// Get the gantt parent data.
	for (ManagerAssignment assigns : staff.getAssignedManagers()) {

	    // Add all projects.
	    Project proj = assigns.getProject();
	    TaskGanttBean projectBean = new TaskGanttBean(proj, myGanttBean);
	    ganttBeanList.add(projectBean);

	    // For each milestone in this project, add.
	    for (Milestone milestone : proj.getMilestones()) {
		TaskGanttBean milestoneBean = new TaskGanttBean(milestone,
			projectBean);
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

	    TaskGanttBean ganttBean = new TaskGanttBean(task, parentId);
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

	// Get the tasks (children) of each parent.
	for (Task task : staff.getTasks()) {
	    int taskStatusInt = task.getStatus();
	    TaskStatus taskStatus = TaskStatus.of(taskStatusInt);
	    Integer statCount = taskStatusMap.get(taskStatus) == null ? 1
		    : taskStatusMap.get(taskStatus) + 1;
	    taskStatusMap.put(taskStatus, statCount);
	}

	return taskStatusMap;
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
    public Map<Status, Map<String, Double>> getAttendanceStatusCountMap(
	    Staff staff, Set<Attendance> attendanceList) {
	String statusCount = "statusCount";
	String equivalentWage = "equivalentWage";

	// And count number per status.
	Map<Status, Map<String, Double>> attendanceStatusMap = new HashMap<Status, Map<String, Double>>();

	for (Attendance attendance : attendanceList) {

	    Status attnStat = attendance.getStatus();

	    // Get and set status count.
	    Double statCount = attendanceStatusMap.get(attnStat) == null ? 1
		    : attendanceStatusMap.get(attnStat).get(statusCount) + 1;
	    Map<String, Double> breakdown = new HashMap<String, Double>();
	    breakdown.put(statusCount, statCount);
	    breakdown.put(equivalentWage, statCount * staff.getWage());
	    attendanceStatusMap.put(attnStat, breakdown);
	}

	return attendanceStatusMap;
    }
}
