package com.cebedo.pmsys.domain;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.cebedo.pmsys.bean.PayrollComputationResult;
import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.enums.PayrollStatus;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.utils.DateUtils;

public class ProjectPayroll implements IDomainObject {

    private static final long serialVersionUID = 5324023297418291423L;
    /**
     * Key: company:%s:project:%s:payroll:%s
     */
    private Company company;
    private Project project;
    private UUID uuid;

    /**
     * Specs.
     */
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

    public void setPayrollComputationResult(PayrollComputationResult payrollComputationResult) {
	this.payrollComputationResult = payrollComputationResult;
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

    public ProjectPayroll(Company company2, Project project2, SystemUser creator2) {
	setCompany(company2);
	setProject(project2);
	setCreator(creator2);
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

    public String getStartEndDisplay() {
	String start = DateUtils.formatDate(startDate, "yyyy/MM/dd");
	String end = DateUtils.formatDate(endDate, "yyyy/MM/dd");
	return start + " to " + end;
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

    /**
     * Construct a pattern based on given.
     * 
     * @param companyID
     * @param projectID
     * @return
     */
    public static String constructPattern(long companyID, Long projectID) {
	return String.format(RedisKeyRegistry.KEY_PROJECT_PAYROLL, companyID, projectID, "*");
    }

    /**
     * Get the key of this payroll.
     */
    @Override
    public String getKey() {
	// company:%s:project:%s:payroll:%s
	return String.format(RedisKeyRegistry.KEY_PROJECT_PAYROLL, this.company.getId(),
		this.project.getId(), this.uuid);
    }

    public UUID getUuid() {
	return uuid;
    }

    public void setUuid(UUID uuid) {
	this.uuid = uuid;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof ProjectPayroll ? ((ProjectPayroll) obj).getKey().equals(getKey()) : false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }
}
