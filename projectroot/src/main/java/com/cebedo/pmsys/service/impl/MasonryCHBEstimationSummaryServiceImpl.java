package com.cebedo.pmsys.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.MasonryCHBEstimateResults;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.Estimate;
import com.cebedo.pmsys.domain.MasonryCHBEstimationSummary;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.repository.EstimateValueRepo;
import com.cebedo.pmsys.repository.MasonryCHBEstimationSummaryValueRepo;
import com.cebedo.pmsys.service.MasonryCHBEstimationSummaryService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DataStructUtils;

@Service
public class MasonryCHBEstimationSummaryServiceImpl implements MasonryCHBEstimationSummaryService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private MasonryCHBEstimationSummaryValueRepo masonryCHBEstimationSummaryValueRepo;
    private EstimateValueRepo estimateValueRepo;

    public void setEstimateValueRepo(EstimateValueRepo estimateValueRepo) {
	this.estimateValueRepo = estimateValueRepo;
    }

    public void setMasonryCHBEstimationSummaryValueRepo(
	    MasonryCHBEstimationSummaryValueRepo masonryCHBEstimationSummaryValueRepo) {
	this.masonryCHBEstimationSummaryValueRepo = masonryCHBEstimationSummaryValueRepo;
    }

    /**
     * Set the estimationSummary.
     */
    @Override
    @Transactional
    public String set(MasonryCHBEstimationSummary obj) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(obj)) {
	    this.messageHelper.unauthorized(RedisConstants.OBJECT_MASONRY_CHB_ESTIMATION_SUMMARY,
		    obj.getKey());
	    return AlertBoxGenerator.ERROR;
	}

	// If we're creating.
	boolean isCreate = obj.getUuid() == null;

	// Get the list of checked estimate list.
	List<Estimate> estimateList = this.estimateValueRepo.multiGet(DataStructUtils
		.convertArrayToList(obj.getEstimationToCompute()));

	for (Estimate estimate : estimateList) {

	    // Get the estimated quantity.
	    MasonryCHBEstimateResults chbEstimate = estimate.getResultCHBEstimate();

	    // Set the area specifics.
	    obj.setArea(estimate.getShape().getArea());
	    obj.setEstimationAllowance(estimate.getEstimationAllowance());

	    // For every type of CHB type selected,
	    // there is an individual cost estimate.

	    obj.setChbDimensions(chbEstimate.getChbDimensions());

	    // Get the result.
	    // Get total cost =
	    // cost per piece * no. of pieces
	    double totalPiecesCHB = chbEstimate.getTotalCHB();
	    obj.setTotalPiecesCHB(totalPiecesCHB);
	    double totalCost = obj.getCostPerPieceCHB() * totalPiecesCHB;

	    // Set the total cost.
	    obj.setTotalCostOfCHB(totalCost);

	    // Commit this cost estimate.
	    obj.setUuid(UUID.randomUUID());
	    this.masonryCHBEstimationSummaryValueRepo.set(obj);
	}

	// Log.
	this.messageHelper.send(AuditAction.SET, RedisConstants.OBJECT_MASONRY_CHB_ESTIMATION_SUMMARY,
		obj.getKey());

	// If create.
	if (isCreate) {
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_MASONRY_CHB_ESTIMATION_SUMMARY_DISPLAY, obj.getName());
	}

	// If update.
	this.masonryCHBEstimationSummaryValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_MASONRY_CHB_ESTIMATION_SUMMARY_DISPLAY, obj.getName());
    }

}
