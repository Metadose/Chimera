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

import com.cebedo.pmsys.bean.GeneratorExcel;
import com.cebedo.pmsys.bean.StatisticsEstimateCost;
import com.cebedo.pmsys.bean.StatisticsProgramOfWorks;
import com.cebedo.pmsys.bean.StatisticsProject;
import com.cebedo.pmsys.bean.StatisticsStaff;
import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.constants.RegistryExcel;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.EquipmentExpense;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.CalendarEventType;
import com.cebedo.pmsys.enums.EstimateCostType;
import com.cebedo.pmsys.enums.SortOrder;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Task.TaskSubType;
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
import com.google.gson.Gson;

@Service
public class ProjectServiceImpl implements ProjectService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

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
	this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_EXPORT, "Analysis", projID);

	// Prepare data.
	ProjectAux projAux = this.projectAuxValueRepo.get(ProjectAux.constructKey(proj));
	double plannedDirect = projAux.getGrandTotalCostsDirect();
	double plannedIndirect = projAux.getGrandTotalCostsIndirect();
	double actualDirect = projAux.getGrandTotalActualCostsDirect();
	double actualIndirect = projAux.getGrandTotalActualCostsIndirect();
	double plannedProjCost = plannedDirect + plannedIndirect;
	double actualProjCost = actualDirect + actualIndirect;

	// Construct essentials.
	GeneratorExcel xlsGen = new GeneratorExcel();

	// Basic details and physical target.
	// Project estimate (Time).
	// Done.
	String sheetName = "Overview";
	xlsAnalysisOverview(xlsGen, sheetName, proj, plannedProjCost, actualProjCost);
	// Done.
	xlsAnalysisProgress(xlsGen, sheetName, proj);

	// Project estimate (Estimate Costs).
	// Done.
	xlsAnalysisEstimateCost(xlsGen, proj, plannedDirect, plannedIndirect, plannedProjCost,
		actualDirect, actualIndirect, actualProjCost);

	// Staff.
	xlsAnalysisStaff(xlsGen, proj);

	// Program of works.
	xlsAnalysisProgramOfWorks(xlsGen, proj);

	// Project expenses (Project modules).
	xlsAnalysisExpenses(xlsGen, proj);

	// Re-size the columns.
	xlsGen.fixColumnSizes();

	return xlsGen.getWorkbook();
    }

    /**
     * Program of works.
     * 
     * @param xlsGen
     * @param proj
     */
    private void xlsAnalysisProgramOfWorks(GeneratorExcel xlsGen, Project proj) {
	String sheetName = "Program of Works";

	Set<Task> tasks = proj.getAssignedTasks();
	int tasksPopulation = tasks.size();
	xlsGen.addRow(sheetName, "Number of Tasks", tasksPopulation);

	// Process tasks.
	StatisticsProgramOfWorks statisticsPOW = new StatisticsProgramOfWorks(tasks);

	xlsGen.addRow(sheetName, IndexedColors.YELLOW, RegistryExcel.HEADER1_DESCRIPTIVE_MAX_MIN,
		RegistryExcel.HEADER1_DESCRIPTIVE_MAX_MIN_EXTRA);

	// (Max) Which task took the most duration and actualDuration?
	// How long? Who did it?
	List<Task> maxPlannedDuration = statisticsPOW.getMaxDuration();
	List<Task> maxActualDuration = statisticsPOW.getMaxActualDuration();
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Maximum Durations",
		"Which tasks took the MOST durations?");
	xlsGen.addRowsTasks(sheetName, maxPlannedDuration, TaskSubType.ESTIMATED);
	xlsGen.addRowsTasks(sheetName, maxActualDuration, TaskSubType.ACTUAL);

	// (Min) Which task took the least duration and actualDuration?
	// How long? Who did it?
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Minimum Durations",
		"Which tasks took the LEAST durations?");
	List<Task> leastPlannedDuration = statisticsPOW.getMinDuration();
	List<Task> leastActualDuration = statisticsPOW.getMinActualDuration();
	xlsGen.addRowsTasks(sheetName, leastPlannedDuration, TaskSubType.ESTIMATED);
	xlsGen.addRowsTasks(sheetName, leastActualDuration, TaskSubType.ACTUAL);

	// (Max) Biggest negative (duration - actualDuration)
	// How much? Who did it?
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Maximum Difference",
		"Most OVER-estimated task(s)");
	List<Task> maxDifference = statisticsPOW.getMaxDifferenceDuration();
	xlsGen.addRowsTasks(sheetName, maxDifference, TaskSubType.DIFFERENCE);

	// (Min) Least negative (duration - actualDuration)
	// How much? Who did it?
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Minimum Difference",
		"Most UNDER-estimated task(s)");
	List<Task> minDifference = statisticsPOW.getMinDifferenceDuration();
	xlsGen.addRowsTasks(sheetName, minDifference, TaskSubType.DIFFERENCE);

	// Absolute difference.
	// (Max) Biggest negative (duration - actualDuration)
	// How much? Who did it?
	List<Task> maxAbsolute = statisticsPOW.getMaxAbsoluteDuration();
	List<Task> minAbsolute = statisticsPOW.getMinAbsoluteDuration();
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Max Absolute Difference",
		"Task(s) with the GREATEST difference (estimated duration minus actual duration)");
	xlsGen.addRowsTasks(sheetName, maxAbsolute, TaskSubType.ABSOLUTE);

	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Min Absolute Difference",
		"Task(s) with the LEAST difference (estimated duration minus actual duration)");
	xlsGen.addRowsTasks(sheetName, minAbsolute, TaskSubType.ABSOLUTE);

	// (Mean) Of all tasks: duration. How much?
	double meanPlanned = statisticsPOW.getMeanDuration();
	double meanActual = statisticsPOW.getMeanActualDuration();
	double meanDiff = statisticsPOW.getMeanDifference();
	double meanAbs = statisticsPOW.getMeanAbsolute();

	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Means", "Average value");
	xlsGen.addRow(sheetName, "Duration (Estimated)", meanPlanned);
	xlsGen.addRow(sheetName, "Duration (Actual)", meanActual);
	xlsGen.addRow(sheetName, "Duration (Difference)", meanDiff);
	xlsGen.addRow(sheetName, "Duration (Absolute Difference)", meanAbs);

	// Map of task status to number.
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Counter",
		"How many tasks are set for each task status?");
	Map<TaskStatus, Integer> taskStatusCountMap = getTaskStatusCountMap(proj);
	xlsGen.addRowsTaskCount(sheetName, taskStatusCountMap, tasksPopulation);
    }

    /**
     * Basic details.
     * 
     * @param xlsGen
     * @param sheetName
     * @param proj
     * @param actualProjCost
     * @param projCost
     */
    private void xlsAnalysisOverview(GeneratorExcel xlsGen, String sheetName, Project proj,
	    double projCost, double actualProjCost) {

	// Basic details.
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Project Details");
	xlsGen.addRow(sheetName, "Name", proj.getName());
	xlsGen.addRow(sheetName, "Location", proj.getLocation());
	xlsGen.addRow(sheetName, "Status", proj.getStatusEnum().label());

	// Physical target.
	double phyTarget = proj.getPhysicalTarget();
	double phyTargetCost = projCost / phyTarget;
	double projCostPercent = (actualProjCost / projCost) * 100;
	double projCostDiff = projCost - actualProjCost;
	double projCostDiffPercent = (projCostDiff / projCost) * 100;

	xlsGen.addRowEmpty(sheetName);
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Project Cost & Physical Target");
	xlsGen.addRow(sheetName, "Physical Target (sq.m.)", phyTarget);
	xlsGen.addRowEmpty(sheetName);

	// Project cost.
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Project Cost", "PHP", "Percent (%)");
	xlsGen.addRow(sheetName, "Estimated", projCost);
	if (proj.isCompleted()) {
	    xlsGen.addRow(sheetName, "Actual", actualProjCost, projCostPercent,
		    "% comparison with Estimated");
	    xlsGen.addRow(sheetName, "Difference", projCostDiff, projCostDiffPercent,
		    "% comparison with Estimated");
	}
	xlsGen.addRowEmpty(sheetName);

	// Physical target.
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Physical Target Cost", "PHP / sq.m.",
		"Percent (%)");
	xlsGen.addRow(sheetName, "Estimated", phyTargetCost);
	if (proj.isCompleted()) {
	    double actualPhyTargetCost = actualProjCost / phyTarget;
	    double diffTargetCosts = phyTargetCost - actualPhyTargetCost;
	    double percentActualPhy = (actualPhyTargetCost / phyTargetCost) * 100;
	    double percentDiffPhy = (diffTargetCosts / phyTargetCost) * 100;

	    xlsGen.addRow(sheetName, "Actual", actualPhyTargetCost, percentActualPhy,
		    "% comparison with Estimated");
	    xlsGen.addRow(sheetName, "Difference", diffTargetCosts, percentDiffPhy,
		    "% comparison with Estimated");
	}
	xlsGen.addRowEmpty(sheetName);
    }

    /**
     * Expenses from project modules.
     * 
     * @param xlsGen
     * @param proj
     */
    private void xlsAnalysisExpenses(GeneratorExcel xlsGen, Project proj) {

	// Compute data.
	List<ProjectPayroll> payrolls = this.projectPayrollService.listDesc(proj);
	List<Delivery> deliveries = this.deliveryService.listDesc(proj);
	List<EquipmentExpense> equipmentExpenses = this.equipmentExpenseService.listDesc(proj);
	List<Expense> otherExpenses = this.expenseService.listDesc(proj);
	StatisticsProject statisticsProj = new StatisticsProject(payrolls, deliveries, equipmentExpenses,
		otherExpenses);

	// Prepare variables.
	String sheetName = RegistryExcel.SHEET_EXPENSES;

	// Analysis (Mean).
	double meanPayroll = statisticsProj.getMeanPayroll();
	double meanDeliveries = statisticsProj.getMeanDelivery();
	double meanEquip = statisticsProj.getMeanEquipment();
	double meanOtherExpenses = statisticsProj.getMeanOtherExpenses();
	double meanProject = statisticsProj.getMeanProject();
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, RegistryExcel.MEANS);
	xlsGen.addRow(sheetName, "Payroll", meanPayroll);
	xlsGen.addRow(sheetName, "Deliveries", meanDeliveries);
	xlsGen.addRow(sheetName, "Equipment", meanEquip);
	xlsGen.addRow(sheetName, "Other Expenses", meanOtherExpenses);
	xlsGen.addRow(sheetName, "Overall Project", meanProject);

	// Population.
	double popPayroll = this.projectPayrollService.getSize(payrolls);
	double popDelivery = deliveries.size();
	double popEquip = equipmentExpenses.size();
	double popOtherExpenses = otherExpenses.size();
	double popProject = popPayroll + popDelivery + popEquip + popOtherExpenses;
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, RegistryExcel.HEADER2_POPULATION);
	xlsGen.addRow(sheetName, "Payroll", popPayroll);
	xlsGen.addRow(sheetName, "Deliveries", popDelivery);
	xlsGen.addRow(sheetName, "Equipment", popEquip);
	xlsGen.addRow(sheetName, "Other Expenses", popOtherExpenses);
	xlsGen.addRow(sheetName, "Overall Project", popProject);

	// Analysis (Max).
	xlsGen.addStatisticsExpensesDescriptive(sheetName, statisticsProj,
		StatisticsProject.DESCRIPTIVE_MAX);

	// Analysis (Min).
	xlsGen.addStatisticsExpensesDescriptive(sheetName, statisticsProj,
		StatisticsProject.DESCRIPTIVE_MIN);

	// Descending expenses.
	Integer limit = 5;
	xlsGen.addStatisticsExpensesPlain(sheetName, limit, SortOrder.DESCENDING, statisticsProj);

	// Ascending expenses.
	xlsGen.addStatisticsExpensesPlain(sheetName, limit, SortOrder.ASCENDING, statisticsProj);
    }

    /**
     * Analysis regarding staff members and attendances.
     * 
     * @param xlsGen
     * @param sheetName
     * @param proj
     */
    private void xlsAnalysisStaff(GeneratorExcel xlsGen, Project proj) {

	String sheetName = "Staff";

	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Descriptive",
		"Descriptive statistics on attendance data");

	// Number of staff members assigned to this project.
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Basic");
	Set<Staff> assignedStaff = proj.getAssignedStaff();
	int numOfAssignedStaff = assignedStaff.size();
	xlsGen.addRow(sheetName, "Number of Assigned Staff", numOfAssignedStaff);

	// Mean salary per day.
	StatisticsStaff statisticsStaff = new StatisticsStaff(proj, assignedStaff);
	double meanWage = statisticsStaff.getMeanWage();
	xlsGen.addRow(sheetName, "Salary Mean (Daily)", meanWage, "Average daily staff salary");

	// Summation of salary per day.
	double sumWage = statisticsStaff.getSumWage();
	xlsGen.addRow(sheetName, "Salary Sum (Daily)", sumWage, "Total daily staff salary");

	// Mean per attendance status.
	xlsGen.addRowEmpty(sheetName);
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Means",
		"Average number of attendances per type of attendance");
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Attendance Type", "Mean");
	double meanAbsences = statisticsStaff.getMeanOf(AttendanceStatus.ABSENT);
	double meanOvertime = statisticsStaff.getMeanOf(AttendanceStatus.OVERTIME);
	double meanLate = statisticsStaff.getMeanOf(AttendanceStatus.LATE);
	double meanHalfday = statisticsStaff.getMeanOf(AttendanceStatus.HALFDAY);
	double meanLeave = statisticsStaff.getMeanOf(AttendanceStatus.LEAVE);

	xlsGen.addRow(sheetName, AttendanceStatus.ABSENT.label(), meanAbsences);
	xlsGen.addRow(sheetName, AttendanceStatus.OVERTIME.label(), meanOvertime);
	xlsGen.addRow(sheetName, AttendanceStatus.LATE.label(), meanLate);
	xlsGen.addRow(sheetName, AttendanceStatus.HALFDAY.label(), meanHalfday);
	xlsGen.addRow(sheetName, AttendanceStatus.LEAVE.label(), meanLeave);

	// Staff list with the most per attendance. Get top 5.
	// Top 5 staff member per attendance status.
	int max = 5;
	xlsGen.addRowEmpty(sheetName, 2);
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN,
		String.format("Top %s Per Attendance Type", max),
		"Staff member(s) with the MOST number of attendances per type");
	xlsGen.addStatisticsAttendanceEntries(sheetName, statisticsStaff, max, SortOrder.DESCENDING);

	// Bottom 5 staff member per attendance status.
	xlsGen.addRowEmpty(sheetName, 2);
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN,
		String.format("Bottom %s Per Attendance Type", max),
		"Staff member(s) with the LEAST number of attendances per type");
	xlsGen.addStatisticsAttendanceEntries(sheetName, statisticsStaff, max, SortOrder.ASCENDING);
    }

    /**
     * Progress section of analysis Excel.
     * 
     * @param xlsGen
     * @param sheetName
     * @param proj
     */
    private void xlsAnalysisProgress(GeneratorExcel xlsGen, String sheetName, Project proj) {

	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Progress");

	Date dateStart = proj.getDateStart();
	Date dateCompletionTarget = proj.getTargetCompletionDate();
	int plannedNumOfDays = proj.getCalDaysTotal(); // Expected project days.

	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "", "Date", "No. of Days", "Percent (%)",
		"Remarks");
	xlsGen.addRow(sheetName, "Start", DateUtils.formatDate(dateStart));
	xlsGen.addRow(sheetName, "Completion (Estimated)", DateUtils.formatDate(dateCompletionTarget),
		plannedNumOfDays);

	// If project is completed,
	// do post-project analysis.
	if (proj.isCompleted()) {

	    Date dateCompletionActual = proj.getActualCompletionDate();
	    if (dateCompletionActual == null) {
		xlsGen.addRow(sheetName, "Completion (Actual)", "(Not Set)");
	    } else {

		// How many days was the actual project runtime?
		int actualNumOfDays = Days
			.daysBetween(new DateTime(dateStart), new DateTime(dateCompletionActual))
			.getDays();
		double percentActualNumDays = (((double) actualNumOfDays) / ((double) plannedNumOfDays))
			* 100;

		// Distance between the planned and actual number of days.
		int difference = plannedNumOfDays - actualNumOfDays;
		String diffLabel = difference > 0 ? "Ahead of Schedule"
			: (difference < 0 ? "Delayed" : "On-Time");
		double percentDiff = ((double) difference / (double) plannedNumOfDays) * 100;

		// Render.
		xlsGen.addRow(sheetName, "Completion (Actual)",
			DateUtils.formatDate(dateCompletionActual), actualNumOfDays,
			percentActualNumDays);
		xlsGen.addRow(sheetName, "Difference", "", difference, percentDiff, diffLabel);
	    }
	}
	// If project is not yet completed.
	else {
	    // Number of days remaining.
	    int remainingNumOfDays = Days.daysBetween(new DateTime(new Date(System.currentTimeMillis())),
		    new DateTime(dateCompletionTarget)).getDays();
	    double remainingDaysPercent = ((double) remainingNumOfDays / (double) plannedNumOfDays)
		    * 100;
	    xlsGen.addRow(sheetName, "Remaining", String.format("(%s)", proj.getStatusEnum().label()),
		    remainingNumOfDays, remainingDaysPercent);
	}
    }

    /**
     * Analysis of project estimation and estimated costs.
     * 
     * @param xlsGen
     * @param proj
     * @param plannedDirect
     * @param plannedIndirect
     * @param plannedProjCost
     * @param actualDirect
     * @param actualIndirect
     * @param actualProjCost
     */
    private void xlsAnalysisEstimateCost(GeneratorExcel xlsGen, Project proj, double plannedDirect,
	    double plannedIndirect, double plannedProjCost, double actualDirect, double actualIndirect,
	    double actualProjCost) {

	String sheetName = "Cost Estimate";

	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Costs Total and Proportion");

	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Project Cost", "Total", "Direct",
		"Percent (%)", "Indirect", "Percent (%)");
	double percentEstimatedDirect = (plannedDirect / plannedProjCost) * 100;
	double percentEstimatedIndirect = (plannedIndirect / plannedProjCost) * 100;
	xlsGen.addRow(sheetName, "Estimated", plannedProjCost, plannedDirect, percentEstimatedDirect,
		plannedIndirect, percentEstimatedIndirect);

	// If the project is completed, display actual costs
	// and compare.
	if (proj.isCompleted()) {
	    double percentActualDirect = (actualDirect / actualProjCost) * 100;
	    double percentActualIndirect = (actualIndirect / actualProjCost) * 100;

	    double diffProjCost = plannedProjCost - actualProjCost;
	    double diffDirect = plannedDirect - actualDirect;
	    double diffIndirect = plannedIndirect - actualIndirect;

	    double percentDiffDirect = (diffDirect / diffProjCost) * 100;
	    double percentDiffIndirect = (diffIndirect / diffProjCost) * 100;

	    xlsGen.addRow(sheetName, "Actual", actualProjCost, actualDirect, percentActualDirect,
		    actualIndirect, percentActualIndirect);
	    xlsGen.addRow(sheetName, "Difference", diffProjCost, diffDirect, percentDiffDirect,
		    diffIndirect, percentDiffIndirect);
	}

	// Statistics of estimated costs.
	List<EstimateCost> estimatedCosts = this.estimateCostService.list(proj);
	StatisticsEstimateCost statEstimates = new StatisticsEstimateCost(estimatedCosts);

	// Means.
	double meanPlannedDirect = statEstimates.getMeanPlannedDirect();
	double meanPlannedIndirect = statEstimates.getMeanPlannedIndirect();
	double meanPlannedOverall = statEstimates.getMeanPlannedOverall();

	xlsGen.addRowEmpty(sheetName);
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Means");
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Project Cost", "Mean (Average)", "Direct",
		"Indirect");
	xlsGen.addRow(sheetName, "Estimated", meanPlannedOverall, meanPlannedDirect,
		meanPlannedIndirect);

	if (proj.isCompleted()) {
	    double meanActualDirect = statEstimates.getMeanActualDirect();
	    double meanActualIndirect = statEstimates.getMeanActualIndirect();
	    double meanActualOverall = statEstimates.getMeanActualOverall();

	    xlsGen.addRow(sheetName, "Actual", meanActualOverall, meanActualDirect, meanActualIndirect);
	    xlsGen.addRow(sheetName, "Difference", meanPlannedOverall - meanActualOverall,
		    meanPlannedDirect - meanActualDirect, meanPlannedIndirect - meanActualIndirect);
	}

	// Maximum costs.
	xlsGen.addRowEmpty(sheetName);
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, RegistryExcel.HEADER2_MAXIMUM,
		RegistryExcel.HEADER2_MAX_COST_EXTRA);
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Name", "Cost", "Type", "Cost Type");
	xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMaxPlannedDirect(),
		EstimateCostType.SUB_TYPE_PLANNED);
	xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMaxPlannedIndirect(),
		EstimateCostType.SUB_TYPE_PLANNED);
	if (proj.isCompleted()) {
	    xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMaxActualDirect(),
		    EstimateCostType.SUB_TYPE_ACTUAL);
	    xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMaxActualIndirect(),
		    EstimateCostType.SUB_TYPE_ACTUAL);
	}

	// Minimum costs.
	xlsGen.addRowEmpty(sheetName);
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, RegistryExcel.HEADER2_MINIMUM,
		RegistryExcel.HEADER2_MIN_COST_EXTRA);
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Name", "Cost", "Type", "Cost Type");
	xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMinPlannedDirect(),
		EstimateCostType.SUB_TYPE_PLANNED);
	xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMinPlannedIndirect(),
		EstimateCostType.SUB_TYPE_PLANNED);
	if (proj.isCompleted()) {
	    xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMinActualDirect(),
		    EstimateCostType.SUB_TYPE_ACTUAL);
	    xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMinActualIndirect(),
		    EstimateCostType.SUB_TYPE_ACTUAL);
	}

	Integer limit = 5;

	if (proj.isCompleted()) {
	    // Top absolute differences (planned - actual) of direct.
	    xlsGen.addRowEmpty(sheetName, 2);
	    xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Absolute Differences",
		    RegistryExcel.HEADER1_COMPUTED_ABS_EXTRA);
	    xlsGen.addRowEmpty(sheetName);
	    xlsGen.addStatisticsEstimatesComputed(sheetName, statEstimates,
		    EstimateCostType.SUB_TYPE_ABSOLUTE, SortOrder.DESCENDING, limit);
	}

	// Top entries.
	xlsGen.addRowEmpty(sheetName, 2);
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Top Costs",
		RegistryExcel.HEADER1_SORTED_ENTRIES_TOP_EXTRA);
	xlsGen.addStatisticsEstimatesEntries(sheetName, proj, statEstimates, SortOrder.DESCENDING,
		limit);

	// Bottom entries.
	xlsGen.addRowEmpty(sheetName, 2);
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Bottom Costs",
		RegistryExcel.HEADER1_SORTED_ENTRIES_BOTTOM_EXTRA);
	xlsGen.addStatisticsEstimatesEntries(sheetName, proj, statEstimates, SortOrder.ASCENDING, limit);

	if (proj.isCompleted()) {
	    // Top absolute differences (planned - actual) of direct.
	    xlsGen.addRowEmpty(sheetName, 2);
	    xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Top Differences",
		    RegistryExcel.HEADER1_COMPUTED_DIFF_TOP_EXTRA);
	    xlsGen.addRowEmpty(sheetName);
	    xlsGen.addStatisticsEstimatesComputed(sheetName, statEstimates,
		    EstimateCostType.SUB_TYPE_DIFFERENCE, SortOrder.DESCENDING, limit);

	    // Bottom absolute differences (planned - actual) of direct.
	    xlsGen.addRowEmpty(sheetName, 2);
	    xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Bottom Differences",
		    RegistryExcel.HEADER1_COMPUTED_DIFF_BOTTOM_EXTRA);
	    xlsGen.addRowEmpty(sheetName);
	    xlsGen.addStatisticsEstimatesComputed(sheetName, statEstimates,
		    EstimateCostType.SUB_TYPE_DIFFERENCE, SortOrder.ASCENDING, limit);
	}
    }

}