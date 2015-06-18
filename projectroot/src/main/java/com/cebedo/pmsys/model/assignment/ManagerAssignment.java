package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;

@Entity
@Table(name = ManagerAssignment.TABLE_NAME)
@AssociationOverrides({
	@AssociationOverride(name = ManagerAssignment.PRIMARY_KEY + ".project", joinColumns = @JoinColumn(name = Project.COLUMN_PRIMARY_KEY)),
	@AssociationOverride(name = ManagerAssignment.PRIMARY_KEY + ".manager", joinColumns = @JoinColumn(name = Staff.COLUMN_PRIMARY_KEY)) })
public class ManagerAssignment implements Serializable {

    private static final long serialVersionUID = -1657266662898654573L;
    public static final String OBJECT_LABEL = "Manager Assignment";
    public static final String OBJECT_NAME = "managerAssignment";
    public static final String TABLE_NAME = "assignments_project_manager";
    public static final String PRIMARY_KEY = "assignmentID";
    public static final String COLUMN_PROJECT_POSITION = "project_position";

    private ManagerAssignmentID assignmentID = new ManagerAssignmentID();
    private String projectPosition;

    @EmbeddedId
    public ManagerAssignmentID getAssignmentID() {
	return assignmentID;
    }

    public void setAssignmentID(ManagerAssignmentID assignmentID) {
	this.assignmentID = assignmentID;
    }

    @Transient
    public Project getProject() {
	return getAssignmentID().getProject();
    }

    public void setProject(Project project) {
	getAssignmentID().setProject(project);
    }

    @Transient
    public Staff getManager() {
	return getAssignmentID().getManager();
    }

    public void setManager(Staff staff) {
	getAssignmentID().setManager(staff);
    }

    @Column(name = "project_position", nullable = false, length = 32)
    public String getProjectPosition() {
	return projectPosition;
    }

    public void setProjectPosition(String projectPosition) {
	this.projectPosition = projectPosition;
    }
}
