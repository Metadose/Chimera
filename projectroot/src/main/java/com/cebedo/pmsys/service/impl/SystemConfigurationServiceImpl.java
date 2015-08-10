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
    public void create(SystemConfiguration systemConfiguration) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Company authCompany = auth.getCompany();
	systemConfiguration.setCompany(authCompany);
	this.systemConfigurationDAO.create(systemConfiguration);

	// Log.
	this.messageHelper.send(AuditAction.ACTION_CREATE, SystemConfiguration.OBJECT_NAME,
		systemConfiguration.getId());

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
    public void update(SystemConfiguration systemConfiguration) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(systemConfiguration)) {
	    this.messageHelper
		    .unauthorized(SystemConfiguration.OBJECT_NAME, systemConfiguration.getId());
	    return;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_UPDATE, SystemConfiguration.OBJECT_NAME,
		systemConfiguration.getId());

	this.systemConfigurationDAO.update(systemConfiguration);
    }

    @Override
    @Transactional
    public void delete(long id) {
	SystemConfiguration conf = this.systemConfigurationDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(conf)) {
	    this.messageHelper.unauthorized(SystemConfiguration.OBJECT_NAME, conf.getId());
	    return;
	}
	// Log.
	this.messageHelper
		.send(AuditAction.ACTION_DELETE, SystemConfiguration.OBJECT_NAME, conf.getId());

	this.systemConfigurationDAO.delete(id);
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
    public SystemConfiguration getByName(String name) {
	SystemConfiguration config = this.systemConfigurationDAO.getByName(name);
	// Security check.
	if (!this.authHelper.isActionAuthorized(config)) {
	    this.messageHelper.unauthorized(SystemConfiguration.OBJECT_NAME, config.getId());
	    return new SystemConfiguration();
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_GET, SystemConfiguration.OBJECT_NAME, config.getId());
	return config;
    }

    @Override
    @Transactional
    public void merge(SystemConfiguration config) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(config)) {
	    this.messageHelper.unauthorized(SystemConfiguration.OBJECT_NAME, config.getId());
	    return;
	}
	// Log.
	this.messageHelper.send(AuditAction.ACTION_MERGE, SystemConfiguration.OBJECT_NAME,
		config.getId());

	this.systemConfigurationDAO.merge(config);
    }
}
