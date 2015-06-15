package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.cebedo.pmsys.model.Field;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

@Embeddable
public class StaffFieldAssignmentID implements Serializable {

    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("StaffFieldAssignmentID");

    private Staff staff;
    private Field field;
    private String label;
    private String value;

    public StaffFieldAssignmentID() {
	;
    }

    public StaffFieldAssignmentID(Staff staff, Field field2) {
	setStaff(staff);
	setField(field2);
    }

    @ManyToOne
    public Staff getStaff() {
	return staff;
    }

    public void setStaff(Staff staff) {
	this.staff = staff;
    }

    @ManyToOne
    public Field getField() {
	return field;
    }

    public void setField(Field field) {
	this.field = field;
    }

    @Column(name = "label", nullable = false, length = 32)
    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    @Column(name = "value", nullable = false, length = 255)
    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

}
