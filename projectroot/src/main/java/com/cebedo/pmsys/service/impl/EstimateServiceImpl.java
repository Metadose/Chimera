package com.cebedo.pmsys.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.bean.ConcreteEstimateResults;
import com.cebedo.pmsys.bean.EstimateBean;
import com.cebedo.pmsys.bean.EstimationInputBean;
import com.cebedo.pmsys.bean.EstimationOutputRowBean;
import com.cebedo.pmsys.bean.MasonryCHBEstimateResults;
import com.cebedo.pmsys.bean.MasonryCHBFootingEstimateResults;
import com.cebedo.pmsys.bean.MasonryCHBLayingEstimateResults;
import com.cebedo.pmsys.bean.MasonryPlasteringEstimateResults;
import com.cebedo.pmsys.bean.ShapeBean;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.EstimationOutput;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.CommonLengthUnit;
import com.cebedo.pmsys.enums.EstimateType;
import com.cebedo.pmsys.enums.TableCHBDimensions;
import com.cebedo.pmsys.enums.TableCHBFootingDimensions;
import com.cebedo.pmsys.enums.TableCHBFootingMixture;
import com.cebedo.pmsys.enums.TableCHBLayingMixture;
import com.cebedo.pmsys.enums.TableConcreteProportion;
import com.cebedo.pmsys.enums.TablePlasterMixture;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.ExcelHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.EstimationOutputValueRepo;
import com.cebedo.pmsys.service.EstimateService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.google.gson.Gson;

@Service
public class EstimateServiceImpl implements EstimateService {

    // Details.
    private static final int EXCEL_DETAILS_NAME = 1;
    private static final int EXCEL_DETAILS_AREA = 2;
    private static final int EXCEL_DETAILS_VOLUME = 3;

    // Estimate and Remarks.
    private static final int EXCEL_ESTIMATE_MASONRY_CONCRETE = 4;
    private static final int EXCEL_ESTIMATE_MASONRY_CHB = 5;
    private static final int EXCEL_ESTIMATE_MASONRY_CHB_LAYING = 6;
    private static final int EXCEL_ESTIMATE_MASONRY_PLASTERING = 7;
    private static final int EXCEL_ESTIMATE_MASONRY_FOUNDATION_HEIGHT = 8;
    private static final int EXCEL_ESTIMATE_MASONRY_CHB_FOOTING = 9;
    private static final int EXCEL_ESTIMATE_MASONRY_FOOTING_LENGTH = 10;
    private static final int EXCEL_ESTIMATE_MR_CHB = 11;
    private static final int EXCEL_DETAILS_REMARKS = 12;

    // Cost
    private static final int EXCEL_COST_CHB = 13;
    private static final int EXCEL_COST_CEMENT_40KG = 14;
    private static final int EXCEL_COST_CEMENT_50KG = 15;
    private static final int EXCEL_COST_SAND = 16;
    private static final int EXCEL_COST_GRAVEL = 17;

    private MessageHelper messageHelper = new MessageHelper();
    private AuthHelper authHelper = new AuthHelper();
    private ExcelHelper excelHelper = new ExcelHelper();

    private EstimationOutputValueRepo estimationOutputValueRepo;

    public void setEstimationOutputValueRepo(EstimationOutputValueRepo estimationOutputValueRepo) {
	this.estimationOutputValueRepo = estimationOutputValueRepo;
    }

    @Override
    @Transactional
    public String estimate(EstimationInputBean estimateInput) {

	// Security check.
	Project proj = estimateInput.getProject();
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_ESTIMATE, Project.OBJECT_NAME, proj.getId(),
		EstimationInputBean.class.getName());

	// Do the commit.
	// If create.
	if (estimateInput.getEstimationFile() != null) {

	    // New object.
	    EstimationOutput estimationOutput = new EstimationOutput(estimateInput);

	    // Convert the excel file to objects.
	    List<EstimateBean> estimateBeans = convertExcelToEstimates(
		    estimateInput.getEstimationFile(), estimateInput.getProject());

	    // Process each object.
	    List<EstimationOutputRowBean> rowListForJSON = new ArrayList<EstimationOutputRowBean>();
	    for (EstimateBean estimateBean : estimateBeans) {

		// Set allowance.
		estimateBean.setEstimationAllowance(estimateInput.getEstimationAllowance());

		// Compute.
		computeQuantityEstimate(estimateBean);
		computeCostEstimate(estimateBean);

		// Add to list of beans to be converted to JSON later.
		rowListForJSON.add(new EstimationOutputRowBean(estimateBean));
	    }

	    // Set the list.
	    String rowListJson = new Gson().toJson(rowListForJSON, ArrayList.class);
	    estimationOutput.setResults(estimateInput, estimateBeans, rowListJson);

	    // Save the object.
	    estimationOutput.setUuid(UUID.randomUUID());
	    this.estimationOutputValueRepo.set(estimationOutput);

	    return AlertBoxGenerator.SUCCESS.generateCreate(RedisConstants.OBJECT_ESTIMATE, "TODO");
	}

