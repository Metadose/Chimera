package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.model.assignment.StaffFieldAssignment;
import com.cebedo.pmsys.model.assignment.TaskFieldAssignment;

public interface FieldDAO {

	public void create(Field field);

	public Field getByID(long id);

	public void update(Field field);

	public void delete(long id);

	public List<Field> list();

	public List<Field> listWithAllCollections();

	public void assignFieldToProject(FieldAssignment fieldAssignment);

	public void unassignFieldFromProject(long fieldID, long projID, String label,
			String value);

	public void unassignAllFieldsFromProject(long projectID);

	public FieldAssignment getFieldByKeys(long projectID, long fieldID,
			String label, String value);

	public void deleteAssignedField(long projectID, long fieldID, String label,
			String value);

	public void assignFieldToTask(TaskFieldAssignment taskField);

	public void unassignAllFieldsFromTask(long taskID);

	public void unassignFieldFromTask(long fieldID, long taskID, String label,
			String value);

	public void unassignFieldFromStaff(long fieldID, long staffID, String label,
			String value);

	public void assignFieldToStaff(StaffFieldAssignment fieldAssignment);

	public void unassignAllFieldsFromStaff(long staffID);

	public void updateAssignedProjectField(FieldAssignment assignment);

	public void updateAssignedField(String table, String objectKeyCol,
			long objectID, long fieldID, String oldLabel, String oldValue,
			String label, String value);

}
