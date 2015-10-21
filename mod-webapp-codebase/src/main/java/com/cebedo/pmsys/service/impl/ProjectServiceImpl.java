package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.bean.StatisticsEstimateCost;
import com.cebedo.pmsys.bean.StatisticsProgramOfWorks;
import com.cebedo.pmsys.bean.StatisticsProject;
import com.cebedo.pmsys.bean.StatisticsStaff;
import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.EquipmentExpense;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.domain.IExpense;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.CalendarEventType;
import com.cebedo.pmsys.enums.EstimateCostType;
import com.cebedo.pmsys.enums.ProjectStatus;
import com.cebedo.pmsys.enums.SortOrder;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.ExcelHelper;
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
import com.cebedo.pmsys.service.DeliveryService;
import com.cebedo.pmsys.service.EquipmentExpenseService;
import com.cebedo.pmsys.service.EstimateCostService;
import com.cebedo.pmsys.service.ExpenseService;
import com.cebedo.pmsys.service.ProjectPayrollService;
import com.cebedo.pmsys.service.ProjectService;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.service.TaskService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DateUtils;
import com.cebedo.pmsys.validator.MultipartFileValidator;
import com.cebedo.pmsys.validator.ProjectValidator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

@Service
public class ProjectServiceImpl implements ProjectService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private ValidationHelper validationHelper = new ValidationHelper();
    private ExcelHelper xlsHelp = new ExcelHelper();

    private ProjectDAO projectDAO;
    private CompanyDAO companyDAO;
    private ProjectAuxValueRepo projectAuxValueRepo;
    private StaffService staffService;
    private TaskService taskService;
    private EstimateCostService estimateCostService;

    // For the balance sheet.
    private ProjectPayrollService projectPayrollService;
    private DeliveryService deliveryService;
    private EquipmentExpenseService equipmentExpenseService;
    private ExpenseService expenseService;

    @Autowired
    @Qualifier(value = "expenseService")
    public void setExpenseService(ExpenseService expenseService) {
	this.expenseService = expenseService;
    }

    @Autowired
    @Qualifier(value = "equipmentExpenseService")
    public void setEquipmentExpenseService(EquipmentExpenseService equipmentExpenseService) {
	this.equipmentExpenseService = equipmentExpenseService;
    }

    @Autowired
    @Qualifier(value = "deliveryService")
    public void setDeliveryService(DeliveryService deliveryService) {
	this.deliveryService = deliveryService;
    }

    @Autowired
    @Qualifier(value = "projectPayrollService")
    public void setProjectPayrollService(ProjectPayrollService projectPayrollService) {
	this.projectPayrollService = projectPayrollService;
    }

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
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
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
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
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
	String invalid = this.taskService.createMassTasks(project, includeTasks, result);
	if (invalid != null) {
	    return invalid;
	}

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
	this.messageHelper.auditableID(AuditAction.ACTION_CREATE, Project.OBJECT_NAME, project.getId(),
		project.getName());

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
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
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

	staffList = this.staffService.refineStaffList(staffList, result);

	// There was a problem with the list of staff you provided. Please
	// review the list and try again.
	if (staffList == null) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_STAFF_MASS_UPLOAD_GENERIC);
	}

	// Log.
	this.messageHelper.auditableKey(AuditAction.ACTION_CREATE_MASS, Project.OBJECT_NAME,
		proj.getId(), Staff.OBJECT_NAME, "Mass", proj, "Mass");

	// If everything's ok.
	assignAllStaffToProject(proj, staffList);

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
	for (Staff staff : projectStaff) {
	    this.messageHelper.auditableID(AuditAction.ACTION_ASSIGN, Project.OBJECT_NAME, proj.getId(),
		    Staff.OBJECT_NAME, staff.getId(), proj, staff.getFullName());
	}
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
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
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
	this.messageHelper.auditableKey(AuditAction.ACTION_UPDATE, Project.OBJECT_NAME, project.getId(),
		"", "", project, project.getName());

	// Response for the user.
	return AlertBoxGenerator.SUCCESS.generateUpdate(Project.OBJECT_NAME, project.getName());
    }

    @Override
    @Transactional
    public List<Project> list() {

	AuthenticationToken token = this.authHelper.getAuth();
	this.messageHelper.nonAuditableListNoAssoc(AuditAction.ACTION_LIST, Project.OBJECT_NAME);

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
	    this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_GET, Project.OBJECT_NAME, id);
	    return project;
	}

	// Log a warning.
	this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
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
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
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
	this.messageHelper.auditableID(AuditAction.ACTION_DELETE, Project.OBJECT_NAME, project.getId(),
		project.getName());

	// Success response.
	return AlertBoxGenerator.SUCCESS.generateDelete(Project.OBJECT_NAME, project.getName());
    }

    @Override
    @Transactional
    public List<Project> listWithAllCollections() {
	// Log.
	AuthenticationToken token = this.authHelper.getAuth();
	this.messageHelper.nonAuditableListNoAssoc(AuditAction.ACTION_LIST, Project.OBJECT_NAME);

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
	    this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_GET, Project.OBJECT_NAME, id);
	    return project;
	}

	// Log then return empty object.
	this.messageHelper.unauthorizedID(Project.OBJECT_NAME, id);
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
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return ""; // Returning empty since expecting a JSON.
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_GET_JSON, Project.OBJECT_NAME,
		proj.getId(), JSONTimelineGantt.class.getName());

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
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new HashMap<TaskStatus, Integer>();
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_GET_MAP, Project.OBJECT_NAME,
		proj.getId(), TaskStatus.class.getName());

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
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return ""; // Empty, expecting JSON.
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_GET_JSON, Project.OBJECT_NAME,
		proj.getId(), JSONCalendarEvent.class.getName());

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
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.auditableKey(AuditAction.ACTION_UPDATE, Project.OBJECT_NAME, project.getId(),
		"", "", project, project.getName());

	// Do service.
	project.setActualCompletionDate(null);
	project.setStatus(status);
	this.projectDAO.update(project);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateMarkAs(Project.OBJECT_NAME, project.getName());
    }

    @Override
    @Transactional
    public Set<AuditLog> logs(long projID) {
	// Get the task.
	Project project = this.projectDAO.getByID(projID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
	    return new HashSet<AuditLog>();
	}

	Set<AuditLog> logs = this.projectDAO.logs(projID);
	for (AuditLog log : logs) {
	    log.setAuditAction(AuditAction.of(log.getAction()));
	}
	return logs;
    }

    @Override
    @Transactional
    public HSSFWorkbook exportXLSBalanceSheet(long projID) {
	return exportXLSBalanceSheet(projID, null, null);
    }

    @Override
    @Transactional
    public HSSFWorkbook exportXLSBalanceSheet(long projID, Date startDate, Date endDate) {
	Project proj = this.projectDAO.getByIDWithAllCollections(projID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new HSSFWorkbook();
	}
	boolean isRange = startDate != null && endDate != null;

	// Construct sheet name.
	String sheetName = "";
	if (isRange) {
	    String dateFormat = "yyyy-MM-dd";
	    sheetName = String.format("%s to %s", DateUtils.formatDate(startDate, dateFormat),
		    DateUtils.formatDate(endDate, dateFormat));
	} else {
	    sheetName = "Balance Sheet";
	}
	this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_EXPORT, sheetName, projID);

	// Load lists.
	List<ProjectPayroll> payrolls = this.projectPayrollService.listDesc(proj, startDate, endDate);
	List<Delivery> deliveries = this.deliveryService.listDesc(proj, startDate, endDate);
	List<EquipmentExpense> equipmentExpenses = this.equipmentExpenseService.listDesc(proj, startDate,
		endDate);
	List<Expense> otherExpenses = this.expenseService.listDesc(proj, startDate, endDate);

	// Get totals.
	double totalPayroll = 0;
	double totalDelivery = 0;
	double totalEquipment = 0;
	double totalOthers = 0;
	double totalGrand = 0;
	if (isRange) {
	    StatisticsProject statistics = new StatisticsProject(payrolls, deliveries, equipmentExpenses,
		    otherExpenses);
	    totalPayroll = statistics.getSumPayroll();
	    totalDelivery = statistics.getSumDelivery();
	    totalEquipment = statistics.getSumEquipment();
	    totalOthers = statistics.getSumOtherExpenses();
	    totalGrand = totalPayroll + totalDelivery + totalEquipment + totalOthers;
	} else {
	    ProjectAux aux = this.projectAuxValueRepo.get(ProjectAux.constructKey(proj));
	    totalPayroll = aux.getGrandTotalPayroll();
	    totalDelivery = aux.getGrandTotalDelivery();
	    totalEquipment = aux.getGrandTotalEquipmentExpenses();
	    totalOthers = aux.getGrandTotalOtherExpenses();
	    totalGrand = aux.getCurrentTotalProject();
	}

	// Initialize.
	HSSFWorkbook wb = new HSSFWorkbook();
	HSSFSheet sheet = wb.createSheet(sheetName);

	// For grand total.
	int rowIndex = 0;
	HSSFRow row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue(proj.getName());
	row.createCell(1).setCellValue(sheetName);
	rowIndex++;
	rowIndex++;

	// For headers.
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Payroll");
	row.createCell(1).setCellValue("Total");
	row.createCell(2).setCellValue(totalPayroll);
	rowIndex++;

	// Setup the table.
	for (ProjectPayroll payroll : payrolls) {
	    HSSFRow expenseRow = sheet.createRow(rowIndex);
	    expenseRow.createCell(1).setCellValue(payroll.getStartEndDisplay());
	    expenseRow.createCell(2).setCellValue(payroll.getTotal());
	    rowIndex++;
	}
	rowIndex++;

	// For headers.
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Inventory");
	row.createCell(1).setCellValue("Total");
	row.createCell(2).setCellValue(totalDelivery);
	rowIndex++;

	// Setup the table.
	for (Delivery delivery : deliveries) {
	    HSSFRow expenseRow = sheet.createRow(rowIndex);
	    expenseRow.createCell(1).setCellValue(delivery.getName());
	    expenseRow.createCell(2).setCellValue(delivery.getGrandTotalOfMaterials());
	    rowIndex++;
	}
	rowIndex++;

	// For headers.
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Equipment");
	row.createCell(1).setCellValue("Total");
	row.createCell(2).setCellValue(totalEquipment);
	rowIndex++;

	// Setup the table.
	for (EquipmentExpense equipment : equipmentExpenses) {
	    HSSFRow expenseRow = sheet.createRow(rowIndex);
	    expenseRow.createCell(1).setCellValue(equipment.getName());
	    expenseRow.createCell(2).setCellValue(equipment.getCost());
	    rowIndex++;
	}
	rowIndex++;

	// For headers.
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Other Expenses");
	row.createCell(1).setCellValue("Total");
	row.createCell(2).setCellValue(totalOthers);
	rowIndex++;

	// Setup the table.
	for (Expense expense : otherExpenses) {
	    HSSFRow expenseRow = sheet.createRow(rowIndex);
	    expenseRow.createCell(1).setCellValue(expense.getName());
	    expenseRow.createCell(2).setCellValue(expense.getCost());
	    rowIndex++;
	}
	rowIndex++;
	rowIndex++;

	row = sheet.createRow(rowIndex);
	row.createCell(1).setCellValue("Grand Total");
	row.createCell(2).setCellValue(totalGrand);

	return wb;
    }

    @Override
    @Transactional
    public HSSFWorkbook exportXLSAnalysis(long projID) {
	Project proj = this.projectDAO.getByIDWithAllCollections(projID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new HSSFWorkbook();
	}

	// Construct sheet name.
	String sheetName = "Analysis";
	this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_EXPORT, sheetName, projID);

	// Start constructing the Excel.
	ProjectAux projAux = this.projectAuxValueRepo.get(ProjectAux.constructKey(proj));
	HSSFWorkbook wb = new HSSFWorkbook();
	HSSFSheet sheet = wb.createSheet(sheetName);
	int rowIndex = 0;
	HSSFRow row = sheet.createRow(rowIndex);
	rowIndex = this.xlsHelp.addSectionLabel("Project Analysis", sheet, rowIndex);
	rowIndex++;

	// Basic details.
	rowIndex = xlsAnalysisDetails(row, sheet, rowIndex, proj);

	// Project estimated cost.
	double plannedDirect = projAux.getGrandTotalCostsDirect();
	double plannedIndirect = projAux.getGrandTotalCostsIndirect();
	double actualDirect = projAux.getGrandTotalActualCostsDirect();
	double actualIndirect = projAux.getGrandTotalActualCostsIndirect();
	double plannedProjCost = plannedDirect + plannedIndirect;
	double actualProjCost = actualDirect + actualIndirect;
	xlsAnalysisEstimate(wb, row, projAux, plannedDirect, plannedIndirect, plannedProjCost,
		actualDirect, actualIndirect, actualProjCost, proj);

	// Pass Excel objects then process.
	xlsAnalysisExpenses(wb, row, proj);

	// Physical Target.
	rowIndex = xlsAnalysisPhysicalTarget(row, sheet, rowIndex, proj, plannedProjCost,
		actualProjCost);

	// Progress.
	rowIndex = xlsAnalysisProgress(row, sheet, rowIndex, proj);

	// Staff.
	rowIndex = xlsAnalysisStaff(row, sheet, rowIndex, proj);

	// Program of works.
	Set<Task> tasks = proj.getAssignedTasks();
	int numOfTasks = tasks.size();
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Number of Tasks");
	row.createCell(1).setCellValue(numOfTasks);
	rowIndex++;

	// Process tasks.
	StatisticsProgramOfWorks statisticsPOW = new StatisticsProgramOfWorks(tasks);

	// (Max) Which task took the most duration and actualDuration?
	// How long? Who did it?
	List<Task> maxPlannedDuration = statisticsPOW.getMaxDuration();
	List<Task> maxActualDuration = statisticsPOW.getMaxActualDuration();

	// (Min) Which task took the least duration and actualDuration?
	// How long? Who did it?
	List<Task> leastPlannedDuration = statisticsPOW.getMinDuration();
	List<Task> leastActualDuration = statisticsPOW.getMinActualDuration();

	// (Max) Biggest negative (duration - actualDuration)
	// How much? Who did it?
	List<Task> maxDifference = statisticsPOW.getMaxDifferenceDuration();

	// (Min) Least negative (duration - actualDuration)
	// How much? Who did it?
	List<Task> minDifference = statisticsPOW.getMinDifferenceDuration();

	// (Mean) Of all tasks: duration. How much?
	double meanPlanned = statisticsPOW.getMeanDuration();

	// (Mean) Of all tasks: actualDuration. How much?
	double meanActual = statisticsPOW.getMeanActualDuration();

	// (Difference of Means) duration - actualDuration. How much?
	double meanDiff = statisticsPOW.getMeanDifference();

	// Map of task status to number.
	Map<TaskStatus, Integer> taskStatusCountMap = getTaskStatusCountMap(proj);

	// Resize all sheets.
	for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
	    HSSFSheet wbSheet = wb.getSheetAt(sheetIndex);
	    wbSheet.setDefaultColumnWidth(20);
	    wbSheet.autoSizeColumn(0);
	    wbSheet.setZoom(85, 100);
	}

	return wb;
    }

    private void xlsAnalysisExpenses(HSSFWorkbook wb, HSSFRow row, Project proj) {
	int rowIndex = 0;
	List<ProjectPayroll> payrolls = this.projectPayrollService.listDesc(proj);
	List<Delivery> deliveries = this.deliveryService.listDesc(proj);
	List<EquipmentExpense> equipmentExpenses = this.equipmentExpenseService.listDesc(proj);
	List<Expense> otherExpenses = this.expenseService.listDesc(proj);

	// Analysis (Max).
	StatisticsProject statisticsProj = new StatisticsProject(payrolls, deliveries, equipmentExpenses,
		otherExpenses);
	List<ProjectPayroll> maxPayroll = statisticsProj.getMaxPayrolls();
	List<Delivery> maxDeliveries = statisticsProj.getMaxDelivery();
	List<EquipmentExpense> maxEquip = statisticsProj.getMaxEquipment();
	List<Expense> maxOtherExpenses = statisticsProj.getMaxOtherExpenses();

	// Most costly expenses for each type.
	HSSFSheet sheet = wb.createSheet("Expenses");
	rowIndex = this.xlsHelp.addSectionLabel("Maximum", sheet, rowIndex,
		"Expense(s) with the greatest value");
	rowIndex = this.xlsHelp.addSectionExpenses("Payrolls", row, sheet, rowIndex, maxPayroll);
	rowIndex = this.xlsHelp.addSectionExpenses("Deliveries", row, sheet, rowIndex, maxDeliveries);
	rowIndex = this.xlsHelp.addSectionExpenses("Equipment", row, sheet, rowIndex, maxEquip);
	rowIndex = this.xlsHelp.addSectionExpenses("Other Expenses", row, sheet, rowIndex,
		maxOtherExpenses);

	// Top expenses by category and altogether.
	Integer limit = 5;
	ImmutableList<ProjectPayroll> payrollsCostDesc = statisticsProj
		.getLimitedSortedByCostPayrolls(limit, SortOrder.DESCENDING);
	ImmutableList<Delivery> deliveryCostDesc = statisticsProj.getLimitedSortedByCostDeliveries(limit,
		SortOrder.DESCENDING);
	ImmutableList<EquipmentExpense> equipCostDesc = statisticsProj
		.getLimitedSortedByCostEquipment(limit, SortOrder.DESCENDING);
	ImmutableList<Expense> othersCostDesc = statisticsProj.getLimitedSortedByCostOtherExpenses(limit,
		SortOrder.DESCENDING);
	ImmutableList<IExpense> projDesc = statisticsProj.getLimitedSortedByCostProject(limit,
		SortOrder.DESCENDING);
	rowIndex = this.xlsHelp.addSectionLabel(String.format("Top %s Expenses", limit), sheet, rowIndex,
		"Most expensive");
	rowIndex = this.xlsHelp.addSectionExpenses("Payrolls", row, sheet, rowIndex, payrollsCostDesc);
	rowIndex = this.xlsHelp.addSectionExpenses("Deliveries", row, sheet, rowIndex, deliveryCostDesc);
	rowIndex = this.xlsHelp.addSectionExpenses("Equipment", row, sheet, rowIndex, equipCostDesc);
	rowIndex = this.xlsHelp.addSectionExpenses("Other Expenses", row, sheet, rowIndex,
		othersCostDesc);
	rowIndex = this.xlsHelp.addSectionExpenses("Overall Project", row, sheet, rowIndex, projDesc);

	// Analysis (Min).
	List<ProjectPayroll> minPayroll = statisticsProj.getMinPayrolls();
	List<Delivery> minDeliveries = statisticsProj.getMinDeliveries();
	List<EquipmentExpense> minEquip = statisticsProj.getMinEquipment();
	List<Expense> minOtherExpenses = statisticsProj.getMinOtherExpenses();
	rowIndex = this.xlsHelp.addSectionLabel("Minimum", sheet, rowIndex);
	rowIndex = this.xlsHelp.addSectionExpenses("Payrolls", row, sheet, rowIndex, minPayroll);
	rowIndex = this.xlsHelp.addSectionExpenses("Deliveries", row, sheet, rowIndex, minDeliveries);
	rowIndex = this.xlsHelp.addSectionExpenses("Equipment", row, sheet, rowIndex, minEquip);
	rowIndex = this.xlsHelp.addSectionExpenses("Other Expenses", row, sheet, rowIndex,
		minOtherExpenses);

	// Bottom expenses by category and altogether.
	ImmutableList<ProjectPayroll> payrollsCostAsc = statisticsProj
		.getLimitedSortedByCostPayrolls(limit, SortOrder.ASCENDING);
	ImmutableList<Delivery> deliveryCostAsc = statisticsProj.getLimitedSortedByCostDeliveries(limit,
		SortOrder.ASCENDING);
	ImmutableList<EquipmentExpense> equipCostAsc = statisticsProj
		.getLimitedSortedByCostEquipment(limit, SortOrder.ASCENDING);
	ImmutableList<Expense> othersCostAsc = statisticsProj.getLimitedSortedByCostOtherExpenses(limit,
		SortOrder.ASCENDING);
	ImmutableList<IExpense> projAsc = statisticsProj.getLimitedSortedByCostProject(limit,
		SortOrder.ASCENDING);
	rowIndex = this.xlsHelp.addSectionLabel(String.format("Top %s Least Expensive", limit), sheet,
		rowIndex);
	rowIndex = this.xlsHelp.addSectionExpenses("Payrolls", row, sheet, rowIndex, payrollsCostAsc);
	rowIndex = this.xlsHelp.addSectionExpenses("Deliveries", row, sheet, rowIndex, deliveryCostAsc);
	rowIndex = this.xlsHelp.addSectionExpenses("Equipment", row, sheet, rowIndex, equipCostAsc);
	rowIndex = this.xlsHelp.addSectionExpenses("Other Expenses", row, sheet, rowIndex,
		othersCostAsc);
	rowIndex = this.xlsHelp.addSectionExpenses("Overall Project", row, sheet, rowIndex, projAsc);

	// Analysis (Mean).
	double meanPayroll = statisticsProj.getMeanPayroll();
	double meanDeliveries = statisticsProj.getMeanDelivery();
	double meanEquip = statisticsProj.getMeanEquipment();
	double meanOtherExpenses = statisticsProj.getMeanOtherExpenses();
	double meanProject = statisticsProj.getMeanProject();
	Map<String, Double> labelValueMap = new HashMap<String, Double>();
	labelValueMap.put("Payroll", meanPayroll);
	labelValueMap.put("Deliveries", meanDeliveries);
	labelValueMap.put("Equipment", meanEquip);
	labelValueMap.put("Other Expenses", meanOtherExpenses);
	labelValueMap.put("Overall Project", meanProject);
	rowIndex = this.xlsHelp.addSectionFromMap("Means", row, sheet, rowIndex, labelValueMap);

	// Population.
	labelValueMap = new HashMap<String, Double>();
	double popPayroll = this.projectPayrollService.getSize(payrolls);
	double popDelivery = deliveries.size();
	double popEquip = equipmentExpenses.size();
	double popOtherExpenses = otherExpenses.size();
	double popProject = popPayroll + popDelivery + popEquip + popOtherExpenses;
	labelValueMap.put("Payroll", popPayroll);
	labelValueMap.put("Deliveries", popDelivery);
	labelValueMap.put("Equipment", popEquip);
	labelValueMap.put("Other Expenses", popOtherExpenses);
	labelValueMap.put("Overall Project", popProject);
	rowIndex = this.xlsHelp.addSectionFromMap("Number of Entries", row, sheet, rowIndex,
		labelValueMap);
    }

    /**
     * Analysis regarding staff members and attendances.
     * 
     * @param row
     * @param sheet
     * @param rowIndex
     * @param proj
     * @return
     */
    private int xlsAnalysisStaff(HSSFRow row, HSSFSheet sheet, int rowIndex, Project proj) {
	rowIndex = this.xlsHelp.addSectionLabel("Staff", sheet, rowIndex);

	// Number of staff members assigned to this project.
	Set<Staff> assignedStaff = proj.getAssignedStaff();
	int numOfAssignedStaff = assignedStaff.size();
	rowIndex = this.xlsHelp.addRow("Number of Assigned Staff", numOfAssignedStaff, row, sheet,
		rowIndex);

	// Mean salary per day.
	StatisticsStaff statisticsStaff = new StatisticsStaff(proj, assignedStaff);
	double meanWage = statisticsStaff.getMeanWage();
	rowIndex = this.xlsHelp.addRow("Salary Mean (Daily)", meanWage, row, sheet, rowIndex);

	// Summation of salary per day.
	double sumWage = statisticsStaff.getSumWage();
	rowIndex = this.xlsHelp.addRow("Salary Sum (Daily)", sumWage, row, sheet, rowIndex);

	// Staff list with the most absences. Get top 5 absentees.
	// Top 5 staff member per attendance status.
	int max = 5;
	ImmutableList<Entry<Staff, Integer>> topAbsent = statisticsStaff
		.getAttendancesByStatusDesc(AttendanceStatus.ABSENT, max);
	ImmutableList<Entry<Staff, Integer>> topOvertime = statisticsStaff
		.getAttendancesByStatusDesc(AttendanceStatus.OVERTIME, max);
	ImmutableList<Entry<Staff, Integer>> topLate = statisticsStaff
		.getAttendancesByStatusDesc(AttendanceStatus.LATE, max);
	ImmutableList<Entry<Staff, Integer>> topHalfday = statisticsStaff
		.getAttendancesByStatusDesc(AttendanceStatus.HALFDAY, max);
	ImmutableList<Entry<Staff, Integer>> topLeave = statisticsStaff
		.getAttendancesByStatusDesc(AttendanceStatus.LEAVE, max);

	// Bottom 5 staff member per attendance status.
	ImmutableList<Entry<Staff, Integer>> bottomAbsent = statisticsStaff
		.getAttendancesByStatusAsc(AttendanceStatus.ABSENT, max);
	ImmutableList<Entry<Staff, Integer>> bottomOvertime = statisticsStaff
		.getAttendancesByStatusAsc(AttendanceStatus.OVERTIME, max);
	ImmutableList<Entry<Staff, Integer>> bottomLate = statisticsStaff
		.getAttendancesByStatusAsc(AttendanceStatus.LATE, max);
	ImmutableList<Entry<Staff, Integer>> bottomHalfday = statisticsStaff
		.getAttendancesByStatusAsc(AttendanceStatus.HALFDAY, max);
	ImmutableList<Entry<Staff, Integer>> bottomLeave = statisticsStaff
		.getAttendancesByStatusAsc(AttendanceStatus.LEAVE, max);

	// Mean per attendance status.
	double meanAbsences = statisticsStaff.getMeanOf(AttendanceStatus.ABSENT);
	double meanOvertime = statisticsStaff.getMeanOf(AttendanceStatus.OVERTIME);
	double meanLate = statisticsStaff.getMeanOf(AttendanceStatus.LATE);
	double meanHalfday = statisticsStaff.getMeanOf(AttendanceStatus.HALFDAY);
	double meanLeave = statisticsStaff.getMeanOf(AttendanceStatus.LEAVE);

	rowIndex++;
	return rowIndex;
    }

    /**
     * Progress section of analysis Excel.
     * 
     * @param row
     * @param sheet
     * @param rowIndex
     * @param proj
     * @return
     */
    private int xlsAnalysisProgress(HSSFRow row, HSSFSheet sheet, int rowIndex, Project proj) {
	rowIndex = this.xlsHelp.addSectionLabel("Project Runtime", sheet, rowIndex);

	Date dateStart = proj.getDateStart();
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Date Start");
	row.createCell(1).setCellValue(DateUtils.formatDate(dateStart));
	rowIndex++;

	// If project is completed,
	// do post-project analysis.
	ProjectStatus projStatus = proj.getStatusEnum();
	if (projStatus == ProjectStatus.COMPLETED) {

	    Date dateCompletionActual = proj.getActualCompletionDate();
	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue("Date Completion (Actual)");
	    row.createCell(1).setCellValue(dateCompletionActual == null ? "(Not Set)"
		    : DateUtils.formatDate(dateCompletionActual));
	    rowIndex++;

	    // Expected project runtime.
	    int plannedNumOfDays = proj.getCalDaysTotal();
	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue("Number of Days (Planned)");
	    row.createCell(1).setCellValue(plannedNumOfDays);
	    rowIndex++;

	    if (dateCompletionActual != null) {
		// How many days was the actual project runtime?
		int actualNumOfDays = Days
			.daysBetween(new DateTime(dateStart), new DateTime(dateCompletionActual))
			.getDays();
		row = sheet.createRow(rowIndex);
		row.createCell(0).setCellValue("Number of Days (Actual)");
		row.createCell(1).setCellValue(actualNumOfDays);
		rowIndex++;

		// Distance between the planned and actual number of days.
		int difference = plannedNumOfDays - actualNumOfDays;
		String diffLabel = difference > 0 ? "Ahead of Schedule"
			: (difference < 0 ? "Delayed" : "On-Time");
		row = sheet.createRow(rowIndex);
		row.createCell(0).setCellValue("Number of Days (Difference)");
		row.createCell(1).setCellValue(difference);
		row.createCell(2).setCellValue(diffLabel);
		rowIndex++;

		// Actual vs Planned number of days.
		// How many days were actually used?
		double percentUsed = ((double) actualNumOfDays / (double) plannedNumOfDays) * 100;
		row = sheet.createRow(rowIndex);
		row.createCell(0).setCellValue("Number of Days (Percent Used)");
		row.createCell(1).setCellValue(percentUsed);
		rowIndex++;
	    }
	}
	// If project is not yet completed.
	else {
	    // Target date of completion.
	    Date dateCompletionTarget = proj.getTargetCompletionDate();
	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue("Date Completion (Target)");
	    row.createCell(1).setCellValue(DateUtils.formatDate(dateCompletionTarget));
	    rowIndex++;

	    // Expected project runtime.
	    int plannedNumOfDays = proj.getCalDaysTotal();
	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue("Number of Days (Planned)");
	    row.createCell(1).setCellValue(plannedNumOfDays);
	    rowIndex++;

	    // Number of days remaining.
	    int remainingNumOfDays = Days.daysBetween(new DateTime(new Date(System.currentTimeMillis())),
		    new DateTime(dateCompletionTarget)).getDays();
	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue("Number of Days (Remaining)");
	    row.createCell(1).setCellValue(remainingNumOfDays);
	    rowIndex++;

	    double remainingDaysPercent = ((double) remainingNumOfDays / (double) plannedNumOfDays)
		    * 100;
	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue("Percent of Days (Remaining)");
	    row.createCell(1).setCellValue(remainingDaysPercent);
	    rowIndex++;
	}
	rowIndex++;

	return rowIndex;
    }

    /**
     * Analysis regarding the physical target.
     * 
     * @param row
     * @param sheet
     * @param rowIndex
     * @param proj
     * @param projCost
     * @param actualProjCost
     * @return
     */
    private int xlsAnalysisPhysicalTarget(HSSFRow row, HSSFSheet sheet, int rowIndex, Project proj,
	    double projCost, double actualProjCost) {
	rowIndex = this.xlsHelp.addSectionLabel("Physical Target Details", sheet, rowIndex);

	double phyTarget = proj.getPhysicalTarget();
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Physical Target (sq.m.)");
	row.createCell(1).setCellValue(phyTarget);
	rowIndex++;

	double phyTargetCost = projCost / phyTarget;
	row = sheet.createRow(rowIndex); // PHP per square meter.
	row.createCell(0).setCellValue("Planned Phy. Tgt. Cost (PHP/sq.m.)");
	row.createCell(1).setCellValue(phyTargetCost);
	rowIndex++;

	if (proj.getStatusEnum() == ProjectStatus.COMPLETED) {
	    double actualPhyTargetCost = actualProjCost / phyTarget;
	    row = sheet.createRow(rowIndex); // PHP per square meter.
	    row.createCell(0).setCellValue("Actual Phy. Tgt. Cost (PHP/sq.m.)");
	    row.createCell(1).setCellValue(actualPhyTargetCost);
	    rowIndex++;

	    row = sheet.createRow(rowIndex); // PHP per square meter.
	    row.createCell(0).setCellValue("Difference Phy. Tgt. Cost (PHP/sq.m.)");
	    row.createCell(1).setCellValue(phyTargetCost - actualPhyTargetCost);
	    rowIndex++;
	}

	rowIndex++;
	return rowIndex;
    }

    /**
     * Analysis of project estimation and estimated costs.
     * 
     * @param wb
     * 
     * @param row
     * @param rowIndex
     * @param projAux
     * @param plannedDirect
     * @param plannedIndirect
     * @param projCost
     * @param actualDirect
     * @param actualIndirect
     * @param actualProjCost
     * @param proj
     * @return
     */
    private void xlsAnalysisEstimate(HSSFWorkbook wb, HSSFRow row, ProjectAux projAux,
	    double plannedDirect, double plannedIndirect, double projCost, double actualDirect,
	    double actualIndirect, double actualProjCost, Project proj) {

	HSSFSheet sheet = wb.createSheet("Estimate");
	int rowIndex = 0;
	rowIndex = this.xlsHelp.addSectionLabel("PROJECT COST ESTIMATE", sheet, rowIndex,
		IndexedColors.PALE_BLUE);
	rowIndex++;
	rowIndex = this.xlsHelp.addSectionLabel("SUM", sheet, rowIndex, IndexedColors.YELLOW,
		"Summation");
	rowIndex = this.xlsHelp.addSectionLabel("Cost Totals", sheet, rowIndex,
		"Cost total per category");
	rowIndex = this.xlsHelp.addRow("Planned Cost (Direct)", plannedDirect, row, sheet, rowIndex);
	rowIndex = this.xlsHelp.addRow("Planned Cost (Indirect)", plannedIndirect, row, sheet, rowIndex);
	rowIndex = this.xlsHelp.addRow("Planned Cost (Total)", projCost, row, sheet, rowIndex);
	rowIndex++;

	// If the project is completed, display actual costs
	// and compare.
	if (proj.isCompleted()) {
	    rowIndex = this.xlsHelp.addRow("Actual Cost (Direct)", actualDirect, row, sheet, rowIndex);
	    rowIndex = this.xlsHelp.addRow("Actual Cost (Indirect)", actualIndirect, row, sheet,
		    rowIndex);
	    rowIndex = this.xlsHelp.addRow("Actual Cost (Total)", actualProjCost, row, sheet, rowIndex);
	    rowIndex++;
	    rowIndex = this.xlsHelp.addRow("Difference (Direct)", plannedDirect - actualDirect, row,
		    sheet, rowIndex);
	    rowIndex = this.xlsHelp.addRow("Difference (Indirect)", plannedIndirect - actualIndirect,
		    row, sheet, rowIndex);
	    rowIndex = this.xlsHelp.addRow("Difference (Total)", projCost - actualProjCost, row, sheet,
		    rowIndex);
	}

	// Statistics of estimated costs.
	List<EstimateCost> estimatedCosts = this.estimateCostService.list(proj);
	StatisticsEstimateCost statEstimates = new StatisticsEstimateCost(estimatedCosts);
	rowIndex++;

	// Maximum costs.
	rowIndex = this.xlsHelp.addSectionLabel("MAX & MIN", sheet, rowIndex, IndexedColors.YELLOW,
		"Max and min of the dataset");
	rowIndex = this.xlsHelp.addSectionLabel("Maximum Costs", sheet, rowIndex,
		"Estimate(s) with the MOST expensive cost per category");
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getMaxPlannedDirect(), row, sheet,
		rowIndex, EstimateCostType.SUB_TYPE_PLANNED);
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getMaxPlannedIndirect(), row, sheet,
		rowIndex, EstimateCostType.SUB_TYPE_PLANNED);
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getMaxActualDirect(), row, sheet,
		rowIndex, EstimateCostType.SUB_TYPE_ACTUAL);
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getMaxActualIndirect(), row, sheet,
		rowIndex, EstimateCostType.SUB_TYPE_ACTUAL);
	rowIndex++;

	// Minimum costs.
	rowIndex = this.xlsHelp.addSectionLabel("Minimum Costs", sheet, rowIndex,
		"Estimate(s) with the LEAST expensive cost per category");
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getMinPlannedDirect(), row, sheet,
		rowIndex, EstimateCostType.SUB_TYPE_PLANNED);
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getMinPlannedIndirect(), row, sheet,
		rowIndex, EstimateCostType.SUB_TYPE_PLANNED);
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getMinActualDirect(), row, sheet,
		rowIndex, EstimateCostType.SUB_TYPE_ACTUAL);
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getMinActualIndirect(), row, sheet,
		rowIndex, EstimateCostType.SUB_TYPE_ACTUAL);
	rowIndex++;

	// Top estimated costs.
	Integer limit = 5;
	SortOrder order = SortOrder.DESCENDING;
	rowIndex = this.xlsHelp.addSectionLabel("TOP ENTRIES", sheet, rowIndex, IndexedColors.YELLOW,
		"Greatest costs in descending order");
	rowIndex = this.xlsHelp.addSectionLabel("Top Planned Direct", sheet, rowIndex,
		String.format("Top %s MOST expensive ESTIMATED DIRECT costs", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getSortedPlannedDirect(order, limit),
		row, sheet, rowIndex, EstimateCostType.SUB_TYPE_PLANNED);
	rowIndex++;

	rowIndex = this.xlsHelp.addSectionLabel("Top Planned Indirect", sheet, rowIndex,
		String.format("Top %s MOST expensive ESTIMATED INDIRECT costs", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getSortedPlannedIndirect(order, limit),
		row, sheet, rowIndex, EstimateCostType.SUB_TYPE_PLANNED);
	rowIndex++;

	rowIndex = this.xlsHelp.addSectionLabel("Top Actual Direct", sheet, rowIndex,
		String.format("Top %s MOST expensive ACTUAL DIRECT costs", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getSortedActualDirect(order, limit),
		row, sheet, rowIndex, EstimateCostType.SUB_TYPE_ACTUAL);
	rowIndex++;

	rowIndex = this.xlsHelp.addSectionLabel("Top Actual Indirect", sheet, rowIndex,
		String.format("Top %s MOST expensive ACTUAL INDIRECT costs", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getSortedActualIndirect(order, limit),
		row, sheet, rowIndex, EstimateCostType.SUB_TYPE_ACTUAL);
	rowIndex++;

	// Bottom estimated costs.
	order = SortOrder.ASCENDING;
	rowIndex = this.xlsHelp.addSectionLabel("BOTTOM ENTRIES", sheet, rowIndex, IndexedColors.YELLOW,
		"Least costs in ascending order");
	rowIndex = this.xlsHelp.addSectionLabel("Bottom Planned Direct", sheet, rowIndex,
		String.format("Bottom %s LEAST expensive ESTIMATED DIRECT costs", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getSortedPlannedDirect(order, limit),
		row, sheet, rowIndex, EstimateCostType.SUB_TYPE_PLANNED);
	rowIndex++;

	rowIndex = this.xlsHelp.addSectionLabel("Bottom Planned Indirect", sheet, rowIndex,
		String.format("Bottom %s LEAST expensive ESTIMATED INDIRECT costs", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getSortedPlannedIndirect(order, limit),
		row, sheet, rowIndex, EstimateCostType.SUB_TYPE_PLANNED);
	rowIndex++;

	rowIndex = this.xlsHelp.addSectionLabel("Bottom Actual Direct", sheet, rowIndex,
		String.format("Bottom %s LEAST expensive ACTUAL DIRECT costs", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getSortedActualDirect(order, limit),
		row, sheet, rowIndex, EstimateCostType.SUB_TYPE_ACTUAL);
	rowIndex++;

	rowIndex = this.xlsHelp.addSectionLabel("Bottom Actual Indirect", sheet, rowIndex,
		String.format("Bottom %s LEAST expensive ACTUAL INDIRECT costs", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(statEstimates.getSortedActualIndirect(order, limit),
		row, sheet, rowIndex, EstimateCostType.SUB_TYPE_ACTUAL);
	rowIndex++;

	// Top Overestimated Direct.
	order = SortOrder.DESCENDING;
	ImmutableList<Entry<EstimateCost, Double>> diffDirectDesc = statEstimates
		.getSortedDifferencesDirect(limit, order);
	rowIndex = this.xlsHelp.addSectionLabel("TOP DIFFERENCES", sheet, rowIndex, IndexedColors.YELLOW,
		"Greatest differences (estimated minus actual) in descending order");
	rowIndex = this.xlsHelp.addSectionLabel("Top Difference Direct", sheet, rowIndex,
		String.format("Top %s DIFFERENCE between the estimated and actual cost", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(diffDirectDesc, row, sheet, rowIndex,
		EstimateCostType.SUB_TYPE_DIFFERENCE);
	rowIndex++;

	// Top differences (planned - actual) of indirect.
	ImmutableList<Entry<EstimateCost, Double>> diffIndirectDesc = statEstimates
		.getSortedDifferencesIndirect(limit, order);
	rowIndex = this.xlsHelp.addSectionLabel("Top Difference Indirect", sheet, rowIndex,
		String.format("Top %s DIFFERENCE between the estimated and actual cost", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(diffIndirectDesc, row, sheet, rowIndex,
		EstimateCostType.SUB_TYPE_DIFFERENCE);
	rowIndex++;

	// Top differences (planned - actual) of overall.
	ImmutableList<Entry<EstimateCost, Double>> diffOverallDesc = statEstimates
		.getSortedDifferencesOverall(limit, order);
	rowIndex = this.xlsHelp.addSectionLabel("Top Difference Overall", sheet, rowIndex,
		String.format("Top %s DIFFERENCE between the estimated and actual cost", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(diffOverallDesc, row, sheet, rowIndex,
		EstimateCostType.SUB_TYPE_DIFFERENCE);
	rowIndex++;

	// Bottom absolute differences (planned - actual) of direct.
	order = SortOrder.ASCENDING;
	ImmutableList<Entry<EstimateCost, Double>> diffDirectAsc = statEstimates
		.getSortedDifferencesDirect(limit, order);
	rowIndex = this.xlsHelp.addSectionLabel("BOTTOM DIFFERENCES", sheet, rowIndex,
		IndexedColors.YELLOW, "Least differences (estimated minus actual) in ascending order");
	rowIndex = this.xlsHelp.addSectionLabel("Bottom Difference Direct", sheet, rowIndex,
		String.format("Bottom %s DIFFERENCE between the estimated and actual cost", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(diffDirectAsc, row, sheet, rowIndex,
		EstimateCostType.SUB_TYPE_DIFFERENCE);
	rowIndex++;

	// Bottom absolute differences (planned - actual) of indirect.
	ImmutableList<Entry<EstimateCost, Double>> diffIndirectAsc = statEstimates
		.getSortedDifferencesIndirect(limit, order);
	rowIndex = this.xlsHelp.addSectionLabel("Bottom Difference Indirect", sheet, rowIndex,
		String.format("Bottom %s DIFFERENCE between the estimated and actual cost", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(diffIndirectAsc, row, sheet, rowIndex,
		EstimateCostType.SUB_TYPE_DIFFERENCE);
	rowIndex++;

	// Bottom absolute differences (planned - actual) of overall.
	ImmutableList<Entry<EstimateCost, Double>> diffOverallAsc = statEstimates
		.getSortedDifferencesOverall(limit, order);
	rowIndex = this.xlsHelp.addSectionLabel("Bottom Difference Overall", sheet, rowIndex,
		String.format("Bottom %s DIFFERENCE between the estimated and actual cost", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(diffOverallAsc, row, sheet, rowIndex,
		EstimateCostType.SUB_TYPE_DIFFERENCE);
	rowIndex++;

	// Top absolute differences (planned - actual) of direct.
	order = SortOrder.DESCENDING;
	rowIndex = this.xlsHelp.addSectionLabel("ABSOLUTE DIFFERENCES", sheet, rowIndex,
		IndexedColors.YELLOW, "Most over and underestimated costs");
	ImmutableList<Entry<EstimateCost, Double>> absDiffDirectDesc = statEstimates
		.getSortedAbsDiffDirect(limit, order);
	rowIndex = this.xlsHelp.addSectionLabel("Top Absolute Difference Direct", sheet, rowIndex, String
		.format("Top %s ABSOLUTE DIFFERENCE between the estimated and actual cost", limit));
	rowIndex = this.xlsHelp.addSectionEstimates(absDiffDirectDesc, row, sheet, rowIndex,
		EstimateCostType.SUB_TYPE_ABSOLUTE);
	rowIndex++;

	// Top absolute differences (planned - actual) of indirect.
	ImmutableList<Entry<EstimateCost, Double>> absDiffIndirectDesc = statEstimates
		.getSortedAbsDiffIndirect(limit, order);
	rowIndex = this.xlsHelp.addSectionLabel("Top Absolute Difference Indirect", sheet, rowIndex,
		String.format("Top %s ABSOLUTE DIFFERENCE between the estimated and actual cost",
			limit));
	rowIndex = this.xlsHelp.addSectionEstimates(absDiffIndirectDesc, row, sheet, rowIndex,
		EstimateCostType.SUB_TYPE_ABSOLUTE);
	rowIndex++;

	// Top absolute differences (planned - actual) of overall.
	ImmutableList<Entry<EstimateCost, Double>> absDiffOverallDesc = statEstimates
		.getSortedAbsDiffOverall(limit, order);
	rowIndex = this.xlsHelp.addSectionLabel("Top Absolute Difference Overall", sheet, rowIndex,
		String.format("Top %s ABSOLUTE DIFFERENCE between the estimated and actual cost",
			limit));
	rowIndex = this.xlsHelp.addSectionEstimates(absDiffOverallDesc, row, sheet, rowIndex,
		EstimateCostType.SUB_TYPE_ABSOLUTE);
	rowIndex++;

	// Means.
	rowIndex = this.xlsHelp.addSectionLabel("MEANS", sheet, rowIndex, IndexedColors.YELLOW,
		"Average cost per category");
	Map<String, Double> labelValueMap = Maps.newLinkedHashMap();
	double meanPlannedDirect = statEstimates.getMeanPlannedDirect();
	double meanPlannedIndirect = statEstimates.getMeanPlannedIndirect();
	double meanPlannedOverall = statEstimates.getMeanPlannedOverall();
	double meanActualDirect = statEstimates.getMeanActualDirect();
	double meanActualIndirect = statEstimates.getMeanActualIndirect();
	double meanActualOverall = statEstimates.getMeanActualOverall();
	labelValueMap.put("Estimated Direct", meanPlannedDirect);
	labelValueMap.put("Estimated Indirect", meanPlannedIndirect);
	labelValueMap.put("Estimated Overall", meanPlannedOverall);
	labelValueMap.put("Actual Direct", meanActualDirect);
	labelValueMap.put("Actual Indirect", meanActualIndirect);
	labelValueMap.put("Actual Overall", meanActualOverall);
	rowIndex = this.xlsHelp.addSectionFromMap("Mean per Category", row, sheet, rowIndex,
		labelValueMap);
	rowIndex++;

    }

    /**
     * Displaying project details in the analysis Excel file.
     * 
     * @param row
     * @param sheet
     * @param rowIndex
     * @param proj
     * @return
     */
    private int xlsAnalysisDetails(HSSFRow row, HSSFSheet sheet, int rowIndex, Project proj) {

	rowIndex = this.xlsHelp.addSectionLabel("Details", sheet, rowIndex);
	rowIndex = this.xlsHelp.addRow("Name", proj.getName(), row, sheet, rowIndex);
	rowIndex = this.xlsHelp.addRow("Location", proj.getLocation(), row, sheet, rowIndex);
	rowIndex = this.xlsHelp.addRow("Status", proj.getStatusEnum().label(), row, sheet, rowIndex);
	rowIndex++;

	return rowIndex;
    }

}