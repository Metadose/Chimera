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

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.ConstantsRedis;
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
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.pojo.JSONCalendarEvent;
import com.cebedo.pmsys.pojo.JSONTimelineGantt;
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

	// Security check.
	if (!this.authHelper.hasAccess(company)) {
	    this.messageHelper.unauthorized(Company.OBJECT_NAME, company.getId());
	    return new ArrayList<Staff>();
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_CONVERT_FILE, Company.OBJECT_NAME, company.getId(),
		MultipartFile.class.getName(), multipartFile.getOriginalFilename());

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
			double wage = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
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

	Company company = null;
	List<Staff> refinedStaff = new ArrayList<Staff>();
	for (Staff staff : staffList) {

	    // Security check.
	    if (!this.authHelper.isActionAuthorized(staff)) {
		this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
		return new ArrayList<Staff>();
	    }
	    company = company == null ? staff.getCompany() : company;

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

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Company.OBJECT_NAME, company.getId(),
		Staff.OBJECT_NAME);

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
	this.messageHelper.send(AuditAction.ACTION_CREATE, Staff.OBJECT_NAME, staff.getId());

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateCreate(Staff.OBJECT_NAME, staff.getFullName());
    }

    /**
     * Get staff by id.
     */
    @Override
    @Transactional
    public Staff getByID(long id) {
	Staff stf = this.staffDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(stf)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, stf.getId());
	    return new Staff();
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET, Staff.OBJECT_NAME, stf.getId());

	// Return obj.
	return stf;
    }

    /**
     * Update a staff.
     */
    @Override
    @Transactional
    public String update(Staff staff) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(staff)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_UPDATE, Staff.OBJECT_NAME, staff.getId());

	// Do service.
	this.staffDAO.update(staff);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUpdate(Staff.OBJECT_NAME, staff.getFullName());
    }

    /**
     * Delete a staff.
     */
    @Override
    @Transactional
    public String delete(long id) {
	Staff stf = this.staffDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(stf)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, stf.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_DELETE, Staff.OBJECT_NAME, stf.getId());

	this.staffDAO.delete(id);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateDelete(Staff.OBJECT_NAME, stf.getFullName());
    }

    /**
     * List all staff.
     */
    @Override
    @Transactional
    public List<Staff> list() {

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Staff.OBJECT_NAME);

	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    // Return list.
	    return this.staffDAO.list(null);
	}

	// Log info.
	Company co = token.getCompany();

	// Return non-super list.
	return this.staffDAO.list(co.getId());
    }

    /**
     * List all staff with all collections.
     */
    @Override
    @Transactional
    public List<Staff> listWithAllCollections() {

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Staff.OBJECT_NAME);

	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    // Return list.
	    return this.staffDAO.listWithAllCollections(null);
	}

	// Log info.
	Company co = token.getCompany();

	// Return list.
	return this.staffDAO.listWithAllCollections(co.getId());
    }

    /**
     * Get with all collections and id.
     */
    @Override
    @Transactional
    public Staff getWithAllCollectionsByID(long id) {
	Staff stf = this.staffDAO.getWithAllCollectionsByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(stf)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, stf.getId());
	    return new Staff();
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET, Staff.OBJECT_NAME, stf.getId());

	// Return obj.
	return stf;
    }

    /**
     * List all staff.
     */
    @Override
    @Transactional
    public List<Staff> list(Long companyID) {

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Staff.OBJECT_NAME);

	AuthenticationToken auth = this.authHelper.getAuth();
	if (auth.isSuperAdmin()) {
	    // Return list.
	    return this.staffDAO.list(null);
	}

	// Return list.
	return this.staffDAO.list(companyID);
    }

    /**
     * List unassigned staff given a project.
     */
    @Override
    @Transactional
    public List<Staff> listUnassignedInProject(Long companyID, Project project) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return new ArrayList<Staff>();
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Project.OBJECT_NAME, project.getId(),
		Staff.OBJECT_NAME);

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
		// Do service.
		// Return success.
		staff.setCompany(auth.getCompany());
		this.staffDAO.create(staff);

		// Log.
		this.messageHelper.send(AuditAction.ACTION_CREATE, Staff.OBJECT_NAME, staff.getId());

		return AlertBoxGenerator.SUCCESS.generateCreate(Staff.OBJECT_NAME, staff.getFullName());

	    }

	    // Security check.
	    if (!this.authHelper.isActionAuthorized(user)) {
		this.messageHelper.unauthorized(SystemUser.OBJECT_NAME, user.getId());
		return AlertBoxGenerator.ERROR;
	    }

	    // Get the company from the user.
	    staff.setCompany(user.getCompany());

	    // Do service.
	    // Update the staff.
	    this.staffDAO.create(staff);

	    // If coming from the system user,
	    // attach relationship with user.
	    user.setStaff(staff);
	    this.systemUserDAO.update(user);

	    // Log.
	    this.messageHelper.send(AuditAction.ACTION_CREATE, Staff.OBJECT_NAME, staff.getId());

	    // Return success.
	    return AlertBoxGenerator.SUCCESS.generateCreate(Staff.OBJECT_NAME, staff.getFullName());
	}

	// Create the staff first since to attach it's relationship
	// with the company.
	Company authCompany = auth.getCompany();
	staff.setCompany(authCompany);

	// Do service.
	this.staffDAO.create(staff);

	// Log.
	this.messageHelper.send(AuditAction.ACTION_CREATE, Staff.OBJECT_NAME, staff.getId());

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateCreate(Staff.OBJECT_NAME, staff.getFullName());
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
    public String getCalendarJSON(Set<Attendance> attendanceList) {
	// Get calendar events.
	List<JSONCalendarEvent> jSONCalendarEvents = new ArrayList<JSONCalendarEvent>();

	Staff staff = null;
	for (Attendance attendance : attendanceList) {

	    staff = staff == null ? attendance.getStaff() : staff;

	    // Security check.
	    if (!this.authHelper.isActionAuthorized(attendance)) {
		this.messageHelper.unauthorized(ConstantsRedis.OBJECT_ATTENDANCE, attendance.getKey());
		return AlertBoxGenerator.ERROR;
	    }

	    Date myDate = attendance.getDate();
	    String start = DateUtils.formatDate(myDate, "yyyy-MM-dd");
	    AttendanceStatus attnStat = attendance.getStatus() == null ? AttendanceStatus.of(attendance
		    .getStatusID()) : attendance.getStatus();

	    // Construct the event bean for this attendance.
	    JSONCalendarEvent event = new JSONCalendarEvent();
	    event.setStart(start);
	    event.setTitle(attnStat.name());
	    event.setId(start);
	    event.setClassName(attnStat.css());
	    event.setAttendanceStatus(String.valueOf(attendance.getStatus()));
	    event.setAttendanceWage(String.valueOf(attendance.getWage()));
	    if (attnStat == AttendanceStatus.OVERTIME) {
		event.setBorderColor("Red");
	    }
	    jSONCalendarEvents.add(event);
	}

	// Log.
	if (attendanceList.size() > 0) {
	    this.messageHelper.send(AuditAction.ACTION_GET_JSON, Staff.OBJECT_NAME, staff.getId(),
		    JSONCalendarEvent.class.getName());
	}

	return new Gson().toJson(jSONCalendarEvents, ArrayList.class);
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

	// Security check.
	if (!this.authHelper.isActionAuthorized(staff)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET_JSON, Staff.OBJECT_NAME, staff.getId(),
		JSONTimelineGantt.class.getName());

	// Get gantt-data.
	List<JSONTimelineGantt> ganttBeanList = new ArrayList<JSONTimelineGantt>();

	// Add myself.
	JSONTimelineGantt myGanttBean = new JSONTimelineGantt(staff);
	ganttBeanList.add(myGanttBean);

	// Get the tasks (children) of each parent.
	for (Task task : staff.getTasks()) {

	    // Construct parent ID.
	    Project proj = task.getProject();
	    String parentId = Project.OBJECT_NAME + "-" + proj.getId();
	    JSONTimelineGantt jSONTimelineGantt = new JSONTimelineGantt(task, parentId);
	    ganttBeanList.add(jSONTimelineGantt);
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

	// Security check.
	if (!this.authHelper.isActionAuthorized(staff)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
	    return new HashMap<TaskStatus, Integer>();
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET_MAP, Staff.OBJECT_NAME, staff.getId(),
		TaskStatus.class.getName());

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

	// Log.
	if (attendanceList.size() > 0) {
	    this.messageHelper.send(AuditAction.ACTION_GET_MAP, Staff.OBJECT_NAME, attendanceList
		    .iterator().next().getStaff().getId(), AttendanceStatus.class.getName());
	}

	// And count number per status.
	Map<AttendanceStatus, Map<String, Double>> attendanceStatusMap = new HashMap<AttendanceStatus, Map<String, Double>>();
	Map<AttendanceStatus, Map<String, Double>> attendanceStatusSorted = new LinkedHashMap<AttendanceStatus, Map<String, Double>>();

	for (Attendance attendance : attendanceList) {

	    // Security check.
	    if (!this.authHelper.isActionAuthorized(attendance)) {
		this.messageHelper.unauthorized(ConstantsRedis.OBJECT_ATTENDANCE, attendance.getKey());
		return new HashMap<AttendanceStatus, Map<String, Double>>();
	    }

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

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Staff.OBJECT_NAME);

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

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_ASSIGN_MASS, Project.OBJECT_NAME, project.getId(),
		Staff.OBJECT_NAME);

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

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_UNASSIGN, Project.OBJECT_NAME, project.getId(),
		Staff.OBJECT_NAME, staffID);

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
	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_UNASSIGN_ALL, Project.OBJECT_NAME, project.getId(),
		Staff.OBJECT_NAME);

	project.setAssignedStaff(new HashSet<Staff>());
	this.projectDAO.merge(project);
	return AlertBoxGenerator.SUCCESS.generateUnassignAll(Staff.OBJECT_NAME);
    }

    @Transactional
    @Override
    public List<Staff> listUnassignedStaffInProject(Long companyID, Project project) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return new ArrayList<Staff>();
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, Project.OBJECT_NAME, project.getId(),
		Staff.OBJECT_NAME);

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

	// Security check.
	if (!this.authHelper.isActionAuthorized(projectPayroll)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_PAYROLL, projectPayroll.getKey());
	    return new ArrayList<Staff>();
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, ConstantsRedis.OBJECT_PAYROLL,
		projectPayroll.getKey(), Staff.OBJECT_NAME);

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

}
