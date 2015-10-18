package com.cebedo.pmsys.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.cebedo.pmsys.bean.ComparatorValue.ValueOrdering;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.helper.BeanHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.service.AttendanceService;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class StatisticsStaff extends SummaryStatistics {

    private static final long serialVersionUID = 3100862015017954781L;

    private Project project;
    private Set<Staff> staffList = new HashSet<Staff>();
    private Set<Attendance> attendances = new HashSet<Attendance>();
    private Map<AttendanceStatus, HashMap<Staff, Integer>> attendaceMap = new HashMap<AttendanceStatus, HashMap<Staff, Integer>>();

    private Ordering<Map.Entry<Staff, Integer>> byMapValuesAsc = new Ordering<Map.Entry<Staff, Integer>>() {
	@Override
	public int compare(Map.Entry<Staff, Integer> left, Map.Entry<Staff, Integer> right) {
	    return left.getValue().compareTo(right.getValue());
	}
    };
    private Ordering<Map.Entry<Staff, Integer>> byMapValuesDesc = new Ordering<Map.Entry<Staff, Integer>>() {
	@Override
	public int compare(Map.Entry<Staff, Integer> left, Map.Entry<Staff, Integer> right) {
	    return (left.getValue().compareTo(right.getValue())) * -1;
	}
    };

    public StatisticsStaff() {
	;
    }

    public StatisticsStaff(Project proj, Set<Staff> assignedStaff) {
	this.staffList = assignedStaff;
	this.project = proj;

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

    public double getMeanWage() {
	addValuesWage();
	double mean = getMean();
	clear();
	return mean;
    }

    private void addValuesWage() {
	for (Staff staff : this.staffList) {
	    addValue(staff.getWage());
	}
    }

    public double getSumWage() {
	addValuesWage();
	double sum = getSum();
	clear();
	return sum;
    }

    public ImmutableList<Entry<Staff, Integer>> getTop(AttendanceStatus status) {
	return getSortedAttendance(status, null, ValueOrdering.DESCENDING);
    }

    public ImmutableList<Entry<Staff, Integer>> getTop(AttendanceStatus status, Integer maxCount) {
	return getSortedAttendance(status, maxCount, ValueOrdering.DESCENDING);
    }

    public ImmutableList<Entry<Staff, Integer>> getBottom(AttendanceStatus status) {
	return getSortedAttendance(status, null, ValueOrdering.ASCENDING);
    }

    public ImmutableList<Entry<Staff, Integer>> getBottom(AttendanceStatus status, Integer maxCount) {
	return getSortedAttendance(status, maxCount, ValueOrdering.ASCENDING);
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
	    Integer maxCount, ValueOrdering order) {

	HashMap<Staff, Integer> storedMap = this.attendaceMap.get(status);
	if (storedMap == null) {
	    return FluentIterable.from(new ArrayList<Entry<Staff, Integer>>()).toList();
	}
	ArrayList<Entry<Staff, Integer>> sortedEntries = Lists.newArrayList(storedMap.entrySet());
	Collections.sort(sortedEntries,
		order == ValueOrdering.ASCENDING ? this.byMapValuesAsc : this.byMapValuesDesc);

	// If not null, limit the return to specific number.
	if (maxCount != null) {
	    return FluentIterable.from(sortedEntries).limit(maxCount).toList();
	}
	return FluentIterable.from(sortedEntries).toList();
    }

    /**
     * Get mean count of a given status.
     * 
     * @param status
     * @return
     */
    public double getMeanOf(AttendanceStatus status) {
	ImmutableList<Entry<Staff, Integer>> staffCount = getTop(status);
	for (Entry<Staff, Integer> pair : staffCount) {
	    addValue(pair.getValue());
	}
	double mean = getMean();
	clear();
	return mean;
    }
}
