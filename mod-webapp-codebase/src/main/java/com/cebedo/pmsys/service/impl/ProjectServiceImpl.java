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

import com.cebedo.pmsys.base.IObjectExpense;
import com.cebedo.pmsys.bean.StatisticsEstimateCost;
import com.cebedo.pmsys.bean.StatisticsProgramOfWorks;
import com.cebedo.pmsys.bean.StatisticsProject;
import com.cebedo.pmsys.bean.StatisticsStaff;
import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.SortOrder;
import com.cebedo.pmsys.enums.StatusAttendance;
import com.cebedo.pmsys.enums.StatusTask;
import com.cebedo.pmsys.enums.TypeCalendarEvent;
import com.cebedo.pmsys.enums.TypeEstimateCost;
import com.cebedo.pmsys.factory.AlertBoxFactory;
import com.cebedo.pmsys.factory.HSSFWorkbookFactory;
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
import com.cebedo.pmsys.repository.impl.MaterialValueRepoImpl;
import com.cebedo.pmsys.repository.impl.ProjectAuxValueRepoImpl;
import com.cebedo.pmsys.service.DeliveryService;
import com.cebedo.pmsys.service.EquipmentExpenseService;
import com.cebedo.pmsys.service.EstimateCostService;
import com.cebedo.pmsys.service.ExpenseService;
import com.cebedo.pmsys.service.MaterialService;
import com.cebedo.pmsys.service.ProjectPayrollService;
import com.cebedo.pmsys.service.ProjectService;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.service.TaskService;
import com.cebedo.pmsys.token.AuthenticationToken;
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
    private ProjectAuxValueRepoImpl projectAuxValueRepo;
    private StaffService staffService;
    private TaskService taskService;
    private EstimateCostService estimateCostService;
    private MaterialValueRepoImpl materialValueRepo;
    private MaterialService materialService;

    // For the balance sheet.
    private ProjectPayrollService projectPayrollService;
    private DeliveryService deliveryService;
    private EquipmentExpenseService equipmentExpenseService;
    private ExpenseService expenseService;

    @Autowired
    @Qualifier(value = "materialService")
    public void setMaterialService(MaterialService materialService) {
	this.materialService = materialService;
    }

    @Autowired
    @Qualifier(value = "materialValueRepo")
    public void setMaterialValueRepo(MaterialValueRepoImpl materialValueRepo) {
	this.materialValueRepo = materialValueRepo;
    }

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

    public void setProjectAuxValueRepo(ProjectAuxValueRepoImpl projectAuxValueRepo) {
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
	if (!this.authHelper.hasAccess(project)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
	    return AlertBoxFactory.ERROR;
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
	    return AlertBoxFactory.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_FILE_CORRUPT_INVALID);
	}

	List<EstimateCost> includeCosts = new ArrayList<EstimateCost>();
	List<EstimateCost> projectCosts = this.estimateCostService.list(project);

	// Check if this task is already committed.
	for (EstimateCost cost : costs) {

	    String name = cost.getName();
	    double estimatedCost = cost.getCost();
	    double actualCost = cost.getActualCost();
	    TypeEstimateCost costType = cost.getCostType();
	    boolean include = true;

	    // Check for existing.
	    for (EstimateCost projCost : projectCosts) {
		String projName = projCost.getName();
		double projEstimatedCost = projCost.getCost();
		double projActualCost = projCost.getActualCost();
		TypeEstimateCost projCostType = projCost.getCostType();

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
	String invalid = this.estimateCostService.createMassCosts(includeCosts, result, project.getId());
	if (invalid != null) {
	    return invalid;
	}

	return AlertBoxFactory.SUCCESS.generateCreateEntries(ConstantsRedis.OBJECT_ESTIMATE_COST);
    }

    @Override
    @Transactional
    public String uploadExcelMaterials(MultipartFile multipartFile, Project project, Delivery delivery,
	    BindingResult result) {

	// Security check.
	if (!this.authHelper.hasAccess(project)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
	    return AlertBoxFactory.ERROR;
	}

	// Service layer form validation.
	this.multipartFileValidator.validate(multipartFile, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Do service.
	List<Material> materials = this.materialService.convertExcelToMaterials(multipartFile, project);
	if (materials == null) {
	    return AlertBoxFactory.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_FILE_CORRUPT_INVALID);
	}

	List<Material> includeMaterials = new ArrayList<Material>();

	// List of existing materials in this delivery.
	String pattern = Material.constructPattern(delivery);
	Set<String> keys = this.materialValueRepo.keys(pattern);
	List<Material> deliveryMaterials = this.materialValueRepo.multiGet(keys);

	// Check if this task is already committed.
	for (Material material : materials) {

	    String name = material.getName();
	    double quantity = material.getQuantity();
	    String unit = material.getUnitOfMeasure();
	    double costPerUnit = material.getCostPerUnitMaterial();
	    String remarks = material.getRemarks();

	    // Check for existing.
	    boolean include = true;
	    for (Material existingMaterial : deliveryMaterials) {

		String existingName = existingMaterial.getName();
		double existingQuantity = existingMaterial.getQuantity();
		double existingCostPerUnit = existingMaterial.getCostPerUnitMaterial();
		String existingRemarks = existingMaterial.getRemarks();
		String existingUnit = existingMaterial.getUnitOfMeasure();

		// If we found a match, break. Don't include to list to commit.
		if (name.equalsIgnoreCase(existingName) && quantity == existingQuantity
			&& costPerUnit == existingCostPerUnit
			&& remarks.equalsIgnoreCase(existingRemarks)
			&& unit.equalsIgnoreCase(existingUnit)) {

		    include = false;
		    break;
		}
	    }

	    // If we are including this object,
	    // set necessary attributes.
	    if (include) {
		material.setCompany(project.getCompany());
		material.setProject(project);
		material.setDelivery(delivery);
		includeMaterials.add(material);
	    }
	}

	// Create mass.
	for (Material material : includeMaterials) {
	    this.materialService.create(material, result);
	}
	return AlertBoxFactory.SUCCESS.generateCreateEntries(ConstantsRedis.OBJECT_MATERIAL);
    }

    /**
     * Create Tasks from an Excel file.
     */
    @Override
    @Transactional
    public String uploadExcelTasks(MultipartFile multipartFile, Project project, BindingResult result) {

	// Security check.
	if (!this.authHelper.hasAccess(project)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
	    return AlertBoxFactory.ERROR;
	}

	// Service layer form validation.
	this.multipartFileValidator.validate(multipartFile, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Do service.
	List<Task> tasks = this.taskService.convertExcelToTaskList(multipartFile, project);
	if (tasks == null) {
	    return AlertBoxFactory.FAILED
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

	return AlertBoxFactory.SUCCESS.generateAssignEntries(Task.OBJECT_NAME);
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
	return AlertBoxFactory.SUCCESS.generateCreate(Project.OBJECT_NAME, project.getName());
    }

    /**
     * Create Staff members from Excel.
     */
    @Override
    @Transactional
    public String uploadExcelStaff(MultipartFile multipartFile, Project proj, BindingResult result) {

	// Security check.
	if (!this.authHelper.hasAccess(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxFactory.ERROR;
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
	    return AlertBoxFactory.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_FILE_CORRUPT_INVALID);
	}

	staffList = this.staffService.refineStaffList(staffList, result);

	// There was a problem with the list of staff you provided. Please
	// review the list and try again.
	if (staffList == null) {
	    return AlertBoxFactory.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_PROJECT_STAFF_MASS_UPLOAD_GENERIC);
	}

	// Log.
	this.messageHelper.auditableKey(AuditAction.ACTION_CREATE_MASS, Project.OBJECT_NAME,
		proj.getId(), Staff.OBJECT_NAME, "Mass", proj, "Mass");

	// If everything's ok.
	assignAllStaffToProject(proj, staffList);

	return AlertBoxFactory.SUCCESS.generateCreateEntries(Staff.OBJECT_NAME);
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
	if (!this.authHelper.hasAccess(project)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
	    return AlertBoxFactory.ERROR;
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

	this.projectDAO.merge(project);

	// Log.
	this.messageHelper.auditableKey(AuditAction.ACTION_UPDATE, Project.OBJECT_NAME, project.getId(),
		"", "", project, project.getName());

	// Response for the user.
	return AlertBoxFactory.SUCCESS.generateUpdate(Project.OBJECT_NAME, project.getName());
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
	if (this.authHelper.hasAccess(project)) {
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
	if (!this.authHelper.hasAccess(project) && !this.authHelper.isCompanyAdmin()) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
	    return AlertBoxFactory.ERROR;
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
	return AlertBoxFactory.SUCCESS.generateDelete(Project.OBJECT_NAME, project.getName());
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

    @Transactional
    @Override
    public Project getByIDWithAllCollections(long id) {
	return getByIDWithAllCollections(id, false);
    }

    @Override
    @Transactional
    public Project getByIDWithAllCollections(long id, boolean override) {

	Project project = this.projectDAO.getByIDWithAllCollections(id);

	// Log and return.
	if (override || this.authHelper.hasAccess(project)) {
	    this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_GET, Project.OBJECT_NAME, id);
	    return project;
	}

	// Log then return empty object.
	this.messageHelper.unauthorizedID(Project.OBJECT_NAME, id);
	return new Project();
    }

    @Transactional
    @Override
    public String getGanttJSON(Project proj) {
	return getGanttJSON(proj, false);
    }

    /**
     * Construct a JSON to be used by the Gantt dhtmlx.
     */
    @Override
    @Transactional
    public String getGanttJSON(Project proj, boolean override) {

	// Security check.
	if (!override && !this.authHelper.hasAccess(proj)) {
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

    @Transactional
    @Override
    public Map<StatusTask, Integer> getTaskStatusCountMap(Project proj) {
	return getTaskStatusCountMap(proj, false);
    }

    /**
     * Get task status and count map.
     * 
     * @param staff
     * @return
     */
    @Transactional
    @Override
    public Map<StatusTask, Integer> getTaskStatusCountMap(Project proj, boolean override) {

	// Security check.
	if (!override && !this.authHelper.hasAccess(proj)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, proj.getId());
	    return new HashMap<StatusTask, Integer>();
	}

	// Log.
	this.messageHelper.nonAuditableIDWithAssocNoKey(AuditAction.ACTION_GET_MAP, Project.OBJECT_NAME,
		proj.getId(), StatusTask.class.getName());

	// Get summary of tasks.
	// For each task status, count how many.
	Map<StatusTask, Integer> taskStatusMap = new HashMap<StatusTask, Integer>();
	Map<StatusTask, Integer> taskStatusMapSorted = new LinkedHashMap<StatusTask, Integer>();

	// Get the tasks (children) of each parent.
	for (Task task : proj.getAssignedTasks()) {
	    int taskStatusInt = task.getStatus();
	    StatusTask taskStatus = StatusTask.of(taskStatusInt);
	    Integer statCount = taskStatusMap.get(taskStatus) == null ? 1
		    : taskStatusMap.get(taskStatus) + 1;
	    taskStatusMap.put(taskStatus, statCount);
	}

	// If status count is null,
	// Add it as zero.
	for (StatusTask status : StatusTask.class.getEnumConstants()) {
	    Integer count = taskStatusMap.get(status);
	    taskStatusMapSorted.put(status, count == null ? 0 : count);
	}

	return taskStatusMapSorted;
    }

    @Transactional
    @Override
    public String getCalendarJSON(Project proj) {
	return getCalendarJSON(proj, false);
    }

    @Transactional
    @Override
    public String getCalendarJSON(Project proj, boolean override) {

	// Security check.
	if (!override && !this.authHelper.hasAccess(proj)) {
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
	    event.setClassName(TypeCalendarEvent.TASK.css());

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
	if (!this.authHelper.hasAccess(project)) {
	    this.messageHelper.unauthorizedID(Project.OBJECT_NAME, project.getId());
	    return AlertBoxFactory.ERROR;
	}
	// Log.
	this.messageHelper.auditableKey(AuditAction.ACTION_UPDATE, Project.OBJECT_NAME, project.getId(),
		"", "", project, project.getName());

	// Do service.
	project.setActualCompletionDate(null);
	project.setStatus(status);
	this.projectDAO.update(project);

	// Return success.
	return AlertBoxFactory.SUCCESS.generateMarkAs(Project.OBJECT_NAME, project.getName());
    }

    @Override
    @Transactional
    public Set<AuditLog> logs(long projID) {
	// Get the task.
	Project project = this.projectDAO.getByID(projID);

	// Security check.
	if (!this.authHelper.hasAccess(project)) {
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
	if (!this.authHelper.hasAccess(proj)) {
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
	List<IObjectExpense> payrolls = this.projectPayrollService.listDescExpense(proj, startDate,
		endDate);
	List<IObjectExpense> deliveries = this.deliveryService.listDescExpense(proj, startDate, endDate);
	List<IObjectExpense> equipmentExpenses = this.equipmentExpenseService.listDescExpense(proj,
		startDate, endDate);
	List<IObjectExpense> otherExpenses = this.expenseService.listDescExpense(proj, startDate,
		endDate);

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
	for (IObjectExpense payroll : payrolls) {
	    HSSFRow expenseRow = sheet.createRow(rowIndex);
	    expenseRow.createCell(1).setCellValue(payroll.getName());
	    expenseRow.createCell(2).setCellValue(payroll.getCost());
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
	for (IObjectExpense delivery : deliveries) {
	    HSSFRow expenseRow = sheet.createRow(rowIndex);
	    expenseRow.createCell(1).setCellValue(delivery.getName());
	    expenseRow.createCell(2).setCellValue(delivery.getCost());
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
	for (IObjectExpense equipment : equipmentExpenses) {
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
	for (IObjectExpense expense : otherExpenses) {
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
	if (!this.authHelper.hasAccess(proj)) {
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
	HSSFWorkbookFactory xlsGen = new HSSFWorkbookFactory();

	// Basic details and physical target.
	// Project estimate (Time).
	// Done.
	String sheetName = "Overview";
	xlsAnalysisOverview(xlsGen, sheetName, proj, plannedProjCost, actualProjCost);
	xlsAnalysisProgress(xlsGen, sheetName, proj);

	// Project estimate (Estimate Costs).
	xlsAnalysisEstimateCost(xlsGen, proj, plannedDirect, plannedIndirect, plannedProjCost,
		actualDirect, actualIndirect, actualProjCost);

	// Staff.
	xlsAnalysisStaff(xlsGen, proj);

	// Program of works.
	xlsAnalysisProgramOfWorks(xlsGen, proj);

	// Project expenses (Project modules).
	xlsAnalysisExpenses(xlsGen, proj);

	// Re-size the columns.
	xlsGen.fixSheets();

	return xlsGen.getWorkbook();
    }

    /**
     * Program of works.
     * 
     * @param xlsGen
     * @param proj
     */
    private void xlsAnalysisProgramOfWorks(HSSFWorkbookFactory xlsGen, Project proj) {
	String sheetName = "Program of Works";

	Set<Task> tasks = proj.getAssignedTasks();
	int tasksPopulation = tasks.size();
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Descriptive",
		"Descriptive statistics on program of works");
	xlsGen.addRow(sheetName, IndexedColors.YELLOW, "Basic");
	xlsGen.addRow(sheetName, "Number of Tasks", tasksPopulation);
	xlsGen.addRowEmpty(sheetName);

	// Process tasks.
	StatisticsProgramOfWorks statisticsPOW = new StatisticsProgramOfWorks(tasks);

	// (Mean) Of all tasks: duration. How much?
	double meanPlanned = statisticsPOW.getMeanDuration();
	double meanActual = statisticsPOW.getMeanActualDuration();
	double meanDiff = statisticsPOW.getMeanDifference();
	double meanAbs = statisticsPOW.getMeanAbsolute();

	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Means", "Average duration per type of data");
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Type", "Duration");
	xlsGen.addRow(sheetName, "Estimated", meanPlanned);
	xlsGen.addRow(sheetName, "Actual", meanActual);
	xlsGen.addRow(sheetName, "Difference", meanDiff);
	xlsGen.addRow(sheetName, "Absolute Difference", meanAbs);
	xlsGen.addRowEmpty(sheetName);

	// Map of task status to number.
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Task Total & Percentage",
		"How many tasks are set for each task status?");
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Status", "Total", "Percentage (%)");
	Map<StatusTask, Integer> taskStatusCountMap = getTaskStatusCountMap(proj);
	xlsGen.addRowsTaskCount(sheetName, taskStatusCountMap, tasksPopulation);
	xlsGen.addRowEmpty(sheetName);

	// (Max) Which task took the most duration and actualDuration?
	// How long? Who did it?
	List<Task> maxPlannedDuration = statisticsPOW.getMaxDuration();
	List<Task> maxActualDuration = statisticsPOW.getMaxActualDuration();
	List<Task> maxDifference = statisticsPOW.getMaxDifferenceDuration();
	List<Task> maxAbsolute = statisticsPOW.getMaxAbsoluteDuration();

	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Maximum");
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Type", "Task", "Duration");
	xlsGen.addRowsTasks(sheetName, maxPlannedDuration, TaskSubType.ESTIMATED);
	xlsGen.addRowsTasks(sheetName, maxActualDuration, TaskSubType.ACTUAL);
	xlsGen.addRowsTasks(sheetName, maxDifference, TaskSubType.DIFFERENCE);
	xlsGen.addRowsTasks(sheetName, maxAbsolute, TaskSubType.ABSOLUTE);
	xlsGen.addRowEmpty(sheetName);

	// (Min) Which task took the least duration and actualDuration?
	// How long? Who did it?
	List<Task> leastPlannedDuration = statisticsPOW.getMinDuration();
	List<Task> leastActualDuration = statisticsPOW.getMinActualDuration();
	List<Task> minDifference = statisticsPOW.getMinDifferenceDuration();
	List<Task> minAbsolute = statisticsPOW.getMinAbsoluteDuration();

	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Minimum");
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Type", "Task", "Duration");
	xlsGen.addRowsTasks(sheetName, leastPlannedDuration, TaskSubType.ESTIMATED);
	xlsGen.addRowsTasks(sheetName, leastActualDuration, TaskSubType.ACTUAL);
	xlsGen.addRowsTasks(sheetName, minDifference, TaskSubType.DIFFERENCE);
	xlsGen.addRowsTasks(sheetName, minAbsolute, TaskSubType.ABSOLUTE);
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
    private void xlsAnalysisOverview(HSSFWorkbookFactory xlsGen, String sheetName, Project proj,
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
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Project Cost", "PHP", "Percentage (%)");
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
		"Percentage (%)");
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
    private void xlsAnalysisExpenses(HSSFWorkbookFactory xlsGen, Project proj) {

	// Compute data.
	List<IObjectExpense> payrolls = this.projectPayrollService.listDescExpense(proj);
	List<IObjectExpense> deliveries = this.deliveryService.listDescExpense(proj);
	List<IObjectExpense> equipmentExpenses = this.equipmentExpenseService.listDescExpense(proj);
	List<IObjectExpense> otherExpenses = this.expenseService.listDescExpense(proj);
	StatisticsProject statisticsProj = new StatisticsProject(payrolls, deliveries, equipmentExpenses,
		otherExpenses);

	// Prepare variables.
	String sheetName = "Expenses";

	// Analysis (Mean).
	double meanPayroll = statisticsProj.getMeanPayroll();
	double meanDeliveries = statisticsProj.getMeanDelivery();
	double meanEquip = statisticsProj.getMeanEquipment();
	double meanOtherExpenses = statisticsProj.getMeanOtherExpenses();
	double meanProject = statisticsProj.getMeanProject();
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Means");
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Expense Type", "Mean");
	xlsGen.addRow(sheetName, "Payroll", meanPayroll);
	xlsGen.addRow(sheetName, "Deliveries", meanDeliveries);
	xlsGen.addRow(sheetName, "Equipment", meanEquip);
	xlsGen.addRow(sheetName, "Other Expenses", meanOtherExpenses);
	xlsGen.addRow(sheetName, "Overall Project", meanProject);
	xlsGen.addRowEmpty(sheetName);

	// Population.
	double popPayroll = this.projectPayrollService.getSize(payrolls);
	double popDelivery = deliveries.size();
	double popEquip = equipmentExpenses.size();
	double popOtherExpenses = otherExpenses.size();
	double popProject = popPayroll + popDelivery + popEquip + popOtherExpenses;
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Number of Entries");
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Expense Type", "No. of Entries");
	xlsGen.addRow(sheetName, "Payroll", popPayroll);
	xlsGen.addRow(sheetName, "Deliveries", popDelivery);
	xlsGen.addRow(sheetName, "Equipment", popEquip);
	xlsGen.addRow(sheetName, "Other Expenses", popOtherExpenses);
	xlsGen.addRow(sheetName, "Overall Project", popProject);
	xlsGen.addRowEmpty(sheetName);

	// Analysis (Max).
	xlsGen.addStatisticsExpensesDescriptive(sheetName, statisticsProj,
		StatisticsProject.DESCRIPTIVE_MAX);
	xlsGen.addRowEmpty(sheetName);

	// Analysis (Min).
	xlsGen.addStatisticsExpensesDescriptive(sheetName, statisticsProj,
		StatisticsProject.DESCRIPTIVE_MIN);
	xlsGen.addRowEmpty(sheetName);

	// Descending expenses.
	Integer limit = 5;
	xlsGen.addStatisticsExpensesPlain(sheetName, limit, SortOrder.DESCENDING, statisticsProj);
	xlsGen.addRowEmpty(sheetName);

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
    private void xlsAnalysisStaff(HSSFWorkbookFactory xlsGen, Project proj) {

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
	double meanPresent = statisticsStaff.getMeanOf(StatusAttendance.PRESENT);
	double meanAbsences = statisticsStaff.getMeanOf(StatusAttendance.ABSENT);
	double meanOvertime = statisticsStaff.getMeanOf(StatusAttendance.OVERTIME);
	double meanLate = statisticsStaff.getMeanOf(StatusAttendance.LATE);
	double meanHalfday = statisticsStaff.getMeanOf(StatusAttendance.HALFDAY);
	double meanLeave = statisticsStaff.getMeanOf(StatusAttendance.LEAVE);

	xlsGen.addRow(sheetName, StatusAttendance.PRESENT.label(), meanPresent);
	xlsGen.addRow(sheetName, StatusAttendance.ABSENT.label(), meanAbsences);
	xlsGen.addRow(sheetName, StatusAttendance.OVERTIME.label(), meanOvertime);
	xlsGen.addRow(sheetName, StatusAttendance.LATE.label(), meanLate);
	xlsGen.addRow(sheetName, StatusAttendance.HALFDAY.label(), meanHalfday);
	xlsGen.addRow(sheetName, StatusAttendance.LEAVE.label(), meanLeave);

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
    private void xlsAnalysisProgress(HSSFWorkbookFactory xlsGen, String sheetName, Project proj) {

	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Progress");

	Date dateStart = proj.getDateStart();
	Date dateCompletionTarget = proj.getTargetCompletionDate();
	int plannedNumOfDays = proj.getCalDaysTotal(); // Expected project days.

	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "", "Date", "No. of Days", "Percentage (%)",
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
    private void xlsAnalysisEstimateCost(HSSFWorkbookFactory xlsGen, Project proj, double plannedDirect,
	    double plannedIndirect, double plannedProjCost, double actualDirect, double actualIndirect,
	    double actualProjCost) {

	String sheetName = "Cost Estimate";

	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Costs Total and Percentage");

	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Project Cost", "Total", "Direct",
		"Percentage (%)", "Indirect", "Percentage (%)");
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
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Maximum",
		"Estimate(s) with the MOST expensive cost");
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Name", "Cost", "Type", "Cost Type");
	xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMaxPlannedDirect(),
		TypeEstimateCost.SUB_TYPE_PLANNED);
	xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMaxPlannedIndirect(),
		TypeEstimateCost.SUB_TYPE_PLANNED);
	if (proj.isCompleted()) {
	    xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMaxActualDirect(),
		    TypeEstimateCost.SUB_TYPE_ACTUAL);
	    xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMaxActualIndirect(),
		    TypeEstimateCost.SUB_TYPE_ACTUAL);
	}

	// Minimum costs.
	xlsGen.addRowEmpty(sheetName);
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Minimum",
		"Estimate(s) with the LEAST expensive cost");
	xlsGen.addRowHeader(sheetName, IndexedColors.YELLOW, "Name", "Cost", "Type", "Cost Type");
	xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMinPlannedDirect(),
		TypeEstimateCost.SUB_TYPE_PLANNED);
	xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMinPlannedIndirect(),
		TypeEstimateCost.SUB_TYPE_PLANNED);
	if (proj.isCompleted()) {
	    xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMinActualDirect(),
		    TypeEstimateCost.SUB_TYPE_ACTUAL);
	    xlsGen.addRowsEstimateCosts(sheetName, statEstimates.getMinActualIndirect(),
		    TypeEstimateCost.SUB_TYPE_ACTUAL);
	}

	Integer limit = 5;

	if (proj.isCompleted()) {
	    // Top absolute differences (planned - actual) of direct.
	    xlsGen.addRowEmpty(sheetName, 2);
	    xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Absolute Differences",
		    "Most over and underestimated costs");
	    xlsGen.addRowEmpty(sheetName);
	    xlsGen.addStatisticsEstimatesComputed(sheetName, statEstimates,
		    TypeEstimateCost.SUB_TYPE_ABSOLUTE, SortOrder.DESCENDING, limit);
	}

	// Top entries.
	xlsGen.addRowEmpty(sheetName, 2);
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Top Costs",
		"Greatest costs in descending order");
	xlsGen.addStatisticsEstimatesEntries(sheetName, proj, statEstimates, SortOrder.DESCENDING,
		limit);

	// Bottom entries.
	xlsGen.addRowEmpty(sheetName, 2);
	xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Bottom Costs",
		"Least costs in ascending order");
	xlsGen.addStatisticsEstimatesEntries(sheetName, proj, statEstimates, SortOrder.ASCENDING, limit);

	if (proj.isCompleted()) {
	    // Top absolute differences (planned - actual) of direct.
	    xlsGen.addRowEmpty(sheetName, 2);
	    xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Top Differences",
		    "Greatest differences (estimated minus actual) in descending order");
	    xlsGen.addRowEmpty(sheetName);
	    xlsGen.addStatisticsEstimatesComputed(sheetName, statEstimates,
		    TypeEstimateCost.SUB_TYPE_DIFFERENCE, SortOrder.DESCENDING, limit);

	    // Bottom absolute differences (planned - actual) of direct.
	    xlsGen.addRowEmpty(sheetName, 2);
	    xlsGen.addRow(sheetName, IndexedColors.SEA_GREEN, "Bottom Differences",
		    "Least differences (estimated minus actual) in ascending order");
	    xlsGen.addRowEmpty(sheetName);
	    xlsGen.addStatisticsEstimatesComputed(sheetName, statEstimates,
		    TypeEstimateCost.SUB_TYPE_DIFFERENCE, SortOrder.ASCENDING, limit);
	}
    }

}