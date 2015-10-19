package com.cebedo.pmsys.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.enums.SortOrder;
import com.cebedo.pmsys.helper.BeanHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.service.AttendanceService;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class StatisticsStaff extends SummaryStatistics {

    private static final long serialVersionUID = 3100862015017954781L;

    private Set<Staff> staffList = new HashSet<Staff>();
    private Set<Attendance> attendances = new HashSet<Attendance>();
    private Map<AttendanceStatus, HashMap<Staff, Integer>> attendaceMap = new HashMap<AttendanceStatus, HashMap<Staff, Integer>>();
    private ComparatorMapEntry mapEntryComparator = new ComparatorMapEntry();

    public StatisticsStaff() {
	;
    }

    public StatisticsStaff(Project proj, Set<Staff> assignedStaff) {

	// Set the staff list.
	this.staffList = assignedStaff;

	// Add all attendances of staff members.
	BeanHelper beanHelper = new BeanHelper();
	AttendanceService attendanceService = (AttendanceService) beanHelper
		.getBean("attendanceService");
	for (Staff staff : assignedStaff) {
	    Set<Attendance> attendanceList = attendanceService.rangeStaffAttendance(proj, staff);
	    this.attendances.addAll(attendanceList);
	}

	// Initialize for the attendances.
	// Create a map of staff to number of (attendance status).
	for (Attendance attendance : this.attendances) {

	    // Open the map for this status.
	    AttendanceStatus status = attendance.getStatus();
	    HashMap<Staff, Integer> staffAttendance = this.attendaceMap.get(status);
	    Staff staffMember = attendance.getStaff();

	    // If first time.
	    if (staffAttendance == null) {
		staffAttendance = new HashMap<Staff, Integer>();
		staffAttendance.put(staffMember, 1);
	    } else {
		Integer count = staffAttendance.get(staffMember);
		staffAttendance.put(staffMember, count == null ? 1 : count + 1);
	    }
	    this.attendaceMap.put(status, staffAttendance);
	}
    }

    /**
     * Get the mean wage of all staff members.
     * 
     * @return
     */
    public double getMeanWage() {
	addValuesWage();
	double mean = getMean();
	clear();
	return mean;
    }

    /**
     * Add all wage values.
     */
    private void addValuesWage() {
	for (Staff staff : this.staffList) {
	    addValue(staff.getWage());
	}
    }

    /**
     * Get the sum of all staff member's wage.
     * 
     * @return
     */
    public double getSumWage() {
	addValuesWage();
	double sum = getSum();
	clear();
	return sum;
    }

    public ImmutableList<Entry<Staff, Integer>> getAllAttendancesByStatusDesc(AttendanceStatus status) {
	return getSortedAttendance(status, null, SortOrder.DESCENDING);
    }

    public ImmutableList<Entry<Staff, Integer>> getAttendancesByStatusDesc(AttendanceStatus status,
	    Integer maxCount) {
	return getSortedAttendance(status, maxCount, SortOrder.DESCENDING);
    }

    public ImmutableList<Entry<Staff, Integer>> getAllAttendancesByStatusAsc(AttendanceStatus status) {
	return getSortedAttendance(status, null, SortOrder.ASCENDING);
    }

    public ImmutableList<Entry<Staff, Integer>> getAttendancesByStatusAsc(AttendanceStatus status,
	    Integer maxCount) {
	return getSortedAttendance(status, maxCount, SortOrder.ASCENDING);
    }

    /**
     * Given the attendance status, get the list ordered in descending or
     * ascending, limited by max number of counts.
     * 
     * @param status
     * @param maxCount
     * @param order
     * @return
     */
    private ImmutableList<Entry<Staff, Integer>> getSortedAttendance(AttendanceStatus status,
	    Integer maxCount, SortOrder order) {

	HashMap<Staff, Integer> storedMap = this.attendaceMap.get(status);

	// If empty map.
	if (storedMap == null) {
	    return FluentIterable.from(new ArrayList<Entry<Staff, Integer>>()).toList();
	}

	// Sort.
	ArrayList<Entry<Staff, Integer>> sortedEntries = Lists.newArrayList(storedMap.entrySet());
	Collections.sort(sortedEntries, this.mapEntryComparator.setOrder(order));

	// If not null, limit the return to specific number.
	if (maxCount != null) {
	    return FluentIterable.from(sortedEntries).limit(maxCount).toList();
	}

	// Return all sorted entries.
	return FluentIterable.from(sortedEntries).toList();
    }

    /**
     * Get list of unsorted attendances.
     * 
     * @param status
     * @param maxCount
     * @return
     */
    private ImmutableList<Entry<Staff, Integer>> getUnsortedAttendance(AttendanceStatus status) {
	return getUnsortedAttendance(status, null);
    }

    /**
     * Get list of unsorted attendances.
     * 
     * @param status
     * @param maxCount
     * @return
     */
    private ImmutableList<Entry<Staff, Integer>> getUnsortedAttendance(AttendanceStatus status,
	    Integer maxCount) {

	HashMap<Staff, Integer> storedMap = this.attendaceMap.get(status);

	// If empty map.
	if (storedMap == null) {
	    return FluentIterable.from(new ArrayList<Entry<Staff, Integer>>()).toList();
	}

	// Sort.
	ArrayList<Entry<Staff, Integer>> unsortedEntries = Lists.newArrayList(storedMap.entrySet());

	// If not null, limit the return to specific number.
	if (maxCount != null) {
	    return FluentIterable.from(unsortedEntries).limit(maxCount).toList();
	}

	// Return all sorted entries.
	return FluentIterable.from(unsortedEntries).toList();
    }

    /**
     * Get mean count of a given status.
     * 
     * @param status
     * @return
     */
    public double getMeanOf(AttendanceStatus status) {
	ImmutableList<Entry<Staff, Integer>> staffCount = getUnsortedAttendance(status);
	for (Entry<Staff, Integer> pair : staffCount) {
	    addValue(pair.getValue());
	}
	double mean = getMean();
	clear();
	return mean;
    }
}
