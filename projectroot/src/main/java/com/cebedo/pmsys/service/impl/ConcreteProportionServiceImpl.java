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
import com.cebedo.pmsys.domain.Unit;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.repository.ConcreteProportionValueRepo;
import com.cebedo.pmsys.repository.UnitValueRepo;
import com.cebedo.pmsys.service.ConcreteProportionService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class ConcreteProportionServiceImpl implements ConcreteProportionService {

    private AuthHelper authHelper = new AuthHelper();
    private ConcreteProportionValueRepo concreteProportionValueRepo;
    private UnitValueRepo unitValueRepo;

    public void setUnitValueRepo(UnitValueRepo unitValueRepo) {
	this.unitValueRepo = unitValueRepo;
    }

    public void setConcreteProportionValueRepo(
	    ConcreteProportionValueRepo concreteProportionValueRepo) {
	this.concreteProportionValueRepo = concreteProportionValueRepo;
    }

    @Override
    @Transactional
    public void rename(ConcreteProportion obj, String newKey) {
	this.concreteProportionValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, ConcreteProportion> m) {
	this.concreteProportionValueRepo.multiSet(m);
    }

    private Unit getUnitObj(String key) {
	return this.unitValueRepo.get(key);
    }

    /**
     * Set the concreteProportion.
     */
    @Override
    @Transactional
    public String set(ConcreteProportion obj) {

	// Set the units.
	Unit cement40Unit = getUnitObj(obj.getUnitKeyCement40kg());
	obj.setUnitCement40kg(cement40Unit);

	Unit cement50Unit = getUnitObj(obj.getUnitKeyCement50kg());
	obj.setUnitCement50kg(cement50Unit);

	Unit sandUnit = getUnitObj(obj.getUnitKeySand());
	obj.setUnitSand(sandUnit);

	Unit gravelUnit = getUnitObj(obj.getUnitKeyGravel());
	obj.setUnitGravel(gravelUnit);

	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.concreteProportionValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_CONCRETE_PROPORTION_DISPLAY,
		    obj.getName());
	}

	// If update.
	this.concreteProportionValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_CONCRETE_PROPORTION_DISPLAY,
		obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.concreteProportionValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(ConcreteProportion obj) {
	this.concreteProportionValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public ConcreteProportion get(String key) {
	return this.concreteProportionValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.concreteProportionValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<ConcreteProportion> multiGet(Collection<String> keys) {
	return this.concreteProportionValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.concreteProportionValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<ConcreteProportion> list() {
	Company company = this.authHelper.getAuth().getCompany();
	String pattern = ConcreteProportion.constructPattern(company);
	Set<String> keys = this.concreteProportionValueRepo.keys(pattern);
	return this.concreteProportionValueRepo.multiGet(keys);
    }

}
