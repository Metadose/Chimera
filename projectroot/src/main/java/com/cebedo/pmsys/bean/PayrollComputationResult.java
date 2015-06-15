package com.cebedo.pmsys.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.utils.NumberFormatUtils;
import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

public class PayrollComputationResult implements Serializable {

    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("PayrollComputationResult");
    private Date startDate, endDate;
    private Map<Staff, String> staffToWageMap = new HashMap<Staff, String>();
    private double overallTotalOfStaff = 0;
    private Map<Staff, Map<AttendanceStatus, Map<String, Double>>> staffPayrollBreakdownMap = new HashMap<Staff, Map<AttendanceStatus, Map<String, Double>>>();
    private List<TreeGridRowBean> treeGrid = new ArrayList<TreeGridRowBean>();

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public void setStaffToWageMap(Map<Staff, String> staffToWageMap) {
	this.staffToWageMap = staffToWageMap;
    }

    public void setOverallTotalOfStaff(double overallTotalOfStaff) {
	this.overallTotalOfStaff = overallTotalOfStaff;
    }

    public void setStaffPayrollBreakdownMap(
	    Map<Staff, Map<AttendanceStatus, Map<String, Double>>> staffPayrollBreakdownMap) {
	this.staffPayrollBreakdownMap = staffPayrollBreakdownMap;
    }

    public void setTreeGrid(List<TreeGridRowBean> treeGrid) {
	this.treeGrid = treeGrid;
    }

    public Date getStartDate() {
	return startDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public Map<Staff, String> getStaffToWageMap() {
	return staffToWageMap;
    }

    public double getOverallTotalOfStaff() {
	return overallTotalOfStaff;
    }

    public String getOverallTotalOfStaffAsString() {
	return NumberFormatUtils.getCurrencyFormatter().format(
		overallTotalOfStaff);
    }

    public Map<Staff, Map<AttendanceStatus, Map<String, Double>>> getStaffPayrollBreakdownMap() {
	return staffPayrollBreakdownMap;
    }

    public List<TreeGridRowBean> getTreeGrid() {
	return treeGrid;
    }

}
