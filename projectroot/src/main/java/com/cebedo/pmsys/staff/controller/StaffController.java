package com.cebedo.pmsys.staff.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cebedo.pmsys.field.controller.FieldController;
import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.field.service.FieldService;
import com.cebedo.pmsys.payroll.domain.Attendance;
import com.cebedo.pmsys.payroll.domain.AttendanceMass;
import com.cebedo.pmsys.payroll.domain.Status;
import com.cebedo.pmsys.payroll.service.PayrollService;
import com.cebedo.pmsys.project.service.ProjectService;
import com.cebedo.pmsys.security.securityrole.model.SecurityRole;
import com.cebedo.pmsys.staff.model.ManagerAssignment;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.model.StaffTeamAssignment;
import com.cebedo.pmsys.staff.service.StaffService;
import com.cebedo.pmsys.system.bean.CalendarEventBean;
import com.cebedo.pmsys.system.bean.DateRangeBean;
import com.cebedo.pmsys.system.constants.SystemConstants;
import com.cebedo.pmsys.system.helper.DateHelper;
import com.cebedo.pmsys.system.ui.AlertBoxFactory;
import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.task.model.TaskStatus;
import com.cebedo.pmsys.team.controller.TeamController;
import com.cebedo.pmsys.team.model.Team;
import com.cebedo.pmsys.team.service.TeamService;
import com.google.gson.Gson;

@Controller
@SessionAttributes(value = { StaffController.ATTR_STAFF,
		StaffController.ATTR_ATTENDANCE, StaffController.ATTR_ATTENDANCE_MASS,
		StaffController.ATTR_CALENDAR_MIN_DATE,
		StaffController.ATTR_CALENDAR_MAX_DATE,
		StaffController.ATTR_CALENDAR_MIN_DATE_STR }, types = { Staff.class,
		Attendance.class, AttendanceMass.class, })
@RequestMapping(Staff.OBJECT_NAME)
public class StaffController {

	public static final String ATTR_LIST = "staffList";
	public static final String ATTR_STAFF = Staff.OBJECT_NAME;
	public static final String ATTR_PAYROLL_TOTAL_WAGE = "payrollTotalWage";
	public static final String JSP_LIST = "staffList";
	public static final String JSP_EDIT = "staffEdit";

	public static final String ATTR_TASK_STATUS_MAP = "taskStatusMap";

	public static final String ATTR_CALENDAR_JSON = "calendarJSON";
	public static final String ATTR_CALENDAR_STATUS_LIST = "calendarStatusList";
	public static final String ATTR_CALENDAR_MIN_DATE_STR = "minDateStr";
	public static final String ATTR_CALENDAR_MIN_DATE = "minDate";
	public static final String ATTR_CALENDAR_MAX_DATE = "maxDate";
	public static final String ATTR_CALENDAR_RANGE_DATES = "rangeDate";

	public static final String ATTR_ATTENDANCE = Attendance.OBJECT_NAME;
	public static final String ATTR_ATTENDANCE_LIST = "attendanceList";
	public static final String ATTR_ATTENDANCE_STATUS_MAP = "attendanceStatusMap";
	public static final String ATTR_ATTENDANCE_MASS = "massAttendance";

	private StaffService staffService;
	private TeamService teamService;
	private FieldService fieldService;
	private ProjectService projectService;
	private PayrollService payrollService;

	@Autowired(required = true)
	@Qualifier(value = "payrollService")
	public void setPayrollService(PayrollService s) {
		this.payrollService = s;
	}

	@Autowired(required = true)
	@Qualifier(value = "projectService")
	public void setProjectService(ProjectService s) {
		this.projectService = s;
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
	@Qualifier(value = "teamService")
	public void setTeamService(TeamService s) {
		this.teamService = s;
	}

	@RequestMapping(value = { SystemConstants.REQUEST_ROOT,
			SystemConstants.REQUEST_LIST }, method = RequestMethod.GET)
	public String listStaff(Model model) {
		model.addAttribute(ATTR_LIST,
				this.staffService.listWithAllCollections());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_LIST);
		return JSP_LIST;
	}

