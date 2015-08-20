package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.service.CompanyService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class CompanyServiceImpl implements CompanyService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private CompanyDAO companyDAO;

    public void setCompanyDAO(CompanyDAO companyDAO) {
	this.companyDAO = companyDAO;
    }

    /**
     * Create a new company.
     */
    @Override
    @Transactional
    public String create(Company company) {

	// Security check.
	if (!this.authHelper.isSuperAdmin()) {
	    this.messageHelper.unauthorized(Company.OBJECT_NAME, company.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// If start > end.
	Date start = company.getDateStarted();
	Date end = company.getDateExpiration();
	if (start.after(end)) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_START_DATE_GT_END_DATE);
	}

	this.companyDAO.create(company);

	// Log.
	this.messageHelper.send(AuditAction.ACTION_CREATE, Company.OBJECT_NAME, company.getId());

	// Do actual service and construct response.
	return AlertBoxGenerator.SUCCESS.generateCreate(Company.OBJECT_NAME, company.getName());
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
	    this.messageHelper.unauthorized(Company.OBJECT_NAME, company.getId());
	    return new Company();
	}

	// Log then
	// return actual object.
	this.messageHelper.send(AuditAction.ACTION_GET, Company.OBJECT_NAME, company.getId());
	return company;
    }

    /**
     * Update a company.
     */
    @Override
    @Transactional
    public String update(Company company) {

	// Security check.
	if (!this.authHelper.isSuperAdmin()) {
	    this.messageHelper.unauthorized(Company.OBJECT_NAME, company.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// If start > end.
	Date start = company.getDateStarted();
	Date end = company.getDateExpiration();
	if (start.after(end)) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_START_DATE_GT_END_DATE);
	}

	// Create post-service operations.
	this.messageHelper.send(AuditAction.ACTION_UPDATE, Company.OBJECT_NAME, company.getId());

	// Do actual update to object.
	// Construct alert box response.
	this.companyDAO.update(company);
	return AlertBoxGenerator.SUCCESS.generateUpdate(Company.OBJECT_NAME, company.getName());
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
	    this.messageHelper.unauthorized(Company.OBJECT_NAME, company.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Proceed to post-service operations.
	this.messageHelper.send(AuditAction.ACTION_DELETE, Company.OBJECT_NAME, company.getId());

	// Do actual service.
	// Generate response.
	this.companyDAO.delete(id);
	return AlertBoxGenerator.SUCCESS.generateDelete(Company.OBJECT_NAME, company.getName());
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
	this.messageHelper.send(AuditAction.ACTION_LIST, Company.OBJECT_NAME);
	return this.companyDAO.list(null);
    }
}
