package com.cebedo.pmsys.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.bean.PairCountValue;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;

public interface StaffService {

    /**
     * Create a new staff.
     * 
     * @param staff
     * @return
     */
    public String create(Staff staff);

    public Staff getByID(long id);

    /**
     * Update a staff.
     * 
     * @param staff
     * @return
     */
    public String update(Staff staff);

    /**
     * Delete a staff.
     * 
     * @param id
     * @return
     */
    public String delete(long id);

    public List<Staff> list();

    public List<Staff> list(Long companyID);

    public List<Staff> listWithAllCollections();

    public Staff getWithAllCollectionsByID(long id);

    /**
     * 
     * @param staffID
     * @return
     */

    /**
     * 
     * @param staffID
     * @return
     */

    /**
     * 
     * @param stAssign
     * @return
     */

    public List<Staff> listUnassignedInProject(Long companyID, Project project);

    /**
     * Create a staff from a specific origin.
     * 
     * @param staff
     * @param origin
     * @param originID
     * @return
     */
    public String createFromOrigin(Staff staff, String origin, String originID);

    public String getCalendarJSON(Set<Attendance> attendanceList);

    public String getGanttJSON(Staff staff);

    public Map<TaskStatus, Integer> getTaskStatusCountMap(Staff staff);

    public Map<AttendanceStatus, PairCountValue> getAttendanceStatusCountMap(
	    Set<Attendance> attendanceList);

    /**
     * List all staff from company except.
     * 
     * @param coID
     * @param staff
     * @return
     */
    public List<Staff> listExcept(Long coID, Set<Staff> staff);

    public String assignStaffMass(Project project);

    public String unassignStaffMember(Project project, long staffID);

    public String unassignAllStaffMembers(Project project);

    public List<Staff> listUnassignedStaffInProject(Long companyID, Project proj);

    public List<Staff> listUnassignedStaffInProjectPayroll(Long companyID, ProjectPayroll projectPayroll);

    /**
     * List staff with users, and filter by given set.
     * 
     * @param companyID
     * @param managers
     * @return
     */

    public List<Staff> convertExcelToStaffList(MultipartFile multipartFile, Company company);

    public List<Staff> createOrGetStaffInList(List<Staff> staffList);

}
