package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.Map;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.utils.DateUtils;

public class Attendance implements IDomainObject {

    private static final long serialVersionUID = -724701840751019923L;
    /**
     * Key: company:123:staff:123:attendance:date:2014.12.31:status:123
     */
    private Company company;
    private Staff staff;
    private Date date;
    private AttendanceStatus status;

    /**
     * Specs.
     */
    private double wage;

    /**
     * Extension map.
     */
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
	setDate(new Date(System.currentTimeMillis()));
    }

    public Attendance(Company coID, Staff stf, Date tstamp) {
	setCompany(coID);
	setStaff(stf);
	setDate(tstamp);
    }

    public Attendance(Company coID, Staff stf, AttendanceStatus stat,
	    Date tstamp) {
	setCompany(coID);
	setStaff(stf);
	setStatus(stat);
	setDate(tstamp);
    }

    public Attendance(Company coID, Staff stf, AttendanceStatus stat,
	    Date tstamp, double w) {
	setCompany(coID);
	setStaff(stf);
	setStatus(stat);
	setDate(tstamp);
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
	setDate(timestamp2);
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

    public Date getDate() {
	return date;
    }

    public String getFormattedDateString(String pattern) {
	return DateUtils.formatDate(this.date, pattern);
    }

    public void setDate(Date d) {
	this.date = d;
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

    public static String constructPattern(Staff staff, Date myDate) {
	Company company = staff.getCompany();
	String date = DateUtils.formatDate(myDate, "yyyy.MM.dd");
	return String.format(RedisKeyRegistry.KEY_ATTENDANCE, company.getId(),
		staff.getId(), date, "*");
    }

    public static String constructPattern(Staff staff) {
	Company company = staff.getCompany();
	return String.format(RedisKeyRegistry.KEY_ATTENDANCE, company.getId(),
		staff.getId(), "*", "*");
    }

    public static String constructKey(Staff staff, Date myDate,
	    AttendanceStatus myStatus) {
	Company company = staff.getCompany();
	String date = DateUtils.formatDate(myDate, "yyyy.MM.dd");
	int status = myStatus.id();
	return String.format(RedisKeyRegistry.KEY_ATTENDANCE, company.getId(),
		staff.getId(), date, status);
    }

    /**
     * Key: company:%s:staff:%s:attendance:date:%s:status:%s
     */
    @Override
    public String getKey() {
	Date myDate = getDate();
	String date = DateUtils.formatDate(myDate, "yyyy.MM.dd");
	int status = (getStatus() == null ? getStatusID() : getStatus().id());
	return String.format(RedisKeyRegistry.KEY_ATTENDANCE,
		this.company.getId(), this.staff.getId(), date, status);
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof Attendance ? ((Attendance) obj).getKey().equals(
		getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }
}