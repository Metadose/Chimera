package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.cebedo.pmsys.bean.MassAttendanceBean;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.enums.Status;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Team;

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

	public double getTotalWageFromAttendance(Collection<Attendance> attendances);

	public void multiSet(MassAttendanceBean attendanceMass);
}
