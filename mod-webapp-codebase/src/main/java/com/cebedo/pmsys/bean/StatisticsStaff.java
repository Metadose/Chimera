package com.cebedo.pmsys.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.cebedo.pmsys.bean.ComparatorValue.ValueOrdering;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.service.AttendanceService;

public class StatisticsStaff extends SummaryStatistics {

    private static final long serialVersionUID = 3100862015017954781L;

    private Project project;
    private Set<Staff> staffList;
    private Set<Attendance> attendances;
    private Map<AttendanceStatus, Map<Staff, Integer>> attendaceMap = new HashMap<AttendanceStatus, Map<Staff, Integer>>();

    private AttendanceService attendanceService;

    @Autowired
    @Qualifier(value = "attendanceService")
    public void setAttendanceService(AttendanceService attendanceService) {
	this.attendanceService = attendanceService;
    }

    public StatisticsStaff() {
	;
    }

    public StatisticsStaff(Project proj, Set<Staff> assignedStaff) {
	this.staffList = assignedStaff;
	this.project = proj;

	for (Staff staff : assignedStaff) {
	    Set<Attendance> attendanceList = this.attendanceService.rangeStaffAttendance(proj, staff);
	    this.attendances.addAll(attendanceList);
	}

	// Initialize for the attendances.
	// Create a map of staff to number of (attendance status).
	for (Attendance attendance : this.attendances) {

	    // Open the map for this status.
	    AttendanceStatus status = attendance.getStatus();
	    Map<Staff, Integer> staffAttendance = this.attendaceMap.get(status);
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

    public Map<Staff, Integer> getTop(AttendanceStatus status) {
	return getSortedAttendance(status, null, ValueOrdering.DESCENDING);
    }

    public Map<Staff, Integer> getTop(AttendanceStatus status, Integer maxCount) {
	return getSortedAttendance(status, maxCount, ValueOrdering.DESCENDING);
    }

    public Map<Staff, Integer> getBottom(AttendanceStatus status) {
	return getSortedAttendance(status, null, ValueOrdering.ASCENDING);
    }

    public Map<Staff, Integer> getBottom(AttendanceStatus status, Integer maxCount) {
	return getSortedAttendance(status, maxCount, ValueOrdering.ASCENDING);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Map<Staff, Integer> getSortedAttendance(AttendanceStatus status, Integer maxCount,
	    ValueOrdering order) {

	// Get map from pre-computed attendances.
	// Sort by count (value).
	Map<Staff, Integer> staffAttendance = this.attendaceMap.get(status);
	ComparatorValue comparator = new ComparatorValue(staffAttendance, order);
	TreeMap sortedMap = new TreeMap(comparator);
	sortedMap.putAll(staffAttendance);

	// If not null, limit the return to specific number.
	if (maxCount != null) {

	    Map<Staff, Integer> returnMap = new HashMap<Staff, Integer>();
	    int topIndex = 0;
	    for (Object key : sortedMap.keySet()) {

		// If has reached the limit, return.
		if (topIndex == maxCount) {
		    return returnMap;
		}
		// Get the value and key.
		// Put to the return map.
		Staff staff = (Staff) key;
		Integer value = (Integer) sortedMap.get(staff);
		returnMap.put(staff, value);
		topIndex++;
	    }
	}
	return staffAttendance;
    }

    public double getMeanOf(AttendanceStatus absent) {
	Map<Staff, Integer> staffCount = getTop(absent);
	for (Integer count : staffCount.values()) {
	    addValue(count);
	}
	double mean = getMean();
	clear();
	return mean;
    }
}
