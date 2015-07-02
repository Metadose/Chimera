package com.cebedo.pmsys.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.ConcreteEstimateResults;
import com.cebedo.pmsys.bean.MasonryBlockLayingEstimateResults;
import com.cebedo.pmsys.bean.MasonryCHBEstimateResults;
import com.cebedo.pmsys.constants.MessageRegistry;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.BlockLayingMixture;
import com.cebedo.pmsys.domain.CHB;
import com.cebedo.pmsys.domain.ConcreteProportion;
import com.cebedo.pmsys.domain.Estimate;
import com.cebedo.pmsys.domain.EstimationAllowance;
import com.cebedo.pmsys.domain.Shape;
import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.enums.EstimateType;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.CHBValueRepo;
import com.cebedo.pmsys.repository.ConcreteProportionValueRepo;
import com.cebedo.pmsys.repository.EstimateValueRepo;
import com.cebedo.pmsys.repository.EstimationAllowanceValueRepo;
import com.cebedo.pmsys.repository.ShapeValueRepo;
import com.cebedo.pmsys.service.BlockLayingMixtureService;
import com.cebedo.pmsys.service.EstimateService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.udojava.evalex.Expression;

@Service
public class EstimateServiceImpl implements EstimateService {

    private EstimateValueRepo estimateValueRepo;
    private ShapeValueRepo shapeValueRepo;
    private ConcreteProportionValueRepo concreteProportionValueRepo;
    private CHBValueRepo chbValueRepo;
    private EstimationAllowanceValueRepo estimationAllowanceValueRepo;
    private BlockLayingMixtureService blockLayingMixtureService;

    public void setBlockLayingMixtureService(
	    BlockLayingMixtureService blockLayingMixtureService) {
	this.blockLayingMixtureService = blockLayingMixtureService;
    }

    public void setEstimationAllowanceValueRepo(
	    EstimationAllowanceValueRepo estimationAllowanceValueRepo) {
	this.estimationAllowanceValueRepo = estimationAllowanceValueRepo;
    }

    public void setChbValueRepo(CHBValueRepo chbValueRepo) {
	this.chbValueRepo = chbValueRepo;
    }

    public void setConcreteProportionValueRepo(
	    ConcreteProportionValueRepo concreteProportionValueRepo) {
	this.concreteProportionValueRepo = concreteProportionValueRepo;
    }

    public void setShapeValueRepo(ShapeValueRepo shapeValueRepo) {
	this.shapeValueRepo = shapeValueRepo;
    }

    public void setEstimateValueRepo(EstimateValueRepo estimateValueRepo) {
	this.estimateValueRepo = estimateValueRepo;
    }

    @Override
    @Transactional
    public void rename(Estimate obj, String newKey) {
	this.estimateValueRepo.rename(obj, newKey);
    }

    @Override
    @Transactional
    public void multiSet(Map<String, Estimate> m) {
	this.estimateValueRepo.multiSet(m);
    }

    /**
     * Set the estimate.
     */
    @Override
    @Transactional
    public String set(Estimate obj) {

	// Set the shape.
	Shape shape = this.shapeValueRepo.get(obj.getShapeKey());
	obj.setShape(shape);

	// Set the estimate types.
	List<EstimateType> estimateTypes = new ArrayList<EstimateType>();
	for (int estimateTypeID : obj.getEstimateType()) {
	    EstimateType type = EstimateType.of(estimateTypeID);
	    estimateTypes.add(type);
	}
	obj.setEstimateTypes(estimateTypes);

	// Do the commit.
	// If create.
	if (obj.getUuid() == null) {
	    obj.setUuid(UUID.randomUUID());
	    this.estimateValueRepo.set(obj);
	    return AlertBoxGenerator.SUCCESS.generateCreate(
		    RedisConstants.OBJECT_ESTIMATE, obj.getName());
	}

	// If update.
	this.estimateValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(
		RedisConstants.OBJECT_ESTIMATE, obj.getName());
    }

