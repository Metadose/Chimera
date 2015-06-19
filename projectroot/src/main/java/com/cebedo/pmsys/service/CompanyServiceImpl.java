package com.cebedo.pmsys.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.CompanyDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.LogHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class CompanyServiceImpl implements CompanyService {

    private static Logger logger = Logger.getLogger(Company.OBJECT_NAME);
    private AuthHelper authHelper = new AuthHelper();
    private LogHelper logHelper = new LogHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private CompanyDAO companyDAO;

    public void setCompanyDAO(CompanyDAO companyDAO) {
	this.companyDAO = companyDAO;
    }

    @Override
    @Transactional
    public String create(Company company) {
	AuthenticationToken auth = this.authHelper.getAuth();

	String result = "";
	if (auth.isSuperAdmin()) {
	    // Log and notifications happen here.
	    this.messageHelper.sendAction(Company.OBJECT_NAME,
		    AuditAction.CREATE, company.getId(), company.getName());

	    // Do actual service and construct response.
	    this.companyDAO.create(company);
	    result = AlertBoxGenerator.SUCCESS.generateCreate(
		    Company.OBJECT_NAME, company.getName());
	} else {
	    // If you are not a super admin,
	    // you're not allowed to create a new company.
	    result = AlertBoxGenerator.FAILED.generateCreate(Company.OBJECT_NAME,
		    company.getName());
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.CREATE, Company.OBJECT_NAME, company.getId(),
		    company.getName()));
	}
	return result;
    }

    @Override
    @Transactional
    public Company getByID(long id) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Company company = this.companyDAO.getByID(id);

	if (this.authHelper.isActionAuthorized(company)) {
	    // Log then
	    // return actual object.
	    logger.info(this.logHelper.logGetObject(auth, Company.OBJECT_NAME,
		    id, company.getName()));
	    return company;
	}

	// Warn in logs then return empty object.
	logger.warn(this.logHelper.logUnauthorized(auth, AuditAction.GET,
		Company.OBJECT_NAME, id, company.getName()));
	return new Company();
    }

    @Override
    @Transactional
    public String update(Company company) {
	AuthenticationToken auth = this.authHelper.getAuth();

	// Only super admins can update companies.
	String result = "";
	if (auth.isSuperAdmin()) {

	    // Create post-service operations.
	    this.messageHelper.sendAction(Company.OBJECT_NAME,
		    AuditAction.UPDATE, company.getId(), company.getName());

	    // Do actual update to object.
	    // Construct alert box response.
	    this.companyDAO.update(company);
	    result = AlertBoxGenerator.SUCCESS.generateUpdate(
		    Company.OBJECT_NAME, company.getName());
	} else {
	    // Warn
	    // then construct response.
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.UPDATE, Company.OBJECT_NAME, company.getId(),
		    company.getName()));
	    result = AlertBoxGenerator.FAILED.generateUpdate(Company.OBJECT_NAME,
		    company.getName());
	}
	return result;
    }

    @Override
    @Transactional
    public String delete(long id) {
	String result = "";
	AuthenticationToken auth = this.authHelper.getAuth();
	Company company = this.companyDAO.getByID(id);

	// Only super admins can delete companies.
	if (auth.isSuperAdmin()) {

	    // Proceed to post-service operations.
	    this.messageHelper.sendAction(Company.OBJECT_NAME,
		    AuditAction.DELETE, id, company.getName());

	    // Do actual service.
	    // Generate response.
	    this.companyDAO.delete(id);
	    result = AlertBoxGenerator.SUCCESS.generateDelete(
		    Company.OBJECT_NAME, company.getName());
	} else {
	    // Log then
	    // construct response.
	    logger.warn(this.logHelper.logUnauthorized(auth,
		    AuditAction.DELETE, Company.OBJECT_NAME, company.getId(),
		    company.getName()));
	    result = AlertBoxGenerator.FAILED.generateDelete(Company.OBJECT_NAME,
		    company.getName());
	}
	return result;
    }

    @Override
    @Transactional
    public List<Company> list() {
	AuthenticationToken token = this.authHelper.getAuth();

	// Only super admins can see the list.
	if (token.isSuperAdmin()) {

	    // List as super admin.
	    logger.info(this.logHelper.logListAsSuperAdmin(token,
		    Company.OBJECT_NAME));
	    return this.companyDAO.list(null);
	}

	// Warning log.
	// Return empty list.
	logger.warn(this.logHelper.logUnauthorizedSuperAdminOnly(token,
		AuditAction.LIST, Company.OBJECT_NAME));
	return new ArrayList<Company>();
    }
}
