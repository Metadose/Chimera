package com.cebedo.pmsys.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.constants.ConstantsSystem;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.pojo.FormDateRange;
import com.cebedo.pmsys.pojo.FormMassAttendance;
import com.cebedo.pmsys.service.AttendanceService;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DateUtils;

@Controller
@SessionAttributes(value = { StaffController.ATTR_STAFF, StaffController.ATTR_ATTENDANCE,
	StaffController.ATTR_ATTENDANCE_MASS, StaffController.ATTR_CALENDAR_MIN_DATE,
	StaffController.ATTR_CALENDAR_MAX_DATE, StaffController.ATTR_CALENDAR_MAX_DATE_STR }, types = {
	Staff.class, Attendance.class, FormMassAttendance.class })
@RequestMapping(Staff.OBJECT_NAME)
public class StaffController {

    public static final String ATTR_LIST = "staffList";
    public static final String ATTR_STAFF = Staff.OBJECT_NAME;
    public static final String ATTR_PAYROLL_TOTAL_WAGE = "payrollTotalWage";
    public static final String JSP_LIST = Staff.OBJECT_NAME + "/staffList";
    public static final String JSP_EDIT = Staff.OBJECT_NAME + "/staffEdit";

    public static final String ATTR_TASK_STATUS_MAP = "taskStatusMap";

    public static final String ATTR_GANTT_JSON = "ganttJSON";

    public static final String ATTR_CALENDAR_JSON = "calendarJSON";
    public static final String ATTR_CALENDAR_STATUS_LIST = "calendarStatusList";
    public static final String ATTR_CALENDAR_MAX_DATE_STR = "maxDateStr";
    public static final String ATTR_CALENDAR_MIN_DATE = "minDate";
    public static final String ATTR_CALENDAR_MAX_DATE = "maxDate";
    public static final String ATTR_CALENDAR_RANGE_DATES = "rangeDate";

    public static final String ATTR_ATTENDANCE = ConstantsRedis.OBJECT_ATTENDANCE;
    public static final String ATTR_ATTENDANCE_LIST = "attendanceList";
    public static final String ATTR_ATTENDANCE_STATUS_MAP = "attendanceStatusMap";
    public static final String ATTR_ATTENDANCE_MASS = "massAttendance";

    private StaffService staffService;
    private AttendanceService attendanceService;

    @Autowired(required = true)
    @Qualifier(value = "attendanceService")
    public void setAttendanceService(AttendanceService s) {
	this.attendanceService = s;
    }

    @Autowired(required = true)
    @Qualifier(value = "staffService")
    public void setStaffService(StaffService s) {
	this.staffService = s;
    }

    @RequestMapping(value = { ConstantsSystem.REQUEST_ROOT, ConstantsSystem.REQUEST_LIST }, method = RequestMethod.GET)
    public String listStaff(Model model) {
	model.addAttribute(ATTR_LIST, this.staffService.listWithAllCollections());
	return JSP_LIST;
    }

