package com.cebedo.pmsys.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.bean.PairCountValue;
import com.cebedo.pmsys.bean.PayrollResultComputation;
import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.dao.FieldDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.dao.SystemUserDAO;
import com.cebedo.pmsys.dao.TaskDAO;
import com.cebedo.pmsys.domain.Attendance;
import com.cebedo.pmsys.domain.CompanyAux;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.EquipmentExpense;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.domain.Material;
import com.cebedo.pmsys.domain.ProjectAux;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.domain.PullOut;
import com.cebedo.pmsys.domain.UserAux;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.StatusAttendance;
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
import com.cebedo.pmsys.pojo.JSONPayrollResult;
import com.cebedo.pmsys.repository.impl.AttendanceValueRepoImpl;
import com.cebedo.pmsys.repository.impl.CompanyAuxValueRepoImpl;
import com.cebedo.pmsys.repository.impl.DeliveryValueRepoImpl;
import com.cebedo.pmsys.repository.impl.EquipmentExpenseValueRepoImpl;
import com.cebedo.pmsys.repository.impl.EstimateCostValueRepoImpl;
import com.cebedo.pmsys.repository.impl.ExpenseValueRepoImpl;
import com.cebedo.pmsys.repository.impl.MaterialValueRepoImpl;
import com.cebedo.pmsys.repository.impl.ProjectAuxValueRepoImpl;
import com.cebedo.pmsys.repository.impl.ProjectPayrollValueRepoImpl;
import com.cebedo.pmsys.repository.impl.PullOutValueRepoImpl;
import com.cebedo.pmsys.repository.impl.UserAuxValueRepoImpl;
import com.cebedo.pmsys.service.CompanyService;
import com.cebedo.pmsys.service.ProjectPayrollComputerService;
import com.cebedo.pmsys.service.SystemConfigurationService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.utils.ImageUtils;
import com.cebedo.pmsys.validator.CompanyValidator;
import com.cebedo.pmsys.validator.ImageUploadValidator;
import com.google.common.primitives.Longs;

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
    private UserAuxValueRepoImpl userAuxValueRepo;
    private ProjectPayrollValueRepoImpl projectPayrollValueRepo;
    private EstimateCostValueRepoImpl estimateCostValueRepo;
    private ExpenseValueRepoImpl expenseValueRepo;
    private EquipmentExpenseValueRepoImpl equipmentExpenseValueRepo;
    private DeliveryValueRepoImpl deliveryValueRepo;
    private MaterialValueRepoImpl materialValueRepo;
    private PullOutValueRepoImpl pullOutValueRepo;
    private ProjectPayrollComputerService projectPayrollComputerService;
    private CompanyAuxValueRepoImpl companyAuxValueRepo;

    @Autowired
    @Qualifier(value = "companyAuxValueRepo")
    public void setCompanyAuxValueRepo(CompanyAuxValueRepoImpl companyAuxValueRepo) {
	this.companyAuxValueRepo = companyAuxValueRepo;
    }

    @Autowired
    @Qualifier(value = "projectPayrollComputerService")
    public void setProjectPayrollComputerService(
	    ProjectPayrollComputerService projectPayrollComputerService) {
	this.projectPayrollComputerService = projectPayrollComputerService;
    }

    @Autowired
    @Qualifier(value = "materialValueRepo")
    public void setMaterialValueRepo(MaterialValueRepoImpl materialValueRepo) {
	this.materialValueRepo = materialValueRepo;
    }

    @Autowired
    @Qualifier(value = "pullOutValueRepo")
    public void setPullOutValueRepo(PullOutValueRepoImpl pullOutValueRepo) {
	this.pullOutValueRepo = pullOutValueRepo;
    }

    @Autowired
    @Qualifier(value = "deliveryValueRepo")
    public void setDeliveryValueRepo(DeliveryValueRepoImpl deliveryValueRepo) {
	this.deliveryValueRepo = deliveryValueRepo;
    }

    @Autowired
    @Qualifier(value = "equipmentExpenseValueRepo")
    public void setEquipmentExpenseValueRepo(EquipmentExpenseValueRepoImpl equipmentExpenseValueRepo) {
	this.equipmentExpenseValueRepo = equipmentExpenseValueRepo;
    }

    @Autowired
    @Qualifier(value = "expenseValueRepo")
    public void setExpenseValueRepo(ExpenseValueRepoImpl expenseValueRepo) {
	this.expenseValueRepo = expenseValueRepo;
    }

    @Autowired
    @Qualifier(value = "estimateCostValueRepo")
    public void setEstimateCostValueRepo(EstimateCostValueRepoImpl estimateCostValueRepo) {
	this.estimateCostValueRepo = estimateCostValueRepo;
    }

    @Autowired
    @Qualifier(value = "projectPayrollValueRepo")
    public void setProjectPayrollValueRepo(ProjectPayrollValueRepoImpl projectPayrollValueRepo) {
	this.projectPayrollValueRepo = projectPayrollValueRepo;
    }

    @Autowired
    @Qualifier(value = "userAuxValueRepo")
    public void setUserAuxValueRepo(UserAuxValueRepoImpl userAuxValueRepo) {
	this.userAuxValueRepo = userAuxValueRepo;
    }

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
     * Update the company auxiliary.
     * 
     * @param aux
     * @return
     */
    @Override
    @Transactional
    public String setAux(CompanyAux aux) {
	this.companyAuxValueRepo.set(aux);
	Company com = this.companyDAO.getByID(aux.getCompany().getId());

	// Log.
	this.messageHelper.auditableID(AuditAction.ACTION_UPDATE, ConstantsRedis.OBJECT_AUX_COMPANY,
		com.getId(), com.getName());

	return AlertBoxFactory.SUCCESS.generateUpdate(Company.OBJECT_NAME, com.getName());
    }

    /**
     * Get the auxiliary object of the company.
     * 
     * @param company
     * @return
     */
    @Override
    @Transactional
    public CompanyAux getAux(Company company) {
	String key = CompanyAux.constructKey(company);
	CompanyAux aux = this.companyAuxValueRepo.get(key);

	// If the auxiliary was not set,
	// create a new object, and set it to the repository.
	if (aux == null) {
	    aux = new CompanyAux(company);
	    this.companyAuxValueRepo.set(aux);
	}
	return aux;
    }

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

    @Override
    @Transactional
    public String clearLogs() {
	this.messageHelper.auditableID(AuditAction.ACTION_DELETE_ALL, Company.OBJECT_NAME, 0,
		AuditLog.OBJECT_NAME);
	this.companyDAO.executeDelete(new AuditLog());
	return AlertBoxFactory.SUCCESS.generateDeleteAll("log");
    }

    @Override
    @Transactional
    public String clearLogs(int id) {
	this.messageHelper.auditableID(AuditAction.ACTION_DELETE_ALL, Company.OBJECT_NAME, id,
		AuditLog.OBJECT_NAME);
	this.companyDAO.executeDelete(new AuditLog(), id);
	return AlertBoxFactory.SUCCESS.generateDeleteAll("log");
    }

    /**
     * Deletes a company.
     */
    @Override
    @Transactional
    public String delete(long id) {

	Company company = this.companyDAO.getByIDWithLazyCollections(id);

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
	this.companyDAO.delete(company);
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
    public String clone(Company companyTarget) {
	long companyId = companyTarget.getId();
	String cloneName = companyTarget.getName();

	// Set cloning configuration.
	Company company = this.companyDAO.getByIDWithLazyCollections(companyId);
	company.setRandomizeNames(companyTarget.isRandomizeNames());
	company.setClonePassword(companyTarget.getClonePassword());

	if (!this.authHelper.isSuperAdmin()) {
	    this.messageHelper.unauthorizedID(Company.OBJECT_NAME, company.getId());
	    return AlertBoxFactory.ERROR;
	}

	if (company.getName().equals(cloneName)) {
	    return AlertBoxFactory.FAILED.generateClone(Company.OBJECT_NAME, company.getName());
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

	// Clone also the auxiliary.
	String auxKey = CompanyAux.constructKey(company);
	CompanyAux originalAux = this.companyAuxValueRepo.get(auxKey);
	CompanyAux cloneAux = originalAux.clone();
	cloneAux.setCompany(clone);
	this.companyAuxValueRepo.set(cloneAux);

	// Clone the users and the staff members of a company.
	Map<Long, Staff> oldIdToNewStaff = new HashMap<Long, Staff>();
	Map<Long, SystemUser> oldIdToNewUser = new HashMap<Long, SystemUser>();
	cloneUsersAndStaff(company, clone, oldIdToNewStaff, oldIdToNewUser);

	// Clone the configurations.
	cloneConfigs(company, clone);

	// Clone projects.
	cloneProjects(company, clone, oldIdToNewStaff, oldIdToNewUser);

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
	    Staff newStaff = oldIdToNewStaff.get(originalStf.getId());
	    if (newStaff == null) {
		continue;
	    }
	    clonedStaffSet.add(newStaff);
	}
	return clonedStaffSet;
    }

    /**
     * Get equivalents as ID.
     * 
     * @param originalStaffIds
     * @param oldIdToNewStaff
     * @return
     */
    private long[] getClonedEquivalent(long[] originalStaffIds, Map<Long, Staff> oldIdToNewStaff) {
	Set<Long> clonedStaffSet = new HashSet<Long>();
	for (long originalId : originalStaffIds) {
	    Staff stf = oldIdToNewStaff.get(originalId);
	    if (stf != null) {
		clonedStaffSet.add(stf.getId());
	    }
	}
	return Longs.toArray(clonedStaffSet);
    }

    /**
     * Clone projects.
     * 
     * @param originalCompany
     * @param cloneCompany
     * @param oldIdToNewUser
     */
    private void cloneProjects(Company originalCompany, Company cloneCompany,
	    Map<Long, Staff> oldIdToNewStaff, Map<Long, SystemUser> oldIdToNewUser) {

	for (Project originalProj : originalCompany.getProjects()) {

	    // This project.
	    Set<Staff> originalStaffSet = originalProj.getAssignedStaff();
	    Project cloneProj = originalProj.clone();
	    cloneProject(cloneCompany, originalProj, cloneProj, originalStaffSet, oldIdToNewStaff);

	    // Tasks.
	    cloneTasks(cloneCompany, cloneProj, oldIdToNewStaff, originalProj.getAssignedTasks());

	    // Assign the fields.
	    cloneFields(originalProj, cloneProj);

	    // Clone Redis objects.
	    // Attendance.
	    cloneAttendances(cloneCompany, cloneProj, originalProj, originalStaffSet, oldIdToNewStaff);

	    // Project auxiliary.
	    cloneProjectAux(cloneCompany, cloneProj, originalProj);

	    // Payroll.
	    clonePayrolls(originalCompany, originalProj, cloneCompany, cloneProj, oldIdToNewStaff,
		    oldIdToNewUser);

	    // Estimated Costs.
	    cloneEstimateCosts(cloneCompany, cloneProj, originalProj);

	    // Other Expenses.
	    cloneOtherExpenses(cloneCompany, cloneProj, originalProj, oldIdToNewStaff);

	    // Equipment Expenses.
	    cloneEquipmentExpenses(cloneCompany, cloneProj, originalProj, oldIdToNewStaff);

	    // Inventory.
	    cloneInventory(cloneCompany, cloneProj, originalProj, oldIdToNewStaff);

	    // TODO Finish automated estimation tasks first before cloning.
	    // Estimation output.
	}
    }

    /**
     * Clone inventory which include delivery, materials and pull-outs.
     * 
     * @param cloneCompany
     * @param cloneProj
     * @param originalProj
     * @param oldIdToNewStaff
     */
    private void cloneInventory(Company cloneCompany, Project cloneProj, Project originalProj,
	    Map<Long, Staff> oldIdToNewStaff) {

	String patternDelivery = Delivery.constructPattern(originalProj);
	Set<String> keysDelivery = this.deliveryValueRepo.keys(patternDelivery);
	List<Delivery> originalDeliveries = this.deliveryValueRepo.multiGet(keysDelivery);

	// Loop through all the deliveries.
	for (Delivery originalDelivery : originalDeliveries) {

	    // Do delivery changes.
	    Delivery cloneDelivery = originalDelivery.clone();
	    cloneDelivery.setUuid(UUID.randomUUID());
	    cloneDelivery.setProject(cloneProj);
	    cloneDelivery.setCompany(cloneCompany);
	    this.deliveryValueRepo.set(cloneDelivery);

	    // Fetch all materials.
	    String patternMaterial = Material.constructPattern(originalDelivery);
	    Set<String> keysMaterial = this.materialValueRepo.keys(patternMaterial);
	    List<Material> originalMaterials = this.materialValueRepo.multiGet(keysMaterial);

	    // Loop all materials.
	    for (Material originalMaterial : originalMaterials) {

		// Do material changes.
		Material cloneMaterial = originalMaterial.clone();
		cloneMaterial.setUuid(UUID.randomUUID());
		cloneMaterial.setDelivery(cloneDelivery);
		cloneMaterial.setProject(cloneProj);
		cloneMaterial.setCompany(cloneCompany);
		this.materialValueRepo.set(cloneMaterial);

		// Fetch all pull-outs.
		String patternPullOut = PullOut.constructPattern(originalMaterial);
		Set<String> keysPullOut = this.pullOutValueRepo.keys(patternPullOut);
		List<PullOut> originalPullOuts = this.pullOutValueRepo.multiGet(keysPullOut);

		// Loop pull-outs.
		for (PullOut originalPullOut : originalPullOuts) {

		    Staff newStaff = oldIdToNewStaff.get(originalPullOut.getStaff().getId());

		    PullOut clonePullOut = originalPullOut.clone();
		    clonePullOut.setUuid(UUID.randomUUID());
		    clonePullOut.setMaterial(cloneMaterial);
		    clonePullOut.setDelivery(cloneDelivery);
		    clonePullOut.setProject(cloneProj);
		    clonePullOut.setCompany(cloneCompany);
		    clonePullOut.setStaff(newStaff);
		    this.pullOutValueRepo.set(clonePullOut);
		}
	    }
	}
    }

    /**
     * Clone equipment expenses.
     * 
     * @param cloneCompany
     * @param cloneProj
     * @param originalProj
     * @param oldIdToNewStaff
     */
    private void cloneEquipmentExpenses(Company cloneCompany, Project cloneProj, Project originalProj,
	    Map<Long, Staff> oldIdToNewStaff) {

	String pattern = EquipmentExpense.constructPattern(originalProj);
	Set<String> keys = this.equipmentExpenseValueRepo.keys(pattern);
	List<EquipmentExpense> originalEquipExps = this.equipmentExpenseValueRepo.multiGet(keys);

	for (EquipmentExpense originalEquipExp : originalEquipExps) {

	    Staff newStaff = oldIdToNewStaff.get(originalEquipExp.getStaff().getId());
	    EquipmentExpense cloneEquipExp = originalEquipExp.clone();
	    cloneEquipExp.setUuid(UUID.randomUUID());
	    cloneEquipExp.setProject(cloneProj);
	    cloneEquipExp.setCompany(cloneCompany);
	    cloneEquipExp.setStaff(newStaff);
	    this.equipmentExpenseValueRepo.set(cloneEquipExp);
	}
    }

    /**
     * Clone other expenses in this project.
     * 
     * @param cloneCompany
     * @param cloneProj
     * @param originalProj
     * @param oldIdToNewStaff
     */
    private void cloneOtherExpenses(Company cloneCompany, Project cloneProj, Project originalProj,
	    Map<Long, Staff> oldIdToNewStaff) {

	String pattern = Expense.constructPattern(originalProj);
	Set<String> keys = this.expenseValueRepo.keys(pattern);
	List<Expense> originalExpenses = this.expenseValueRepo.multiGet(keys);

	for (Expense originalExpense : originalExpenses) {

	    Staff newStaff = oldIdToNewStaff.get(originalExpense.getStaff().getId());
	    Expense cloneExpense = originalExpense.clone();
	    cloneExpense.setUuid(UUID.randomUUID());
	    cloneExpense.setProject(cloneProj);
	    cloneExpense.setCompany(cloneCompany);
	    cloneExpense.setStaff(newStaff);
	    this.expenseValueRepo.set(cloneExpense);
	}
    }

    /**
     * Clone estimated costs.
     * 
     * @param cloneCompany
     * @param cloneProj
     * @param originalProj
     */
    private void cloneEstimateCosts(Company cloneCompany, Project cloneProj, Project originalProj) {

	String pattern = EstimateCost.constructPattern(originalProj);
	Set<String> keys = this.estimateCostValueRepo.keys(pattern);
	List<EstimateCost> originalCosts = this.estimateCostValueRepo.multiGet(keys);

	for (EstimateCost originalCost : originalCosts) {

	    EstimateCost cloneCost = originalCost.clone();
	    cloneCost.setUuid(UUID.randomUUID());
	    cloneCost.setProject(cloneProj);
	    cloneCost.setCompany(cloneCompany);
	    this.estimateCostValueRepo.set(cloneCost);
	}
    }

    /**
     * Clone a single project..
     * 
     * @param cloneCompany
     * @param originalProj
     * @param cloneProj
     * @param originalStaffSet
     * @param oldIdToNewStaff
     */
    private void cloneProject(Company cloneCompany, Project originalProj, Project cloneProj,
	    Set<Staff> originalStaffSet, Map<Long, Staff> oldIdToNewStaff) {

	// Get the set of original staff,
	// loop through the processed map and
	// get equivalent clones given the ID.
	cloneProj.setId(0);
	cloneProj.setCompany(cloneCompany);
	cloneProj.setAuditLogs(null);

	// Update collections.
	Set<Staff> clonedStaffSet = getClonedEquivalent(originalStaffSet, oldIdToNewStaff);
	cloneProj.setAssignedFields(null);
	cloneProj.setAssignedTasks(null);
	cloneProj.setAssignedStaff(clonedStaffSet);
	this.projectDAO.create(cloneProj);
    }

    /**
     * Clone tasks in program of works.
     * 
     * @param cloneCompany
     * @param oldIdToNewStaff
     */
    private void cloneTasks(Company cloneCompany, Project cloneProject, Map<Long, Staff> oldIdToNewStaff,
	    Set<Task> originalTasks) {

	for (Task originalTask : originalTasks) {

	    Task cloneTask = originalTask.clone();
	    cloneTask.setId(0);
	    cloneTask.setCompany(cloneCompany);
	    cloneTask.setProject(cloneProject);
	    cloneTask.setStaff(getClonedEquivalent(originalTask.getStaff(), oldIdToNewStaff));
	    this.taskDAO.create(cloneTask);
	}
    }

    /**
     * Clone the assigned fields.
     * 
     * @param originalProj
     * @param cloneProj
     */
    private void cloneFields(Project originalProj, Project cloneProj) {
	Set<FieldAssignment> origianlFAssignments = originalProj.getAssignedFields();
	if (origianlFAssignments != null) {
	    for (FieldAssignment originalAssign : origianlFAssignments) {
		FieldAssignment cloneField = originalAssign.clone();
		cloneField.setProject(cloneProj);
		this.fieldDAO.assignFieldToProject(cloneField);
	    }
	}
    }

    /**
     * Clone the payrolls in this project.
     * 
     * @param originalCompany
     * @param originalProj
     * @param cloneCompany
     * @param cloneProj
     * @param oldIdToNewStaff
     * @param oldIdToNewUser
     */
    private void clonePayrolls(Company originalCompany, Project originalProj, Company cloneCompany,
	    Project cloneProj, Map<Long, Staff> oldIdToNewStaff, Map<Long, SystemUser> oldIdToNewUser) {

	// Fetch original payrolls.
	String pattern = ProjectPayroll.constructPattern(originalCompany.getId(), originalProj.getId());
	Set<String> keys = this.projectPayrollValueRepo.keys(pattern);
	List<ProjectPayroll> originalPayrolls = this.projectPayrollValueRepo.multiGet(keys);

	// Loop through all the payrolls.
	for (ProjectPayroll originalPayroll : originalPayrolls) {

	    long creatorId = originalPayroll.getCreator().getId();
	    Set<Staff> cloneAssignedStaff = getClonedEquivalent(originalPayroll.getAssignedStaffList(),
		    oldIdToNewStaff);
	    Set<Staff> cloneStaffList = getClonedEquivalent(originalPayroll.getStaffList(),
		    oldIdToNewStaff);
	    long[] cloneStaffIds = getClonedEquivalent(originalPayroll.getStaffIDs(), oldIdToNewStaff);

	    ProjectPayroll clonePayroll = originalPayroll.clone();
	    clonePayroll.setUuid(UUID.randomUUID());
	    clonePayroll.setCompany(cloneCompany);
	    clonePayroll.setCreator(oldIdToNewUser.get(creatorId));
	    clonePayroll.setAssignedStaffList(cloneAssignedStaff);
	    clonePayroll.setStaffList(cloneStaffList);
	    clonePayroll.setStaffIDs(cloneStaffIds);
	    clonePayroll.setProject(cloneProj);

	    // Results.
	    PayrollResultComputation originalResult = originalPayroll.getPayrollComputationResult();
	    if (originalResult == null) {
		continue;
	    }

	    // Staff to wage map.
	    Map<Staff, Double> cloneStaffToWage = new HashMap<Staff, Double>();
	    Map<Staff, Double> originalStaffToWage = originalResult.getStaffToWageMap();

	    for (Staff originalStaff : originalStaffToWage.keySet()) {

		Staff newStaff = oldIdToNewStaff.get(originalStaff.getId());
		Double value = originalStaffToWage.get(originalStaff);
		cloneStaffToWage.put(newStaff, value);
	    }

	    // Staff payroll breakdown.
	    Map<Staff, Map<StatusAttendance, PairCountValue>> cloneStaffPayrollBreakdown = new HashMap<Staff, Map<StatusAttendance, PairCountValue>>();
	    Map<Staff, Map<StatusAttendance, PairCountValue>> originalStaffPayrollBreakdown = originalResult
		    .getStaffPayrollBreakdownMap();

	    for (Staff originalStaff : originalStaffPayrollBreakdown.keySet()) {

		Staff newStaff = oldIdToNewStaff.get(originalStaff.getId());
		Map<StatusAttendance, PairCountValue> value = originalStaffPayrollBreakdown
			.get(originalStaff);
		cloneStaffPayrollBreakdown.put(newStaff, value);
	    }

	    // Tree grid.
	    List<JSONPayrollResult> cloneRows = new ArrayList<JSONPayrollResult>();
	    List<JSONPayrollResult> originalRows = originalResult.getTreeGrid();

	    for (JSONPayrollResult originalRow : originalRows) {

		JSONPayrollResult cloneRow = originalRow.clone();
		cloneRow.setUuid(UUID.randomUUID());
		cloneRows.add(cloneRow);
	    }

	    // Clone result.
	    PayrollResultComputation cloneResult = originalResult.clone();
	    cloneResult.setStaffPayrollBreakdownMap(cloneStaffPayrollBreakdown);
	    cloneResult.setStaffToWageMap(cloneStaffToWage);
	    cloneResult.setTreeGrid(cloneRows);

	    // Set the results.
	    String jsonResult = this.projectPayrollComputerService.getPayrollJSONResult(cloneStaffToWage,
		    cloneStaffPayrollBreakdown);
	    clonePayroll.setPayrollComputationResult(cloneResult);
	    clonePayroll.setPayrollJSON(jsonResult);
	    this.projectPayrollValueRepo.set(clonePayroll);
	}
    }

    /**
     * Clone the project auxiliary.
     * 
     * @param cloneCompany
     * @param cloneProj
     * @param originalProj
     */
    private void cloneProjectAux(Company cloneCompany, Project cloneProj, Project originalProj) {
	String key = ProjectAux.constructKey(originalProj);
	ProjectAux originalAux = this.projectAuxValueRepo.get(key);
	ProjectAux cloneAux = originalAux.clone();
	cloneAux.setCompany(cloneCompany);
	cloneAux.setProject(cloneProj);
	this.projectAuxValueRepo.set(cloneAux);
    }

    /**
     * Clone the attendances of staff members from a project.
     * 
     * @param cloneCompany
     * @param cloneProj
     * @param originalProj
     * @param originalStaffSet
     * @param oldIdToNewStaff
     */
    private void cloneAttendances(Company cloneCompany, Project cloneProj, Project originalProj,
	    Set<Staff> originalStaffSet, Map<Long, Staff> oldIdToNewStaff) {

	for (Staff originalStaff : originalStaffSet) {
	    String attendancesPattern = Attendance.constructPattern(originalProj, originalStaff);
	    Set<String> attnKeys = this.attendanceValueRepo.keys(attendancesPattern);
	    List<Attendance> originalAttnds = this.attendanceValueRepo.multiGet(attnKeys);

	    for (Attendance attn : originalAttnds) {
		Attendance cloneAttn = attn.clone();
		cloneAttn.setCompany(cloneCompany);
		cloneAttn.setProject(cloneProj);
		cloneAttn.setStaff(oldIdToNewStaff.get(originalStaff.getId()));
		this.attendanceValueRepo.set(cloneAttn);
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
     * @param cloneCompany
     * @param oldIdToNewUser
     */
    private void cloneUsersAndStaff(Company company, Company cloneCompany,
	    Map<Long, Staff> oldIdToNewStaff, Map<Long, SystemUser> oldIdToNewUser) {

	// If we are randomizing the names, load the names.
	ClassLoader loader = this.getClass().getClassLoader();
	InputStream maleNames = loader.getResourceAsStream("hispanic-first-male.names");
	InputStream lastNames = loader.getResourceAsStream("hispanic-last.names");
	String[] malesArr = {};
	String[] lastsArr = {};

	boolean randomizeNames = company.isRandomizeNames();
	if (randomizeNames) {
	    try {
		String males = IOUtils.toString(maleNames);
		String lasts = IOUtils.toString(lastNames);
		malesArr = males.split("\n");
		lastsArr = lasts.split("\n");
		maleNames.close();
		lastNames.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

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
	    cloneStaff.setCompany(cloneCompany);
	    cloneStaff.setTasks(null);
	    cloneStaff.setUser(null);

	    // If we are randomizing the name.
	    if (randomizeNames) {
		Random random = new Random();
		int maleIndex = random.nextInt(malesArr.length - 1);
		String randomMale = malesArr[maleIndex];
		cloneStaff.setFirstName(randomMale);

		int middleNameIndex = random.nextInt(lastsArr.length - 1);
		String randomMiddleName = lastsArr[middleNameIndex];
		cloneStaff.setMiddleName(randomMiddleName);

		int lastNameIndex = random.nextInt(lastsArr.length - 1);
		String randomLastName = lastsArr[lastNameIndex];
		cloneStaff.setLastName(randomLastName);
	    }

	    // Create the new staff.
	    this.staffDAO.create(cloneStaff);
	    oldIdToNewStaff.put(oldId, cloneStaff);
	}

	// System users.
	for (SystemUser originalUser : users) {
	    SystemUser cloneUser = originalUser.clone();

	    // If the staff was set in the original object,
	    // set it here too.
	    Staff userStaff = originalUser.getStaff();
	    if (userStaff != null) {

		// Set the staff.
		long oldId = userStaff.getId();
		Staff stf = oldIdToNewStaff.get(oldId);
		cloneUser.setStaff(stf);

		// Update the user name.
		String newUserName = String.format("%s_%s", stf.getFirstName(), stf.getLastName())
			.toLowerCase();
		cloneUser.setUsername(newUserName);
	    }

	    // Update the password.
	    String encPassword = this.authHelper.encodePassword(company.getClonePassword(), cloneUser);
	    cloneUser.setPassword(encPassword);

	    cloneUser.setAuditLogs(null);
	    cloneUser.setCompany(cloneCompany);
	    cloneUser.setId(0);
	    this.systemUserDAO.create(cloneUser);

	    // Add to map.
	    oldIdToNewUser.put(originalUser.getId(), cloneUser);

	    // Clone the user auxiliaries.
	    cloneUserAux(cloneCompany, cloneUser, originalUser);
	}
    }

    /**
     * Clone the user auxiliary.
     * 
     * @param cloneCompany
     * @param cloneUser
     * @param originalUser
     */
    private void cloneUserAux(Company cloneCompany, SystemUser cloneUser, SystemUser originalUser) {
	String key = UserAux.constructKey(originalUser);
	UserAux originalAux = this.userAuxValueRepo.get(key);
	UserAux cloneAux = originalAux.clone();
	cloneAux.setCompany(cloneCompany);
	cloneAux.setUser(cloneUser);
	this.userAuxValueRepo.set(cloneAux);
    }

}
