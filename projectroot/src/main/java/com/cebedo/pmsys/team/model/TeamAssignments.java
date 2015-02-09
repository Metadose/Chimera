package com.cebedo.pmsys.team.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = TeamAssignments.TABLE_NAME)
public class TeamAssignments implements Serializable {

	public static final String TABLE_NAME = "assignments_team";

	private static final long serialVersionUID = 1L;
}