	// If file is null, return an error.
	return AlertBoxGenerator.ERROR;
    }

    /**
     * Estimate the total cost given the materials and results.
     * 
     * @param estimateBean
     */
    private void computeCostEstimate(EstimateBean estimateBean) {

	ConcreteEstimateResults concrete = estimateBean.getResultConcreteEstimate();
	MasonryCHBEstimateResults chb = estimateBean.getResultCHBEstimate();
	MasonryCHBLayingEstimateResults chbLaying = estimateBean.getResultCHBLayingEstimate();
	MasonryPlasteringEstimateResults plaster = estimateBean.getResultPlasteringEstimate();
	MasonryCHBFootingEstimateResults footing = estimateBean.getResultCHBFootingEstimate();

	// Concrete.
	double costCement40kg = 0, costCement50kg = 0, costSand = 0, costGravel = 0, costCHB = 0;
	costCement40kg += concrete.getCostCement40kg();
	costCement50kg += concrete.getCostCement50kg();
	costSand += concrete.getCostSand();
	costGravel += concrete.getCostGravel();

	// CHB.
	costCHB += chb.getCostCHB();

	// CHB Laying.
	costCement40kg += chbLaying.getCostCement40kg();
	costCement50kg += chbLaying.getCostCement50kg();
	costSand += chbLaying.getCostSand();

	// Plaster.
	costCement40kg += plaster.getCostCement40kg();
	costCement50kg += plaster.getCostCement50kg();
	costSand += plaster.getCostSand();

	// Footing.
	costCement40kg += footing.getCostCement40kg();
	costCement50kg += footing.getCostCement50kg();
	costSand += footing.getCostSand();
	costGravel += footing.getCostGravel();

	// Set the results.
	estimateBean.setCostCement40kg(costCement40kg);
	estimateBean.setCostCement50kg(costCement50kg);
	estimateBean.setCostSand(costSand);
	estimateBean.setCostGravel(costGravel);
	estimateBean.setCostCHB(costCHB);
    }

    /**
     * Convert Yes/No input from Excel to boolean type.
     * 
     * @param workbook
     * @param cell
     * @return
     */
    private boolean getEstimateBooleanFromExcel(HSSFWorkbook workbook, Cell cell) {
	String concrete = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
		: this.excelHelper.getValueAsExpected(workbook, cell));
	return StringUtils.deleteWhitespace(concrete).equals("Yes") ? true : false;
    }

    /**
     * Convert Excel to a list of Estimates.
     * 
     * @param multipartFile
     * @return
     */
    private List<EstimateBean> convertExcelToEstimates(MultipartFile multipartFile, Project proj) {
	try {

	    // Create Workbook instance holding reference to .xls file
	    // Get first/desired sheet from the workbook.
	    HSSFWorkbook workbook = new HSSFWorkbook(multipartFile.getInputStream());
	    HSSFSheet sheet = workbook.getSheetAt(0);

	    // Iterate through each rows one by one.
	    Iterator<Row> rowIterator = sheet.iterator();

	    // Construct estimate containers.
	    List<EstimateBean> estimateBeans = new ArrayList<EstimateBean>();
	    while (rowIterator.hasNext()) {

		Row row = rowIterator.next();
		int rowCountDisplay = row.getRowNum() + 1;

		// Skip first 3 lines.
		if (rowCountDisplay <= 3) {
		    continue;
		}

		// For each row, iterate through all the columns
		Iterator<Cell> cellIterator = row.cellIterator();

		// Every row, is an Estimate object.
		EstimateBean estimateBean = new EstimateBean(proj);
		ShapeBean shapeBean = new ShapeBean();
		List<EstimateType> estimateTypes = estimateBean.getEstimateTypes();

		while (cellIterator.hasNext()) {

		    // Cell in this row and column.
		    Cell cell = cellIterator.next();
		    int colCountDisplay = cell.getColumnIndex() + 1;

		    switch (colCountDisplay) {

		    case EXCEL_DETAILS_NAME:
			String name = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateBean.setName(name);
			continue;

		    case EXCEL_DETAILS_AREA:
			double area = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			shapeBean.setArea(area);
			shapeBean.setOriginalArea(area);
			estimateBean.setShape(shapeBean);
			continue;

		    case EXCEL_DETAILS_VOLUME:
			double volume = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			shapeBean.setVolume(volume);
			shapeBean.setOriginalVolume(volume);
			estimateBean.setShape(shapeBean);
			continue;

		    case EXCEL_ESTIMATE_MASONRY_CONCRETE:
			boolean concrete = getEstimateBooleanFromExcel(workbook, cell);
			if (concrete) {
			    estimateTypes.add(EstimateType.CONCRETE);
			    estimateBean.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_ESTIMATE_MASONRY_CHB:
			boolean chb = getEstimateBooleanFromExcel(workbook, cell);
			if (chb) {
			    estimateTypes.add(EstimateType.MASONRY_CHB);
			    estimateBean.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_ESTIMATE_MASONRY_CHB_LAYING:
			boolean chbLaying = getEstimateBooleanFromExcel(workbook, cell);
			if (chbLaying) {
			    estimateTypes.add(EstimateType.MASONRY_BLOCK_LAYING);
			    estimateBean.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_ESTIMATE_MASONRY_PLASTERING:
			boolean plaster = getEstimateBooleanFromExcel(workbook, cell);
			if (plaster) {
			    estimateTypes.add(EstimateType.MASONRY_PLASTERING);
			    estimateBean.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_ESTIMATE_MASONRY_FOUNDATION_HEIGHT:
			double foundation = (Double) (this.excelHelper
				.getValueAsExpected(workbook, cell) == null ? 0 : this.excelHelper
				.getValueAsExpected(workbook, cell));
			estimateBean.setChbFoundationHeight(foundation);
			continue;

		    case EXCEL_ESTIMATE_MASONRY_CHB_FOOTING:
			boolean footing = getEstimateBooleanFromExcel(workbook, cell);
			if (footing) {
			    estimateTypes.add(EstimateType.MASONRY_CHB_FOOTING);
			    estimateBean.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_ESTIMATE_MASONRY_FOOTING_LENGTH:
			double footingLength = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			shapeBean.setFootingLength(footingLength);
			estimateBean.setShape(shapeBean);
			continue;

		    case EXCEL_ESTIMATE_MR_CHB:
			boolean mrCHB = getEstimateBooleanFromExcel(workbook, cell);
			if (mrCHB) {
			    estimateTypes.add(EstimateType.METAL_REINFORCEMENT_CHB);
			    estimateBean.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_DETAILS_REMARKS:
			String remarks = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateBean.setRemarks(remarks);
			continue;

		    case EXCEL_COST_CHB:
			double costCHB = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateBean.setCostPerUnitCHB(costCHB);
			continue;

		    case EXCEL_COST_CEMENT_40KG:
			double costCement40kg = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			estimateBean.setCostPerUnitCement40kg(costCement40kg);
			continue;

		    case EXCEL_COST_CEMENT_50KG:
			double costCement50kg = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			estimateBean.setCostPerUnitCement50kg(costCement50kg);
			continue;

		    case EXCEL_COST_SAND:
			double costSand = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateBean.setCostPerUnitSand(costSand);
			continue;

		    case EXCEL_COST_GRAVEL:
			double costGravel = (Double) (this.excelHelper
				.getValueAsExpected(workbook, cell) == null ? 0 : this.excelHelper
				.getValueAsExpected(workbook, cell));
			estimateBean.setCostPerUnitGravel(costGravel);
			continue;

		    }
		}

		estimateBeans.add(estimateBean);
	    }
	    return estimateBeans;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return new ArrayList<EstimateBean>();
    }

    /**
     * Estimate a project's quantity of materials.
     */
    private void computeQuantityEstimate(EstimateBean estimateBean) {

	// TODO What if area is negative?

	// Shape to compute.
	ShapeBean shapeBean = estimateBean.getShape();

	// Prepare area and volume.
	// Set allowances.
	double allowance = estimateBean.getEstimationAllowance().getAllowance();
	if (allowance != 0.0) {
	    double area = shapeBean.getArea();
	    double volume = shapeBean.getVolume();
	    shapeBean.setArea(area + (area * allowance));
	    shapeBean.setVolume(volume + (volume * allowance));
	}

	// If we're estimating masonry CHB.
	if (estimateBean.willComputeMasonryCHB()) {
	    estimateCHBTotal(estimateBean, shapeBean);
	}

	// If we're estimating masonry block laying.
	if (estimateBean.willComputeMasonryBlockLaying()) {
	    estimateCHBLaying(estimateBean, shapeBean);
	}

	// If we're estimating masonry plastering.
	if (estimateBean.willComputeMasonryPlastering()) {
	    estimateMasonryPlastering(estimateBean, shapeBean);
	}

	// If we're estimating masonry CHB footing.
	if (estimateBean.willComputeMasonryCHBFooting()) {
	    estimateMasonryCHBFooting(estimateBean);
	}

	// If computing concrete.
	if (estimateBean.willComputeConcrete()) {
	    estimateConcrete(estimateBean, shapeBean);
	}
    }

    /**
     * Estimate the CHB footings.
     * 
     * @param estimateBean
     * @param proportions
     */
    private void estimateMasonryCHBFooting(EstimateBean estimateBean) {

	// Get the dimension key.
	// And the footing mixes.
	TableCHBFootingDimensions chbFooting = estimateBean.getChbFootingDimensions();
	String mixClass = estimateBean.getEstimationClass().getConcreteProportion().getMixClass();

	// Get the footing mixture given the mix class and footing dimensions.
	TableCHBFootingMixture footingMixture = getCHBFootingMixture(chbFooting, mixClass);

	// Get thickness and width.
	// TODO Do conversion for other calculations also.
	double footingThickness = convertToMeter(chbFooting.getThickessUnit(), chbFooting.getThickness());
	double footingWidth = convertToMeter(chbFooting.getWidthUnit(), chbFooting.getWidth());

	// TODO Optimize below code.
	// getLength(estimate) is called somewhere else in this class.
	double length = estimateBean.getShape().getFootingLength();
	double footingVolume = footingThickness * footingWidth * length;

	// Estimations.
	double cement = footingVolume * footingMixture.getPartCement40kg();
	double sand = footingVolume * footingMixture.getPartSand();
	double gravel = footingVolume * footingMixture.getPartGravel();

	// Put the results.
	MasonryCHBFootingEstimateResults footingResults = new MasonryCHBFootingEstimateResults(
		estimateBean, cement, gravel, sand);

	// Set the result map of the CHB footing estimate.
	estimateBean.setResultCHBFootingEstimate(footingResults);
    }

    /**
     * Get the CHB footing mixture.
     * 
     * @param footingMixes
     * @param dimensionKey
     * @param prop
     * @return
     */
    private TableCHBFootingMixture getCHBFootingMixture(TableCHBFootingDimensions chbFooting,
	    String mixClass) {

	for (TableCHBFootingMixture footingMix : TableCHBFootingMixture.class.getEnumConstants()) {

	    TableCHBFootingDimensions footing = footingMix.getFootingDimensions();
	    String footingClass = footingMix.getMixClass();

	    if (chbFooting == footing && footingClass.equals(mixClass)) {
		return footingMix;
	    }
	}
	return TableCHBFootingMixture.CLASS_A_15_60;
    }

    /**
     * Don't include the area below ground when plastering.
     * 
     * @param estimateBean
     * @param length
     * @param area
     */
    private double minusAreaBelowGround(EstimateBean estimateBean, double length, double area) {

	// If the unit is not meter,
	// convert it.
	double foundationHeight = estimateBean.getChbFoundationHeight();
	CommonLengthUnit lengthUnit = estimateBean.getChbFoundationUnit();
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
     * @param estimateBean
     * @param shapeBean
     * @param shapeArea
     * @param length
     * @param area
     */
    private double addAreaTopSide(EstimateBean estimateBean, ShapeBean shapeBean, double shapeArea,
	    double length, double area) {

	// Get the thickness.
	double thickness = shapeBean.getVolume() / shapeArea;

	// Get the area and add to overall area.
	double topSideArea = length * thickness;
	area += topSideArea;

	return area;
    }

    /**
     * Estimate amount of plastering.
     * 
     * @param estimateBean
     * @param shapeBean
     * @param proportions
     */
    private void estimateMasonryPlastering(EstimateBean estimateBean, ShapeBean shapeBean) {

	// Get the length "longest side of the shape".
	double length = shapeBean.getFootingLength();

	// Consider the height below ground.
	// Get the height of foundation (height of wall below the
	// ground) and don't include that to the area to be plastered.
	double shapeArea = shapeBean.getArea();
	double area = minusAreaBelowGround(estimateBean, length, shapeArea);

	// If we're plastering back to back,
	// multiply the area by 2.
	// Else, plaster only 1 side.
	area = area * 2;

	// If we're plastering the top side,
	// get the thickness area then plaster it.
	area = addAreaTopSide(estimateBean, shapeBean, shapeArea, length, area);

	double volume = area * TablePlasterMixture.STANDARD_PLASTER_THICKNESS;

	// Find the appropriate plaster mixture
	// given this proportion.
	TableConcreteProportion proportion = estimateBean.getEstimationClass().getConcreteProportion();
	String proportionMixClass = proportion.getMixClass();

	// Find the plaster mix.
	TablePlasterMixture plasterMixture = TablePlasterMixture.CLASS_A;
	for (TablePlasterMixture plasterMix : TablePlasterMixture.class.getEnumConstants()) {

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
		estimateBean, bags40kg, bags50kg, sand);
	estimateBean.setResultPlasteringEstimate(plasteringResults);
    }

    /**
     * Estimate the block laying.
     * 
     * @param estimateBean
     * @param shapeBean
     * @param chbList
     */
    private void estimateCHBLaying(EstimateBean estimateBean, ShapeBean shapeBean) {

	// Prepare needed arguments.
	TableCHBDimensions chb = estimateBean.getChbDimensions();
	TableConcreteProportion proportion = estimateBean.getEstimationClass().getConcreteProportion();
	TableCHBLayingMixture chbLayingMix = getCHBLayingMixture(chb, proportion);

	// Get the inputs.
	double area = shapeBean.getArea();
	double bags = chbLayingMix.getPartCement40kgBag(); // 40kg bags.
	double sand = chbLayingMix.getPartSand(); // Cubic meters.

	// Compute.
	double bagsNeeded = area * bags;
	double sandNeeded = area * sand;

	// Set the results.
	MasonryCHBLayingEstimateResults layingResults = new MasonryCHBLayingEstimateResults(
		estimateBean, bagsNeeded, sandNeeded);
	estimateBean.setResultCHBLayingEstimate(layingResults);
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
	for (TableCHBLayingMixture mix : TableCHBLayingMixture.class.getEnumConstants()) {

	    String layingMixClass = mix.getMixClass();
	    TableCHBDimensions chbFromLaying = mix.getChb();

	    // Get correct CHB,
	    // and correct concrete proportion.
	    if (layingMixClass.equals(proportionMixClass) && chbFromLaying == chb) {
		return mix;
	    }
	}
	return TableCHBLayingMixture.CLASS_A_20_20_40;
    }

    /**
     * Estimate the number of components needed for this concrete.
     * 
     * @param estimateBean
     * @param shapeBean
     */
    private void estimateConcrete(EstimateBean estimateBean, ShapeBean shapeBean) {

	double volume = shapeBean.getVolume();

	TableConcreteProportion tableConcreteProportion = estimateBean.getEstimationClass()
		.getConcreteProportion();

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

	ConcreteEstimateResults concreteResults = new ConcreteEstimateResults(estimateBean,
		estCement40kg, estCement50kg, estSand, estGravel);

	// Set the results.
	estimateBean.setResultConcreteEstimate(concreteResults);
    }

    /**
     * Get quantity estimation of masonry.
     * 
     * @param estimateBean
     * 
     * @param estimateBean
     * @param shapeBean
     * @param chb
     * @return
     */
    private void estimateCHBTotal(EstimateBean estimateBean, ShapeBean shapeBean) {

	double area = shapeBean.getArea();

	// Get total CHBs.
	double totalCHB = area * TableCHBDimensions.STANDARD_CHB_PER_SQ_M;

	// Results of the estimate.
	MasonryCHBEstimateResults masonryCHBEstimateResults = new MasonryCHBEstimateResults(
		estimateBean, totalCHB);
	estimateBean.setResultCHBEstimate(masonryCHBEstimateResults);
    }

    /**
     * Convert the value to meter.
     * 
     * @param lengthUnit
     * @param value
     * @return
     */
    private BigDecimal convertToMeter(CommonLengthUnit lengthUnit, BigDecimal value) {
	double meterConvert = lengthUnit.conversionToMeter();
	double convertedValue = meterConvert * value.doubleValue();
	value = new BigDecimal(convertedValue);
	return value;
    }

    private double convertToMeter(CommonLengthUnit lengthUnit, double value) {
	return convertToMeter(lengthUnit, new BigDecimal(value)).doubleValue();
    }

}
