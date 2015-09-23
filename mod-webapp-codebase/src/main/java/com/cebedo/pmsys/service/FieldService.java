package com.cebedo.pmsys.service;

import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.model.assignment.FieldAssignment;

public interface FieldService {

    /**
     * Assign a field to a project.
     * 
     * @param result
     */
    public String assignField(FieldAssignment fieldAssignment, long fieldID, long projectID,
	    BindingResult result);

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
     * 
     * @param result
     */
    public String updateField(long projectID, long fieldID, String oldLabel, String oldValue,
	    String label, String value, BindingResult result);
}
