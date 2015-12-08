package com.cebedo.pmsys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.factory.AlertBoxFactory;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.service.SystemConfigurationService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.validator.SystemConfigurationValidator;

@Service
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    @Value("${webapp.config.version.beta}")
    private Boolean betaServer;

    @Value("${webapp.config.files.home}")
    private String serverHome;

    private SystemConfigurationDAO systemConfigurationDAO;

    public void setBetaServer(Boolean betaServer) {
	this.betaServer = betaServer;
    }

    public void setSystemConfigurationDAO(SystemConfigurationDAO systemConfigurationDAO) {
	this.systemConfigurationDAO = systemConfigurationDAO;
    }

    @Autowired
    SystemConfigurationValidator systemConfigurationValidator;

    @Override
    @Transactional
    public String create(SystemConfiguration systemConfiguration, BindingResult result) {

	// Result is null if it is a system override call.
	if (result != null) {

	    // Security check.
	    // Only super admins can create a config.
	    if (!this.authHelper.isSuperAdmin()) {
		this.messageHelper.unauthorizedID(SystemConfiguration.OBJECT_NAME,
			systemConfiguration.getId());
		return AlertBoxFactory.ERROR;
	    }

	    // Service layer form validation.
	    this.systemConfigurationValidator.validate(systemConfiguration, result);
	    if (result.hasErrors()) {
		return this.validationHelper.errorMessageHTML(result);
	    }

	    // Set the company.
	    AuthenticationToken auth = this.authHelper.getAuth();
	    Company authCompany = auth.getCompany();
	    systemConfiguration.setCompany(authCompany);

	    // Log.
	    this.messageHelper.auditableID(AuditAction.ACTION_CREATE, SystemConfiguration.OBJECT_NAME,
		    systemConfiguration.getId(), systemConfiguration.getName());
	}

	this.systemConfigurationDAO.create(systemConfiguration);

	return AlertBoxFactory.SUCCESS.generateCreate(SystemConfiguration.OBJECT_NAME,
		systemConfiguration.getName());
    }

    @Override
    @Transactional
    public SystemConfiguration getByID(long id) {
	SystemConfiguration conf = this.systemConfigurationDAO.getByID(id);

	// Security check.
	if (!this.authHelper.hasAccess(conf)) {
	    this.messageHelper.unauthorizedID(SystemConfiguration.OBJECT_NAME, conf.getId());
	    return new SystemConfiguration();
	}
	// Log.
	this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_GET, SystemConfiguration.OBJECT_NAME,
		conf.getId());

	return conf;
    }

    @Override
    @Transactional
    public String update(SystemConfiguration systemConfiguration, BindingResult result) {

	// Security check.
	if (!this.authHelper.hasAccess(systemConfiguration)) {
	    this.messageHelper.unauthorizedID(SystemConfiguration.OBJECT_NAME,
		    systemConfiguration.getId());
	    return AlertBoxFactory.ERROR;
	}

	// Service layer form validation.
	this.systemConfigurationValidator.validate(systemConfiguration, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Log.
	this.messageHelper.auditableID(AuditAction.ACTION_UPDATE, SystemConfiguration.OBJECT_NAME,
		systemConfiguration.getId(), systemConfiguration.getName());

	this.systemConfigurationDAO.update(systemConfiguration);

	return AlertBoxFactory.SUCCESS.generateUpdate(SystemConfiguration.OBJECT_NAME,
		systemConfiguration.getName());
    }

    @Override
    @Transactional
    public String delete(long id) {
	SystemConfiguration conf = this.systemConfigurationDAO.getByID(id);

	// Security check.
	if (!this.authHelper.hasAccess(conf)) {
	    this.messageHelper.unauthorizedID(SystemConfiguration.OBJECT_NAME, conf.getId());
	    return AlertBoxFactory.ERROR;
	}
	// Log.
	this.messageHelper.auditableID(AuditAction.ACTION_DELETE, SystemConfiguration.OBJECT_NAME,
		conf.getId(), conf.getName());

	this.systemConfigurationDAO.delete(id);

	return AlertBoxFactory.SUCCESS.generateDelete(SystemConfiguration.OBJECT_NAME, conf.getName());
    }

    @Override
    @Transactional
    public List<SystemConfiguration> list() {

	// Log.
	this.messageHelper.nonAuditableListNoAssoc(AuditAction.ACTION_LIST,
		SystemConfiguration.OBJECT_NAME);

	AuthenticationToken token = this.authHelper.getAuth();
	if (token.isSuperAdmin()) {
	    return this.systemConfigurationDAO.list(null);
	}
	return this.systemConfigurationDAO.list(token.getCompany().getId());
    }

    @Override
    @Transactional
    public String getValueByName(String name, boolean override) {
	SystemConfiguration conf = this.systemConfigurationDAO.getByName(name);

	if (!override) {
	    // Security check.
	    if (!this.authHelper.hasAccess(conf)) {
		this.messageHelper.unauthorizedID(SystemConfiguration.OBJECT_NAME, conf.getId());
		return "";
	    }
	    // Log.
	    this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_GET,
		    SystemConfiguration.OBJECT_NAME, conf.getId());
	}

	return conf == null ? null : conf.getValue();
    }

    @Override
    @Transactional
    public SystemConfiguration getByName(String name, boolean override) {
	SystemConfiguration config = this.systemConfigurationDAO.getByName(name);

	if (!override) {
	    // Security check.
	    if (!this.authHelper.hasAccess(config)) {
		this.messageHelper.unauthorizedID(SystemConfiguration.OBJECT_NAME, config.getId());
		return new SystemConfiguration();
	    }
	    // Log.
	    this.messageHelper.nonAuditableIDNoAssoc(AuditAction.ACTION_GET,
		    SystemConfiguration.OBJECT_NAME, config.getId());
	}
	return config;
    }

    /**
     * This is a system-only function.<br>
     * Do not do security checks.<br>
     * Do not do logs and audits.
     */
    @Override
    @Transactional
    public String merge(SystemConfiguration config, BindingResult result) {

	// Service layer form validation.
	if (result != null) {
	    this.systemConfigurationValidator.validate(config, result);
	    if (result.hasErrors()) {
		return this.validationHelper.errorMessageHTML(result);
	    }
	}
	this.systemConfigurationDAO.merge(config);
	return AlertBoxFactory.SUCCESS.generateUpdate(SystemConfiguration.OBJECT_NAME, config.getName());
    }

    @Override
    public Boolean getBetaServer() {
	return betaServer;
    }

    @Override
    public String getServerHome() {
	return serverHome;
    }

    public void setServerHome(String serverHome) {
	this.serverHome = serverHome;
    }

}
