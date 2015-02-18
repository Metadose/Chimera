package com.cebedo.pmsys.field.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.cebedo.pmsys.project.model.Project;

@Embeddable
public class FieldAssignmentID implements Serializable {

	private static final long serialVersionUID = 1L;

	private Project project;
	private Field field;
	private String label;
	private String value;

	public FieldAssignmentID() {
		;
	}

	public FieldAssignmentID(Project proj, Field field2) {
		setProject(proj);
		setField(field2);
	}

	@ManyToOne(cascade = CascadeType.ALL)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(cascade = CascadeType.ALL)
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
