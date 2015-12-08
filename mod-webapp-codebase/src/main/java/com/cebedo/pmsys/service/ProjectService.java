package com.cebedo.pmsys.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.constants.RegistryCache;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.enums.StatusProject;
import com.cebedo.pmsys.enums.StatusTask;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Project;

public interface ProjectService {

    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#project.id")
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'INVENTORY_CREATE')")
    public String uploadExcelMaterials(MultipartFile multipartFile, Project project, Delivery delivery,
	    BindingResult result);

    /**
     * Mass create estimate costs.
     * 
     * @param multipartFile
     * @param project
     * @param result
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#project.id")
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'ESTIMATE_CREATE')")
    public String uploadExcelCosts(MultipartFile multipartFile, Project project, BindingResult result);

    /**
     * Create a new project.
     * 
     * @param project
     * @param result
     * @return
     */
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY')")
    public String create(Project project, BindingResult result);

    /**
     * Update a project.
     * 
     * @param project
     * @param result
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#project.id")
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'CONTRACT_UPDATE')")
    public String update(Project project, BindingResult result);

    /**
     * Delete a project.
     * 
     * @param id
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#id")
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY')")
    public String delete(long id);

    /**
     * Mass create and assign staff members.
     * 
     * @param multipartFile
     * @param proj
     * @param result
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#proj.id")
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'STAFF_CREATE')")
    public String uploadExcelStaff(MultipartFile multipartFile, Project proj, BindingResult result);

    /**
     * Mass create tasks in the Project of Works.
     * 
     * @param multipartFile
     * @param project
     * @param result
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#project.id")
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'PROGRAM_OF_WORKS_CREATE')")
    public String uploadExcelTasks(MultipartFile multipartFile, Project project, BindingResult result);

    /**
     * Clear the Actual Completion Date value.
     * 
     * @param proj
     * @return
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#project.id")
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'CONTRACT_UPDATE')")
    public String clearActualCompletionDate(Project project);

    /**
     * Update the status of the project.
     * 
     * @param projectID
     *            ID of the project.
     * @param projectStatusID
     *            ID of the project status.
     * @return Response message to the user.
     */
    @CacheEvict(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#projID")
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'CONTRACT_UPDATE')")
    public String mark(long projID, int projectStatusID);

    /**
     * Get the list of audit logs in a specific project.
     * 
     * @param projectID
     * @return Set of audit logs of the project.
     */
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'LOGS_VIEW')")
    public Set<AuditLog> logs(long projectID);

    public Project getByID(long projectID);

    public List<Project> list();

    public List<Project> listWithAllCollections();

    @Cacheable(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#id")
    public Project getByIDWithAllCollections(long id);

    @Cacheable(value = RegistryCache.PROJECT_GET_WITH_COLLECTIONS, key = "#id")
    public Project getByIDWithAllCollections(long id, boolean override);

    public String getGanttJSON(Project proj);

    public Map<StatusTask, Integer> getTaskStatusCountMap(Project proj);

    public String getCalendarJSON(Project proj);

    /**
     * Export an {@link HSSFWorkbook} Excel XLS file containing a
     * {@link Project} analysis from start to end {@link Date}.
     * 
     * @param projectID
     *            ID of the {@link Project}.
     * @return {@link HSSFWorkbook} Excel XLS file which contains the balance
     *         sheet.
     */
    public HSSFWorkbook exportXLSAnalysis(long projectID);

    /**
     * Export an {@link HSSFWorkbook} Excel XLS file containing a balance sheet
     * <br>
     * from start to end (if project is {@link StatusProject#COMPLETED}) or<br>
     * from start to current {@link Date} (if {@link StatusProject#NEW} or
     * {@link StatusProject#ONGOING}).
     * 
     * @param projectID
     *            ID of the {@link Project}.
     * @return {@link HSSFWorkbook} Excel XLS file which contains the balance
     *         sheet.
     */
    public HSSFWorkbook exportXLSBalanceSheet(long projectID);

    /**
     * Export an {@link HSSFWorkbook} Excel XLS file containing a balance sheet
     * from the given start and end {@link Date}.
     * 
     * @param projectID
     *            ID of the {@link Project}.
     * @param startDate
     *            Start {@link Date} of the balance sheet.
     * @param endDate
     *            End {@link Date} of the balance sheet.
     * @return {@link HSSFWorkbook} Excel XLS file which contains the balance
     *         sheet.
     */
    public HSSFWorkbook exportXLSBalanceSheet(long projectID, Date startDate, Date endDate);

    public String getGanttJSON(Project proj, boolean override);

    public String getCalendarJSON(Project proj, boolean override);

    public Map<StatusTask, Integer> getTaskStatusCountMap(Project proj, boolean override);

}