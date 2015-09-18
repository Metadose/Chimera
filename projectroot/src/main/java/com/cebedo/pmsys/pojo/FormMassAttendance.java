package com.cebedo.pmsys.pojo;

import java.util.Date;

import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;

public class FormMassAttendance {

    private Project project;
    private Staff staff;
    private Date startDate;
    private Date endDate;
    private int statusID;
    private double wage;
    private boolean includeSaturdays;
    private boolean includeSundays;

    public FormMassAttendance() {
	;
    }

    public FormMassAttendance(Project project, Staff stf) {
	setProject(project);
	setStaff(stf);
	setWage(stf.getWage());
    }

    public boolean isIncludeSaturdays() {
	return includeSaturdays;
    }

    public void setIncludeSaturdays(boolean include) {
	this.includeSaturdays = include;
    }

    public Staff getStaff() {
	return staff;
    }

    public void setStaff(Staff staff) {
	this.staff = staff;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public int getStatusID() {
	return statusID;
    }

    public void setStatusID(int statusID) {
	this.statusID = statusID;
    }

    public double getWage() {
	return wage;
    }

    public void setWage(double wage) {
	this.wage = wage;
    }

    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    public boolean isIncludeSundays() {
	return includeSundays;
    }

    public void setIncludeSundays(boolean includeSundays) {
	this.includeSundays = includeSundays;
    }

}