    @Override
    @Transactional
    public void delete(Collection<String> keys) {
	this.estimateValueRepo.delete(keys);
    }

    @Override
    @Transactional
    public void setIfAbsent(Estimate obj) {
	this.estimateValueRepo.setIfAbsent(obj);
    }

    @Override
    @Transactional
    public Estimate get(String key) {
	return this.estimateValueRepo.get(key);
    }

    @Override
    @Transactional
    public Set<String> keys(String pattern) {
	return this.estimateValueRepo.keys(pattern);
    }

    @Override
    @Transactional
    public Collection<Estimate> multiGet(Collection<String> keys) {
	return this.estimateValueRepo.multiGet(keys);
    }

    @Override
    @Transactional
    public String delete(String key) {
	this.estimateValueRepo.delete(key);
	return "";
    }

    @Override
    @Transactional
    public List<Estimate> list(Project proj) {
	String pattern = Estimate.constructPattern(proj);
	Set<String> keys = this.estimateValueRepo.keys(pattern);
	return this.estimateValueRepo.multiGet(keys);
    }

    /**
     * Compute for area and volume.
     * 
     * @param shape
     * @param estimate
     */
    private void setAreaVolume(Estimate estimate, Shape shape) {

	// Allowance.
	double allowance = estimate.getEstimationAllowance()
		.getEstimationAllowance();

	// Area.
	double area = getArea(estimate, shape);
	area += (area * allowance);

	// Volume.
	double volume = getVolume(estimate, shape);
	volume += (volume * allowance);

	shape.setArea(area);
	shape.setVolume(volume);
    }

    /**
     * Set the estimation allowance.
     * 
     * @param estimate
     */
    private void setEstimationAllowance(Estimate estimate) {
	// Set allowances.
	EstimationAllowance allowance = this.estimationAllowanceValueRepo
		.get(estimate.getEstimationAllowanceKey());
	estimate.setEstimationAllowance(allowance);
    }

    /**
     * Prepare inputs.
     * 
     * @param estimate
     * @param shape
     * @param chbList
     * @param blockLayingList
     */
    private void prepareInputs(Estimate estimate, Shape shape, List<CHB> chbList) {

	// Set allowances.
	setEstimationAllowance(estimate);

	// Compute for area and volume.
	setAreaVolume(estimate, shape);

	// Prepare estimation inputs.
	prepareMasonryCHBEstimationInputs(estimate, chbList);
    }

    /**
     * Estimate a project's quantity of materials.
     */
    @Override
    @Transactional
    public String computeQuantityEstimate(Estimate estimate) {

	// If we're computing block laying,
	// but didn't specify any block at all,
	// return fail.
	if (estimate.willComputeMasonryBlockLaying()
		&& estimate.getChbMeasurementKeys().length == 0) {
	    return AlertBoxGenerator.FAILED.setMessage(
		    MessageRegistry.ESTIMATE_MASONRY_BLM_NO_CHB_LIST)
		    .generateHTML();
	}

	// Shape to compute.
	Shape shape = estimate.getShape();

	// List of CHB's chosen.
	List<CHB> chbList = new ArrayList<CHB>();

	// Prepare inputs.
	prepareInputs(estimate, shape, chbList);

	// If we're estimating masonry CHB.
	if (estimate.willComputeMasonryCHB()) {
	    computeMasonryCHB(estimate, shape, chbList);
	}

	// If we're estimating masonry block laying.
	if (estimate.willComputeMasonryBlockLaying()) {
	    computeMasonryBlockLaying(estimate, shape, chbList);
	}

	// If computing concrete.
	if (estimate.willComputeConcrete()) {
	    computeConcrete(estimate, shape);
	}

	this.estimateValueRepo.set(estimate);

	return AlertBoxGenerator.SUCCESS.generateCompute(
		RedisConstants.OBJECT_ESTIMATE, estimate.getName());
    }

