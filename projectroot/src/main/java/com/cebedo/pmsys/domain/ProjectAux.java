package com.cebedo.pmsys.domain;

import java.util.Map;

import com.cebedo.pmsys.constants.RedisKeyRegistry;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Project;

public class ProjectAux implements IDomainObject {

    private static final long serialVersionUID = 4250896962237506975L;

    private Company company;
    private Project project;

    /**
     * Extension map.
     */
    private Map<String, Object> extMap;

    public Map<String, Object> getExtMap() {
	return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
	this.extMap = extMap;
    }

    /**
     * company.fk:%s:project:%s
     */
    @Override
    public String getKey() {
	return String.format(RedisKeyRegistry.KEY_PROJECT_AUX,
		this.company.getId(), this.project.getId());
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

}
