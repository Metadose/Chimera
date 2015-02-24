package com.cebedo.pmsys.task.model;

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
@Table(name = TaskFieldAssignment.TABLE_NAME)
@AssociationOverrides({
		@AssociationOverride(name = TaskFieldAssignment.PRIMARY_KEY + ".task", joinColumns = @JoinColumn(name = Task.COLUMN_PRIMARY_KEY)),
		@AssociationOverride(name = TaskFieldAssignment.PRIMARY_KEY + ".field", joinColumns = @JoinColumn(name = Field.COLUMN_PRIMARY_KEY)) })
public class TaskFieldAssignment implements Serializable {

	public static final String CLASS_NAME = "TaskFieldAssignment";
	public static final String TABLE_NAME = "assignments_task_field";
	public static final String PRIMARY_KEY = "assignmentID";

	private static final long serialVersionUID = 1L;

	private TaskFieldAssignmentID assignmentID = new TaskFieldAssignmentID();

	@EmbeddedId
	public TaskFieldAssignmentID getAssignmentID() {
		return assignmentID;
	}

	public void setAssignmentID(TaskFieldAssignmentID assignmentID) {
		this.assignmentID = assignmentID;
	}

	@Transient
	public Task getTask() {
		return getAssignmentID().getTask();
	}

	public void setTask(Task task) {
		getAssignmentID().setTask(task);
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
