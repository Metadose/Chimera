package com.cebedo.pmsys.service.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.PayrollResultComputation;
import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.CSSClass;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.pojo.JSONPayrollResult;
import com.cebedo.pmsys.service.AttendanceService;
import com.cebedo.pmsys.service.ProjectPayrollComputerService;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.utils.DataStructUtils;
import com.cebedo.pmsys.utils.NumberFormatUtils;
import com.google.gson.Gson;

@Service
public class ProjectPayrollComputerServiceImpl implements ProjectPayrollComputerService {

    private static final String IDENTIFIER_ALREADY_EXISTS = "Check ";

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private AttendanceService attendanceService;
    private StaffService staffService;

    public void setStaffService(StaffService staffService) {
	this.staffService = staffService;
    }

    public void setAttendanceService(AttendanceService s) {
	this.attendanceService = s;
    }

    public ProjectPayrollComputerServiceImpl() {
	;
    }

    // In this "proj", we are computing "staffIDsToCompute",
    // in range "startDate", "endDate".
    private ProjectPayroll projectPayroll;
    private List<Long> staffIDsToCompute;
    private Date startDate, endDate;

    // Already computed? Add ID to map "computedMap".
    private Map<Long, String> alreadyComputedMap = new HashMap<Long, String>();

    // Map of staff and corresponding wage.
    private Map<Staff, String> staffToWageMap = new HashMap<Staff, String>();

    // Total wage for group "managersTotal".
    private double overallTotalOfStaff = 0;

    // "allStaffWageBreakdown" Attendance status with corresponding count map.
    // This is the breakdown of total for each staff.
    private Map<Staff, Map<AttendanceStatus, Map<String, Double>>> staffPayrollBreakdownMap = new HashMap<Staff, Map<AttendanceStatus, Map<String, Double>>>();

    // JSON tree grid.
    private List<JSONPayrollResult> treeGrid = new ArrayList<JSONPayrollResult>();

    // Currency formatter.
    private NumberFormat formatter = NumberFormatUtils.getCurrencyFormatter();

    // Results.
    private double overallTotalOfWage = 0;
    private PayrollResultComputation payrollResult = new PayrollResultComputation();
    private Map<AttendanceStatus, Integer> overallBreakdownCountMap = new HashMap<AttendanceStatus, Integer>();
    private Map<AttendanceStatus, Double> overallBreakdownWageMap = new HashMap<AttendanceStatus, Double>();

    /**
     * Clear old data.
     */
    private void clear() {
	this.staffIDsToCompute = null;
	this.startDate = null;
	this.endDate = null;

	this.alreadyComputedMap = new HashMap<Long, String>();
	this.staffToWageMap = new HashMap<Staff, String>();
	this.overallTotalOfStaff = 0;

	this.staffPayrollBreakdownMap = new HashMap<Staff, Map<AttendanceStatus, Map<String, Double>>>();
	this.treeGrid = new ArrayList<JSONPayrollResult>();

	this.overallTotalOfWage = 0;
	this.payrollResult = new PayrollResultComputation();
	this.overallBreakdownCountMap = new HashMap<AttendanceStatus, Integer>();
	this.overallBreakdownWageMap = new HashMap<AttendanceStatus, Double>();
    }

    /**
     * Put the staff's wage breakdown.
     * 
     * @param staff
     */
    private void putStaffBreakdown(Staff staff) {
	Map<AttendanceStatus, Map<String, Double>> attendanceStatusCountMap = getStaffBreakdownMap(
		staff, this.startDate, this.endDate);
	this.staffPayrollBreakdownMap.put(staff, attendanceStatusCountMap);
    }

