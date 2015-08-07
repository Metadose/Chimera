package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.MassAttendanceBean;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.repository.AttendanceValueRepo;
import com.cebedo.pmsys.service.AttendanceService;
import com.cebedo.pmsys.utils.DateUtils;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();

    private AttendanceValueRepo attendanceValueRepo;

    public void setAttendanceValueRepo(AttendanceValueRepo r) {
	this.attendanceValueRepo = r;
    }

    @Override
    @Transactional
    public void set(Attendance attendance) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(attendance)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_ATTENDANCE, attendance.getKey());
	    return; // TODO Put notification.
	}

	// Log.
	this.messageHelper.send(AuditAction.SET, RedisConstants.OBJECT_ATTENDANCE, attendance.getKey());

	// Set the status.
	if (attendance.getStatus() == null) {
	    attendance.setStatus(AttendanceStatus.of(attendance.getStatusID()));
	}
	AttendanceStatus status = attendance.getStatus();

	// If status is delete.
	if (status == AttendanceStatus.DELETE) {
	    deleteAllInDate(attendance);
	    return;
	}

	// If status is absent.
	if (status == AttendanceStatus.ABSENT && attendance.getWage() > 0) {
	    attendance.setWage(0);
	}
	if (status != AttendanceStatus.ABSENT && attendance.getWage() == 0) {
	    Staff staff = attendance.getStaff();
	    attendance.setWage(getWage(staff, status));
	}

	// Delete all previously declared attendance in this date.
	deleteAllInDate(attendance);
	this.attendanceValueRepo.set(attendance);
    }

    /**
     * Delete all previously declared attendance in this date.
     * 
     * @param attendance
     */
    private void deleteAllInDate(Attendance attendance) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(attendance)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_ATTENDANCE, attendance.getKey());
	    return;
	}

	Staff staff = attendance.getStaff();
	String key = Attendance.constructPattern(staff, attendance.getDate());
	Set<String> keys = this.attendanceValueRepo.keys(key);
	this.attendanceValueRepo.delete(keys);

	// Log.
	this.messageHelper.send(AuditAction.DELETE, RedisConstants.OBJECT_ATTENDANCE,
		attendance.getKey());
    }

    @Override
    @Transactional
    public void set(Staff staff, AttendanceStatus status) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(staff)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
	    return; // TODO Put notification.
	}

	Attendance attendance = new Attendance(staff, status);
	double wage = getWage(staff, status);
	attendance.setWage(wage);
	this.attendanceValueRepo.set(attendance);

	// Log.
	this.messageHelper.send(AuditAction.SET, Staff.OBJECT_NAME, staff.getId(),
		RedisConstants.OBJECT_ATTENDANCE, attendance.getKey());
    }

    @Override
    @Transactional
    public void set(Staff staff, AttendanceStatus status, Date timestamp) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(staff)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
	    return; // TODO Put notification.
	}

	Attendance attendance = new Attendance(staff, status, timestamp);
	double wage = getWage(staff, status);
	attendance.setWage(wage);
	this.attendanceValueRepo.set(attendance);

	// Log.
	this.messageHelper.send(AuditAction.SET, RedisConstants.OBJECT_ATTENDANCE, attendance.getKey());
    }

    /**
     * Get corresponding wage of a staff member.
     * 
     * @param staff
     * @param status
     * @return
     */
    private double getWage(Staff staff, AttendanceStatus status) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(staff)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
	    return 0.0;
	}

	// Log.
	this.messageHelper.send(AuditAction.GET, Staff.OBJECT_NAME, staff.getId(), Staff.PROPERTY_WAGE);

	double wage = 0;

	if (status == AttendanceStatus.PRESENT) {
	    wage = staff.getWage();

	} else if (status == AttendanceStatus.LEAVE) {
	    wage = staff.getWage();

	} else if (status == AttendanceStatus.LATE) {
	    wage = staff.getWage();

	} else if (status == AttendanceStatus.ABSENT) {
	    wage = 0;

	} else if (status == AttendanceStatus.HALFDAY) {
	    wage = staff.getWage() / 2;
	}
	return wage;
    }

    /**
     * Get total wage of all attendances.
     */
    @Transactional
    @Override
    public double getTotalWageFromAttendance(Collection<Attendance> attendances) {

	double totalWage = 0;
	Staff staff = null;

	for (Attendance attd : attendances) {

	    // Security check.
	    if (!this.authHelper.isActionAuthorized(attd)) {
		this.messageHelper.unauthorized(RedisConstants.OBJECT_ATTENDANCE, attd.getKey());
		return 0.0;
	    }

	    totalWage += attd.getWage();
	    staff = staff == null ? attd.getStaff() : staff;
	}

	// Log.
	this.messageHelper.send(AuditAction.GET, Staff.OBJECT_NAME, staff.getId(), Staff.PROPERTY_WAGE);

	return totalWage;
    }

    /**
     * Get total wage of staff given a min and max date.
     */
    @Transactional
    @Override
    public double getTotalWageOfStaffInRange(Staff staff, Date min, Date max) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(staff)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
	    return 0.0;
	}

	// Log.
	this.messageHelper.send(AuditAction.GET, Staff.OBJECT_NAME, staff.getId(), Staff.PROPERTY_WAGE);

	// Get all the attendances.
	Set<Attendance> attendances = this.rangeStaffAttendance(staff, min, max);

	// Get total wage of all attendances.
	return getTotalWageFromAttendance(attendances);
    }

    /**
     * Get attendances given a range of min and max.
     */
    @Override
    @Transactional
    public Set<Attendance> rangeStaffAttendance(Staff staff, long min, long max) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(staff)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
	    return new HashSet<Attendance>();
	}

	// Log.
	this.messageHelper.send(AuditAction.RANGE, Staff.OBJECT_NAME, staff.getId(),
		RedisConstants.OBJECT_ATTENDANCE);

	Set<String> keys = this.attendanceValueRepo.keys(Attendance.constructPattern(staff));
	Set<Attendance> attnSet = new HashSet<Attendance>();
	for (String key : keys) {
	    Attendance attn = this.attendanceValueRepo.get(key);

	    // TODO Optimize this.
	    // Leverage on the Date object. Don't use Timestamp millis.
	    long timestamp = attn.getDate().getTime();
	    if (timestamp <= max && timestamp >= min) {
		attnSet.add(attn);
	    }
	}
	return attnSet;
    }

    @Override
    @Transactional
    public Set<Attendance> rangeStaffAttendance(Staff staff, Date min, Date max) {
	return this.rangeStaffAttendance(staff, min.getTime(), max.getTime());
    }

    @Override
    @Transactional
    public Attendance get(Staff staff, AttendanceStatus status, Date timestamp) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(staff)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
	    return new Attendance();
	}

	Attendance attn = this.attendanceValueRepo
		.get(Attendance.constructKey(staff, timestamp, status));

	// Log.
	this.messageHelper.send(AuditAction.GET, Staff.OBJECT_NAME, staff.getId(),
		RedisConstants.OBJECT_ATTENDANCE, attn.getKey());

	return attn;
    }

    @Override
    @Transactional
    public void multiSet(MassAttendanceBean attendanceMass) {
	Staff staff = attendanceMass.getStaff();

	// Security check.
	if (!this.authHelper.isActionAuthorized(staff)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
	    return; // TODO Notify?
	}

	// Log.
	this.messageHelper.send(AuditAction.SET_MULTI, Staff.OBJECT_NAME, staff.getId(),
		MassAttendanceBean.class.getName());

	// Get the wage.
	AttendanceStatus status = AttendanceStatus.of(attendanceMass.getStatusID());
	if (status == AttendanceStatus.ABSENT) {
	    attendanceMass.setWage(0);
	}
	double wage = attendanceMass.getWage();

	// Get the dates.
	boolean includeWeekends = attendanceMass.isIncludeWeekends();
	Date startDate = attendanceMass.getStartDate();
	Date endDate = attendanceMass.getEndDate();
	List<Date> dates = DateUtils.getDatesBetweenDates(startDate, endDate);
	Map<String, Attendance> keyAttendanceMap = new HashMap<String, Attendance>();

	// Iterate through all dates.
	for (Date date : dates) {
	    Company co = staff.getCompany();
	    Attendance attendance = new Attendance(co, staff, status, date, wage);

	    // Check if date is a weekend.
	    int dayOfWeek = DateUtils.getDayOfWeek(date);
	    boolean isWeekend = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;

	    // If status is delete.
	    if (status == AttendanceStatus.DELETE) {
		if (!includeWeekends && isWeekend) {
		    continue;
		}
		deleteAllInDate(attendance);
		continue;
	    }

	    deleteAllInDate(attendance);
	    if (!includeWeekends && isWeekend) {
		continue;
	    }
	    keyAttendanceMap.put(attendance.getKey(), attendance);
	}
	if (status != AttendanceStatus.DELETE) {
	    this.attendanceValueRepo.multiSet(keyAttendanceMap);
	}
    }

    @Override
    @Transactional
    public List<Attendance> getAllAttendance(Staff staff) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(staff)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
	    return new ArrayList<Attendance>();
	}

	// Log.
	this.messageHelper.send(AuditAction.LIST, Staff.OBJECT_NAME, staff.getId(),
		RedisConstants.OBJECT_ATTENDANCE);

	Set<String> keys = this.attendanceValueRepo.keys(Attendance.constructPattern(staff));
	return this.attendanceValueRepo.multiGet(keys);
    }

}
