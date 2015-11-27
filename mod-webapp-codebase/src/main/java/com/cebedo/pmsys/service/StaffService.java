package com.cebedo.pmsys.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.bean.PairCountValue;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.StatusAttendance;
import com.cebedo.pmsys.enums.StatusTask;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;

public interface StaffService {

    /**
     * Create a new staff.
     * 
     * @param staff
     * @param result
     * @return
     */
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'STAFF_CREATE')")
    public String create(Staff staff, BindingResult result);

    /**
     * Update a staff.
     * 
     * @param staff
     * @return
     */
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'STAFF_UPDATE')")
    public String update(Staff staff, BindingResult result);

    /**
     * Delete a staff.
     * 
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'STAFF_DELETE')")
    public String delete(long id);

    /**
     * Export list of staff members.
     * 
     * @param projID
     * @return
     */
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'STAFF_VIEW')")
    public HSSFWorkbook exportXLS(long projID);

    /**
     * Assign a staff member to a project.
     * 
     * @param project
     * @return
     */
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'STAFF_UPDATE')")
    public String assignStaffMass(Project project);

    /**
     * Unassign a staff member from a project.
     * 
     * @param project
     * @param staffID
     * @return
     */
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'STAFF_UPDATE')")
    public String unassignStaffMember(Project project, long staffID);

    /**
     * Unassign all staff members in a project.
     * 
     * @param project
     * @return
     */
    @PreAuthorize("hasAnyRole('ADMIN_COMPANY', 'STAFF_UPDATE')")
    public String unassignAllStaffMembers(Project project);

    public List<Staff> listUnassignedStaffInProjectPayroll(Long companyID,
	    ProjectPayroll projectPayroll);

    public List<Staff> convertExcelToStaffList(MultipartFile multipartFile, Company company);

    public List<Staff> refineStaffList(List<Staff> staffList, BindingResult result);

    public Staff getByID(long id);

    public List<Staff> list();

    public List<Staff> list(Long companyID);

    public List<Staff> listWithAllCollections();

    public Staff getWithAllCollectionsByID(long id);

    public List<Staff> listUnassignedInProject(Long companyID, Project project);

    public String getCalendarJSON(Set<Attendance> attendanceList);

    public String getGanttJSON(Staff staff);

    public Map<StatusTask, Integer> getTaskStatusCountMap(Staff staff);

    public Map<StatusAttendance, PairCountValue> getAttendanceStatusCountMap(
	    Set<Attendance> attendanceList);

    public List<Staff> listExcept(Long coID, Set<Staff> staff);

    public List<Staff> listUnassignedInProject(Long companyID, Project proj, boolean override);

}
