package com.cebedo.pmsys.staff.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.staff.dao.StaffDAO;
import com.cebedo.pmsys.staff.model.Staff;

@Service
public class StaffServiceImpl implements StaffService {

	private StaffDAO staffDAO;

	public void setStaffDAO(StaffDAO staffDAO) {
		this.staffDAO = staffDAO;
	}

	@Override
	@Transactional
	public void create(Staff staff) {
		this.staffDAO.create(staff);
	}

	@Override
	@Transactional
	public Staff getByID(long id) {
		return this.staffDAO.getByID(id);
	}

	@Override
	@Transactional
	public void update(Staff staff) {
		this.staffDAO.update(staff);
	}

	@Override
	@Transactional
	public void delete(long id) {
		this.staffDAO.delete(id);
	}

	@Override
	@Transactional
	public List<Staff> list() {
		return this.staffDAO.list();
	}

}
