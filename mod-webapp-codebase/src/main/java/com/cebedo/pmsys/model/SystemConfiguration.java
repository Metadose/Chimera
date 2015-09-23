package com.cebedo.pmsys.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = SystemConfiguration.TABLE_NAME)
public class SystemConfiguration implements Serializable {

    private static final long serialVersionUID = -1551965044511625142L;
    public static final String OBJECT_NAME = "config";
    public static final String TABLE_NAME = "system_configuration";
    public static final String COLUMN_PRIMARY_KEY = "sysconf_id";
    public static final String COLUMN_NAME = "name";

    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_NAME = "name";

    private long id;
    private String name;
    private String value;
    private Company company;

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
	this.name = StringUtils.trim(name);
    }

    @Column(name = "value")
    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = StringUtils.trim(value);
    }

    @ManyToOne
    @JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

}