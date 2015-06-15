package com.cebedo.pmsys.service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.PayrollComputationResult;
import com.cebedo.pmsys.bean.TreeGridRowBean;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.enums.CSSClass;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.utils.DataStructUtils;
import com.cebedo.pmsys.utils.NumberFormatUtils;
import com.google.gson.Gson;

@Service
public class ProjectPayrollComputerServiceImpl implements
	ProjectPayrollComputerService {

    private static final String IDENTIFIER_ALREADY_EXISTS = "Check ";

    private PayrollService payrollService;
    private StaffService staffService;

    public void setStaffService(StaffService staffService) {
	this.staffService = staffService;
    }

    public void setPayrollService(PayrollService payrollService) {
	this.payrollService = payrollService;
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
    private List<TreeGridRowBean> treeGrid = new ArrayList<TreeGridRowBean>();

    // Currency formatter.
    private NumberFormat formatter = NumberFormatUtils.getCurrencyFormatter();

    // Results.
    private PayrollComputationResult payrollResult = new PayrollComputationResult();

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
	this.treeGrid = new ArrayList<TreeGridRowBean>();
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
		this.staffToWageMap.put(staff, IDENTIFIER_ALREADY_EXISTS
			+ this.alreadyComputedMap.get(staffID));
		continue;
	    }

	    // Get wage then add to map.
	    // Get the total of this guy.
	    double staffWageTotal = this.payrollService
		    .getTotalWageOfStaffInRange(staff, this.startDate,
			    this.endDate);

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
    private Map<AttendanceStatus, Map<String, Double>> getStaffBreakdownMap(
	    Staff manager, Date min, Date max) {
	// Attendance count map.
	Set<Attendance> attendanceList = this.payrollService
		.rangeStaffAttendance(manager, min, max);
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

    private String getBreakdownCount(Map<String, Double> countAndWage) {
	// Get the count, wage and format.
	double count = countAndWage
		.get(StaffServiceImpl.STAFF_ATTENDANCE_STATUS_COUNT);
	return "(" + (int) count + ")";
    }

    private String getBreakdownWage(Map<String, Double> countAndWage) {
	Double wage = countAndWage
		.get(StaffServiceImpl.STAFF_ATTENDANCE_EQUIVALENT_WAGE);
	return this.formatter.format(wage);
    }

    /**
     * Get the staff's breakdown of attendance count and wage.
     * 
     * @param staffWageBreakdown
     * @param rowBean
     * @return
     */
    private TreeGridRowBean setAttendanceBreakdown(
	    Map<AttendanceStatus, Map<String, Double>> staffWageBreakdown,
	    TreeGridRowBean rowBean) {

	// OVERTIME.
	Map<String, Double> overtimeCountAndWage = staffWageBreakdown
		.get(AttendanceStatus.OVERTIME);
	rowBean.setBreakdownOvertimeCount(getBreakdownCount(overtimeCountAndWage));
	rowBean.setBreakdownOvertimeWage(getBreakdownWage(overtimeCountAndWage));

	// ABSENT.
	Map<String, Double> absentCountAndWage = staffWageBreakdown
		.get(AttendanceStatus.ABSENT);
	rowBean.setBreakdownAbsentCount(getBreakdownCount(absentCountAndWage));
	rowBean.setBreakdownAbsentWage(getBreakdownWage(absentCountAndWage));

	// HALFDAY.
	Map<String, Double> halfdayCountAndWage = staffWageBreakdown
		.get(AttendanceStatus.HALFDAY);
	rowBean.setBreakdownHalfdayCount(getBreakdownCount(halfdayCountAndWage));
	rowBean.setBreakdownHalfdayWage(getBreakdownWage(halfdayCountAndWage));

	// LATE.
	Map<String, Double> lateCountAndWage = staffWageBreakdown
		.get(AttendanceStatus.LATE);
	rowBean.setBreakdownLateCount(getBreakdownCount(lateCountAndWage));
	rowBean.setBreakdownLateWage(getBreakdownWage(lateCountAndWage));

	// LEAVE.
	Map<String, Double> leaveCountAndWage = staffWageBreakdown
		.get(AttendanceStatus.LEAVE);
	rowBean.setBreakdownLeaveCount(getBreakdownCount(leaveCountAndWage));
	rowBean.setBreakdownLeaveWage(getBreakdownWage(leaveCountAndWage));

	// PRESENT.
	Map<String, Double> presentCountAndWage = staffWageBreakdown
		.get(AttendanceStatus.PRESENT);
	rowBean.setBreakdownPresentCount(getBreakdownCount(presentCountAndWage));
	rowBean.setBreakdownPresentWage(getBreakdownWage(presentCountAndWage));

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
	TreeGridRowBean headerBean = new TreeGridRowBean(headerPKey, -1,
		CSSClass.PRIMARY.getSpanHTML("TOTAL"), staffTotalStr, "&nbsp;");
	this.treeGrid.add(headerBean);

	// Loop through managers.
	for (Staff staff : this.staffToWageMap.keySet()) {

	    // Get details.
	    long rowPKey = Math.abs(randomno.nextLong());
	    boolean isManager = staff.isManager(this.projectPayroll
		    .getProject());
	    String rowName = isManager ? CSSClass.DANGER.getSpanHTML("MANAGER",
		    staff.getFullName()) : CSSClass.SUCCESS.getSpanHTML(
		    "STAFF", staff.getFullName());
	    String value = this.staffToWageMap.get(staff);
	    boolean skip = value.contains(IDENTIFIER_ALREADY_EXISTS);
	    String rowValue = getTreeGridRowValue(skip, value);

	    // Add to bean.
	    TreeGridRowBean rowBean = new TreeGridRowBean(rowPKey, headerPKey,
		    rowName, rowValue, NumberFormatUtils.getCurrencyFormatter()
			    .format(staff.getWage()));

	    // Breakdown.
	    if (!skip) {
		Map<AttendanceStatus, Map<String, Double>> staffWageBreakdown = this.staffPayrollBreakdownMap
			.get(staff);
		rowBean = setAttendanceBreakdown(staffWageBreakdown, rowBean);
	    }

	    // Add to tree grid list.
	    this.treeGrid.add(rowBean);
	}
    }

    @Transactional
    @Override
    public void compute(Project proj, Date min, Date max,
	    ProjectPayroll projectPayroll) {

	// Clear old data.
	clear();

	// Initialize.
	this.startDate = min;
	this.endDate = max;
	this.projectPayroll = projectPayroll;
	this.staffIDsToCompute = DataStructUtils
		.convertArrayToList(projectPayroll.getStaffIDs());

	// Compute.
	compute();
	results();
    }

    private void results() {
	this.payrollResult.setStartDate(this.startDate);
	this.payrollResult.setEndDate(this.endDate);
	this.payrollResult.setOverallTotalOfStaff(this.overallTotalOfStaff);
	this.payrollResult
		.setStaffPayrollBreakdownMap(this.staffPayrollBreakdownMap);
	this.payrollResult.setStaffToWageMap(this.staffToWageMap);
	this.payrollResult.setTreeGrid(this.treeGrid);
    }

    @Transactional
    @Override
    public PayrollComputationResult getPayrollResult() {
	return payrollResult;
    }

}