    /**
     * Compute staff.
     */
    private void compute() {

	// this.staffIDsToCompute

	for (Staff staff : this.projectPayroll.getAssignedStaffList()) {

	    // Get the staff,
	    // check if already computed.
	    long staffID = staff.getId();
	    if (!this.staffIDsToCompute.contains(staffID)) {
		continue;
	    }

	    // If a staff has already been computed before,
	    // don't compute again.
	    if (this.alreadyComputedMap.containsKey(staffID)) {
		this.staffToWageMap.put(staff,
			IDENTIFIER_ALREADY_EXISTS + this.alreadyComputedMap.get(staffID));
		continue;
	    }

	    // Get wage then add to map.
	    // Get the total of this guy.
	    double staffWageTotal = this.attendanceService.getTotalWageOfStaffInRange(staff,
		    this.startDate, this.endDate);

	    // Add it to the overall total of managers.
	    this.overallTotalOfStaff += staffWageTotal;

	    // Add the value to the map.
	    // And to the "already computed" map.
	    this.staffToWageMap.put(staff, String.valueOf(staffWageTotal));
	    this.alreadyComputedMap.put(staffID, "Staff List");

	    // Get the breakdown of this total.
	    // Add the breakdown to the map.
	    putStaffBreakdown(staff);
	}
    }

    /**
     * Get the breakdown of the total wage.
     * 
     * @param manager
     * @param min
     * @param max
     * @return
     */
    private Map<AttendanceStatus, Map<String, Double>> getStaffBreakdownMap(Staff manager, Date min,
	    Date max) {
	// Attendance count map.
	Set<Attendance> attendanceList = this.attendanceService.rangeStaffAttendance(manager, min, max);
	Map<AttendanceStatus, Map<String, Double>> attendanceStatusCountMap = this.staffService
		.getAttendanceStatusCountMap(attendanceList);
	return attendanceStatusCountMap;
    }

    /**
     * Construct the JSON equivalent of the result computation.
     * 
     * @return
     */
    @Transactional
    @Override
    public String getPayrollJSONResult() {

	// Staff
	constructTreeGridStaff();

	return new Gson().toJson(this.treeGrid, ArrayList.class);
    }

    /**
     * Get the count breakdown.
     * 
     * @param staffWageBreakdown
     * @param overtime
     * @return
     */
    private String getBreakdownCount(Map<AttendanceStatus, Map<String, Double>> staffWageBreakdown,
	    AttendanceStatus status) {

	// Get the count, wage and format.
	Map<String, Double> countAndWage = staffWageBreakdown.get(status);
	double count = countAndWage.get(StaffServiceImpl.STAFF_ATTENDANCE_STATUS_COUNT);

	// Get the value.
	// Get the old value.
	// Add new value to old.
	int intValueOfCount = (int) count;
	Integer oldValue = this.overallBreakdownCountMap.get(status) == null ? 0
		: this.overallBreakdownCountMap.get(status);
	this.overallBreakdownCountMap.put(status, oldValue + intValueOfCount);

	return intValueOfCount == 0 ? "-" : "(" + intValueOfCount + ")";
    }

    /**
     * Get the wage breakdown.
     * 
     * @param countAndWage
     * @return
     */
    private String getBreakdownWage(Map<AttendanceStatus, Map<String, Double>> staffWageBreakdown,
	    AttendanceStatus status) {

	// Get the count, wage and format.
	Map<String, Double> countAndWage = staffWageBreakdown.get(status);
	Double wage = countAndWage.get(StaffServiceImpl.STAFF_ATTENDANCE_EQUIVALENT_WAGE);

	// Get the value.
	// Get the old value.
	// Add new value to old.
	Double oldValue = this.overallBreakdownWageMap.get(status) == null ? 0
		: this.overallBreakdownWageMap.get(status);
	this.overallBreakdownWageMap.put(status, oldValue + wage);

	if (wage == 0.0) {
	    return "-";
	}
	return this.formatter.format(wage);
    }

