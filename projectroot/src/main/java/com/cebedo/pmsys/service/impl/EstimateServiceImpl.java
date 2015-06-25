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
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.ConcreteProportion;
import com.cebedo.pmsys.domain.Estimate;
import com.cebedo.pmsys.domain.Shape;
import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.enums.EstimateType;
import com.cebedo.pmsys.model.Project;
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

	// To be used later.
	UUID originalUUID = estimate.getUuid();

	// Evaluate the the math expression using a java library.
	// Not wolfram. Use wolfram only when complicated.
	// If computing concrete,
	// compute.
	if (estimate.willComputeConcrete()) {

	    // Get the shape of the slab.
	    Shape shape = estimate.getShape();

	    // Clean the formula.
	    // Convert string to an expression object.
	    String formula = shape.getFormula();
	    formula = StringUtils
		    .remove(formula, Shape.DELIMITER_OPEN_VARIABLE);
	    formula = StringUtils.remove(formula,
		    Shape.DELIMITER_CLOSE_VARIABLE);
	    Expression mathExp = new Expression(formula);

	    // Get the formula inputs.
	    // And the units.
	    Map<String, String> inputs = estimate.getFormulaInputs();
	    Map<String, CommonLengthUnit> inputUnits = estimate
		    .getFormulaInputsUnits();

	    // Loop through each variable and replace each variable.
	    for (String variable : shape.getVariableNames()) {

		// Get the value and the unit.
		String rawValue = inputs.get(variable);
		String value = (rawValue == null || !StringUtils
			.isNumeric(rawValue)) ? "0" : rawValue;
		CommonLengthUnit lengthUnit = inputUnits.get(variable);

		// If the unit is not meter,
		// convert it.
		if (lengthUnit != CommonLengthUnit.METER) {
		    double meterConvert = lengthUnit.conversionToMeter();
		    double valueDbl = Double.parseDouble(value);
		    double convertedValue = meterConvert * valueDbl;
		    value = convertedValue + "";
		}

		mathExp = mathExp.with(variable, value);
	    }

	    for (String proportionKey : estimate.getConcreteProportionKeys()) {

		// Set the concrete proportion based on key.
		ConcreteProportion proportion = this.concreteProportionValueRepo
			.get(proportionKey);
		estimate.setConcreteProportion(proportion);

		// Set new keys where the only entry is "proportionKey".
		// So that when we "Edit" this Estimate in JSP,
		// only "proportionKey" is checked in checkboxes.
		estimate.setConcreteProportionKeys((String[]) ArrayUtils.add(
			ArrayUtils.EMPTY_STRING_ARRAY, proportionKey));

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

		// Save the results.
		ConcreteEstimateResults concreteResults = new ConcreteEstimateResults(
			estCement40kg, estCement50kg, estSand, estGravel);
		estimate.setResultEstimateConcrete(concreteResults);

		// Set last computed.
		estimate.setLastComputed(new Date(System.currentTimeMillis()));

		// Save the object.
		this.estimateValueRepo.set(estimate);

		// Change the UUID for the other concrete proportions.
		estimate.setUuid(UUID.randomUUID());
	    }
	}

	estimate.setUuid(originalUUID);

	return AlertBoxGenerator.SUCCESS.generateCompute(
		RedisConstants.OBJECT_ESTIMATE, estimate.getName());
    }
    /**
     * Set old totals of concrete estimation.
     * 
     * @param projAux
     * @param estCement40kg
     * @param estCement50kg
     * @param estSand
     * @param estGravel
     * @return
     */
    // private ProjectAux setOldConcreteComponentValues(ProjectAux projAux,
    // double estCement40kg, double estCement50kg, double estSand,
    // double estGravel) {
    // double newTotalCement40 = projAux.getTotalCement40kg() - estCement40kg;
    // double newTotalCement50 = projAux.getTotalCement50kg() - estCement50kg;
    // double newTotalSand = projAux.getTotalSand() - estSand;
    // double newTotalGravel = projAux.getTotalGravel() - estGravel;
    //
    // projAux.setTotalCement40kg(newTotalCement40);
    // projAux.setTotalCement50kg(newTotalCement50);
    // projAux.setTotalSand(newTotalSand);
    // projAux.setTotalGravel(newTotalGravel);
    //
    // return projAux;
    // }

    /**
     * Set new totals of concrete estimation.
     * 
     * @param projAux
     * @param estCement40kg
     * @param estCement50kg
     * @param estSand
     * @param estGravel
     * @return
     */
    // private ProjectAux setNewConcreteComponentValues(ProjectAux projAux,
    // double estCement40kg, double estCement50kg, double estSand,
    // double estGravel) {
    // double newTotalCement40 = projAux.getTotalCement40kg() + estCement40kg;
    // double newTotalCement50 = projAux.getTotalCement50kg() + estCement50kg;
    // double newTotalSand = projAux.getTotalSand() + estSand;
    // double newTotalGravel = projAux.getTotalGravel() + estGravel;
    //
    // projAux.setTotalCement40kg(newTotalCement40);
    // projAux.setTotalCement50kg(newTotalCement50);
    // projAux.setTotalSand(newTotalSand);
    // projAux.setTotalGravel(newTotalGravel);
    //
    // return projAux;
    // }

}
