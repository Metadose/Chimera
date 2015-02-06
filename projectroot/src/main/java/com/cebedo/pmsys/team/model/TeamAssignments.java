package com.cebedo.pmsys.team.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = TeamAssignments.tableName)
public class TeamAssignments implements Serializable {

	public static final String tableName = "team_assignments";

	private static final long serialVersionUID = 1L;
}
