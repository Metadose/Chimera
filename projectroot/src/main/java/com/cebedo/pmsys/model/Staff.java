package com.cebedo.pmsys.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cebedo.pmsys.model.assignment.ManagerAssignment;
import com.cebedo.pmsys.model.assignment.StaffFieldAssignment;
import com.cebedo.pmsys.model.assignment.StaffTeamAssignment;

@Entity
@Table(name = Staff.TABLE_NAME)
public class Staff implements Serializable {

    public static final String OBJECT_NAME = "staff";
    public static final String TABLE_NAME = "staff";
    public static final String COLUMN_PRIMARY_KEY = "staff_id";

    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_PREFIX = "prefix";
    public static final String PROPERTY_FIRST_NAME = "firstName";
    public static final String PROPERTY_MIDDLE_NAME = "middleName";
    public static final String PROPERTY_LAST_NAME = "lastName";
    public static final String PROPERTY_SUFFIX = "suffix";

    public static final String PROPERTY_TRANSIENT_FULL_NAME = "Full Name";

    public static final String SUB_MODULE_PROFILE = "profile";

    private static final long serialVersionUID = 1L;

    private long id;
    private String thumbnailURL;
    private String prefix;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String companyPosition;
    private Set<ManagerAssignment> assignedManagers;
    private Set<Task> tasks;
    private Set<Delivery> deliveries;
    private Set<Expense> expenses;

    private String email;
    private String contactNumber;
    private Set<ProjectFile> files;
    private Set<Team> teams;
    private Set<StaffFieldAssignment> fieldAssignments;
    private Company company;
    private SystemUser user;
    private double wage;

    public Staff() {
	;
    }

    public Staff(long id) {
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

    @Column(name = "thumbnail_url", length = 255)
    public String getThumbnailURL() {
	return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
	this.thumbnailURL = thumbnailURL;
    }

    @Column(name = "name_prefix", length = 8)
    public String getPrefix() {
	return prefix;
    }

    public void setPrefix(String prefix) {
	this.prefix = prefix;
    }

    @Column(name = "name_first", length = 32)
    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    @Column(name = "name_middle", length = 16)
    public String getMiddleName() {
	return middleName;
    }

    public void setMiddleName(String middleName) {
	this.middleName = middleName;
    }

    @Column(name = "name_last", length = 16)
    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    @Column(name = "name_suffix", length = 8)
    public String getSuffix() {
	return suffix;
    }

    public void setSuffix(String suffix) {
	this.suffix = suffix;
    }

    @Column(name = "position", length = 32)
    public String getCompanyPosition() {
	return companyPosition;
    }

    public void setCompanyPosition(String companyPosition) {
	this.companyPosition = companyPosition;
    }

    @OneToMany(mappedBy = ManagerAssignment.PRIMARY_KEY + ".manager", cascade = CascadeType.REMOVE)
    public Set<ManagerAssignment> getAssignedManagers() {
	return assignedManagers;
    }

    public void setAssignedManagers(Set<ManagerAssignment> managers) {
	this.assignedManagers = managers;
    }

    @ManyToMany(mappedBy = "staff")
    public Set<Task> getTasks() {
	return tasks;
    }

    public void setTasks(Set<Task> tasks) {
	this.tasks = tasks;
    }

    @ManyToMany(mappedBy = "staff")
    public Set<Delivery> getDeliveries() {
	return deliveries;
    }

    public void setDeliveries(Set<Delivery> deliveries) {
	this.deliveries = deliveries;
    }

    @Column(name = "email", length = 32)
    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    @Column(name = "wage")
    public double getWage() {
	return wage;
    }

    public void setWage(double wage) {
	this.wage = wage;
    }

    @Column(name = "contact_number", length = 32)
    public String getContactNumber() {
	return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
	this.contactNumber = contactNumber;
    }

    @OneToMany(mappedBy = "uploader")
    public Set<ProjectFile> getFiles() {
	return files;
    }

    public void setFiles(Set<ProjectFile> files) {
	this.files = files;
    }

    @ManyToMany
    @JoinTable(name = StaffTeamAssignment.TABLE_NAME, joinColumns = { @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = { @JoinColumn(name = Team.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
    public Set<Team> getTeams() {
	return teams;
    }

    public void setTeams(Set<Team> teams) {
	this.teams = teams;
    }

    @OneToMany(mappedBy = StaffFieldAssignment.PRIMARY_KEY + ".staff", cascade = CascadeType.REMOVE)
    public Set<StaffFieldAssignment> getFieldAssignments() {
	return fieldAssignments;
    }

    public void setFieldAssignments(
	    Set<StaffFieldAssignment> staffFieldAssignments) {
	this.fieldAssignments = staffFieldAssignments;
    }

    @ManyToOne
    @JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    @OneToOne(mappedBy = Staff.OBJECT_NAME, cascade = CascadeType.ALL)
    public SystemUser getUser() {
	return user;
    }

    public void setUser(SystemUser user) {
	this.user = user;
    }

    @Transient
    public String getFullName() {
	String fullName = getPrefix() == null ? "" : getPrefix() + " ";
	fullName += getFirstName() == null ? "" : getFirstName() + " ";
	fullName += getMiddleName() == null ? "" : getMiddleName() + " ";
	fullName += getLastName() == null ? "" : getLastName() + " ";
	fullName += getSuffix() == null ? "" : getSuffix();
	return fullName;
    }

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    public Set<Expense> getExpenses() {
	return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
	this.expenses = expenses;
    }
}
