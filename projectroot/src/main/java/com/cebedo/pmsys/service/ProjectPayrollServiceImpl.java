package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.PayrollComputationResult;
import com.cebedo.pmsys.bean.PayrollIncludeStaffBean;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.SystemUserDAO;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.PayrollStatus;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.assignment.ManagerAssignment;
import com.cebedo.pmsys.repository.ProjectPayrollValueRepo;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DateUtils;
import com.cebedo.pmsys.utils.NumberFormatUtils;

@Service
public class ProjectPayrollServiceImpl implements ProjectPayrollService {

    private ProjectPayrollValueRepo projectPayrollValueRepo;
    private ProjectService projectService;
    private SystemUserDAO systemUserDAO;
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

    public void setSystemUserDAO(SystemUserDAO systemUserDAO) {
	this.systemUserDAO = systemUserDAO;
    }

    public void setProjectService(ProjectService projectService) {
	this.projectService = projectService;
    }

    public void setProjectPayrollValueRepo(
	    ProjectPayrollValueRepo projectPayrollValueRepo) {
	this.projectPayrollValueRepo = projectPayrollValueRepo;
    }

    @Override
    @Transactional
    public String setAndGetResultJSON(Project proj, Date startDate,
	    Date endDate, ProjectPayroll projectPayroll) {

	String payrollJSON = getPayrollJSON(proj, startDate, endDate,
		projectPayroll);

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
	ProjectPayroll oldPayroll = this.projectPayrollValueRepo
		.get(projectPayroll.getKey());
	double oldGrandTotal = projectAux.getGrandTotalPayroll();
	double oldPayrollResult = oldPayroll.getPayrollComputationResult() == null ? 0
		: oldPayroll.getPayrollComputationResult()
			.getOverallTotalOfStaff();
	double revertedGrandTotal = oldGrandTotal - oldPayrollResult;

	// Get new payroll result.
	// Construct new grand total.
	double newPayrollResult = payrollComputationResult
		.getOverallTotalOfStaff();
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
    public void rename(ProjectPayroll obj, String newKey) {
	this.projectPayrollValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, ProjectPayroll> m) {
	this.projectPayrollValueRepo.multiSet(m);
    }

    @Override
    @Transactional
    public void set(ProjectPayroll obj) {
	this.projectPayrollValueRepo.set(obj);
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.projectPayrollValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(ProjectPayroll obj) {
	this.projectPayrollValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public ProjectPayroll get(String key) {
	return this.projectPayrollValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.projectPayrollValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<ProjectPayroll> multiGet(Collection<String> keys) {
	return this.projectPayrollValueRepo.multiGet(keys);
    }

    /**
     * Delete a payroll entry.
     */
    @Transactional
    @Override
    public String delete(String key) {

	ProjectPayroll payroll = this.projectPayrollValueRepo.get(key);

	// Revert the grand total in project auxillary.
	// Get the aux obj.
	ProjectAux projAux = this.projectAuxService.get(payroll.getProject());

	// Get value.
	// Recompute reverted value.
	double totalAllPayrolls = projAux.getGrandTotalPayroll();
	double totalThisPayroll = payroll.getPayrollComputationResult()
		.getOverallTotalOfStaff();
	double revertedTotal = totalAllPayrolls - totalThisPayroll;

	// Set the reverted value.
	// Save it.
	projAux.setGrandTotalPayroll(revertedTotal);
	this.projectAuxService.set(projAux);

	// Delete the payroll object.
	this.projectPayrollValueRepo.delete(key);

	// Return.
	return AlertBoxGenerator.SUCCESS.generateDelete(
		RedisConstants.OBJECT_PAYROLL, payroll.getStartEndDisplay());
    }

    /**
     * Get the grand total value as string, formatted as currency.
     */
    @Transactional
    @Override
    public String getPayrollGrandTotalAsString(List<ProjectPayroll> payrollList) {
	double total = 0;
	for (ProjectPayroll payroll : payrollList) {
	    PayrollComputationResult result = payroll
		    .getPayrollComputationResult();
	    if (result != null) {
		total += result.getOverallTotalOfStaff();
	    }
	}
	return NumberFormatUtils.getCurrencyFormatter().format(total);
    }

    @Transactional
    @Override
    public String createPayroll(HttpSession session, Project proj,
	    ProjectPayroll projectPayroll) {

	// Take a snapshot of the project structure
	// during the creation of the payroll.
	boolean isUpdating = projectPayroll.isSaved();

	// Get all managers in this project.
	// Preserve list of managers during this time.
	// FIXME What if manager/approver is deleted? Payroll is orphaned.
	Set<ManagerAssignment> managers = this.projectService
		.getAllManagersAssignmentsWithUsers(proj);
	projectPayroll.setManagerAssignments(managers);
	projectPayroll.setStaffList(proj.getAssignedStaff());

	// Generate actual object from form ID.
	SystemUser approver = this.systemUserDAO
		.getWithSecurityByID(projectPayroll.getApproverID());
	projectPayroll.setApprover(approver);
	projectPayroll
		.setStatus(PayrollStatus.of(projectPayroll.getStatusID()));

	// Preserve project structure.
	projectPayroll.setSaved(true);

	// Date parts of the response.
	String response = "";
	String datePart = getResponseDatePart(projectPayroll);

	// Generate response.
	if (isUpdating) {
	    response = AlertBoxGenerator.SUCCESS.generateUpdatePayroll(
		    RedisConstants.OBJECT_PAYROLL, datePart);
	} else {
	    response = AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_PAYROLL, datePart);
	    projectPayroll.setUuid(UUID.randomUUID());
	}

	// Set the new values.
	this.projectPayrollValueRepo.set(projectPayroll);

	return response;
    }

    /**
     * Get date part of the alert box response.
     * 
     * @param projectPayroll
     * @return
     */
    public static String getResponseDatePart(ProjectPayroll projectPayroll) {
	// Date parts of the response.
	String startStr = DateUtils.formatDate(projectPayroll.getStartDate(),
		"yyyy/MM/dd");
	String endStr = DateUtils.formatDate(projectPayroll.getEndDate(),
		"yyyy/MM/dd");
	String datePart = startStr + " to " + endStr;
	return datePart;
    }

    @Transactional
    @Override
    public String getPayrollJSON(Project proj, Date startDate, Date endDate,
	    ProjectPayroll projectPayroll) {

	// Do the computation.
	this.projectPayrollComputerService.compute(startDate, endDate,
		projectPayroll);

	// Return the JSON equivalent of the result.
	return this.projectPayrollComputerService.getPayrollJSONResult();
    }

    /**
     * Get all payrolls given a project.
     */
    @Transactional
    @Override
    public List<ProjectPayroll> getAllPayrolls(Project proj) {

	// Get the needed ID's for the key.
	// Construct the key.
	long companyID = proj.getCompany() == null ? 0 : proj.getCompany()
		.getId();
	String pattern = ProjectPayroll.constructPattern(companyID,
		proj.getId());

	// Get all keys based on pattern.
	// Multi-get all objects based on keys.
	Set<String> keys = this.projectPayrollValueRepo.keys(pattern);
	List<ProjectPayroll> projectPayrolls = this.projectPayrollValueRepo
		.multiGet(keys);

	// Sort the list in descending order.
	Collections.sort(projectPayrolls, new Comparator<ProjectPayroll>() {
	    @Override
	    public int compare(ProjectPayroll aObj, ProjectPayroll bObj) {
		Date aStart = aObj.getStartDate();
		Date bStart = bObj.getStartDate();

		// To sort in ascending,
		// remove Not's.
		return !(aStart.before(bStart)) ? -1
			: !(aStart.after(bStart)) ? 1 : 0;
	    }
	});

	return projectPayrolls;
    }

    @Transactional
    @Override
    public String createPayrollClearComputation(HttpSession session,
	    ProjectPayroll projectPayroll, String toClear) {

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
	String response = AlertBoxGenerator.SUCCESS.generateUpdatePayroll(
		RedisConstants.OBJECT_PAYROLL, datePart);
	return response;
    }

    @Transactional
    @Override
    public String includeStaffToPayroll(ProjectPayroll projectPayroll,
	    PayrollIncludeStaffBean includeStaffBean) {
	Set<Staff> staffList = projectPayroll.getStaffList();
	Staff staff = this.staffDAO.getByID(includeStaffBean.getStaffID());
	staffList.add(staff);
	projectPayroll.setStaffList(staffList);
	this.projectPayrollValueRepo.set(projectPayroll);

	return AlertBoxGenerator.SUCCESS.generateInclude(Staff.OBJECT_NAME,
		staff.getFullName());
    }

}
