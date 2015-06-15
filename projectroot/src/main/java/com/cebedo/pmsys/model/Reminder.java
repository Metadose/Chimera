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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

@Entity
@Table(name = Reminder.TABLE_NAME)
public class Reminder implements Serializable {

    public static final String OBJECT_NAME = "reminder";
    public static final String TABLE_NAME = "reminders";
    public static final String COLUMN_PRIMARY_KEY = OBJECT_NAME + "_id";
    public static final String PROPERTY_ID = "id";

    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("Reminder");

    private long id;
    private String title;
    private String content;
    private Date datetime;
    private Project project;
    private Company company;
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

    @Column(name = "title", nullable = false, length = 32)
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    @Column(name = "content")
    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
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

    @OneToMany(mappedBy = "reminder", cascade = CascadeType.ALL)
    public Set<Expense> getExpenses() {
	return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
	this.expenses = expenses;
    }

}
