package com.cebedo.pmsys.staff.service;

import java.util.List;

import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.model.StaffTeamAssignment;

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

}
