package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.cebedo.pmsys.bean.PayrollComputationResult;
import com.cebedo.pmsys.enums.PayrollStatus;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.Team;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;
import com.cebedo.pmsys.utils.DateUtils;
import com.cebedo.pmsys.utils.RedisKeyPartUtils;
import com.cebedo.pmsys.utils.SerialVersionUIDUtils;

public class ProjectPayroll implements IDomainObject {

    private static final long serialVersionUID = SerialVersionUIDUtils
	    .convertStringToLong("ProjectPayroll");

    private Company company;
    private Project project;
    private SystemUser approver;
    private SystemUser creator;
    private PayrollStatus status; // Give me all payrolls not yet approved.
    private Date startDate;
    private Date endDate;

    /**
     * Bean-backed form.
     */
    private long approverID;
    private int statusID;

    /**
     * Other properties.
     */
    private boolean saved;
    private long[] staffIDs;
    private Map<String, Object> extMap;

    /**
     * List of staff during the object was created.
     */
    private Set<ManagerAssignment> managerAssignments;
    private Set<Staff> staffList;

    /**
     * List of assigned.
     */
    private Set<Staff> assignedStaffList;

    /**
     * Project Structure snapshot.
     */
    private String payrollJSON;
    private Date lastComputed;
    private PayrollComputationResult payrollComputationResult;

    public PayrollComputationResult getPayrollComputationResult() {
	return payrollComputationResult;
    }

    public void setPayrollComputationResult(
	    PayrollComputationResult payrollComputationResult) {
	this.payrollComputationResult = payrollComputationResult;
    }

    public Set<ManagerAssignment> getManagerAssignments() {
	return managerAssignments;
    }

    public void setManagerAssignments(Set<ManagerAssignment> managerAssignments) {
	this.managerAssignments = managerAssignments;
    }

    public Date getLastComputed() {
	return lastComputed;
    }

    public void setLastComputed(Date lastComputed) {
	this.lastComputed = lastComputed;
    }

    public Set<Staff> getAssignedStaffList() {
	return assignedStaffList;
    }

    public void setAssignedStaffList(Set<Staff> assignedStaffList) {
	this.assignedStaffList = assignedStaffList;
    }

    public ProjectPayroll() {
	;
    }

    public ProjectPayroll(Company company2, Project project2,
	    SystemUser creator2) {
	setCompany(company2);
	setProject(project2);
	setCreator(creator2);
    }

    public SystemUser getApprover() {
	return approver;
    }

    public void setApprover(SystemUser approverID) {
	this.approver = approverID;
    }

    public long getApproverID() {
	return approverID;
    }

    public void setApproverID(long approverID) {
	this.approverID = approverID;
    }

    public int getStatusID() {
	return statusID;
    }

    public void setStatusID(int statusID) {
	this.statusID = statusID;
    }

    public long[] getStaffIDs() {
	return staffIDs;
    }

    public void setStaffIDs(long[] staffIDs) {
	this.staffIDs = staffIDs;
    }

    public boolean isSaved() {
	return saved;
    }

    public void setSaved(boolean saved) {
	this.saved = saved;
    }

    public SystemUser getCreator() {
	return creator;
    }

    public void setCreator(SystemUser creatorID) {
	this.creator = creatorID;
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

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company companyID) {
	this.company = companyID;
    }

    public Project getProject() {
	return project;
    }

    public void setProject(Project projectID) {
	this.project = projectID;
    }

    public PayrollStatus getStatus() {
	return status;
    }

    public void setStatus(PayrollStatus status2) {
	this.status = status2;
    }

    public String getPayrollJSON() {
	return payrollJSON;
    }

    public void setPayrollJSON(String payrollJSON) {
	this.payrollJSON = payrollJSON;
    }

    public Set<Staff> getStaffList() {
	return staffList;
    }

    public void setStaffList(Set<Staff> staffList) {
	this.staffList = staffList;
    }

    @Override
    public Map<String, Object> getExtMap() {
	return extMap;
    }

