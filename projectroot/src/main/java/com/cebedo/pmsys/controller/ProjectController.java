package com.cebedo.pmsys.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.bean.EstimateBean;
import com.cebedo.pmsys.bean.EstimationInputBean;
import com.cebedo.pmsys.bean.FieldAssignmentBean;
import com.cebedo.pmsys.bean.MassUploadBean;
import com.cebedo.pmsys.bean.PayrollIncludeStaffBean;
import com.cebedo.pmsys.bean.StaffAssignmentBean;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.constants.URLRegistry;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.EstimationOutput;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.enums.CalendarEventType;
import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.enums.CommonMassUnit;
import com.cebedo.pmsys.enums.CommonVolumeUnit;
import com.cebedo.pmsys.enums.GanttElement;
import com.cebedo.pmsys.enums.MaterialCategory;
import com.cebedo.pmsys.enums.MilestoneStatus;
import com.cebedo.pmsys.enums.PayrollStatus;
import com.cebedo.pmsys.enums.TableEstimationAllowance;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.service.DeliveryService;
import com.cebedo.pmsys.service.EstimateService;
import com.cebedo.pmsys.service.EstimationOutputService;
import com.cebedo.pmsys.service.FieldService;
import com.cebedo.pmsys.service.MaterialService;
import com.cebedo.pmsys.service.ProjectAuxService;
import com.cebedo.pmsys.service.ProjectPayrollService;
import com.cebedo.pmsys.service.ProjectService;
import com.cebedo.pmsys.service.PullOutService;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.service.TaskService;
import com.cebedo.pmsys.service.impl.ProjectPayrollServiceImpl;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Controller
@SessionAttributes(

value = { Project.OBJECT_NAME, ProjectController.ATTR_FIELD, "old" + ProjectController.ATTR_FIELD,
	RedisConstants.OBJECT_PAYROLL, RedisConstants.OBJECT_DELIVERY, RedisConstants.OBJECT_MATERIAL,
	RedisConstants.OBJECT_PULL_OUT, RedisConstants.OBJECT_ESTIMATE,
	ProjectController.ATTR_MASS_UPLOAD_STAFF_BEAN, ProjectController.ATTR_TASK },

types = { Project.class, FieldAssignmentBean.class, ProjectPayroll.class, Delivery.class,
	Material.class, PullOut.class, EstimateBean.class, MassUploadBean.class, Task.class }

)
@RequestMapping(Project.OBJECT_NAME)
public class ProjectController {

    // TODO Clean this whole thing.
    public static final String ATTR_LIST = "projectList";
    public static final String ATTR_PROJECT = Project.OBJECT_NAME;
    public static final String ATTR_PROJECT_AUX = RedisConstants.OBJECT_PROJECT_AUX;
    public static final String ATTR_DELIVERY = RedisConstants.OBJECT_DELIVERY;
    public static final String ATTR_MATERIAL = RedisConstants.OBJECT_MATERIAL;
    public static final String ATTR_PULL_OUT = RedisConstants.OBJECT_PULL_OUT;
    public static final String ATTR_FIELD = Field.OBJECT_NAME;
    public static final String ATTR_STAFF = Staff.OBJECT_NAME;
    public static final String ATTR_TASK = Task.OBJECT_NAME;
    public static final String ATTR_ALL_STAFF = "allStaff";
    public static final String ATTR_PROJECT_PAYROLL = "projectPayroll";
    public static final String ATTR_MATERIAL_LIST = "materialList";
    public static final String ATTR_PULL_OUT_LIST = "pullOutList";
    public static final String ATTR_CONCRETE_ESTIMATION_SUMMARIES = "concreteEstimationSummaries";
    public static final String ATTR_MASONRY_CHB_ESTIMATION_SUMMARIES = "masonryCHBEstimationSummaries";
    public static final String ATTR_SHAPE_LIST = "shapeList";

    public static final String ATTR_MASS_UPLOAD_STAFF_BEAN = "massUploadStaffBean";
    public static final String ATTR_ESTIMATE = RedisConstants.OBJECT_ESTIMATE;
    public static final String ATTR_ESTIMATE_INPUT = "estimationInput";
    public static final String ATTR_ESTIMATE_OUTPUT_LIST = "estimationOutputList";
    public static final String ATTR_ESTIMATE_OUTPUT_JSON = "estimateJSON";
    public static final String ATTR_ESTIMATE_ALLOWANCE_LIST = "allowanceList";
    public static final String ATTR_ESTIMATE_MASONRY_LIST = "masonryEstimateList";
    public static final String ATTR_ESTIMATE_TYPES = "estimateTypes";
    public static final String ATTR_ESTIMATE_CONCRETE_LIST = "concreteEstimateList";
    public static final String ATTR_ESTIMATE_COMBINED_LIST = "combinedEstimateList";
    public static final String ATTR_CONCRETE_ESTIMATION_SUMMARY = RedisConstants.OBJECT_CONCRETE_ESTIMATION_SUMMARY;
    public static final String ATTR_CONCRETE_PROPORTION_LIST = "concreteProportionList";
    public static final String ATTR_CHB_LIST = "chbList";
    public static final String ATTR_BLOCK_LAYING_MIXTURE_LIST = "blockLayingMixtureList";
    public static final String ATTR_CHB_FOOTING_DIMENSION_LIST = "chbFootingDimensionList";

    public static final String ATTR_DELIVERY_LIST = "deliveryList";
    public static final String ATTR_PAYROLL_LIST = "payrollList";
    public static final String ATTR_COMMON_UNITS_LIST = "commonUnitsList";
    public static final String ATTR_CHB_MR_HORIZONTAL_LIST = "chbHorizontalReinforcementList";
    public static final String ATTR_CHB_MR_VERTICAL_LIST = "chbVerticalReinforcementList";
    public static final String ATTR_UNIT_LIST_LENGTH = "unitListLength";
    public static final String ATTR_UNIT_LIST_MASS = "unitListMass";
    public static final String ATTR_UNIT_LIST_VOLUME = "unitListVolume";
    public static final String ATTR_MATERIAL_CATEGORY_LIST = "materialCategoryList";
    public static final String ATTR_PAYROLL_LIST_TOTAL = "payrollListTotal";
    public static final String ATTR_STAFF_POSITION = "staffPosition";
    public static final String ATTR_TEAM_ASSIGNMENT = "teamAssignment";
    public static final String ATTR_FILE = "file";

