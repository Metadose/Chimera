package com.cebedo.pmsys.model.assignment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cebedo.pmsys.base.IObjectAssignment;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;

@Entity
@Table(name = ProjectStaffAssignment.TABLE_NAME)
public class ProjectStaffAssignment implements IObjectAssignment {

    private static final long serialVersionUID = -7814680016856682810L;

    public static final String OBJECT_NAME = "ProjectStaffAssignment";
    public static final String TABLE_NAME = "assignments_project_staff";

    private long projectID;
    private long staffID;

    @Id
    @Column(name = Project.COLUMN_PRIMARY_KEY, nullable = false)
    public long getProjectID() {
	return projectID;
    }

    public void setProjectID(long projectID) {
	this.projectID = projectID;
    }

    @Id
    @Column(name = Staff.COLUMN_PRIMARY_KEY, nullable = false)
    public long getStaffID() {
	return staffID;
    }

    public void setStaffID(long stfID) {
	this.staffID = stfID;
    }

    @Transient
    @Override
    public String getName() {
	return String.format("[Project: %s, Staff: %s]", getProjectID(), getStaffID());
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
