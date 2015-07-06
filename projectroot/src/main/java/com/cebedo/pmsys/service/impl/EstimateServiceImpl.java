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
import com.cebedo.pmsys.bean.MasonryCHBFootingEstimateResults;
import com.cebedo.pmsys.bean.MasonryPlasteringEstimateResults;
import com.cebedo.pmsys.constants.MessageRegistry;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.BlockLayingMixture;
import com.cebedo.pmsys.domain.CHB;
import com.cebedo.pmsys.domain.CHBFootingDimension;
import com.cebedo.pmsys.domain.CHBFootingMixture;
import com.cebedo.pmsys.domain.ConcreteProportion;
import com.cebedo.pmsys.domain.Estimate;
import com.cebedo.pmsys.domain.EstimationAllowance;
import com.cebedo.pmsys.domain.PlasterMixture;
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
    private void prepareInputs(Estimate estimate, Shape shape,
	    List<CHB> chbList, List<ConcreteProportion> proportions) {

	// Set allowances.
	setEstimationAllowance(estimate);

	// Compute for area and volume.
	setAreaVolume(estimate, shape);

	// Prepare estimation inputs.
	prepareMasonryCHBEstimationInputs(estimate, chbList);

	// Prepare the concrete proportions.
	prepareConcreteEstimationInputs(estimate, proportions);
    }

    /**
     * Prepare the concrete proportions.
     * 
     * @param estimate
     * @param proportions
     */
    private void prepareConcreteEstimationInputs(Estimate estimate,
	    List<ConcreteProportion> proportions) {
	for (String proportionKey : estimate.getConcreteProportionKeys()) {
	    ConcreteProportion prop = this.concreteProportionValueRepo
		    .get(proportionKey);
	    proportions.add(prop);
	}
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

	// List of chosen inputs.
	List<CHB> chbList = new ArrayList<CHB>();
	List<ConcreteProportion> proportions = new ArrayList<ConcreteProportion>();

	// TODO What if area is negative?

	// Prepare inputs.
	prepareInputs(estimate, shape, chbList, proportions);

	// If we're estimating masonry CHB.
	if (estimate.willComputeMasonryCHB()) {
	    computeMasonryCHB(estimate, shape, chbList);
	}

	// If we're estimating masonry block laying.
	if (estimate.willComputeMasonryBlockLaying()) {
	    computeMasonryBlockLaying(estimate, shape, chbList, proportions);
	}

	// If we're estimating masonry plastering.
	if (estimate.willComputeMasonryPlastering()) {
	    computeMasonryPlastering(estimate, shape, proportions);
	}

	// If we're estimating masonry CHB footing.
	if (estimate.willComputeMasonryCHBFooting()) {
	    computeMasonryCHBFooting(estimate, proportions);
	}

	// If computing concrete.
	if (estimate.willComputeConcrete()) {
	    computeConcrete(estimate, shape, proportions);
	}

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
    private void computeMasonryCHBFooting(Estimate estimate,
	    List<ConcreteProportion> proportions) {

	// Get the dimension key.
	// And the footing mixes.
	String dimensionKey = estimate.getChbFootingDimensionKey();
	CHBFootingDimension footingDimension = this.chbFootingDimensionService
		.get(dimensionKey);
	List<CHBFootingMixture> footingMixes = this.chbFootingMixtureService
		.list();

	// For every proportion,
	// there is a corresponding footing mix.
	Map<ConcreteProportion, MasonryCHBFootingEstimateResults> resultMapCHBFooting = new HashMap<ConcreteProportion, MasonryCHBFootingEstimateResults>();
	for (ConcreteProportion prop : proportions) {

	    // Get the footing mixture, based on the concrete proportion
	    // and dimension.
	    CHBFootingMixture chbFootingMix = getCHBFootingMixture(
		    footingMixes, dimensionKey, prop);
	    if (chbFootingMix.getUuid() == null) {
		continue;
	    }

	    double footingThickness = convertToMeter(
		    footingDimension.getThicknessUnit(),
		    footingDimension.getThickness());
	    double footingWidth = convertToMeter(
		    footingDimension.getWidthUnit(),
		    footingDimension.getWidth());

	    // TODO Optimize below code.
	    // getLength(estimate) is called somewhere else in this class.
	    double length = getLength(estimate);
	    double footingVolume = footingThickness * footingWidth * length;

	    // Estimations.
	    double cement = footingVolume * chbFootingMix.getCement();
	    double sand = footingVolume * chbFootingMix.getSand();
	    double gravel = footingVolume * chbFootingMix.getGravel();

	    // Put the results.
	    MasonryCHBFootingEstimateResults footingResults = new MasonryCHBFootingEstimateResults(
		    cement, gravel, sand, prop, footingDimension, chbFootingMix);
	    resultMapCHBFooting.put(prop, footingResults);
	}
	// Set the result map of the CHB footing estimate.
	estimate.setResultMapMasonryCHBFooting(resultMapCHBFooting);
    }

    /**
     * Get the CHB footing mixture.
     * 
     * @param footingMixes
     * @param dimensionKey
     * @param prop
     * @return
     */
    private CHBFootingMixture getCHBFootingMixture(
	    List<CHBFootingMixture> footingMixes, String dimensionKey,
	    ConcreteProportion prop) {
	for (CHBFootingMixture footingMix : footingMixes) {

	    String footingMixKey = footingMix.getFootingDimension().getKey();
	    // If the dimension keys are equal.
	    // If the concrete proportions are equal.
	    if (dimensionKey.equals(footingMixKey)
		    && prop.equals(footingMix.getConcreteProportion())) {
		return footingMix;
	    }
	}
	return new CHBFootingMixture();
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
    private void computeMasonryPlastering(Estimate estimate, Shape shape,
	    List<ConcreteProportion> proportions) {

	// If a shape has no sides,
	// then automatically, the number of sides to plaster will be 1.
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

	double volume = area * 0.016; // 0.016 is the standard thickness of
				      // plaster.

	// Get the list of plaster mixtures.
	List<PlasterMixture> plasterMixtures = this.plasterMixtureService
		.list();
	Map<ConcreteProportion, MasonryPlasteringEstimateResults> plasteringEstimateResults = new HashMap<ConcreteProportion, MasonryPlasteringEstimateResults>();

	for (ConcreteProportion proportion : proportions) {

	    // Find the appropriate plaster mixture
	    // given this proportion.
	    String propKey = proportion.getKey();
	    PlasterMixture plasterMixture = new PlasterMixture();
	    for (PlasterMixture plasterMix : plasterMixtures) {
		String plasterMixProportionKey = plasterMix
			.getConcreteProportion().getKey();
		if (propKey.equals(plasterMixProportionKey)) {
		    plasterMixture = plasterMix;
		    break;
		}
	    }

	    double bags40kg = volume * plasterMixture.getCement40kg();
	    double bags50kg = volume * plasterMixture.getCement50kg();
	    double sand = volume * plasterMixture.getSand();

	    // Set the results, concrete proportion, plaster mixture,
	    // is back to back, plaster top side.
	    MasonryPlasteringEstimateResults plasteringResults = new MasonryPlasteringEstimateResults(
		    bags40kg, bags50kg, sand, proportion, plasterMixture,
		    plasterBackToBack, estimate.isPlasterTopSide());
	    plasteringEstimateResults.put(proportion, plasteringResults);
	}

	// Set all results.
	estimate.setResultMapMasonryPlastering(plasteringEstimateResults);
    }

    /**
     * Estimate the block laying.
     * 
     * @param estimate
     * @param shape
     * @param chbList
     */
    private void computeMasonryBlockLaying(Estimate estimate, Shape shape,
	    List<CHB> chbList, List<ConcreteProportion> concreteProportions) {

	// Prepare the map,
	// and the BLM list.
	Map<CHB, List<MasonryBlockLayingEstimateResults>> blockLayingResults = new HashMap<CHB, List<MasonryBlockLayingEstimateResults>>();
	List<BlockLayingMixture> mixList = this.blockLayingMixtureService
		.list();

	// Loop through all mixtures.
	for (CHB chb : chbList) {

	    // For every CHB,
	    // you have a list of results depending on your list of proportions.
	    List<MasonryBlockLayingEstimateResults> results = new ArrayList<MasonryBlockLayingEstimateResults>();

	    for (ConcreteProportion proportion : concreteProportions) {

		// Given a CHB and proportion,
		// get the block laying mix.
		BlockLayingMixture mixture = chb.getBlockLayingMixture(mixList,
			proportion);

		// If a mixture does not exist for this CHB,
		// continue.
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
			chb, mixture, bagsNeeded, sandNeeded, proportion);
		results.add(layingResults);
	    }

	    // Set to map of CHB,
	    // to list of proportion results.
	    blockLayingResults.put(chb, results);
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
    private void computeConcrete(Estimate estimate, Shape shape,
	    List<ConcreteProportion> concreteProportions) {

	Map<ConcreteProportion, ConcreteEstimateResults> resultMapConcrete = new HashMap<ConcreteProportion, ConcreteEstimateResults>();

	// Loop through all concrete proportion keys.
	for (ConcreteProportion proportion : concreteProportions) {

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
