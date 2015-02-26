package com.cebedo.pmsys.staff.model;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cebedo.pmsys.field.model.Field;

@Entity
@Table(name = StaffFieldAssignment.TABLE_NAME)
@AssociationOverrides({
		@AssociationOverride(name = StaffFieldAssignment.PRIMARY_KEY + ".staff", joinColumns = @JoinColumn(name = Staff.COLUMN_PRIMARY_KEY)),
		@AssociationOverride(name = StaffFieldAssignment.PRIMARY_KEY + ".field", joinColumns = @JoinColumn(name = Field.COLUMN_PRIMARY_KEY)) })
public class StaffFieldAssignment implements Serializable {

	public static final String CLASS_NAME = "StaffFieldAssignment";
	public static final String TABLE_NAME = "assignments_staff_field";
	public static final String PRIMARY_KEY = "assignmentID";

	private static final long serialVersionUID = 1L;

	private StaffFieldAssignmentID assignmentID = new StaffFieldAssignmentID();

	@EmbeddedId
	public StaffFieldAssignmentID getAssignmentID() {
		return assignmentID;
	}

	public void setAssignmentID(StaffFieldAssignmentID assignmentID) {
		this.assignmentID = assignmentID;
	}

	@Transient
	public Staff getStaff() {
		return getAssignmentID().getStaff();
	}

	public void setStaff(Staff staff) {
		getAssignmentID().setStaff(staff);
	}

	@Transient
	public Field getField() {
		return getAssignmentID().getField();
	}

	public void setField(Field field) {
		getAssignmentID().setField(field);
	}

	@Transient
	public String getLabel() {
		return getAssignmentID().getLabel();
	}

	public void setLabel(String str) {
		getAssignmentID().setLabel(str);
	}

	@Transient
	public String getValue() {
		return getAssignmentID().getValue();
	}

	public void setValue(String str) {
		getAssignmentID().setValue(str);
	}
}
