package com.cebedo.pmsys.service;

import java.util.List;

import com.cebedo.pmsys.model.SystemConfiguration;

public interface SystemConfigurationService {

	public void create(SystemConfiguration systemConfiguration);

	public SystemConfiguration getByID(long id);

	public void update(SystemConfiguration systemConfiguration);

	public void delete(long id);

	public List<SystemConfiguration> list();

	public String getValueByName(String name);

	public SystemConfiguration getByName(String name);

}
