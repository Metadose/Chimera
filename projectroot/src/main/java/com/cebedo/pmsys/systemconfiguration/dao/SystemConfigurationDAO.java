package com.cebedo.pmsys.systemconfiguration.dao;

import java.util.List;

import com.cebedo.pmsys.systemconfiguration.model.SystemConfiguration;

public interface SystemConfigurationDAO {

	public void create(SystemConfiguration systemConfiguration);

	public SystemConfiguration getByID(long id);

	public void update(SystemConfiguration systemConfiguration);

	public void delete(long id);

	public List<SystemConfiguration> list();

	public String getValueByName(String name);

}
