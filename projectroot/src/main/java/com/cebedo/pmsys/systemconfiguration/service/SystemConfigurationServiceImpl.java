package com.cebedo.pmsys.systemconfiguration.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.common.AuthUtils;
import com.cebedo.pmsys.company.model.Company;
import com.cebedo.pmsys.login.authentication.AuthenticationToken;
import com.cebedo.pmsys.systemconfiguration.dao.SystemConfigurationDAO;
import com.cebedo.pmsys.systemconfiguration.model.SystemConfiguration;

@Service
public class SystemConfigurationServiceImpl implements
		SystemConfigurationService {

	private SystemConfigurationDAO systemConfigurationDAO;

	public void setSystemConfigurationDAO(
			SystemConfigurationDAO systemConfigurationDAO) {
		this.systemConfigurationDAO = systemConfigurationDAO;
	}

	@Override
	@Transactional
	public void create(SystemConfiguration systemConfiguration) {
		this.systemConfigurationDAO.create(systemConfiguration);
		AuthenticationToken auth = AuthUtils.getAuth();
		Company authCompany = auth.getCompany();
		if (AuthUtils.notNullObjNotSuperAdmin(authCompany)) {
			systemConfiguration.setCompany(authCompany);
			this.systemConfigurationDAO.update(systemConfiguration);
		}
	}

	@Override
	@Transactional
	public SystemConfiguration getByID(long id) {
		SystemConfiguration conf = this.systemConfigurationDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(conf)) {
			return conf;
		}
		return new SystemConfiguration();
	}

	@Override
	@Transactional
	public void update(SystemConfiguration systemConfiguration) {
		if (AuthUtils.isActionAuthorized(systemConfiguration)) {
			this.systemConfigurationDAO.update(systemConfiguration);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		SystemConfiguration conf = this.systemConfigurationDAO.getByID(id);
		if (AuthUtils.isActionAuthorized(conf)) {
			this.systemConfigurationDAO.delete(id);
		}
	}

	@Override
	@Transactional
	public List<SystemConfiguration> list() {
		AuthenticationToken token = AuthUtils.getAuth();
		if (token.isSuperAdmin()) {
			return this.systemConfigurationDAO.list(null);
		}
		return this.systemConfigurationDAO.list(token.getCompany().getId());
	}

	@Override
	@Transactional
	public String getValueByName(String name) {
		SystemConfiguration conf = this.systemConfigurationDAO.getByName(name);
		if (AuthUtils.isActionAuthorized(conf)) {
			return conf.getValue();
		}
		return "";
	}
}
