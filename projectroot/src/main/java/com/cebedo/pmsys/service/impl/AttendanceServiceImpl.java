package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.MassAttendanceBean;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;
import com.cebedo.pmsys.repository.AttendanceValueRepo;
import com.cebedo.pmsys.service.AttendanceService;
import com.cebedo.pmsys.service.ProjectService;
import com.cebedo.pmsys.utils.DateUtils;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private AttendanceValueRepo attendanceValueRepo;
    private ProjectService projectService;

    public void setStaffDAO(StaffDAO staffDAO) {
    }

    public void setProjectService(ProjectService projectService) {
	this.projectService = projectService;
    }

    public void setAttendanceValueRepo(AttendanceValueRepo r) {
	this.attendanceValueRepo = r;
    }

    @Override
    @Transactional
    public void set(Attendance attendance) {
	if (attendance.getStatus() == null) {
	    attendance.setStatus(AttendanceStatus.of(attendance.getStatusID()));
	}
	AttendanceStatus status = attendance.getStatus();

	if (status == AttendanceStatus.DELETE) {
	    deleteAllInDate(attendance);
	    return;
	}
	if (status == AttendanceStatus.ABSENT && attendance.getWage() > 0) {
	    attendance.setWage(0);
	}
	if (status != AttendanceStatus.ABSENT && attendance.getWage() == 0) {
	    Staff staff = attendance.getStaff();
	    attendance.setWage(getWage(staff, status));
	}
	// Delete all previously declared attendance in this date.
	deleteAllInDate(attendance);
	this.attendanceValueRepo.set(attendance);
    }

    /**
     * Delete all previously declared attendance in this date.
     * 
     * @param attendance
     */
    public void deleteAllInDate(Attendance attendance) {
	// Delete all previously declared attendance in this date.
	Staff staff = attendance.getStaff();
	String key = Attendance.constructPattern(staff, attendance.getDate());
	Set<String> keys = this.attendanceValueRepo.keys(key);
	this.attendanceValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void set(Staff staff, AttendanceStatus status) {
	Attendance attendance = new Attendance(staff, status);
	double wage = getWage(staff, status);
	attendance.setWage(wage);
	this.attendanceValueRepo.set(attendance);

	// Clear all cache associated with this staff.
	Set<ManagerAssignment> manAssigns = staff.getAssignedManagers();
	if (!manAssigns.isEmpty()) {
	    for (ManagerAssignment assignment : manAssigns) {
		this.projectService.clearProjectCache(assignment.getProject()
			.getId());
	    }
	}
    }

    @Override
    @Transactional
    public void set(Staff staff, AttendanceStatus status, Date timestamp) {
	Attendance attendance = new Attendance(staff, status, timestamp);
	double wage = getWage(staff, status);
	attendance.setWage(wage);
	this.attendanceValueRepo.set(attendance);
    }

    private double getWage(Staff staff, AttendanceStatus status) {
	double wage = 0;

	if (status == AttendanceStatus.PRESENT) {
	    wage = staff.getWage();

	} else if (status == AttendanceStatus.LEAVE) {
	    wage = staff.getWage();

	} else if (status == AttendanceStatus.LATE) {
	    wage = staff.getWage();

	} else if (status == AttendanceStatus.ABSENT) {
	    wage = 0;

	} else if (status == AttendanceStatus.HALFDAY) {
	    wage = staff.getWage() / 2;
	}
	return wage;
    }

    @Override
    @Transactional
    public double getTotalWageOfTeamInRange(Team team, Date min, Date max) {

	// Get all members in a team.
	Set<Staff> members = team.getMembers();

	// Compute total wage of all members in team.
	double totalWage = 0;
	for (Staff member : members) {
	    double staffWage = getTotalWageOfStaffInRange(member, min, max);
	    totalWage += staffWage;
	}
	return totalWage;
    }

    @Transactional
    @Override
    public double getTotalWageOfProjectInRange(Project project, Date min,
	    Date max) {

	// List of staff IDs that were already computed.
	// Overall total.
	List<Long> alreadyComputedStaff = new ArrayList<Long>();
	double projectTotal = 0;

	// TODO Get salary of all in team.
	project.getAssignedTeams();

	// Get salary of all managers.
	Set<ManagerAssignment> managerAssigns = project.getManagerAssignments();
	for (ManagerAssignment assign : managerAssigns) {

	    // If the staff has already been computed.
	    Staff stf = assign.getManager();
	    long staffID = stf.getId();
	    if (alreadyComputedStaff.contains(staffID)) {
		continue;
	    }

	    // Else, compute, add to grand total, add to computed list.
	    double stfWage = getTotalWageOfStaffInRange(stf, min, max);
	    projectTotal += stfWage;
	    alreadyComputedStaff.add(staffID);
	}

	return projectTotal;
    }

    @Transactional
    @Override
    public double getTotalWageFromAttendance(Collection<Attendance> attendances) {
	// Get total wage of all attendances.
	double totalWage = 0;
	for (Attendance attd : attendances) {
	    totalWage += attd.getWage();
	}
	return totalWage;
    }

    @Transactional
    @Override
    public double getTotalWageOfStaffInRange(Staff staff, Date min, Date max) {

	// Get all the attendances.
	Set<Attendance> attendances = this
		.rangeStaffAttendance(staff, min, max);

	// Get total wage of all attendances.
	return getTotalWageFromAttendance(attendances);
    }

    @Override
    @Transactional
    public Set<Attendance> rangeStaffAttendance(Staff staff, long min, long max) {
	Set<String> keys = this.attendanceValueRepo.keys(Attendance
		.constructPattern(staff));
	Set<Attendance> attnSet = new HashSet<Attendance>();
	for (String key : keys) {
	    Attendance attn = this.attendanceValueRepo.get(key);

	    // TODO Optimize this.
	    // Leverage on the Date object. Don't use Timestamp millis.
	    long timestamp = attn.getDate().getTime();
	    if (timestamp <= max && timestamp >= min) {
		attnSet.add(attn);
	    }
	}
	return attnSet;
    }

    @Override
    @Transactional
    public Set<Attendance> rangeStaffAttendance(Staff staff, Date min, Date max) {
	return this.rangeStaffAttendance(staff, min.getTime(), max.getTime());
    }

    @Override
    @Transactional
    public Attendance get(Staff staff, AttendanceStatus status, Date timestamp) {
	return this.attendanceValueRepo.get(Attendance.constructKey(staff,
		timestamp, status));
    }

    @Override
    @Transactional
    public void multiSet(MassAttendanceBean attendanceMass) {
	Staff staff = attendanceMass.getStaff();
	AttendanceStatus status = AttendanceStatus.of(attendanceMass
		.getStatusID());
	if (status == AttendanceStatus.ABSENT) {
	    attendanceMass.setWage(0);
	}
	double wage = attendanceMass.getWage();

	// Iterate through all dates.
	boolean includeWeekends = attendanceMass.isIncludeWeekends();
	Date startDate = attendanceMass.getStartDate();
	Date endDate = attendanceMass.getEndDate();
	List<Date> dates = DateUtils.getDatesBetweenDates(startDate, endDate);
	Map<String, Attendance> keyAttendanceMap = new HashMap<String, Attendance>();
	for (Date date : dates) {
	    Company co = staff.getCompany();
	    Attendance attendance = new Attendance(co, staff, status, date,
		    wage);

	    // Check if date is a weekend.
	    int dayOfWeek = DateUtils.getDayOfWeek(date);
	    boolean isWeekend = dayOfWeek == Calendar.SATURDAY
		    || dayOfWeek == Calendar.SUNDAY;

	    // If status is delete.
	    if (status == AttendanceStatus.DELETE) {
		if (!includeWeekends && isWeekend) {
		    continue;
		}
		deleteAllInDate(attendance);
		continue;
	    }

	    deleteAllInDate(attendance);
	    if (!includeWeekends && isWeekend) {
		continue;
	    }
	    keyAttendanceMap.put(attendance.getKey(), attendance);
	}
	if (status != AttendanceStatus.DELETE) {
	    this.attendanceValueRepo.multiSet(keyAttendanceMap);
	}
    }

    @Override
    @Transactional
    public List<Attendance> getAllAttendance(Staff staff) {
	Set<String> keys = this.attendanceValueRepo.keys(Attendance
		.constructPattern(staff));
	return this.attendanceValueRepo.multiGet(keys);
    }

}
