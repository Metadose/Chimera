package com.cebedo.pmsys.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.pojo.JSONPayrollResult;
import com.cebedo.pmsys.utils.NumberFormatUtils;

public class PayrollResultComputation implements Serializable {

    private static final long serialVersionUID = 6109887323126902978L;
    private Date startDate, endDate;
    private Map<Staff, Double> staffToWageMap = new HashMap<Staff, Double>();
    private double overallTotalOfStaff = 0;
    private Map<Staff, Map<AttendanceStatus, PairCountValue>> staffPayrollBreakdownMap = new HashMap<Staff, Map<AttendanceStatus, PairCountValue>>();
    private List<JSONPayrollResult> treeGrid = new ArrayList<JSONPayrollResult>();

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public void setStaffToWageMap(Map<Staff, Double> staffToWageMap) {
	this.staffToWageMap = staffToWageMap;
    }

    public void setOverallTotalOfStaff(double overallTotalOfStaff) {
	this.overallTotalOfStaff = overallTotalOfStaff;
    }

    public void setStaffPayrollBreakdownMap(
	    Map<Staff, Map<AttendanceStatus, PairCountValue>> staffPayrollBreakdownMap) {
	this.staffPayrollBreakdownMap = staffPayrollBreakdownMap;
    }

    public void setTreeGrid(List<JSONPayrollResult> treeGrid) {
	this.treeGrid = treeGrid;
    }

    public Date getStartDate() {
	return startDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public Map<Staff, Double> getStaffToWageMap() {
	return staffToWageMap;
    }

    public double getOverallTotalOfStaff() {
	return overallTotalOfStaff;
    }

    public String getOverallTotalOfStaffAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(overallTotalOfStaff);
    }

    public Map<Staff, Map<AttendanceStatus, PairCountValue>> getStaffPayrollBreakdownMap() {
	return staffPayrollBreakdownMap;
    }

    public List<JSONPayrollResult> getTreeGrid() {
	return treeGrid;
    }

}
