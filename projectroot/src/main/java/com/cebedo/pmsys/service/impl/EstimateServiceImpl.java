package com.cebedo.pmsys.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cebedo.pmsys.bean.ConcreteEstimateResults;
import com.cebedo.pmsys.bean.MasonryCHBEstimateResults;
import com.cebedo.pmsys.bean.MasonryCHBFootingEstimateResults;
import com.cebedo.pmsys.bean.MasonryCHBLayingEstimateResults;
import com.cebedo.pmsys.bean.MasonryPlasteringEstimateResults;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.Estimate;
import com.cebedo.pmsys.domain.EstimationAllowance;
import com.cebedo.pmsys.domain.Shape;
import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.enums.EstimateType;
import com.cebedo.pmsys.enums.TableCHBDimensions;
import com.cebedo.pmsys.enums.TableCHBFootingDimensions;
import com.cebedo.pmsys.enums.TableCHBFootingMixture;
import com.cebedo.pmsys.enums.TableCHBLayingMixture;
import com.cebedo.pmsys.enums.TableConcreteProportion;
import com.cebedo.pmsys.enums.TablePlasterMixture;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.CHBHorizontalReinforcementValueRepo;
import com.cebedo.pmsys.repository.CHBValueRepo;
import com.cebedo.pmsys.repository.CHBVerticalReinforcementValueRepo;
import com.cebedo.pmsys.repository.ConcreteProportionValueRepo;
import com.cebedo.pmsys.repository.EstimateValueRepo;
import com.cebedo.pmsys.repository.EstimationAllowanceValueRepo;
import com.cebedo.pmsys.repository.ShapeValueRepo;
import com.cebedo.pmsys.service.BlockLayingMixtureService;
import com.cebedo.pmsys.service.CHBFootingDimensionService;
import com.cebedo.pmsys.service.CHBFootingMixtureService;
import com.cebedo.pmsys.service.EstimateService;
import com.cebedo.pmsys.service.PlasterMixtureService;
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
    private PlasterMixtureService plasterMixtureService;
    private CHBFootingMixtureService chbFootingMixtureService;
    private CHBFootingDimensionService chbFootingDimensionService;
    private CHBVerticalReinforcementValueRepo chbVerticalReinforcementValueRepo;
    private CHBHorizontalReinforcementValueRepo chbHorizontalReinforcementValueRepo;

    public void setChbVerticalReinforcementValueRepo(
	    CHBVerticalReinforcementValueRepo chbVerticalReinforcementValueRepo) {
	this.chbVerticalReinforcementValueRepo = chbVerticalReinforcementValueRepo;
    }

    public void setChbHorizontalReinforcementValueRepo(
	    CHBHorizontalReinforcementValueRepo chbHorizontalReinforcementValueRepo) {
	this.chbHorizontalReinforcementValueRepo = chbHorizontalReinforcementValueRepo;
    }

    public void setChbFootingDimensionService(
	    CHBFootingDimensionService chbFootingDimensionService) {
	this.chbFootingDimensionService = chbFootingDimensionService;
    }

    public void setChbFootingMixtureService(
	    CHBFootingMixtureService chbFootingMixtureService) {
	this.chbFootingMixtureService = chbFootingMixtureService;
    }

    public void setPlasterMixtureService(
	    PlasterMixtureService plasterMixtureService) {
	this.plasterMixtureService = plasterMixtureService;
    }

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
     * @param proportions
     * @param blockLayingList
     */
    private void prepareInputs(Estimate estimate, Shape shape) {

	// Set allowances.
	setEstimationAllowance(estimate);

	// Compute for area and volume.
	setAreaVolume(estimate, shape);
    }

    /**
     * Estimate a project's quantity of materials.
     */
    @Override
    @Transactional
    public String computeQuantityEstimate(Estimate estimate) {

	// TODO Handle incorrect requests.
	// If we're computing block laying,
	// but didn't specify any block at all,
	// return fail.
	// if (estimate.willComputeMasonryBlockLaying()
	// && estimate.getChbMeasurementKeys().length == 0) {
	// return AlertBoxGenerator.FAILED.setMessage(
	// MessageRegistry.ESTIMATE_MASONRY_BLM_NO_CHB_LIST)
	// .generateHTML();
	// }

	// Shape to compute.
	Shape shape = estimate.getShape();

	// TODO What if area is negative?

	// Prepare inputs.
	prepareInputs(estimate, shape);

	// If we're estimating masonry CHB.
	if (estimate.willComputeMasonryCHB()) {
	    estimateCHBTotal(estimate, shape);
	}

	// If we're estimating masonry block laying.
	if (estimate.willComputeMasonryBlockLaying()) {
	    estimateCHBLaying(estimate, shape);
	}

	// If we're estimating masonry plastering.
	if (estimate.willComputeMasonryPlastering()) {
	    estimateMasonryPlastering(estimate, shape);
	}

	// If we're estimating masonry CHB footing.
	if (estimate.willComputeMasonryCHBFooting()) {
	    estimateMasonryCHBFooting(estimate);
	}

	// If computing concrete.
	if (estimate.willComputeConcrete()) {
	    estimateConcrete(estimate, shape);
	}

	// TODO If computing metal reinforcement in CHB.
	// if (estimate.willComputeMRCHB()) {
	// CHBVerticalReinforcement vertReinforcement =
	// this.chbVerticalReinforcementValueRepo
	// .get(estimate.getChbVerticalReinforcementKey());
	//
	// CHBHorizontalReinforcement horizReinforcement =
	// this.chbHorizontalReinforcementValueRepo
	// .get(estimate.getChbHorizontalReinforcementKey());
	//
	// vertReinforcement.getBarLengthPerSqm();
	// vertReinforcement.getBarsSpacing().spacing();
	//
	// horizReinforcement.getBarLengthPerSqm();
	// horizReinforcement.getBarsLayer().layer();
	// }

	estimate.setLastComputed(new Date(System.currentTimeMillis()));
	this.estimateValueRepo.set(estimate);

	return AlertBoxGenerator.SUCCESS.generateCompute(
		RedisConstants.OBJECT_ESTIMATE, estimate.getName());
    }

    /**
     * Estimate the CHB footings.
     * 
     * @param estimate
     * @param proportions
     */
    private void estimateMasonryCHBFooting(Estimate estimate) {

	// Get the dimension key.
	// And the footing mixes.
	TableCHBFootingDimensions chbFooting = estimate
		.getChbFootingDimensions();
	String mixClass = estimate.getEstimationClass().getConcreteProportion()
		.getMixClass();

	// Get the footing mixture given the mix class and footing dimensions.
	TableCHBFootingMixture footingMixture = getCHBFootingMixture(
		chbFooting, mixClass);

	// Get thickness and width.
	// TODO Do conversion for other calculations also.
	double footingThickness = convertToMeter(chbFooting.getThickessUnit(),
		chbFooting.getThickness());
	double footingWidth = convertToMeter(chbFooting.getWidthUnit(),
		chbFooting.getWidth());

	// TODO Optimize below code.
	// getLength(estimate) is called somewhere else in this class.
	double length = getLength(estimate);
	double footingVolume = footingThickness * footingWidth * length;

	// Estimations.
	double cement = footingVolume * footingMixture.getPartCement50kg();
	double sand = footingVolume * footingMixture.getPartSand();
	double gravel = footingVolume * footingMixture.getPartGravel();

	// Put the results.
	MasonryCHBFootingEstimateResults footingResults = new MasonryCHBFootingEstimateResults(
		cement, gravel, sand, chbFooting, footingMixture);

	// Set the result map of the CHB footing estimate.
	estimate.setResultCHBFootingEstimate(footingResults);
    }

    /**
     * Get the CHB footing mixture.
     * 
     * @param footingMixes
     * @param dimensionKey
     * @param prop
     * @return
     */
    private TableCHBFootingMixture getCHBFootingMixture(
	    TableCHBFootingDimensions chbFooting, String mixClass) {

	for (TableCHBFootingMixture footingMix : TableCHBFootingMixture.class
		.getEnumConstants()) {

	    TableCHBFootingDimensions footing = footingMix
		    .getFootingDimensions();
	    String footingClass = footingMix.getMixClass();

	    if (chbFooting == footing && footingClass.equals(mixClass)) {
		return footingMix;
	    }
	}
	return TableCHBFootingMixture.CLASS_A_15_60;
    }

    /**
     * Get the length (longest side) from the user input.
     * 
     * @param estimate
     * @return
     */
    private double getLength(Estimate estimate) {
	double length = 0.0;
	for (String value : estimate.getAreaFormulaInputs().values()) {
	    double formulaVal = Double.valueOf(value);
	    if (formulaVal > length) {
		length = formulaVal;
	    }
	}
	return length;
    }

    /**
     * Don't include the area below ground when plastering.
     * 
     * @param estimate
     * @param length
     * @param area
     */
    private double minusAreaBelowGround(Estimate estimate, double length,
	    double area) {

	// If the unit is not meter,
	// convert it.
	double foundationHeight = estimate.getChbFoundationHeight();
	CommonLengthUnit lengthUnit = estimate.getChbFoundationUnit();
	if (lengthUnit != CommonLengthUnit.METER) {
	    foundationHeight = convertToMeter(lengthUnit, foundationHeight);
	}

	double areaBelowGround = length * foundationHeight;
	area -= areaBelowGround;
	return area;
    }

    /**
     * Add the area of the top side.
     * 
     * @param estimate
     * @param shape
     * @param shapeArea
     * @param length
     * @param area
     */
    private double addAreaTopSide(Estimate estimate, Shape shape,
	    double shapeArea, double length, double area) {
	boolean plasterTopSide = estimate.isPlasterTopSide();
	if (plasterTopSide) {

	    // Get the thickness.
	    double thickness = shape.getVolume() / shapeArea;

	    // Get the area and add to overall area.
	    double topSideArea = length * thickness;
	    area += topSideArea;
	}
	return area;
    }

    /**
     * Estimate amount of plastering.
     * 
     * @param estimate
     * @param shape
     * @param proportions
     */
    private void estimateMasonryPlastering(Estimate estimate, Shape shape) {

	// If a shape has no sides,
	// then automatically, the number of sides to plaster will be 1.
	// TODO Simplify all below. Minimize decision making.
	if (!shape.isWithSides()) {
	    estimate.setPlasterBackToBack(false);
	}

	// If we're plastering back to back,
	// multiply the area by 2.
	// Else, plaster only 1 side.
	boolean plasterBackToBack = estimate.isPlasterBackToBack();
	double shapeArea = shape.getArea();
	double area = plasterBackToBack ? shapeArea * 2 : shape.getArea();

	// Get the length "longest side of the shape".
	double length = getLength(estimate);

	// Consider the height below ground.
	// Get the height of foundation (height of wall below the
	// ground) and don't include that to the area to be plastered.
	area = minusAreaBelowGround(estimate, length, area);

	// If we're plastering the top side,
	// get the thickness area then plaster it.
	area = addAreaTopSide(estimate, shape, shapeArea, length, area);

	double volume = area * TablePlasterMixture.STANDARD_PLASTER_THICKNESS;

	// Find the appropriate plaster mixture
	// given this proportion.
	TableConcreteProportion proportion = estimate.getEstimationClass()
		.getConcreteProportion();
	String proportionMixClass = proportion.getMixClass();

	// Find the plaster mix.
	TablePlasterMixture plasterMixture = TablePlasterMixture.CLASS_A;
	for (TablePlasterMixture plasterMix : TablePlasterMixture.class
		.getEnumConstants()) {

	    String plasterMixClass = plasterMix.getMixClass();
	    if (plasterMixClass.equals(proportionMixClass)) {
		plasterMixture = plasterMix;
		break;
	    }
	}

	double bags40kg = volume * plasterMixture.getPartCement40kg();
	double bags50kg = volume * plasterMixture.getPartCement50kg();
	double sand = volume * plasterMixture.getPartSand();

	// Set the results, concrete proportion, plaster mixture,
	// is back to back, plaster top side.
	MasonryPlasteringEstimateResults plasteringResults = new MasonryPlasteringEstimateResults(
		bags40kg, bags50kg, sand, proportion, plasterMixture,
		plasterBackToBack, estimate.isPlasterTopSide());
	estimate.setResultPlasteringEstimate(plasteringResults);
    }

    /**
     * Estimate the block laying.
     * 
     * @param estimate
     * @param shape
     * @param chbList
     */
    private void estimateCHBLaying(Estimate estimate, Shape shape) {

	// Prepare needed arguments.
	TableCHBDimensions chb = estimate.getChbDimensions();
	TableConcreteProportion proportion = estimate.getEstimationClass()
		.getConcreteProportion();
	TableCHBLayingMixture chbLayingMix = getCHBLayingMixture(chb,
		proportion);

	// Get the inputs.
	double area = shape.getArea();
	double bags = chbLayingMix.getPartCement50kgBag(); // 50kg bags.
	double sand = chbLayingMix.getPartSand(); // Cubic meters.

	// Compute.
	double bagsNeeded = area * bags;
	double sandNeeded = area * sand;

	// Set the results.
	MasonryCHBLayingEstimateResults layingResults = new MasonryCHBLayingEstimateResults(
		chb, chbLayingMix, proportion, bagsNeeded, sandNeeded);
	estimate.setResultCHBLayingEstimate(layingResults);
    }

    /**
     * Get the CHB laying mixture.
     * 
     * @param chb
     * @param proportion
     * @return
     */
    private TableCHBLayingMixture getCHBLayingMixture(TableCHBDimensions chb,
	    TableConcreteProportion proportion) {

	String proportionMixClass = proportion.getMixClass();

	// Loop through all block laying mixtures.
	for (TableCHBLayingMixture mix : TableCHBLayingMixture.class
		.getEnumConstants()) {

	    String layingMixClass = mix.getMixClass();
	    TableCHBDimensions chbFromLaying = mix.getChb();

	    // Get correct CHB,
	    // and correct concrete proportion.
	    if (layingMixClass.equals(proportionMixClass)
		    && chbFromLaying == chb) {
		return mix;
	    }
	}
	return TableCHBLayingMixture.CLASS_A_20_20_40;
    }

    /**
     * Estimate the number of components needed for this concrete.
     * 
     * @param estimate
     * @param shape
     */
    private void estimateConcrete(Estimate estimate, Shape shape) {

	// Now, compute the estimated concrete.
	ConcreteEstimateResults concreteResults = getConcreteEstimateResults(
		estimate.getEstimationClass().getConcreteProportion(), shape);

	// Set the results.
	estimate.setResultConcreteEstimate(concreteResults);
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
     * 
     * @param estimate
     * @param shape
     * @param chb
     * @return
     */
    private void estimateCHBTotal(Estimate estimate, Shape shape) {

	double area = shape.getArea();

	// Get total CHBs.
	double totalCHB = area * TableCHBDimensions.STANDARD_CHB_PER_SQ_M;

	// Results of the estimate.
	MasonryCHBEstimateResults masonryCHBEstimateResults = new MasonryCHBEstimateResults(
		estimate.getChbDimensions(), totalCHB);
	estimate.setResultCHBEstimate(masonryCHBEstimateResults);
    }

    /**
     * Compute the estimated concrete.
     * 
     * @param allowance
     * 
     * @param tableConcreteProportion
     * @param mathExp
     * @return
     */
    private ConcreteEstimateResults getConcreteEstimateResults(
	    TableConcreteProportion tableConcreteProportion, Shape shape) {

	double volume = shape.getVolume();

	// Get the ingredients.
	// Now, compute the estimated concrete.
	double cement40kg = tableConcreteProportion.getPartCement40kg();
	double cement50kg = tableConcreteProportion.getPartCement50kg();
	double sand = tableConcreteProportion.getPartSand();
	double gravel = tableConcreteProportion.getPartGravel();

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
		value = convertToMeter(lengthUnit, value);
	    }

	    mathExp = mathExp.with(variable, value);
	}

	return mathExp;
    }

    /**
     * Convert the value to meter.
     * 
     * @param lengthUnit
     * @param value
     * @return
     */
    private BigDecimal convertToMeter(CommonLengthUnit lengthUnit,
	    BigDecimal value) {
	double meterConvert = lengthUnit.conversionToMeter();
	double convertedValue = meterConvert * value.doubleValue();
	value = new BigDecimal(convertedValue);
	return value;
    }

    private double convertToMeter(CommonLengthUnit lengthUnit, double value) {
	return convertToMeter(lengthUnit, new BigDecimal(value)).doubleValue();
    }

}
