package com.cebedo.pmsys.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.enums.Status;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.assignment.StaffTeamAssignment;

public interface StaffService {

    public void create(Staff staff);

    public Staff getByID(long id);

    public void update(Staff staff);

    public void delete(long id);

    public List<Staff> list();

    public List<Staff> list(Long companyID);

    public List<Staff> listWithAllCollections();

    public void assignProjectManager(long projectID, long staffID,
	    String position);

    public void unassignProjectManager(long projectID, long staffID);

    public void unassignAllProjectManagers(long projectID);

    public Staff getWithAllCollectionsByID(long id);

    public void unassignTeam(long teamID, long staffID);

    public void unassignAllTeams(long staffID);

    public void assignTeam(StaffTeamAssignment stAssign);

    public List<Staff> listUnassignedInProject(Long companyID, Project project);

    public String getNameByID(long staffID);

    public void createFromOrigin(Staff staff, String origin, String originID);

    public String getCalendarJSON(Staff staff, Set<Attendance> attendanceList);

    public String getGanttJSON(Staff staff);

    public Map<TaskStatus, Integer> getTaskStatusCountMap(Staff staff);

    public Map<Status, Map<String, Double>> getAttendanceStatusCountMap(
	    Staff staff, Set<Attendance> attendanceList);

}
