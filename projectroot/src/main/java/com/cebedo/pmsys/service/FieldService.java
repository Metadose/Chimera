package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.assignment.FieldAssignment;

public interface FieldService {

    /**
     * Assign a field to a project.
     */
    public String assignField(FieldAssignment fieldAssignment, long fieldID, long projectID);

    /**
     * Unassign a field from a project.
     */
    public String unassignField(long fieldID, long projectID, String label, String value);

    /**
     * Unassign all fields from a project.
     */
    public String unassignAllFields(long projectID);

    /**
     * Update an assign project field.
     */
    public String updateField(long projectID, long fieldID, String oldLabel,
	    String oldValue, String label, String value);
}
