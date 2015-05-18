package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.FieldDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.TaskDAO;
import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.model.assignment.StaffFieldAssignment;
import com.cebedo.pmsys.model.assignment.TaskFieldAssignment;

@Service
public class FieldServiceImpl implements FieldService {

	private FieldDAO fieldDAO;
	private ProjectDAO projectDAO;
	private TaskDAO taskDAO;
	private StaffDAO staffDAO;

	public void setStaffDAO(StaffDAO staffDAO) {
		this.staffDAO = staffDAO;
	}

	public void setTaskDAO(TaskDAO taskDAO) {
		this.taskDAO = taskDAO;
	}

	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public void setFieldDAO(FieldDAO fieldDAO) {
		this.fieldDAO = fieldDAO;
	}

	@Override
	@Transactional
	public void create(Field field) {
		this.fieldDAO.create(field);
	}

	@Override
	@Transactional
	public Field getByID(long id) {
		return this.fieldDAO.getByID(id);
	}

	@Override
	@Transactional
	public void update(Field field) {
		this.fieldDAO.update(field);
	}

	@Override
	@Transactional
	public void delete(long id) {
		this.fieldDAO.delete(id);
	}

	@Override
	@Transactional
	public List<Field> list() {
		return this.fieldDAO.list();
	}

	@Override
	@Transactional
	public List<Field> listWithAllCollections() {
		return this.fieldDAO.listWithAllCollections();
	}

	@CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
	@Override
	@Transactional
	public void assignProject(FieldAssignment fieldAssignment, long fieldID,
			long projectID) {
		Field field = this.fieldDAO.getByID(fieldID);
		Project proj = this.projectDAO.getByID(projectID);
		fieldAssignment.setField(field);
		fieldAssignment.setProject(proj);
		this.fieldDAO.assignProject(fieldAssignment);
	}

	@CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
	@Override
	@Transactional
	public void unassignProject(long fieldID, long projectID, String label,
			String value) {
		this.fieldDAO.unassignProject(fieldID, projectID, label, value);
	}

	@CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
	@Override
	@Transactional
	public void unassignAllProjects(long projectID) {
		this.fieldDAO.unassignAllProjects(projectID);
	}

	@CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#projectID")
	@Override
	@Transactional
	public void updateAssignedProjectField(long projectID, long fieldID,
			String oldLabel, String oldValue, String label, String value) {
		this.fieldDAO.updateAssignedField(FieldAssignment.TABLE_NAME,
				Project.COLUMN_PRIMARY_KEY, projectID, fieldID, oldLabel,
				oldValue, label, value);
	}

	@Override
	@Transactional
	public void assignTask(TaskFieldAssignment taskField, long fieldID,
			long taskID) {
		Field field = this.fieldDAO.getByID(fieldID);
		Task task = this.taskDAO.getByID(taskID);
		taskField.setField(field);
		taskField.setTask(task);
		this.fieldDAO.assignTask(taskField);
	}

	@Override
	@Transactional
	public void unassignAllTasks(long taskID) {
		this.fieldDAO.unassignAllTasks(taskID);
	}

	@Override
	@Transactional
	public void unassignTask(long fieldID, long taskID, String label,
			String value) {
		this.fieldDAO.unassignTask(fieldID, taskID, label, value);
	}

	@Override
	@Transactional
	public void updateAssignedTaskField(long taskID, long fieldID,
			String oldLabel, String oldValue, String label, String value) {
		// Delete the old version of the field.
		this.fieldDAO.unassignTask(fieldID, taskID, oldLabel, oldValue);

		// Save a new one.
		TaskFieldAssignment newFieldAssignment = new TaskFieldAssignment();
		Field field = this.fieldDAO.getByID(fieldID);
		Task task = this.taskDAO.getByID(taskID);
		newFieldAssignment.setTask(task);
		newFieldAssignment.setField(field);
		newFieldAssignment.setLabel(label);
		newFieldAssignment.setValue(value);
		this.fieldDAO.assignTask(newFieldAssignment);
	}

	@Override
	@Transactional
	public void unassignStaff(long fieldID, long staffID, String label,
			String value) {
		this.fieldDAO.unassignStaff(fieldID, staffID, label, value);
	}

	@Override
	@Transactional
	public void assignStaff(StaffFieldAssignment fieldAssignment, long fieldID,
			long staffID) {
		Field field = this.fieldDAO.getByID(fieldID);
		Staff staff = this.staffDAO.getByID(staffID);
		fieldAssignment.setField(field);
		fieldAssignment.setStaff(staff);
		this.fieldDAO.assignStaff(fieldAssignment);
	}

	@Override
	@Transactional
	public void unassignAllStaff(long staffID) {
		this.fieldDAO.unassignAllStaff(staffID);
	}

	@Override
	@Transactional
	public void updateAssignedStaffField(long staffID, long fieldID,
			String oldLabel, String oldValue, String label, String value) {
		// Delete the old version of the field.
		this.fieldDAO.unassignStaff(fieldID, staffID, oldLabel, oldValue);

		// Save a new one.
		StaffFieldAssignment newFieldAssignment = new StaffFieldAssignment();
		Field field = this.fieldDAO.getByID(fieldID);
		Staff staff = this.staffDAO.getByID(staffID);
		newFieldAssignment.setStaff(staff);
		newFieldAssignment.setField(field);
		newFieldAssignment.setLabel(label);
		newFieldAssignment.setValue(value);
		this.fieldDAO.assignStaff(newFieldAssignment);
	}
}
