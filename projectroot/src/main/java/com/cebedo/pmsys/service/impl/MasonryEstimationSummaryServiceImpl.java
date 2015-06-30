package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.MasonryEstimateResults;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.CHB;
import com.cebedo.pmsys.domain.Estimate;
import com.cebedo.pmsys.domain.MasonryEstimationSummary;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.EstimateValueRepo;
import com.cebedo.pmsys.repository.MasonryEstimationSummaryValueRepo;
import com.cebedo.pmsys.service.MasonryEstimationSummaryService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DataStructUtils;

@Service
public class MasonryEstimationSummaryServiceImpl implements
	MasonryEstimationSummaryService {

    private MasonryEstimationSummaryValueRepo masonryEstimationSummaryValueRepo;
    private EstimateValueRepo estimateValueRepo;

    public void setEstimateValueRepo(EstimateValueRepo estimateValueRepo) {
	this.estimateValueRepo = estimateValueRepo;
    }

    public void setMasonryEstimationSummaryValueRepo(
	    MasonryEstimationSummaryValueRepo masonryEstimationSummaryValueRepo) {
	this.masonryEstimationSummaryValueRepo = masonryEstimationSummaryValueRepo;
    }

    @Override
    @Transactional
    public void rename(MasonryEstimationSummary obj, String newKey) {
	this.masonryEstimationSummaryValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, MasonryEstimationSummary> m) {
	this.masonryEstimationSummaryValueRepo.multiSet(m);
    }

    /**
     * Set the masonryEstimationSummary.
     */
    @Override
    @Transactional
    public String set(MasonryEstimationSummary obj) {

	// If we're creating.
	boolean isCreate = obj.getUuid() == null;

	// Get the list of checked estimate list.
	List<Estimate> estimateList = this.estimateValueRepo
		.multiGet(DataStructUtils.convertArrayToList(obj
			.getEstimationToCompute()));

	for (Estimate estimate : estimateList) {

	    // Get the estimated quantity.
	    Map<CHB, MasonryEstimateResults> masonryResultMap = estimate
		    .getResultMapMasonry();

	    // For every type of CHB type selected,
	    // there is an individual cost estimate.
	    for (CHB chb : masonryResultMap.keySet()) {

		// Get the result.
		// Get total cost =
		// cost per piece * no. of pieces
		MasonryEstimateResults estimateResult = masonryResultMap
			.get(chb);
		double totalCost = obj.getCostPerPieceCHB()
			* estimateResult.getTotalCHB();

		// Set the total cost.
		obj.setTotalCostOfCHB(totalCost);

		// Commit this cost estimate.
		obj.setUuid(UUID.randomUUID());
		this.masonryEstimationSummaryValueRepo.set(obj);
	    }
	}

	// If create.
	if (isCreate) {
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_MASONRY_ESTIMATION_SUMMARY_DISPLAY,
		    obj.getName());
	}

	// If update.
	this.masonryEstimationSummaryValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_MASONRY_ESTIMATION_SUMMARY_DISPLAY,
		obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.masonryEstimationSummaryValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(MasonryEstimationSummary obj) {
	this.masonryEstimationSummaryValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public MasonryEstimationSummary get(String key) {
	return this.masonryEstimationSummaryValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.masonryEstimationSummaryValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<MasonryEstimationSummary> multiGet(Collection<String> keys) {
	return this.masonryEstimationSummaryValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.masonryEstimationSummaryValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<MasonryEstimationSummary> list(Project proj) {
	String pattern = MasonryEstimationSummary.constructPattern(proj);
	Set<String> keys = this.masonryEstimationSummaryValueRepo.keys(pattern);
	return this.masonryEstimationSummaryValueRepo.multiGet(keys);
    }

}
