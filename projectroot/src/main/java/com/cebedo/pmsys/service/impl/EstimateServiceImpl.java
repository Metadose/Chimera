package com.cebedo.pmsys.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.ConcreteEstimateResults;
import com.cebedo.pmsys.bean.MasonryEstimateResults;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.CHB;
import com.cebedo.pmsys.domain.ConcreteProportion;
import com.cebedo.pmsys.domain.Estimate;
import com.cebedo.pmsys.domain.Shape;
import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.enums.EstimateType;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.CHBValueRepo;
import com.cebedo.pmsys.repository.ConcreteProportionValueRepo;
import com.cebedo.pmsys.repository.EstimateValueRepo;
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

	// To be used later.
	UUID originalUUID = estimate.getUuid();

	// If we're estimating masonry.
	if (estimate.willComputeMasonry()) {

	    // Results of the estimate.
	    MasonryEstimateResults masonryEstimateResults = getMasonryEstimateResults(
		    estimate, shape);
	    estimate.setResultEstimateMasonry(masonryEstimateResults);

	    // Save the object.
	    this.estimateValueRepo.set(estimate);
	}

	// Evaluate the the math expression using a java library.
	// Not wolfram. Use wolfram only when complicated.
	// If computing concrete,
	// compute.
	if (estimate.willComputeConcrete()) {

	    // Clean the formula.
	    // Convert string to an expression object.
	    String volumeFormula = shape.getVolumeFormulaWithoutDelimiters();

	    // Replace all variables with the inputs given by the user.
	    Expression mathExp = replaceVariablesWithInputs(volumeFormula,
		    estimate.getVolumeFormulaInputs(), estimate.getShape()
			    .getVolumeVariableNames(),
		    estimate.getVolumeFormulaInputsUnits());

	    for (String proportionKey : estimate.getConcreteProportionKeys()) {

		// Set the concrete proportion based on key.
		ConcreteProportion proportion = this.concreteProportionValueRepo
			.get(proportionKey);
		setConcreteProportions(estimate, proportion);

		// Now, compute the estimated concrete.
		ConcreteEstimateResults concreteResults = getConcreteEstimateResults(
			proportion, mathExp);

		// Set the results.
		// Set last computed.
		// Save the object.
		estimate.setResultEstimateConcrete(concreteResults);
		estimate.setLastComputed(new Date(System.currentTimeMillis()));
		this.estimateValueRepo.set(estimate);

		// Change the UUID for the other concrete proportions.
		estimate.setUuid(UUID.randomUUID());
		estimate.setCopy(true);
	    }
	}

	// Set original UUID.
	estimate.setUuid(originalUUID);
	estimate.setCopy(false);

	return AlertBoxGenerator.SUCCESS.generateCompute(
		RedisConstants.OBJECT_ESTIMATE, estimate.getName());
    }

    /**
     * Get quantity estimation of masonry.
     * 
     * @param estimate
     * @param shape
     * @return
     */
    private MasonryEstimateResults getMasonryEstimateResults(Estimate estimate,
	    Shape shape) {
	// Get how many CHB per square meter.
	// Set CHB object.
	CHB chb = this.chbValueRepo.get(estimate.getChbMeasurementKey());

	// Compute for area.
	Expression mathExp = replaceVariablesWithInputs(
		shape.getAreaFormulaWithoutDelimiters(),
		estimate.getAreaFormulaInputs(), shape.getAreaVariableNames(),
		estimate.getAreaFormulaInputsUnits());
	BigDecimal area = mathExp.eval();

	// Get total CHBs.
	double totalCHB = area.doubleValue() * chb.getPerSqM();

	// Results of the estimate.
	MasonryEstimateResults masonryEstimateResults = new MasonryEstimateResults(
		chb, totalCHB);

	return masonryEstimateResults;
    }

    /**
     * Set the proportion of concrete components.
     * 
     * @param estimate
     * @param proportion
     */
    private void setConcreteProportions(Estimate estimate,
	    ConcreteProportion proportion) {
	estimate.setConcreteProportion(proportion);

	// Set new keys where the only entry is "proportionKey".
	// So that when we "Edit" this Estimate in JSP,
	// only "proportionKey" is checked in checkboxes.
	estimate.setConcreteProportionKeys((String[]) ArrayUtils.add(
		ArrayUtils.EMPTY_STRING_ARRAY, proportion.getKey()));
    }

    /**
     * Compute the estimated concrete.
     * 
     * @param proportion
     * @param mathExp
     * @return
     */
    private ConcreteEstimateResults getConcreteEstimateResults(
	    ConcreteProportion proportion, Expression mathExp) {
	// Get the ingredients.
	// Now, compute the estimated concrete.
	double cement40kg = proportion.getPartCement40kg();
	double cement50kg = proportion.getPartCement50kg();
	double sand = proportion.getPartSand();
	double gravel = proportion.getPartGravel();

	// Compute.
	BigDecimal volume = mathExp.eval();
	double estCement40kg = volume.doubleValue() * cement40kg;
	double estCement50kg = volume.doubleValue() * cement50kg;
	double estSand = volume.doubleValue() * sand;
	double estGravel = volume.doubleValue() * gravel;

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
