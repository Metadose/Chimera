package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.ConcreteProportion;
import com.cebedo.pmsys.domain.PlasterMixture;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.repository.ConcreteProportionValueRepo;
import com.cebedo.pmsys.repository.PlasterMixtureValueRepo;
import com.cebedo.pmsys.service.PlasterMixtureService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class PlasterMixtureServiceImpl implements PlasterMixtureService {

    private AuthHelper authHelper = new AuthHelper();
    private PlasterMixtureValueRepo plasterMixtureValueRepo;
    private ConcreteProportionValueRepo concreteProportionValueRepo;

    public void setConcreteProportionValueRepo(
	    ConcreteProportionValueRepo concreteProportionValueRepo) {
	this.concreteProportionValueRepo = concreteProportionValueRepo;
    }

    public void setPlasterMixtureValueRepo(
	    PlasterMixtureValueRepo plasterMixtureValueRepo) {
	this.plasterMixtureValueRepo = plasterMixtureValueRepo;
    }

    @Override
    @Transactional
    public void rename(PlasterMixture obj, String newKey) {
	this.plasterMixtureValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, PlasterMixture> m) {
	this.plasterMixtureValueRepo.multiSet(m);
    }

    /**
     * Set the plasterMixture.
     */
    @Override
    @Transactional
    public String set(PlasterMixture obj) {

	// Set the proportion.
	ConcreteProportion proportion = this.concreteProportionValueRepo
		.get(obj.getConcreteProportionKey());
	obj.setConcreteProportion(proportion);

	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.plasterMixtureValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_UNIT, obj.getName());
	}

	// If update.
	this.plasterMixtureValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_UNIT, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.plasterMixtureValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(PlasterMixture obj) {
	this.plasterMixtureValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public PlasterMixture get(String key) {
	return this.plasterMixtureValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.plasterMixtureValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<PlasterMixture> multiGet(Collection<String> keys) {
	return this.plasterMixtureValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.plasterMixtureValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<PlasterMixture> list() {
	Company company = this.authHelper.getAuth().getCompany();
	String pattern = PlasterMixture.constructPattern(company);
	Set<String> keys = this.plasterMixtureValueRepo.keys(pattern);
	return this.plasterMixtureValueRepo.multiGet(keys);
    }

}
