package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.Map;

import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.utils.DateUtils;

public class Attendance implements IDomainObject {

    private static final long serialVersionUID = 1L;
    private Company company;
    private Staff staff;
    private Date timestamp;
    private AttendanceStatus status;
    private double wage;
    private Map<String, Object> extMap;

    /**
     * Bean-backed form.
     */
    private int statusID;

    public int getStatusID() {
	return statusID;
    }

    public void setStatusID(int statusID) {
	this.statusID = statusID;
    }

    public Attendance() {
	;
    }

    public Attendance(Company coID, Staff stf) {
	setCompany(coID);
	setStaff(stf);
    }

    public Attendance(Company coID, Staff stf, AttendanceStatus stat) {
	setCompany(coID);
	setStaff(stf);
	setStatus(stat);
	setTimestamp(new Date(System.currentTimeMillis()));
    }

    public Attendance(Company coID, Staff stf, Date tstamp) {
	setCompany(coID);
	setStaff(stf);
	setTimestamp(tstamp);
    }

    public Attendance(Company coID, Staff stf, AttendanceStatus stat,
	    Date tstamp) {
	setCompany(coID);
	setStaff(stf);
	setStatus(stat);
	setTimestamp(tstamp);
    }

    public Attendance(Company coID, Staff stf, AttendanceStatus stat,
	    Date tstamp, double w) {
	setCompany(coID);
	setStaff(stf);
	setStatus(stat);
	setTimestamp(tstamp);
	setWage(w);
    }

    public Attendance(Staff staff, AttendanceStatus status) {
	setCompany(staff.getCompany());
	setStaff(staff);
	setStatus(status);
    }

    public Attendance(Staff staff, AttendanceStatus status, Date timestamp2) {
	setCompany(staff.getCompany());
	setStaff(staff);
	setStatus(status);
	setTimestamp(timestamp2);
    }

    public Staff getStaff() {
	return staff;
    }

    public void setStaff(Staff staff) {
	this.staff = staff;
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company companyID) {
	this.company = companyID;
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

	String companyPart = Company.OBJECT_NAME + ":" + this.company.getId()
		+ ":";

	String key = companyPart + "staff:" + this.staff.getId()
		+ ":payroll:attendance:date:" + date + ":status:"
		+ getStatus().id();

	return key;
    }
}