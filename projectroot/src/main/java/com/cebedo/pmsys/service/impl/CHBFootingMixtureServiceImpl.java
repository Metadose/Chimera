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
import com.cebedo.pmsys.domain.CHBFootingMixture;
import com.cebedo.pmsys.domain.ConcreteProportion;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.repository.CHBFootingDimensionValueRepo;
import com.cebedo.pmsys.repository.CHBFootingMixtureValueRepo;
import com.cebedo.pmsys.repository.ConcreteProportionValueRepo;
import com.cebedo.pmsys.service.CHBFootingMixtureService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class CHBFootingMixtureServiceImpl implements CHBFootingMixtureService {

    private AuthHelper authHelper = new AuthHelper();
    private CHBFootingMixtureValueRepo chbFootingMixtureValueRepo;
    private ConcreteProportionValueRepo concreteProportionValueRepo;
    private CHBFootingDimensionValueRepo chbFootingDimensionValueRepo;

    public void setChbFootingMixtureValueRepo(
	    CHBFootingMixtureValueRepo chbFootingMixtureValueRepo) {
	this.chbFootingMixtureValueRepo = chbFootingMixtureValueRepo;
    }

    public void setConcreteProportionValueRepo(
	    ConcreteProportionValueRepo concreteProportionValueRepo) {
	this.concreteProportionValueRepo = concreteProportionValueRepo;
    }

    public void setChbFootingDimensionValueRepo(
	    CHBFootingDimensionValueRepo chbFootingDimensionValueRepo) {
	this.chbFootingDimensionValueRepo = chbFootingDimensionValueRepo;
    }

    @Override
    @Transactional
    public void rename(CHBFootingMixture obj, String newKey) {
	this.chbFootingMixtureValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, CHBFootingMixture> m) {
	this.chbFootingMixtureValueRepo.multiSet(m);
    }

    /**
     * Set the chbFootingMixture.
     */
    @Override
    @Transactional
    public String set(CHBFootingMixture obj) {

	CHBFootingDimension footingDimension = this.chbFootingDimensionValueRepo
		.get(obj.getChbFootingDimensionKey());
	ConcreteProportion prop = this.concreteProportionValueRepo.get(obj
		.getConcreteProportionKey());
	obj.setFootingDimension(footingDimension);
	obj.setConcreteProportion(prop);

	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.chbFootingMixtureValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_UNIT, obj.getName());
	}

	// If update.
	this.chbFootingMixtureValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_UNIT, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.chbFootingMixtureValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(CHBFootingMixture obj) {
	this.chbFootingMixtureValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public CHBFootingMixture get(String key) {
	return this.chbFootingMixtureValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.chbFootingMixtureValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<CHBFootingMixture> multiGet(Collection<String> keys) {
	return this.chbFootingMixtureValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.chbFootingMixtureValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<CHBFootingMixture> list() {
	Company company = this.authHelper.getAuth().getCompany();
	String pattern = CHBFootingMixture.constructPattern(company);
	Set<String> keys = this.chbFootingMixtureValueRepo.keys(pattern);
	return this.chbFootingMixtureValueRepo.multiGet(keys);
    }

}
