package com.cebedo.pmsys.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = SecurityRole.TABLE_NAME)
public class SecurityRole implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String OBJECT_NAME = "securityrole";
	public static final String TABLE_NAME = "security_roles";
	public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";

	public static final String PROPERTY_ID = "id";

	public static final String ROLE_FIELD_EDITOR = "ROLE_FIELD_EDITOR";
	public static final String ROLE_TASK_EDITOR = "ROLE_TASK_EDITOR";
	public static final String ROLE_PROJECT_EDITOR = "ROLE_PROJECT_EDITOR";
	public static final String ROLE_STAFF_EDITOR = "ROLE_STAFF_EDITOR";
	public static final String ROLE_PHOTO_EDITOR = "ROLE_PHOTO_EDITOR";
	public static final String ROLE_PROJECTFILE_EDITOR = "ROLE_PROJECTFILE_EDITOR";
	public static final String ROLE_TEAM_EDITOR = "ROLE_TEAM_EDITOR";
	public static final String ROLE_CONFIG_EDITOR = "ROLE_CONFIG_EDITOR";
	public static final String ROLE_SYSTEMUSER_EDITOR = "ROLE_SYSTEMUSER_EDITOR";
	public static final String ROLE_LOG_EDITOR = "ROLE_LOG_EDITOR";

	private long id;
	private String name;
	private String label;
	private Set<SystemUser> users;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_PRIMARY_KEY, unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 32)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "label", nullable = false, length = 32)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@ManyToMany(mappedBy = "securityRoles")
	public Set<SystemUser> getUsers() {
		return users;
	}

	public void setUsers(Set<SystemUser> users) {
		this.users = users;
	}

}
