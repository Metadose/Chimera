package com.cebedo.pmsys.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.model.assignment.TaskStaffAssignment;
import com.cebedo.pmsys.utils.DateUtils;

@Entity
@Table(name = Task.TABLE_NAME)
public class Task implements Serializable {

    private static final long serialVersionUID = 2244337663166988762L;
    public static final String TABLE_NAME = "tasks";
    public static final String OBJECT_NAME = "task";

    public static final String COLUMN_PRIMARY_KEY = "task_id";
    public static final String COLUMN_DATE_START = "date_start";
    public static final String COLUMN_STATUS = "status";

    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_TITLE = "title";
    public static final String PROPERTY_PROJECT = "project";

    private long id;
    private String title;
    private String content;
    private Date dateStart;
    private double duration;
    private Project project;
    private Set<Staff> staff;
    private int status;
    private Company company;

    public Task() {
	;
    }

    public Task(Project proj) {
	setProject(proj);
	Company company = proj.getCompany();
	if (company != null) {
	    setCompany(company);
	}
    }

    public Task(Company company2, Project project2) {
	setCompany(company2);
	setProject(project2);
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

    @Column(name = "title", nullable = false)
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = StringUtils.trim(title);
    }

    @Column(name = "content", nullable = false)
    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = StringUtils.trim(content);
    }

    @Column(name = "date_start", nullable = false)
    @Temporal(TemporalType.DATE)
    public Date getDateStart() {
	return dateStart;
    }

    @Transient
    public Date getEndDate() {
	return DateUtils.addDays(dateStart, ((Double) Math.ceil(duration)).intValue());
    }

    public void setDateStart(Date dateStart) {
	this.dateStart = dateStart;
    }

    @Column(name = "duration", nullable = false)
    public double getDuration() {
	return duration;
    }

    public void setDuration(double duration) {
	this.duration = duration;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Project.COLUMN_PRIMARY_KEY)
    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    @ManyToMany
    @JoinTable(name = TaskStaffAssignment.TABLE_NAME, joinColumns = { @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = { @JoinColumn(name = Staff.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
    public Set<Staff> getStaff() {
	return staff;
    }

    public void setStaff(Set<Staff> staff) {
	this.staff = staff;
    }

    @Column(name = "status", nullable = false)
    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    @Transient
    public TaskStatus getStatusEnum() {
	return TaskStatus.of(getStatus());
    }

    @Override
    public String toString() {
	return "[Task] " + getId() + " [Content] " + getContent();
    }

    /**
     * Add new staff on the task.
     * 
     * @param stf
     */
    public void assignStaff(Staff stf) {
	// TODO Make this function work.
	// Identify what Set object to initialize Set.
	if (staff == null) {
	    Set<Staff> staffList = new TreeSet<Staff>();
	    staffList.add(stf);
	    setStaff(staffList);
	} else {
	    this.staff.add(stf);
	}
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
