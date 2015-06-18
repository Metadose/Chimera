package com.cebedo.pmsys.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.bean.FieldAssignmentBean;
import com.cebedo.pmsys.bean.MultipartBean;
import com.cebedo.pmsys.bean.PayrollIncludeStaffBean;
import com.cebedo.pmsys.bean.StaffAssignmentBean;
import com.cebedo.pmsys.bean.TeamAssignmentBean;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.constants.SystemConstants;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.enums.CalendarEventType;
import com.cebedo.pmsys.enums.GanttElement;
import com.cebedo.pmsys.enums.MilestoneStatus;
import com.cebedo.pmsys.enums.PayrollStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.model.Photo;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.ProjectFile;
import com.cebedo.pmsys.model.SecurityRole;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;
import com.cebedo.pmsys.service.DeliveryService;
import com.cebedo.pmsys.service.FieldService;
import com.cebedo.pmsys.service.MaterialService;
import com.cebedo.pmsys.service.PhotoService;
import com.cebedo.pmsys.service.ProjectAuxService;
import com.cebedo.pmsys.service.ProjectFileService;
import com.cebedo.pmsys.service.ProjectPayrollService;
import com.cebedo.pmsys.service.ProjectPayrollServiceImpl;
import com.cebedo.pmsys.service.ProjectService;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.service.TeamService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxFactory;
import com.cebedo.pmsys.utils.DateUtils;

@Controller
@SessionAttributes(value = { Project.OBJECT_NAME, ProjectController.ATTR_FIELD,
	"old" + ProjectController.ATTR_FIELD,
	ProjectController.ATTR_PROJECT_FILE, RedisConstants.OBJECT_PAYROLL,
	RedisConstants.OBJECT_DELIVERY, RedisConstants.OBJECT_MATERIAL,
	RedisConstants.OBJECT_PULL_OUT }, types = { Project.class,
	FieldAssignmentBean.class, ProjectFile.class, ProjectPayroll.class,
	Delivery.class, Material.class, PullOut.class })
@RequestMapping(Project.OBJECT_NAME)
public class ProjectController {

    public static final String ATTR_LIST = "projectList";
    public static final String ATTR_PROJECT = Project.OBJECT_NAME;
    public static final String ATTR_PROJECT_AUX = RedisConstants.OBJECT_PROJECT_AUX;
    public static final String ATTR_DELIVERY = RedisConstants.OBJECT_DELIVERY;
    public static final String ATTR_MATERIAL = RedisConstants.OBJECT_MATERIAL;
    public static final String ATTR_PULL_OUT = RedisConstants.OBJECT_PULL_OUT;
    public static final String ATTR_FIELD = Field.OBJECT_NAME;
    public static final String ATTR_PHOTO = Photo.OBJECT_NAME;
    public static final String ATTR_STAFF = Staff.OBJECT_NAME;
    public static final String ATTR_TASK = Task.OBJECT_NAME;
    public static final String ATTR_ALL_STAFF = "allStaff";
    public static final String ATTR_PROJECT_PAYROLL = "projectPayroll";
    public static final String ATTR_MATERIAL_LIST = "materialList";
    public static final String ATTR_DELIVERY_LIST = "deliveryList";
    public static final String ATTR_PAYROLL_LIST = "payrollList";
    public static final String ATTR_PAYROLL_LIST_TOTAL = "payrollListTotal";
    public static final String ATTR_PROJECT_FILE = ProjectFile.OBJECT_NAME;
    public static final String ATTR_STAFF_POSITION = "staffPosition";
    public static final String ATTR_TEAM_ASSIGNMENT = "teamAssignment";
    public static final String ATTR_FILE = "file";

    public static final String ATTR_PAYROLL_SELECTOR_STATUS = "payrollStatusArr";
    public static final String ATTR_PAYROLL_SELECTOR_APPROVER = "payrollApproverOptions";

    public static final String ATTR_CALENDAR_EVENT_TYPES_MAP = "calendarEventTypesMap";
    public static final String ATTR_CALENDAR_EVENT_TYPES_LIST = "calendarEventTypes";
    public static final String ATTR_CALENDAR_JSON = "calendarJSON";
    public static final String ATTR_GANTT_JSON = "ganttJSON";
    public static final String ATTR_GANTT_TYPE_LIST = "ganttElemTypeList";

    public static final String ATTR_TIMELINE_TASK_STATUS_MAP = "taskStatusMap";
    public static final String ATTR_TIMELINE_MILESTONE_SUMMARY_MAP = "milestoneSummary";
    public static final String ATTR_TIMELINE_SUMMARY_MAP = "timelineSummaryMap";

    public static final String ATTR_PAYROLL_JSON = "payrollJSON";
    public static final String ATTR_PAYROLL_CHECKBOX_MANAGERS = "managerList";
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
    public static final String JSP_EDIT_FIELD = Field.OBJECT_NAME
	    + "/assignedFieldEdit";