    /**
     * Get the staff's breakdown of attendance count and wage.
     * 
     * @param staffWageBreakdown
     * @param rowBean
     * @return
     */
    private JSONPayrollResult setAttendanceBreakdown(
	    Map<AttendanceStatus, Map<String, Double>> staffWageBreakdown, JSONPayrollResult rowBean) {

	// OVERTIME.
	rowBean.setBreakdownOvertimeCount(getBreakdownCount(staffWageBreakdown,
		AttendanceStatus.OVERTIME));
	rowBean.setBreakdownOvertimeWage(getBreakdownWage(staffWageBreakdown, AttendanceStatus.OVERTIME));

	// ABSENT.
	rowBean.setBreakdownAbsentCount(getBreakdownCount(staffWageBreakdown, AttendanceStatus.ABSENT));
	rowBean.setBreakdownAbsentWage(getBreakdownWage(staffWageBreakdown, AttendanceStatus.ABSENT));

	// HALFDAY.
	rowBean.setBreakdownHalfdayCount(getBreakdownCount(staffWageBreakdown, AttendanceStatus.HALFDAY));
	rowBean.setBreakdownHalfdayWage(getBreakdownWage(staffWageBreakdown, AttendanceStatus.HALFDAY));

	// LATE.
	rowBean.setBreakdownLateCount(getBreakdownCount(staffWageBreakdown, AttendanceStatus.LATE));
	rowBean.setBreakdownLateWage(getBreakdownWage(staffWageBreakdown, AttendanceStatus.LATE));

	// LEAVE.
	rowBean.setBreakdownLeaveCount(getBreakdownCount(staffWageBreakdown, AttendanceStatus.LEAVE));
	rowBean.setBreakdownLeaveWage(getBreakdownWage(staffWageBreakdown, AttendanceStatus.LEAVE));

	// PRESENT.
	rowBean.setBreakdownPresentCount(getBreakdownCount(staffWageBreakdown, AttendanceStatus.PRESENT));
	rowBean.setBreakdownPresentWage(getBreakdownWage(staffWageBreakdown, AttendanceStatus.PRESENT));

	return rowBean;
    }

    /**
     * Return value of tree grid.
     * 
     * @param skip
     * @param value
     * @param this.formatter
     * @return
     */
    private String getTreeGridRowValue(boolean skip, String value) {
	// return skip ? "<i>(" + value + ")</i>" : this.formatter.format(Double
	// .valueOf(value));
	return skip ? "" : this.formatter.format(Double.valueOf(value));
    }

    /**
     * Get partial tree grid for managers.
     * 
     * @param managerPayrollMap
     * @param headerManagerPKey
     * @param randomno
     * @param this.formatter
     * @param this.treeGrid
     * @param allStaffWageBreakdown
     * @return
     */
    private void constructTreeGridStaff() {

	Random randomno = new Random();

	// Manager total.
	// Add header beans.
	String staffTotalStr = this.formatter.format(this.overallTotalOfStaff);
	long headerPKey = Math.abs(randomno.nextLong());
	JSONPayrollResult headerBean = new JSONPayrollResult(headerPKey, -1,
		CSSClass.PRIMARY.getSpanHTML("TOTAL"), staffTotalStr, "&nbsp;");
	this.treeGrid.add(headerBean);

	// Loop through managers.
	// Sort by formal name.
	Set<Staff> staffSet = this.staffToWageMap.keySet();
	List<Staff> staffList = DataStructUtils.convertSetToList(staffSet);
	Collections.sort(staffList, new Comparator<Staff>() {
	    @Override
	    public int compare(Staff aObj, Staff bObj) {
		String aName = aObj.getFormalName();
		String bName = bObj.getFormalName();

		int a = aName.compareToIgnoreCase(bName);
		return a < 0 ? -1 : a > 0 ? 1 : 0;
	    }
	});

	// Loop through all staff.
	for (Staff staff : staffList) {

	    // Get details.
	    long rowPKey = Math.abs(randomno.nextLong());
	    String rowName = CSSClass.SUCCESS.getSpanHTML("STAFF", staff.getFormalName());
	    String value = this.staffToWageMap.get(staff);
	    boolean skip = value.contains(IDENTIFIER_ALREADY_EXISTS);
	    String rowValue = getTreeGridRowValue(skip, value);

	    // Add to bean.
	    double staffWage = staff.getWage();
	    this.overallTotalOfWage += staffWage;
	    String staffWageStr = NumberFormatUtils.getCurrencyFormatter().format(staffWage);
	    JSONPayrollResult rowBean = new JSONPayrollResult(rowPKey, headerPKey, rowName, rowValue,
		    staffWageStr);

	    // Breakdown.
	    if (!skip) {
		Map<AttendanceStatus, Map<String, Double>> staffWageBreakdown = this.staffPayrollBreakdownMap
			.get(staff);
		rowBean = setAttendanceBreakdown(staffWageBreakdown, rowBean);
	    }

	    // Add to tree grid list.
	    this.treeGrid.add(rowBean);
	}

	// Update the mother bean.
	// Add the breakdown.
	setBreakdownMotherBean();
    }

