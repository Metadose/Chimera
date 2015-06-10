package com.cebedo.pmsys.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.wrapper.ProjectPayrollWrapper;

public interface ProjectService {

    /**
     * Create a new project.
     * 
     * @param project
     * @return
     */
    public String create(Project project);

    public Project getByID(long projectID);

    /**
     * Update a project.
     * 
     * @param project
     * @return
     */
    public String update(Project project);

    /**
     * Delete a project.
     * 
     * @param id
     * @return
     */
    public String delete(long id);

    public List<Project> list();

    public List<Project> listWithAllCollections();

    public Project getByIDWithAllCollections(long id);

    public List<Project> listWithTasks();

    public String getNameByID(long projectID);

    public void clearProjectCache(long projectID);

    public void clearSearchCache(Long companyID);

    public String getGanttJSON(Project proj);

    public Map<String, Object> getTimelineSummaryMap(Project proj);

    public Map<TaskStatus, Integer> getTaskStatusCountMap(Project proj);

    public String getCalendarJSON(Project proj);

    public void clearListCache();

    public List<Staff> getAllStaff(Project proj);

    public Map<String, Object> getProjectStructureMap(Project proj,
	    Date startDate, Date endDate);

    public List<Staff> getAllManagers(Project proj);

    public String getPayrollJSON(Project proj, Date startDate, Date endDate,
	    ProjectPayroll getPayrollJSON);

    public List<ProjectPayrollWrapper> getAllPayrolls(Project proj);

    public List<Staff> getAllManagersWithUsers(Project proj);

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

}