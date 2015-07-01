package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.EstimationAllowance;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.repository.EstimationAllowanceValueRepo;
import com.cebedo.pmsys.service.EstimationAllowanceService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class EstimationAllowanceServiceImpl implements
	EstimationAllowanceService {

    private AuthHelper authHelper = new AuthHelper();
    private EstimationAllowanceValueRepo estimationAllowanceValueRepo;

    public void setEstimationAllowanceValueRepo(
	    EstimationAllowanceValueRepo estimationAllowanceValueRepo) {
	this.estimationAllowanceValueRepo = estimationAllowanceValueRepo;
    }

    @Override
    @Transactional
    public void rename(EstimationAllowance obj, String newKey) {
	this.estimationAllowanceValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, EstimationAllowance> m) {
	this.estimationAllowanceValueRepo.multiSet(m);
    }

    /**
     * Set the estimationAllowance.
     */
    @Override
    @Transactional
    public String set(EstimationAllowance obj) {

	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.estimationAllowanceValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_UNIT, obj.getName());
	}

	// If update.
	this.estimationAllowanceValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_UNIT, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.estimationAllowanceValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(EstimationAllowance obj) {
	this.estimationAllowanceValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public EstimationAllowance get(String key) {
	return this.estimationAllowanceValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.estimationAllowanceValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<EstimationAllowance> multiGet(Collection<String> keys) {
	return this.estimationAllowanceValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.estimationAllowanceValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<EstimationAllowance> list() {
	Company company = this.authHelper.getAuth().getCompany();
	String pattern = EstimationAllowance.constructPattern(company);
	Set<String> keys = this.estimationAllowanceValueRepo.keys(pattern);
	return this.estimationAllowanceValueRepo.multiGet(keys);
    }

}
