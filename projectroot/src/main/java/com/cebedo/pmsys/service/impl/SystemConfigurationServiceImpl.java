package com.cebedo.pmsys.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.SystemConfiguration;
import com.cebedo.pmsys.service.SystemConfigurationService;
import com.cebedo.pmsys.token.AuthenticationToken;

@Service
public class SystemConfigurationServiceImpl implements
		SystemConfigurationService {

	private AuthHelper authHelper = new AuthHelper();
	private SystemConfigurationDAO systemConfigurationDAO;

	public void setSystemConfigurationDAO(
			SystemConfigurationDAO systemConfigurationDAO) {
		this.systemConfigurationDAO = systemConfigurationDAO;
	}

	@Override
	@Transactional
	public void create(SystemConfiguration systemConfiguration) {
		AuthenticationToken auth = this.authHelper.getAuth();
		Company authCompany = auth.getCompany();
		systemConfiguration.setCompany(authCompany);
		this.systemConfigurationDAO.create(systemConfiguration);
	}

	@Override
	@Transactional
	public SystemConfiguration getByID(long id) {
		SystemConfiguration conf = this.systemConfigurationDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(conf)) {
			return conf;
		}
		return new SystemConfiguration();
	}

	@Override
	@Transactional
	public void update(SystemConfiguration systemConfiguration) {
		if (this.authHelper.isActionAuthorized(systemConfiguration)) {
			this.systemConfigurationDAO.update(systemConfiguration);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		SystemConfiguration conf = this.systemConfigurationDAO.getByID(id);
		if (this.authHelper.isActionAuthorized(conf)) {
			this.systemConfigurationDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<SystemConfiguration> list() {
		AuthenticationToken token = this.authHelper.getAuth();
		if (token.isSuperAdmin()) {
			return this.systemConfigurationDAO.list(null);
		}
		return this.systemConfigurationDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public String getValueByName(String name) {
		SystemConfiguration conf = this.systemConfigurationDAO.getByName(name);
		return conf.getValue();
	}

	@Override
	@Transactional
	public SystemConfiguration getByName(String name) {
		return this.systemConfigurationDAO.getByName(name);
	}
}
