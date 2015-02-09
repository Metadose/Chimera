package com.cebedo.pmsys.field.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = FieldAssignment.TABLE_NAME)
public class FieldAssignment implements Serializable {

	public static final String TABLE_NAME = "assignments_field";

	private static final long serialVersionUID = 1L;

	private String label;
	private String value;

	@Column(name = "label", nullable = false, length = 32)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Column(name = "value", nullable = false)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
