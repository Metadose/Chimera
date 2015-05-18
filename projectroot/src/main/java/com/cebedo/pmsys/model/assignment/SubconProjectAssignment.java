package com.cebedo.pmsys.model.assignment;

import java.io.Serializable;

//@Entity
//@Table(name = SubconProjectAssignment.TABLE_NAME)
public class SubconProjectAssignment implements Serializable {

	public static final String TABLE_NAME = "assignments_subcon_project";
	private static final long serialVersionUID = 1L;

	private long projectID;
	private long subconID;

	// @Id
	// @Column(name = Project.COLUMN_PRIMARY_KEY, nullable = false)
	public long getProjectID() {
		return projectID;
	}

	public void setProjectID(long projectID) {
		this.projectID = projectID;
	}

	// @Id
	// @Column(name = Subcontractor.COLUMN_PRIMARY_KEY, nullable = false)
	public long getSubcontractorID() {
		return subconID;
	}

	public void setSubcontractorID(long subconID) {
		this.subconID = subconID;
	}
}
