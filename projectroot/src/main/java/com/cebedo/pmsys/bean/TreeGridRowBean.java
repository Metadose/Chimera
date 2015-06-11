package com.cebedo.pmsys.bean;

public class TreeGridRowBean {

    private long primaryKey;
    private long foreignKey;
    private String name;
    private String value;

    /**
     * Attendance breakdown.
     */

    // Overtime.
    private String breakdownOvertime;
    private String breakdownOvertimeCount = "&nbsp;";
    private String breakdownOvertimeWage = "&nbsp;";

    // Present.
    private String breakdownPresent;
    private String breakdownPresentCount = "&nbsp;";
    private String breakdownPresentWage = "&nbsp;";

    // Late.
    private String breakdownLate;
    private String breakdownLateCount = "&nbsp;";
    private String breakdownLateWage = "&nbsp;";

    // Half-day.
    private String breakdownHalfday;
    private String breakdownHalfdayCount = "&nbsp;";
    private String breakdownHalfdayWage = "&nbsp;";

    // Leave.
    private String breakdownLeave;
    private String breakdownLeaveCount = "&nbsp;";
    private String breakdownLeaveWage = "&nbsp;";

    // Absent.
    private String breakdownAbsent;
    private String breakdownAbsentCount = "&nbsp;";
    private String breakdownAbsentWage = "&nbsp;";

    /**
     * Getters and setters.
     */
    public String getBreakdownOvertimeCount() {
	return breakdownOvertimeCount;
    }

    public void setBreakdownOvertimeCount(String breakdownOvertimeCount) {
	this.breakdownOvertimeCount = breakdownOvertimeCount;
    }

    public String getBreakdownOvertimeWage() {
	return breakdownOvertimeWage;
    }

    public void setBreakdownOvertimeWage(String breakdownOvertimeWage) {
	this.breakdownOvertimeWage = breakdownOvertimeWage;
    }

    public String getBreakdownPresentCount() {
	return breakdownPresentCount;
    }

    public void setBreakdownPresentCount(String breakdownPresentCount) {
	this.breakdownPresentCount = breakdownPresentCount;
    }

    public String getBreakdownPresentWage() {
	return breakdownPresentWage;
    }

    public void setBreakdownPresentWage(String breakdownPresentWage) {
	this.breakdownPresentWage = breakdownPresentWage;
    }

    public String getBreakdownLateCount() {
	return breakdownLateCount;
    }

    public void setBreakdownLateCount(String breakdownLateCount) {
	this.breakdownLateCount = breakdownLateCount;
    }

    public String getBreakdownLateWage() {
	return breakdownLateWage;
    }

    public void setBreakdownLateWage(String breakdownLateWage) {
	this.breakdownLateWage = breakdownLateWage;
    }

    public String getBreakdownHalfdayCount() {
	return breakdownHalfdayCount;
    }

    public void setBreakdownHalfdayCount(String breakdownHalfdayCount) {
	this.breakdownHalfdayCount = breakdownHalfdayCount;
    }

    public String getBreakdownHalfdayWage() {
	return breakdownHalfdayWage;
    }

    public void setBreakdownHalfdayWage(String breakdownHalfdayWage) {
	this.breakdownHalfdayWage = breakdownHalfdayWage;
    }

    public String getBreakdownLeaveCount() {
	return breakdownLeaveCount;
    }

    public void setBreakdownLeaveCount(String breakdownLeaveCount) {
	this.breakdownLeaveCount = breakdownLeaveCount;
    }

    public String getBreakdownLeaveWage() {
	return breakdownLeaveWage;
    }

    public void setBreakdownLeaveWage(String breakdownLeaveWage) {
	this.breakdownLeaveWage = breakdownLeaveWage;
    }

    public String getBreakdownAbsentCount() {
	return breakdownAbsentCount;
    }

    public void setBreakdownAbsentCount(String breakdownAbsentCount) {
	this.breakdownAbsentCount = breakdownAbsentCount;
    }

    public String getBreakdownAbsentWage() {
	return breakdownAbsentWage;
    }

    public void setBreakdownAbsentWage(String breakdownAbsentWage) {
	this.breakdownAbsentWage = breakdownAbsentWage;
    }

    public String getBreakdownOvertime() {
	return breakdownOvertime;
    }

    public void setBreakdownOvertime(String breakdownOvertime) {
	this.breakdownOvertime = breakdownOvertime;
    }

    public String getBreakdownPresent() {
	return breakdownPresent;
    }

    public void setBreakdownPresent(String breakdownPresent) {
	this.breakdownPresent = breakdownPresent;
    }

    public String getBreakdownLate() {
	return breakdownLate;
    }

    public void setBreakdownLate(String breakdownLate) {
	this.breakdownLate = breakdownLate;
    }

    public String getBreakdownHalfday() {
	return breakdownHalfday;
    }

    public void setBreakdownHalfday(String breakdownHalfday) {
	this.breakdownHalfday = breakdownHalfday;
    }

    public String getBreakdownLeave() {
	return breakdownLeave;
    }

    public void setBreakdownLeave(String breakdownLeave) {
	this.breakdownLeave = breakdownLeave;
    }

    public String getBreakdownAbsent() {
	return breakdownAbsent;
    }

    public void setBreakdownAbsent(String breakdownAbsent) {
	this.breakdownAbsent = breakdownAbsent;
    }

    public TreeGridRowBean() {
	;
    }

    public TreeGridRowBean(long pKey, long fKey, String n, String v) {
	setPrimaryKey(pKey);
	setForeignKey(fKey);
	setName(n);
	setValue(v);
    }

    public long getPrimaryKey() {
	return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
	this.primaryKey = primaryKey;
    }

    public long getForeignKey() {
	return foreignKey;
    }

    public void setForeignKey(long foreignKey) {
	this.foreignKey = foreignKey;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

}
