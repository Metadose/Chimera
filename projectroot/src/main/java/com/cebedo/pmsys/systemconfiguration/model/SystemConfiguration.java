package com.cebedo.pmsys.systemconfiguration.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = SystemConfiguration.TABLE_NAME)
public class SystemConfiguration implements Serializable {

	public static final String CLASS_NAME = "SystemConfiguration";
	public static final String OBJECT_NAME = "config";
	public static final String TABLE_NAME = "system_configuration";
	public static final String COLUMN_PRIMARY_KEY = "sysconf_id";
	public static final String COLUMN_NAME = "name";

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private String value;

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

	@Column(name = "value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
