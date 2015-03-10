package com.cebedo.pmsys.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import com.cebedo.pmsys.field.model.Field;

@Embeddable
public class TaskFieldAssignmentID implements Serializable {

	private static final long serialVersionUID = 1L;

	private Task task;
	private Field field;
	private String label;
	private String value;

	public TaskFieldAssignmentID() {
		;
	}

	public TaskFieldAssignmentID(Task proj, Field field2) {
		setTask(proj);
		setField(field2);
	}

	@ManyToOne
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
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
