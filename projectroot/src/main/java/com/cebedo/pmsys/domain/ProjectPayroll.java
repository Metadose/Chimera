package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.utils.DateUtils;
import com.cebedo.pmsys.utils.RedisKeyPartUtils;

public class ProjectPayroll implements IDomainObject {

    private static final long serialVersionUID = 1L;

    /**
     * Keys<br>
     * company:45:project:21:approver:55:creator:151:status:4:startdate:2015.05
     * .01:enddate:2015.05.15
     */
    private Long companyID;
    private long projectID;
    private long approverID; // User ID.
    private long creatorID; // User ID.
    private int statusID; // Give me all payrolls not yet approved.
    private Date startDate;
    private Date endDate;

    /**
     * Other properties.
     */
    private boolean saved;
    private long[] staffIDs;
    private List<Long> staffList;
    private Map<String, Object> payrollMap;
    private Map<String, Object> extMap;

    /**
     * Project Structure snapshot.
     */
    private List<Staff> managers;
    private Map<String, Object> projectStructure;
    private String payrollJSON;
    private Date lastComputed;

    public Date getLastComputed() {
	return lastComputed;
    }

    public void setLastComputed(Date lastComputed) {
	this.lastComputed = lastComputed;
    }

    public ProjectPayroll() {
	;
    }

    public ProjectPayroll(Long companyID2, long projectID2, long creatorID2) {
	setCompanyID(companyID2);
	setProjectID(projectID2);
	setCreatorID(creatorID2);
    }

    public long getApproverID() {
	return approverID;
    }

    public void setApproverID(long approverID) {
	this.approverID = approverID;
    }

    public List<Staff> getManagers() {
	return managers;
    }

    public void setManagers(List<Staff> managers) {
	this.managers = managers;
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

    public long getCreatorID() {
	return creatorID;
    }

    public void setCreatorID(long creatorID) {
	this.creatorID = creatorID;
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

    public Long getCompanyID() {
	return companyID;
    }

    public void setCompanyID(Long companyID) {
	this.companyID = companyID;
    }

    public long getProjectID() {
	return projectID;
    }

    public void setProjectID(long projectID) {
	this.projectID = projectID;
    }

    public int getStatusID() {
	return statusID;
    }

    public void setStatusID(int statusID) {
	this.statusID = statusID;
    }

    public String getPayrollJSON() {
	return payrollJSON;
    }

    public void setPayrollJSON(String payrollJSON) {
	this.payrollJSON = payrollJSON;
    }

    public Map<String, Object> getPayrollMap() {
	return payrollMap;
    }

    public void setPayrollMap(Map<String, Object> payrollMap) {
	this.payrollMap = payrollMap;
    }

    public List<Long> getStaffList() {
	return staffList;
    }

    public void setStaffList(List<Long> staffList) {
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

    public Map<String, Object> getProjectStructure() {
	return projectStructure;
    }

    public void setProjectStructure(Map<String, Object> projectStructure) {
	this.projectStructure = projectStructure;
    }

    public static String constructKey(long companyID, Long projectID,
	    Long approverID, Long creatorID, Integer statusID, Date startDate,
	    Date endDate) {

	String companyPart = Company.OBJECT_NAME + ":" + companyID
		+ ":payroll:";
	String projectPart = RedisKeyPartUtils.generateKeyPartWithWildcard(
		Project.OBJECT_NAME, projectID);
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

    /**
     * company:45:project:21:approver:55:creator:151:status:4:<br>
     * startdate:2015.05.01:enddate:2015.05.15
     */
    @Override
    public String getKey() {
	long companyID = getCompanyID() == null ? 0 : getCompanyID();
	String companyPart = Company.OBJECT_NAME + ":" + companyID
		+ ":payroll:";
	String projectPart = Project.OBJECT_NAME + ":" + getProjectID() + ":";
	String approverPart = "approver:" + getApproverID() + ":";
	String creatorPart = "creator:" + getCreatorID() + ":";
	String statusPart = "status:" + getStatusID() + ":";

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

	String companyPart = Company.OBJECT_NAME + ":" + companyID
		+ ":payroll:";
	String projectPart = RedisKeyPartUtils.generateKeyPartWithWildcard(
		Project.OBJECT_NAME, projectID);
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
	long companyID = getCompanyID() == null ? 0 : getCompanyID();
	String companyPart = Company.OBJECT_NAME + ":" + companyID
		+ ":payroll:";
	String projectPart = Project.OBJECT_NAME + ":" + getProjectID() + ":";

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
}
