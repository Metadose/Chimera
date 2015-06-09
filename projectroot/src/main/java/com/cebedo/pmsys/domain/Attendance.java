package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.Map;

import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.utils.DateUtils;

public class Attendance implements IDomainObject {

    private static final long serialVersionUID = 1L;
    private Long companyID;
    private long staffID;
    private Date timestamp;
    private int statusID;
    private double wage;
    private Map<String, Object> extMap;

    public Attendance() {
	;
    }

    public Attendance(Long coID, long stf) {
	setCompanyID(coID);
	setStaffID(stf);
    }

    public Attendance(Long coID, long stf, int stat) {
	setCompanyID(coID);
	setStaffID(stf);
	setStatusID(stat);
	setTimestamp(new Date(System.currentTimeMillis()));
    }

    public Attendance(Long coID, long stf, Date tstamp) {
	setCompanyID(coID);
	setStaffID(stf);
	setTimestamp(tstamp);
    }

    public Attendance(Long coID, long stf, int stat, Date tstamp) {
	setCompanyID(coID);
	setStaffID(stf);
	setStatusID(stat);
	setTimestamp(tstamp);
    }

    public Attendance(Long coID, long stf, int stat, Date tstamp, double w) {
	setCompanyID(coID);
	setStaffID(stf);
	setStatusID(stat);
	setTimestamp(tstamp);
	setWage(w);
    }

    public long getStaffID() {
	return staffID;
    }

    public void setStaffID(long staff) {
	this.staffID = staff;
    }

    public Long getCompanyID() {
	return companyID;
    }

    public void setCompanyID(Long companyID) {
	this.companyID = companyID;
    }

    public Date getTimestamp() {
	return timestamp;
    }

    public String getFormattedDateString(String pattern) {
	return DateUtils.formatDate(this.timestamp, pattern);
    }

    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
    }

    public int getStatusID() {
	return statusID;
    }

    public void setStatusID(int status) {
	this.statusID = status;
    }

    public AttendanceStatus getStatus() {
	return AttendanceStatus.of(getStatusID());
    }

    public double getWage() {
	return wage;
    }

    public void setWage(double wage) {
	this.wage = wage;
    }

    @Override
    public Map<String, Object> getExtMap() {
	return extMap;
    }

    @Override
    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    public static String constructKey(Staff staff, Date tstamp) {
	// Construct key.
	Company co = staff.getCompany();
	Long companyID = co == null ? 0 : co.getId();
	String companyPart = Company.OBJECT_NAME + ":" + companyID + ":";

	long staffID = staff.getId();
	String date = DateUtils.formatDate(tstamp, "yyyy.MM.dd");
	String key = companyPart + "staff:" + staffID
		+ ":payroll:attendance:date:" + date + ":status:*";
	return key;
    }

    public static String constructKey(Staff staff) {
	// Construct key.
	Company co = staff.getCompany();
	Long companyID = co == null ? 0 : co.getId();
	String companyPart = Company.OBJECT_NAME + ":" + companyID + ":";

	long staffID = staff.getId();
	String key = companyPart + "staff:" + staffID
		+ ":payroll:attendance:date:*:status:*";
	return key;
    }

    public static String constructKey(Staff staff, Date timestamp,
	    AttendanceStatus status) {

	long staffID = staff.getId();
	String date = DateUtils.formatDate(timestamp, "yyyy.MM.dd");

	// Construct key.
	Company co = staff.getCompany();
	Long companyID = co == null ? 0 : co.getId();
	String companyPart = Company.OBJECT_NAME + ":" + companyID + ":";
	String key = companyPart + "staff:" + staffID
		+ ":payroll:attendance:date:" + date + ":status:" + status.id();

	return key;
    }

    /**
     * Key sample:
     * company:2123:staff:1123:payroll:attendance:timestamp:12345:status:123<br>
     * company:2123:staff:1123:payroll:attendance:date:2015.03.15:status:123<br>
     */
    @Override
    public String getKey() {

	Date myDate = getTimestamp();
	String date = DateUtils.formatDate(myDate, "yyyy.MM.dd");

	String companyPart = Company.OBJECT_NAME + ":" + this.companyID + ":";
	String key = companyPart + "staff:" + this.staffID
		+ ":payroll:attendance:date:" + date + ":status:"
		+ getStatusID();

	return key;
    }
}