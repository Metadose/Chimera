package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.CHBFootingDimension;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.repository.CHBFootingDimensionValueRepo;
import com.cebedo.pmsys.service.CHBFootingDimensionService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class CHBFootingDimensionServiceImpl implements
	CHBFootingDimensionService {

    private AuthHelper authHelper = new AuthHelper();
    private CHBFootingDimensionValueRepo chbFootingDimensionValueRepo;

    public void setChbFootingDimensionValueRepo(
	    CHBFootingDimensionValueRepo chbFootingDimensionValueRepo) {
	this.chbFootingDimensionValueRepo = chbFootingDimensionValueRepo;
    }

    @Override
    @Transactional
    public void rename(CHBFootingDimension obj, String newKey) {
	this.chbFootingDimensionValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, CHBFootingDimension> m) {
	this.chbFootingDimensionValueRepo.multiSet(m);
    }

    /**
     * Set the chbFootingDimension.
     */
    @Override
    @Transactional
    public String set(CHBFootingDimension obj) {

	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.chbFootingDimensionValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_UNIT, obj.getName());
	}

	// If update.
	this.chbFootingDimensionValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_UNIT, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.chbFootingDimensionValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(CHBFootingDimension obj) {
	this.chbFootingDimensionValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public CHBFootingDimension get(String key) {
	return this.chbFootingDimensionValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.chbFootingDimensionValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<CHBFootingDimension> multiGet(Collection<String> keys) {
	return this.chbFootingDimensionValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.chbFootingDimensionValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<CHBFootingDimension> list() {
	Company company = this.authHelper.getAuth().getCompany();
	String pattern = CHBFootingDimension.constructPattern(company);
	Set<String> keys = this.chbFootingDimensionValueRepo.keys(pattern);
	return this.chbFootingDimensionValueRepo.multiGet(keys);
    }

}
