package com.cebedo.pmsys.domain;

import java.util.Date;

import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.helper.DateHelper;
import com.cebedo.pmsys.model.Staff;

public class Attendance implements IDomainObject {

    public static final String OBJECT_NAME = "attendance";
    public static final String JSP_EDIT = "attendanceEdit";
    public static final String JSP_LIST = "attendanceList";
    private static final long serialVersionUID = 1L;
    private Staff staff;
    private Date timestamp;
    private AttendanceStatus status;
    private int statusID;
    private double wage;

    public Attendance() {
	;
    }

    public Attendance(Staff stf) {
	setStaff(stf);
    }

    public Attendance(Staff stf, AttendanceStatus stat) {
	setStaff(stf);
	setStatus(stat);
	setTimestamp(new Date(System.currentTimeMillis()));
    }

    public Attendance(Staff stf, Date tstamp) {
	setStaff(stf);
	setTimestamp(tstamp);
    }

    public Attendance(Staff stf, AttendanceStatus stat, Date tstamp) {
	setStaff(stf);
	setStatus(stat);
	setTimestamp(tstamp);
    }

    public Attendance(Staff stf, AttendanceStatus stat, Date tstamp, double w) {
	setStaff(stf);
	setStatus(stat);
	setTimestamp(tstamp);
	setWage(w);
    }

    public int getStatusID() {
	return statusID;
    }

    public void setStatusID(int statusID) {
	this.statusID = statusID;
    }

    public Staff getStaff() {
	return staff;
    }

    public void setStaff(Staff staff) {
	this.staff = staff;
    }

    public Date getTimestamp() {
	return timestamp;
    }

    public String getFormattedDateString(String pattern) {
	return DateHelper.formatDate(this.timestamp, pattern);
    }

    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
    }

    public AttendanceStatus getStatus() {
	return status;
    }

    public void setStatus(AttendanceStatus status) {
	this.status = status;
    }

    public double getWage() {
	return wage;
    }

    public void setWage(double wage) {
	this.wage = wage;
    }

    public static String constructKey(Staff staff, Date tstamp) {
	long staffID = staff.getId();
	String date = DateHelper.formatDate(tstamp, "yyyy.MM.dd");
	String key = "staff:" + staffID + ":payroll:attendance:date:" + date
		+ ":status:*";
	return key;
    }

    public static String constructKey(Staff staff) {
	long staffID = staff.getId();
	String key = "staff:" + staffID + ":payroll:attendance:date:*:status:*";
	return key;
    }

    public static String constructKey(Staff staff, Date timestamp,
	    AttendanceStatus status) {
	long staffID = staff.getId();

	String date = DateHelper.formatDate(timestamp, "yyyy.MM.dd");

	String key = "staff:" + staffID + ":payroll:attendance:date:" + date
		+ ":status:" + status.id();
	return key;
    }

    /**
     * Key sample:
     * user:2123:staff:1123:payroll:attendance:timestamp:12345:status:123<br>
     * user:2123:staff:1123:payroll:attendance:date:2015.03.15:status:123<br>
     */
    @Override
    public String getKey() {
	long staffID = this.staff.getId();
	Date myDate = getTimestamp();
	String date = DateHelper.formatDate(myDate, "yyyy.MM.dd");

	String key = "staff:" + staffID + ":payroll:attendance:date:" + date
		+ ":status:" + getStatus().id();
	return key;
    }
}