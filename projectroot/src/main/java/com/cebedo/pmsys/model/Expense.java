package com.cebedo.pmsys.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = Expense.TABLE_NAME)
public class Expense implements Serializable {

    public static final String TABLE_NAME = "expenses";
    public static final String OBJECT_NAME = "expense";
    public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String description;
    private double value;
    private Date date;

    /**
     * Any of the following can have expenses.<br>
     * Relationship is 1 of any below, to Many expenses.
     */
    private Task task;
    private Project project;
    private Company company;
    private Staff staff;
    private Team team;
    private Delivery delivery;
    private Material material;
    private Milestone milestone;
    private Reminder reminder;
    private Storage storage;
    private Supplier expenses;

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

    @Column(name = "description")
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Column(name = "value", nullable = false)
    public double getValue() {
	return value;
    }

    public void setValue(double value) {
	this.value = value;
    }

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    @ManyToOne
    @JoinColumn(name = Task.COLUMN_PRIMARY_KEY)
    public Task getTask() {
	return task;
    }

    public void setTask(Task task) {
	this.task = task;
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
}
