package com.cebedo.pmsys.staff.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = ManagerAssignments.TABLE_NAME)
public class ManagerAssignments implements Serializable {

	public static final String TABLE_NAME = "manager_assignments";

	private static final long serialVersionUID = 1L;

	private String projectPosition;
}
