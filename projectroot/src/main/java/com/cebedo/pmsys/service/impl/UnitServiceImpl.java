package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.Unit;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.repository.UnitValueRepo;
import com.cebedo.pmsys.service.UnitService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class UnitServiceImpl implements UnitService {

    private AuthHelper authHelper = new AuthHelper();
    private UnitValueRepo unitValueRepo;

    public void setUnitValueRepo(UnitValueRepo unitValueRepo) {
	this.unitValueRepo = unitValueRepo;
    }

    @Override
    @Transactional
    public void rename(Unit obj, String newKey) {
	this.unitValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, Unit> m) {
	this.unitValueRepo.multiSet(m);
    }

    /**
     * Set the unit.
     */
    @Override
    @Transactional
    public String set(Unit obj) {

	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.unitValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_UNIT, obj.getName());
	}

	// If update.
	this.unitValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_UNIT, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.unitValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(Unit obj) {
	this.unitValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public Unit get(String key) {
	return this.unitValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.unitValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<Unit> multiGet(Collection<String> keys) {
	return this.unitValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.unitValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<Unit> list() {
	Company company = this.authHelper.getAuth().getCompany();
	String pattern = Unit.constructPattern(company);
	Set<String> keys = this.unitValueRepo.keys(pattern);
	return this.unitValueRepo.multiGet(keys);
    }

}
