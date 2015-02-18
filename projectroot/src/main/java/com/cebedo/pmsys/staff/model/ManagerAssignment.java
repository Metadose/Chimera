package com.cebedo.pmsys.staff.model;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cebedo.pmsys.project.model.Project;

@Entity
@Table(name = ManagerAssignment.TABLE_NAME)
@AssociationOverrides({
		@AssociationOverride(name = ManagerAssignment.PRIMARY_KEY + ".project", joinColumns = @JoinColumn(name = Project.COLUMN_PRIMARY_KEY)),
		@AssociationOverride(name = ManagerAssignment.PRIMARY_KEY + ".manager", joinColumns = @JoinColumn(name = Staff.COLUMN_PRIMARY_KEY)) })
public class ManagerAssignment implements Serializable {

	public static final String TABLE_NAME = "assignments_project_manager";
	public static final String PRIMARY_KEY = "assignmentID";
	public static final String COLUMN_PROJECT_POSITION = "project_position";

	private static final long serialVersionUID = 1L;

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
