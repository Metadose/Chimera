package com.cebedo.pmsys.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = Company.TABLE_NAME)
public class Company implements Serializable {

    private static final long serialVersionUID = 8860496412110526096L;
    public static final String TABLE_NAME = "companies";
    public static final String OBJECT_NAME = "company";
    public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";

    public static final String PROPERTY_ID = "id";

    private long id;
    private String name = "";
    private String description;
    private Date dateStarted;
    private Date dateExpiration;
    // FIXME What the hell? "admins" but getting all employees?
    @Deprecated
    private Set<Staff> admins;
    private Set<SystemUser> employees;
    private Set<Project> projects;
    private Set<SystemConfiguration> configs;
    private Set<Task> tasks;
    private Set<AuditLog> auditLogs;

    public Company() {
	;
    }

    public Company(long id) {
	setId(id);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_PRIMARY_KEY, unique = true, nullable = false)
    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    @Column(name = "name", nullable = false, length = 64)
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = StringUtils.trim(name);
    }

    @Column(name = "description")
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = StringUtils.trim(description);
    }

    @Column(name = "date_started", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateStarted() {
	return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
	this.dateStarted = dateStarted;
    }

    @Column(name = "date_expiration", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateExpiration() {
	return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
	this.dateExpiration = dateExpiration;
    }

    @Deprecated
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    public Set<Staff> getAdmins() {
	return admins;
    }

    public void setAdmins(Set<Staff> admins) {
	this.admins = admins;
    }

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    public Set<SystemUser> getEmployees() {
	return employees;
    }

    public void setEmployees(Set<SystemUser> employees) {
	this.employees = employees;
    }

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    public Set<Project> getProjects() {
	return projects;
    }

    public void setProjects(Set<Project> projects) {
	this.projects = projects;
    }

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    public Set<SystemConfiguration> getConfigs() {
	return configs;
    }

    public void setConfigs(Set<SystemConfiguration> configs) {
	this.configs = configs;
    }

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    public Set<Task> getTasks() {
	return tasks;
    }

    public void setTasks(Set<Task> tasks) {
	this.tasks = tasks;
    }

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    public Set<AuditLog> getAuditLogs() {
	return auditLogs;
    }

    public void setAuditLogs(Set<AuditLog> auditLogs) {
	this.auditLogs = auditLogs;
    }

    public String toString() {
	String str = "";
	str += "id=" + id;
	str += ", name=" + name;
	str += ", description=" + description;
	str += ", dateStarted=" + dateStarted;
	str += ", dateExpiration=" + dateExpiration;
	return str;
    }
}
