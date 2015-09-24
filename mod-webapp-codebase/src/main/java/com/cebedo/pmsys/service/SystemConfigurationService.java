package com.cebedo.pmsys.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.cebedo.pmsys.model.SystemConfiguration;

public interface SystemConfigurationService {

    public String create(SystemConfiguration systemConfiguration, BindingResult result);

    public SystemConfiguration getByID(long id);

    public String update(SystemConfiguration systemConfiguration, BindingResult result);

    public String delete(long id);

    public List<SystemConfiguration> list();

    public String getValueByName(String name, boolean override);

    public SystemConfiguration getByName(String name, boolean override);

    public String merge(SystemConfiguration config, BindingResult result);

    public Boolean getBetaServer();

}
