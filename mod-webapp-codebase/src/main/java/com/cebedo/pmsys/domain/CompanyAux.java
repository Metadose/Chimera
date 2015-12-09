package com.cebedo.pmsys.domain;

import com.cebedo.pmsys.base.IObjectDomain;
import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.constants.RegistryRedisKeys;
import com.cebedo.pmsys.enums.CategoryPricing;
import com.cebedo.pmsys.model.Company;

public class CompanyAux implements IObjectDomain {

    private static final long serialVersionUID = -2047831218965779560L;

    private Company company;
    private int categoryPricing = CategoryPricing.TRIAL_1.getId();
    private int limitProjects = 1;
    private String remarks = "";

    public CompanyAux() {
	;
    }

    public CompanyAux(Company company2) {
	this.company = company2;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    @Override
    public String getName() {
	return this.company.getName();
    }

    @Override
    public String getObjectName() {
	return ConstantsRedis.OBJECT_AUX_COMPANY;
    }

    @Override
    public Company getCompany() {
	return this.company;
    }

    public static String constructKey(Company com) {
	return String.format(RegistryRedisKeys.KEY_AUX_COMPANY, com.getId());
    }

    @Override
    public String getKey() {
	return String.format(RegistryRedisKeys.KEY_AUX_COMPANY, this.company.getId());
    }

    public int getCategoryPricing() {
	return categoryPricing;
    }

    public void setCategoryPricing(int categoryPricing) {
	this.categoryPricing = categoryPricing;
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

    public int getLimitProjects() {
	return limitProjects;
    }

    public void setLimitProjects(int limitProjects) {
	this.limitProjects = limitProjects;
    }

    public CompanyAux clone() {
	try {
	    return (CompanyAux) super.clone();
	} catch (Exception e) {
	    return null;
	}
    }

}