	/**
	 * Commit function that would create/update staff.
	 * 
	 * @param staff
	 * @param redirectAttrs
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE, method = RequestMethod.POST)
	public String create(@ModelAttribute(ATTR_STAFF) Staff staff,
			SessionStatus status, RedirectAttributes redirectAttrs) {
		// Add ui notifications.
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;

		// Create staff.
		if (staff.getId() == 0) {
			this.staffService.create(staff);
			alertFactory.setMessage("Successfully <b>created</b> staff <b>"
					+ staff.getFullName() + "</b>.");

			// Add redirs attrs.
			redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
					alertFactory.generateHTML());
			status.setComplete();
			return SystemConstants.CONTROLLER_REDIRECT + ATTR_STAFF + "/"
					+ SystemConstants.REQUEST_LIST;
		}
		// Update staff.
		this.staffService.update(staff);
		alertFactory.setMessage("Successfully <b>updated</b> staff <b>"
				+ staff.getFullName() + "</b>.");

		// Add redirs attrs.
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_STAFF + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + staff.getId();
	}

	/**
	 * Create a staff from the origin.
	 * 
	 * @param staff
	 * @param projectID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_CREATE + "/"
			+ SystemConstants.FROM + "/{" + SystemConstants.ORIGIN + "}/{"
			+ SystemConstants.ORIGIN_ID + "}", method = RequestMethod.POST)
	public String createFromOrigin(@ModelAttribute(ATTR_STAFF) Staff staff,
			@PathVariable(value = SystemConstants.ORIGIN) String origin,
			@PathVariable(value = SystemConstants.ORIGIN_ID) String originID,
			SessionStatus status, RedirectAttributes redirectAttrs) {
		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		if (staff.getId() == 0) {
			this.staffService.createFromOrigin(staff, origin, originID);

			alertFactory.setMessage("Successfully <b>created</b> staff <b>"
					+ staff.getFullName() + "</b>.");
		} else {
			alertFactory.setMessage("Successfully <b>updated</b> staff <b>"
					+ staff.getFullName() + "</b>.");

			this.staffService.update(staff);

			// Update all associated project.
			for (ManagerAssignment assignment : staff.getAssignedManagers()) {
				if (assignment.getProject() == null) {
					continue;
				}
				this.projectService.clearProjectCache(assignment.getProject()
						.getId());
			}
		}
		status.setComplete();
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());
		return SystemConstants.CONTROLLER_REDIRECT + origin + "/"
				+ SystemConstants.REQUEST_EDIT + "/" + originID;
	}

	/**
	 * Add an attendance in mass.
	 * 
	 * @return
	 */
	@RequestMapping(value = { SystemConstants.REQUEST_ADD + "/"
			+ Attendance.OBJECT_NAME + "/" + SystemConstants.MASS }, method = RequestMethod.POST)
	public String addAttendanceMass(
			@ModelAttribute(ATTR_ATTENDANCE_MASS) AttendanceMass attendanceMass,
			RedirectAttributes redirectAttrs, HttpSession session, Model model,
			SessionStatus status) {

		// If start date is > end date.
		Date startDate = attendanceMass.getStartDate();
		Date endDate = attendanceMass.getEndDate();

		if (startDate.after(endDate)) {
			// TODO
			redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
					AlertBoxFactory.FAILED.generateCreate("test", "TODO"));
			// Dont set completed.
			// Otherwise, min and max dates will be deleted in session.
			// status.setComplete();
			return SystemConstants.CONTROLLER_REDIRECT + Staff.OBJECT_NAME
					+ "/" + SystemConstants.REQUEST_EDIT + "/"
					+ attendanceMass.getStaff().getId();
		}

		// Do service.
		this.payrollService.multiSet(attendanceMass);

