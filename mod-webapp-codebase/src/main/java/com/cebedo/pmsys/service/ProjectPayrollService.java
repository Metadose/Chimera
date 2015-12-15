package com.cebedo.pmsys.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;

import com.cebedo.pmsys.base.IObjectExpense;
import com.cebedo.pmsys.constants.RegistryCache;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.pojo.FormPayrollIncludeStaff;

public interface ProjectPayrollService {

    @PreAuthorize("hasRole('ADMIN_SUPER')")
    public String computeAll(Project proj);

    public List<IObjectExpense> listDescExpense(Project proj);

    public List<IObjectExpense> listDescExpense(Project proj, Date startDate, Date endDate);

    public HSSFWorkbook exportXLSAll(Project proj);

    public HSSFWorkbook exportXLS(String payrollKey);

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectId")
    public String delete(String key, long projectId);

    public ProjectPayroll get(String key);

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#proj.id")
    public String compute(Project proj, Date startDate, Date endDate, ProjectPayroll projectPayroll);

    /**
     * Create or update a payroll.
     * 
     * @param session
     * @param proj
     * @param projectPayroll
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#proj.id")
    public String createPayroll(Project proj, ProjectPayroll projectPayroll);

    /**
     * Update the payroll then clear the computation.
     * 
     * @param session
     * @param projectPayroll
     * @param toClear
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projectPayroll.project.id")
    public String updatePayroll(HttpSession session, ProjectPayroll projectPayroll, String toClear);

    public String getPayrollGrandTotalAsString(List<ProjectPayroll> payrollList);

    public String includeStaffToPayroll(ProjectPayroll projectPayroll,
	    FormPayrollIncludeStaff includeStaffBean);

    public String getPayrollJSON(Project proj, Date startDate, Date endDate,
	    ProjectPayroll getPayrollJSON);

    public List<ProjectPayroll> listDesc(Project proj);

    public List<ProjectPayroll> listAsc(Project proj);

    public List<ProjectPayroll> listDesc(Project proj, Date startDate, Date endDate);

    public int getSize(List<IObjectExpense> objs);

    public List<ProjectPayroll> listAsc(Project proj, boolean override);

}
