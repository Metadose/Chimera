package com.cebedo.pmsys.dao;

import java.util.List;

import com.cebedo.pmsys.model.SystemConfiguration;

public interface SystemConfigurationDAO {

	public void create(SystemConfiguration systemConfiguration);

	public SystemConfiguration getByID(long id);

	public void update(SystemConfiguration systemConfiguration);

	public void delete(long id);

	public List<SystemConfiguration> list(Long companyID);

	public String getValueByName(String name);

	public SystemConfiguration getByName(String name);

	public void merge(SystemConfiguration config);

}