		// TODO
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				AlertBoxFactory.SUCCESS.generateCreate("test", "TODO"));

		return editStaffWithMinDate(model, session);
	}

	/**
	 * Add an attendance.
	 * 
	 * @return
	 */
	@RequestMapping(value = { SystemConstants.REQUEST_ADD + "/"
			+ Attendance.OBJECT_NAME }, method = RequestMethod.POST)
	public String addAttendance(
			@ModelAttribute(ATTR_ATTENDANCE) Attendance attendance,
			RedirectAttributes redirectAttrs, HttpSession session, Model model,
			SessionStatus status) {

		// Do service.
		this.payrollService.set(attendance);

		// TODO
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				AlertBoxFactory.SUCCESS.generateCreate("test", "TODO"));
		return editStaffWithMinDate(model, session);
	}

	/**
	 * Open a page to create or edit an attendance.
	 * 
	 * @return
	 */
	@RequestMapping(value = { SystemConstants.REQUEST_EDIT + "/"
			+ Attendance.OBJECT_NAME + "/{" + Attendance.OBJECT_NAME
			+ "}/{status}" }, method = RequestMethod.GET)
	public String editAttendance(
			@PathVariable(Attendance.OBJECT_NAME) long timestamp,
			@PathVariable("status") int status, HttpSession session, Model model) {

		// Get staff from session.
		Staff staff = (Staff) session.getAttribute(ATTR_STAFF);

		// Construct bean.
		Attendance attendance = new Attendance();
		if (timestamp == 0) {
			attendance = new Attendance(staff);
		} else {
			// TODO Make function for this in service.
			attendance = this.payrollService.get(staff, Status.of(status),
					new Date(timestamp));
		}

		// Attach bean to model.
		model.addAttribute(ATTR_ATTENDANCE, attendance);
		return Attendance.OBJECT_NAME + "/" + Attendance.JSP_EDIT;
	}

	/**
	 * Delete coming from the staff list page.
	 * 
	 * @param id
	 * @param redirectAttrs
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE + "/{"
			+ Staff.OBJECT_NAME + "}", method = RequestMethod.GET)
	public String delete(@PathVariable(Staff.OBJECT_NAME) long id,
			SessionStatus status, RedirectAttributes redirectAttrs) {

		Staff staff = this.staffService.getByID(id);

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>deleted</b> staff <b>"
				+ staff.getFullName() + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		this.staffService.delete(id);
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_STAFF + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	/**
	 * Delete coming from the staff edit page.
	 * 
	 * @param status
	 * @param session
	 * @param redirectAttrs
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_STAFF_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_DELETE, method = RequestMethod.POST)
	public String delete(SessionStatus status, HttpSession session,
			RedirectAttributes redirectAttrs) {

		Staff staff = (Staff) session.getAttribute(ATTR_STAFF);
		if (staff == null) {
			AlertBoxFactory alertFactory = AlertBoxFactory.FAILED;
			alertFactory
					.setMessage("Error occured when you tried to <b>delete</b> staff. Please try again.");
			redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
					alertFactory.generateHTML());
			status.setComplete();
			return SystemConstants.CONTROLLER_REDIRECT + ATTR_STAFF + "/"
					+ SystemConstants.REQUEST_LIST;
		}

		AlertBoxFactory alertFactory = AlertBoxFactory.SUCCESS;
		alertFactory.setMessage("Successfully <b>deleted</b> staff <b>"
				+ staff.getFullName() + "</b>.");
		redirectAttrs.addFlashAttribute(SystemConstants.UI_PARAM_ALERT,
				alertFactory.generateHTML());

		this.staffService.delete(staff.getId());
		status.setComplete();
		return SystemConstants.CONTROLLER_REDIRECT + ATTR_STAFF + "/"
				+ SystemConstants.REQUEST_LIST;
	}

	/**
	 * If the Create/Edit Staff request is coming from the project.
	 * 
	 * @param staffID
	 * @param projectID
	 * @param model
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
			+ Staff.OBJECT_NAME + "}/" + SystemConstants.FROM + "/{"
			+ SystemConstants.ORIGIN + "}/{" + SystemConstants.ORIGIN_ID + "}", method = RequestMethod.GET)
	public String editStaffFromOrigin(
			@PathVariable(Staff.OBJECT_NAME) long staffID,
			@PathVariable(value = SystemConstants.ORIGIN) String origin,
			@PathVariable(value = SystemConstants.ORIGIN_ID) long originID,
			Model model) {
		// Add origin details.
		model.addAttribute(SystemConstants.ORIGIN, origin);
		model.addAttribute(SystemConstants.ORIGIN_ID, originID);

		// If new, create it.
		if (staffID == 0) {
			Staff stf = new Staff();
			model.addAttribute(ATTR_STAFF, stf);
			model.addAttribute(SystemConstants.ATTR_ACTION,
					SystemConstants.ACTION_CREATE);
			return JSP_EDIT;
		}

		// Else if not new, edit it.
		model.addAttribute(ATTR_STAFF,
				this.staffService.getWithAllCollectionsByID(staffID));
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
		return JSP_EDIT;
	}

	/**
	 * Open a view page where the user can edit the staff.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/"
			+ SystemConstants.RANGE)
	public String editStaffRangeDates(
			@ModelAttribute(ATTR_CALENDAR_RANGE_DATES) DateRangeBean rangeDates,
			HttpSession session, Model model) {
		// Get prelim objects.
		List<Team> teamList = this.teamService.list();
		List<Field> fields = this.fieldService.list();
		model.addAttribute(TeamController.JSP_LIST, teamList);
		model.addAttribute(FieldController.JSP_LIST, fields);
		Staff staff = (Staff) session.getAttribute(ATTR_STAFF);

		// Get the start and end date from the bean.
		Date min = rangeDates.getStartDate();
		Date max = rangeDates.getEndDate();

		// Given min and max, get range of attendances.
		// Get wage given attendances.
		String minDateStr = DateHelper.formatDate(min, "yyyy-MM-dd");

		// Add attributes to model.
		setModelAttributes(model, staff, min, max, minDateStr);
		return JSP_EDIT;
	}

	/**
	 * Forward to an edit page with an empty staff.
	 * 
	 * @param model
	 * @return
	 */
	public String editEmptyStaff(Model model) {
		model.addAttribute(ATTR_STAFF, new Staff());
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_CREATE);
		return JSP_EDIT;
	}

	/**
	 * Get corresponding calendar events given a set of attendances.
	 * 
	 * @param attendanceList
	 * @return
	 */
	private List<CalendarEventBean> getCalendarEvents(
			Set<Attendance> attendanceList) {
		List<CalendarEventBean> calendarEvents = new ArrayList<CalendarEventBean>();
		for (Attendance attendance : attendanceList) {
			CalendarEventBean event = new CalendarEventBean();

			Date myDate = attendance.getTimestamp();
			Status attnStat = attendance.getStatus();
			String start = DateHelper.formatDate(myDate, "yyyy-MM-dd");
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
		return calendarEvents;
	}

	/**
	 * Get calendar min and max dates from session.
	 * 
	 * @param session
	 * @return
	 */
	private Map<String, Date> getCalendarRangeDates(HttpSession session) {
		Date min = (Date) session.getAttribute(ATTR_CALENDAR_MIN_DATE);
		Date max = (Date) session.getAttribute(ATTR_CALENDAR_MAX_DATE);

		if (min == null) {
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH); // Zero-based.
			min = new GregorianCalendar(year, month, 1).getTime();

			// Based on minimum, get max days in current month.
			// Given max days, create max object.
			cal.setTime(min);
			int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			max = new GregorianCalendar(year, month, maxDays).getTime();
		}
		Map<String, Date> datePair = new HashMap<String, Date>();
		datePair.put(ATTR_CALENDAR_MIN_DATE, min);
		datePair.put(ATTR_CALENDAR_MAX_DATE, max);
		return datePair;
	}

	/**
	 * Open a view page where the user can edit the staff.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	private String editStaffWithMinDate(Model model, HttpSession session) {

		// TODO Check if where this is used.
		// If not used, delete.
		model.addAttribute(TeamController.JSP_LIST, this.teamService.list());
		model.addAttribute(FieldController.JSP_LIST, this.fieldService.list());

		// Get staff object.
		// Get the current year and month.
		// This will be minimum.
		Staff staff = (Staff) session.getAttribute(ATTR_STAFF);
		Date minDate = (Date) session.getAttribute(ATTR_CALENDAR_MIN_DATE);
		Date maxDate = (Date) session.getAttribute(ATTR_CALENDAR_MAX_DATE);
		String minDateStr = (String) session
				.getAttribute(ATTR_CALENDAR_MIN_DATE_STR);

		// Set model attributes.
		setModelAttributes(model, staff, minDate, maxDate, minDateStr);
		return JSP_EDIT;
	}

	/**
	 * Open a view page where the user can edit the staff.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = SystemConstants.REQUEST_EDIT + "/{"
			+ Staff.COLUMN_PRIMARY_KEY + "}")
	public String editStaff(@PathVariable(Staff.COLUMN_PRIMARY_KEY) int id,
			Model model, HttpSession session) {

		// TODO Check if where this is used.
		// If not used, delete.
		model.addAttribute(TeamController.JSP_LIST, this.teamService.list());
		model.addAttribute(FieldController.JSP_LIST, this.fieldService.list());

		// If action is to create new staff.
		if (id == 0) {
			return editEmptyStaff(model);
		}

		// Get staff object.
		// Get the current year and month.
		// This will be minimum.
		Staff staff = this.staffService.getWithAllCollectionsByID(id);
		Map<String, Date> datePair = getCalendarRangeDates(session);
		Date min = datePair.get(ATTR_CALENDAR_MIN_DATE);
		Date max = datePair.get(ATTR_CALENDAR_MAX_DATE);

		// Set model attributes.
		setModelAttributes(model, staff, min, max, null);
		return JSP_EDIT;
	}

	/**
	 * Get summary of tasks to count.
	 * 
	 * @param staff
	 * @return
	 */
	private Map<TaskStatus, Integer> getTaskStatusMap(Staff staff) {
		Map<TaskStatus, Integer> taskStatusMap = new HashMap<TaskStatus, Integer>();
		for (Task task : staff.getTasks()) {
			TaskStatus taskStatus = TaskStatus.of(task.getStatus());
			Integer statCount = taskStatusMap.get(taskStatus) == null ? 1
					: taskStatusMap.get(taskStatus) + 1;
			taskStatusMap.put(taskStatus, statCount);
		}
		return taskStatusMap;
	}

	/**
	 * Set model attributes before forwarding to JSP.
	 * 
	 * @param model
	 * @param staff
	 * @param min
	 * @param max
	 * @param minDateStr
	 */
	private void setModelAttributes(Model model, Staff staff, Date min,
			Date max, String minDateStr) {

		// Given min and max, get range of attendances.
		// Get wage given attendances.
		Set<Attendance> attendanceList = this.payrollService
				.rangeStaffAttendance(staff, min, max);

		// Get calendar events.
		// And count number per status.
		List<CalendarEventBean> calendarEvents = new ArrayList<CalendarEventBean>();
		Map<Status, Integer> attendanceStatusMap = new HashMap<Status, Integer>();

		for (Attendance attendance : attendanceList) {

			Date myDate = attendance.getTimestamp();
			String start = DateHelper.formatDate(myDate, "yyyy-MM-dd");
			Status attnStat = attendance.getStatus();

			// Get and set status count.
			Integer statCount = attendanceStatusMap.get(attnStat) == null ? 1
					: attendanceStatusMap.get(attnStat) + 1;
			attendanceStatusMap.put(attnStat, statCount);

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

		// Given min and max, get range of attendances.
		// Get wage given attendances.
		double totalWage = this.payrollService
				.getTotalWageFromAttendance(attendanceList);

		// Get summary of tasks.
		Map<TaskStatus, Integer> taskStatusMap = getTaskStatusMap(staff);

		model.addAttribute(ATTR_CALENDAR_STATUS_LIST,
				Status.getAllStatusInMap());
		model.addAttribute(ATTR_CALENDAR_MIN_DATE_STR,
				minDateStr == null ? DateHelper.formatDate(min, "yyyy-MM-dd")
						: minDateStr);
		model.addAttribute(ATTR_CALENDAR_RANGE_DATES, new DateRangeBean());
		model.addAttribute(ATTR_CALENDAR_MIN_DATE, min);
		model.addAttribute(ATTR_CALENDAR_MAX_DATE, max);
		model.addAttribute(ATTR_TASK_STATUS_MAP, taskStatusMap);
		model.addAttribute(ATTR_PAYROLL_TOTAL_WAGE, totalWage);
		model.addAttribute(ATTR_ATTENDANCE_STATUS_MAP, attendanceStatusMap);
		model.addAttribute(ATTR_ATTENDANCE_MASS, new AttendanceMass(staff));
		model.addAttribute(ATTR_ATTENDANCE, new Attendance(staff));
		model.addAttribute(ATTR_CALENDAR_JSON,
				new Gson().toJson(calendarEvents, ArrayList.class));
		model.addAttribute(ATTR_ATTENDANCE_LIST, attendanceList);
		model.addAttribute(ATTR_STAFF, staff);
		model.addAttribute(SystemConstants.ATTR_ACTION,
				SystemConstants.ACTION_EDIT);
	}

	/**
	 * Unassign a staff from a team.
	 * 
	 * @param teamID
	 * @param staffID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Team.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView unassignTeam(
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID,
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin) {
		this.staffService.unassignTeam(teamID, staffID);
		if (origin.equals(Team.OBJECT_NAME)) {
			return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
					+ Team.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT
					+ "/" + teamID);
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ staffID);
	}

	/**
	 * Unassign all teams from a staff.
	 * 
	 * @param teamID
	 * @param staffID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_UNASSIGN + "/"
			+ Team.OBJECT_NAME + "/" + SystemConstants.ALL, method = RequestMethod.POST)
	public ModelAndView unassignAllTeams(
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID) {
		this.staffService.unassignAllTeams(staffID);
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ staffID);
	}

	/**
	 * Assign a team to a staff.
	 * 
	 * @param staffID
	 * @param teamID
	 * @return
	 */
	@PreAuthorize("hasRole('" + SecurityRole.ROLE_TEAM_EDITOR + "')")
	@RequestMapping(value = SystemConstants.REQUEST_ASSIGN + "/"
			+ Team.OBJECT_NAME, method = RequestMethod.POST)
	public ModelAndView assignTeam(
			@RequestParam(Staff.COLUMN_PRIMARY_KEY) long staffID,
			@RequestParam(Team.COLUMN_PRIMARY_KEY) long teamID,
			@RequestParam(value = SystemConstants.ORIGIN, required = false) String origin,
			@RequestParam(value = SystemConstants.ORIGIN_ID, required = false) String originID) {
		StaffTeamAssignment stAssign = new StaffTeamAssignment();
		stAssign.setStaffID(staffID);
		stAssign.setTeamID(teamID);
		this.staffService.assignTeam(stAssign);
		if (!origin.isEmpty()) {
			return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
					+ origin + "/" + SystemConstants.REQUEST_EDIT + "/"
					+ originID);
		}
		return new ModelAndView(SystemConstants.CONTROLLER_REDIRECT
				+ Staff.OBJECT_NAME + "/" + SystemConstants.REQUEST_EDIT + "/"
				+ staffID);
	}
}
