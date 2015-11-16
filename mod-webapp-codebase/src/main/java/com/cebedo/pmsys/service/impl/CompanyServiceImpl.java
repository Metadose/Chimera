package com.cebedo.pmsys.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.factory.AlertBoxFactory;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.FileHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.AuditLog;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.pojo.FormMultipartFile;
import com.cebedo.pmsys.repository.impl.ProjectAuxValueRepoImpl;
import com.cebedo.pmsys.service.CompanyService;
import com.cebedo.pmsys.service.SystemConfigurationService;
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
	return AlertBoxFactory.SUCCESS.generateUpdate(Company.OBJECT_NAME, company.getName());
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
}
