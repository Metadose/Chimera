package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.CHB;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.repository.CHBValueRepo;
import com.cebedo.pmsys.service.CHBService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class CHBServiceImpl implements CHBService {

    private AuthHelper authHelper = new AuthHelper();
    private CHBValueRepo chbValueRepo;

    public void setChbValueRepo(CHBValueRepo chbValueRepo) {
	this.chbValueRepo = chbValueRepo;
    }

    @Override
    @Transactional
    public void rename(CHB obj, String newKey) {
	this.chbValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, CHB> m) {
	this.chbValueRepo.multiSet(m);
    }

    /**
     * Set the chb.
     */
    @Override
    @Transactional
    public String set(CHB obj) {

	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.chbValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_UNIT, obj.getName());
	}

	// If update.
	this.chbValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_UNIT, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.chbValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(CHB obj) {
	this.chbValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public CHB get(String key) {
	return this.chbValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.chbValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<CHB> multiGet(Collection<String> keys) {
	return this.chbValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.chbValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<CHB> list() {
	Company company = this.authHelper.getAuth().getCompany();
	String pattern = CHB.constructPattern(company);
	Set<String> keys = this.chbValueRepo.keys(pattern);
	return this.chbValueRepo.multiGet(keys);
    }

}
