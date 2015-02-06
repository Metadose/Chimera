package com.cebedo.pmsys.field.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.cebedo.pmsys.project.model.Project;

@Entity
@Table(name = FieldAssignments.tableName)
public class FieldAssignments implements Serializable {

	public static final String tableName = "field_assignments";
	public static final String primaryKey = "id";

	private static final long serialVersionUID = 1L;

	private int id;
	private String label;
	private String value;
	private Project project;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = primaryKey, unique = true, nullable = true)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
