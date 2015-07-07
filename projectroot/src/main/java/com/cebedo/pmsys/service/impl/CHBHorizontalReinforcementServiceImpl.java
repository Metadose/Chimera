package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.CHBHorizontalReinforcement;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.repository.CHBHorizontalReinforcementValueRepo;
import com.cebedo.pmsys.service.CHBHorizontalReinforcementService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class CHBHorizontalReinforcementServiceImpl implements
	CHBHorizontalReinforcementService {

    private AuthHelper authHelper = new AuthHelper();
    private CHBHorizontalReinforcementValueRepo chbHorizontalReinforcementValueRepo;

    public void setChbHorizontalReinforcementValueRepo(
	    CHBHorizontalReinforcementValueRepo chbHorizontalReinforcementValueRepo) {
	this.chbHorizontalReinforcementValueRepo = chbHorizontalReinforcementValueRepo;
    }

    @Override
    @Transactional
    public void rename(CHBHorizontalReinforcement obj, String newKey) {
	this.chbHorizontalReinforcementValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, CHBHorizontalReinforcement> m) {
	this.chbHorizontalReinforcementValueRepo.multiSet(m);
    }

    /**
     * Set the chbHorizontalReinforcement.
     */
    @Override
    @Transactional
    public String set(CHBHorizontalReinforcement obj) {

	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.chbHorizontalReinforcementValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_UNIT, obj.getName());
	}

	// If update.
	this.chbHorizontalReinforcementValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_UNIT, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.chbHorizontalReinforcementValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(CHBHorizontalReinforcement obj) {
	this.chbHorizontalReinforcementValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public CHBHorizontalReinforcement get(String key) {
	return this.chbHorizontalReinforcementValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.chbHorizontalReinforcementValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<CHBHorizontalReinforcement> multiGet(
	    Collection<String> keys) {
	return this.chbHorizontalReinforcementValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.chbHorizontalReinforcementValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<CHBHorizontalReinforcement> list() {
	Company company = this.authHelper.getAuth().getCompany();
	String pattern = CHBHorizontalReinforcement.constructPattern(company);
	Set<String> keys = this.chbHorizontalReinforcementValueRepo
		.keys(pattern);
	return this.chbHorizontalReinforcementValueRepo.multiGet(keys);
    }

}
