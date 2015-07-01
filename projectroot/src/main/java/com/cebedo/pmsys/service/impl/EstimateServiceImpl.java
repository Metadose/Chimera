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
import com.cebedo.pmsys.bean.MasonryEstimateResults;
import com.cebedo.pmsys.constants.RedisConstants;
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

    @Override
    @Transactional
    public String computeQuantityEstimate(Estimate estimate) {

	// Shape to compute.
	Shape shape = estimate.getShape();

	// Set allowances.
	EstimationAllowance allowance = this.estimationAllowanceValueRepo
		.get(estimate.getEstimationAllowanceKey());
	estimate.setEstimationAllowance(allowance);

	// Compute for area and volume.
	double area = getArea(estimate, shape);
	double volume = getVolume(estimate, shape);
	shape.setArea(area);
	shape.setVolume(volume);

	// If we're estimating masonry.
	if (estimate.willComputeMasonry()) {

	    // Result map.
	    Map<CHB, MasonryEstimateResults> resultMapMasonry = new HashMap<CHB, MasonryEstimateResults>();

	    // Loop through all inputs.
	    for (String chbKey : estimate.getChbMeasurementKeys()) {

		// Get CHB object.
		// Set results of the estimate, given the CHB.
		CHB chb = this.chbValueRepo.get(chbKey);

		// Get results.
		MasonryEstimateResults masonryEstimateResults = getMasonryEstimateResults(
			estimate, shape, chb);
		resultMapMasonry.put(chb, masonryEstimateResults);
	    }

	    estimate.setResultMapMasonry(resultMapMasonry);
	}

	// Evaluate the the math expression using a java library.
	// Not wolfram. Use wolfram only when complicated.
	// If computing concrete,
	// compute.
	if (estimate.willComputeConcrete()) {

	    Map<ConcreteProportion, ConcreteEstimateResults> resultMapConcrete = new HashMap<ConcreteProportion, ConcreteEstimateResults>();

	    // Loop through all concrete proportion keys.
	    for (String proportionKey : estimate.getConcreteProportionKeys()) {

		// Set the concrete proportion based on key.
		ConcreteProportion proportion = this.concreteProportionValueRepo
			.get(proportionKey);

		// Now, compute the estimated concrete.
		ConcreteEstimateResults concreteResults = getConcreteEstimateResults(
			allowance, proportion, shape);

		// Set the results.
		// Set last computed.
		// Save the object.
		resultMapConcrete.put(proportion, concreteResults);
		estimate.setLastComputed(new Date(System.currentTimeMillis()));
	    }

	    estimate.setResultMapConcrete(resultMapConcrete);

	} // End of will compute concrete.

	this.estimateValueRepo.set(estimate);

	return AlertBoxGenerator.SUCCESS.generateCompute(
		RedisConstants.OBJECT_ESTIMATE, estimate.getName());
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
    private MasonryEstimateResults getMasonryEstimateResults(Estimate estimate,
	    Shape shape, CHB chb) {

	// Get the area.
	// Add allowance.
	double allowance = estimate.getEstimationAllowance()
		.getEstimationAllowance();
	double area = shape.getArea();
	area += (area * allowance);

	// Get total CHBs.
	double totalCHB = area * chb.getPerSqM();

	// Results of the estimate.
	MasonryEstimateResults masonryEstimateResults = new MasonryEstimateResults(
		chb, totalCHB);

	return masonryEstimateResults;
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
	    EstimationAllowance allowance, ConcreteProportion proportion,
	    Shape shape) {

	double volume = shape.getVolume();
	volume += (volume * allowance.getEstimationAllowance());

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