    /**
     * Commit function that would create/update staff.
     * 
     * @param staff
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_CREATE, method = RequestMethod.POST)
    public String create(@ModelAttribute(ATTR_STAFF) Staff staff, SessionStatus status,
	    RedirectAttributes redirectAttrs) {
	// Add ui notifications.
	AlertBoxGenerator alertFactory = AlertBoxGenerator.SUCCESS;

	// Create staff.
	if (staff.getId() == 0) {
	    this.staffService.create(staff);
	    alertFactory.setMessage("Successfully <b>created</b> staff <b>" + staff.getFullName()
		    + "</b>.");

	    // Add redirs attrs.
	    redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());
	    status.setComplete();
	    return ConstantsSystem.CONTROLLER_REDIRECT + ATTR_STAFF + "/" + ConstantsSystem.REQUEST_LIST;
	}
	// Update staff.
	this.staffService.update(staff);
	alertFactory.setMessage("Successfully <b>updated</b> staff <b>" + staff.getFullName() + "</b>.");

	// Add redirs attrs.
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());
	status.setComplete();
	return ConstantsSystem.CONTROLLER_REDIRECT + ATTR_STAFF + "/" + ConstantsSystem.REQUEST_EDIT
		+ "/" + staff.getId();
    }

    /**
     * Create a staff from the origin.
     * 
     * @param staff
     * @param projectID
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_CREATE + "/" + ConstantsSystem.FROM + "/{"
	    + ConstantsSystem.ORIGIN + "}/{" + ConstantsSystem.ORIGIN_ID + "}", method = RequestMethod.POST)
    public String createFromOrigin(@ModelAttribute(ATTR_STAFF) Staff staff,
	    @PathVariable(value = ConstantsSystem.ORIGIN) String origin,
	    @PathVariable(value = ConstantsSystem.ORIGIN_ID) String originID, SessionStatus status,
	    RedirectAttributes redirectAttrs) {
	AlertBoxGenerator alertFactory = AlertBoxGenerator.SUCCESS;
	if (staff.getId() == 0) {
	    this.staffService.createFromOrigin(staff, origin, originID);

	    alertFactory.setMessage("Successfully <b>created</b> staff <b>" + staff.getFullName()
		    + "</b>.");
	} else {
	    alertFactory.setMessage("Successfully <b>updated</b> staff <b>" + staff.getFullName()
		    + "</b>.");

	    this.staffService.update(staff);
	}
	status.setComplete();
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());
	return ConstantsSystem.CONTROLLER_REDIRECT + origin + "/" + ConstantsSystem.REQUEST_EDIT + "/"
		+ originID;
    }

    /**
     * Add an attendance in mass.
     * 
     * @return
     */
    @Deprecated
    @RequestMapping(value = { ConstantsSystem.REQUEST_ADD + "/" + ConstantsRedis.OBJECT_ATTENDANCE + "/"
	    + ConstantsSystem.MASS }, method = RequestMethod.POST)
    public String addAttendanceMass(
	    @ModelAttribute(ATTR_ATTENDANCE_MASS) FormMassAttendance attendanceMass,
	    RedirectAttributes redirectAttrs, HttpSession session, Model model, SessionStatus status) {

	// If start date is > end date.
	Date startDate = attendanceMass.getStartDate();
	Date endDate = attendanceMass.getEndDate();

	if (startDate.after(endDate)) {
	    // TODO
	    redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT,
		    AlertBoxGenerator.FAILED.generateCreate("test", "TODO"));
	    // Dont set completed.
	    // Otherwise, min and max dates will be deleted in session.
	    // status.setComplete();
	    return ConstantsSystem.CONTROLLER_REDIRECT + Staff.OBJECT_NAME + "/"
		    + ConstantsSystem.REQUEST_EDIT + "/" + attendanceMass.getStaff().getId();
	}

	// Do service.
	this.attendanceService.multiSet(attendanceMass);