    public static final String ATTR_PAYROLL_SELECTOR_STATUS = "payrollStatusArr";

    public static final String ATTR_CALENDAR_EVENT_TYPES_MAP = "calendarEventTypesMap";
    public static final String ATTR_CALENDAR_EVENT_TYPES_LIST = "calendarEventTypes";
    public static final String ATTR_CALENDAR_JSON = "calendarJSON";
    public static final String ATTR_GANTT_JSON = "ganttJSON";
    public static final String ATTR_GANTT_TYPE_LIST = "ganttElemTypeList";

    public static final String ATTR_TIMELINE_TASK_STATUS_MAP = "taskStatusMap";
    public static final String ATTR_TIMELINE_MILESTONE_SUMMARY_MAP = "milestoneSummary";
    public static final String ATTR_TIMELINE_SUMMARY_MAP = "timelineSummaryMap";

    public static final String ATTR_PAYROLL_JSON = "payrollJSON";
    public static final String ATTR_PAYROLL_CHECKBOX_STAFF = "staffList";
    public static final String ATTR_STAFF_LIST = "staffList";
    public static final String ATTR_STAFF_LIST_AVAILABLE = "availableStaffToAssign";
    public static final String ATTR_PAYROLL_MANUAL_STAFF_LIST = "manualStaffList";
    public static final String ATTR_PAYROLL_INCLUDE_STAFF = "payrollIncludeStaff";

    public static final String ATTR_MAP_ID_TO_MILESTONE = "idToMilestoneMap";

    public static final String KEY_SUMMARY_TOTAL_TASKS = "Total Tasks";
    public static final String KEY_SUMMARY_TOTAL_MILESTONES = "Total Milestones";
    public static final String KEY_SUMMARY_TOTAL_TASKS_ASSIGNED_MILESTONES = "Total Tasks Assigned to Milestones";
    public static final String KEY_SUMMARY_TOTAL_MILESTONE_NEW = "Total Milestones (Not Yet Started)";
    public static final String KEY_SUMMARY_TOTAL_MILESTONE_ONGOING = "Total Milestones (Ongoing)";
    public static final String KEY_SUMMARY_TOTAL_MILESTONE_DONE = "Total Milestones (Done)";

    public static final String KEY_PROJECT_STRUCTURE_MANAGERS = "Managers";

    public static final String JSP_LIST = Project.OBJECT_NAME + "/projectList";
    public static final String JSP_EDIT = Project.OBJECT_NAME + "/projectEdit";
    public static final String JSP_EDIT_FIELD = Project.OBJECT_NAME + "/assignedFieldEdit";

    private AuthHelper authHelper = new AuthHelper();

    private ProjectService projectService;
    private StaffService staffService;
    private FieldService fieldService;
    private DeliveryService deliveryService;
    private MaterialService materialService;
    private ProjectPayrollService projectPayrollService;
    private ProjectAuxService projectAuxService;
    private PullOutService pullOutService;
    private EstimateService estimateService;
    private EstimationOutputService estimationOutputService;
    private TaskService taskService;

    @Autowired(required = true)
    @Qualifier(value = "taskService")
    public void setTaskService(TaskService taskService) {
	this.taskService = taskService;
    }

    @Autowired(required = true)
    @Qualifier(value = "estimationOutputService")
    public void setEstimationOutputService(EstimationOutputService estimationOutputService) {
	this.estimationOutputService = estimationOutputService;
    }

    @Autowired(required = true)
    @Qualifier(value = "estimateService")
    public void setEstimateService(EstimateService estimateService) {
	this.estimateService = estimateService;
    }

    @Autowired(required = true)
    @Qualifier(value = "pullOutService")
    public void setPullOutService(PullOutService pullOutService) {
	this.pullOutService = pullOutService;
    }

    @Autowired(required = true)
    @Qualifier(value = "projectAuxService")
    public void setProjectAuxService(ProjectAuxService projectAuxService) {
	this.projectAuxService = projectAuxService;
    }

    @Autowired(required = true)
    @Qualifier(value = "projectPayrollService")
    public void setProjectPayrollService(ProjectPayrollService projectPayrollService) {
	this.projectPayrollService = projectPayrollService;
    }

    @Autowired(required = true)
    @Qualifier(value = "materialService")
    public void setMaterialService(MaterialService materialService) {
	this.materialService = materialService;
    }

    @Autowired(required = true)
    @Qualifier(value = "deliveryService")
    public void setDeliveryService(DeliveryService deliveryService) {
	this.deliveryService = deliveryService;
    }

    @Autowired(required = true)
    @Qualifier(value = "fieldService")
    public void setFieldService(FieldService s) {
	this.fieldService = s;
    }

    @Autowired(required = true)
    @Qualifier(value = "staffService")
    public void setStaffService(StaffService s) {
	this.staffService = s;
    }

    @Autowired(required = true)
    @Qualifier(value = "projectService")
    public void setProjectService(ProjectService s) {
	this.projectService = s;
    }

    /**
     * List projects.
     * 
     * @param model
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_ROOT, SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
    public String listProjects(Model model) {
	model.addAttribute(ATTR_LIST, this.projectService.list());
	return JSP_LIST;
    }

    /**
     * Unassign all staff from a project.
     * 
     * @param projectID
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/" + Staff.OBJECT_NAME + "-member" + "/"
	    + SystemConstants.ALL, method = RequestMethod.GET)
    public String unassignAllStaffMembers(HttpSession session, SessionStatus status,
	    RedirectAttributes redirectAttrs) {

	Project project = (Project) session.getAttribute(ATTR_PROJECT);

	// Get response.
	String response = this.staffService.unassignAllStaffMembers(project);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	return editPage(project.getId(), status);
    }

    /**
     * Complete the session and return back to the project edit page.
     * 
     * @param projectID
     * @param status
     * @return
     */
    private String editPage(long projectID, SessionStatus status) {
	status.setComplete();
	return String.format(URLRegistry.REDIRECT_PROJECT_EDIT, projectID);
    }

