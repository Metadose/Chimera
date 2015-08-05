package com.cebedo.pmsys.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.ConcreteEstimateResults;
import com.cebedo.pmsys.domain.ConcreteEstimationSummary;
import com.cebedo.pmsys.domain.Estimate;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.ConcreteEstimationSummaryValueRepo;
import com.cebedo.pmsys.repository.EstimateValueRepo;
import com.cebedo.pmsys.service.ConcreteEstimationSummaryService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.DataStructUtils;

@Service
public class ConcreteEstimationSummaryServiceImpl implements ConcreteEstimationSummaryService {

    private ConcreteEstimationSummaryValueRepo concreteEstimationSummaryValueRepo;
    private EstimateValueRepo estimateValueRepo;

    public void setEstimateValueRepo(EstimateValueRepo estimateValueRepo) {
	this.estimateValueRepo = estimateValueRepo;
    }

    public void setConcreteEstimationSummaryValueRepo(
	    ConcreteEstimationSummaryValueRepo concreteEstimationSummaryValueRepo) {
	this.concreteEstimationSummaryValueRepo = concreteEstimationSummaryValueRepo;
    }

    @Override
    @Transactional
    public void rename(ConcreteEstimationSummary obj, String newKey) {
	this.concreteEstimationSummaryValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, ConcreteEstimationSummary> m) {
	this.concreteEstimationSummaryValueRepo.multiSet(m);
    }

    /**
     * Set the concreteEstimationSummary.
     */
    @Override
    @Transactional
    public String set(ConcreteEstimationSummary obj) {

	boolean isCreate = obj.getUuid() == null;

	// Loop through each checked quantity estimate.
	// Then get the sum.
	List<Estimate> estimateList = this.estimateValueRepo.multiGet(DataStructUtils
		.convertArrayToList(obj.getEstimationToCompute()));

	// For each estimate chosen in JSP.
	for (Estimate estimate : estimateList) {

	    // Get result map for concrete.
	    ConcreteEstimateResults concreteEstimate = estimate.getResultConcreteEstimate();
	    obj.setEstimationAllowance(estimate.getEstimationAllowance());

	    // For every summary, every proportion must have a different
	    // total and grand total.

	    obj.setConcreteProportion(estimate.getConcreteProportion());

	    // Get the values.
	    double unitsCement40kg = concreteEstimate.getCement40kg();
	    double unitsCement50kg = concreteEstimate.getCement50kg();
	    double unitsSand = concreteEstimate.getSand();
	    double unitsGravel = concreteEstimate.getGravel();

	    // Set the values as the object's
	    // quantity estimate.
	    obj.setTotalUnitsCement40kg(unitsCement40kg);
	    obj.setTotalUnitsCement50kg(unitsCement50kg);
	    obj.setTotalUnitsSand(unitsSand);
	    obj.setTotalUnitsGravel(unitsGravel);

	    // Compute for the total cost of each component.
	    double totalCostCement40kg = unitsCement40kg * obj.getCostPerUnitCement40kg();
	    double totalCostCement50kg = unitsCement50kg * obj.getCostPerUnitCement50kg();
	    double totalCostSand = unitsSand * obj.getCostPerUnitSand();
	    double totalCostGravel = unitsGravel * obj.getCostPerUnitGravel();

	    // Set the costs.
	    obj.setTotalCostCement40kg(totalCostCement40kg);
	    obj.setTotalCostCement50kg(totalCostCement50kg);
	    obj.setTotalCostSand(totalCostSand);
	    obj.setTotalCostGravel(totalCostGravel);

	    // Compute for the grand total.
	    obj.setGrandTotalCostIf40kg(totalCostCement40kg + totalCostSand + totalCostGravel);
	    obj.setGrandTotalCostIf50kg(totalCostCement50kg + totalCostSand + totalCostGravel);

	    // Set last computed.
	    obj.setLastComputed(new Date(System.currentTimeMillis()));

	    // Commit.
	    obj.setUuid(UUID.randomUUID());
	    this.concreteEstimationSummaryValueRepo.set(obj);
	}

	// If create.
	if (isCreate) {
	    return AlertBoxGenerator.SUCCESS.generateCreate("TODO", obj.getName());
	}

	// If update.
	this.concreteEstimationSummaryValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate("TODO", obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.concreteEstimationSummaryValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(ConcreteEstimationSummary obj) {
	this.concreteEstimationSummaryValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public ConcreteEstimationSummary get(String key) {
	return this.concreteEstimationSummaryValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.concreteEstimationSummaryValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<ConcreteEstimationSummary> multiGet(Collection<String> keys) {
	return this.concreteEstimationSummaryValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.concreteEstimationSummaryValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<ConcreteEstimationSummary> list(Project proj) {
	String pattern = ConcreteEstimationSummary.constructPattern(proj);
	Set<String> keys = this.concreteEstimationSummaryValueRepo.keys(pattern);
	return this.concreteEstimationSummaryValueRepo.multiGet(keys);
    }

}
