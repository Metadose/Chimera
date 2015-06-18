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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cebedo.pmsys.model.assignment.StaffDeliveryAssignment;

@Entity
@Table(name = DeliveryToDelete.TABLE_NAME)
public class DeliveryToDelete implements Serializable {

    private static final long serialVersionUID = -8212252379058366825L;
    public static final String TABLE_NAME = "deliveries";
    public static final String OBJECT_NAME = "delivery";
    public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";
    public static final String PROPERTY_ID = "id";

    private long id;
    private String name;
    private String description;
    private Date datetime;
    private Project project;
    private Company company;
    private Set<Staff> staff;
    private Storage storage;
    private Set<MaterialToRemove> materials;
    private Set<Expense> expenses;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_PRIMARY_KEY, unique = true, nullable = false)
    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = Storage.COLUMN_PRIMARY_KEY)
    public Storage getStorage() {
	return storage;
    }

    public void setStorage(Storage storage) {
	this.storage = storage;
    }

    @ManyToMany
    @JoinTable(name = StaffDeliveryAssignment.TABLE_NAME, joinColumns = { @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = { @JoinColumn(name = Staff.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
    public Set<Staff> getStaff() {
	return staff;
    }

    public void setStaff(Set<Staff> staff) {
	this.staff = staff;
    }

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    public Set<MaterialToRemove> getMaterials() {
	return materials;
    }

    public void setMaterials(Set<MaterialToRemove> materials) {
	this.materials = materials;
    }

    @Column(name = "name", nullable = false, length = 32)
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Column(name = "datetime", nullable = false)
    @Temporal(TemporalType.DATE)
    public Date getDatetime() {
	return datetime;
    }

    public void setDatetime(Date datetime) {
	this.datetime = datetime;
    }

    @ManyToOne
    @JoinColumn(name = Project.COLUMN_PRIMARY_KEY)
    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    @ManyToOne
    @JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    public Set<Expense> getExpenses() {
	return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
	this.expenses = expenses;
    }

}
