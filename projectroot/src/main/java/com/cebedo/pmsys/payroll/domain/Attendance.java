package com.cebedo.pmsys.payroll.domain;

import java.util.Date;

import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.system.helper.DateHelper;
import com.cebedo.pmsys.system.redis.domain.IDomainObject;
import com.cebedo.pmsys.systemuser.model.SystemUser;

public class Attendance implements IDomainObject {

	public static final String OBJECT_NAME = "attendance";
	public static final String JSP_EDIT = "attendanceEdit";
	public static final String JSP_LIST = "attendanceList";
	private static final long serialVersionUID = 1L;
	private Staff staff;
	private Date timestamp;
	private Status status;
	private int statusID;
	private double wage;

	public Attendance() {
		;
	}

	public Attendance(Staff stf) {
		setStaff(stf);
	}

	public Attendance(Staff stf, Status stat) {
		setStaff(stf);
		setStatus(stat);
		setTimestamp(new Date(System.currentTimeMillis()));
	}

	public Attendance(Staff stf, Date tstamp) {
		setStaff(stf);
		setTimestamp(tstamp);
	}

	public Attendance(Staff stf, Status stat, Date tstamp) {
		setStaff(stf);
		setStatus(stat);
		setTimestamp(tstamp);
	}

	public Attendance(Staff stf, Status stat, Date tstamp, double w) {
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public double getWage() {
		return wage;
	}

	public void setWage(double wage) {
		this.wage = wage;
	}

	public static String constructKey(Staff staff, Date tstamp) {
		SystemUser user = staff.getUser();
		long userID = user == null ? 0 : user.getId();
		long staffID = staff.getId();

		String date = DateHelper.formatDate(tstamp, "yyyy.MM.dd");

		String key = "user:" + userID + ":staff:" + staffID
				+ ":payroll:attendance:date:" + date + ":status:*";
		return key;
	}

	public static String constructKey(Staff staff) {
		SystemUser user = staff.getUser();
		long userID = user == null ? 0 : user.getId();
		long staffID = staff.getId();
		String key = "user:" + userID + ":staff:" + staffID
				+ ":payroll:attendance:date:*:status:*";
		return key;
	}

	public static String constructKey(Staff staff, Date timestamp, Status status) {
		SystemUser user = staff.getUser();
		long userID = user == null ? 0 : user.getId();
		long staffID = staff.getId();

		String date = DateHelper.formatDate(timestamp, "yyyy.MM.dd");

		String key = "user:" + userID + ":staff:" + staffID
				+ ":payroll:attendance:date:" + date + ":status:" + status.id();
		return key;
	}

	/**
	 * Key sample:
	 * user:2123:staff:1123:payroll:attendance:timestamp:12345:status:123<br>
	 * user:2123:staff:1123:payroll:attendance:date:2015.03.15:status:123<br>
	 */
	@Override
	public String getKey() {
		SystemUser user = this.staff.getUser();
		long userID = user == null ? 0 : user.getId();
		long staffID = this.staff.getId();
		Date myDate = getTimestamp();
		String date = DateHelper.formatDate(myDate, "yyyy.MM.dd");

		String key = "user:" + userID + ":staff:" + staffID
				+ ":payroll:attendance:date:" + date + ":status:"
				+ getStatus().id();
		return key;
	}
}