    /**
     * Unassign a staff from a project.
     * 
     * @param projectID
     * @param staffID
     * @param position
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/" + Staff.OBJECT_NAME + "-member"
	    + "/{" + Staff.OBJECT_NAME + "}", method = RequestMethod.GET)
    public String unassignStaffMember(HttpSession session, SessionStatus status,
	    @PathVariable(Staff.OBJECT_NAME) long staffID, RedirectAttributes redirectAttrs) {

	Project project = (Project) session.getAttribute(ATTR_PROJECT);

	// Get response.
	String response = this.staffService.unassignStaffMember(project, staffID);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	return editPage(project.getId(), status);
    }

    /**
     * Assign a staff to a project.
     * 
     * @param projectID
     * @param staffID
     * @param staffAssignment
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/" + Staff.OBJECT_NAME + "/"
	    + SystemConstants.MASS, method = RequestMethod.POST)
    public String assignMassStaff(HttpSession session, SessionStatus status,
	    @ModelAttribute(ATTR_PROJECT) Project project, RedirectAttributes redirectAttrs) {

	// Get response.
	// Do service, clear session.
	// Then redirect.
	String response = this.staffService.assignStaffMass(project);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	return editPage(project.getId(), status);
    }

    /**
     * Create a new project.
     * 
     * @param project
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
    public String create(@ModelAttribute(ATTR_PROJECT) Project project,
	    RedirectAttributes redirectAttrs, SessionStatus status) {

	// If request is to create a new project.
	if (project.getId() == 0) {

	    // Get response.
	    String response = this.projectService.create(project);

	    // Attach response.
	    redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	    return editPage(project.getId(), status);
	}

	// Get response.
	// If request is to edit a project.
	String response = this.projectService.update(project);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	return editPage(project.getId(), status);
    }

    /**
     * Update existing project fields.
     * 
     * @param session
     * @param fieldIdentifiers
     * @param status
     * @param model
     * @return
     */
    @RequestMapping(value = Field.OBJECT_NAME + "/" + SystemConstants.REQUEST_UPDATE, method = RequestMethod.POST)
    public String updateField(HttpSession session,
	    @ModelAttribute(ATTR_FIELD) FieldAssignmentBean newFaBean, SessionStatus status,
	    RedirectAttributes redirectAttrs) {

	// Old values.
	FieldAssignmentBean faBean = (FieldAssignmentBean) session.getAttribute("old" + ATTR_FIELD);

	// Get response.
	// Do service.
	String response = this.fieldService.updateAssignedProjectField(faBean.getProjectID(),
		faBean.getFieldID(), faBean.getLabel(), faBean.getValue(), newFaBean.getLabel(),
		newFaBean.getValue());

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Clear session and redirect.
	return editPage(faBean.getProjectID(), status);
    }

    /**
     * Unassign a field from a project.
     * 
     * @param fieldID
     * @param projectID
     * @return
     */
    @RequestMapping(value = Field.OBJECT_NAME + "/" + SystemConstants.REQUEST_DELETE, method = RequestMethod.GET)
    public String deleteProjectField(HttpSession session, SessionStatus status,
	    RedirectAttributes redirectAttrs) {

	// Fetch bean from session.
	FieldAssignmentBean faBean = (FieldAssignmentBean) session.getAttribute(ATTR_FIELD);

	// Do service.
	// Clear session attrs then redirect.
	// Get response.
	String response = this.fieldService.unassignFieldFromProject(faBean.getFieldID(),
		faBean.getProjectID(), faBean.getLabel(), faBean.getValue());

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	return editPage(faBean.getProjectID(), status);
    }

    /**
     * Getter. Opening the edit page of a project field.
     * 
     * @param id
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = Field.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/{"
	    + Field.OBJECT_NAME + "}", method = RequestMethod.GET)
    public String editField(HttpSession session,
	    @PathVariable(Field.OBJECT_NAME) String fieldIdentifiers, Model model) {

	// Get project id.
	Project proj = (Project) session.getAttribute(ProjectController.ATTR_PROJECT);
	long projectID = proj.getId();
	long fieldID = Long.valueOf(fieldIdentifiers.split(Field.IDENTIFIER_SEPARATOR)[0]);
	String label = fieldIdentifiers.split(Field.IDENTIFIER_SEPARATOR)[1];
	String value = fieldIdentifiers.split(Field.IDENTIFIER_SEPARATOR)[2];

	// Set to model attribute "field".
	model.addAttribute(ATTR_PROJECT, proj);
	model.addAttribute(ATTR_FIELD, new FieldAssignmentBean(projectID, fieldID, label, value));
	session.setAttribute("old" + ATTR_FIELD, new FieldAssignmentBean(projectID, fieldID, label,
		value));

	return JSP_EDIT_FIELD;
    }

    /**
     * Delete a project.
     * 
     * @param id
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_DELETE + "/{" + Project.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.GET)
    public String delete(@PathVariable(Project.COLUMN_PRIMARY_KEY) int id,
	    RedirectAttributes redirectAttrs, SessionStatus status) {

	// Reset search entries in cache.
	AuthenticationToken auth = this.authHelper.getAuth();
	Project project = this.projectService.getByID(id);

	// Get company and
	// clear cache.
	Long companyID = null;
	if (auth.getCompany() == null) {
	    if (project.getCompany() != null) {
		companyID = project.getCompany().getId();
	    }
	} else {
	    companyID = auth.getCompany().getId();
	}
	this.projectService.clearSearchCache(companyID);

	// Do service.
	// FIXME Cleanup also the SYS_HOME.
	// Get response.
	String response = this.projectService.delete(id);

	// Attach response.
	// Alert result.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	return listPage(status);
    }

    /**
     * Return to the list page.
     * 
     * @param status
     * @return
     */
    public String listPage(SessionStatus status) {
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECT + "/" + SystemConstants.REQUEST_LIST;
    }

