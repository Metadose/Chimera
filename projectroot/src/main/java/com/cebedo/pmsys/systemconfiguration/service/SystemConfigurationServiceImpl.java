package com.cebedo.pmsys.systemconfiguration.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	}

	@Override
	@Transactional
	public SystemConfiguration getByID(long id) {
		return this.systemConfigurationDAO.getByID(id);
	}

	@Override
	@Transactional
	public void update(SystemConfiguration systemConfiguration) {
		this.systemConfigurationDAO.update(systemConfiguration);
	}

	@Override
	@Transactional
	public void delete(long id) {
		this.systemConfigurationDAO.delete(id);
	}

	@Override
	@Transactional
	public List<SystemConfiguration> list() {
		return this.systemConfigurationDAO.list();
	}

}
