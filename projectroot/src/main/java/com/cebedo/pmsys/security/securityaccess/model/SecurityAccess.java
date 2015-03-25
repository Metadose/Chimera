package com.cebedo.pmsys.security.securityaccess.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.cebedo.pmsys.systemuser.model.SystemUser;

@Entity
@Table(name = SecurityAccess.TABLE_NAME)
public class SecurityAccess implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String OBJECT_NAME = "securityaccess";
	public static final String TABLE_NAME = "security_access";
	public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";

	public static final String PROPERTY_ID = "id";

	private long id;
	private String name;
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

	@ManyToMany(mappedBy = "securityAccess")
	public Set<SystemUser> getUsers() {
		return users;
	}

	public void setUsers(Set<SystemUser> users) {
		this.users = users;
	}

}
