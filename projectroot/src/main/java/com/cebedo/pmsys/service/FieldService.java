package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.model.assignment.StaffFieldAssignment;
import com.cebedo.pmsys.model.assignment.TaskFieldAssignment;

public interface FieldService {

    /**
     * Create a new field.
     * 
     * @param field
     * @return
     */
    public String create(Field field);

    public Field getByID(long id);

    /**
     * Update a field.
     * 
     * @param field
     * @return
     */
    public String update(Field field);

    /**
     * Delete a field.
     * 
     * @param id
     * @return
     */
    public String delete(long id);

    public List<Field> list();

    public List<Field> listWithAllCollections();

    /**
     * Assign a new field to a project.
     * 
     * @param fieldAssignment
     * @param fieldID
     * @param projectID
     * @return
     */
    public String assignFieldToProject(FieldAssignment fieldAssignment,
	    long fieldID, long projectID);

    /**
     * Unassign a field from a project.
     * 
     * @param fieldID
     * @param projID
     * @param label
     * @param value
     * @return
     */
    public String unassignFieldFromProject(long fieldID, long projID,
	    String label, String value);

    /**
     * Unassign all field under a project.
     * 
     * @param projectID
     * @return
     */
    public String unassignAllFieldsFromProject(long projectID);

    /**
     * Update an assign project field.
     * 
     * @param projectID
     * @param fieldID
     * @param oldLabel
     * @param oldValue
     * @param label2
     * @param value2
     * @return
     */
    public String updateAssignedProjectField(long projectID, long fieldID,
	    String oldLabel, String oldValue, String label2, String value2);

    /**
     * Assign field to a task.
     * 
     * @param taskField
     * @param fieldID
     * @param taskID
     * @return
     */
    public String assignFieldToTask(TaskFieldAssignment taskField,
	    long fieldID, long taskID);

    /**
     * Unassign all fields from a task.
     * 
     * @param taskID
     * @return
     */
    public String unassignAllFieldsFromTask(long taskID);

    /**
     * Unassign a field from a task.
     * 
     * @param fieldID
     * @param taskID
     * @param label
     * @param value
     * @return
     */
    public String unassignFieldFromTask(long fieldID, long taskID,
	    String label, String value);

    /**
     * Update an assigned task field.
     * 
     * @param taskID
     * @param fieldID
     * @param oldLabel
     * @param oldValue
     * @param label
     * @param value
     * @return
     */
    public String updateAssignedTaskField(long taskID, long fieldID,
	    String oldLabel, String oldValue, String label, String value);

    /**
     * Unassign a field from staff.
     * 
     * @param fieldID
     * @param staffID
     * @param label
     * @param value
     * @return
     */
    public String unassignFieldFromStaff(long fieldID, long staffID,
	    String label, String value);

    /**
     * Assign a field to a staff.
     * 
     * @param fieldAssignment
     * @param fieldID
     * @param staffID
     * @return
     */
    public String assignFieldToStaff(StaffFieldAssignment fieldAssignment,
	    long fieldID, long staffID);

    /**
     * Unassign all fields from a staff.
     * 
     * @param staffID
     * @return
     */
    public String unassignAllFieldsFromStaff(long staffID);

    /**
     * Update an assigned staff field.
     * 
     * @param staffID
     * @param fieldID
     * @param oldLabel
     * @param oldValue
     * @param label
     * @param value
     * @return
     */
    public String updateAssignedStaffField(long staffID, long fieldID,
	    String oldLabel, String oldValue, String label, String value);
}
