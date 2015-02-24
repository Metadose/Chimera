package com.cebedo.pmsys.field.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.field.dao.FieldDAO;
import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.field.model.FieldAssignment;
import com.cebedo.pmsys.project.dao.ProjectDAO;
import com.cebedo.pmsys.project.model.Project;
import com.cebedo.pmsys.task.dao.TaskDAO;
import com.cebedo.pmsys.task.model.Task;
import com.cebedo.pmsys.task.model.TaskFieldAssignment;

@Service
public class FieldServiceImpl implements FieldService {

	private FieldDAO fieldDAO;
	private ProjectDAO projectDAO;
	private TaskDAO taskDAO;

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

	@Override
	@Transactional
	public void unassignProject(long fieldID, long projID, String label,
			String value) {
		this.fieldDAO.unassignProject(fieldID, projID, label, value);
	}

	@Override
	@Transactional
	public void unassignAllProjects(long projectID) {
		this.fieldDAO.unassignAllProjects(projectID);
	}

	@Override
	@Transactional
	public void updateAssignedProjectField(long projectID, long fieldID,
			String oldLabel, String oldValue, String label, String value) {
		// Delete the old version of the field.
		this.fieldDAO.deleteAssignedField(projectID, fieldID, oldLabel,
				oldValue);

		// Save a new one.
		FieldAssignment newFieldAssignment = new FieldAssignment();
		Field field = this.fieldDAO.getByID(fieldID);
		Project proj = this.projectDAO.getByID(projectID);
		newFieldAssignment.setProject(proj);
		newFieldAssignment.setField(field);
		newFieldAssignment.setLabel(label);
		newFieldAssignment.setValue(value);
		this.fieldDAO.assignProject(newFieldAssignment);
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

}
