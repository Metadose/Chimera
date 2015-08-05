package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.bean.CalendarEventBean;
import com.cebedo.pmsys.bean.GanttBean;
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.SystemUserDAO;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.ExcelHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DateUtils;
import com.cebedo.pmsys.wrapper.StaffWrapper;
import com.google.gson.Gson;

@Service
public class StaffServiceImpl implements StaffService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private LogHelper logHelper = new LogHelper();
    private static Logger logger = Logger.getLogger(Staff.OBJECT_NAME);
    private ExcelHelper excelHelper = new ExcelHelper();

    public static final String STAFF_ATTENDANCE_STATUS_COUNT = "statusCount";
    public static final String STAFF_ATTENDANCE_EQUIVALENT_WAGE = "equivalentWage";

    public static final int EXCEL_COLUMN_PREFIX = 1;
    public static final int EXCEL_COLUMN_FIRST = 2;
    public static final int EXCEL_COLUMN_MIDDLE = 3;
    public static final int EXCEL_COLUMN_LAST = 4;
    public static final int EXCEL_COLUMN_SUFFIX = 5;
    public static final int EXCEL_COLUMN_COMPANY_POSITION = 6;
    public static final int EXCEL_COLUMN_WAGE = 7;
    public static final int EXCEL_COLUMN_CONTACT_NUMBER = 8;
    public static final int EXCEL_COLUMN_EMAIL = 9;

    private StaffDAO staffDAO;
    private ProjectDAO projectDAO;
    private SystemUserDAO systemUserDAO;
    private CompanyDAO companyDAO;

    public void setCompanyDAO(CompanyDAO companyDAO) {
	this.companyDAO = companyDAO;
    }

    public void setSystemUserDAO(SystemUserDAO systemUserDAO) {
	this.systemUserDAO = systemUserDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
	this.staffDAO = staffDAO;
    }

    @Override
    @Transactional
    public List<Staff> convertExcelToStaffList(MultipartFile multipartFile, Company company) {
	try {

	    // Create Workbook instance holding reference to .xls file
	    // Get first/desired sheet from the workbook.
	    HSSFWorkbook workbook = new HSSFWorkbook(multipartFile.getInputStream());
	    HSSFSheet sheet = workbook.getSheetAt(0);

	    // Iterate through each rows one by one.
	    Iterator<Row> rowIterator = sheet.iterator();

	    // Construct estimate containers.
	    List<Staff> staffList = new ArrayList<Staff>();
	    while (rowIterator.hasNext()) {

		Row row = rowIterator.next();
		int rowCountDisplay = row.getRowNum() + 1;

		// Skip first line.
		if (rowCountDisplay <= 1) {
		    continue;
		}

		// For each row, iterate through all the columns
		Iterator<Cell> cellIterator = row.cellIterator();

		// Every row, is a Staff object.
		Staff staff = new Staff();
		staff.setCompany(company);

		while (cellIterator.hasNext()) {

		    // Cell in this row and column.
		    Cell cell = cellIterator.next();
		    int colCountDisplay = cell.getColumnIndex() + 1;

		    switch (colCountDisplay) {

		    case EXCEL_COLUMN_PREFIX:
			String prefix = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			staff.setPrefix(prefix);
			continue;

		    case EXCEL_COLUMN_FIRST:
			String first = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			staff.setFirstName(first);
			continue;

		    case EXCEL_COLUMN_MIDDLE:
			String middle = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			staff.setMiddleName(middle);
			continue;

		    case EXCEL_COLUMN_LAST:
			String last = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			staff.setLastName(last);
			continue;

		    case EXCEL_COLUMN_SUFFIX:
			String sfx = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			staff.setSuffix(sfx);
			continue;

		    case EXCEL_COLUMN_COMPANY_POSITION:
			String position = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			staff.setCompanyPosition(position);
			continue;

		    case EXCEL_COLUMN_WAGE:
			double wage = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			staff.setWage(wage);
			continue;

		    case EXCEL_COLUMN_CONTACT_NUMBER:
			Object contactNumber = (Object) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? "" : this.excelHelper.getValueAsExpected(workbook, cell));
			staff.setContactNumber(contactNumber instanceof Double ? String
				.valueOf(contactNumber) : (String) contactNumber);
			continue;

		    case EXCEL_COLUMN_EMAIL:
			String email = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			staff.setEmail(email);
			continue;

		    }
		}

		staffList.add(staff);
	    }
	    return staffList;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return new ArrayList<Staff>();
    }

    @Override
    @Transactional
    public List<Staff> createOrGetStaffInList(List<Staff> staffList) {
	List<Staff> refinedStaff = new ArrayList<Staff>();
	for (Staff staff : staffList) {

	    // Search by name.
	    // If the staff already exists.
	    // Use staff in DB instead of the one from Excel.
	    Staff staffByName = this.staffDAO.getStaffByName(staff);
	    if (staffByName != null) {
		refinedStaff.add(staffByName);
		continue;
	    }
	    create(staff);
	    refinedStaff.add(staff);
	}
	return refinedStaff;
    }

    /**
     * Create a new staff.
     */
    @Override
    @Transactional
    public String create(Staff staff) {
	AuthenticationToken auth = this.authHelper.getAuth();

	// Do service.
	// Create the staff first since to attach it's relationship
	// with the company.
	Company authCompany = auth.getCompany();
	staff.setCompany(authCompany);
	this.staffDAO.create(staff);

	// Log and notify.
	this.messageHelper.send(auth, AuditAction.CREATE, Staff.OBJECT_NAME, staff.getId());

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateCreate(Staff.OBJECT_NAME, staff.getFullName());
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
	    logger.info(this.logHelper.logGetObject(auth, Staff.OBJECT_NAME, id, stf.getFullName()));

	    // Return obj.
	    return stf;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET, Staff.OBJECT_NAME,
		stf.getId(), stf.getFullName()));

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
	    this.messageHelper.send(auth, AuditAction.UPDATE, Staff.OBJECT_NAME, staff.getId());

	    // Do service.
	    this.staffDAO.update(staff);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateUpdate(Staff.OBJECT_NAME, staff.getFullName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.UPDATE, Staff.OBJECT_NAME,
		staff.getId(), staff.getFullName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateUpdate(Staff.OBJECT_NAME, staff.getFullName());
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
	    this.messageHelper.send(auth, AuditAction.DELETE, Staff.OBJECT_NAME, stf.getId());
	    this.staffDAO.delete(id);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateDelete(Staff.OBJECT_NAME, stf.getFullName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.DELETE, Staff.OBJECT_NAME,
		stf.getId(), stf.getFullName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateDelete(Staff.OBJECT_NAME, stf.getFullName());
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
	    logger.info(this.logHelper.logListAsSuperAdmin(token, Staff.OBJECT_NAME));

	    // Return list.
	    return this.staffDAO.list(null);
	}

	// Log info.
	Company co = token.getCompany();
	logger.info(this.logHelper.logListFromCompany(token, Staff.OBJECT_NAME, co));

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
	    logger.info(this.logHelper.logListWithCollectionsAsSuperAdmin(token, Staff.OBJECT_NAME));

	    // Return list.
	    return this.staffDAO.listWithAllCollections(null);
	}

	// Log info.
	Company co = token.getCompany();
	logger.info(this.logHelper.logListWithCollectionsFromCompany(token, Staff.OBJECT_NAME, co));

	// Return list.
	return this.staffDAO.listWithAllCollections(co.getId());
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
	    logger.info(this.logHelper.logGetObjectWithAllCollections(auth, Staff.OBJECT_NAME, id,
		    stf.getFullName()));

	    // Return obj.
	    return stf;
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET_WITH_COLLECTIONS,
		Staff.OBJECT_NAME, id, stf.getFullName()));

	// Return empty.
	return new Staff();
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
	    logger.info(this.logHelper.logListAsSuperAdmin(auth, Staff.OBJECT_NAME));

	    // Return list.
	    return this.staffDAO.list(null);
	}

	// Log info.
	Company co = this.companyDAO.getByID(companyID);
	logger.info(this.logHelper.logListFromCompany(auth, Staff.OBJECT_NAME, co));

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

	    // Complete list.
	    List<Staff> companyStaffList = this.staffDAO.list(companyID);
	    List<StaffWrapper> wrappedStaffList = StaffWrapper.wrap(companyStaffList);

	    // Staff assigned as staff.
	    List<StaffWrapper> assignedStaffList = StaffWrapper.wrapSet(project.getAssignedStaff());

	    // Remove assigned managers.
	    // Remove assigned staff.
	    wrappedStaffList.removeAll(assignedStaffList);

	    // Return as unwrapped.
	    return StaffWrapper.unwrap(StaffWrapper.removeEmptyNames(wrappedStaffList));
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
		Staff.PROPERTY_TRANSIENT_FULL_NAME, staff.getId(), staff.getFullName()));
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
	    SystemUser user = this.systemUserDAO.getByID(Long.parseLong(originID));

	    if (user == null) {

		// Get the company from the executor.
		staff.setCompany(auth.getCompany());

		if (this.authHelper.isActionAuthorized(staff)) {

		    // Log and notify.
		    this.messageHelper.send(auth, AuditAction.CREATE, Staff.OBJECT_NAME, staff.getId());

		    // Do service.
		    this.staffDAO.create(staff);

		    // Return success.
		    return AlertBoxGenerator.SUCCESS.generateCreate(Staff.OBJECT_NAME,
			    staff.getFullName());
		}
	    } else {
		// Get the company from the user.
		staff.setCompany(user.getCompany());

		if (this.authHelper.isActionAuthorized(staff)) {

		    // Log and notify.
		    this.messageHelper.send(auth, AuditAction.CREATE, Staff.OBJECT_NAME, staff.getId());

		    // Do service.
		    // Update the staff.
		    this.staffDAO.create(staff);

		    // If coming from the system user,
		    // attach relationship with user.
		    user.setStaff(staff);
		    this.systemUserDAO.update(user);

		    // Return success.
		    return AlertBoxGenerator.SUCCESS.generateCreate(Staff.OBJECT_NAME,
			    staff.getFullName());
		}
	    }

	    // Log warn.
	    logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE, Staff.OBJECT_NAME,
		    staff.getId(), staff.getFullName()));

	    // Return fail.
	    return AlertBoxGenerator.FAILED.generateCreate(Staff.OBJECT_NAME, staff.getFullName());
	}

	// Create the staff first since to attach it's relationship
	// with the company.
	Company authCompany = auth.getCompany();
	staff.setCompany(authCompany);
	if (this.authHelper.isActionAuthorized(staff)) {

	    // Log and notify.
	    this.messageHelper.send(auth, AuditAction.CREATE, Staff.OBJECT_NAME, staff.getId());

	    // Do service.
	    this.staffDAO.create(staff);

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateCreate(Staff.OBJECT_NAME, staff.getFullName());
	}

	// Log warn.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.CREATE, Staff.OBJECT_NAME,
		staff.getId(), staff.getFullName()));

	// Return fail.
	return AlertBoxGenerator.FAILED.generateCreate(Staff.OBJECT_NAME, staff.getFullName());
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

	    Date myDate = attendance.getDate();
	    String start = DateUtils.formatDate(myDate, "yyyy-MM-dd");
	    AttendanceStatus attnStat = attendance.getStatus() == null ? AttendanceStatus.of(attendance
		    .getStatusID()) : attendance.getStatus();

	    // Construct the event bean for this attendance.
	    CalendarEventBean event = new CalendarEventBean();
	    event.setStart(start);
	    event.setTitle(attnStat.name());
	    event.setId(start);
	    event.setClassName(attnStat.css());
	    event.setAttendanceStatus(String.valueOf(attendance.getStatus()));
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
	List<Long> addedProjects = new ArrayList<Long>();
	List<Long> addedMilestones = new ArrayList<Long>();

	// Get the tasks (children) of each parent.
	for (Task task : staff.getTasks()) {

	    // Get the data for the gantt chart.
	    // Get the parent of this task.
	    String parentId = "";
	    Project proj = task.getProject();

	    // If this milestone has been added,
	    // then simply pass the id.
	    Milestone milestone = task.getMilestone();
	    if (milestone != null) {

		// Else, add this project.
		// Then add all milestones.
		if (!addedMilestones.contains(milestone.getId())) {

		    Project project = milestone.getProject();
		    GanttBean projectBean = new GanttBean(project, myGanttBean);
		    long projID = project.getId();

		    // If the project has not yet been added.
		    if (!addedProjects.contains(projID)) {
			addedProjects.add(projID);
			ganttBeanList.add(projectBean);
		    }

		    // For each milestone in this project, add.
		    for (Milestone projectMilestone : project.getMilestones()) {

			// If has already been added,
			// continue.
			if (addedMilestones.contains(projectMilestone.getId())) {
			    continue;
			}
			GanttBean milestoneBean = new GanttBean(projectMilestone, projectBean);
			ganttBeanList.add(milestoneBean);
			addedMilestones.add(projectMilestone.getId());
		    }

		}

		// Construct the id.
		parentId = Milestone.OBJECT_NAME + "-" + task.getMilestone().getId();

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

	// And count number per status.
	Map<AttendanceStatus, Map<String, Double>> attendanceStatusMap = new HashMap<AttendanceStatus, Map<String, Double>>();
	Map<AttendanceStatus, Map<String, Double>> attendanceStatusSorted = new LinkedHashMap<AttendanceStatus, Map<String, Double>>();

	for (Attendance attendance : attendanceList) {

	    AttendanceStatus attnStat = attendance.getStatus();

	    // Get and set status count.
	    Double statCount = attendanceStatusMap.get(attnStat) == null ? 1 : attendanceStatusMap.get(
		    attnStat).get(STAFF_ATTENDANCE_STATUS_COUNT) + 1;
	    Map<String, Double> breakdown = new HashMap<String, Double>();
	    breakdown.put(STAFF_ATTENDANCE_STATUS_COUNT, statCount);
	    double value = attnStat == AttendanceStatus.ABSENT ? 0 : statCount * attendance.getWage();
	    breakdown.put(STAFF_ATTENDANCE_EQUIVALENT_WAGE, value);
	    attendanceStatusMap.put(attnStat, breakdown);
	}

	// If status count is null,
	// Add it as zero.
	for (AttendanceStatus status : AttendanceStatus.class.getEnumConstants()) {
	    if (status == AttendanceStatus.DELETE) {
		continue;
	    }
	    Map<String, Double> breakdown = attendanceStatusMap.get(status);
	    if (breakdown == null) {
		breakdown = new HashMap<String, Double>();
		breakdown.put(STAFF_ATTENDANCE_STATUS_COUNT, (double) 0);
		breakdown.put(STAFF_ATTENDANCE_EQUIVALENT_WAGE, (double) 0);
		attendanceStatusSorted.put(status, breakdown);
	    }
	    attendanceStatusSorted.put(status, breakdown);
	}

	return attendanceStatusSorted;
    }

    /**
     * List all in company except given staff.
     */
    @Transactional
    @Override
    public List<Staff> listExcept(Long coID, Set<Staff> doNotInclude) {

	// Get all staff from the company.
	List<Staff> companyStaffList = this.staffDAO.list(coID);
	List<StaffWrapper> wrappedCompanyStaffList = StaffWrapper.wrap(companyStaffList);

	// Staff to NOT include.
	List<StaffWrapper> wrappedDoNotInclude = StaffWrapper.wrapSet(doNotInclude);

	// Company list (minus) do not include list = result list.
	wrappedCompanyStaffList.removeAll(wrappedDoNotInclude);

	// Return the result.
	return StaffWrapper.unwrap(wrappedCompanyStaffList);
    }

    @Transactional
    @Override
    public String assignStaffMass(Project project) {

	// Transform the array of id's to
	// actual objects.
	long[] staffIDs = project.getStaffIDs();
	Set<Staff> staffList = new HashSet<Staff>();
	for (long id : staffIDs) {
	    Staff staff = this.staffDAO.getWithAllCollectionsByID(id);
	    staffList.add(staff);
	}

	// Get the existing and append.
	Set<Staff> assignedStaffList = project.getAssignedStaff();
	assignedStaffList.addAll(staffList);
	project.setAssignedStaff(assignedStaffList);

	// Update.
	this.projectDAO.merge(project);

	return AlertBoxGenerator.SUCCESS.generateAssignEntries(Staff.OBJECT_NAME);
    }

    @Transactional
    @Override
    public String unassignStaffMember(Project project, long staffID) {

	// Get index of staff to remove.
	Set<Staff> assignedStaffList = project.getAssignedStaff();
	Set<Staff> newStaffList = new HashSet<Staff>();
	for (Staff staff : assignedStaffList) {
	    if (staff.getId() == staffID) {
		continue;
	    }
	    newStaffList.add(staff);
	}

	// Do service.
	project.setAssignedStaff(newStaffList);
	this.projectDAO.merge(project);

	// Construct response.
	Staff staff = this.staffDAO.getByID(staffID);
	return AlertBoxGenerator.SUCCESS.generateUnassign(Staff.OBJECT_NAME, staff.getFullName());
    }

    @Transactional
    @Override
    public String unassignAllStaffMembers(Project project) {
	project.setAssignedStaff(new HashSet<Staff>());
	this.projectDAO.merge(project);
	return AlertBoxGenerator.SUCCESS.generateUnassignAll(Staff.OBJECT_NAME);
    }

    @Transactional
    @Override
    public List<Staff> listUnassignedStaffInProject(Long companyID, Project project) {
	if (this.authHelper.isActionAuthorized(project)) {
	    // Full list.
	    List<Staff> companyStaffList = this.staffDAO.list(companyID);
	    List<StaffWrapper> wrappedStaffList = StaffWrapper.wrap(companyStaffList);

	    // Minus list.
	    List<StaffWrapper> assignedStaffList = StaffWrapper.wrapSet(project.getAssignedStaff());

	    // Do minus.
	    wrappedStaffList.removeAll(assignedStaffList);
	    return StaffWrapper.unwrap(StaffWrapper.removeEmptyNames(wrappedStaffList));
	}
	return new ArrayList<Staff>();
    }

    @Transactional
    @Override
    public List<Staff> listUnassignedStaffInProjectPayroll(Long companyID, ProjectPayroll projectPayroll) {

	Project project = projectPayroll.getProject();

	if (this.authHelper.isActionAuthorized(project)) {
	    // Full list.
	    List<Staff> companyStaffList = this.staffDAO.list(companyID);
	    List<StaffWrapper> wrappedStaffList = StaffWrapper.wrap(companyStaffList);

	    // Minus list.
	    List<StaffWrapper> assignedStaffList = StaffWrapper.wrapSet(projectPayroll.getStaffList());

	    // Do minus.
	    wrappedStaffList.removeAll(assignedStaffList);
	    return StaffWrapper.unwrap(StaffWrapper.removeEmptyNames(wrappedStaffList));
	}
	return new ArrayList<Staff>();
    }

    @Override
    @Transactional
    public List<Staff> listWithUsers(Long companyID) {
	List<Staff> staffList = this.staffDAO.list(companyID);
	List<Staff> returnList = new ArrayList<Staff>();

	for (Staff staff : staffList) {
	    if (staff.getUser() == null) {
		continue;
	    }
	    returnList.add(staff);
	}
	return returnList;
    }

    @Override
    @Transactional
    public List<Staff> listWithUsersAndFilter(Long companyID, Set<Staff> filterList) {

	// Get staff with users.
	List<Staff> staffList = listWithUsers(companyID);

	// Wrap the staff list and filter list.
	// Remove filterlist from staff list.
	List<StaffWrapper> wrappedStaffList = StaffWrapper.wrap(staffList);
	List<StaffWrapper> wrappedFilterList = StaffWrapper.wrapSet(filterList);
	wrappedStaffList.removeAll(wrappedFilterList);

	// Return unwrapped.
	return StaffWrapper.unwrap(wrappedStaffList);
    }

}
