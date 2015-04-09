package com.cebedo.pmsys.staff.dao;

import java.util.List;

import com.cebedo.pmsys.staff.model.ManagerAssignment;
import com.cebedo.pmsys.staff.model.Staff;
import com.cebedo.pmsys.staff.model.StaffTeamAssignment;

public interface StaffDAO {

	public void create(Staff staff);

	public Staff getByID(long id);

	public Staff getWithAllCollectionsByID(long id);

	public void update(Staff staff);

	public void delete(long id);

	public List<Staff> list(Long companyID);

	public List<Staff> listWithAllCollections(Long companyID);

	public void assignProjectManager(ManagerAssignment assignment);

	public void unassignProjectManager(long projectID, long staffID);

	public void unassignAllProjectManagers(long projectID);

	public void unassignTeam(long teamID, long staffID);

	public void unassignAllTeams(long staffID);

	public void assignTeam(StaffTeamAssignment stAssign);

	public String getNameByID(long staffID);

	public List<Staff> listStaffFromCache(Long companyID);

}
