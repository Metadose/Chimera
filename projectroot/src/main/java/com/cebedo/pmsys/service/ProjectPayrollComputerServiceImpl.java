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

import com.cebedo.pmsys.bean.TreeGridRowBean;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.enums.CSSClass;
import com.cebedo.pmsys.enums.PayrollType;
import com.cebedo.pmsys.model.Delivery;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;
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
    private Project project;
    private List<Long> staffIDsToCompute;
    private Date startDate, endDate;

    // Already computed? Add ID to map "computedMap".
    private Map<Long, String> alreadyComputedMap = new HashMap<Long, String>();

    // Map of manager and corresponding wage.
    // Map of object, and for each object's set of staff.
    private Map<ManagerAssignment, String> managerToWageMap = new HashMap<ManagerAssignment, String>();
    private Map<Team, Map<Staff, String>> teamToStaffToWageMap = new HashMap<Team, Map<Staff, String>>();
    private Map<Delivery, Map<Staff, String>> deliveryToStaffToWageMap = new HashMap<Delivery, Map<Staff, String>>();
    private Map<Task, Map<Staff, String>> taskToStaffToWageMap = new HashMap<Task, Map<Staff, String>>();
    private Map<Task, Map<Team, Map<Staff, String>>> taskToTeamToStaffToWageMap = new HashMap<Task, Map<Team, Map<Staff, String>>>();

    // Total wage for group "managersTotal".
    private double overallTotalOfAll = 0;
    private double overallTotalOfManagers = 0;
    private double overallTotalOfTeams = 0;
    private double overallTotalOfDeliveries = 0;
    private double overallTotalOfTasks = 0;

    // For each group ("Team 1", "Team 2", "Delivery 1", "Delivery 2", etc.),
    // there is a corresponding total.
    private Map<Team, Double> mapOfTeamToTotal = new HashMap<Team, Double>();
    private Map<Delivery, Double> mapOfDeliveryToTotal = new HashMap<Delivery, Double>();
    private Map<Task, Double> mapOfTaskToTotal = new HashMap<Task, Double>();
    private Map<Task, Map<Team, Double>> mapOfTaskTeamToTotal = new HashMap<Task, Map<Team, Double>>();

    // "allStaffWageBreakdown" Attendance status with corresponding count map.
    // This is the breakdown of total for each staff.
    private Map<Staff, Map<AttendanceStatus, Map<String, Double>>> staffPayrollBreakdownMap = new HashMap<Staff, Map<AttendanceStatus, Map<String, Double>>>();

    // JSON tree grid.
    private List<TreeGridRowBean> treeGrid = new ArrayList<TreeGridRowBean>();

    // Currency formatter.
    private NumberFormat formatter = NumberFormatUtils.getCurrencyFormatter();

    /**
     * Get map of payrolls.
     */
    private void compute() {

	// Compute managers.
	computeManagers();

	// Compute teams.
	computeTeams();

	// Compute tasks.
	computeTasks();

	// Compute deliveries.
	computeDelivery();

	this.overallTotalOfAll = this.overallTotalOfManagers
		+ this.overallTotalOfTeams + this.overallTotalOfDeliveries
		+ this.overallTotalOfTasks;
    }

    /**
     * Clear old data.
     */
    private void clear() {
	this.project = null;
	this.staffIDsToCompute = null;
	this.startDate = null;
	this.endDate = null;

	this.alreadyComputedMap = new HashMap<Long, String>();
	this.managerToWageMap = new HashMap<ManagerAssignment, String>();
	this.teamToStaffToWageMap = new HashMap<Team, Map<Staff, String>>();
	this.deliveryToStaffToWageMap = new HashMap<Delivery, Map<Staff, String>>();
	this.taskToStaffToWageMap = new HashMap<Task, Map<Staff, String>>();
	this.taskToTeamToStaffToWageMap = new HashMap<Task, Map<Team, Map<Staff, String>>>();

	this.overallTotalOfAll = 0;
	this.overallTotalOfManagers = 0;
	this.overallTotalOfTeams = 0;
	this.overallTotalOfDeliveries = 0;
	this.overallTotalOfTasks = 0;

	this.mapOfTeamToTotal = new HashMap<Team, Double>();
	this.mapOfDeliveryToTotal = new HashMap<Delivery, Double>();
	this.mapOfTaskToTotal = new HashMap<Task, Double>();
	this.mapOfTaskTeamToTotal = new HashMap<Task, Map<Team, Double>>();

	this.staffPayrollBreakdownMap = new HashMap<Staff, Map<AttendanceStatus, Map<String, Double>>>();
	this.treeGrid = new ArrayList<TreeGridRowBean>();
    }

    /**
     * Compute tasks.
     */
    private void computeTasks() {

	// Loop through all tasks.
	for (Task task : this.project.getAssignedTasks()) {
	    Map<Staff, String> staffPayrollMap = new HashMap<Staff, String>();
	    double thisTaskTotal = 0;

	    // A task can have a list of staff.
	    for (Staff assignedStaff : task.getStaff()) {
		long staffID = assignedStaff.getId();

		if (!this.staffIDsToCompute.contains(staffID)) {
		    continue;
		}

		// If a staff has already been computed before,
		// don't compute again.
		if (this.alreadyComputedMap.containsKey(staffID)) {
		    staffPayrollMap.put(
			    assignedStaff,
			    IDENTIFIER_ALREADY_EXISTS
				    + this.alreadyComputedMap.get(staffID));
		    continue;
		}

		// Add to map
		// and add to computed list.
		double totalWageOfStaff = this.payrollService
			.getTotalWageOfStaffInRange(assignedStaff,
				this.startDate, this.endDate);
		thisTaskTotal += totalWageOfStaff;
		staffPayrollMap.put(assignedStaff,
			String.valueOf(totalWageOfStaff));
		this.alreadyComputedMap.put(staffID, "Task " + task.getTitle());

		// Get the breakdown of this total.
		putStaffBreakdown(assignedStaff);
	    }

	    this.taskToStaffToWageMap.put(task, staffPayrollMap);

	    // A task can also have teams.
	    double thisTaskTeamTotal = 0;
	    Map<Team, Map<Staff, String>> teamStaffWageMap = new HashMap<Team, Map<Staff, String>>();
	    Map<Team, Double> taskTeamToTotal = new HashMap<Team, Double>();
	    for (Team team : task.getTeams()) {

		// Consider only task-based teams.
		// Teams that are paid if there are tasks.
		if (team.getPayrollTypeEnum() == PayrollType.TASK) {

		    // Loop through each member of the team.
		    double thisTeamTotal = 0;
		    Map<Staff, String> staffWageMap = new HashMap<Staff, String>();
		    for (Staff staff : team.getMembers()) {

			// Skip if not in target.
			long staffID = staff.getId();
			if (!this.staffIDsToCompute.contains(staffID)) {
			    continue;
			}

			// If a staff has already been computed before,
			// don't compute again.
			if (this.alreadyComputedMap.containsKey(staffID)) {
			    staffWageMap.put(staff, IDENTIFIER_ALREADY_EXISTS
				    + this.alreadyComputedMap.get(staffID));
			    continue;
			}

			// Add to map
			// and add to computed list.
			double totalWageOfStaff = this.payrollService
				.getTotalWageOfStaffInRange(staff,
					this.startDate, this.endDate);
			thisTeamTotal += totalWageOfStaff;
			staffWageMap.put(staff,
				String.valueOf(totalWageOfStaff));
			this.alreadyComputedMap.put(staffID, "Task Team "
				+ team.getName());

			// Get the breakdown of this total.
			putStaffBreakdown(staff);
		    }

		    thisTaskTeamTotal += thisTeamTotal;
		    teamStaffWageMap.put(team, staffWageMap);

		    // For every team, there is a total.
		    taskTeamToTotal.put(team, thisTeamTotal);

		} // End If we are a task-based.
	    } // End loop of all teams.

	    // Add the task team total to the overall task.
	    thisTaskTotal += thisTaskTeamTotal;

	    // For every team with total, assign it to task.
	    this.mapOfTaskTeamToTotal.put(task, taskTeamToTotal);

	    // Add to task list.
	    this.overallTotalOfTasks += thisTaskTotal;

	    // For every task, there is a corresponding total.
	    this.mapOfTaskToTotal.put(task, thisTaskTotal);

	    this.taskToTeamToStaffToWageMap.put(task, teamStaffWageMap);
	}
    }

    /**
     * Compute delivery.
     */
    private void computeDelivery() {

	// Wage for delivery.
	// Loop through all the deliveries.
	for (Delivery delivery : this.project.getDeliveries()) {

	    Map<Staff, String> staffPayrollMap = new HashMap<Staff, String>();
	    double thisDeliveryTotal = 0;

	    for (Staff assignedStaff : delivery.getStaff()) {

		// Skip if not in target.
		long staffID = assignedStaff.getId();
		if (!this.staffIDsToCompute.contains(staffID)) {
		    continue;
		}

		// If a staff has already been computed before,
		// don't compute again.
		if (this.alreadyComputedMap.containsKey(staffID)) {
		    staffPayrollMap.put(
			    assignedStaff,
			    IDENTIFIER_ALREADY_EXISTS
				    + this.alreadyComputedMap.get(staffID));
		    continue;
		}

		// Add to map
		// and add to computed list.
		double totalWageOfStaff = this.payrollService
			.getTotalWageOfStaffInRange(assignedStaff,
				this.startDate, this.endDate);
		thisDeliveryTotal += totalWageOfStaff;
		staffPayrollMap.put(assignedStaff,
			String.valueOf(totalWageOfStaff));
		this.alreadyComputedMap.put(staffID,
			"Delivery " + delivery.getName());

		// Get the breakdown of this total.
		putStaffBreakdown(assignedStaff);
	    }

	    // Add to team list.
	    this.overallTotalOfDeliveries += thisDeliveryTotal;
	    this.mapOfDeliveryToTotal.put(delivery, thisDeliveryTotal);
	    this.deliveryToStaffToWageMap.put(delivery, staffPayrollMap);
	}
    }

    /**
     * Compute teams.
     */
    private void computeTeams() {

	// Loop through all the teams.
	for (Team team : this.project.getAssignedTeams()) {

	    // If the team is project-based,
	    // add him to computation. Compute now.
	    if (team.getPayrollTypeEnum() != PayrollType.PROJECT) {
		continue;
	    }

	    Map<Staff, String> staffPayrollMap = new HashMap<Staff, String>();
	    double thisTeamTotal = 0;

	    for (Staff member : team.getMembers()) {

		// If not a target ID to compute,
		// skip.
		long memberID = member.getId();
		if (!this.staffIDsToCompute.contains(memberID)) {
		    continue;
		}

		// If a staff has already been computed before,
		// don't compute again.
		if (this.alreadyComputedMap.containsKey(memberID)) {
		    staffPayrollMap.put(member, IDENTIFIER_ALREADY_EXISTS
			    + this.alreadyComputedMap.get(memberID));
		    continue;
		}

		// Add to map
		// and add to computed list.
		double totalWageOfStaff = this.payrollService
			.getTotalWageOfStaffInRange(member, this.startDate,
				this.endDate);
		thisTeamTotal += totalWageOfStaff;
		staffPayrollMap.put(member, String.valueOf(totalWageOfStaff));

		// Add to computed map.
		this.alreadyComputedMap.put(memberID, "Team " + team.getName());

		// Get the breakdown of this total.
		putStaffBreakdown(member);
	    }

	    // Add to team list.
	    this.overallTotalOfTeams += thisTeamTotal;
	    this.mapOfTeamToTotal.put(team, thisTeamTotal);
	    this.teamToStaffToWageMap.put(team, staffPayrollMap);
	}
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
     * Compute managers.
     */
    private void computeManagers() {

	for (ManagerAssignment assignment : this.project
		.getManagerAssignments()) {

	    // Get the staff,
	    // check if already computed.
	    Staff manager = assignment.getManager();
	    long managerID = manager.getId();
	    if (!this.staffIDsToCompute.contains(managerID)) {
		continue;
	    }

	    // If a staff has already been computed before,
	    // don't compute again.
	    if (this.alreadyComputedMap.containsKey(managerID)) {
		this.managerToWageMap.put(assignment, IDENTIFIER_ALREADY_EXISTS
			+ this.alreadyComputedMap.get(managerID));
		continue;
	    }

	    // Get wage then add to map.
	    // Get the total of this guy.
	    double managerWageTotal = this.payrollService
		    .getTotalWageOfStaffInRange(manager, this.startDate,
			    this.endDate);

	    // Add it to the overall total of managers.
	    this.overallTotalOfManagers += managerWageTotal;

	    // Add the value to the map.
	    // And to the "already computed" map.
	    this.managerToWageMap.put(assignment,
		    String.valueOf(managerWageTotal));
	    this.alreadyComputedMap.put(managerID, "Manager List");

	    // Get the breakdown of this total.
	    // Add the breakdown to the map.
	    putStaffBreakdown(manager);
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

	// Get needed data for the mother.
	// Add the mother bean.
	Random randomno = new Random();
	long motherPKey = Math.abs(randomno.nextLong());
	String overallTotalStr = this.formatter.format(this.overallTotalOfAll);
	TreeGridRowBean motherBean = new TreeGridRowBean(
		motherPKey,
		-1,
		CSSClass.SUCCESS.getSpanHTML("PROJECT", this.project.getName()),
		overallTotalStr);
	this.treeGrid.add(motherBean);

	// Managers.
	constructTreeGridManager(randomno, motherPKey);

	// Teams.
	constructTreeGridTeam(randomno, motherPKey);

	// Tasks.
	constructTreeGridTask(randomno, motherPKey);

	// Deliveries.
	constructTreeGridDelivery(randomno, motherPKey);

	return new Gson().toJson(this.treeGrid, ArrayList.class);
    }

    /**
     * Get partial tree grid for team.
     * 
     * @param teamPayrollMap
     * @param randomno
     * @param teamGroup
     * @param this.formatter
     * @param headerTeamPKey
     * @param this.treeGrid
     * @param allStaffWageBreakdown
     * @return
     */
    private void constructTreeGridDelivery(Random randomno, long motherPKey) {

	// Add the mother key.
	String thisTotalStr = this.formatter
		.format(this.overallTotalOfDeliveries);
	long headerPKey = Math.abs(randomno.nextLong());
	TreeGridRowBean headerBean = new TreeGridRowBean(headerPKey,
		motherPKey,
		CSSClass.PRIMARY.getSpanHTML("GROUP", "Deliveries"),
		thisTotalStr);
	this.treeGrid.add(headerBean);

	// Loop through teams.
	for (Delivery delivery : this.deliveryToStaffToWageMap.keySet()) {

	    // If staff list is empty, skip it.
	    Map<Staff, String> staffMap = this.deliveryToStaffToWageMap
		    .get(delivery);
	    if (staffMap.keySet().isEmpty()) {
		continue;
	    }

	    // Get details.
	    // Add to bean.
	    long thisPKey = Math.abs(randomno.nextLong());
	    double thisObjectTotal = this.mapOfDeliveryToTotal.get(delivery);
	    String thisObjectTotalStr = this.formatter.format(thisObjectTotal);
	    TreeGridRowBean thisBean = new TreeGridRowBean(thisPKey,
		    headerPKey, CSSClass.INFO.getSpanHTML("DELIVERY",
			    delivery.getName()), thisObjectTotalStr);
	    this.treeGrid.add(thisBean);

	    // Staff list total and breakdown.
	    setStaffListTotalAndBreakdown(staffMap, randomno, thisPKey);
	}
    }

    /**
     * Get list of total and breakdown of a given staff list.
     * 
     * @param this.treeGrid
     * @param staffMap
     * @param randomno
     * @param this.formatter
     * @param motherPKey
     * @param allStaffWageBreakdown
     * @return
     */
    private void setStaffListTotalAndBreakdown(Map<Staff, String> staffMap,
	    Random randomno, long motherPKey) {

	// Add all staff inside team.
	for (Staff staff : staffMap.keySet()) {

	    // Get details.
	    long rowPKey = Math.abs(randomno.nextLong());
	    String rowName = CSSClass.DEFAULT.getSpanHTML("STAFF",
		    staff.getFullName());
	    String value = staffMap.get(staff);
	    boolean skip = value.contains(IDENTIFIER_ALREADY_EXISTS);
	    String rowValue = getTreeGridRowValue(skip, value);

	    // Add to bean.
	    TreeGridRowBean rowBean = new TreeGridRowBean(rowPKey, motherPKey,
		    rowName, rowValue);

	    // Breakdown.
	    if (!skip) {
		Map<AttendanceStatus, Map<String, Double>> staffWageBreakdown = this.staffPayrollBreakdownMap
			.get(staff);
		rowBean = setAttendanceBreakdown(staffWageBreakdown, rowBean);
	    }

	    this.treeGrid.add(rowBean);
	}
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
	rowBean.setBreakdownOvertime(getBreakdownText(overtimeCountAndWage));

	// ABSENT.
	Map<String, Double> absentCountAndWage = staffWageBreakdown
		.get(AttendanceStatus.ABSENT);
	rowBean.setBreakdownAbsent(getBreakdownText(absentCountAndWage));

	// HALFDAY.
	Map<String, Double> halfdayCountAndWage = staffWageBreakdown
		.get(AttendanceStatus.HALFDAY);
	rowBean.setBreakdownHalfday(getBreakdownText(halfdayCountAndWage));

	// LATE.
	Map<String, Double> lateCountAndWage = staffWageBreakdown
		.get(AttendanceStatus.LATE);
	rowBean.setBreakdownLate(getBreakdownText(lateCountAndWage));

	// LEAVE.
	Map<String, Double> leaveCountAndWage = staffWageBreakdown
		.get(AttendanceStatus.LEAVE);
	rowBean.setBreakdownLeave(getBreakdownText(leaveCountAndWage));

	// PRESENT.
	Map<String, Double> presentCountAndWage = staffWageBreakdown
		.get(AttendanceStatus.PRESENT);
	rowBean.setBreakdownPresent(getBreakdownText(presentCountAndWage));

	return rowBean;
    }

    /**
     * Get the breakdown of an attendance status.
     * 
     * @param countAndWage
     * @return
     */
    private String getBreakdownText(Map<String, Double> countAndWage) {

	// Get the count, wage and format.
	double count = countAndWage
		.get(StaffServiceImpl.STAFF_ATTENDANCE_STATUS_COUNT);
	Double wage = countAndWage
		.get(StaffServiceImpl.STAFF_ATTENDANCE_EQUIVALENT_WAGE);

	// Construct the text.
	String countPart = "(" + (int) count + ")";
	String breakdownText = countPart + " " + this.formatter.format(wage);
	return breakdownText;
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
     * Get partial tree grid for team.
     * 
     * @param teamPayrollMap
     * @param randomno
     * @param teamGroup
     * @param this.formatter
     * @param headerTeamPKey
     * @param this.treeGrid
     * @param allStaffWageBreakdown
     * @return
     */
    private void constructTreeGridTeam(Random randomno, long motherPKey) {

	// Add the mother key.
	String teamsTotalStr = this.formatter.format(this.overallTotalOfTeams);
	long headerTeamPKey = Math.abs(randomno.nextLong());
	TreeGridRowBean headerTeamBean = new TreeGridRowBean(headerTeamPKey,
		motherPKey, CSSClass.PRIMARY.getSpanHTML("GROUP", "Teams"),
		teamsTotalStr);
	this.treeGrid.add(headerTeamBean);

	// Loop through teams.
	for (Team team : this.teamToStaffToWageMap.keySet()) {

	    // If team is empty, skip it.
	    Map<Staff, String> staffMap = this.teamToStaffToWageMap.get(team);
	    if (staffMap.keySet().isEmpty()) {
		continue;
	    }

	    // Get details.
	    // Add this team to bean.
	    long teamPKey = Math.abs(randomno.nextLong());
	    double thisTeamTotal = this.mapOfTeamToTotal.get(team);
	    String thisTeamTotalStr = this.formatter.format(thisTeamTotal);
	    TreeGridRowBean teamBean = new TreeGridRowBean(teamPKey,
		    headerTeamPKey, CSSClass.INFO.getSpanHTML("TEAM",
			    team.getName()), thisTeamTotalStr);
	    this.treeGrid.add(teamBean);

	    // Staff list total and breakdown.
	    setStaffListTotalAndBreakdown(staffMap, randomno, teamPKey);
	}
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
    private void constructTreeGridManager(Random randomno, long motherPKey) {

	// Manager total.
	// Add header beans.
	String managersTotalStr = this.formatter
		.format(this.overallTotalOfManagers);
	long headerManagerPKey = Math.abs(randomno.nextLong());
	TreeGridRowBean headerManagerBean = new TreeGridRowBean(
		headerManagerPKey, motherPKey, CSSClass.PRIMARY.getSpanHTML(
			"GROUP", "Managers"), managersTotalStr);
	this.treeGrid.add(headerManagerBean);

	// Loop through managers.
	for (ManagerAssignment managerAssignment : this.managerToWageMap
		.keySet()) {

	    // Get details.
	    Staff manager = managerAssignment.getManager();
	    long rowPKey = Math.abs(randomno.nextLong());
	    String rowName = CSSClass.DEFAULT.getSpanHTML("STAFF",
		    manager.getFullName());
	    String value = this.managerToWageMap.get(managerAssignment);
	    boolean skip = value.contains(IDENTIFIER_ALREADY_EXISTS);
	    String rowValue = getTreeGridRowValue(skip, value);

	    // Add to bean.
	    TreeGridRowBean rowBean = new TreeGridRowBean(rowPKey,
		    headerManagerPKey, rowName, rowValue);

	    // Breakdown.
	    if (!skip) {
		Map<AttendanceStatus, Map<String, Double>> staffWageBreakdown = this.staffPayrollBreakdownMap
			.get(manager);
		rowBean = setAttendanceBreakdown(staffWageBreakdown, rowBean);
	    }

	    // Add to tree grid list.
	    this.treeGrid.add(rowBean);
	}
    }

    /**
     * Get partial tree grid for team.
     * 
     * @param teamPayrollMap
     * @param randomno
     * @param teamGroup
     * @param this.formatter
     * @param headerTeamPKey
     * @param this.treeGrid
     * @param allStaffWageBreakdown
     * @return
     */
    private void constructTreeGridTask(Random randomno, long motherPKey) {

	// Add the mother key.
	String thisTotalStr = this.formatter.format(this.overallTotalOfTasks);
	long headerPKey = Math.abs(randomno.nextLong());
	TreeGridRowBean headerBean = new TreeGridRowBean(headerPKey,
		motherPKey, CSSClass.PRIMARY.getSpanHTML("GROUP", "Tasks"),
		thisTotalStr);
	this.treeGrid.add(headerBean);

	// Loop through teams.
	for (Task task : this.taskToStaffToWageMap.keySet()) {

	    // Loop through teams in this task.
	    Map<Team, Map<Staff, String>> taskTeamStaffWageMap = this.taskToTeamToStaffToWageMap
		    .get(task);
	    Map<Team, Double> taskTeamToTotalWageMap = this.mapOfTaskTeamToTotal
		    .get(task);
	    Map<Staff, String> taskStaffMap = this.taskToStaffToWageMap
		    .get(task);

	    // Empty variables.
	    boolean emptyTaskStaff = taskStaffMap.keySet().isEmpty();
	    boolean emptyTaskTeamStaff = taskTeamStaffWageMap.keySet().size() == 0;

	    // Continue if the task -> staff is not empty,
	    // and the task -> team -> staff is not empty.
	    if (emptyTaskStaff && emptyTaskTeamStaff) {
		continue;
	    }

	    // This current task's key.
	    long thisPKey = Math.abs(randomno.nextLong());
	    double thisObjectTotal = this.mapOfTaskToTotal.get(task);
	    String thisObjectTotalStr = this.formatter.format(thisObjectTotal);
	    TreeGridRowBean thisBean = new TreeGridRowBean(thisPKey,
		    headerPKey, CSSClass.INFO.getSpanHTML("TASK",
			    task.getTitle()), thisObjectTotalStr);
	    this.treeGrid.add(thisBean);

	    // Only include this if not empty.
	    if (!emptyTaskTeamStaff) {
		for (Team team : taskTeamStaffWageMap.keySet()) {

		    // Get details.
		    // Add to bean.
		    long teamPKey = Math.abs(randomno.nextLong());
		    double thisTeamTotal = taskTeamToTotalWageMap.get(team);
		    String thisTeamTotalStr = this.formatter
			    .format(thisTeamTotal);
		    TreeGridRowBean teamBean = new TreeGridRowBean(teamPKey,
			    thisPKey, CSSClass.INFO.getSpanHTML("TEAM",
				    team.getName()), thisTeamTotalStr);
		    this.treeGrid.add(teamBean);

		    // Add the staff members inside this team.
		    // Skip if the staff list if empty.
		    Map<Staff, String> teamStaffMap = taskTeamStaffWageMap
			    .get(team);
		    if (teamStaffMap.keySet().isEmpty()) {
			continue;
		    }
		    setStaffListTotalAndBreakdown(teamStaffMap, randomno,
			    teamPKey);
		}
	    }

	    // If staff list is empty, skip it.
	    // Staff list total and breakdown.
	    if (!emptyTaskStaff) {
		setStaffListTotalAndBreakdown(taskStaffMap, randomno, thisPKey);
	    }

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
	this.project = proj;
	this.staffIDsToCompute = DataStructUtils
		.convertArrayToList(projectPayroll.getStaffIDs());

	// Compute.
	compute();
    }

}
