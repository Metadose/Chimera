package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.PayrollComputationResult;
import com.cebedo.pmsys.bean.PayrollIncludeStaffBean;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.PayrollStatus;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.repository.ProjectPayrollValueRepo;
import com.cebedo.pmsys.service.ProjectAuxService;
import com.cebedo.pmsys.service.ProjectPayrollComputerService;
import com.cebedo.pmsys.service.ProjectPayrollService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DateUtils;
import com.cebedo.pmsys.utils.NumberFormatUtils;

@Service
public class ProjectPayrollServiceImpl implements ProjectPayrollService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();

    public static final String PROPERTY_GRAND_TOTAL = "Grand Total";

    private ProjectPayrollValueRepo projectPayrollValueRepo;
    private ProjectPayrollComputerService projectPayrollComputerService;
    private StaffDAO staffDAO;
    private ProjectAuxService projectAuxService;

    public void setProjectAuxService(ProjectAuxService projectAuxService) {
	this.projectAuxService = projectAuxService;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
	this.staffDAO = staffDAO;
    }

    public void setProjectPayrollComputerService(
	    ProjectPayrollComputerService projectPayrollComputerService) {
	this.projectPayrollComputerService = projectPayrollComputerService;
    }

    public void setProjectPayrollValueRepo(ProjectPayrollValueRepo projectPayrollValueRepo) {
	this.projectPayrollValueRepo = projectPayrollValueRepo;
    }

    @Override
    @Transactional
    public String computeAndGetResultJSON(Project proj, Date startDate, Date endDate,
	    ProjectPayroll projectPayroll) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.COMPUTE, Project.OBJECT_NAME, proj.getId(),
		RedisConstants.OBJECT_PAYROLL, projectPayroll.getKey());

	String payrollJSON = getPayrollJSON(proj, startDate, endDate, projectPayroll);

	// Get the resulting state of the computation.
	// And save it.
	PayrollComputationResult payrollComputationResult = this.projectPayrollComputerService
		.getPayrollResult();
	projectPayroll.setPayrollComputationResult(payrollComputationResult);
	projectPayroll.setLastComputed(new Date(System.currentTimeMillis()));
	projectPayroll.setPayrollJSON(payrollJSON);

	// Add the total result to the
	// grand total of the whole project.
	ProjectAux projectAux = this.projectAuxService.get(proj);

	// Revert back to old grand total.
	// Old payroll object.
	ProjectPayroll oldPayroll = this.projectPayrollValueRepo.get(projectPayroll.getKey());
	double oldGrandTotal = projectAux.getGrandTotalPayroll();
	double oldPayrollResult = oldPayroll.getPayrollComputationResult() == null ? 0 : oldPayroll
		.getPayrollComputationResult().getOverallTotalOfStaff();
	double revertedGrandTotal = oldGrandTotal - oldPayrollResult;

	// Get new payroll result.
	// Construct new grand total.
	double newPayrollResult = payrollComputationResult.getOverallTotalOfStaff();
	double newGrandTotal = revertedGrandTotal + newPayrollResult;

	// The new grandtotal.
	// Then save it.
	projectAux.setGrandTotalPayroll(newGrandTotal);
	this.projectAuxService.set(projectAux);
	this.projectPayrollValueRepo.set(projectPayroll);

	// Return the result earlier.
	return payrollJSON;
    }

    @Override
    @Transactional
    public ProjectPayroll get(String key) {

	ProjectPayroll obj = this.projectPayrollValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_PAYROLL, obj.getKey());
	    return new ProjectPayroll();
	}
	// Log.
	this.messageHelper.send(AuditAction.GET, RedisConstants.OBJECT_PAYROLL, obj.getKey());

	return obj;
    }

    /**
     * Delete a payroll entry.
     */
    @Transactional
    @Override
    public String delete(String key) {

	ProjectPayroll payroll = this.projectPayrollValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(payroll)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_PAYROLL, payroll.getKey());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.DELETE, RedisConstants.OBJECT_PAYROLL, payroll.getKey());

	// Revert the grand total in project auxillary.
	// Get the aux obj.
	ProjectAux projAux = this.projectAuxService.get(payroll.getProject());

	// Get value.
	// Recompute reverted value.
	double totalAllPayrolls = projAux.getGrandTotalPayroll();
	double totalThisPayroll = payroll.getPayrollComputationResult().getOverallTotalOfStaff();
	double revertedTotal = totalAllPayrolls - totalThisPayroll;

	// Set the reverted value.
	// Save it.
	projAux.setGrandTotalPayroll(revertedTotal);
	this.projectAuxService.set(projAux);

	// Delete the payroll object.
	this.projectPayrollValueRepo.delete(key);

	// Return.
	return AlertBoxGenerator.SUCCESS.generateDelete(RedisConstants.OBJECT_PAYROLL,
		payroll.getStartEndDisplay());
    }

    /**
     * Get the grand total value as string, formatted as currency.
     */
    @Transactional
    @Override
    public String getPayrollGrandTotalAsString(List<ProjectPayroll> payrollList) {
	double total = 0;

	Project proj = null;
	for (ProjectPayroll payroll : payrollList) {

	    // Security check.
	    if (!this.authHelper.isActionAuthorized(payroll)) {
		this.messageHelper.unauthorized(RedisConstants.OBJECT_PAYROLL, payroll.getKey());
		return NumberFormatUtils.getCurrencyFormatter().format(0);
	    }

	    proj = proj == null ? payroll.getProject() : proj;

	    PayrollComputationResult result = payroll.getPayrollComputationResult();
	    if (result != null) {
		total += result.getOverallTotalOfStaff();
	    }
	}
	// Log.
	this.messageHelper.send(AuditAction.GET, Project.OBJECT_NAME, proj.getId(),
		RedisConstants.OBJECT_PAYROLL, PROPERTY_GRAND_TOTAL);
	return NumberFormatUtils.getCurrencyFormatter().format(total);
    }

    @Transactional
    @Override
    public String createPayroll(Project proj, ProjectPayroll projectPayroll) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(projectPayroll)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Take a snapshot of the project structure
	// during the creation of the payroll.
	boolean isUpdating = projectPayroll.isSaved();

	// Preserve list of managers during this time.
	projectPayroll.setStaffList(proj.getAssignedStaff());

	// Generate actual object from form ID.
	projectPayroll.setStatus(PayrollStatus.of(projectPayroll.getStatusID()));

	// Preserve project structure.
	projectPayroll.setSaved(true);

	// Date parts of the response.
	String response = "";
	String datePart = getResponseDatePart(projectPayroll);

	// Generate response.
	if (isUpdating) {
	    response = AlertBoxGenerator.SUCCESS.generateUpdatePayroll(RedisConstants.OBJECT_PAYROLL,
		    datePart);
	} else {
	    response = AlertBoxGenerator.SUCCESS.generateCreate(RedisConstants.OBJECT_PAYROLL, datePart);
	    projectPayroll.setUuid(UUID.randomUUID());
	}

	// Set the new values.
	this.projectPayrollValueRepo.set(projectPayroll);

	// Log.
	this.messageHelper.send(AuditAction.CREATE, RedisConstants.OBJECT_PAYROLL,
		projectPayroll.getKey());

	return response;
    }

    /**
     * Get date part of the alert box response.
     * 
     * @param projectPayroll
     * @return
     */
    // TODO Transfer this somewhere else, like DateUtils, or whatever.
    @Deprecated
    public static String getResponseDatePart(ProjectPayroll projectPayroll) {
	// Date parts of the response.
	String startStr = DateUtils.formatDate(projectPayroll.getStartDate(), "yyyy/MM/dd");
	String endStr = DateUtils.formatDate(projectPayroll.getEndDate(), "yyyy/MM/dd");
	String datePart = startStr + " to " + endStr;
	return datePart;
    }

    @Transactional
    @Override
    public String getPayrollJSON(Project proj, Date startDate, Date endDate,
	    ProjectPayroll projectPayroll) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(projectPayroll)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_PAYROLL, projectPayroll.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// Do the computation.
	this.projectPayrollComputerService.compute(startDate, endDate, projectPayroll);

	// Log.
	this.messageHelper.send(AuditAction.GET_JSON, Project.OBJECT_NAME, proj.getId(),
		RedisConstants.OBJECT_PAYROLL, projectPayroll.getKey());

	// Return the JSON equivalent of the result.
	return this.projectPayrollComputerService.getPayrollJSONResult();
    }

    /**
     * Get all payrolls given a project.
     */
    @Transactional
    @Override
    public List<ProjectPayroll> getAllPayrolls(Project proj) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return new ArrayList<ProjectPayroll>();
	}

	// Log.
	this.messageHelper.send(AuditAction.LIST, Project.OBJECT_NAME, proj.getId(),
		RedisConstants.OBJECT_PAYROLL);

	// Get the needed ID's for the key.
	// Construct the key.
	long companyID = proj.getCompany() == null ? 0 : proj.getCompany().getId();
	String pattern = ProjectPayroll.constructPattern(companyID, proj.getId());

	// Get all keys based on pattern.
	// Multi-get all objects based on keys.
	Set<String> keys = this.projectPayrollValueRepo.keys(pattern);
	List<ProjectPayroll> projectPayrolls = this.projectPayrollValueRepo.multiGet(keys);

	// Sort the list in descending order.
	Collections.sort(projectPayrolls, new Comparator<ProjectPayroll>() {
	    @Override
	    public int compare(ProjectPayroll aObj, ProjectPayroll bObj) {
		Date aStart = aObj.getStartDate();
		Date bStart = bObj.getStartDate();

		// To sort in ascending,
		// remove Not's.
		return !(aStart.before(bStart)) ? -1 : !(aStart.after(bStart)) ? 1 : 0;
	    }
	});

	return projectPayrolls;
    }

    @Transactional
    @Override
    public String updatePayrollClearComputation(HttpSession session, ProjectPayroll projectPayroll,
	    String toClear) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(projectPayroll)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_PAYROLL, projectPayroll.getKey());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.UPDATE, RedisConstants.OBJECT_PAYROLL,
		projectPayroll.getKey());

	// If the update button is clicked from the "right-side"
	// project structure checkboxes, reset the payroll JSON.
	if (toClear.equals("computation")) {
	    projectPayroll.setPayrollJSON(null);
	    projectPayroll.setLastComputed(null);
	}

	// Transform the selected ID's into actual objects.
	// Store the actual objects in the payroll object.
	long[] staffIDs = projectPayroll.getStaffIDs();
	Set<Staff> assignedStaffList = new HashSet<Staff>();
	for (long staffID : staffIDs) {
	    Staff staff = this.staffDAO.getWithAllCollectionsByID(staffID);
	    assignedStaffList.add(staff);
	}
	projectPayroll.setAssignedStaffList(assignedStaffList);

	// Do store.
	this.projectPayrollValueRepo.set(projectPayroll);

	// Generate response.
	String datePart = getResponseDatePart(projectPayroll);
	String response = AlertBoxGenerator.SUCCESS.generateUpdatePayroll(RedisConstants.OBJECT_PAYROLL,
		datePart);
	return response;
    }

    @Transactional
    @Override
    public String includeStaffToPayroll(ProjectPayroll projectPayroll,
	    PayrollIncludeStaffBean includeStaffBean) {
	// Security check.
	if (!this.authHelper.isActionAuthorized(projectPayroll)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_PAYROLL, projectPayroll.getKey());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.UPDATE, RedisConstants.OBJECT_PAYROLL,
		projectPayroll.getKey());

	Set<Staff> staffList = projectPayroll.getStaffList();
	Staff staff = this.staffDAO.getByID(includeStaffBean.getStaffID());
	staffList.add(staff);
	projectPayroll.setStaffList(staffList);
	this.projectPayrollValueRepo.set(projectPayroll);

	return AlertBoxGenerator.SUCCESS.generateInclude(Staff.OBJECT_NAME, staff.getFullName());
    }

}
