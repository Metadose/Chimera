package com.cebedo.pmsys.bean;

public class TreeGridRowBean {

    private long primaryKey;
    private long foreignKey;
    private String name;
    private String value;

    /**
     * Attendance breakdown.
     */

    private String breakdownOvertime = "&nbsp;";
    private String breakdownPresent = "&nbsp;";
    private String breakdownLate = "&nbsp;";
    private String breakdownHalfday = "&nbsp;";
    private String breakdownLeave = "&nbsp;";
    private String breakdownAbsent = "&nbsp;";

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
