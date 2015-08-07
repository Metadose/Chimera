package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.assignment.FieldAssignment;

public interface FieldService {

    /**
     * Assign a field to a project.
     */
    public String assignFieldToProject(FieldAssignment fieldAssignment, long fieldID, long projectID);

    /**
     * Unassign a field from a project.
     */
    public String unassignFieldFromProject(long fieldID, long projectID, String label, String value);

    /**
     * Unassign all fields from a project.
     */
    public String unassignAllFieldsFromProject(long projectID);

    /**
     * Update an assign project field.
     */
    public String updateAssignedProjectField(long projectID, long fieldID, String oldLabel,
	    String oldValue, String label, String value);
}