    /**
     * Unassign all fields of a project.
     * 
     * @param fieldID
     * @param projectID
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/" + Field.OBJECT_NAME + "/"
	    + SystemConstants.ALL, method = RequestMethod.GET)
    public String unassignAllFields(HttpSession session, SessionStatus status,
	    RedirectAttributes redirectAttrs) {

	// Get project ID.
	Project proj = (Project) session.getAttribute(ProjectController.ATTR_PROJECT);
	long projectID = proj.getId();

	// Get response.
	String response = this.fieldService.unassignAllFieldsFromProject(projectID);

	// Attach response.
	// Construct notification.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Do service and clear session vars.
	// Then return.
	return editPage(projectID, status);
    }

    /**
     * Assign a field to a project.
     * 
     * @param fieldAssignment
     * @param fieldID
     * @param projectID
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/" + Field.OBJECT_NAME, method = RequestMethod.POST)
    public String assignField(HttpSession session,
	    @ModelAttribute(ATTR_FIELD) FieldAssignmentBean faBean, RedirectAttributes redirectAttrs,
	    SessionStatus status) {

	// Get project from session.
	// Construct commit object.
	Project proj = (Project) session.getAttribute(ProjectController.ATTR_PROJECT);
	long fieldID = 1;
	FieldAssignment fieldAssignment = new FieldAssignment();
	fieldAssignment.setLabel(faBean.getLabel());
	fieldAssignment.setField(new Field(faBean.getFieldID()));
	fieldAssignment.setValue(faBean.getValue());
	fieldAssignment.setProject(proj);

	// Do service.
	// Get response.
	String response = this.fieldService.assignFieldToProject(fieldAssignment, fieldID, proj.getId());

	// Construct ui notifications.
	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Remove session variables.
	// Evict project cache.
	return editPage(proj.getId(), status);
    }

    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/" + RedisConstants.OBJECT_PAYROLL + "/"
	    + SystemConstants.REQUEST_INCLUDE + "/" + Staff.OBJECT_NAME, method = RequestMethod.POST)
    public String includeStaffToPayroll(
	    @ModelAttribute(ATTR_PROJECT_PAYROLL) ProjectPayroll projectPayroll,
	    @ModelAttribute(ATTR_PAYROLL_INCLUDE_STAFF) PayrollIncludeStaffBean includeStaffBean,
	    RedirectAttributes redirectAttrs) {

	String response = this.projectPayrollService.includeStaffToPayroll(projectPayroll,
		includeStaffBean);

	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	return payrollEndState(projectPayroll);
    }

    /**
     * Do Update a pull out.
     * 
     * @param delivery
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_UPDATE + "/" + RedisConstants.OBJECT_PULL_OUT }, method = RequestMethod.POST)
    public String updatePullout(@ModelAttribute(RedisConstants.OBJECT_PULL_OUT) PullOut pullout,
	    RedirectAttributes redirectAttrs) {

	// Do service and get response.
	String response = this.pullOutService.update(pullout);

	// Add to redirect attrs.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Complete the transaction.
	return editSubmodulePage(RedisConstants.OBJECT_PULL_OUT, pullout.getKey());
    }

    /**
     * Update a material object.
     * 
     * @param delivery
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_UPDATE + "/" + RedisConstants.OBJECT_MATERIAL }, method = RequestMethod.POST)
    public String updateMaterial(@ModelAttribute(RedisConstants.OBJECT_MATERIAL) Material material,
	    RedirectAttributes redirectAttrs) {

	// Do service and get response.
	String response = this.materialService.update(material);

	// Add to redirect attrs.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Complete the transaction.
	return editSubmodulePage(RedisConstants.OBJECT_MATERIAL, material.getKey());
    }

    /**
     * Create a delivery object.
     * 
     * @param delivery
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE + "/" + RedisConstants.OBJECT_DELIVERY }, method = RequestMethod.POST)
    public String createDelivery(@ModelAttribute(RedisConstants.OBJECT_DELIVERY) Delivery delivery,
	    RedirectAttributes redirectAttrs) {

	// Do service and get response.
	String response = this.deliveryService.set(delivery);

	// Add to redirect attrs.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	return editSubmodulePage(RedisConstants.OBJECT_DELIVERY, delivery.getKey());
    }

    /**
     * Return to the edit page of the submodule.
     * 
     * @param submodule
     * @param key
     * @return
     */
    private String editSubmodulePage(String submodule, String key) {
	String deliveryEdit = SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + submodule + "/" + key + "-end";
	return deliveryEdit;
    }

    /**
     * Create many tasks by uploading an Excel file.
     */
    @RequestMapping(value = { URLRegistry.MASS_UPLOAD_AND_ASSIGN_TASK }, method = RequestMethod.POST)
    public String createMassTask(
	    @ModelAttribute(ATTR_MASS_UPLOAD_STAFF_BEAN) MassUploadBean massUploadTask,
	    RedirectAttributes redirectAttrs, SessionStatus status, HttpSession session) {

	Project proj = massUploadTask.getProject();

	// Do service and get response.
	String response = this.projectService.createTasksFromExcel(massUploadTask.getFile(), proj);

	// Add to redirect attrs.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Complete the transaction.
	return editPage(proj.getId(), status);
    }

    /**
     * Create many staff members by uploading an Excel file.
     */
    @RequestMapping(value = { URLRegistry.MASS_UPLOAD_AND_ASSIGN_STAFF }, method = RequestMethod.POST)
    public String createMassStaff(
	    @ModelAttribute(ATTR_MASS_UPLOAD_STAFF_BEAN) MassUploadBean massUploadStaff,
	    RedirectAttributes redirectAttrs, SessionStatus status, HttpSession session) {

	Project proj = massUploadStaff.getProject();

	// Do service and get response.
	String response = this.projectService.createStaffFromExcel(massUploadStaff.getFile(), proj);

	// Add to redirect attrs.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Complete the transaction.
	return editPage(proj.getId(), status);
    }

