package com.cebedo.pmsys.payroll.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.cebedo.pmsys.payroll.domain.Attendance;
import com.cebedo.pmsys.payroll.domain.AttendanceMass;
import com.cebedo.pmsys.payroll.domain.Status;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.team.model.Team;

public interface PayrollService {

	/**
	 * Add a new attendance object.
	 * 
	 * @param obj
	 */
	public void set(Staff staff, Status status);

	public void set(Attendance attendance);

	public void set(Staff staff, Status status, Date timestamp);

	public Attendance get(Staff staff, Status status, Date timestamp);

	public List<Attendance> getAllAttendance(Staff staff);

	/**
	 * Get attendances of a staff in a range of time.
	 * 
	 * @param staff
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<Attendance> rangeStaffAttendance(Staff staff, Date min, Date max);

	public Set<Attendance> rangeStaffAttendance(Staff staff, long min, long max);

	/**
	 * Get the total wage of all staff in a team in a range of dates.
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public double getTotalWageOfTeamInRange(Team team, Date min, Date max);

	/**
	 * Get the total wage of all staff in a project in a range of dates.
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public double getTotalWageOfProjectInRange(Project project, Date min,
			Date max);

	/**
	 * Get the total wage of a staff in a range of dates.
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public double getTotalWageOfStaffInRange(Staff staff, Date min, Date max);

	public void multiSet(AttendanceMass attendanceMass);
}