	// TODO
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT,
		AlertBoxGenerator.SUCCESS.generateCreate("test", "TODO"));

	return editStaffWithMaxDate(model, session, startDate);
    }

    /**
     * Add an attendance.
     * 
     * @return
     */
    @Deprecated
    @RequestMapping(value = { ConstantsSystem.REQUEST_ADD + "/" + ConstantsRedis.OBJECT_ATTENDANCE }, method = RequestMethod.POST)
    public String addAttendance(@ModelAttribute(ATTR_ATTENDANCE) Attendance attendance,
	    RedirectAttributes redirectAttrs, HttpSession session, Model model, SessionStatus status) {

	// Do service.
	this.attendanceService.set(attendance);

	// TODO
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT,
		AlertBoxGenerator.SUCCESS.generateCreate("test", "TODO"));
	return editStaffWithMaxDate(model, session, attendance.getDate());
    }

    /**
     * Delete coming from the staff list page.
     * 
     * @param id
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_DELETE + "/{" + Staff.OBJECT_NAME + "}", method = RequestMethod.GET)
    public String delete(@PathVariable(Staff.OBJECT_NAME) long id, SessionStatus status,
	    RedirectAttributes redirectAttrs) {

	Staff staff = this.staffService.getByID(id);

	AlertBoxGenerator alertFactory = AlertBoxGenerator.SUCCESS;
	alertFactory.setMessage("Successfully <b>deleted</b> staff <b>" + staff.getFullName() + "</b>.");
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());

	this.staffService.delete(id);
	status.setComplete();
	return ConstantsSystem.CONTROLLER_REDIRECT + ATTR_STAFF + "/" + ConstantsSystem.REQUEST_LIST;
    }

    /**
     * Delete coming from the staff edit page.
     * 
     * @param status
     * @param session
     * @param redirectAttrs
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_DELETE, method = RequestMethod.POST)
    public String delete(SessionStatus status, HttpSession session, RedirectAttributes redirectAttrs) {

	Staff staff = (Staff) session.getAttribute(ATTR_STAFF);
	if (staff == null) {
	    AlertBoxGenerator alertFactory = AlertBoxGenerator.FAILED;
	    alertFactory
		    .setMessage("Error occured when you tried to <b>delete</b> staff. Please try again.");
	    redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());
	    status.setComplete();
	    return ConstantsSystem.CONTROLLER_REDIRECT + ATTR_STAFF + "/" + ConstantsSystem.REQUEST_LIST;
	}

	AlertBoxGenerator alertFactory = AlertBoxGenerator.SUCCESS;
	alertFactory.setMessage("Successfully <b>deleted</b> staff <b>" + staff.getFullName() + "</b>.");
	redirectAttrs.addFlashAttribute(ConstantsSystem.UI_PARAM_ALERT, alertFactory.generateHTML());

	this.staffService.delete(staff.getId());
	status.setComplete();
	return ConstantsSystem.CONTROLLER_REDIRECT + ATTR_STAFF + "/" + ConstantsSystem.REQUEST_LIST;
    }

    /**
     * If the Create/Edit Staff request is coming from the project.<br>
     * staff/edit/{id}/from/{origin}/{originid}
     * 
     * @param staffID
     * @param projectID
     * @param model
     * @return
     */
    @Deprecated
    @RequestMapping(value = ConstantsSystem.REQUEST_EDIT + "/{" + Staff.OBJECT_NAME + "}/"
	    + ConstantsSystem.FROM + "/{" + ConstantsSystem.ORIGIN + "}/{" + ConstantsSystem.ORIGIN_ID
	    + "}", method = RequestMethod.GET)
    public String editStaffFromOrigin(HttpSession session,
	    @PathVariable(Staff.OBJECT_NAME) long staffID,
	    @PathVariable(value = ConstantsSystem.ORIGIN) String origin,
	    @PathVariable(value = ConstantsSystem.ORIGIN_ID) long originID, Model model) {
	// Add origin details.
	model.addAttribute(ConstantsSystem.ORIGIN, origin);
	model.addAttribute(ConstantsSystem.ORIGIN_ID, originID);

	// If new, create it.
	if (staffID == 0) {
	    Staff stf = new Staff();
	    model.addAttribute(ATTR_STAFF, stf);
	    return JSP_EDIT;
	}

	Staff staff = this.staffService.getWithAllCollectionsByID(staffID);

	// Else if not new, edit it.
	Map<String, Date> datePair = getCalendarRangeDates(session);
	Date min = datePair.get(ATTR_CALENDAR_MIN_DATE);
	Date max = datePair.get(ATTR_CALENDAR_MAX_DATE);

	setModelAttributes(model, staff, min, max, null);

	return JSP_EDIT;
    }

    /**
     * Open a view page where the user can edit the staff.
     * 
     * @param id
     * @param model
     * @return
     */
    @Deprecated
    @RequestMapping(value = ConstantsSystem.REQUEST_EDIT + "/" + ConstantsSystem.RANGE)
    public String editStaffRangeDates(
	    @ModelAttribute(ATTR_CALENDAR_RANGE_DATES) FormDateRange rangeDates, HttpSession session,
	    Model model) {
	// Get prelim objects.
	Staff staff = (Staff) session.getAttribute(ATTR_STAFF);

	// Get the start and end date from the bean.
	Date min = rangeDates.getStartDate();
	Date max = rangeDates.getEndDate();

	// Given min and max, get range of attendances.
	// Get wage given attendances.
	String maxDateStr = DateUtils.formatDate(max, "yyyy-MM-dd");

	// Add attributes to model.
	setModelAttributes(model, staff, min, max, maxDateStr);
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
	return JSP_EDIT;
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
    private String editStaffWithMaxDate(Model model, HttpSession session, Date minDate) {

	// If the min date from session is lesser
	// than min date passed, use from session.
	Date minDateFromSession = (Date) session.getAttribute(ATTR_CALENDAR_MIN_DATE);
	if (minDateFromSession.before(minDate)) {
	    minDate = minDateFromSession;
	}

	// Get staff object.
	// Get the current year and month.
	// This will be minimum.
	Staff staff = (Staff) session.getAttribute(ATTR_STAFF);
	Date maxDate = (Date) session.getAttribute(ATTR_CALENDAR_MAX_DATE);
	String maxDateStr = (String) session.getAttribute(ATTR_CALENDAR_MAX_DATE_STR);

	// Set model attributes.
	setModelAttributes(model, staff, minDate, maxDate, maxDateStr);
	return JSP_EDIT;
    }

    /**
     * Open a view page where the user can edit the staff.
     * 
     * @param id
     * @param model
     * @return
     */
    private String editStaffWithMinDate(Model model, HttpSession session) {

	// Get staff object.
	// Get the current year and month.
	// This will be minimum.
	Staff staff = (Staff) session.getAttribute(ATTR_STAFF);
	Date minDate = (Date) session.getAttribute(ATTR_CALENDAR_MIN_DATE);
	Date maxDate = (Date) session.getAttribute(ATTR_CALENDAR_MAX_DATE);
	String maxDateStr = (String) session.getAttribute(ATTR_CALENDAR_MAX_DATE_STR);

	// Set model attributes.
	setModelAttributes(model, staff, minDate, maxDate, maxDateStr);
	return JSP_EDIT;
    }

    /**
     * Open a view page where the user can edit the staff.
     * 
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = ConstantsSystem.REQUEST_EDIT + "/{" + Staff.COLUMN_PRIMARY_KEY + "}")
    public String editStaff(@PathVariable(Staff.COLUMN_PRIMARY_KEY) int id, Model model,
	    HttpSession session) {

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
     * Set model attributes before forwarding to JSP.
     * 
     * @param model
     * @param staff
     * @param min
     * @param max
     * @param maxDateStr
     */
    private void setModelAttributes(Model model, Staff staff, Date min, Date max, String maxDateStr) {

	// Given min and max, get range of attendances.
	// Get wage given attendances.
	Set<Attendance> attendanceList = this.attendanceService.rangeStaffAttendance(staff, min, max);

	// Given min and max, get range of attendances.
	// Get wage given attendances.
	model.addAttribute(ATTR_PAYROLL_TOTAL_WAGE,
		this.attendanceService.getTotalWageFromAttendance(attendanceList));

	// Get attendance status map based on enum.
	model.addAttribute(ATTR_CALENDAR_STATUS_LIST, AttendanceStatus.getAllStatusInMap());

	// Get start date of calendar.
	// Add minimum and maximum of data loaded.
	model.addAttribute(ATTR_CALENDAR_MAX_DATE_STR,
		maxDateStr == null ? DateUtils.formatDate(max, "yyyy-MM-dd") : maxDateStr);
	model.addAttribute(ATTR_CALENDAR_MIN_DATE, min);
	model.addAttribute(ATTR_CALENDAR_MAX_DATE, max);
	model.addAttribute(ATTR_TASK_STATUS_MAP, this.staffService.getTaskStatusCountMap(staff));
	model.addAttribute(ATTR_ATTENDANCE_STATUS_MAP,
		this.staffService.getAttendanceStatusCountMap(attendanceList));

	// Add objects.
	// Add form beans.
	Company co = staff.getCompany();
	model.addAttribute(ATTR_ATTENDANCE_LIST, attendanceList);
	model.addAttribute(ATTR_STAFF, staff);
	model.addAttribute(ATTR_CALENDAR_RANGE_DATES, new FormDateRange());
	model.addAttribute(ATTR_ATTENDANCE_MASS, new FormMassAttendance(staff));
	model.addAttribute(ATTR_ATTENDANCE, new Attendance(co, staff));

	// Add front-end JSONs.
	model.addAttribute(ATTR_CALENDAR_JSON, this.staffService.getCalendarJSON(attendanceList));
	model.addAttribute(ATTR_GANTT_JSON, this.staffService.getGanttJSON(staff));
    }

}
