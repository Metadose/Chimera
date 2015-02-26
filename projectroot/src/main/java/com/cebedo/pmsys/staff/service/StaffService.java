package com.cebedo.pmsys.staff.service;

import java.util.List;

import com.cebedo.pmsys.staff.model.Staff;

public interface StaffService {

	public void create(Staff staff);

	public Staff getByID(long id);

	public void update(Staff staff);

	public void delete(long id);

	public List<Staff> list();

	public List<Staff> listWithAllCollections();

	public void assignProjectManager(long projectID, long staffID,
			String position);

	public void unassignProjectManager(long projectID, long staffID);

	public void unassignAllProjectManagers(long projectID);

	public Staff getWithAllCollectionsByID(int id);

	public void unassignTeam(long teamID, long staffID);

	public void unassignAllTeams(long staffID);

}
