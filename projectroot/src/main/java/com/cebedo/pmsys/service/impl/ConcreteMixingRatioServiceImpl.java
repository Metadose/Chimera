package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.ConcreteMixingRatio;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.repository.ConcreteMixingRatioValueRepo;
import com.cebedo.pmsys.service.ConcreteMixingRatioService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class ConcreteMixingRatioServiceImpl implements
	ConcreteMixingRatioService {

    private AuthHelper authHelper = new AuthHelper();
    private ConcreteMixingRatioValueRepo concreteMixingRatioValueRepo;

    public void setConcreteMixingRatioValueRepo(
	    ConcreteMixingRatioValueRepo concreteMixingRatioValueRepo) {
	this.concreteMixingRatioValueRepo = concreteMixingRatioValueRepo;
    }

    @Override
    @Transactional
    public void rename(ConcreteMixingRatio obj, String newKey) {
	this.concreteMixingRatioValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, ConcreteMixingRatio> m) {
	this.concreteMixingRatioValueRepo.multiSet(m);
    }

    /**
     * Set the concreteMixingRatio.
     */
    @Override
    @Transactional
    public String set(ConcreteMixingRatio obj) {

	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.concreteMixingRatioValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_UNIT, obj.getName());
	}

	// If update.
	this.concreteMixingRatioValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_UNIT, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.concreteMixingRatioValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(ConcreteMixingRatio obj) {
	this.concreteMixingRatioValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public ConcreteMixingRatio get(String key) {
	return this.concreteMixingRatioValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.concreteMixingRatioValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<ConcreteMixingRatio> multiGet(Collection<String> keys) {
	return this.concreteMixingRatioValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.concreteMixingRatioValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<ConcreteMixingRatio> list() {
	return null;
    }

}
