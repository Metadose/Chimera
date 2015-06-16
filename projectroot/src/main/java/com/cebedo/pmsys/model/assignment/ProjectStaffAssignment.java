package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

@Entity
@Table(name = ProjectStaffAssignment.TABLE_NAME)
public class ProjectStaffAssignment implements Serializable {

    public static final String TABLE_NAME = "assignments_project_staff";

    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("ProjectStaffAssignment");

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

}