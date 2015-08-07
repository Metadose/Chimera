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
import com.cebedo.pmsys.bean.EstimationInputBean;
import com.cebedo.pmsys.bean.EstimationOutputRowBean;
import com.cebedo.pmsys.bean.MasonryCHBEstimateResults;
import com.cebedo.pmsys.bean.MasonryCHBFootingEstimateResults;
import com.cebedo.pmsys.bean.MasonryCHBLayingEstimateResults;
import com.cebedo.pmsys.bean.MasonryPlasteringEstimateResults;
import com.cebedo.pmsys.constants.RedisConstants;
import com.cebedo.pmsys.domain.Estimate;
import com.cebedo.pmsys.domain.EstimationOutput;
import com.cebedo.pmsys.domain.Shape;
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

    public static final int EXCEL_COLUMN_DETAILS_NAME = 1;
    public static final int EXCEL_COLUMN_DETAILS_AREA = 2;
    public static final int EXCEL_COLUMN_DETAILS_VOLUME = 3;
    public static final int EXCEL_COLUMN_ESTIMATE_MASONRY_CONCRETE = 4;
    public static final int EXCEL_COLUMN_ESTIMATE_MASONRY_CHB = 5;
    public static final int EXCEL_COLUMN_ESTIMATE_MASONRY_CHB_LAYING = 6;
    public static final int EXCEL_COLUMN_ESTIMATE_MASONRY_PLASTERING = 7;
    public static final int EXCEL_COLUMN_ESTIMATE_MASONRY_FOUNDATION_HEIGHT = 8;
    public static final int EXCEL_COLUMN_ESTIMATE_MASONRY_CHB_FOOTING = 9;
    public static final int EXCEL_COLUMN_ESTIMATE_MASONRY_FOOTING_LENGTH = 10;
    public static final int EXCEL_COLUMN_ESTIMATE_MR_CHB = 11;
    public static final int EXCEL_COLUMN_DETAILS_REMARKS = 12;

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
	this.messageHelper.send(AuditAction.ESTIMATE, Project.OBJECT_NAME, proj.getId(),
		EstimationInputBean.class.getName());

	// Do the commit.
	// If create.
	if (estimateInput.getEstimationFile() != null) {

	    // New object.
	    EstimationOutput estimationOutput = new EstimationOutput(estimateInput);

	    // Convert the excel file to objects.
	    List<Estimate> estimates = convertExcelToEstimates(estimateInput.getEstimationFile(),
		    estimateInput.getProject());

	    // Process each object.
	    List<EstimationOutputRowBean> rowList = new ArrayList<EstimationOutputRowBean>();
	    for (Estimate estimate : estimates) {
		estimate.setEstimationAllowance(estimateInput.getEstimationAllowance());
		computeQuantityEstimate(estimate);
		rowList.add(estimate.getResultRow());
	    }

	    // Set the list.
	    // Set the object.
	    String rowListJson = new Gson().toJson(rowList, ArrayList.class);
	    estimationOutput.setResults(estimateInput, estimates, rowListJson);
	    this.estimationOutputValueRepo.set(estimationOutput);

	    return AlertBoxGenerator.SUCCESS.generateCreate(RedisConstants.OBJECT_ESTIMATE, "TODO");
	}

	// TODO If update.
	// this.estimateValueRepo.set(obj);
	return AlertBoxGenerator.SUCCESS.generateUpdate(RedisConstants.OBJECT_ESTIMATE, "TODO");
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
    private List<Estimate> convertExcelToEstimates(MultipartFile multipartFile, Project proj) {
	try {

	    // Create Workbook instance holding reference to .xls file
	    // Get first/desired sheet from the workbook.
	    HSSFWorkbook workbook = new HSSFWorkbook(multipartFile.getInputStream());
	    HSSFSheet sheet = workbook.getSheetAt(0);

	    // Iterate through each rows one by one.
	    Iterator<Row> rowIterator = sheet.iterator();

	    // Construct estimate containers.
	    List<Estimate> estimates = new ArrayList<Estimate>();
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
		// TODO Add Project object.
		Estimate estimate = new Estimate(proj);
		Shape shape = new Shape();
		List<EstimateType> estimateTypes = estimate.getEstimateTypes();

		while (cellIterator.hasNext()) {

		    // Cell in this row and column.
		    Cell cell = cellIterator.next();
		    int colCountDisplay = cell.getColumnIndex() + 1;

		    switch (colCountDisplay) {

		    case EXCEL_COLUMN_DETAILS_NAME:
			String name = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimate.setName(name);
			continue;

		    case EXCEL_COLUMN_DETAILS_AREA:
			double area = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			shape.setArea(area);
			shape.setOriginalArea(area);
			estimate.setShape(shape);
			continue;

		    case EXCEL_COLUMN_DETAILS_VOLUME:
			double volume = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			shape.setVolume(volume);
			shape.setOriginalVolume(volume);
			estimate.setShape(shape);
			continue;

		    case EXCEL_COLUMN_ESTIMATE_MASONRY_CONCRETE:
			boolean concrete = getEstimateBooleanFromExcel(workbook, cell);
			if (concrete) {
			    estimateTypes.add(EstimateType.CONCRETE);
			    estimate.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_COLUMN_ESTIMATE_MASONRY_CHB:
			boolean chb = getEstimateBooleanFromExcel(workbook, cell);
			if (chb) {
			    estimateTypes.add(EstimateType.MASONRY_CHB);
			    estimate.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_COLUMN_ESTIMATE_MASONRY_CHB_LAYING:
			boolean chbLaying = getEstimateBooleanFromExcel(workbook, cell);
			if (chbLaying) {
			    estimateTypes.add(EstimateType.MASONRY_BLOCK_LAYING);
			    estimate.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_COLUMN_ESTIMATE_MASONRY_PLASTERING:
			boolean plaster = getEstimateBooleanFromExcel(workbook, cell);
			if (plaster) {
			    estimateTypes.add(EstimateType.MASONRY_PLASTERING);
			    estimate.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_COLUMN_ESTIMATE_MASONRY_FOUNDATION_HEIGHT:
			double foundation = (Double) (this.excelHelper
				.getValueAsExpected(workbook, cell) == null ? "" : this.excelHelper
				.getValueAsExpected(workbook, cell));
			estimate.setChbFoundationHeight(foundation);
			continue;

		    case EXCEL_COLUMN_ESTIMATE_MASONRY_CHB_FOOTING:
			boolean footing = getEstimateBooleanFromExcel(workbook, cell);
			if (footing) {
			    estimateTypes.add(EstimateType.MASONRY_CHB_FOOTING);
			    estimate.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_COLUMN_ESTIMATE_MASONRY_FOOTING_LENGTH:
			double footingLength = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? "" : this.excelHelper.getValueAsExpected(workbook, cell));
			shape.setFootingLength(footingLength);
			estimate.setShape(shape);
			continue;

		    case EXCEL_COLUMN_ESTIMATE_MR_CHB:
			boolean mrCHB = getEstimateBooleanFromExcel(workbook, cell);
			if (mrCHB) {
			    estimateTypes.add(EstimateType.METAL_REINFORCEMENT_CHB);
			    estimate.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_COLUMN_DETAILS_REMARKS:
			String remarks = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimate.setRemarks(remarks);
			continue;

		    }
		}

		estimates.add(estimate);
	    }
	    return estimates;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return new ArrayList<Estimate>();
    }

    /**
     * Estimate a project's quantity of materials.
     */
    private void computeQuantityEstimate(Estimate estimate) {

	// TODO What if area is negative?

	// Shape to compute.
	Shape shape = estimate.getShape();

	// Prepare area and volume.
	// Set allowances.
	double allowance = estimate.getEstimationAllowance().getAllowance();
	if (allowance != 0.0) {
	    double area = shape.getArea();
	    double volume = shape.getVolume();
	    shape.setArea(area + (area * allowance));
	    shape.setVolume(volume + (volume * allowance));
	}

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

	estimate.setUuid(UUID.randomUUID());
	EstimationOutputRowBean row = new EstimationOutputRowBean(estimate);
	estimate.setResultRow(row);
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
	TableCHBFootingDimensions chbFooting = estimate.getChbFootingDimensions();
	String mixClass = estimate.getEstimationClass().getConcreteProportion().getMixClass();

	// Get the footing mixture given the mix class and footing dimensions.
	TableCHBFootingMixture footingMixture = getCHBFootingMixture(chbFooting, mixClass);

	// Get thickness and width.
	// TODO Do conversion for other calculations also.
	double footingThickness = convertToMeter(chbFooting.getThickessUnit(), chbFooting.getThickness());
	double footingWidth = convertToMeter(chbFooting.getWidthUnit(), chbFooting.getWidth());

	// TODO Optimize below code.
	// getLength(estimate) is called somewhere else in this class.
	double length = estimate.getShape().getFootingLength();
	double footingVolume = footingThickness * footingWidth * length;

	// Estimations.
	double cement = footingVolume * footingMixture.getPartCement40kg();
	double sand = footingVolume * footingMixture.getPartSand();
	double gravel = footingVolume * footingMixture.getPartGravel();

	// Put the results.
	MasonryCHBFootingEstimateResults footingResults = new MasonryCHBFootingEstimateResults(cement,
		gravel, sand, chbFooting, footingMixture);

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
     * @param estimate
     * @param length
     * @param area
     */
    private double minusAreaBelowGround(Estimate estimate, double length, double area) {

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
    private double addAreaTopSide(Estimate estimate, Shape shape, double shapeArea, double length,
	    double area) {

	// Get the thickness.
	double thickness = shape.getVolume() / shapeArea;

	// Get the area and add to overall area.
	double topSideArea = length * thickness;
	area += topSideArea;

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

	// Get the length "longest side of the shape".
	double length = shape.getFootingLength();

	// Consider the height below ground.
	// Get the height of foundation (height of wall below the
	// ground) and don't include that to the area to be plastered.
	double shapeArea = shape.getArea();
	double area = minusAreaBelowGround(estimate, length, shapeArea);

	// If we're plastering back to back,
	// multiply the area by 2.
	// Else, plaster only 1 side.
	area = area * 2;

	// If we're plastering the top side,
	// get the thickness area then plaster it.
	area = addAreaTopSide(estimate, shape, shapeArea, length, area);

	double volume = area * TablePlasterMixture.STANDARD_PLASTER_THICKNESS;

	// Find the appropriate plaster mixture
	// given this proportion.
	TableConcreteProportion proportion = estimate.getEstimationClass().getConcreteProportion();
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
		bags40kg, bags50kg, sand, proportion, plasterMixture);
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
	TableConcreteProportion proportion = estimate.getEstimationClass().getConcreteProportion();
	TableCHBLayingMixture chbLayingMix = getCHBLayingMixture(chb, proportion);

	// Get the inputs.
	double area = shape.getArea();
	double bags = chbLayingMix.getPartCement40kgBag(); // 40kg bags.
	double sand = chbLayingMix.getPartSand(); // Cubic meters.

	// Compute.
	double bagsNeeded = area * bags;
	double sandNeeded = area * sand;

	// Set the results.
	MasonryCHBLayingEstimateResults layingResults = new MasonryCHBLayingEstimateResults(chb,
		chbLayingMix, proportion, bagsNeeded, sandNeeded);
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
     * @param estimate
     * @param shape
     */
    private void estimateConcrete(Estimate estimate, Shape shape) {

	// Now, compute the estimated concrete.
	ConcreteEstimateResults concreteResults = getConcreteEstimateResults(estimate
		.getEstimationClass().getConcreteProportion(), shape);

	// Set the results.
	estimate.setResultConcreteEstimate(concreteResults);
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

	ConcreteEstimateResults concreteResults = new ConcreteEstimateResults(estCement40kg,
		estCement50kg, estSand, estGravel);

	return concreteResults;
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