    /**
     * Set the breakdown for the motherbean.
     */
    private void setBreakdownMotherBean() {
	JSONPayrollResult motherBean = this.treeGrid.get(0);
	motherBean.setWage(this.formatter.format(this.overallTotalOfWage));
	motherBean.setBreakdownAbsentCount(getOverallBreakdownCountStr(AttendanceStatus.ABSENT));
	motherBean.setBreakdownAbsentWage(getOverallBreakdownWageStr(AttendanceStatus.ABSENT));
	motherBean.setBreakdownHalfdayCount(getOverallBreakdownCountStr(AttendanceStatus.HALFDAY));
	motherBean.setBreakdownHalfdayWage(getOverallBreakdownWageStr(AttendanceStatus.HALFDAY));
	motherBean.setBreakdownLateCount(getOverallBreakdownCountStr(AttendanceStatus.LATE));
	motherBean.setBreakdownLateWage(getOverallBreakdownWageStr(AttendanceStatus.LATE));
	motherBean.setBreakdownLeaveCount(getOverallBreakdownCountStr(AttendanceStatus.LEAVE));
	motherBean.setBreakdownLeaveWage(getOverallBreakdownWageStr(AttendanceStatus.LEAVE));
	motherBean.setBreakdownOvertimeCount(getOverallBreakdownCountStr(AttendanceStatus.OVERTIME));
	motherBean.setBreakdownOvertimeWage(getOverallBreakdownWageStr(AttendanceStatus.OVERTIME));
	motherBean.setBreakdownPresentCount(getOverallBreakdownCountStr(AttendanceStatus.PRESENT));
	motherBean.setBreakdownPresentWage(getOverallBreakdownWageStr(AttendanceStatus.PRESENT));
	this.treeGrid.set(0, motherBean);
    }

    private String getOverallBreakdownWageStr(AttendanceStatus status) {
	return this.overallBreakdownWageMap.get(status) == 0.0 ? "-" : this.formatter
		.format(this.overallBreakdownWageMap.get(status));
    }

    private String getOverallBreakdownCountStr(AttendanceStatus status) {
	return this.overallBreakdownCountMap.get(status) == 0 ? "-" : "("
		+ this.overallBreakdownCountMap.get(status) + ")";
    }

    @Transactional
    @Override
    public void compute(Date min, Date max, ProjectPayroll projectPayroll) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(projectPayroll)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_PAYROLL, projectPayroll.getKey());
	    return;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_COMPUTE, ConstantsRedis.OBJECT_PAYROLL,
		projectPayroll.getKey());

	// Clear old data.
	clear();

	// Initialize.
	this.startDate = min;
	this.endDate = max;
	this.projectPayroll = projectPayroll;
	this.staffIDsToCompute = DataStructUtils.convertArrayToList(projectPayroll.getStaffIDs());

	// Compute.
	compute();
	results();
    }

    private void results() {
	this.payrollResult.setStartDate(this.startDate);
	this.payrollResult.setEndDate(this.endDate);
	this.payrollResult.setOverallTotalOfStaff(this.overallTotalOfStaff);
	this.payrollResult.setStaffPayrollBreakdownMap(this.staffPayrollBreakdownMap);
	this.payrollResult.setStaffToWageMap(this.staffToWageMap);
	this.payrollResult.setTreeGrid(this.treeGrid);
    }

    @Transactional
    @Override
    public PayrollResultComputation getPayrollResult() {
	return payrollResult;
    }

}
