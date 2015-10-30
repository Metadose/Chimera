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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.cebedo.pmsys.enums.HTMLCSSDetails;
import com.cebedo.pmsys.enums.StatusProject;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.model.assignment.ProjectStaffAssignment;
import com.cebedo.pmsys.utils.NumberFormatUtils;

@Entity
@Table(name = Project.TABLE_NAME)
public class Project implements Serializable {

    private static final long serialVersionUID = -7773714241039540737L;
    public static final String OBJECT_NAME = "project";
    public static final String TABLE_NAME = "projects";

    public static final String COLUMN_PRIMARY_KEY = "project_id";
    public static final String COLUMN_NAME = "name";

    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_NAME = "name";

    public static final String SUB_MODULE_PROFILE = "profile";

    private long id;
    private String name;
    private int status = StatusProject.NEW.id();
    private Set<FieldAssignment> assignedFields;
    private Set<Staff> assignedStaff;
    private String location;
    private String notes;
    private Set<Task> assignedTasks;
    private Company company;

    // Formal fields.
    private double physicalTarget;
    private Date dateStart;
    private Date targetCompletionDate;
    private Date actualCompletionDate;

    // Audit logs.
    private Set<AuditLog> auditLogs;

    /**
     * Bean-backed forms.
     */
    private long[] staffIDs;

    public Project() {
	;
    }

    public Project(long id) {
	setId(id);
    }

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<AuditLog> getAuditLogs() {
	return auditLogs;
    }

    public void setAuditLogs(Set<AuditLog> auditLogs) {
	this.auditLogs = auditLogs;
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

    @Column(name = "name", nullable = false, length = 32)
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = StringUtils.trim(name);
    }

    @Column(name = "status", nullable = false, length = 2)
    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    @Transient
    public StatusProject getStatusEnum() {
	return StatusProject.of(getStatus());
    }

    @Transient
    public HTMLCSSDetails getCSSofDelay() {
	// Delay.
	if (getCalDaysRemaining() < 0) {
	    return HTMLCSSDetails.DELAYED;
	}
	// On time.
	return HTMLCSSDetails.ON_TIME;
    }

    @OrderBy(Project.COLUMN_PRIMARY_KEY)
    @OneToMany(mappedBy = FieldAssignment.PRIMARY_KEY + ".project", cascade = CascadeType.REMOVE)
    public Set<FieldAssignment> getAssignedFields() {
	return assignedFields;
    }

    public void setAssignedFields(Set<FieldAssignment> fields) {
	this.assignedFields = fields;
    }

    @ManyToMany
    @JoinTable(name = ProjectStaffAssignment.TABLE_NAME, joinColumns = {
	    @JoinColumn(name = COLUMN_PRIMARY_KEY) }, inverseJoinColumns = {
		    @JoinColumn(name = Staff.COLUMN_PRIMARY_KEY, nullable = false, updatable = false) })
    public Set<Staff> getAssignedStaff() {
	return assignedStaff;
    }

    public void setAssignedStaff(Set<Staff> assignedStaff) {
	this.assignedStaff = assignedStaff;
    }

    @Transient
    public long[] getStaffIDs() {
	return staffIDs;
    }

    public void setStaffIDs(long[] staffIDs) {
	this.staffIDs = staffIDs;
    }

    /**
     * Project to Task many-to-many without extra columns.
     */
    @OrderBy(Task.COLUMN_DATE_START)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<Task> getAssignedTasks() {
	return assignedTasks;
    }

    public void setAssignedTasks(Set<Task> assignedTasks) {
	this.assignedTasks = assignedTasks;
    }

    @Column(name = "location", length = 108)
    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = StringUtils.trim(location);
    }

    @Column(name = "notes")
    public String getNotes() {
	return notes;
    }

    public void setNotes(String notes) {
	this.notes = StringUtils.trim(notes);
    }

    @Override
    public String toString() {
	return "[Project] " + getId() + " [Name] " + getName();
    }

    @ManyToOne
    @JoinColumn(name = Company.COLUMN_PRIMARY_KEY)
    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    @Column(name = "physical_target")
    public double getPhysicalTarget() {
	return physicalTarget;
    }

    @Transient
    public String getPhysicalTargetAsString() {
	return NumberFormatUtils.getQuantityFormatter().format(getPhysicalTarget());
    }

    public void setPhysicalTarget(double physicalTarget) {
	this.physicalTarget = physicalTarget;
    }

    @Column(name = "date_start")
    @Temporal(TemporalType.DATE)
    public Date getDateStart() {
	return dateStart;
    }

    public void setDateStart(Date dateStart) {
	this.dateStart = dateStart;
    }

    @Column(name = "date_completion_target")
    @Temporal(TemporalType.DATE)
    public Date getTargetCompletionDate() {
	return targetCompletionDate;
    }

    @Transient
    public int getCalDaysRemaining() {
	Date now = null;
	if (getStatusEnum() == StatusProject.COMPLETED) {
	    now = getActualCompletionDate();
	} else {
	    now = new Date(System.currentTimeMillis());
	}
	Date target = getTargetCompletionDate();
	return Days.daysBetween(new DateTime(now), new DateTime(target)).getDays();
    }

    @Transient
    public double getCalDaysRemainingAsPercent() {
	double daysRemain = getCalDaysRemaining();
	double daysTotal = getCalDaysTotal();
	double remainPercent = (daysRemain / daysTotal) * 100;
	return remainPercent;
    }

    @Transient
    public String getCalDaysRemainingAsPercentAsString() {
	double remainPercent = getCalDaysRemainingAsPercent();
	return NumberFormatUtils.getQuantityFormatter().format(remainPercent);
    }

    @Transient
    public double getCalDaysProgressAsPercent() {
	double remainPercent = getCalDaysRemainingAsPercent();
	return remainPercent < 0 ? 0 : remainPercent;
    }

    @Transient
    public int getCalDaysTotal() {
	Date startDate = getDateStart();
	Date target = getTargetCompletionDate();
	return Days.daysBetween(new DateTime(startDate), new DateTime(target)).getDays();
    }

    @Transient
    public String getCalDaysTotalAsString() {
	return NumberFormatUtils.getQuantityFormatter().format(getCalDaysTotal());
    }

    @Transient
    public String getCalDaysRemainingAsString() {
	return NumberFormatUtils.getQuantityFormatter().format(getCalDaysRemaining());
    }

    public void setTargetCompletionDate(Date targetCompletionDate) {
	this.targetCompletionDate = targetCompletionDate;
    }

    @Column(name = "date_completion_actual")
    @Temporal(TemporalType.DATE)
    public Date getActualCompletionDate() {
	return actualCompletionDate;
    }

    public void setActualCompletionDate(Date actualCompletionDate) {
	this.actualCompletionDate = actualCompletionDate;
    }

    @Transient
    public boolean isCompleted() {
	return getStatusEnum() == StatusProject.COMPLETED;
    }

}