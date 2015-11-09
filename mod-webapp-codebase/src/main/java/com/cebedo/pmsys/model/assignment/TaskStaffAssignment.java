package com.cebedo.pmsys.model.assignment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cebedo.pmsys.base.IObjectAssignment;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;

@Entity
@Table(name = TaskStaffAssignment.TABLE_NAME)
public class TaskStaffAssignment implements IObjectAssignment {

    private static final long serialVersionUID = -3860362688425526255L;
    public static final String TABLE_NAME = "assignments_task_staff";
    public static final String OBJECT_NAME = "TaskStaffAssignment";

    public static final String PROPERTY_TASK_ID = "taskID";
    public static final String PROPERTY_STAFF_ID = "staffID";

    private long taskID;
    private long staffID;

    @Id
    @Column(name = Task.COLUMN_PRIMARY_KEY, nullable = false)
    public long getTaskID() {
	return taskID;
    }

    public void setTaskID(long taskID) {
	this.taskID = taskID;
    }

    @Id
    @Column(name = Staff.COLUMN_PRIMARY_KEY, nullable = false)
    public long getStaffID() {
	return staffID;
    }

    public void setStaffID(long staffID) {
	this.staffID = staffID;
    }

    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof TaskStaffAssignment)) {
	    return false;
	}
	long taskID = getTaskID();
	long staffID = getStaffID();

	TaskStaffAssignment comparedObj = (TaskStaffAssignment) obj;
	long taskIDcompare = comparedObj.getTaskID();
	long staffIDcompare = comparedObj.getStaffID();

	boolean taskIDEqual = taskID == taskIDcompare;
	boolean staffIDEqual = staffID == staffIDcompare;

	return taskIDEqual && staffIDEqual;
    }

    @Override
    public int hashCode() {
	Long identifier = getTaskID() + getStaffID();
	return identifier.hashCode();
    }

    @Transient
    @Override
    public String getName() {
	return String.format("[Task:%s, Staff: %s]", getTaskID(), getStaffID());
    }

    @Transient
    @Override
    public String getObjectName() {
	return OBJECT_NAME;
    }

    @Transient
    @Override
    public String getTableName() {
	return TABLE_NAME;
    }

}