    /**
     * Do create/update on an estimate.
     * 
     * @param delivery
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE + "/" + RedisConstants.OBJECT_ESTIMATE }, method = RequestMethod.POST)
    public String createEstimate(
	    @ModelAttribute(ProjectController.ATTR_ESTIMATE_INPUT) EstimationInputBean estimateInput,
	    RedirectAttributes redirectAttrs, SessionStatus status, HttpSession session) {

	// Do service and get response.
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	estimateInput.setProject(proj);
	String response = this.estimateService.estimate(estimateInput);

	// Add to redirect attrs.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Complete the transaction.
	return editPage(proj.getId(), status);
    }

    /**
     * Delete a delivery.
     * 
     * @param key
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = { URLRegistry.VIEW_ESTIMATION_RESULTS }, method = RequestMethod.GET)
    public String viewEstimation(@PathVariable(RedisConstants.OBJECT_ESTIMATION_OUTPUT) String key,
	    Model model) {

	// Get estimation output.
	EstimationOutput output = this.estimationOutputService.get(key);

	// Attach to model.
	model.addAttribute(RedisConstants.OBJECT_ESTIMATION_OUTPUT, output);

	// Return.
	return RedisConstants.JSP_ESTIMATION_OUTPUT_EDIT;
    }

    /**
     * Delete an estimation.
     * 
     * @param key
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = { URLRegistry.DELETE_ESTIMATION_RESULTS }, method = RequestMethod.GET)
    public String deleteEstimate(@PathVariable(RedisConstants.OBJECT_ESTIMATION_OUTPUT) String key,
	    RedirectAttributes redirectAttrs, SessionStatus status, HttpSession session) {

	// Do service
	// and get response.
	String response = this.estimationOutputService.delete(key);

	// Attach to redirect.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Set completed.
	// Return.
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	return editPage(proj.getId(), status);
    }

    /**
     * Delete a delivery.
     * 
     * @param key
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_DELETE + "/" + RedisConstants.OBJECT_DELIVERY
	    + "/{" + RedisConstants.OBJECT_DELIVERY + "}-end" }, method = RequestMethod.GET)
    public String deleteDelivery(@PathVariable(RedisConstants.OBJECT_DELIVERY) String key,
	    RedirectAttributes redirectAttrs, SessionStatus status, HttpSession session) {

	// Do service
	// and get response.
	String response = this.deliveryService.delete(key);

	// Attach to redirect.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Set completed.
	// Return.
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	return editPage(proj.getId(), status);
    }

    /**
     * Delete a payroll entry.
     * 
     * @param key
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_DELETE + "/" + RedisConstants.OBJECT_PAYROLL
	    + "/{" + RedisConstants.OBJECT_PAYROLL + "}-end" }, method = RequestMethod.GET)
    public String deleteProjectPayroll(@PathVariable(RedisConstants.OBJECT_PAYROLL) String key,
	    RedirectAttributes redirectAttrs, SessionStatus status, HttpSession session) {

	// Do service
	// and get response.
	String response = this.projectPayrollService.delete(key);

	// Attach to redirect.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Set completed.
	// Return.
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	return editPage(proj.getId(), status);
    }

    /**
     * Delete a pull-out entry.
     * 
     * @param key
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_DELETE + "/" + RedisConstants.OBJECT_PULL_OUT
	    + "/{" + RedisConstants.OBJECT_PULL_OUT + "}-end" }, method = RequestMethod.GET)
    public String deletePullOut(@PathVariable(RedisConstants.OBJECT_PULL_OUT) String key,
	    RedirectAttributes redirectAttrs, SessionStatus status, HttpSession session) {

	// Do service
	// and get response.
	String response = this.pullOutService.delete(key);

	// Attach to redirect.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Set completed.
	// Return.
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	return editPage(proj.getId(), status);
    }

    /**
     * Create a task.
     * 
     * @param task
     * @param status
     * @param origin
     * @param originID
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = URLRegistry.CREATE_TASK, method = RequestMethod.POST)
    public String createTask(@ModelAttribute(ATTR_TASK) Task task, SessionStatus status,
	    RedirectAttributes redirectAttrs) {

	AlertBoxGenerator alertFactory = AlertBoxGenerator.SUCCESS;

	if (task.getId() == 0) {
	    this.taskService.create(task);
	    alertFactory.setMessage("Successfully <b>created</b> task <b>" + task.getTitle() + "</b>.");
	} else {
	    this.taskService.update(task);
	    alertFactory.setMessage("Successfully <b>updated</b> task <b>" + task.getTitle() + "</b>.");
	}
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, alertFactory.generateHTML());
	return editPage(task.getProject().getId(), status);
    }

    /**
     * Edit a task.
     * 
     * @param taskID
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = URLRegistry.EDIT_TASK, method = RequestMethod.GET)
    public String editTask(@PathVariable(Task.OBJECT_NAME) long taskID, Model model, HttpSession session) {

	Project proj = (Project) session.getAttribute(ATTR_PROJECT);

	// If ID is zero,
	// Open a page with empty values, ready to create.
	if (taskID == 0) {
	    model.addAttribute(ATTR_TASK, new Task(proj));
	    return TaskController.JSP_EDIT;
	}

	// Else, get the object from DB
	// then populate the fields in JSP.
	Task task = this.taskService.getByIDWithAllCollections(taskID);
	model.addAttribute(ATTR_TASK, task);
	return TaskController.JSP_EDIT;
    }

    /**
     * Delete all tasks.
     * 
     * @param taskID
     * @param session
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = URLRegistry.DELETE_TASK_ALL, method = RequestMethod.GET)
    public String deleteAllTask(HttpSession session, RedirectAttributes redirectAttrs,
	    SessionStatus status) {

	// Do service and get response.
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	String response = this.taskService.deleteAllTasksByProject(proj.getId());
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Complete the transaction.
	// Redirect.
	return editPage(proj.getId(), status);
    }

    /**
     * Delete all tasks and milestones.
     * 
     * @param taskID
     * @param session
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = URLRegistry.DELETE_PROGRAM_OF_WORKS, method = RequestMethod.GET)
    public String deleteAllTaskAndMilestone(HttpSession session, RedirectAttributes redirectAttrs,
	    SessionStatus status) {

	// Do service and get response.
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	String response = this.projectService.deleteProgramOfWorks(proj);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Complete the transaction.
	// Redirect.
	return editPage(proj.getId(), status);
    }

    /**
     * Delete a task.
     * 
     * @param taskID
     * @param session
     * @param redirectAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = URLRegistry.DELETE_TASK, method = RequestMethod.GET)
    public String deleteTask(@PathVariable(Task.OBJECT_NAME) long taskID, HttpSession session,
	    RedirectAttributes redirectAttrs, SessionStatus status) {

	// Do service and get response.
	String response = this.taskService.delete(taskID);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Complete the transaction.
	// Redirect.
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	return editPage(proj.getId(), status);
    }

    /**
     * Open an edit page for a material.
     * 
     * @param key
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/" + RedisConstants.OBJECT_MATERIAL + "/{"
	    + RedisConstants.OBJECT_MATERIAL + "}-end" }, method = RequestMethod.GET)
    public String editMaterial(@PathVariable(RedisConstants.OBJECT_MATERIAL) String key, Model model,
	    HttpSession session) {

	// Construct the bean for the form.
	Material material = this.materialService.get(key);
	model.addAttribute(ATTR_MATERIAL, material);

	// Get the list of staff in this project.
	// This is for the selector.
	// Who pulled-out the material?
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	Set<Staff> staffList = proj.getAssignedStaff();

	// Add material category list.
	// And Units list.
	model.addAttribute(ATTR_MATERIAL_CATEGORY_LIST, MaterialCategory.class.getEnumConstants());
	model.addAttribute(ATTR_UNIT_LIST_LENGTH, CommonLengthUnit.class.getEnumConstants());
	model.addAttribute(ATTR_UNIT_LIST_MASS, CommonMassUnit.class.getEnumConstants());
	model.addAttribute(ATTR_UNIT_LIST_VOLUME, CommonVolumeUnit.class.getEnumConstants());

	// Add the staff list to model.
	model.addAttribute(ATTR_STAFF_LIST, staffList);

	return RedisConstants.JSP_MATERIAL_EDIT;
    }

    /**
     * Open a create page for a pull out.
     * 
     * @param key
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_PULL_OUT + "/" + RedisConstants.OBJECT_MATERIAL
	    + "/{" + RedisConstants.OBJECT_MATERIAL + "}-end" }, method = RequestMethod.GET)
    public String pulloutMaterial(@PathVariable(RedisConstants.OBJECT_MATERIAL) String key, Model model,
	    HttpSession session) {

	// Construct the bean for the form.
	Material material = this.materialService.get(key);
	PullOut pullOut = new PullOut(material);
	model.addAttribute(ATTR_PULL_OUT, pullOut);

	// Get the list of staff in this project.
	// This is for the selector.
	// Who pulled-out the material?
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	Set<Staff> staffList = proj.getAssignedStaff();

	// Add the staff list to model.
	model.addAttribute(ATTR_STAFF_LIST, staffList);

	return RedisConstants.JSP_MATERIAL_PULLOUT;
    }

    /**
     * Do the pull-out of materials.
     * 
     * @param pullOut
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_DO_PULL_OUT + "/" + RedisConstants.OBJECT_MATERIAL }, method = RequestMethod.POST)
    public String createPullOut(@ModelAttribute(ATTR_PULL_OUT) PullOut pullOut,
	    RedirectAttributes redirectAttrs) {

	// Do service
	// and get response.
	String response = this.pullOutService.create(pullOut);

	// Add to model.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	return editSubmodulePage(RedisConstants.OBJECT_PULL_OUT, pullOut.getKey());
    }

    /**
     * Delete a material.
     * 
     * @param material
     * @param redirecAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_DELETE + "/" + RedisConstants.OBJECT_MATERIAL
	    + "/{" + RedisConstants.OBJECT_MATERIAL + "}-end" }, method = RequestMethod.GET)
    public String deleteMaterial(@PathVariable(RedisConstants.OBJECT_MATERIAL) String key,
	    RedirectAttributes redirecAttrs, SessionStatus status, HttpSession session) {

	// Do service
	// and get response.
	String response = this.materialService.delete(key);

	// Attach to redirect attributes.
	redirecAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Set completed.
	// Return to the project.
	Project project = (Project) session.getAttribute(ATTR_PROJECT);
	return editPage(project.getId(), status);
    }

    /**
     * Add a material to delivery.
     * 
     * @param material
     * @param redirecAttrs
     * @param status
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_ADD + "/" + RedisConstants.OBJECT_MATERIAL }, method = RequestMethod.POST)
    public String addMaterial(@ModelAttribute(RedisConstants.OBJECT_MATERIAL) Material material,
	    RedirectAttributes redirecAttrs, SessionStatus status) {

	// Do service
	// and get response.
	String response = this.materialService.create(material);

	// Attach to redirect attributes.
	redirecAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + RedisConstants.OBJECT_DELIVERY + "/"
		+ material.getDelivery().getKey() + "-end";
    }

    /**
     * Open an edit page with payroll object.
     * 
     * @param payrollKey
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE + "/" + RedisConstants.OBJECT_PAYROLL }, method = RequestMethod.POST)
    public String createPayroll(@ModelAttribute(ATTR_PROJECT_PAYROLL) ProjectPayroll projectPayroll,
	    Model model, HttpSession session, RedirectAttributes redirectAttrs) {

	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	if (proj == null) {
	    proj = this.projectService.getByIDWithAllCollections(projectPayroll.getProject().getId());
	}

	// Do service.
	String response = this.projectPayrollService.createPayroll(proj, projectPayroll);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// List of possible approvers.
	setFormSelectors(proj, model);

	// Complete the transaction.
	return payrollEndState(projectPayroll);
    }

    /**
     * Open an edit page with payroll object.
     * 
     * @param payrollKey
     * @return
     */
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE + "/" + RedisConstants.OBJECT_PAYROLL + "/"
	    + SystemConstants.CLEAR + "/{" + SystemConstants.CLEAR + "}" }, method = RequestMethod.POST)
    public String createPayrollClearComputation(
	    @ModelAttribute(ATTR_PROJECT_PAYROLL) ProjectPayroll projectPayroll,
	    @PathVariable(SystemConstants.CLEAR) String toClear, Model model, HttpSession session,
	    RedirectAttributes redirectAttrs) {

	// End the session after this.
	// Then redirect to an edit page of this object.
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	if (proj == null) {
	    proj = this.projectService.getByIDWithAllCollections(projectPayroll.getProject().getId());
	}

	// Update the payroll then clear the computation.
	String response = this.projectPayrollService.updatePayrollClearComputation(session,
		projectPayroll, toClear);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// List of possible approvers.
	setFormSelectors(proj, model);

	// Redirect to:
	return payrollEndState(projectPayroll);
    }

    /**
     * Open an edit page with payroll object.
     * 
     * @param payrollKey
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_COMPUTE + "/" + RedisConstants.OBJECT_PAYROLL, method = RequestMethod.GET)
    public String computePayroll(@ModelAttribute(ATTR_PROJECT_PAYROLL) ProjectPayroll projectPayroll,
	    Model model, HttpSession session, RedirectAttributes redirectAttrs) {

	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	if (proj == null) {
	    proj = this.projectService.getByIDWithAllCollections(projectPayroll.getProject().getId());
	}
	Date startDate = projectPayroll.getStartDate();
	Date endDate = projectPayroll.getEndDate();

	// Get payroll maps.
	// And assign to model.
	String payrollJSON = this.projectPayrollService.computeAndGetResultJSON(proj, startDate,
		endDate, projectPayroll);

	model.addAttribute(ATTR_PAYROLL_JSON, payrollJSON);

	// Construct response.
	String datePart = ProjectPayrollServiceImpl.getResponseDatePart(projectPayroll);
	String response = AlertBoxGenerator.SUCCESS.generateCompute(RedisConstants.OBJECT_PAYROLL,
		datePart);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// List of possible approvers.
	// Get all managers in this project.
	setFormSelectors(proj, model);

	return payrollEndState(projectPayroll);
    }

    /**
     * End state of payroll for create, update, and compute.
     * 
     * @param status
     * @param redirectAttrs
     * @param projectPayroll
     * @return
     */
    private String payrollEndState(ProjectPayroll projectPayroll) {

	// /edit/payroll/${payrollRow.getKey()}-end
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + RedisConstants.OBJECT_PAYROLL + "/"
		+ projectPayroll.getKey() + "-end";
    }

    /**
     * Open an edit page for a pull-out.
     * 
     * @param key
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/" + RedisConstants.OBJECT_PULL_OUT + "/{"
	    + RedisConstants.OBJECT_PULL_OUT + "}-end", method = RequestMethod.GET)
    public String editPullOut(@PathVariable(RedisConstants.OBJECT_PULL_OUT) String key, Model model,
	    HttpSession session) {

	// Get the object.
	PullOut pullOut = this.pullOutService.get(key);
	model.addAttribute(ATTR_PULL_OUT, pullOut);

	// Get the list of staff in this project.
	// This is for the selector.
	// Who pulled-out the material?
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	Set<Staff> staffList = proj.getAssignedStaff();

	// Add the staff list to model.
	model.addAttribute(ATTR_STAFF_LIST, staffList);

	// redirect to edit page.
	return RedisConstants.JSP_MATERIAL_PULLOUT;
    }

    /**
     * Open an edit page to create/update an object.
     * 
     * @param key
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/" + RedisConstants.OBJECT_DELIVERY + "/{"
	    + RedisConstants.OBJECT_DELIVERY + "}-end", method = RequestMethod.GET)
    public String editDelivery(@PathVariable(RedisConstants.OBJECT_DELIVERY) String key, Model model,
	    HttpSession session) {

	Project proj = (Project) session.getAttribute(ATTR_PROJECT);

	// If we're creating.
	// Return an empty object.
	if (key.equals("0")) {
	    model.addAttribute(ATTR_DELIVERY, new Delivery(proj));
	    return RedisConstants.JSP_DELIVERY_EDIT;
	}

	// Add material category list.
	// And Units list.
	model.addAttribute(ATTR_MATERIAL_CATEGORY_LIST, MaterialCategory.class.getEnumConstants());
	model.addAttribute(ATTR_UNIT_LIST_LENGTH, CommonLengthUnit.class.getEnumConstants());
	model.addAttribute(ATTR_UNIT_LIST_MASS, CommonMassUnit.class.getEnumConstants());
	model.addAttribute(ATTR_UNIT_LIST_VOLUME, CommonVolumeUnit.class.getEnumConstants());

	// If we're updating,
	// return the object from redis.
	Delivery delivery = this.deliveryService.get(key);
	model.addAttribute(ATTR_DELIVERY, delivery);

	// Get the list of materials this delivery has.
	// Then add to model.
	List<Material> materialList = this.materialService.list(delivery);
	model.addAttribute(ATTR_MATERIAL_LIST, materialList);
	model.addAttribute(ATTR_MATERIAL, new Material(delivery));

	return RedisConstants.JSP_DELIVERY_EDIT;
    }

    /**
     * Open an edit page with payroll object.
     * 
     * @param payrollKey
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/" + RedisConstants.OBJECT_PAYROLL + "/{"
	    + RedisConstants.OBJECT_PAYROLL + "}-end", method = RequestMethod.GET)
    public String editPayroll(@PathVariable(RedisConstants.OBJECT_PAYROLL) String payrollKey,
	    Model model, HttpSession session) {

	// Common to both edit new and existing.
	// List of all payroll status.
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);

	// Set the form selectors.
	// Managers and status.
	setFormSelectors(proj, model);

	// Required for key creation.
	Company co = proj.getCompany();

	// If a new payroll object.
	if (payrollKey.equals("0")) {

	    // Assign the creator.
	    // Add the empty object.
	    // Then redirect.
	    SystemUser creator = this.authHelper.getAuth().getUser();
	    model.addAttribute(ATTR_PROJECT_PAYROLL, new ProjectPayroll(co, proj, creator));
	    return RedisConstants.JSP_PAYROLL_EDIT;
	}

	// Attach to response.
	// If flash attribute was null,
	// use the key.
	ProjectPayroll projectPayroll = this.projectPayrollService.get(payrollKey);

	// Set the project structure.
	Long companyID = co == null ? 0 : co.getId();
	setModelAttributesOfPayroll(projectPayroll, proj, model, companyID);

	return RedisConstants.JSP_PAYROLL_EDIT;
    }

    /**
     * Options for the payroll status selector.
     * 
     * @param model
     */
    private void setFormSelectors(Project proj, Model model) {

	// Status.
	PayrollStatus[] payrollStatusArr = PayrollStatus.class.getEnumConstants();
	model.addAttribute(ATTR_PAYROLL_SELECTOR_STATUS, payrollStatusArr);
    }

    /**
     * Set the project structure.
     * 
     * @param projectPayroll
     * @param proj
     * @param model
     * @param proj
     */
    private void setModelAttributesOfPayroll(ProjectPayroll projectPayroll, Project proj, Model model,
	    Long companyID) {

	// Get collection of all staff here.
	List<Staff> manualStaffList = this.staffService.listUnassignedStaffInProjectPayroll(companyID,
		projectPayroll);

	// Full list minus already included.
	Set<Staff> staff = projectPayroll.getStaffList();

	// Set attributes.
	// Manually include team/staff beans.
	model.addAttribute(ATTR_PAYROLL_INCLUDE_STAFF, new PayrollIncludeStaffBean());

	// Actual object and result JSON.
	model.addAttribute(ATTR_PROJECT_PAYROLL, projectPayroll);
	model.addAttribute(ATTR_PAYROLL_JSON, projectPayroll.getPayrollJSON());

	// Structure/checklist attributes.
	model.addAttribute(ATTR_PAYROLL_CHECKBOX_STAFF, staff);
	model.addAttribute(ATTR_PAYROLL_MANUAL_STAFF_LIST, manualStaffList);
    }

    /**
     * Open an existing/new project page. TODO Remove this function.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/clear/cache/{id}")
    public String clearCache(@PathVariable("id") long id, SessionStatus status) {
	this.projectService.clearProjectCache(id);
	this.projectService.clearListCache();

	return editPage(id, status);
    }

    /**
     * Open an existing/new project page.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{" + Project.COLUMN_PRIMARY_KEY + "}")
    public String editProject(@PathVariable(Project.COLUMN_PRIMARY_KEY) long id, Model model) {

	// Set model attributes that are common in both create and update.
	setModelAttributes(model, id);

	// If ID is zero, create new.
	if (id == 0) {
	    model.addAttribute(ATTR_PROJECT, new Project());
	    return JSP_EDIT;
	}

	Project proj = this.projectService.getByIDWithAllCollections(id);
	model.addAttribute(ATTR_PROJECT, proj);

	// Estimate.
	setEstimateAttributes(proj, model);

	// Staff.
	setStaffAttributes(proj, model);

	// Payroll.
	setPayrollAttributes(proj, model);

	// Inventory.
	setInventoryAttributes(proj, model);

	// Auxillary.
	setAuxAttributes(proj, model);

	// Program of Works.
	setProgramOfWorksAttributes(proj, model);

	return JSP_EDIT;
    }

    /**
     * Set attributes used in Estimate.
     * 
     * @param proj
     * @param model
     */
    private void setEstimateAttributes(Project proj, Model model) {
	List<EstimationOutput> estimates = this.estimationOutputService.list(proj);
	model.addAttribute(ATTR_ESTIMATE_OUTPUT_LIST, estimates);
    }

    /**
     * Set attributes used by Staff and Manager tab.
     * 
     * @param proj
     * @param model
     */
    private void setStaffAttributes(Project proj, Model model) {

	// Get list of fields.
	// Get list of staff members for manager assignments.
	Long companyID = this.authHelper.getAuth().isSuperAdmin() ? null : proj.getCompany().getId();

	// Used in the manager selector.
	List<Staff> staffList = this.staffService.list();

	// Used in the "assign staff constrols".
	// Get the list of staff not yet assigned in this project.
	// Company staff, minus managers, minus assigned.
	List<Staff> availableStaffToAssign = this.staffService.listUnassignedInProject(companyID, proj);

	// Get lists for selectors.
	model.addAttribute(ATTR_STAFF_LIST_AVAILABLE, availableStaffToAssign);
	model.addAttribute(ATTR_STAFF_LIST, staffList);
	model.addAttribute(ATTR_STAFF_POSITION, new StaffAssignmentBean());
	model.addAttribute(ATTR_MASS_UPLOAD_STAFF_BEAN, new MassUploadBean(proj));
    }

    /**
     * Set auxillary objects.
     * 
     * @param proj
     * @param model
     */
    private void setAuxAttributes(Project proj, Model model) {
	// Add the auxillary object.
	ProjectAux projectAux = this.projectAuxService.get(proj);
	model.addAttribute(ATTR_PROJECT_AUX, projectAux);
    }

    /**
     * Set model attributes that are common in both create and update.
     * 
     * @param model
     * @param id
     */
    private void setModelAttributes(Model model, long id) {
	// Set common attributes.
	// Model for forms.
	model.addAttribute(ATTR_FIELD, new FieldAssignmentBean(id, 1));

	// Estimation input.
	model.addAttribute(ATTR_ESTIMATE_INPUT, new EstimationInputBean());
	model.addAttribute(ATTR_ESTIMATE_ALLOWANCE_LIST,
		TableEstimationAllowance.class.getEnumConstants());
    }

    /**
     * Set payroll attributes.
     * 
     * @param proj
     * @param model
     */
    private void setPayrollAttributes(Project proj, Model model) {
	// Get all payrolls.
	// Add to model.
	List<ProjectPayroll> payrollList = this.projectPayrollService.getAllPayrolls(proj);
	model.addAttribute(ATTR_PAYROLL_LIST, payrollList);
    }

    /**
     * Set inventory attributes.
     * 
     * @param proj
     * @param model
     */
    private void setInventoryAttributes(Project proj, Model model) {
	// Get all deliveries.
	// Get all pull-outs.
	// Get inventory.
	// Then add to model.
	List<Delivery> deliveryList = this.deliveryService.list(proj);
	model.addAttribute(ATTR_DELIVERY_LIST, deliveryList);

	// Get all materials.
	// Add to model.
	List<Material> materialList = this.materialService.list(proj);
	model.addAttribute(ATTR_MATERIAL_LIST, materialList);

	// Get all pull-outs.
	// Add to model.
	List<PullOut> pullOutList = this.pullOutService.list(proj);
	model.addAttribute(ATTR_PULL_OUT_LIST, pullOutList);
    }

    /**
     * Set attributes before forwarding back to JSP.
     * 
     * @param proj
     * @param model
     */
    @SuppressWarnings("unchecked")
    private void setProgramOfWorksAttributes(Project proj, Model model) {

	// Gant JSON to be used by the chart in timeline.
	// Get calendar JSON.
	model.addAttribute(ATTR_GANTT_JSON, this.projectService.getGanttJSON(proj));
	model.addAttribute(ATTR_CALENDAR_JSON, this.projectService.getCalendarJSON(proj));

	// Timeline taks status and count map.
	// Summary map found in timeline tab.
	model.addAttribute(ATTR_TIMELINE_TASK_STATUS_MAP,
		this.projectService.getTaskStatusCountMap(proj));
	model.addAttribute(ATTR_CALENDAR_EVENT_TYPES_LIST, CalendarEventType.class.getEnumConstants());
	model.addAttribute(ATTR_GANTT_TYPE_LIST, GanttElement.class.getEnumConstants());

	// Summary of per milestones.
	// Summary of timeline on all milestones.
	// Add map of id to milestone enum.
	Map<String, Object> milestoneSummaryMap = this.projectService.getTimelineSummaryMap(proj);
	Map<Milestone, Map<String, Object>> milestoneCountMap = (Map<Milestone, Map<String, Object>>) milestoneSummaryMap
		.get(ATTR_TIMELINE_MILESTONE_SUMMARY_MAP);
	Map<String, Integer> summaryMap = (Map<String, Integer>) milestoneSummaryMap
		.get(ATTR_TIMELINE_SUMMARY_MAP);
	model.addAttribute(ATTR_TIMELINE_MILESTONE_SUMMARY_MAP, milestoneCountMap);
	model.addAttribute(ATTR_TIMELINE_SUMMARY_MAP, summaryMap);
	model.addAttribute(ATTR_MAP_ID_TO_MILESTONE, MilestoneStatus.getIdToStatusMap());
    }
}