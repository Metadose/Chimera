package com.cebedo.pmsys.staff.dao;

import java.util.List;

import com.cebedo.pmsys.staff.model.ManagerAssignment;
import com.cebedo.pmsys.staff.model.Staff;

public interface StaffDAO {

	public void create(Staff staff);

	public Staff getByID(long id);

	public Staff getWithAllCollectionsByID(long id);

	public void update(Staff staff);

	public void delete(long id);

	public List<Staff> list();

	public List<Staff> listWithAllCollections();

	public void assignProjectManager(ManagerAssignment assignment);

	public void unassignProjectManager(long projectID, long staffID);

	public void unassignAllProjectManagers(long projectID);
}
