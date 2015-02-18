package com.cebedo.pmsys.staff.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.staff.dao.StaffDAO;
import com.cebedo.pmsys.staff.model.ManagerAssignment;
import com.cebedo.pmsys.staff.model.Staff;

@Service
public class StaffServiceImpl implements StaffService {

	private StaffDAO staffDAO;
	private ProjectDAO projectDAO;

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

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

	@Override
	@Transactional
	public List<Staff> listWithAllCollections() {
		return this.staffDAO.listWithAllCollections();
	}

	@Override
	@Transactional
	public void assignProjectManager(long projectID, long staffID,
			String position) {
		Project project = this.projectDAO.getByID(projectID);
		Staff staff = this.staffDAO.getByID(staffID);
		ManagerAssignment assignment = new ManagerAssignment();
		assignment.setProject(project);
		assignment.setManager(staff);
		assignment.setProjectPosition(position);
		this.staffDAO.assignProjectManager(assignment);
	}

}
