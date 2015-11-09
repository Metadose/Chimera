package com.cebedo.pmsys.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.assignment.FieldAssignment;

public interface FieldService {

    public Field getByName(String name);

    /**
     * Assign a field to a project.
     * 
     * @param result
     */
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'CONTRACT_CREATE')")
    public String assignField(FieldAssignment fieldAssignment, long fieldID, long projectID,
	    BindingResult result);

    /**
     * Unassign a field from a project.
     */
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'CONTRACT_DELETE')")
    public String unassignField(long fieldID, long projectID, String label, String value);

    /**
     * Unassign all fields from a project.
     */
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'CONTRACT_DELETE')")
    public String unassignAllFields(long projectID);

    /**
     * Update an assign project field.
     * 
     * @param result
     */
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'CONTRACT_UPDATE')")
    public String updateField(long projectID, long fieldID, String oldLabel, String oldValue,
	    String label, String value, BindingResult result);

    // Used only by a system call, no need to pre-authorize.
    public void create(Field newField);
}
