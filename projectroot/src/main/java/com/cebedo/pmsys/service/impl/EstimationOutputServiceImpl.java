package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.EstimationOutput;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.EstimationOutputValueRepo;
import com.cebedo.pmsys.service.EstimationOutputService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class EstimationOutputServiceImpl implements EstimationOutputService {

    private EstimationOutputValueRepo estimationOutputValueRepo;

    public void setEstimationOutputValueRepo(
	    EstimationOutputValueRepo estimationOutputValueRepo) {
	this.estimationOutputValueRepo = estimationOutputValueRepo;
    }

    @Override
    @Transactional
    public void rename(EstimationOutput obj, String newKey) {
	this.estimationOutputValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, EstimationOutput> m) {
	this.estimationOutputValueRepo.multiSet(m);
    }

    /**
     * Set the estimationOutput.
     */
    @Override
    @Transactional
    public String set(EstimationOutput obj) {

	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.estimationOutputValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_UNIT, "TODO");
	}

	// If update.
	this.estimationOutputValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_UNIT, "TODO");
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.estimationOutputValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(EstimationOutput obj) {
	this.estimationOutputValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public EstimationOutput get(String key) {
	return this.estimationOutputValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.estimationOutputValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<EstimationOutput> multiGet(Collection<String> keys) {
	return this.estimationOutputValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.estimationOutputValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<EstimationOutput> list(Project proj) {
	String pattern = EstimationOutput.constructPattern(proj);
	Set<String> keys = this.estimationOutputValueRepo.keys(pattern);
	return this.estimationOutputValueRepo.multiGet(keys);
    }

}
