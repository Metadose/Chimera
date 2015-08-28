package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.PairCountValue;
import com.cebedo.pmsys.bean.PayrollResultComputation;
import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.pojo.JSONPayrollResult;
import com.cebedo.pmsys.service.AttendanceService;
import com.cebedo.pmsys.service.ProjectPayrollComputerService;
import com.cebedo.pmsys.service.StaffService;
import com.cebedo.pmsys.utils.DataStructUtils;
import com.google.gson.Gson;

@Service
public class ProjectPayrollComputerServiceImpl implements ProjectPayrollComputerService {

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

    // Map of staff and corresponding wage.
    private Map<Staff, Double> staffToWageMap = new HashMap<Staff, Double>();

    // Total wage for group "managersTotal".
    private double overallTotalOfStaff = 0;

    // "allStaffWageBreakdown" Attendance status with corresponding count map.
    // This is the breakdown of total for each staff.
    private Map<Staff, Map<AttendanceStatus, PairCountValue>> staffPayrollBreakdownMap = new HashMap<Staff, Map<AttendanceStatus, PairCountValue>>();

    // JSON tree grid.
    private List<JSONPayrollResult> treeGrid = new ArrayList<JSONPayrollResult>();

    // Results.
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

	this.staffToWageMap = new HashMap<Staff, Double>();
	this.overallTotalOfStaff = 0;

	this.staffPayrollBreakdownMap = new HashMap<Staff, Map<AttendanceStatus, PairCountValue>>();
	this.treeGrid = new ArrayList<JSONPayrollResult>();

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
	Map<AttendanceStatus, PairCountValue> attendanceStatusCountMap = getStaffBreakdownMap(
		this.projectPayroll.getProject(), staff, this.startDate, this.endDate);
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

	    // Get wage then add to map.
	    // Get the total of this guy.
	    double staffWageTotal = this.attendanceService.getTotalWageOfStaffInRange(
		    this.projectPayroll.getProject(), staff, this.startDate, this.endDate);

	    // Add it to the overall total of managers.
	    this.overallTotalOfStaff += staffWageTotal;

	    // Add the value to the map.
	    // And to the "already computed" map.
	    this.staffToWageMap.put(staff, staffWageTotal);

	    // Get the breakdown of this total.
	    // Add the breakdown to the map.
	    putStaffBreakdown(staff);
	}
    }

    /**
     * Get the breakdown of the total wage.
     * 
     * @param project
     * 
     * @param manager
     * @param min
     * @param max
     * @return
     */
    private Map<AttendanceStatus, PairCountValue> getStaffBreakdownMap(Project project, Staff manager,
	    Date min, Date max) {
	// Attendance count map.
	Set<Attendance> attendanceList = this.attendanceService.rangeStaffAttendance(project, manager,
		min, max);
	Map<AttendanceStatus, PairCountValue> attendanceStatusCountMap = this.staffService
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
	try {
	    constructTreeGridStaff();
	    return new Gson().toJson(this.treeGrid, ArrayList.class);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return "";
    }

    /**
     * Get the count breakdown.
     * 
     * @param staffWageBreakdown
     * @param overtime
     * @return
     */
    private int getBreakdownCount(Map<AttendanceStatus, PairCountValue> staffWageBreakdown,
	    AttendanceStatus status) {

	// Get the count, wage and format.
	double count = staffWageBreakdown.get(status).getCount();

	// Get the value.
	// Get the old value.
	// Add new value to old.
	int intValueOfCount = (int) count;
	int oldValue = this.overallBreakdownCountMap.get(status) == null ? 0
		: this.overallBreakdownCountMap.get(status);

	// Update overall breakdown.
	this.overallBreakdownCountMap.put(status, oldValue + intValueOfCount);

	return intValueOfCount;
    }

    /**
     * Get the wage breakdown.
     * 
     * @param countAndWage
     * @return
     */
    private double getBreakdownWage(Map<AttendanceStatus, PairCountValue> staffWageBreakdown,
	    AttendanceStatus status) {

	// Get the count, wage and format.
	double wage = staffWageBreakdown.get(status).getValue();

	// Get the value.
	// Get the old value.
	// Add new value to old.
	double oldValue = this.overallBreakdownWageMap.get(status) == null ? 0
		: this.overallBreakdownWageMap.get(status);

	// Update overall.
	this.overallBreakdownWageMap.put(status, oldValue + wage);

	return wage;
    }

    /**
     * Get the staff's breakdown of attendance count and wage.
     * 
     * @param staffWageBreakdown
     * @param rowBean
     * @return
     */
    private JSONPayrollResult setAttendanceBreakdown(
	    Map<AttendanceStatus, PairCountValue> staffWageBreakdown, JSONPayrollResult rowBean) {

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
	    double rowValue = this.staffToWageMap.get(staff);

	    // Add to bean.
	    JSONPayrollResult rowBean = new JSONPayrollResult(staff.getFormalName(), rowValue,
		    staff.getWage());

	    // Breakdown.
	    Map<AttendanceStatus, PairCountValue> staffWageBreakdown = this.staffPayrollBreakdownMap
		    .get(staff);
	    rowBean = setAttendanceBreakdown(staffWageBreakdown, rowBean);

	    // Add to tree grid list.
	    this.treeGrid.add(rowBean);
	}
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
