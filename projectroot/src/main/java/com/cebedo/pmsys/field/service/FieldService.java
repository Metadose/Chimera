package com.cebedo.pmsys.field.service;

import java.util.List;

import com.cebedo.pmsys.field.model.Field;
import com.cebedo.pmsys.field.model.FieldAssignment;
import com.cebedo.pmsys.staff.model.StaffFieldAssignment;
import com.cebedo.pmsys.task.model.TaskFieldAssignment;

public interface FieldService {

	public void create(Field field);

	public Field getByID(long id);

	public void update(Field field);

	public void delete(long id);

	public List<Field> list();

	public List<Field> listWithAllCollections();

	public void assignProject(FieldAssignment fieldAssignment, long fieldID,
			long projectID);

	public void unassignProject(long fieldID, long projID, String label,
			String value);

	public void unassignAllProjects(long projectID);

	public void updateAssignedProjectField(long projectID, long fieldID,
			String oldLabel, String oldValue, String label2, String value2);

	public void assignTask(TaskFieldAssignment taskField, long fieldID,
			long taskID);

	public void unassignAllTasks(long taskID);

	public void unassignTask(long fieldID, long taskID, String label,
			String value);

	public void updateAssignedTaskField(long taskID, long fieldID,
			String oldLabel, String oldValue, String label, String value);

	public void unassignStaff(long fieldID, long staffID, String label,
			String value);

	public void assignStaff(StaffFieldAssignment fieldAssignment, long fieldID,
			long staffID);

	public void unassignAllStaff(long staffID);

	public void updateAssignedStaffField(long staffID, long fieldID,
			String oldLabel, String oldValue, String label, String value);
}
