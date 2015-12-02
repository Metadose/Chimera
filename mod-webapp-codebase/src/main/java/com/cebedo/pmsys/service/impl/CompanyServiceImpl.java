package com.cebedo.pmsys.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.FieldDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.dao.SystemUserDAO;
import com.cebedo.pmsys.dao.TaskDAO;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.factory.AlertBoxFactory;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.FileHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.model.SystemUser;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.assignment.FieldAssignment;
import com.cebedo.pmsys.pojo.FormMultipartFile;
import com.cebedo.pmsys.repository.impl.AttendanceValueRepoImpl;
import com.cebedo.pmsys.repository.impl.ProjectAuxValueRepoImpl;
import com.cebedo.pmsys.service.CompanyService;
import com.cebedo.pmsys.service.SystemConfigurationService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.utils.ImageUtils;
import com.cebedo.pmsys.validator.CompanyValidator;
import com.cebedo.pmsys.validator.ImageUploadValidator;

@Service
public class CompanyServiceImpl implements CompanyService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    private CompanyDAO companyDAO;
    private ProjectAuxValueRepoImpl projectAuxValueRepo;
    private SystemConfigurationService systemConfigurationService;
    private StaffDAO staffDAO;
    private SystemUserDAO systemUserDAO;
    private SystemConfigurationDAO systemConfigurationDAO;
    private ProjectDAO projectDAO;
    private TaskDAO taskDAO;
    private FieldDAO fieldDAO;
    private AttendanceValueRepoImpl attendanceValueRepo;

    @Autowired
    @Qualifier(value = "attendanceValueRepo")
    public void setAttendanceValueRepo(AttendanceValueRepoImpl r) {
	this.attendanceValueRepo = r;
    }

    @Autowired
    @Qualifier(value = "fieldDAO")
    public void setFieldDAO(FieldDAO fieldDAO) {
	this.fieldDAO = fieldDAO;
    }

    @Autowired
    @Qualifier(value = "taskDAO")
    public void setTaskDAO(TaskDAO taskDAO) {
	this.taskDAO = taskDAO;
    }

    @Autowired
    @Qualifier(value = "projectDAO")
    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    @Autowired
    @Qualifier(value = "systemConfigurationDAO")
    public void setSystemConfigurationDAO(SystemConfigurationDAO systemConfigurationDAO) {
	this.systemConfigurationDAO = systemConfigurationDAO;
    }

    @Autowired
    @Qualifier(value = "systemUserDAO")
    public void setSystemUserDAO(SystemUserDAO systemUserDAO) {
	this.systemUserDAO = systemUserDAO;
    }

    @Autowired
    @Qualifier(value = "staffDAO")
    public void setStaffDAO(StaffDAO staffDAO) {
	this.staffDAO = staffDAO;
    }

    @Autowired
    @Qualifier(value = "systemConfigurationService")
    public void setSystemConfigurationService(SystemConfigurationService systemConfigurationService) {
	this.systemConfigurationService = systemConfigurationService;
    }

    @Autowired
    @Qualifier(value = "projectAuxValueRepo")
    public void setProjectAuxValueRepo(ProjectAuxValueRepoImpl projectAuxValueRepo) {
	this.projectAuxValueRepo = projectAuxValueRepo;
    }

    public void setCompanyDAO(CompanyDAO companyDAO) {
	this.companyDAO = companyDAO;
    }

    @Autowired
    CompanyValidator companyValidator;

    @Autowired
    ImageUploadValidator imageUploadValidator;

    /**
     * Create a new company.
     */
    @Override
    @Transactional
    public String create(Company company, BindingResult result) {

	// Security check.
	if (!this.authHelper.isSuperAdmin()) {
	    this.messageHelper.unauthorizedID(Company.OBJECT_NAME, company.getId());
	    return AlertBoxFactory.ERROR;
	}

	// Service layer form validation.
	this.companyValidator.validate(company, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	this.companyDAO.create(company);

	// Log.
	this.messageHelper.auditableID(AuditAction.ACTION_CREATE, Company.OBJECT_NAME, company.getId(),
		company.getName());

	// Do actual service and construct response.
	return AlertBoxFactory.SUCCESS.generateCreate(Company.OBJECT_NAME, company.getName());
    }

    /**
     * Get a company by ID.
     */
    @Override
    @Transactional
    public Company getByID(long id) {

	Company company = this.companyDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isSuperAdmin()) {
	    this.messageHelper.unauthorizedID(Company.OBJECT_NAME, company.getId());
	    return new Company();
	}

	// Log then
	// return actual object.
	this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_GET, Company.OBJECT_NAME,
		company.getId());
	return company;
    }

    /**
     * Get all logs of this company.
     */
    @Override
    @Transactional
    public List<AuditLog> logs() {
	Company company = this.authHelper.getAuth().getCompany();
	Long companyID = company == null ? null : company.getId();
	List<AuditLog> logs = this.companyDAO.logs(companyID);
	for (AuditLog log : logs) {
	    log.setAuditAction(AuditAction.of(log.getAction()));
	}
	return logs;
    }

    /**
     * Update a company.
     */
    @Override
    @Transactional
    public String update(Company company, BindingResult result) {

	// Security check.
	// If the user does not have access to this company,
	// and the user is not a company admin,
	// return an error.
	if (!this.authHelper.hasAccess(company) && !this.authHelper.isCompanyAdmin()) {
	    this.messageHelper.unauthorizedID(Company.OBJECT_NAME, company.getId());
	    return AlertBoxFactory.ERROR;
	}

	// Service layer form validation.
	this.companyValidator.validate(company, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Create post-service operations.
	this.messageHelper.auditableID(AuditAction.ACTION_UPDATE, Company.OBJECT_NAME, company.getId(),
		company.getName());

	// Do actual update to object.
	// Construct alert box response.
	this.companyDAO.update(company);
	updateSecurityContext(company);

	return AlertBoxFactory.SUCCESS.generateUpdate(Company.OBJECT_NAME, company.getName());
    }

    /**
     * Update the stored authentication.
     * 
     * @param company
     */
    private void updateSecurityContext(Company company) {
	if (!this.authHelper.isSuperAdmin()) {
	    AuthenticationToken auth = this.authHelper.getAuth();
	    auth.setCompany(company);
	    auth.setTheme(company.getThemeID());
	    SecurityContextHolder.getContext().setAuthentication(auth);
	}
    }

    /**
     * Deletes a company.
     */
    @Override
    @Transactional
    public String delete(long id) {

	Company company = this.companyDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isSuperAdmin()) {
	    this.messageHelper.unauthorizedID(Company.OBJECT_NAME, company.getId());
	    return AlertBoxFactory.ERROR;
	}

	// Proceed to post-service operations.
	this.messageHelper.auditableID(AuditAction.ACTION_DELETE, Company.OBJECT_NAME, company.getId(),
		company.getName());

	// Delete also linked redis objects.
	// company:4:*
	// company.fk:4:*
	Set<String> keysSet = this.projectAuxValueRepo.keys(String.format("company:%s:*", id));
	Set<String> keysSet2 = this.projectAuxValueRepo.keys(String.format("company.fk:%s:*", id));
	keysSet.addAll(keysSet2);
	this.projectAuxValueRepo.delete(keysSet);

	// Do actual service.
	// Generate response.
	this.companyDAO.delete(id);
	return AlertBoxFactory.SUCCESS.generateDelete(Company.OBJECT_NAME, company.getName());
    }

    /**
     * List all companies.
     */
    @Override
    @Transactional
    public List<Company> list() {

	// Security check.
	if (!this.authHelper.isSuperAdmin()) {
	    this.messageHelper.unauthorized(Company.OBJECT_NAME);
	    return new ArrayList<Company>();
	}

	// List as super admin.
	this.messageHelper.nonAuditableListNoAssoc(AuditAction.ACTION_LIST, Company.OBJECT_NAME);
	return this.companyDAO.list(null);
    }

    @Override
    @Transactional
    public Company settings() {
	AuthHelper authHelper = new AuthHelper();
	long companyID = authHelper.getAuth().getCompany().getId();
	Company company = this.companyDAO.getByID(companyID);
	this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_GET, Company.OBJECT_NAME,
		company.getId());
	return company;
    }

    @Override
    @Transactional
    public String uploadCompanyLogo(Company company, FormMultipartFile companyLogo,
	    BindingResult result) {
	// Security check.
	// If the user does not have access to this company,
	// and the user is not a company admin,
	// return an error.
	if (!this.authHelper.hasAccess(company) && !this.authHelper.isCompanyAdmin()) {
	    this.messageHelper.unauthorizedID(Company.OBJECT_NAME, company.getId());
	    return AlertBoxFactory.ERROR;
	}

	// Do validator here.
	this.imageUploadValidator.validate(companyLogo, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Create post-service operations.
	this.messageHelper.auditableID(AuditAction.ACTION_UPDATE, Company.OBJECT_NAME, company.getId(),
		company.getName());

	// Construct file path to store the logo file.
	String serverHome = this.systemConfigurationService.getServerHome();

	// Get the file.
	MultipartFile file = companyLogo.getFile();

	// Make directories if necessary.
	String companyLogoPath = String.format("%s/company/%s/logo/logo.png", serverHome,
		company.getId());
	FileHelper.checkDirectoryExistence(companyLogoPath);

	try {
	    // 1) Convert the file format to JPG.
	    // 2) Minimize the resolution (width and height).
	    BufferedImage img = ImageIO.read(file.getInputStream());
	    BufferedImage resizedImg = ImageUtils.scale(img, 200);
	    ImageIO.write(resizedImg, "png", new File(companyLogoPath));
	} catch (Exception e) {
	    e.printStackTrace();
	    return AlertBoxFactory.ERROR;
	}

	// Null if no error. See references of this function.
	return null;
    }

    @Transactional
    @Override
    public String clone(long companyId, String cloneName) {
	Company company = this.companyDAO.getByIDWithLazyCollections(companyId);
	if (!this.authHelper.isSuperAdmin()) {
	    this.messageHelper.unauthorizedID(Company.OBJECT_NAME, company.getId());
	    return AlertBoxFactory.ERROR;
	}

	// Clear all logs, reset ID to zero, set name.
	Company clone = company.clone();
	clone.setId(0);
	clone.setName(cloneName);
	clone.setAuditLogs(null);
	clone.setEmployees(null);
	clone.setStaff(null);
	clone.setConfigs(null);
	clone.setProjects(null);
	clone.setTasks(null);
	this.companyDAO.create(clone);

	// Clone the users and the staff members of a company.
	Map<Long, Staff> oldIdToNewStaff = new HashMap<Long, Staff>();
	cloneUsersAndStaff(company, clone, oldIdToNewStaff);

	// Clone the configurations.
	cloneConfigs(company, clone);

	// Clone projects.
	cloneProjects(company, clone, oldIdToNewStaff);

	return AlertBoxFactory.SUCCESS.generateClone(Company.OBJECT_NAME, cloneName);
    }

    /**
     * Get the cloned equivalent of the set.
     * 
     * @param originalStaff
     * @param oldIdToNewStaff
     * @return
     */
    private Set<Staff> getClonedEquivalent(Set<Staff> originalStaff, Map<Long, Staff> oldIdToNewStaff) {
	Set<Staff> clonedStaffSet = new HashSet<Staff>();
	for (Staff originalStf : originalStaff) {
	    clonedStaffSet.add(oldIdToNewStaff.get(originalStf.getId()));
	}
	return clonedStaffSet;
    }

    /**
     * Clone projects.
     * 
     * @param company
     * @param clone
     */
    private void cloneProjects(Company company, Company clone, Map<Long, Staff> oldIdToNewStaff) {

	Set<Project> projects = company.getProjects();
	Set<Task> tasks = company.getTasks();

	// Map of project to list of tasks.
	Map<Long, Set<Task>> projectTaskMap = new HashMap<Long, Set<Task>>();

	// Tasks.
	for (Task originalTask : tasks) {

	    Project originalProj = originalTask.getProject();
	    Task cloneTask = originalTask.clone();
	    cloneTask.setId(0);
	    cloneTask.setCompany(clone);
	    cloneTask.setProject(null);
	    cloneTask.setStaff(getClonedEquivalent(originalTask.getStaff(), oldIdToNewStaff));
	    this.taskDAO.create(cloneTask);

	    // If the set does not exist yet, make it.
	    Set<Task> taskSet = projectTaskMap.get(originalProj.getId());
	    if (taskSet == null) {
		taskSet = new HashSet<Task>();
	    }
	    taskSet.add(cloneTask);
	    projectTaskMap.put(originalProj.getId(), taskSet);
	}

	// Projects.
	for (Project originalProj : projects) {

	    // Get the set of original staff,
	    // loop through the processed map and
	    // get equivalent clones given the ID.
	    Project cloneProj = originalProj.clone();
	    cloneProj.setId(0);
	    cloneProj.setCompany(clone);
	    cloneProj.setAuditLogs(null);

	    // Update collections.
	    Set<Staff> originalStaffSet = originalProj.getAssignedStaff();
	    Set<Staff> clonedStaffSet = getClonedEquivalent(originalStaffSet, oldIdToNewStaff);
	    Set<Task> clonedTasks = projectTaskMap.get(originalProj.getId());
	    Set<FieldAssignment> origianlFAssignments = cloneProj.getAssignedFields();
	    cloneProj.setAssignedFields(null);
	    cloneProj.setAssignedStaff(clonedStaffSet);
	    cloneProj.setAssignedTasks(clonedTasks);
	    this.projectDAO.create(cloneProj);

	    // Assign the fields.
	    if (origianlFAssignments != null) {
		for (FieldAssignment originalAssign : origianlFAssignments) {
		    FieldAssignment cloneField = originalAssign.clone();
		    cloneField.setProject(cloneProj);
		    this.fieldDAO.assignFieldToProject(cloneField);
		}
	    }

	    // TODO Clone redis objects.
	    for (Staff originalStaff : originalStaffSet) {
		String attendancesPattern = Attendance.constructPattern(originalProj, originalStaff);
		Set<String> attnKeys = this.attendanceValueRepo.keys(attendancesPattern);
		List<Attendance> originalAttnds = this.attendanceValueRepo.multiGet(attnKeys);
	    }
	}
    }

    /**
     * Clone configurations.
     * 
     * @param company
     * @param clone
     */
    private void cloneConfigs(Company company, Company clone) {
	Set<SystemConfiguration> configs = company.getConfigs();
	for (SystemConfiguration conf : configs) {
	    SystemConfiguration cloneConf = conf.clone();
	    cloneConf.setId(0);
	    cloneConf.setCompany(clone);
	    this.systemConfigurationDAO.create(cloneConf);
	}
    }

    /**
     * Clone the users and the staff members of a company.
     * 
     * @param company
     * @param clone
     */
    private void cloneUsersAndStaff(Company company, Company clone, Map<Long, Staff> oldIdToNewStaff) {

	// Get a copy of all the collections.
	Set<SystemUser> users = company.getEmployees(); // Clone user and staff.

	// If staff is already cloned,
	// don't do it again.
	Set<Staff> staff = company.getStaff();

	// Set the clone as the new company of the collections,
	// set also ID's to zero.

	// Staff members.
	for (Staff stf : staff) {
	    Staff cloneStaff = stf.clone();
	    long oldId = cloneStaff.getId();
	    cloneStaff.setId(0);
	    cloneStaff.setCompany(clone);
	    cloneStaff.setTasks(null);
	    cloneStaff.setUser(null);
	    this.staffDAO.create(cloneStaff);
	    oldIdToNewStaff.put(oldId, cloneStaff);
	}

	// System users.
	for (SystemUser user : users) {
	    SystemUser cloneUser = user.clone();

	    // Set the staff.
	    Staff userStaff = user.getStaff();
	    if (userStaff != null) {
		long oldId = userStaff.getId();
		Staff stf = oldIdToNewStaff.get(oldId);
		cloneUser.setStaff(stf);
	    }
	    cloneUser.setAuditLogs(null);
	    cloneUser.setCompany(clone);
	    cloneUser.setId(0);
	    cloneUser.setUsername(String.format("%s_%s", clone.getId(), cloneUser.getUsername()));
	    this.systemUserDAO.create(cloneUser);
	}
    }
}