    /**
     * Estimate the block laying.
     * 
     * @param estimate
     * @param shape
     * @param chbList
     */
    private void computeMasonryBlockLaying(Estimate estimate, Shape shape,
	    List<CHB> chbList) {

	// Prepare the map,
	// and the BLM list.
	Map<CHB, MasonryBlockLayingEstimateResults> blockLayingResults = new HashMap<CHB, MasonryBlockLayingEstimateResults>();
	List<BlockLayingMixture> mixList = this.blockLayingMixtureService
		.list();

	// Loop through all mixtures.
	for (CHB chb : chbList) {

	    // If a mixture does not exist for this CHB,
	    // continue.
	    BlockLayingMixture mixture = chb.getBlockLayingMixture(mixList);
	    if (mixture == null) {
		continue;
	    }

	    // Get the inputs.
	    double area = shape.getArea();
	    double bags = mixture.getCementBags();
	    double sand = mixture.getSand();

	    // Compute.
	    double bagsNeeded = area * bags;
	    double sandNeeded = area * sand;

	    // Set the results.
	    MasonryBlockLayingEstimateResults layingResults = new MasonryBlockLayingEstimateResults(
		    chb, mixture, bagsNeeded, sandNeeded);
	    blockLayingResults.put(chb, layingResults);
	}

	// Set results.
	estimate.setResultMapMasonryBlockLaying(blockLayingResults);
    }

    /**
     * Estimate the number of components needed for this concrete.
     * 
     * @param estimate
     * @param shape
     */
    private void computeConcrete(Estimate estimate, Shape shape) {

	Map<ConcreteProportion, ConcreteEstimateResults> resultMapConcrete = new HashMap<ConcreteProportion, ConcreteEstimateResults>();

	// Loop through all concrete proportion keys.
	for (String proportionKey : estimate.getConcreteProportionKeys()) {

	    // Set the concrete proportion based on key.
	    ConcreteProportion proportion = this.concreteProportionValueRepo
		    .get(proportionKey);

	    // Now, compute the estimated concrete.
	    ConcreteEstimateResults concreteResults = getConcreteEstimateResults(
		    proportion, shape);

	    // Set the results.
	    // Set last computed.
	    // Save the object.
	    resultMapConcrete.put(proportion, concreteResults);
	    estimate.setLastComputed(new Date(System.currentTimeMillis()));
	}

	estimate.setResultMapConcrete(resultMapConcrete);
    }

    /**
     * Estimate number of CHB.
     * 
     * @param estimate
     * @param shape
     * @param chbList
     */
    private void computeMasonryCHB(Estimate estimate, Shape shape,
	    List<CHB> chbList) {

	// Result map.
	Map<CHB, MasonryCHBEstimateResults> resultMapMasonry = new HashMap<CHB, MasonryCHBEstimateResults>();

	// Loop through all inputs.
	// Get results.
	for (CHB chb : chbList) {
	    MasonryCHBEstimateResults masonryCHBEstimateResults = getMasonryEstimateResults(
		    shape, chb);
	    resultMapMasonry.put(chb, masonryCHBEstimateResults);
	}

	// Set the result map.
	estimate.setResultMapMasonryCHB(resultMapMasonry);
    }

    /**
     * Prepare the needed estimation inputs.
     * 
     * @param estimate
     * @param chbList
     * @param blockLayingList
     */
    private void prepareMasonryCHBEstimationInputs(Estimate estimate,
	    List<CHB> chbList) {

	// If we are computing CHB OR
	// block laying, go here.
	if (estimate.willComputeMasonryCHB()) {

	    // Loop through all inputs.
	    // Get CHB object.
	    for (String chbKey : estimate.getChbMeasurementKeys()) {
		CHB chb = this.chbValueRepo.get(chbKey);
		chbList.add(chb);
	    }
	}
    }

