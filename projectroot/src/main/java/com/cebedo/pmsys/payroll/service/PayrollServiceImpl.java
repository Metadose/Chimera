package com.cebedo.pmsys.payroll.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.payroll.domain.Attendance;
import com.cebedo.pmsys.payroll.domain.AttendanceMass;
import com.cebedo.pmsys.payroll.domain.Status;
import com.cebedo.pmsys.payroll.repository.AttendanceValueRepo;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.project.service.ProjectService;
import com.cebedo.pmsys.staff.model.ManagerAssignment;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.system.helper.DateHelper;
import com.cebedo.pmsys.team.model.Team;

@Service
public class PayrollServiceImpl implements PayrollService {

	private AttendanceValueRepo attendanceValueRepo;
	private ProjectService projectService;

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public void setAttendanceZSetRepo(AttendanceValueRepo r) {
		this.attendanceValueRepo = r;
	}

	@Override
	@Transactional
	public void set(Attendance attendance) {
		Status status = attendance.getStatus();
		if (status == null) {
			attendance.setStatus(Status.of(attendance.getStatusID()));
			status = attendance.getStatus();
		}
		if (status == Status.DELETE) {
			deleteAllInDate(attendance);
			return;
		}
		if (status == Status.ABSENT && attendance.getWage() > 0) {
			attendance.setWage(0);
		}
		if (status != Status.ABSENT && attendance.getWage() == 0) {
			attendance.setWage(getWage(attendance.getStaff(), status));
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
		String key = Attendance.constructKey(attendance.getStaff(),
				attendance.getTimestamp());
		Set<String> keys = this.attendanceValueRepo.keys(key);
		this.attendanceValueRepo.delete(keys);
	}

	@Override
	@Transactional
	public void set(Staff staff, Status status) {
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
	public void set(Staff staff, Status status, Date timestamp) {
		Attendance attendance = new Attendance(staff, status, timestamp);
		double wage = getWage(staff, status);
		attendance.setWage(wage);
		this.attendanceValueRepo.set(attendance);
	}

	private double getWage(Staff staff, Status status) {
		double wage = 0;

		if (status == Status.PRESENT) {
			wage = staff.getWage();

		} else if (status == Status.LEAVE) {
			wage = staff.getWage();

		} else if (status == Status.LATE) {
			wage = staff.getWage();

		} else if (status == Status.ABSENT) {
			wage = 0;

		} else if (status == Status.HALFDAY) {
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
	public double getTotalWageOfStaffInRange(Staff staff, Date min, Date max) {

		// Get all the attendances.
		Set<Attendance> attendances = this
				.rangeStaffAttendance(staff, min, max);

		// Get total wage of all attendances.
		double totalWage = 0;
		for (Attendance attd : attendances) {
			totalWage += attd.getWage();
		}
		return totalWage;
	}

	@Override
	@Transactional
	public Set<Attendance> rangeStaffAttendance(Staff staff, long min, long max) {
		Set<String> keys = this.attendanceValueRepo.keys(Attendance
				.constructKey(staff));
		Set<Attendance> attnSet = new HashSet<Attendance>();
		for (String key : keys) {
			Attendance attn = this.attendanceValueRepo.get(key);

			// TODO Optimize this.
			// Leverage on the Date object. Don't use Timestamp millis.
			long timestamp = attn.getTimestamp().getTime();
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
	public Attendance get(Staff staff, Status status, Date timestamp) {
		return this.attendanceValueRepo.get(Attendance.constructKey(staff,
				timestamp, status));
	}

	@Override
	@Transactional
	public void multiSet(AttendanceMass attendanceMass) {
		Staff staff = attendanceMass.getStaff();
		Status status = Status.of(attendanceMass.getStatusID());
		if (status == Status.ABSENT) {
			attendanceMass.setWage(0);
		}
		double wage = attendanceMass.getWage();

		// Iterate through all dates.
		boolean includeWeekends = attendanceMass.isIncludeWeekends();
		Date startDate = attendanceMass.getStartDate();
		Date endDate = attendanceMass.getEndDate();
		List<Date> dates = DateHelper.getDatesBetweenDates(startDate, endDate);
		Map<String, Attendance> keyAttendanceMap = new HashMap<String, Attendance>();
		for (Date date : dates) {
			Attendance attendance = new Attendance(staff, status, date, wage);

			// Check if date is a weekend.
			int dayOfWeek = DateHelper.getDayOfWeek(date);
			boolean isWeekend = dayOfWeek == Calendar.SATURDAY
					|| dayOfWeek == Calendar.SUNDAY;

			// If status is delete.
			if (status == Status.DELETE) {
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
		if (status != Status.DELETE) {
			this.attendanceValueRepo.multiSet(keyAttendanceMap);
		}
	}

	@Override
	@Transactional
	public List<Attendance> getAllAttendance(Staff staff) {
		Set<String> keys = this.attendanceValueRepo.keys(Attendance
				.constructKey(staff));
		return this.attendanceValueRepo.multiGet(keys);
	}

}
