package com.cebedo.pmsys.wrapper;

import java.util.Date;

import com.cebedo.pmsys.enums.PayrollStatus;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.SystemUser;

public class ProjectPayrollWrapper {

    private SystemUser approver;
    private SystemUser creator;
    private Date startDate;
    private Date endDate;
    private PayrollStatus status;
    private Company company;
    private Project project;

    public ProjectPayrollWrapper() {
	;
    }

    public ProjectPayrollWrapper(SystemUser approver2, SystemUser creator2,
	    Date startDate2, Date endDate2, PayrollStatus status2, Company co,
	    Project proj) {
	setApprover(approver2);
	setCreator(creator2);
	setStartDate(startDate2);
	setEndDate(endDate2);
	setStatus(status2);
	setCompany(co);
	setProject(proj);
    }

    public SystemUser getApprover() {
	return approver;
    }

    public void setApprover(SystemUser approver) {
	this.approver = approver;
    }

    public SystemUser getCreator() {
	return creator;
    }

    public void setCreator(SystemUser creator) {
	this.creator = creator;
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

    public PayrollStatus getStatus() {
	return status;
    }

    public void setStatus(PayrollStatus status) {
	this.status = status;
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

}
