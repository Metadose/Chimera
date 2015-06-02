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
    public String assignProject(FieldAssignment fieldAssignment, long fieldID,
	    long projectID);

    /**
     * Unassign a field from a project.
     * 
     * @param fieldID
     * @param projID
     * @param label
     * @param value
     * @return
     */
    public String unassignProject(long fieldID, long projID, String label,
	    String value);

    /**
     * Unassign all field under a project.
     * 
     * @param projectID
     * @return
     */
    public String unassignAllProjects(long projectID);

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
