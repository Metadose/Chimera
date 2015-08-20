package com.cebedo.pmsys.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.service.SystemConfigurationService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();

    private SystemConfigurationDAO systemConfigurationDAO;

    public void setSystemConfigurationDAO(SystemConfigurationDAO systemConfigurationDAO) {
	this.systemConfigurationDAO = systemConfigurationDAO;
    }

    @Override
    @Transactional
    public String create(SystemConfiguration systemConfiguration) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Company authCompany = auth.getCompany();
	systemConfiguration.setCompany(authCompany);
	this.systemConfigurationDAO.create(systemConfiguration);

	// Log.
	this.messageHelper.send(AuditAction.ACTION_CREATE, SystemConfiguration.OBJECT_NAME,
		systemConfiguration.getId());

	return AlertBoxGenerator.SUCCESS.generateCreate(SystemConfiguration.OBJECT_NAME,
		systemConfiguration.getName());
    }

    @Override
    @Transactional
    public SystemConfiguration getByID(long id) {
	SystemConfiguration conf = this.systemConfigurationDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(conf)) {
	    this.messageHelper.unauthorized(SystemConfiguration.OBJECT_NAME, conf.getId());
	    return new SystemConfiguration();
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET, SystemConfiguration.OBJECT_NAME, conf.getId());

	return conf;
    }

    @Override
    @Transactional
    public String update(SystemConfiguration systemConfiguration) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(systemConfiguration)) {
	    this.messageHelper
		    .unauthorized(SystemConfiguration.OBJECT_NAME, systemConfiguration.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_UPDATE, SystemConfiguration.OBJECT_NAME,
		systemConfiguration.getId());

	this.systemConfigurationDAO.update(systemConfiguration);

	return AlertBoxGenerator.SUCCESS.generateUpdate(SystemConfiguration.OBJECT_NAME,
		systemConfiguration.getName());
    }

    @Override
    @Transactional
    public String delete(long id) {
	SystemConfiguration conf = this.systemConfigurationDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(conf)) {
	    this.messageHelper.unauthorized(SystemConfiguration.OBJECT_NAME, conf.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper
		.send(AuditAction.ACTION_DELETE, SystemConfiguration.OBJECT_NAME, conf.getId());

	this.systemConfigurationDAO.delete(id);

	return AlertBoxGenerator.SUCCESS.generateDelete(SystemConfiguration.OBJECT_NAME, conf.getName());
    }

    @Override
    @Transactional
    public List<SystemConfiguration> list() {

	// Log.
	this.messageHelper.send(AuditAction.ACTION_LIST, SystemConfiguration.OBJECT_NAME);

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
	    if (!this.authHelper.isActionAuthorized(conf)) {
		this.messageHelper.unauthorized(SystemConfiguration.OBJECT_NAME, conf.getId());
		return "";
	    }
	    // Log.
	    this.messageHelper.send(AuditAction.ACTION_GET, SystemConfiguration.OBJECT_NAME,
		    conf.getId());
	}

	return conf.getValue();
    }

    @Override
    @Transactional
    public SystemConfiguration getByName(String name, boolean override) {
	SystemConfiguration config = this.systemConfigurationDAO.getByName(name);

	if (override) {
	    // Security check.
	    if (!this.authHelper.isActionAuthorized(config)) {
		this.messageHelper.unauthorized(SystemConfiguration.OBJECT_NAME, config.getId());
		return new SystemConfiguration();
	    }
	    // Log.
	    this.messageHelper.send(AuditAction.ACTION_GET, SystemConfiguration.OBJECT_NAME,
		    config.getId());
	}
	return config;
    }

    @Override
    @Transactional
    public String merge(SystemConfiguration config) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(config)) {
	    this.messageHelper.unauthorized(SystemConfiguration.OBJECT_NAME, config.getId());
	    return AlertBoxGenerator.ERROR;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_MERGE, SystemConfiguration.OBJECT_NAME,
		config.getId());

	this.systemConfigurationDAO.merge(config);

	return AlertBoxGenerator.SUCCESS.generateUpdate(SystemConfiguration.OBJECT_NAME,
		config.getName());
    }
}
