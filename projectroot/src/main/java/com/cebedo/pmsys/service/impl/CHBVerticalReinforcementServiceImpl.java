package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.CHBVerticalReinforcement;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.repository.CHBVerticalReinforcementValueRepo;
import com.cebedo.pmsys.service.CHBVerticalReinforcementService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class CHBVerticalReinforcementServiceImpl implements
	CHBVerticalReinforcementService {

    private AuthHelper authHelper = new AuthHelper();
    private CHBVerticalReinforcementValueRepo chbVerticalReinforcementValueRepo;

    public void setChbVerticalReinforcementValueRepo(
	    CHBVerticalReinforcementValueRepo chbVerticalReinforcementValueRepo) {
	this.chbVerticalReinforcementValueRepo = chbVerticalReinforcementValueRepo;
    }

    @Override
    @Transactional
    public void rename(CHBVerticalReinforcement obj, String newKey) {
	this.chbVerticalReinforcementValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, CHBVerticalReinforcement> m) {
	this.chbVerticalReinforcementValueRepo.multiSet(m);
    }

    /**
     * Set the chbVerticalReinforcement.
     */
    @Override
    @Transactional
    public String set(CHBVerticalReinforcement obj) {

	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.chbVerticalReinforcementValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_UNIT, obj.getName());
	}

	// If update.
	this.chbVerticalReinforcementValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_UNIT, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.chbVerticalReinforcementValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(CHBVerticalReinforcement obj) {
	this.chbVerticalReinforcementValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public CHBVerticalReinforcement get(String key) {
	return this.chbVerticalReinforcementValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.chbVerticalReinforcementValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<CHBVerticalReinforcement> multiGet(Collection<String> keys) {
	return this.chbVerticalReinforcementValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.chbVerticalReinforcementValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<CHBVerticalReinforcement> list() {
	Company company = this.authHelper.getAuth().getCompany();
	String pattern = CHBVerticalReinforcement.constructPattern(company);
	Set<String> keys = this.chbVerticalReinforcementValueRepo.keys(pattern);
	return this.chbVerticalReinforcementValueRepo.multiGet(keys);
    }

}
