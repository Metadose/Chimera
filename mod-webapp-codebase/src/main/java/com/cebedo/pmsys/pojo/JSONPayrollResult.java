package com.cebedo.pmsys.pojo;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class JSONPayrollResult implements Serializable {

    private static final long serialVersionUID = 3657789273539338468L;
    private UUID uuid;
    private String name;
    private double value;
    private double wage = 0;

    /**
     * Attendance breakdown.
     */

    // Overtime.
    private double breakdownOvertimeCount = 0;
    private double breakdownOvertimeWage = 0;

    // Present.
    private double breakdownPresentCount = 0;
    private double breakdownPresentWage = 0;

    // Late.
    private double breakdownLateCount = 0;
    private double breakdownLateWage = 0;

    // Half-day.
    private double breakdownHalfdayCount = 0;
    private double breakdownHalfdayWage = 0;

    // Leave.
    private double breakdownLeaveCount = 0;
    private double breakdownLeaveWage = 0;

    // Absent.
    private double breakdownAbsentCount = 0;
    private double breakdownAbsentWage = 0;

    /**
     * Getters and setters.
     */
    public double getBreakdownOvertimeCount() {
	return breakdownOvertimeCount;
    }

    public void setBreakdownOvertimeCount(double breakdownOvertimeCount) {
	this.breakdownOvertimeCount = breakdownOvertimeCount;
    }

    public double getBreakdownOvertimeWage() {
	return breakdownOvertimeWage;
    }

    public void setBreakdownOvertimeWage(double breakdownOvertimeWage) {
	this.breakdownOvertimeWage = breakdownOvertimeWage;
    }

    public double getBreakdownPresentCount() {
	return breakdownPresentCount;
    }

    public void setBreakdownPresentCount(double breakdownPresentCount) {
	this.breakdownPresentCount = breakdownPresentCount;
    }

    public double getBreakdownPresentWage() {
	return breakdownPresentWage;
    }

    public void setBreakdownPresentWage(double breakdownPresentWage) {
	this.breakdownPresentWage = breakdownPresentWage;
    }

    public double getBreakdownLateCount() {
	return breakdownLateCount;
    }

    public void setBreakdownLateCount(double breakdownLateCount) {
	this.breakdownLateCount = breakdownLateCount;
    }

    public double getBreakdownLateWage() {
	return breakdownLateWage;
    }

    public void setBreakdownLateWage(double breakdownLateWage) {
	this.breakdownLateWage = breakdownLateWage;
    }

    public double getBreakdownHalfdayCount() {
	return breakdownHalfdayCount;
    }

    public void setBreakdownHalfdayCount(double breakdownHalfdayCount) {
	this.breakdownHalfdayCount = breakdownHalfdayCount;
    }

    public double getBreakdownHalfdayWage() {
	return breakdownHalfdayWage;
    }

    public void setBreakdownHalfdayWage(double breakdownHalfdayWage) {
	this.breakdownHalfdayWage = breakdownHalfdayWage;
    }

    public double getBreakdownLeaveCount() {
	return breakdownLeaveCount;
    }

    public void setBreakdownLeaveCount(double breakdownLeaveCount) {
	this.breakdownLeaveCount = breakdownLeaveCount;
    }

    public double getBreakdownLeaveWage() {
	return breakdownLeaveWage;
    }

    public void setBreakdownLeaveWage(double breakdownLeaveWage) {
	this.breakdownLeaveWage = breakdownLeaveWage;
    }

    public double getBreakdownAbsentCount() {
	return breakdownAbsentCount;
    }

    public void setBreakdownAbsentCount(double breakdownAbsentCount) {
	this.breakdownAbsentCount = breakdownAbsentCount;
    }

    public double getBreakdownAbsentWage() {
	return breakdownAbsentWage;
    }

    public void setBreakdownAbsentWage(double breakdownAbsentWage) {
	this.breakdownAbsentWage = breakdownAbsentWage;
    }

    public JSONPayrollResult() {
	;
    }

    public JSONPayrollResult(String n, double v) {
	setUuid(UUID.randomUUID());
	setName(n);
	setValue(v);
    }

    public JSONPayrollResult(String n, double v, double w) {
	setUuid(UUID.randomUUID());
	setName(n);
	setValue(v);
	setWage(w);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = StringUtils.trim(name);
    }

    public double getValue() {
	return value;
    }

    public void setValue(double value) {
	this.value = value;
    }

    public double getWage() {
	return wage;
    }

    public void setWage(double wage) {
	this.wage = wage;
    }

    public UUID getUuid() {
	return uuid;
    }

    public void setUuid(UUID uuid) {
	this.uuid = uuid;
    }

}
