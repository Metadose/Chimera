package com.cebedo.pmsys.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.pojo.FormPayrollIncludeStaff;

public interface ProjectPayrollService {

    public String delete(String key);

    public ProjectPayroll get(String key);

    public String computeAndGetResultJSON(Project proj, Date startDate, Date endDate,
	    ProjectPayroll projectPayroll);

    /**
     * Create or update a payroll.
     * 
     * @param session
     * @param proj
     * @param projectPayroll
     * @return
     */
    public String createPayroll(Project proj, ProjectPayroll projectPayroll);

    /**
     * Update the payroll then clear the computation.
     * 
     * @param session
     * @param projectPayroll
     * @param toClear
     * @return
     */
    public String updatePayrollClearComputation(HttpSession session, ProjectPayroll projectPayroll,
	    String toClear);

    public String getPayrollGrandTotalAsString(List<ProjectPayroll> payrollList);

    public String includeStaffToPayroll(ProjectPayroll projectPayroll,
	    FormPayrollIncludeStaff includeStaffBean);

    public String getPayrollJSON(Project proj, Date startDate, Date endDate,
	    ProjectPayroll getPayrollJSON);

    public List<ProjectPayroll> getAllPayrolls(Project proj);

}