    /**
     * Get the volume of the shape.
     * 
     * @param estimate
     * @param shape
     * @return
     */
    private double getVolume(Estimate estimate, Shape shape) {

	// If any of the following is null, can't compute area.
	if (shape.getVolumeFormula() == null
		|| estimate.getVolumeFormulaInputs() == null
		|| shape.getVolumeVariableNames() == null
		|| estimate.getVolumeFormulaInputsUnits() == null) {
	    return 0.0;
	}

	// Replace all variables with the inputs given by the user.
	Expression mathExp = replaceVariablesWithInputs(
		shape.getVolumeFormulaWithoutDelimiters(),
		estimate.getVolumeFormulaInputs(), estimate.getShape()
			.getVolumeVariableNames(),
		estimate.getVolumeFormulaInputsUnits());

	return mathExp.eval().doubleValue();
    }

    /**
     * Get the area of the shape.
     * 
     * @param estimate
     * @param shape
     * @return
     */
    private double getArea(Estimate estimate, Shape shape) {

	// If any of the following is null, can't compute area.
	if (shape.getAreaFormula() == null
		|| estimate.getAreaFormulaInputs() == null
		|| shape.getAreaVariableNames() == null
		|| estimate.getAreaFormulaInputsUnits() == null) {
	    return 0.0;
	}

	// Compute for area.
	Expression mathExp = replaceVariablesWithInputs(
		shape.getAreaFormulaWithoutDelimiters(),
		estimate.getAreaFormulaInputs(), shape.getAreaVariableNames(),
		estimate.getAreaFormulaInputsUnits());
	BigDecimal area = mathExp.eval();

	return area.doubleValue();
    }

    /**
     * Get quantity estimation of masonry.
     * 
     * @param estimate
     * @param shape
     * @param chb
     * @return
     */
    private MasonryCHBEstimateResults getMasonryEstimateResults(Shape shape,
	    CHB chb) {

	double area = shape.getArea();

	// Get total CHBs.
	double totalCHB = area * chb.getPerSqM();

	// Results of the estimate.
	MasonryCHBEstimateResults masonryCHBEstimateResults = new MasonryCHBEstimateResults(
		chb, totalCHB);

	return masonryCHBEstimateResults;
    }

    /**
     * Compute the estimated concrete.
     * 
     * @param allowance
     * 
     * @param proportion
     * @param mathExp
     * @return
     */
    private ConcreteEstimateResults getConcreteEstimateResults(
	    ConcreteProportion proportion, Shape shape) {

	double volume = shape.getVolume();

	// Get the ingredients.
	// Now, compute the estimated concrete.
	double cement40kg = proportion.getPartCement40kg();
	double cement50kg = proportion.getPartCement50kg();
	double sand = proportion.getPartSand();
	double gravel = proportion.getPartGravel();

	// Compute.
	double estCement40kg = volume * cement40kg;
	double estCement50kg = volume * cement50kg;
	double estSand = volume * sand;
	double estGravel = volume * gravel;

	ConcreteEstimateResults concreteResults = new ConcreteEstimateResults(
		estCement40kg, estCement50kg, estSand, estGravel);

	return concreteResults;
    }

    /**
     * Replace all variables with the inputs given by the user.
     * 
     * @param formula
     * @param formulaInputs
     * @param variableNames
     * @param formulaInputUnits
     * @return
     */
    private Expression replaceVariablesWithInputs(String formula,
	    Map<String, String> formulaInputs, List<String> variableNames,
	    Map<String, CommonLengthUnit> formulaInputUnits) {
	Expression mathExp = new Expression(formula);

	// Loop through each variable and replace each variable.
	for (String variable : variableNames) {

	    // Get the value and the unit.
	    String rawValue = formulaInputs.get(variable);
	    BigDecimal value = (rawValue == null || !StringUtils
		    .isNumeric(rawValue)) ? new BigDecimal(0.0)
		    : new BigDecimal(rawValue);
	    CommonLengthUnit lengthUnit = formulaInputUnits.get(variable);

	    // If the unit is not meter,
	    // convert it.
	    if (lengthUnit != CommonLengthUnit.METER) {
		double meterConvert = lengthUnit.conversionToMeter();
		double convertedValue = meterConvert * value.doubleValue();
		value = new BigDecimal(convertedValue);
	    }

	    mathExp = mathExp.with(variable, value);
	}

	return mathExp;
    }

}
