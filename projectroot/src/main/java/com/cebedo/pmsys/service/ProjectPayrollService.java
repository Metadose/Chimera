package com.cebedo.pmsys.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.pojo.FormPayrollIncludeStaff;

public interface ProjectPayrollService {

    public HSSFWorkbook exportXLSAll(Project proj);

    public HSSFWorkbook exportXLS(String payrollKey);

    public String delete(String key);

    public ProjectPayroll get(String key);

    public String compute(Project proj, Date startDate, Date endDate, ProjectPayroll projectPayroll);

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
    public String updatePayroll(HttpSession session, ProjectPayroll projectPayroll, String toClear);

    public String getPayrollGrandTotalAsString(List<ProjectPayroll> payrollList);

    public String includeStaffToPayroll(ProjectPayroll projectPayroll,
	    FormPayrollIncludeStaff includeStaffBean);

    public String getPayrollJSON(Project proj, Date startDate, Date endDate,
	    ProjectPayroll getPayrollJSON);

    public List<ProjectPayroll> listDesc(Project proj);

    public List<ProjectPayroll> listAsc(Project proj);

}