    @Override
    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    public static String constructKey(long companyID, Long projectID,
	    Long approverID, Long creatorID, Integer statusID, Date startDate,
	    Date endDate) {

	String companyPart = Company.OBJECT_NAME + ":" + companyID + ":";
	String projectPart = RedisKeyPartUtils.generateKeyPartWithWildcard(
		Project.OBJECT_NAME, projectID) + "payroll:";
	String approverPart = RedisKeyPartUtils.generateKeyPartWithWildcard(
		"approver", approverID);
	String creatorPart = RedisKeyPartUtils.generateKeyPartWithWildcard(
		"creator", creatorID);
	String statusPart = RedisKeyPartUtils.generateKeyPartWithWildcard(
		"status", statusID);

	String startDateStr = startDate == null ? "*" : DateUtils.formatDate(
		startDate, "yyyy.MM.dd");
	String endDateStr = endDate == null ? "*" : DateUtils.formatDate(
		endDate, "yyyy.MM.dd");
	String startDatePart = "startdate:" + startDateStr + ":";
	String endDatePart = "enddate:" + endDateStr;

	String key = companyPart + projectPart + approverPart + creatorPart
		+ statusPart + startDatePart + endDatePart;
	return key;
    }

    @Override
    public String getKey() {
	long companyID = this.company == null ? 0 : this.company.getId();
	String companyPart = Company.OBJECT_NAME + ":" + companyID + ":";
	String projectPart = Project.OBJECT_NAME + ":" + getProject().getId()
		+ ":payroll:";
	String approverPart = "approver:" + getApprover().getId() + ":";
	String creatorPart = "creator:" + getCreator().getId() + ":";
	String statusPart = "status:"
		+ (getStatus() == null ? getStatusID() : getStatus().id())
		+ ":";

	String startDateStr = DateUtils
		.formatDate(getStartDate(), "yyyy.MM.dd");
	String endDateStr = DateUtils.formatDate(getEndDate(), "yyyy.MM.dd");
	String startDatePart = "startdate:" + startDateStr + ":";
	String endDatePart = "enddate:" + endDateStr;

	String key = companyPart + projectPart + approverPart + creatorPart
		+ statusPart + startDatePart + endDatePart;
	return key;
    }

    public static String constructKeyWithStrings(Long companyID,
	    long projectID, String approverID, String creatorID,
	    String statusID, String startDate, String endDate) {

	String companyPart = Company.OBJECT_NAME + ":" + companyID + ":";
	String projectPart = RedisKeyPartUtils.generateKeyPartWithWildcard(
		Project.OBJECT_NAME, projectID) + "payroll:";
	String approverPart = RedisKeyPartUtils.generateKeyPartWithWildcard(
		"approver", approverID);
	String creatorPart = RedisKeyPartUtils.generateKeyPartWithWildcard(
		"creator", creatorID);
	String statusPart = RedisKeyPartUtils.generateKeyPartWithWildcard(
		"status", statusID);

	String startDateStr = startDate == null ? "*" : startDate;
	String endDateStr = endDate == null ? "*" : endDate;
	String startDatePart = "startdate:" + startDateStr + ":";
	String endDatePart = "enddate:" + endDateStr;

	String key = companyPart + projectPart + approverPart + creatorPart
		+ statusPart + startDatePart + endDatePart;
	return key;
    }

    public String constructPattern(Date oldStart, Date oldEnd) {
	long companyID = this.company == null ? 0 : this.company.getId();
	String companyPart = Company.OBJECT_NAME + ":" + companyID + ":";
	String projectPart = Project.OBJECT_NAME + ":" + getProject().getId()
		+ ":payroll:";

	String approverPart = "approver:*:";
	String creatorPart = "creator:*:";
	String statusPart = "status:*:";

	String startDateStr = DateUtils.formatDate(oldStart, "yyyy.MM.dd");
	String endDateStr = DateUtils.formatDate(oldEnd, "yyyy.MM.dd");
	String startDatePart = "startdate:" + startDateStr + ":";
	String endDatePart = "enddate:" + endDateStr;

	String key = companyPart + projectPart + approverPart + creatorPart
		+ statusPart + startDatePart + endDatePart;
	return key;
    }

    /**
     * Get all teams in this object.
     * 
     * @return
     */
    public Set<Team> getAllTeams() {
	// Map<Team, Set<Staff>> teamStaffMap = (Map<Team, Set<Staff>>)
	// this.projectStructure
	// .get(ProjectController.KEY_PROJECT_STRUCTURE_TEAMS);
	// return teamStaffMap.keySet();
	// FIXME
	return null;
    }
}
