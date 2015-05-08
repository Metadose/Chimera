package com.cebedo.pmsys.payroll.domain;

import java.util.Date;

import com.cebedo.pmsys.staff.model.Staff;
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
		String key = "user:" + userID + ":staff:" + staffID
				+ ":payroll:attendance:timestamp:" + tstamp.getTime()
				+ ":status:*";
		return key;
	}

	public static String constructKey(Staff staff) {
		SystemUser user = staff.getUser();
		long userID = user == null ? 0 : user.getId();
		long staffID = staff.getId();
		String key = "user:" + userID + ":staff:" + staffID
				+ ":payroll:attendance:timestamp:*:status:*";
		return key;
	}

	public static String constructKey(Staff staff, Date timestamp, Status status) {
		SystemUser user = staff.getUser();
		long userID = user == null ? 0 : user.getId();
		long staffID = staff.getId();
		String key = "user:" + userID + ":staff:" + staffID
				+ ":payroll:attendance:timestamp:" + timestamp.getTime()
				+ ":status:" + status.id();
		return key;
	}

	/**
	 * Key sample:
	 * user:2123:staff:1123:payroll:attendance:timestamp:12345:status:123
	 */
	@Override
	public String getKey() {
		SystemUser user = this.staff.getUser();
		long userID = user == null ? 0 : user.getId();
		long staffID = this.staff.getId();
		String key = "user:" + userID + ":staff:" + staffID
				+ ":payroll:attendance:timestamp:" + getTimestamp().getTime()
				+ ":status:" + getStatus().id();
		return key;
	}
}