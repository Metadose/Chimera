package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.BlockLayingMixture;
import com.cebedo.pmsys.domain.CHB;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.repository.BlockLayingMixtureValueRepo;
import com.cebedo.pmsys.repository.CHBValueRepo;
import com.cebedo.pmsys.service.BlockLayingMixtureService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class BlockLayingMixtureServiceImpl implements BlockLayingMixtureService {

    private AuthHelper authHelper = new AuthHelper();
    private BlockLayingMixtureValueRepo blockLayingMixtureValueRepo;
    private CHBValueRepo chbValueRepo;

    public void setChbValueRepo(CHBValueRepo chbValueRepo) {
	this.chbValueRepo = chbValueRepo;
    }

    public void setBlockLayingMixtureValueRepo(
	    BlockLayingMixtureValueRepo blockLayingMixtureValueRepo) {
	this.blockLayingMixtureValueRepo = blockLayingMixtureValueRepo;
    }

    @Override
    @Transactional
    public void rename(BlockLayingMixture obj, String newKey) {
	this.blockLayingMixtureValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, BlockLayingMixture> m) {
	this.blockLayingMixtureValueRepo.multiSet(m);
    }

    /**
     * Set the blockLayingMixture.
     */
    @Override
    @Transactional
    public String set(BlockLayingMixture obj) {

	// Set the CHB object from key.
	CHB chbMeasurement = this.chbValueRepo.get(obj.getChbKey());
	obj.setChbMeasurement(chbMeasurement);

	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.blockLayingMixtureValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_UNIT, obj.getName());
	}

	// If update.
	this.blockLayingMixtureValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_UNIT, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.blockLayingMixtureValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(BlockLayingMixture obj) {
	this.blockLayingMixtureValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public BlockLayingMixture get(String key) {
	return this.blockLayingMixtureValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.blockLayingMixtureValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<BlockLayingMixture> multiGet(Collection<String> keys) {
	return this.blockLayingMixtureValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.blockLayingMixtureValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<BlockLayingMixture> list() {
	Company company = this.authHelper.getAuth().getCompany();
	String pattern = BlockLayingMixture.constructPattern(company);
	Set<String> keys = this.blockLayingMixtureValueRepo.keys(pattern);
	return this.blockLayingMixtureValueRepo.multiGet(keys);
    }

}
