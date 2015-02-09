package com.cebedo.pmsys.staff.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = ManagerAssignments.TABLE_NAME)
public class ManagerAssignments implements Serializable {

	public static final String TABLE_NAME = "assignments_manager";

	private static final long serialVersionUID = 1L;

	private String projectPosition;

	@Column(name = "project_position", nullable = false, length = 32)
	public String getProjectPosition() {
		return projectPosition;
	}

	public void setProjectPosition(String projectPosition) {
		this.projectPosition = projectPosition;
	}
}
