package com.cebedo.pmsys.staff.dao;

import java.util.List;

import com.cebedo.pmsys.staff.model.Staff;

public interface StaffDAO {

	public void create(Staff staff);

	public Staff getByID(long id);

	public void update(Staff staff);

	public void delete(long id);

	public List<Staff> list();

	public List<Staff> listWithAllCollections();
}
