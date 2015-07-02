package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.MasonryCHBEstimateResults;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.CHB;
import com.cebedo.pmsys.domain.Estimate;
import com.cebedo.pmsys.domain.MasonryCHBEstimationSummary;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.EstimateValueRepo;
import com.cebedo.pmsys.repository.MasonryCHBEstimationSummaryValueRepo;
import com.cebedo.pmsys.service.MasonryCHBEstimationSummaryService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DataStructUtils;

@Service
public class MasonryCHBEstimationSummaryServiceImpl implements
	MasonryCHBEstimationSummaryService {

    private MasonryCHBEstimationSummaryValueRepo masonryCHBEstimationSummaryValueRepo;
    private EstimateValueRepo estimateValueRepo;

    public void setEstimateValueRepo(EstimateValueRepo estimateValueRepo) {
	this.estimateValueRepo = estimateValueRepo;
    }

    public void setMasonryCHBEstimationSummaryValueRepo(
	    MasonryCHBEstimationSummaryValueRepo masonryCHBEstimationSummaryValueRepo) {
	this.masonryCHBEstimationSummaryValueRepo = masonryCHBEstimationSummaryValueRepo;
    }

    @Override
    @Transactional
    public void rename(MasonryCHBEstimationSummary obj, String newKey) {
	this.masonryCHBEstimationSummaryValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, MasonryCHBEstimationSummary> m) {
	this.masonryCHBEstimationSummaryValueRepo.multiSet(m);
    }

    /**
     * Set the estimationSummary.
     */
    @Override
    @Transactional
    public String set(MasonryCHBEstimationSummary obj) {

	// If we're creating.
	boolean isCreate = obj.getUuid() == null;

	// Get the list of checked estimate list.
	List<Estimate> estimateList = this.estimateValueRepo
		.multiGet(DataStructUtils.convertArrayToList(obj
			.getEstimationToCompute()));

	for (Estimate estimate : estimateList) {

	    // Get the estimated quantity.
	    Map<CHB, MasonryCHBEstimateResults> masonryResultMap = estimate
		    .getResultMapMasonryCHB();

	    // Set the area specifics.
	    obj.setAreaFormulaInputs(estimate.getAreaFormulaInputs());
	    obj.setArea(estimate.getShape().getArea());
	    obj.setEstimationAllowance(estimate.getEstimationAllowance());

	    // For every type of CHB type selected,
	    // there is an individual cost estimate.
	    for (CHB chb : masonryResultMap.keySet()) {

		obj.setChbMeasurement(chb);

		// Get the result.
		// Get total cost =
		// cost per piece * no. of pieces
		MasonryCHBEstimateResults estimateResult = masonryResultMap
			.get(chb);
		double totalPiecesCHB = estimateResult.getTotalCHB();
		obj.setTotalPiecesCHB(totalPiecesCHB);
		double totalCost = obj.getCostPerPieceCHB() * totalPiecesCHB;

		// Set the total cost.
		obj.setTotalCostOfCHB(totalCost);

		// Commit this cost estimate.
		obj.setUuid(UUID.randomUUID());
		this.masonryCHBEstimationSummaryValueRepo.set(obj);
	    }
	}

	// If create.
	if (isCreate) {
	    return AlertBoxGenerator.SUCCESS
		    .generateCreate(
			    RedisConstants.OBJECT_MASONRY_CHB_ESTIMATION_SUMMARY_DISPLAY,
			    obj.getName());
	}

	// If update.
	this.masonryCHBEstimationSummaryValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_MASONRY_CHB_ESTIMATION_SUMMARY_DISPLAY,
		obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.masonryCHBEstimationSummaryValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(MasonryCHBEstimationSummary obj) {
	this.masonryCHBEstimationSummaryValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public MasonryCHBEstimationSummary get(String key) {
	return this.masonryCHBEstimationSummaryValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.masonryCHBEstimationSummaryValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<MasonryCHBEstimationSummary> multiGet(
	    Collection<String> keys) {
	return this.masonryCHBEstimationSummaryValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.masonryCHBEstimationSummaryValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<MasonryCHBEstimationSummary> list(Project proj) {
	String pattern = MasonryCHBEstimationSummary.constructPattern(proj);
	Set<String> keys = this.masonryCHBEstimationSummaryValueRepo
		.keys(pattern);
	return this.masonryCHBEstimationSummaryValueRepo.multiGet(keys);
    }

}
