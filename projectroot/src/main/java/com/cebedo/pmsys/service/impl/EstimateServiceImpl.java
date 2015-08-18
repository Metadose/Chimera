package com.cebedo.pmsys.service.impl;

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

import com.cebedo.pmsys.bean.EstimateComputationBean;
import com.cebedo.pmsys.bean.EstimateComputationInputBean;
import com.cebedo.pmsys.bean.EstimateComputationOutputRowJSON;
import com.cebedo.pmsys.bean.EstimateComputationShape;
import com.cebedo.pmsys.bean.EstimateResultConcrete;
import com.cebedo.pmsys.bean.EstimateResultMRCHB;
import com.cebedo.pmsys.bean.EstimateResultMasonryCHB;
import com.cebedo.pmsys.bean.EstimateResultMasonryCHBFooting;
import com.cebedo.pmsys.bean.EstimateResultMasonryCHBLaying;
import com.cebedo.pmsys.bean.EstimateResultMasonryPlastering;
import com.cebedo.pmsys.constants.ConstantsEstimation;
import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.domain.EstimationOutput;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.enums.EstimateType;
import com.cebedo.pmsys.enums.TableDimensionCHB;
import com.cebedo.pmsys.enums.TableDimensionCHBFooting;
import com.cebedo.pmsys.enums.TableMRCHBHorizontal;
import com.cebedo.pmsys.enums.TableMRCHBTieWire;
import com.cebedo.pmsys.enums.TableMRCHBVertical;
import com.cebedo.pmsys.enums.TableMixtureCHBFooting;
import com.cebedo.pmsys.enums.TableMixtureCHBLaying;
import com.cebedo.pmsys.enums.TableMixturePlaster;
import com.cebedo.pmsys.enums.TableProportionConcrete;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.ExcelHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.EstimationOutputValueRepo;
import com.cebedo.pmsys.service.EstimateService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.EstimateUtils;
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
    private static final int EXCEL_ESTIMATE_MASONRY_FOUNDATION_AREA = 8;
    private static final int EXCEL_ESTIMATE_MASONRY_CHB_FOOTING = 9;
    private static final int EXCEL_ESTIMATE_MASONRY_FOOTING_LENGTH = 10;
    private static final int EXCEL_ESTIMATE_MASONRY_FOOTING_WIDTH = 11;
    private static final int EXCEL_ESTIMATE_MASONRY_FOOTING_HEIGHT = 12;
    private static final int EXCEL_ESTIMATE_MR_CHB = 13;
    private static final int EXCEL_DETAILS_REMARKS = 14;

    // Cost
    private static final int EXCEL_COST_CHB = 15;
    private static final int EXCEL_COST_CEMENT_40KG = 16;
    private static final int EXCEL_COST_CEMENT_50KG = 17;
    private static final int EXCEL_COST_SAND = 18;
    private static final int EXCEL_COST_GRAVEL = 19;
    private static final int EXCEL_COST_STEEL_BAR = 20;
    private static final int EXCEL_COST_TIE_WIRE_KILOS = 21;
    private static final int EXCEL_COST_TIE_WIRE_ROLLS = 22;

    private MessageHelper messageHelper = new MessageHelper();
    private AuthHelper authHelper = new AuthHelper();
    private ExcelHelper excelHelper = new ExcelHelper();

    private EstimationOutputValueRepo estimationOutputValueRepo;

    public void setEstimationOutputValueRepo(EstimationOutputValueRepo estimationOutputValueRepo) {
	this.estimationOutputValueRepo = estimationOutputValueRepo;
    }

    @Override
    @Transactional
    public String estimate(EstimateComputationInputBean estimateInput) {

	// Security check.
	Project proj = estimateInput.getProject();
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_ESTIMATE, Project.OBJECT_NAME, proj.getId(),
		EstimateComputationInputBean.class.getName());

	// Do the commit.
	// If create.
	if (estimateInput.getEstimationFile() != null) {

	    // New object.
	    EstimationOutput estimationOutput = new EstimationOutput(estimateInput);

	    // Convert the excel file to objects.
	    List<EstimateComputationBean> estimateComputationBeans = convertExcelToEstimates(
		    estimateInput.getEstimationFile(), estimateInput.getProject());

	    // Process each object.
	    List<EstimateComputationOutputRowJSON> rowListForJSON = new ArrayList<EstimateComputationOutputRowJSON>();
	    for (EstimateComputationBean estimateComputationBean : estimateComputationBeans) {

		// Set allowance.
		estimateComputationBean.setEstimationAllowance(estimateInput.getEstimationAllowance());

		// For each row, compute the total quantity.
		// For each type in the row, compute the cost.
		computeRowQuantityAndPerTypeCost(estimateComputationBean);

		// For each row, compute the total cost.
		computeRowCost(estimateComputationBean);

		// Update the grand total of the estimation.
		updateGrandTotals(estimationOutput, estimateComputationBean);

		// Add to list of beans to be converted to JSON later.
		rowListForJSON.add(new EstimateComputationOutputRowJSON(estimateComputationBean));
	    }

	    // Set the list.
	    String rowListJson = new Gson().toJson(rowListForJSON, ArrayList.class);
	    estimationOutput.setResults(estimateInput, estimateComputationBeans, rowListJson);

	    // Save the object.
	    estimationOutput.setUuid(UUID.randomUUID());
	    this.estimationOutputValueRepo.set(estimationOutput);

	    return AlertBoxGenerator.SUCCESS.generateCreate(ConstantsRedis.OBJECT_ESTIMATE, "TODO");
	}

	// If file is null, return an error.
	return AlertBoxGenerator.ERROR;
    }

    /**
     * Update the quantity and cost grand totals of this estimation.
     * 
     * @param estimationOutput
     * @param estimateComputationBean
     */
    private void updateGrandTotals(EstimationOutput estimationOutput,
	    EstimateComputationBean estimateComputationBean) {

	// Cost.
	double costCHB = estimationOutput.getCostCHB() + estimateComputationBean.getCostCHB();
	double costCement40kg = estimationOutput.getCostCement40kg()
		+ estimateComputationBean.getCostCement40kg();
	double costCement50kg = estimationOutput.getCostCement50kg()
		+ estimateComputationBean.getCostCement50kg();
	double costSand = estimationOutput.getCostSand() + estimateComputationBean.getCostSand();
	double costGravel = estimationOutput.getCostGravel() + estimateComputationBean.getCostGravel();
	double costSteelBars = estimationOutput.getCostSteelBars()
		+ estimateComputationBean.getCostSteelBars();
	double costTieWireKilos = estimationOutput.getCostTieWireKilos()
		+ estimateComputationBean.getCostTieWireKilos();
	double costTieWireRolls = estimationOutput.getCostTieWireRolls()
		+ estimateComputationBean.getCostTieWireRolls();

	estimationOutput.setCostCHB(costCHB);
	estimationOutput.setCostCement40kg(costCement40kg);
	estimationOutput.setCostCement50kg(costCement50kg);
	estimationOutput.setCostSand(costSand);
	estimationOutput.setCostGravel(costGravel);
	estimationOutput.setCostSteelBars(costSteelBars);
	estimationOutput.setCostTieWireKilos(costTieWireKilos);
	estimationOutput.setCostTieWireRolls(costTieWireRolls);

	// Quantity.
	double quantCHB = estimationOutput.getQuantityCHB() + estimateComputationBean.getQuantityCHB();
	double quantCement40kg = estimationOutput.getQuantityCement40kg()
		+ estimateComputationBean.getQuantityCement40kg();
	double quantCement50kg = estimationOutput.getQuantityCement50kg()
		+ estimateComputationBean.getQuantityCement50kg();
	double quantSand = estimationOutput.getQuantitySand()
		+ estimateComputationBean.getQuantitySand();
	double quantGravel = estimationOutput.getQuantityGravel()
		+ estimateComputationBean.getQuantityGravel();
	double quantitySteelBars = estimationOutput.getQuantitySteelBars()
		+ estimateComputationBean.getQuantitySteelBars();
	double quantityTieWireKilos = estimationOutput.getQuantityTieWireKilos()
		+ estimateComputationBean.getQuantityTieWireKilos();
	double quantityTieWireRolls = estimationOutput.getQuantityTieWireRolls()
		+ estimateComputationBean.getQuantityTieWireRolls();

	estimationOutput.setQuantityCHB(quantCHB);
	estimationOutput.setQuantityCement40kg(quantCement40kg);
	estimationOutput.setQuantityCement50kg(quantCement50kg);
	estimationOutput.setQuantitySand(quantSand);
	estimationOutput.setQuantityGravel(quantGravel);
	estimationOutput.setQuantitySteelBars(quantitySteelBars);
	estimationOutput.setQuantityTieWireKilos(quantityTieWireKilos);
	estimationOutput.setQuantityTieWireRolls(quantityTieWireRolls);

	// Grand total.
	double rowTotal = costCHB + costCement40kg + costCement50kg + costSand + costGravel
		+ costSteelBars + costTieWireKilos + costTieWireRolls;
	estimationOutput.setCostGrandTotal(rowTotal);
    }

    /**
     * Estimate the cost for the whole row.
     * 
     * @param estimateComputationBean
     */
    private void computeRowCost(EstimateComputationBean estimateComputationBean) {

	EstimateResultConcrete concrete = estimateComputationBean.getResultConcreteEstimate();
	EstimateResultMasonryCHB chb = estimateComputationBean.getResultCHBEstimate();
	EstimateResultMasonryCHBLaying chbLaying = estimateComputationBean.getResultCHBLayingEstimate();
	EstimateResultMasonryPlastering plaster = estimateComputationBean.getResultPlasteringEstimate();
	EstimateResultMasonryCHBFooting footing = estimateComputationBean.getResultCHBFootingEstimate();
	EstimateResultMRCHB mrCHB = estimateComputationBean.getResultMRCHB();

	double costCement40kg = 0, costCement50kg = 0, costSand = 0, costGravel = 0, costCHB = 0;
	double costSteelBar = 0, costTieWireKG = 0, costTieWireRoll = 0;

	// Concrete.
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

	// Metal reinforcement (CHB).
	costSteelBar += mrCHB.getCostSteelBars();
	costTieWireKG += mrCHB.getCostTieWireKilos();
	costTieWireRoll += mrCHB.getCostTieWireRolls();

	// Set the results for the whole row.
	estimateComputationBean.setCostCement40kg(costCement40kg);
	estimateComputationBean.setCostCement50kg(costCement50kg);
	estimateComputationBean.setCostSand(costSand);
	estimateComputationBean.setCostGravel(costGravel);
	estimateComputationBean.setCostCHB(costCHB);
	estimateComputationBean.setCostSteelBars(costSteelBar);
	estimateComputationBean.setCostTieWireKilos(costTieWireKG);
	estimateComputationBean.setCostTieWireRolls(costTieWireRoll);
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
    private List<EstimateComputationBean> convertExcelToEstimates(MultipartFile multipartFile,
	    Project proj) {
	try {

	    // Create Workbook instance holding reference to .xls file
	    // Get first/desired sheet from the workbook.
	    HSSFWorkbook workbook = new HSSFWorkbook(multipartFile.getInputStream());
	    HSSFSheet sheet = workbook.getSheetAt(0);

	    // Iterate through each rows one by one.
	    Iterator<Row> rowIterator = sheet.iterator();

	    // Construct estimate containers.
	    List<EstimateComputationBean> estimateComputationBeans = new ArrayList<EstimateComputationBean>();

	    double costCHB = 0;
	    double costCement40kg = 0;
	    double costCement50kg = 0;
	    double costSand = 0;
	    double costGravel = 0;
	    double costSteelBars = 0;
	    double costTieWireKilos = 0;
	    double costTieWireRoll = 0;

	    boolean firstRow = true;

	    // Looping all rows.
	    while (rowIterator.hasNext()) {

		Row row = rowIterator.next();
		int rowCountDisplay = row.getRowNum() + 1;

		// Skip lines.
		if (rowCountDisplay <= 4) {
		    continue;
		}

		// For each row, iterate through all the columns
		Iterator<Cell> cellIterator = row.cellIterator();

		// Every row, is an Estimate object.
		EstimateComputationBean estimateComputationBean = new EstimateComputationBean(proj);
		EstimateComputationShape estimateComputationShape = new EstimateComputationShape();
		List<EstimateType> estimateTypes = estimateComputationBean.getEstimateTypes();

		// If this is not the first row,
		// then the costs must have already been initialized.
		if (!firstRow) {
		    estimateComputationBean.setCostPerUnitCHB(costCHB);
		    estimateComputationBean.setCostPerUnitCement40kg(costCement40kg);
		    estimateComputationBean.setCostPerUnitCement50kg(costCement50kg);
		    estimateComputationBean.setCostPerUnitSand(costSand);
		    estimateComputationBean.setCostPerUnitGravel(costGravel);
		    estimateComputationBean.setCostPerUnitSteelBars(costSteelBars);
		    estimateComputationBean.setCostPerUnitTieWireKilos(costTieWireKilos);
		    estimateComputationBean.setCostPerUnitTieWireRolls(costTieWireRoll);
		}

		// Looping all cells in this row.
		while (cellIterator.hasNext()) {

		    // Cell in this row and column.
		    Cell cell = cellIterator.next();
		    int colCountDisplay = cell.getColumnIndex() + 1;

		    switch (colCountDisplay) {

		    case EXCEL_DETAILS_NAME:
			String name = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationBean.setName(name);
			continue;

		    case EXCEL_DETAILS_AREA:
			double area = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationShape.setArea(area);
			estimateComputationShape.setOriginalArea(area);
			estimateComputationBean.setShape(estimateComputationShape);
			continue;

		    case EXCEL_DETAILS_VOLUME:
			double volume = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationShape.setVolume(volume);
			estimateComputationShape.setOriginalVolume(volume);
			estimateComputationBean.setShape(estimateComputationShape);
			continue;

		    case EXCEL_ESTIMATE_MASONRY_CONCRETE:
			boolean concrete = getEstimateBooleanFromExcel(workbook, cell);
			if (concrete) {
			    estimateTypes.add(EstimateType.CONCRETE);
			    estimateComputationBean.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_ESTIMATE_MASONRY_CHB:
			boolean chb = getEstimateBooleanFromExcel(workbook, cell);
			if (chb) {
			    estimateTypes.add(EstimateType.MASONRY_CHB);
			    estimateComputationBean.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_ESTIMATE_MASONRY_CHB_LAYING:
			boolean chbLaying = getEstimateBooleanFromExcel(workbook, cell);
			if (chbLaying) {
			    estimateTypes.add(EstimateType.MASONRY_BLOCK_LAYING);
			    estimateComputationBean.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_ESTIMATE_MASONRY_PLASTERING:
			boolean plaster = getEstimateBooleanFromExcel(workbook, cell);
			if (plaster) {
			    estimateTypes.add(EstimateType.MASONRY_PLASTERING);
			    estimateComputationBean.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_ESTIMATE_MASONRY_FOUNDATION_AREA:
			double foundationArea = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationBean.setAreaBelowGround(foundationArea);
			continue;

		    case EXCEL_ESTIMATE_MASONRY_CHB_FOOTING:
			boolean footing = getEstimateBooleanFromExcel(workbook, cell);
			if (footing) {
			    estimateTypes.add(EstimateType.MASONRY_CHB_FOOTING);
			    estimateComputationBean.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_ESTIMATE_MASONRY_FOOTING_LENGTH:
			double footingLength = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationShape.setFootingLength(footingLength);
			estimateComputationBean.setShape(estimateComputationShape);
			continue;

		    case EXCEL_ESTIMATE_MASONRY_FOOTING_WIDTH:
			double footingWidth = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationShape.setFootingWidth(footingWidth);
			estimateComputationBean.setShape(estimateComputationShape);
			continue;

		    case EXCEL_ESTIMATE_MASONRY_FOOTING_HEIGHT:
			double footingHeight = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationShape.setFootingHeight(footingHeight);
			estimateComputationBean.setShape(estimateComputationShape);
			continue;

		    case EXCEL_ESTIMATE_MR_CHB:
			boolean mrCHB = getEstimateBooleanFromExcel(workbook, cell);
			if (mrCHB) {
			    estimateTypes.add(EstimateType.METAL_REINFORCEMENT_CHB);
			    estimateComputationBean.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_DETAILS_REMARKS:
			String remarks = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationBean.setRemarks(remarks);
			continue;

		    case EXCEL_COST_CHB:
			costCHB = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationBean.setCostPerUnitCHB(costCHB);
			continue;

		    case EXCEL_COST_CEMENT_40KG:
			costCement40kg = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationBean.setCostPerUnitCement40kg(costCement40kg);
			continue;

		    case EXCEL_COST_CEMENT_50KG:
			costCement50kg = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationBean.setCostPerUnitCement50kg(costCement50kg);
			continue;

		    case EXCEL_COST_SAND:
			costSand = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationBean.setCostPerUnitSand(costSand);
			continue;

		    case EXCEL_COST_GRAVEL:
			costGravel = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationBean.setCostPerUnitGravel(costGravel);
			continue;

		    case EXCEL_COST_STEEL_BAR:
			costSteelBars = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationBean.setCostPerUnitSteelBars(costSteelBars);
			continue;

		    case EXCEL_COST_TIE_WIRE_KILOS:
			costTieWireKilos = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationBean.setCostPerUnitTieWireKilos(costTieWireKilos);
			continue;

		    case EXCEL_COST_TIE_WIRE_ROLLS:
			costTieWireRoll = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? 0
				: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationBean.setCostPerUnitTieWireRolls(costTieWireRoll);
			continue;

		    }
		}
		firstRow = false;
		estimateComputationBeans.add(estimateComputationBean);
	    }
	    return estimateComputationBeans;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return new ArrayList<EstimateComputationBean>();
    }

    /**
     * Estimate for:<br>
     * 1) Quantity: Each Estimation type.<br>
     * 2) Quantity: The whole row.<br>
     * 3) Cost: Each Estimation type.
     */
    private void computeRowQuantityAndPerTypeCost(EstimateComputationBean estimateComputationBean) {

	// TODO What if area is negative?

	// Shape to compute.
	EstimateComputationShape estimateComputationShape = estimateComputationBean.getShape();

	// Prepare area and volume.
	// Set allowances.
	double allowance = estimateComputationBean.getEstimationAllowance().getAllowance();
	if (allowance != 0.0) {
	    double area = estimateComputationShape.getArea();
	    double volume = estimateComputationShape.getVolume();
	    estimateComputationShape.setArea(area + (area * allowance));
	    estimateComputationShape.setVolume(volume + (volume * allowance));
	}

	// If computing concrete.
	if (estimateComputationBean.willComputeConcrete()) {
	    estimateConcrete(estimateComputationBean, estimateComputationShape);
	}

	// If we're estimating masonry CHB.
	if (estimateComputationBean.willComputeMasonryCHB()) {
	    estimateCHBTotal(estimateComputationBean, estimateComputationShape);
	}

	// If we're estimating masonry block laying.
	if (estimateComputationBean.willComputeMasonryBlockLaying()) {
	    estimateCHBLaying(estimateComputationBean, estimateComputationShape);
	}

	// If we're estimating masonry plastering.
	if (estimateComputationBean.willComputeMasonryPlastering()) {
	    estimateMasonryPlastering(estimateComputationBean, estimateComputationShape);
	}

	// If we're estimating masonry CHB footing.
	if (estimateComputationBean.willComputeMasonryCHBFooting()) {
	    estimateMasonryCHBFooting(estimateComputationBean);
	}

	// If we're estimating metal reinforcement for CHB.
	if (estimateComputationBean.willComputeMRCHB()) {
	    estimateMRCHB(estimateComputationBean);
	}

    }

    /**
     * Estimate steel bars and tie wires for CHB metal reinforcement.
     * 
     * @param estimateComputationBean
     */
    private void estimateMRCHB(EstimateComputationBean estimateComputationBean) {
	EstimateComputationShape shape = estimateComputationBean.getShape();
	TableMRCHBVertical mrVertical = estimateComputationBean.getMrCHBVertical();
	TableMRCHBHorizontal mrHorizontal = estimateComputationBean.getMrCHBHorizontal();

	double area = shape.getArea();
	double verticalMR = area * mrVertical.getPerSqMeter();
	double horizontalMR = area * mrHorizontal.getPerSqMeter();
	double totalMRLength = verticalMR + horizontalMR;

	// Number of steel bars to buy.
	double steelBars = Math.ceil(totalMRLength
		/ ConstantsEstimation.STEEL_BAR_COMMERCIAL_LENGTH_METER);

	TableMRCHBTieWire tieWireTable = estimateComputationBean.getMrCHBTieWire();
	double kgPerSqMeter = tieWireTable.getKgPerSqMeter();

	// Number of tie wire kilos to buy.
	double tieWireKilos = Math.ceil(area * kgPerSqMeter);

	// Number of tie wire rolls to buy.
	double tieWireRolls = Math.ceil(tieWireKilos / ConstantsEstimation.TIE_WIRE_ONE_ROLL_KILOGRAM);

	// Create the result bean.
	EstimateResultMRCHB resultMRCHB = new EstimateResultMRCHB(estimateComputationBean, steelBars,
		tieWireKilos, tieWireRolls);

	// Set the result to the estimation object.
	estimateComputationBean.setResultMRCHB(resultMRCHB);
	estimateComputationBean.setQuantitySteelBars(steelBars);
	estimateComputationBean.setQuantityTieWireKilos(tieWireKilos);
	estimateComputationBean.setQuantityTieWireRolls(tieWireRolls);
    }

    /**
     * Estimate the CHB footings.
     * 
     * @param estimateComputationBean
     * @param proportions
     */
    private void estimateMasonryCHBFooting(EstimateComputationBean estimateComputationBean) {

	// Get the dimension key.
	// And the footing mixes.
	TableDimensionCHBFooting chbFooting = estimateComputationBean.getChbFootingDimensions();
	String mixClass = estimateComputationBean.getEstimationClass().getConcreteProportion()
		.getMixClass();

	// Get the footing mixture given the mix class and footing dimensions.
	TableMixtureCHBFooting footingMixture = getCHBFootingMixture(chbFooting, mixClass);

	// Compute for volume.
	EstimateComputationShape shape = estimateComputationBean.getShape();
	double length = shape.getFootingLength();
	double width = shape.getFootingWidth();
	double height = shape.getFootingHeight();
	double footingVolume = height * width * length;

	// Estimations.
	double cement40kg = Math.ceil(footingVolume * footingMixture.getPartCement40kg());
	double cement50kg = Math.ceil(EstimateUtils.convert40kgTo50kg(cement40kg));
	double sand = Math.ceil(footingVolume * footingMixture.getPartSand());
	double gravel = Math.ceil(footingVolume * footingMixture.getPartGravel());

	// Put the results.
	// Set the result map of the CHB footing estimate.
	EstimateResultMasonryCHBFooting footingResults = new EstimateResultMasonryCHBFooting(
		estimateComputationBean, cement40kg, gravel, sand);
	estimateComputationBean.setResultCHBFootingEstimate(footingResults);

	// Update the quantity.
	estimateComputationBean.setQuantityCement40kg(estimateComputationBean.getQuantityCement40kg()
		+ cement40kg);
	estimateComputationBean.setQuantityCement50kg(estimateComputationBean.getQuantityCement50kg()
		+ cement50kg);
	estimateComputationBean.setQuantitySand(estimateComputationBean.getQuantitySand() + sand);
	estimateComputationBean.setQuantityGravel(estimateComputationBean.getQuantityGravel() + gravel);
    }

    /**
     * Get the CHB footing mixture.
     * 
     * @param footingMixes
     * @param dimensionKey
     * @param prop
     * @return
     */
    private TableMixtureCHBFooting getCHBFootingMixture(TableDimensionCHBFooting chbFooting,
	    String mixClass) {

	for (TableMixtureCHBFooting footingMix : TableMixtureCHBFooting.class.getEnumConstants()) {

	    TableDimensionCHBFooting footing = footingMix.getFootingDimensions();
	    String footingClass = footingMix.getMixClass();

	    if (chbFooting == footing && footingClass.equals(mixClass)) {
		return footingMix;
	    }
	}
	return TableMixtureCHBFooting.CLASS_A_15_60;
    }

    /**
     * Add the area of the top side.
     * 
     * @param estimateComputationBean
     * @param estimateComputationShape
     * @param shapeArea
     * @param length
     * @param area
     */
    private double addAreaTopSide(final EstimateComputationShape estimateComputationShape,
	    final double shapeArea, double area) {

	// Get the thickness.
	double thickness = estimateComputationShape.getVolume() / shapeArea;

	// Get the area and add to overall area.
	double topSideArea = estimateComputationShape.getFootingLength() * thickness;
	area += topSideArea;

	return area;
    }

    /**
     * Estimate amount of plastering.
     * 
     * @param estimateComputationBean
     * @param estimateComputationShape
     * @param proportions
     */
    private void estimateMasonryPlastering(EstimateComputationBean estimateComputationBean,
	    EstimateComputationShape estimateComputationShape) {

	// Consider the height below ground.
	// Don't include that to the area to be plastered.
	double shapeArea = estimateComputationShape.getArea();
	double area = shapeArea - estimateComputationBean.getAreaBelowGround();

	// The default is to plaster back to back,
	// multiply the area by 2.
	area = area * 2;

	// Plaster also the top side.
	area = addAreaTopSide(estimateComputationShape, shapeArea, area);

	// Get the volume of the plaster.
	double volume = area * ConstantsEstimation.PLASTER_THICKNESS;

	// Get the appropriate plaster mixture.
	TableMixturePlaster plasterMixture = estimateComputationBean.getEstimationClass()
		.getPlasterMixture();

	// Solve for the needed materials.
	double bags40kg = Math.ceil(volume * plasterMixture.getPartCement40kg());
	double bags50kg = Math.ceil(volume * plasterMixture.getPartCement50kg());
	double sand = Math.ceil(volume * plasterMixture.getPartSand());

	// Set the results.
	EstimateResultMasonryPlastering plasteringResults = new EstimateResultMasonryPlastering(
		estimateComputationBean, bags40kg, bags50kg, sand);
	estimateComputationBean.setResultPlasteringEstimate(plasteringResults);
	estimateComputationBean.setQuantityCement40kg(estimateComputationBean.getQuantityCement40kg()
		+ bags40kg);
	estimateComputationBean.setQuantityCement50kg(estimateComputationBean.getQuantityCement50kg()
		+ bags50kg);
	estimateComputationBean.setQuantitySand(estimateComputationBean.getQuantitySand() + sand);
    }

    /**
     * Estimate the block laying.
     * 
     * @param estimateComputationBean
     * @param estimateComputationShape
     * @param chbList
     */
    private void estimateCHBLaying(EstimateComputationBean estimateComputationBean,
	    EstimateComputationShape estimateComputationShape) {

	// Prepare needed arguments.
	TableDimensionCHB chb = estimateComputationBean.getChbDimensions();
	TableProportionConcrete proportion = estimateComputationBean.getEstimationClass()
		.getConcreteProportion();
	TableMixtureCHBLaying chbLayingMix = getCHBLayingMixture(chb, proportion);

	// Get the inputs.
	double area = estimateComputationShape.getArea();
	double bags40kg = chbLayingMix.getPartCement40kgBag(); // 40kg bags.
	double sand = chbLayingMix.getPartSand(); // Cubic meters.

	// Compute.
	double bags40kgNeeded = Math.ceil(area * bags40kg);
	double bags50kgNeeded = Math.ceil(EstimateUtils.convert40kgTo50kg(bags40kgNeeded));
	double sandNeeded = Math.ceil(area * sand);

	// Set the results.
	EstimateResultMasonryCHBLaying layingResults = new EstimateResultMasonryCHBLaying(
		estimateComputationBean, bags40kgNeeded, sandNeeded);
	estimateComputationBean.setResultCHBLayingEstimate(layingResults);

	// Update the quantity.
	estimateComputationBean.setQuantityCement40kg(estimateComputationBean.getQuantityCement40kg()
		+ bags40kgNeeded);
	estimateComputationBean.setQuantityCement50kg(estimateComputationBean.getQuantityCement50kg()
		+ bags50kgNeeded);
	estimateComputationBean.setQuantitySand(estimateComputationBean.getQuantitySand() + sandNeeded);
    }

    /**
     * Get the CHB laying mixture.
     * 
     * @param chb
     * @param proportion
     * @return
     */
    private TableMixtureCHBLaying getCHBLayingMixture(TableDimensionCHB chb,
	    TableProportionConcrete proportion) {

	String proportionMixClass = proportion.getMixClass();

	// Loop through all block laying mixtures.
	for (TableMixtureCHBLaying mix : TableMixtureCHBLaying.class.getEnumConstants()) {

	    String layingMixClass = mix.getMixClass();
	    TableDimensionCHB chbFromLaying = mix.getChb();

	    // Get correct CHB,
	    // and correct concrete proportion.
	    if (layingMixClass.equals(proportionMixClass) && chbFromLaying == chb) {
		return mix;
	    }
	}
	return TableMixtureCHBLaying.CLASS_A_20_20_40;
    }

    /**
     * Estimate the number of components needed for this concrete.
     * 
     * @param estimateComputationBean
     * @param estimateComputationShape
     */
    private void estimateConcrete(EstimateComputationBean estimateComputationBean,
	    EstimateComputationShape estimateComputationShape) {

	double volume = estimateComputationShape.getVolume();

	TableProportionConcrete tableProportionConcrete = estimateComputationBean.getEstimationClass()
		.getConcreteProportion();

	// Get the ingredients.
	// Now, compute the estimated concrete.
	double cement40kg = tableProportionConcrete.getPartCement40kg();
	double cement50kg = tableProportionConcrete.getPartCement50kg();
	double sand = tableProportionConcrete.getPartSand();
	double gravel = tableProportionConcrete.getPartGravel();

	// Compute.
	double estCement40kg = Math.ceil(volume * cement40kg);
	double estCement50kg = Math.ceil(volume * cement50kg);
	double estSand = Math.ceil(volume * sand);
	double estGravel = Math.ceil(volume * gravel);

	// Set the results.
	EstimateResultConcrete concreteResults = new EstimateResultConcrete(estimateComputationBean,
		estCement40kg, estCement50kg, estSand, estGravel);
	estimateComputationBean.setResultConcreteEstimate(concreteResults);

	// Update the quantity.
	estimateComputationBean.setQuantityCement40kg(estimateComputationBean.getQuantityCement40kg()
		+ estCement40kg);
	estimateComputationBean.setQuantityCement50kg(estimateComputationBean.getQuantityCement50kg()
		+ estCement50kg);
	estimateComputationBean.setQuantitySand(estimateComputationBean.getQuantitySand() + estSand);
	estimateComputationBean.setQuantityGravel(estimateComputationBean.getQuantityGravel()
		+ estGravel);
    }

    /**
     * Get quantity estimation of masonry.
     * 
     * @param estimateComputationBean
     * 
     * @param estimateComputationBean
     * @param estimateComputationShape
     * @param chb
     * @return
     */
    private void estimateCHBTotal(EstimateComputationBean estimateComputationBean,
	    EstimateComputationShape estimateComputationShape) {

	double area = estimateComputationShape.getArea();

	// Get total CHBs.
	double totalCHB = area * TableDimensionCHB.STANDARD_CHB_PER_SQ_M;

	// Results of the estimate.
	EstimateResultMasonryCHB estimateResultMasonryCHB = new EstimateResultMasonryCHB(
		estimateComputationBean, totalCHB);
	estimateComputationBean.setResultCHBEstimate(estimateResultMasonryCHB);
	estimateComputationBean.setQuantityCHB(estimateComputationBean.getQuantityCHB() + totalCHB);
    }

}
