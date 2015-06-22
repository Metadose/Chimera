package com.cebedo.pmsys.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.cebedo.pmsys.bean.PayrollIncludeStaffBean;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.model.Project;

public interface ProjectPayrollService {

    public void rename(ProjectPayroll obj, String newKey);

    public void multiSet(Map<String, ProjectPayroll> m);

    public void set(ProjectPayroll obj);

    public void delete(Collection<String> keys);

    public String delete(String key);

    public void setIfAbsent(ProjectPayroll obj);

    public ProjectPayroll get(String key);

    public Set<String> keys(String pattern);

    public Collection<ProjectPayroll> multiGet(Collection<String> keys);

    public String setAndGetResultJSON(Project proj, Date startDate,
	    Date endDate, ProjectPayroll projectPayroll);

    /**
     * Create or update a payroll.
     * 
     * @param session
     * @param proj
     * @param projectPayroll
     * @return
     */
    public String createPayroll(HttpSession session, Project proj,
	    ProjectPayroll projectPayroll);

    /**
     * Update the payroll then clear the computation.
     * 
     * @param session
     * @param projectPayroll
     * @param toClear
     * @return
     */
    public String createPayrollClearComputation(HttpSession session,
	    ProjectPayroll projectPayroll, String toClear);

    public String getPayrollGrandTotalAsString(List<ProjectPayroll> payrollList);

    public String includeStaffToPayroll(ProjectPayroll projectPayroll,
	    PayrollIncludeStaffBean includeStaffBean);

    public String getPayrollJSON(Project proj, Date startDate, Date endDate,
	    ProjectPayroll getPayrollJSON);

    public List<ProjectPayroll> getAllPayrolls(Project proj);

}