    private AuthHelper authHelper = new AuthHelper();

    private ProjectService projectService;
    private StaffService staffService;
    private TeamService teamService;
    private FieldService fieldService;
    private PhotoService photoService;
    private ProjectFileService projectFileService;
    private DeliveryService deliveryService;
    private MaterialService materialService;
    private ProjectPayrollService projectPayrollService;
    private ProjectAuxService projectAuxService;

    @Autowired(required = true)
    @Qualifier(value = "projectAuxService")
    public void setProjectAuxService(ProjectAuxService projectAuxService) {
	this.projectAuxService = projectAuxService;
    }

    @Autowired(required = true)
    @Qualifier(value = "projectPayrollService")
    public void setProjectPayrollService(
	    ProjectPayrollService projectPayrollService) {
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
    @Qualifier(value = "projectFileService")
    public void setProjectFileService(ProjectFileService ps) {
	this.projectFileService = ps;
    }

    @Autowired(required = true)
    @Qualifier(value = "photoService")
    public void setPhotoService(PhotoService ps) {
	this.photoService = ps;
    }

    @Autowired(required = true)
    @Qualifier(value = "fieldService")
    public void setFieldService(FieldService s) {
	this.fieldService = s;
    }

    @Autowired(required = true)
    @Qualifier(value = "teamService")
    public void setTeamService(TeamService s) {
	this.teamService = s;
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
    @RequestMapping(value = { SystemConstants.REQUEST_ROOT,
	    SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Staff.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.GET)
    public String unassignAllProjectManagers(HttpSession session,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	Project project = (Project) session.getAttribute(ATTR_PROJECT);
	long projectID = project.getId();

	// Get response.
	String response = this.staffService
		.unassignAllProjectManagers(projectID);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + projectID;
    }

    /**
     * Unassign all staff from a project.
     * 
     * @param projectID
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Staff.OBJECT_NAME + "-member" + "/" + SystemConstants.ALL, method = RequestMethod.GET)
    public String unassignAllStaffMembers(HttpSession session,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	Project project = (Project) session.getAttribute(ATTR_PROJECT);

	// Get response.
	String response = this.staffService.unassignAllStaffMembers(project);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + project.getId();
    }

    /**
     * Unassign a staff from a project.
     * 
     * @param projectID
     * @param staffID
     * @param position
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Staff.OBJECT_NAME + "/{" + Staff.OBJECT_NAME + "}", method = RequestMethod.GET)
    public String unassignProjectManager(HttpSession session,
	    SessionStatus status,
	    @PathVariable(Staff.OBJECT_NAME) long staffID,
	    RedirectAttributes redirectAttrs) {

	Project project = (Project) session.getAttribute(ATTR_PROJECT);
	long projectID = project.getId();

	// Get response.
	String response = this.staffService.unassignProjectManager(projectID,
		staffID);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + projectID;
    }

    /**
     * Unassign a staff from a project.
     * 
     * @param projectID
     * @param staffID
     * @param position
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Staff.OBJECT_NAME + "-member" + "/{" + Staff.OBJECT_NAME + "}", method = RequestMethod.GET)
    public String unassignStaffMember(HttpSession session,
	    SessionStatus status,
	    @PathVariable(Staff.OBJECT_NAME) long staffID,
	    RedirectAttributes redirectAttrs) {

	Project project = (Project) session.getAttribute(ATTR_PROJECT);
	long projectID = project.getId();

	// Get response.
	String response = this.staffService.unassignStaffMember(project,
		staffID);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + projectID;
    }

    /**
     * Assign a staff to a project.
     * 
     * @param projectID
     * @param staffID
     * @param staffAssignment
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
	    + Staff.OBJECT_NAME + "/" + SystemConstants.MASS, method = RequestMethod.POST)
    public String assignStaffMass(HttpSession session, SessionStatus status,
	    @ModelAttribute(ATTR_PROJECT) Project project,
	    RedirectAttributes redirectAttrs) {

	// Get response.
	// Do service, clear session.
	// Then redirect.
	String response = this.staffService.assignStaffMass(project);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + project.getId();
    }

    /**
     * Assign a staff to a project.
     * 
     * @param projectID
     * @param staffID
     * @param staffAssignment
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
	    + Staff.OBJECT_NAME, method = RequestMethod.POST)
    public String assignProjectManager(
	    HttpSession session,
	    SessionStatus status,
	    @ModelAttribute(ATTR_STAFF_POSITION) StaffAssignmentBean staffAssignment,
	    RedirectAttributes redirectAttrs) {

	Project project = (Project) session.getAttribute(ATTR_PROJECT);
	long staffID = staffAssignment.getStaffID();
	String position = staffAssignment.getPosition();

	// Get response.
	// Do service, clear session.
	// Then redirect.
	String response = this.staffService.assignProjectManager(
		project.getId(), staffID, position);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + project.getId();
    }

    /**
     * Create a new project.
     * 
     * @param project
     * @param redirectAttrs
     * @param status
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
    public String create(@ModelAttribute(ATTR_PROJECT) Project project,
	    RedirectAttributes redirectAttrs, SessionStatus status) {

	// If request is to create a new project.
	if (project.getId() == 0) {

	    // Get response.
	    String response = this.projectService.create(project);

	    // Attach response.
	    redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		    response);

	    status.setComplete();
	    return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECT + "/"
		    + SystemConstants.REQUEST_LIST;
	}

	// Get response.
	// If request is to edit a project.
	String response = this.projectService.update(project);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECT + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + project.getId();
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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = Field.OBJECT_NAME + "/"
	    + SystemConstants.REQUEST_UPDATE, method = RequestMethod.POST)
    public String updateField(HttpSession session,
	    @ModelAttribute(ATTR_FIELD) FieldAssignmentBean newFaBean,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	// Old values.
	FieldAssignmentBean faBean = (FieldAssignmentBean) session
		.getAttribute("old" + ATTR_FIELD);

	// Get response.
	// Do service.
	String response = this.fieldService.updateAssignedProjectField(
		faBean.getProjectID(), faBean.getFieldID(), faBean.getLabel(),
		faBean.getValue(), newFaBean.getLabel(), newFaBean.getValue());

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Clear session and redirect.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + faBean.getProjectID();
    }

    /**
     * Unassign team from a project.
     * 
     * @param projectID
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Team.OBJECT_NAME + "/{" + Team.OBJECT_NAME + "}", method = RequestMethod.GET)
    public String unassignProjectTeam(HttpSession session,
	    SessionStatus status, @PathVariable(Team.OBJECT_NAME) long teamID,
	    RedirectAttributes redirectAttrs) {

	Project proj = (Project) session
		.getAttribute(ProjectController.ATTR_PROJECT);
	long projectID = proj.getId();

	// Get response.
	String response = this.teamService.unassignProjectTeam(projectID,
		teamID);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + projectID;
    }

    /**
     * Assign team to a project.
     * 
     * @param projectID
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
	    + Team.OBJECT_NAME, method = RequestMethod.POST)
    public String assignProjectTeam(
	    @ModelAttribute(ATTR_TEAM_ASSIGNMENT) TeamAssignmentBean taBean,
	    HttpSession session, SessionStatus status,
	    RedirectAttributes redirectAttrs) {

	long teamID = taBean.getTeamID();
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	long projectID = proj.getId();

	// Do service.
	// Get response.
	String response = this.teamService.assignProjectTeam(projectID, teamID);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + projectID;
    }

    /**
     * Unassign all teams inside a project.
     * 
     * @param projectID
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Team.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.GET)
    public String unassignAllProjectTeams(HttpSession session,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	Project proj = (Project) session
		.getAttribute(ProjectController.ATTR_PROJECT);
	long projectID = proj.getId();

	// Get response.
	String response = this.teamService.unassignAllProjectTeams(projectID);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + projectID;
    }

    // @RequestMapping(value = Staff.OBJECT_NAME + "/"
    // + SystemConstants.REQUEST_EDIT + "/{" + Staff.OBJECT_NAME + "}", method =
    // RequestMethod.GET)
    // public String editStaff(
    // @PathVariable(Staff.OBJECT_NAME) long staffID,
    // @RequestParam(value = SystemConstants.ORIGIN, required = false) String
    // origin,
    // @RequestParam(value = SystemConstants.ORIGIN_ID, required = false) long
    // originID,
    // Model model) {
    //
    // // Add origin details.
    // model.addAttribute(SystemConstants.ORIGIN, origin);
    // model.addAttribute(SystemConstants.ORIGIN_ID, originID);
    //
    // // If new, create it.
    // if (staffID == 0) {
    // model.addAttribute(ATTR_STAFF, new Staff());
    // model.addAttribute(SystemConstants.ATTR_ACTION,
    // SystemConstants.ACTION_CREATE);
    // return JSP_EDIT;
    // }
    //
    // // Else if not new, edit it.
    // model.addAttribute(ATTR_STAFF,
    // this.staffService.getWithAllCollectionsByID(staffID));
    // model.addAttribute(SystemConstants.ATTR_ACTION,
    // SystemConstants.ACTION_EDIT);
    // return JSP_EDIT;
    // }

    /**
     * Unassign a field from a project.
     * 
     * @param fieldID
     * @param projectID
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = Field.OBJECT_NAME + "/"
	    + SystemConstants.REQUEST_DELETE, method = RequestMethod.GET)
    public String deleteProjectField(HttpSession session, SessionStatus status,
	    RedirectAttributes redirectAttrs) {

	// Fetch bean from session.
	FieldAssignmentBean faBean = (FieldAssignmentBean) session
		.getAttribute(ATTR_FIELD);

	// Do service.
	// Clear session attrs then redirect.
	// Get response.
	String response = this.fieldService.unassignFieldFromProject(
		faBean.getFieldID(), faBean.getProjectID(), faBean.getLabel(),
		faBean.getValue());

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + faBean.getProjectID();
    }

    /**
     * Getter. Opening the edit page of a project field.
     * 
     * @param id
     * @param redirectAttrs
     * @param status
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = Field.OBJECT_NAME + "/"
	    + SystemConstants.REQUEST_EDIT + "/{" + Field.OBJECT_NAME + "}", method = RequestMethod.GET)
    public String editField(HttpSession session,
	    @PathVariable(Field.OBJECT_NAME) String fieldIdentifiers,
	    Model model) {

	// Get project id.
	Project proj = (Project) session
		.getAttribute(ProjectController.ATTR_PROJECT);
	long projectID = proj.getId();
	long fieldID = Long.valueOf(fieldIdentifiers
		.split(Field.IDENTIFIER_SEPARATOR)[0]);
	String label = fieldIdentifiers.split(Field.IDENTIFIER_SEPARATOR)[1];
	String value = fieldIdentifiers.split(Field.IDENTIFIER_SEPARATOR)[2];

	// Set to model attribute "field".
	model.addAttribute(ATTR_PROJECT, proj);
	model.addAttribute(ATTR_FIELD, new FieldAssignmentBean(projectID,
		fieldID, label, value));
	session.setAttribute("old" + ATTR_FIELD, new FieldAssignmentBean(
		projectID, fieldID, label, value));

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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_DELETE + "/{"
	    + Project.COLUMN_PRIMARY_KEY + "}", method = RequestMethod.GET)
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
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + ATTR_PROJECT + "/"
		+ SystemConstants.REQUEST_LIST;
    }

    /**
     * Delete a project's profile picture.
     * 
     * @param projectID
     * @return
     * @throws IOException
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.PROFILE + "/"
	    + SystemConstants.REQUEST_DELETE, method = RequestMethod.GET)
    public ModelAndView deleteProjectProfile(HttpSession session,
	    SessionStatus status, RedirectAttributes redirectAttrs)
	    throws IOException {
	// Get project id.
	Project proj = (Project) session
		.getAttribute(ProjectController.ATTR_PROJECT);
	long projectID = proj.getId();

	// Do service.
	// Get response.
	String response = this.photoService
		.deleteProfilePicOfProject(projectID);

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Clear session var.
	// Then return.
	status.setComplete();
	return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
		+ Project.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
		+ "/" + projectID);
    }

    /**
     * Upload a project profile pic.
     * 
     * @param mBean
     * @return
     * @throws IOException
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UPLOAD + "/"
	    + SystemConstants.PROFILE, method = RequestMethod.POST)
    public String uploadProfile(HttpSession session,
	    @RequestParam(ATTR_FILE) MultipartFile file, SessionStatus status,
	    RedirectAttributes redirectAttrs) throws IOException {

	Project proj = (Project) session
		.getAttribute(ProjectController.ATTR_PROJECT);
	long projectID = proj.getId();
	String response = "";

	// If file is not empty.
	// Get response.
	if (!file.isEmpty()) {
	    response = this.photoService.uploadProfilePicOfProject(file,
		    projectID);
	} else {
	    // TODO Handle this scenario.
	}

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + projectID;
    }

    /**
     * Unassign all fields of a project.
     * 
     * @param fieldID
     * @param projectID
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
	    + Field.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.GET)
    public String unassignAllFields(HttpSession session, SessionStatus status,
	    RedirectAttributes redirectAttrs) {

	// Get project ID.
	Project proj = (Project) session
		.getAttribute(ProjectController.ATTR_PROJECT);
	long projectID = proj.getId();

	// Get response.
	String response = this.fieldService
		.unassignAllFieldsFromProject(projectID);

	// Attach response.
	// Construct notification.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Do service and clear session vars.
	// Then return.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + projectID;
    }

    /**
     * Assign a field to a project.
     * 
     * @param fieldAssignment
     * @param fieldID
     * @param projectID
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
	    + Field.OBJECT_NAME, method = RequestMethod.POST)
    public String assignField(HttpSession session,
	    @ModelAttribute(ATTR_FIELD) FieldAssignmentBean faBean,
	    RedirectAttributes redirectAttrs, SessionStatus status) {

	// Get project from session.
	// Construct commit object.
	Project proj = (Project) session
		.getAttribute(ProjectController.ATTR_PROJECT);
	long fieldID = 1;
	FieldAssignment fieldAssignment = new FieldAssignment();
	fieldAssignment.setLabel(faBean.getLabel());
	fieldAssignment.setField(new Field(faBean.getFieldID()));
	fieldAssignment.setValue(faBean.getValue());
	fieldAssignment.setProject(proj);

	// Do service.
	// Get response.
	String response = this.fieldService.assignFieldToProject(
		fieldAssignment, fieldID, proj.getId());

	// Construct ui notifications.
	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Remove session variables.
	// Evict project cache.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + proj.getId();
    }

    /**
     * Upload a file to a project.
     * 
     * @param file
     * @param projectID
     * @param description
     * @param redirectAttrs
     * @return
     * @throws IOException
     */
    @RequestMapping(value = SystemConstants.REQUEST_UPLOAD + "/"
	    + ProjectFile.OBJECT_NAME, method = RequestMethod.POST)
    public String uploadFileToProject(
	    @ModelAttribute(ATTR_PROJECT_FILE) MultipartBean mpBean,
	    SessionStatus status, HttpSession session,
	    RedirectAttributes redirectAttrs) throws IOException {

	MultipartFile file = mpBean.getFile();
	String description = mpBean.getDescription();
	long projectID = mpBean.getProjectID();
	String response = "";

	// If file is not empty.
	if (!file.isEmpty()) {

	    // Upload then evict cache.
	    // Get response.
	    response = this.projectFileService.uploadFileToProject(file,
		    projectID, description);
	} else {
	    // TODO Handle this scenario.
	}

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + projectID;
    }

    /**
     * Download a file from Project. TODO Need work on security url here.
     * 
     * @param projectID
     * @param fileID
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_DOWNLOAD + "/"
	    + ProjectFile.OBJECT_NAME + "/{" + ProjectFile.OBJECT_NAME + "}", method = RequestMethod.GET)
    public void downloadFileFromProject(
	    @PathVariable(ProjectFile.OBJECT_NAME) long fileID,
	    HttpServletResponse response) {

	// Get file from server.
	File actualFile = this.projectFileService.getPhysicalFileByID(fileID);

	try {

	    // Serve it to the client.
	    FileInputStream iStream = new FileInputStream(actualFile);
	    response.setContentType("application/octet-stream");
	    response.setContentLength((int) actualFile.length());
	    response.setHeader("Content-Disposition", "attachment; filename=\""
		    + actualFile.getName() + "\"");
	    IOUtils.copy(iStream, response.getOutputStream());
	    response.flushBuffer();

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Upload a photo to a project.
     * 
     * @param file
     * @param projectID
     * @param description
     * @param redirectAttrs
     * @return
     * @throws IOException
     */
    @RequestMapping(value = SystemConstants.REQUEST_UPLOAD + "/"
	    + Photo.OBJECT_NAME, method = RequestMethod.POST)
    public String uploadPhotoToProject(
	    @ModelAttribute(ATTR_PHOTO) MultipartBean mpBean,
	    HttpSession session, SessionStatus status,
	    RedirectAttributes redirectAttrs) throws IOException {

	Project proj = (Project) session
		.getAttribute(ProjectController.ATTR_PROJECT);
	MultipartFile file = mpBean.getFile();
	String description = mpBean.getDescription();
	long projectID = proj.getId();
	String response = "";

	// TODO Limit only uploads to known extensions of an image.
	// If file is not empty.
	if (!file.isEmpty()) {

	    // Get response.
	    response = this.photoService.uploadPhotoToProject(file, projectID,
		    description);
	} else {
	    // TODO Handle scenario.
	}

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + projectID;
    }

    /**
     * Delete a photo from a project.
     * 
     * @param projectID
     * @param id
     * @param redirectAttrs
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_DELETE + "/"
	    + Photo.OBJECT_NAME + "/{" + Photo.OBJECT_NAME + "}", method = RequestMethod.GET)
    public String deletePhoto(@PathVariable(Photo.OBJECT_NAME) long id,
	    HttpSession session, SessionStatus status,
	    RedirectAttributes redirectAttrs) {

	// Do service.
	// Get response.
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	String response = this.photoService.delete(id);
	this.projectService.clearProjectCache(proj.getId());

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + proj.getId();
    }

    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/"
	    + RedisConstants.OBJECT_PAYROLL + "/"
	    + SystemConstants.REQUEST_INCLUDE + "/" + Staff.OBJECT_NAME, method = RequestMethod.POST)
    public String includeStaffToPayroll(
	    @ModelAttribute(ATTR_PROJECT_PAYROLL) ProjectPayroll projectPayroll,
	    @ModelAttribute(ATTR_PAYROLL_INCLUDE_STAFF) PayrollIncludeStaffBean includeStaffBean,
	    RedirectAttributes redirectAttrs) {

	String response = this.projectPayrollService.includeStaffToPayroll(
		projectPayroll, includeStaffBean);

	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	return payrollEndState(projectPayroll);
    }

    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE + "/"
	    + RedisConstants.OBJECT_DELIVERY }, method = RequestMethod.POST)
    public String createDelivery(
	    @ModelAttribute(RedisConstants.OBJECT_DELIVERY) Delivery delivery,
	    RedirectAttributes redirectAttrs, SessionStatus status) {

	// Do service and get response.
	String response = this.deliveryService.set(delivery);

	// Add to redirect attrs.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	// Complete the transaction.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/"
		+ delivery.getProject().getId();
    }

    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = { SystemConstants.REQUEST_PULL_OUT + "/"
	    + RedisConstants.OBJECT_MATERIAL + "/{"
	    + RedisConstants.OBJECT_MATERIAL + "}-end" }, method = RequestMethod.GET)
    public String pulloutMaterial(
	    @PathVariable(RedisConstants.OBJECT_MATERIAL) String key,
	    Model model, HttpSession session) {

	// Construct the bean for the form.
	Material material = this.materialService.get(key);
	PullOut pullOut = new PullOut(material);
	model.addAttribute(ATTR_PULL_OUT, pullOut);

	// Get the list of staff in this project.
	// This is for the selector.
	// Who pulled-out the material?
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	List<Staff> staffList = proj.getAssignedStaffAndManagers();

	// Add the staff list to model.
	model.addAttribute(ATTR_STAFF_LIST, staffList);

	return RedisConstants.JSP_MATERIAL_PULLOUT;
    }

    /**
     * Add a material to delivery.
     * 
     * @param material
     * @param redirecAttrs
     * @param status
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = { SystemConstants.REQUEST_ADD + "/"
	    + RedisConstants.OBJECT_MATERIAL }, method = RequestMethod.POST)
    public String addMaterial(
	    @ModelAttribute(RedisConstants.OBJECT_MATERIAL) Material material,
	    RedirectAttributes redirecAttrs, SessionStatus status) {

	// Do service
	// and get response.
	String response = this.materialService.set(material);

	// Attach to redirect attributes.
	redirecAttrs
		.addFlashAttribute(SystemConstants.UI_PARAM_ALERT, response);

	// Set completed.
	// Then redirect back to the delivery.
	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/"
		+ RedisConstants.OBJECT_DELIVERY + "/"
		+ material.getDelivery().getKey() + "-end";
    }

    /**
     * Open an edit page with payroll object.
     * 
     * @param payrollKey
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE + "/"
	    + RedisConstants.OBJECT_PAYROLL }, method = RequestMethod.POST)
    public String createPayroll(
	    @ModelAttribute(ATTR_PROJECT_PAYROLL) ProjectPayroll projectPayroll,
	    Model model, HttpSession session, RedirectAttributes redirectAttrs) {

	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	if (proj == null) {
	    proj = this.projectService.getByIDWithAllCollections(projectPayroll
		    .getProject().getId());
	}

	// Do service.
	String response = this.projectPayrollService.createPayroll(session,
		proj, projectPayroll);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = { SystemConstants.REQUEST_CREATE + "/"
	    + RedisConstants.OBJECT_PAYROLL + "/" + SystemConstants.CLEAR
	    + "/{" + SystemConstants.CLEAR + "}" }, method = RequestMethod.POST)
    public String createPayrollClearComputation(
	    @ModelAttribute(ATTR_PROJECT_PAYROLL) ProjectPayroll projectPayroll,
	    @PathVariable(SystemConstants.CLEAR) String toClear, Model model,
	    HttpSession session, RedirectAttributes redirectAttrs) {

	// End the session after this.
	// Then redirect to an edit page of this object.
	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	if (proj == null) {
	    proj = this.projectService.getByIDWithAllCollections(projectPayroll
		    .getProject().getId());
	}

	// Update the payroll then clear the computation.
	String response = this.projectPayrollService
		.createPayrollClearComputation(session, projectPayroll, toClear);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_COMPUTE + "/"
	    + RedisConstants.OBJECT_PAYROLL, method = RequestMethod.GET)
    public String computePayroll(
	    @ModelAttribute(ATTR_PROJECT_PAYROLL) ProjectPayroll projectPayroll,
	    Model model, HttpSession session, RedirectAttributes redirectAttrs) {

	Project proj = (Project) session.getAttribute(ATTR_PROJECT);
	if (proj == null) {
	    proj = this.projectService.getByIDWithAllCollections(projectPayroll
		    .getProject().getId());
	}
	Date startDate = projectPayroll.getStartDate();
	Date endDate = projectPayroll.getEndDate();

	// Get payroll maps.
	// And assign to model.
	String payrollJSON = this.projectPayrollService.setAndGetResultJSON(
		proj, startDate, endDate, projectPayroll);

	model.addAttribute(ATTR_PAYROLL_JSON, payrollJSON);

	// Construct response.
	String datePart = ProjectPayrollServiceImpl
		.getResponseDatePart(projectPayroll);
	String response = AlertBoxFactory.SUCCESS.generateCompute(
		RedisConstants.OBJECT_PAYROLL, datePart);
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

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
		+ SystemConstants.REQUEST_EDIT + "/"
		+ RedisConstants.OBJECT_PAYROLL + "/" + projectPayroll.getKey()
		+ "-end";
    }

    /**
     * Construct key needed for payroll edit.
     * 
     * @param projectPayroll
     * @return
     */
    @SuppressWarnings("unused")
    private String createPayrollRedirectKey(ProjectPayroll projectPayroll) {
	// Redirect to:
	// /project/edit/payroll/${payrollKey}-end
	String payrollKey = projectPayroll.getApprover() + "-";
	payrollKey += projectPayroll.getCreator() + "-";
	payrollKey += projectPayroll.getStatus() + "-";
	payrollKey += DateUtils.formatDate(projectPayroll.getStartDate()) + "-";
	payrollKey += DateUtils.formatDate(projectPayroll.getEndDate());

	return payrollKey;
    }

    /**
     * Open an edit page to create/update an object.
     * 
     * @param key
     * @param model
     * @param session
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/"
	    + RedisConstants.OBJECT_DELIVERY + "/{"
	    + RedisConstants.OBJECT_DELIVERY + "}-end", method = RequestMethod.GET)
    public String editDelivery(
	    @PathVariable(RedisConstants.OBJECT_DELIVERY) String key,
	    Model model, HttpSession session) {

	Project proj = (Project) session.getAttribute(ATTR_PROJECT);

	// If we're creating.
	// Return an empty object.
	if (key.equals("0")) {
	    model.addAttribute(ATTR_DELIVERY, new Delivery(proj));
	    return RedisConstants.JSP_DELIVERY_EDIT;
	}

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
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/"
	    + RedisConstants.OBJECT_PAYROLL + "/{"
	    + RedisConstants.OBJECT_PAYROLL + "}-end", method = RequestMethod.GET)
    public String editPayroll(
	    @PathVariable(RedisConstants.OBJECT_PAYROLL) String payrollKey,
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
	    model.addAttribute(ATTR_PROJECT_PAYROLL, new ProjectPayroll(co,
		    proj, creator));
	    return RedisConstants.JSP_PAYROLL_EDIT;
	}

	// Attach to response.
	// If flash attribute was null,
	// use the key.
	ProjectPayroll projectPayroll = this.projectPayrollService
		.get(payrollKey);

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

	// Approvers.
	List<Staff> selectorManagers = this.projectService
		.getAllManagersWithUsers(proj);
	model.addAttribute(ATTR_PAYROLL_SELECTOR_APPROVER, selectorManagers);

	// Status.
	PayrollStatus[] payrollStatusArr = PayrollStatus.class
		.getEnumConstants();
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
    private void setModelAttributesOfPayroll(ProjectPayroll projectPayroll,
	    Project proj, Model model, Long companyID) {

	// For the multi-select box.
	// Get all staff in this project payroll.
	// List of staff "during that time".
	Set<ManagerAssignment> managers = projectPayroll
		.getManagerAssignments();

	// Get collection of all staff here.
	List<Staff> manualStaffList = this.staffService
		.listUnassignedStaffInProjectPayroll(companyID, projectPayroll);

	// Full list minus already included.
	Set<Staff> staff = projectPayroll.getStaffList();

	// Set attributes.
	// Manually include team/staff beans.
	model.addAttribute(ATTR_PAYROLL_INCLUDE_STAFF,
		new PayrollIncludeStaffBean());

	// Actual object and result JSON.
	model.addAttribute(ATTR_PROJECT_PAYROLL, projectPayroll);
	model.addAttribute(ATTR_PAYROLL_JSON, projectPayroll.getPayrollJSON());

	// Structure/checklist attributes.
	model.addAttribute(ATTR_PAYROLL_CHECKBOX_MANAGERS, managers);
	model.addAttribute(ATTR_PAYROLL_CHECKBOX_STAFF, staff);
	model.addAttribute(ATTR_PAYROLL_MANUAL_STAFF_LIST, manualStaffList);
    }

    /**
     * Delete a project file. TODO Make this general, create "From Origin"
     * version.
     * 
     * @param id
     * @param projectID
     * @param redirectAttrs
     * @return
     */
    @PreAuthorize("hasRole('" + SecurityRole.ROLE_PROJECT_EDITOR + "')")
    @RequestMapping(value = SystemConstants.REQUEST_DELETE + "/"
	    + ProjectFile.OBJECT_NAME + "/{" + ProjectFile.OBJECT_NAME + "}", method = RequestMethod.GET)
    public String deleteProjectfile(
	    @PathVariable(ProjectFile.OBJECT_NAME) int id, HttpSession session,
	    SessionStatus status, RedirectAttributes redirectAttrs) {

	Project proj = (Project) session.getAttribute(ATTR_PROJECT);

	// Delete, then
	// Evict cache.
	String response = this.projectFileService.delete(id);
	this.projectService.clearProjectCache(proj.getId());

	// Attach response.
	redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
		response);

	status.setComplete();
	return SystemConstants.CONTROLLER_REDIRECT + Project.OBJECT_NAME + "/"
		+ SystemConstants.REQUEST_EDIT + "/" + proj.getId();
    }

    /**
     * Open an existing/new project page. TODO Remove this function.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/clear/cache/{id}")
    public void clearCache(@PathVariable("id") long id) {
	this.projectService.clearProjectCache(id);
	this.projectService.clearListCache();

	System.out.println("Cache cleared.");
    }

    /**
     * Open an existing/new project page.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
	    + Project.COLUMN_PRIMARY_KEY + "}")
    public String editProject(
	    @PathVariable(Project.COLUMN_PRIMARY_KEY) long id, Model model) {

	// Model for forms.
	model.addAttribute(ATTR_FIELD, new FieldAssignmentBean(id, 1));

	// If ID is zero, create new.
	if (id == 0) {
	    model.addAttribute(ATTR_PROJECT, new Project());
	    return JSP_EDIT;
	}

	// Get payroll rows.
	Project proj = this.projectService.getByIDWithAllCollections(id);

	// Get all payrolls.
	// Add to model.
	List<ProjectPayroll> payrollList = this.projectPayrollService
		.getAllPayrolls(proj);
	model.addAttribute(ATTR_PAYROLL_LIST, payrollList);

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

	// Do post-adding of attrs.
	// Return to the edit page.
	setModelAttributesOfProject(proj, model);
	return JSP_EDIT;
    }

    /**
     * Set attributes before forwarding back to JSP.
     * 
     * @param proj
     * @param model
     */
    @SuppressWarnings("unchecked")
    private void setModelAttributesOfProject(Project proj, Model model) {
	// Get list of fields.
	// Get list of staff members for manager assignments.
	Long companyID = this.authHelper.getAuth().isSuperAdmin() ? null : proj
		.getCompany().getId();

	// Used in the manager selector.
	List<Staff> staffList = this.staffService.listWithUsersAndFilter(
		companyID, proj.getManagers());

	// Used in the "assign staff constrols".
	// Get the list of staff not yet assigned in this project.
	// Company staff, minus managers, minus assigned.
	List<Staff> availableStaffToAssign = this.staffService
		.listUnassignedInProject(companyID, proj);

	// Get lists for selectors.
	// Actual object and beans.
	model.addAttribute(ATTR_STAFF_LIST_AVAILABLE, availableStaffToAssign);
	model.addAttribute(ATTR_STAFF_LIST, staffList);
	model.addAttribute(ATTR_STAFF_POSITION, new StaffAssignmentBean());
	model.addAttribute(ATTR_PROJECT, proj);

	// Gant JSON to be used by the chart in timeline.
	// Get calendar JSON.
	model.addAttribute(ATTR_GANTT_JSON,
		this.projectService.getGanttJSON(proj));
	model.addAttribute(ATTR_CALENDAR_JSON,
		this.projectService.getCalendarJSON(proj));

	// Timeline taks status and count map.
	// Summary map found in timeline tab.
	model.addAttribute(ATTR_TIMELINE_TASK_STATUS_MAP,
		this.projectService.getTaskStatusCountMap(proj));
	model.addAttribute(ATTR_CALENDAR_EVENT_TYPES_LIST,
		CalendarEventType.class.getEnumConstants());
	model.addAttribute(ATTR_GANTT_TYPE_LIST,
		GanttElement.class.getEnumConstants());

	// Summary of per milestones.
	// Summary of timeline on all milestones.
	// Add map of id to milestone enum.
	Map<String, Object> milestoneSummaryMap = this.projectService
		.getTimelineSummaryMap(proj);
	Map<Milestone, Map<String, Object>> milestoneCountMap = (Map<Milestone, Map<String, Object>>) milestoneSummaryMap
		.get(ATTR_TIMELINE_MILESTONE_SUMMARY_MAP);
	Map<String, Integer> summaryMap = (Map<String, Integer>) milestoneSummaryMap
		.get(ATTR_TIMELINE_SUMMARY_MAP);
	model.addAttribute(ATTR_TIMELINE_MILESTONE_SUMMARY_MAP,
		milestoneCountMap);
	model.addAttribute(ATTR_TIMELINE_SUMMARY_MAP, summaryMap);
	model.addAttribute(ATTR_MAP_ID_TO_MILESTONE,
		MilestoneStatus.getIdToStatusMap());

	// Add the auxillary object.
	ProjectAux projectAux = this.projectAuxService.get(proj);
	model.addAttribute(ATTR_PROJECT_AUX, projectAux);
    }
}