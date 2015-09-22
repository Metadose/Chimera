package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.bean.EstimateComputationBean;
import com.cebedo.pmsys.bean.EstimateComputationInputBean;
import com.cebedo.pmsys.bean.EstimateComputationOutputRowJSON;
import com.cebedo.pmsys.bean.EstimateComputationShape;
import com.cebedo.pmsys.bean.EstimateResultConcrete;
import com.cebedo.pmsys.bean.EstimateResultMRCHB;
import com.cebedo.pmsys.bean.EstimateResultMRIndependentFooting;
import com.cebedo.pmsys.bean.EstimateResultMasonryCHB;
import com.cebedo.pmsys.bean.EstimateResultMasonryCHBFooting;
import com.cebedo.pmsys.bean.EstimateResultMasonryCHBLaying;
import com.cebedo.pmsys.bean.EstimateResultMasonryPlastering;
import com.cebedo.pmsys.constants.ConstantsEstimation;
import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
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
import com.cebedo.pmsys.helper.ValidationHelper;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.repository.EstimationOutputValueRepo;
import com.cebedo.pmsys.service.EstimateService;
import com.cebedo.pmsys.ui.AlertBoxGenerator;
import com.cebedo.pmsys.utils.EstimateUtils;
import com.cebedo.pmsys.validator.EstimateInputValidator;
import com.google.gson.Gson;

@Service
public class EstimateServiceImpl implements EstimateService {

    // Details.
    private static final int EXCEL_DETAILS_QUANTITY = 1;
    private static final int EXCEL_DETAILS_NAME = 2;
    private static final int EXCEL_DETAILS_AREA = 3;
    private static final int EXCEL_DETAILS_VOLUME = 4;

    // Estimate and Remarks.
    private static final int EXCEL_ESTIMATE_MASONRY_CONCRETE = 5;
    private static final int EXCEL_ESTIMATE_MASONRY_CHB = 6;
    private static final int EXCEL_ESTIMATE_MASONRY_CHB_LAYING = 7;
    private static final int EXCEL_ESTIMATE_MASONRY_PLASTERING = 8;
    private static final int EXCEL_ESTIMATE_MASONRY_FOUNDATION_AREA = 9;
    private static final int EXCEL_ESTIMATE_MASONRY_CHB_FOOTING = 10;
    private static final int EXCEL_ESTIMATE_MASONRY_FOOTING_LENGTH = 11;
    private static final int EXCEL_ESTIMATE_MASONRY_FOOTING_WIDTH = 12;
    private static final int EXCEL_ESTIMATE_MASONRY_FOOTING_HEIGHT = 13;
    private static final int EXCEL_ESTIMATE_MR_CHB = 14;
    private static final int EXCEL_ESTIMATE_MR_INDEPENDENT_FOOTING = 15;
    private static final int EXCEL_ESTIMATE_MR_FOOTING_BAR_LENGTH = 16;
    private static final int EXCEL_ESTIMATE_MR_FOOTING_BAR_PER_FOOTING = 17;
    private static final int EXCEL_DETAILS_REMARKS = 18;

    // Cost
    private static final int EXCEL_COST_CHB = 19;
    private static final int EXCEL_COST_CEMENT_40KG = 20;
    private static final int EXCEL_COST_CEMENT_50KG = 21;
    private static final int EXCEL_COST_SAND = 22;
    private static final int EXCEL_COST_GRAVEL = 23;
    private static final int EXCEL_COST_STEEL_BAR = 24;
    private static final int EXCEL_COST_TIE_WIRE_KILOS = 25;
    private static final int EXCEL_COST_TIE_WIRE_ROLLS = 26;

    private MessageHelper messageHelper = new MessageHelper();
    private AuthHelper authHelper = new AuthHelper();
    private ExcelHelper excelHelper = new ExcelHelper();
    private ValidationHelper validationHelper = new ValidationHelper();

    private EstimationOutputValueRepo estimationOutputValueRepo;

    public void setEstimationOutputValueRepo(EstimationOutputValueRepo estimationOutputValueRepo) {
	this.estimationOutputValueRepo = estimationOutputValueRepo;
    }

    @Autowired
    EstimateInputValidator estimateInputValidator;

    @Override
    @Transactional
    public HSSFWorkbook exportXLS(String key) {

	EstimationOutput output = this.estimationOutputValueRepo.get(key);

	// Security check.
	if (!this.authHelper.isActionAuthorized(output)) {
	    this.messageHelper.unauthorized(ConstantsRedis.OBJECT_ESTIMATION_OUTPUT, output.getKey());
	    return new HSSFWorkbook();
	}
	this.messageHelper.send(AuditAction.ACTION_EXPORT, ConstantsRedis.OBJECT_ESTIMATE, key);
	HSSFWorkbook wb = new HSSFWorkbook();

	// Summary sheet.
	constructSheetSummary(wb, output);

	// Inputs.
	constructSheetInputs(wb, output);

	// Concrete.
	constructSheetConcrete(wb, output);

	// CHB Laying.
	constructSheetCHBLaying(wb, output);

	// Plastering.
	constructSheetCHBPlastering(wb, output);

	// CHB (Footing).
	constructSheetCHBFooting(wb, output);

	// Metal Reinforcement (CHB).
	constructSheetMRCHB(wb, output);

	// Metal Reinforcement (Independent Footing).
	constructSheetMRIndependentFooting(wb, output);

	return wb;
    }

    private void constructSheetMRIndependentFooting(HSSFWorkbook wb, EstimationOutput output) {
	// For headers.
	HSSFSheet sheet = wb.createSheet("Metal Rein. (Ind. Footing)");
	int rowIndex = 0;

	// Headers.
	HSSFRow row = sheet.createRow(rowIndex);
	HSSFCell cellQuantity = row.createCell(3);
	HSSFCell cellCost = row.createCell(4);
	CellUtil.setAlignment(cellQuantity, wb, CellStyle.ALIGN_CENTER);
	CellUtil.setAlignment(cellCost, wb, CellStyle.ALIGN_CENTER);
	cellQuantity.setCellValue("Quantity");
	cellCost.setCellValue("Cost");
	rowIndex++;

	// Headers.
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Quantity");
	row.createCell(1).setCellValue("Name");
	row.createCell(2).setCellValue("Steel Bar Length (Meters)");
	row.createCell(3).setCellValue("Steel Bar (Pieces)");
	row.createCell(4).setCellValue("Steel Bar (PHP/Piece)");
	rowIndex++;

	for (EstimateComputationBean computedRow : output.getEstimates()) {
	    EstimateResultMRIndependentFooting estimate = computedRow.getResultMRIndependentFooting();

	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue(computedRow.getQuantity());
	    row.createCell(1).setCellValue(computedRow.getName());
	    row.createCell(2).setCellValue(estimate.getSteelBarLength());
	    row.createCell(3).setCellValue(estimate.getSteelBarsQuantity());
	    row.createCell(4).setCellValue(estimate.getCostSteelBars());
	    rowIndex++;
	}
    }

    private void constructSheetMRCHB(HSSFWorkbook wb, EstimationOutput output) {
	// For headers.
	HSSFSheet sheet = wb.createSheet("Metal Reinforcement (CHB)");
	int rowIndex = 0;

	// Headers.
	HSSFRow row = sheet.createRow(rowIndex);
	HSSFCell cellQuantity = row.createCell(3);
	HSSFCell cellCost = row.createCell(6);
	CellUtil.setAlignment(cellQuantity, wb, CellStyle.ALIGN_CENTER);
	CellUtil.setAlignment(cellCost, wb, CellStyle.ALIGN_CENTER);
	cellQuantity.setCellValue("Quantity");
	cellCost.setCellValue("Cost");
	sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 3, 5));
	sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 6, 8));
	rowIndex++;

	// Headers.
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Quantity");
	row.createCell(1).setCellValue("Name");
	row.createCell(2).setCellValue("Steel Bar Length (Meters)");
	row.createCell(3).setCellValue("Steel Bar (Pieces)");
	row.createCell(4).setCellValue("Tie Wire (Kilos)");
	row.createCell(5).setCellValue("Tie Wire (Rolls)");
	row.createCell(6).setCellValue("Steel Bar (PHP/Piece)");
	row.createCell(7).setCellValue("Tie Wire (PHP/Kilo)");
	row.createCell(8).setCellValue("Tie Wire (PHP/Roll)");
	rowIndex++;

	for (EstimateComputationBean computedRow : output.getEstimates()) {
	    EstimateResultMRCHB estimate = computedRow.getResultMRCHB();

	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue(computedRow.getQuantity());
	    row.createCell(1).setCellValue(computedRow.getName());
	    row.createCell(2).setCellValue(estimate.getSteelBarLength());
	    row.createCell(3).setCellValue(estimate.getSteelBarsQuantity());
	    row.createCell(4).setCellValue(estimate.getTieWireKilos());
	    row.createCell(5).setCellValue(estimate.getTieWireRolls());
	    row.createCell(6).setCellValue(estimate.getCostSteelBars());
	    row.createCell(7).setCellValue(estimate.getCostTieWireKilos());
	    row.createCell(8).setCellValue(estimate.getCostTieWireRolls());
	    rowIndex++;
	}
    }

    private void constructSheetCHBFooting(HSSFWorkbook wb, EstimationOutput output) {
	// For headers.
	HSSFSheet sheet = wb.createSheet("CHB (Footing)");
	int rowIndex = 0;

	// Headers.
	HSSFRow row = sheet.createRow(rowIndex);
	HSSFCell cellQuantity = row.createCell(2);
	HSSFCell cellCost = row.createCell(6);
	CellUtil.setAlignment(cellQuantity, wb, CellStyle.ALIGN_CENTER);
	CellUtil.setAlignment(cellCost, wb, CellStyle.ALIGN_CENTER);
	cellQuantity.setCellValue("Quantity");
	cellCost.setCellValue("Cost");
	sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 5));
	sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 6, 9));
	rowIndex++;

	// Headers.
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Quantity");
	row.createCell(1).setCellValue("Name");
	row.createCell(2).setCellValue("Cement (40kg)");
	row.createCell(3).setCellValue("Cement (50kg)");
	row.createCell(4).setCellValue("Sand (cu.m.)");
	row.createCell(5).setCellValue("Gravel (cu.m.)");
	row.createCell(6).setCellValue("Cement (PHP/40kg)");
	row.createCell(7).setCellValue("Cement (PHP/50kg)");
	row.createCell(8).setCellValue("Sand (PHP/cu.m.)");
	row.createCell(9).setCellValue("Gravel (PHP/cu.m.)");
	rowIndex++;

	for (EstimateComputationBean computedRow : output.getEstimates()) {
	    EstimateResultMasonryCHBFooting estimate = computedRow.getResultCHBFootingEstimate();

	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue(computedRow.getQuantity());
	    row.createCell(1).setCellValue(computedRow.getName());
	    row.createCell(2).setCellValue(estimate.getCement40kg());
	    row.createCell(3).setCellValue(estimate.getCement50kg());
	    row.createCell(4).setCellValue(estimate.getSand());
	    row.createCell(5).setCellValue(estimate.getGravel());
	    row.createCell(6).setCellValue(estimate.getCostCement40kg());
	    row.createCell(7).setCellValue(estimate.getCostCement50kg());
	    row.createCell(8).setCellValue(estimate.getCostSand());
	    row.createCell(9).setCellValue(estimate.getCostGravel());
	    rowIndex++;
	}
    }

    private void constructSheetInputs(HSSFWorkbook wb, EstimationOutput output) {

	// For headers.
	HSSFSheet sheet = wb.createSheet("Input");
	int rowIndex = 0;

	// Headers.
	HSSFRow row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Quantity");
	row.createCell(1).setCellValue("Name");
	row.createCell(2).setCellValue("Remarks");
	row.createCell(3).setCellValue("Area (sq.m.)");
	row.createCell(4).setCellValue("Volume (cu.m.)");
	row.createCell(5).setCellValue("Area Below Ground (sq.m.)");
	row.createCell(6).setCellValue("Footing Length (m)");
	row.createCell(7).setCellValue("Footing Width (m)");
	row.createCell(8).setCellValue("Footing Height (m)");
	rowIndex++;

	for (EstimateComputationBean computedRow : output.getEstimates()) {
	    EstimateComputationShape shape = computedRow.getShape();

	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue(computedRow.getQuantity());
	    row.createCell(1).setCellValue(computedRow.getName());
	    row.createCell(2).setCellValue(computedRow.getRemarks());
	    row.createCell(3).setCellValue(shape.getOriginalArea());
	    row.createCell(4).setCellValue(shape.getOriginalVolume());
	    row.createCell(5).setCellValue(computedRow.getAreaBelowGround());
	    row.createCell(6).setCellValue(shape.getFootingLength());
	    row.createCell(7).setCellValue(shape.getFootingWidth());
	    row.createCell(8).setCellValue(shape.getFootingHeight());
	    rowIndex++;
	}
    }

    /**
     * Construct the CHB sheet.
     * 
     * @param wb
     * @param output
     */
    private void constructSheetCHBPlastering(HSSFWorkbook wb, EstimationOutput output) {

	// For headers.
	HSSFSheet sheet = wb.createSheet("CHB (Plastering)");
	int rowIndex = 0;

	// Headers.
	HSSFRow row = sheet.createRow(rowIndex);
	HSSFCell cellQuantity = row.createCell(2);
	HSSFCell cellCost = row.createCell(5);
	CellUtil.setAlignment(cellQuantity, wb, CellStyle.ALIGN_CENTER);
	CellUtil.setAlignment(cellCost, wb, CellStyle.ALIGN_CENTER);
	cellQuantity.setCellValue("Quantity");
	cellCost.setCellValue("Cost");
	sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 4));
	sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 5, 7));
	rowIndex++;

	// Headers.
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Quantity");
	row.createCell(1).setCellValue("Name");
	row.createCell(2).setCellValue("Cement (40kg)");
	row.createCell(3).setCellValue("Cement (50kg)");
	row.createCell(4).setCellValue("Sand (cu.m.)");
	row.createCell(5).setCellValue("Cement (PHP/40kg)");
	row.createCell(6).setCellValue("Cement (PHP/50kg)");
	row.createCell(7).setCellValue("Sand (PHP/cu.m.)");
	rowIndex++;

	for (EstimateComputationBean computedRow : output.getEstimates()) {
	    EstimateResultMasonryPlastering estimate = computedRow.getResultPlasteringEstimate();

	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue(computedRow.getQuantity());
	    row.createCell(1).setCellValue(computedRow.getName());
	    row.createCell(2).setCellValue(estimate.getCement40kg());
	    row.createCell(3).setCellValue(estimate.getCement50kg());
	    row.createCell(4).setCellValue(estimate.getSand());
	    row.createCell(5).setCellValue(estimate.getCostCement40kg());
	    row.createCell(6).setCellValue(estimate.getCostCement50kg());
	    row.createCell(7).setCellValue(estimate.getCostSand());
	    rowIndex++;
	}
    }

    /**
     * Construct the CHB sheet.
     * 
     * @param wb
     * @param output
     */
    private void constructSheetCHBLaying(HSSFWorkbook wb, EstimationOutput output) {

	// For headers.
	HSSFSheet sheet = wb.createSheet("CHB (Setting-Laying)");
	int rowIndex = 0;

	// Headers.
	HSSFRow row = sheet.createRow(rowIndex);
	HSSFCell cellQuantity = row.createCell(2);
	HSSFCell cellCost = row.createCell(6);
	CellUtil.setAlignment(cellQuantity, wb, CellStyle.ALIGN_CENTER);
	CellUtil.setAlignment(cellCost, wb, CellStyle.ALIGN_CENTER);
	cellQuantity.setCellValue("Quantity");
	cellCost.setCellValue("Cost");
	sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 5));
	sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 6, 9));
	rowIndex++;

	// Headers.
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Quantity");
	row.createCell(1).setCellValue("Name");
	row.createCell(2).setCellValue("CHB (Pieces)");
	row.createCell(3).setCellValue("Cement (40kg)");
	row.createCell(4).setCellValue("Cement (50kg)");
	row.createCell(5).setCellValue("Sand (cu.m.)");
	row.createCell(6).setCellValue("CHB (PHP/Piece)");
	row.createCell(7).setCellValue("Cement (PHP/40kg)");
	row.createCell(8).setCellValue("Cement (PHP/50kg)");
	row.createCell(9).setCellValue("Sand (PHP/cu.m.)");
	rowIndex++;

	for (EstimateComputationBean computedRow : output.getEstimates()) {
	    EstimateResultMasonryCHB estimate = computedRow.getResultCHBEstimate();
	    EstimateResultMasonryCHBLaying chbLaying = computedRow.getResultCHBLayingEstimate();
	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue(computedRow.getQuantity());
	    row.createCell(1).setCellValue(computedRow.getName());
	    row.createCell(2).setCellValue(estimate.getTotalCHB());
	    row.createCell(3).setCellValue(chbLaying.getCement40kg());
	    row.createCell(4).setCellValue(chbLaying.getCement50kg());
	    row.createCell(5).setCellValue(chbLaying.getSand());
	    row.createCell(6).setCellValue(estimate.getCostCHB());
	    row.createCell(7).setCellValue(chbLaying.getCostCement40kg());
	    row.createCell(8).setCellValue(chbLaying.getCostCement50kg());
	    row.createCell(9).setCellValue(chbLaying.getCostSand());
	    rowIndex++;
	}
    }

    /**
     * Construct the concrete sheet.
     * 
     * @param wb
     * @param output
     */
    private void constructSheetConcrete(HSSFWorkbook wb, EstimationOutput output) {

	// For headers.
	HSSFSheet sheet = wb.createSheet("Concrete");
	int rowIndex = 0;

	// Headers.
	HSSFRow row = sheet.createRow(rowIndex);
	HSSFCell cellQuantity = row.createCell(2);
	HSSFCell cellCost = row.createCell(6);
	CellUtil.setAlignment(cellQuantity, wb, CellStyle.ALIGN_CENTER);
	CellUtil.setAlignment(cellCost, wb, CellStyle.ALIGN_CENTER);
	cellQuantity.setCellValue("Quantity");
	cellCost.setCellValue("Cost");
	sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 5));
	sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 6, 9));
	rowIndex++;

	// Headers.
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Quantity");
	row.createCell(1).setCellValue("Name");
	row.createCell(2).setCellValue("Cement (40kg)");
	row.createCell(3).setCellValue("Cement (50kg)");
	row.createCell(4).setCellValue("Sand (cu.m.)");
	row.createCell(5).setCellValue("Gravel (cu.m.)");
	row.createCell(6).setCellValue("Cement (PHP/40kg)");
	row.createCell(7).setCellValue("Cement (PHP/50kg)");
	row.createCell(8).setCellValue("Sand (PHP/cu.m.)");
	row.createCell(9).setCellValue("Gravel (PHP/cu.m.)");
	rowIndex++;

	for (EstimateComputationBean computedRow : output.getEstimates()) {
	    EstimateResultConcrete concreteEstimate = computedRow.getResultConcreteEstimate();
	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue(computedRow.getQuantity());
	    row.createCell(1).setCellValue(computedRow.getName());
	    row.createCell(2).setCellValue(concreteEstimate.getCement40kg());
	    row.createCell(3).setCellValue(concreteEstimate.getCement50kg());
	    row.createCell(4).setCellValue(concreteEstimate.getSand());
	    row.createCell(5).setCellValue(concreteEstimate.getGravel());
	    row.createCell(6).setCellValue(concreteEstimate.getCostCement40kg());
	    row.createCell(7).setCellValue(concreteEstimate.getCostCement50kg());
	    row.createCell(8).setCellValue(concreteEstimate.getCostSand());
	    row.createCell(9).setCellValue(concreteEstimate.getCostGravel());
	    rowIndex++;
	}
    }

    /**
     * Construct the summary sheet.
     * 
     * @param wb
     * @param output
     */
    private void constructSheetSummary(HSSFWorkbook wb, EstimationOutput output) {
	// For headers.
	HSSFSheet sheet = wb.createSheet(output.getName());
	int rowIndex = 0;

	// Grand total.
	HSSFRow row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Grand Total");
	row.createCell(1).setCellValue(output.getCostGrandTotal());
	rowIndex++;
	rowIndex++;

	// Headers.
	// Create a cell and put a value in it.
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Material");
	row.createCell(1).setCellValue("Quantity");
	row.createCell(2).setCellValue("Cost (PHP/unit)");
	rowIndex++;

	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Concrete Hollow Blocks (CHB)");
	row.createCell(1).setCellValue(output.getQuantityCHB());
	row.createCell(2).setCellValue(output.getCostCHB());
	rowIndex++;

	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Cement (if buying 40kg)");
	row.createCell(1).setCellValue(output.getQuantityCement40kg());
	row.createCell(2).setCellValue(output.getCostCement40kg());
	rowIndex++;

	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Cement (if buying 50kg)");
	row.createCell(1).setCellValue(output.getQuantityCement50kg());
	row.createCell(2).setCellValue(output.getCostCement50kg());
	rowIndex++;

	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Sand (cu.m.)");
	row.createCell(1).setCellValue(output.getQuantitySand());
	row.createCell(2).setCellValue(output.getCostSand());
	rowIndex++;

	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Gravel (cu.m.)");
	row.createCell(1).setCellValue(output.getQuantityGravel());
	row.createCell(2).setCellValue(output.getCostGravel());
	rowIndex++;

	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Steel Bars Total");
	row.createCell(1).setCellValue(output.getQuantitySteelBars());
	row.createCell(2).setCellValue(output.getCostSteelBars());
	rowIndex++;

	Map<Double, Double> steelBarLenToQty = output.getSteelBarLenToQty();
	for (double len : steelBarLenToQty.keySet()) {
	    double qty = steelBarLenToQty.get(len);
	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue(String.format("- Steel Bars (%s-meter length)", len));
	    row.createCell(1).setCellValue(qty);
	    rowIndex++;
	}

	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Tie Wire (if buying per Kilo)");
	row.createCell(1).setCellValue(output.getQuantityTieWireKilos());
	row.createCell(2).setCellValue(output.getCostTieWireKilos());
	rowIndex++;

	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue("Tie Wire (if buying per Roll)");
	row.createCell(1).setCellValue(output.getQuantityTieWireRolls());
	row.createCell(2).setCellValue(output.getCostTieWireRolls());
    }

    @Override
    @Transactional
    public String estimate(EstimateComputationInputBean estimateInput, BindingResult result) {

	// Security check.
	Project proj = estimateInput.getProject();
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Service layer form validation.
	this.estimateInputValidator.validate(estimateInput, result);
	if (result.hasErrors()) {
	    return this.validationHelper.errorMessageHTML(result);
	}

	// Log.
	this.messageHelper.send(AuditAction.ACTION_ESTIMATE, Project.OBJECT_NAME, proj.getId(),
		EstimateComputationInputBean.class.getName());

	// New object.
	EstimationOutput estimationOutput = new EstimationOutput(estimateInput);

	// Validation is in this function convertExcelToEstimates(...)
	// Convert the excel file to objects.
	List<EstimateComputationBean> estimateComputationBeans = convertExcelToEstimates(
		estimateInput.getEstimationFile(), estimateInput.getProject());

	// Conversion failed.
	if (estimateComputationBeans == null) {
	    return AlertBoxGenerator.FAILED
		    .generateHTML(RegistryResponseMessage.ERROR_COMMON_FILE_CORRUPT_INVALID);
	}

	// Process each object.
	List<EstimateComputationOutputRowJSON> rowListForJSON = new ArrayList<EstimateComputationOutputRowJSON>();
	for (EstimateComputationBean estimateComputationBean : estimateComputationBeans) {

	    // Set allowance.
	    estimateComputationBean.setEstimationAllowance(estimateInput.getEstimationAllowance());

	    // For each row, compute the total quantity.
	    // For each type in the row, compute the cost.
	    computeRowQuantityAndPerTypeCost(estimationOutput, estimateComputationBean);

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

	return AlertBoxGenerator.SUCCESS.generateCreate(ConstantsRedis.OBJECT_ESTIMATE,
		estimateInput.getName());
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
	EstimateResultMRIndependentFooting mrIndieFooting = estimateComputationBean
		.getResultMRIndependentFooting();

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

	// Metal reinforcement (Independent Footing).
	costSteelBar += mrIndieFooting.getCostSteelBars();

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
	try {
	    String concrete = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
		    : this.excelHelper.getValueAsExpected(workbook, cell));
	    if (concrete == null || concrete.isEmpty()) {
		return false;
	    }
	    return StringUtils.deleteWhitespace(concrete).equalsIgnoreCase("Yes") ? true : false;
	} catch (Exception e) {
	    return false;
	}
    }

    /**
     * Convert Excel to a list of Estimates.
     * 
     * @param multipartFile
     * @return
     */
    private List<EstimateComputationBean> convertExcelToEstimates(MultipartFile multipartFile,
	    Project proj) {

	// Service layer form validation.
	boolean valid = this.validationHelper.fileIsNotNullOrEmpty(multipartFile);
	if (!valid) {
	    return null;
	}

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

		    case EXCEL_DETAILS_QUANTITY:
			// If quantity is null (blank), set to 1.
			double qty = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 1 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (qty < 0) {
			    return null;
			}
			estimateComputationBean.setQuantity(qty);
			continue;

		    case EXCEL_DETAILS_NAME:
			String name = (String) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? ""
					: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationBean.setName(name);
			continue;

		    case EXCEL_DETAILS_AREA:
			double area = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (area < 0) {
			    return null;
			}
			estimateComputationShape.setArea(area);
			estimateComputationShape.setOriginalArea(area);
			estimateComputationBean.setShape(estimateComputationShape);
			continue;

		    case EXCEL_DETAILS_VOLUME:
			double volume = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (volume < 0) {
			    return null;
			}
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
			if (foundationArea < 0) {
			    return null;
			}
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
			if (footingLength < 0) {
			    return null;
			}
			estimateComputationShape.setFootingLength(footingLength);
			estimateComputationBean.setShape(estimateComputationShape);
			continue;

		    case EXCEL_ESTIMATE_MASONRY_FOOTING_WIDTH:
			double footingWidth = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (footingWidth < 0) {
			    return null;
			}
			estimateComputationShape.setFootingWidth(footingWidth);
			estimateComputationBean.setShape(estimateComputationShape);
			continue;

		    case EXCEL_ESTIMATE_MASONRY_FOOTING_HEIGHT:
			double footingHeight = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (footingHeight < 0) {
			    return null;
			}
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

		    case EXCEL_ESTIMATE_MR_INDEPENDENT_FOOTING:
			boolean mrIndependentFooting = getEstimateBooleanFromExcel(workbook, cell);
			if (mrIndependentFooting) {
			    estimateTypes.add(EstimateType.METAL_REINFORCEMENT_INDEPENDENT_FOOTING);
			    estimateComputationBean.setEstimateTypes(estimateTypes);
			}
			continue;

		    case EXCEL_ESTIMATE_MR_FOOTING_BAR_LENGTH:
			double mrBarLength = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (mrBarLength < 0) {
			    return null;
			}
			estimateComputationShape.setFootingBarLength(mrBarLength);
			estimateComputationBean.setShape(estimateComputationShape);
			continue;

		    case EXCEL_ESTIMATE_MR_FOOTING_BAR_PER_FOOTING:
			double mrBarsPerFooting = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (mrBarsPerFooting < 0) {
			    return null;
			}
			estimateComputationShape.setFootingNumberOfBars(mrBarsPerFooting);
			estimateComputationBean.setShape(estimateComputationShape);
			continue;

		    case EXCEL_DETAILS_REMARKS:
			String remarks = (String) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? ""
					: this.excelHelper.getValueAsExpected(workbook, cell));
			estimateComputationBean.setRemarks(remarks);
			continue;

		    case EXCEL_COST_CHB:
			costCHB = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null
				? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (costCHB < 0) {
			    return null;
			}
			estimateComputationBean.setCostPerUnitCHB(costCHB);
			continue;

		    case EXCEL_COST_CEMENT_40KG:
			costCement40kg = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (costCement40kg < 0) {
			    return null;
			}
			estimateComputationBean.setCostPerUnitCement40kg(costCement40kg);
			continue;

		    case EXCEL_COST_CEMENT_50KG:
			costCement50kg = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (costCement50kg < 0) {
			    return null;
			}
			estimateComputationBean.setCostPerUnitCement50kg(costCement50kg);
			continue;

		    case EXCEL_COST_SAND:
			costSand = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null
				? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (costSand < 0) {
			    return null;
			}
			estimateComputationBean.setCostPerUnitSand(costSand);
			continue;

		    case EXCEL_COST_GRAVEL:
			costGravel = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (costGravel < 0) {
			    return null;
			}
			estimateComputationBean.setCostPerUnitGravel(costGravel);
			continue;

		    case EXCEL_COST_STEEL_BAR:
			costSteelBars = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (costSteelBars < 0) {
			    return null;
			}
			estimateComputationBean.setCostPerUnitSteelBars(costSteelBars);
			continue;

		    case EXCEL_COST_TIE_WIRE_KILOS:
			costTieWireKilos = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (costTieWireKilos < 0) {
			    return null;
			}
			estimateComputationBean.setCostPerUnitTieWireKilos(costTieWireKilos);
			continue;

		    case EXCEL_COST_TIE_WIRE_ROLLS:
			costTieWireRoll = (Double) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? 0 : this.excelHelper.getValueAsExpected(workbook, cell));
			if (costTieWireRoll < 0) {
			    return null;
			}
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
	return null;
    }

    /**
     * Estimate for:<br>
     * 1) Quantity: Each Estimation type.<br>
     * 2) Quantity: The whole row.<br>
     * 3) Cost: Each Estimation type.
     * 
     * @param estimationOutput
     */
    private void computeRowQuantityAndPerTypeCost(EstimationOutput estimationOutput,
	    EstimateComputationBean estimateComputationBean) {

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
	    estimateMRCHB(estimationOutput, estimateComputationBean);
	}

	// If we're estimating metal reinforcement for an independent footing.
	if (estimateComputationBean.willComputeMRIndependentFooting()) {
	    estimateMRIndependentFooting(estimationOutput, estimateComputationBean);
	}

    }

    /**
     * Estimating metal reinforcement for an independent footing.
     * 
     * @param estimationOutput
     * @param estimateComputationBean
     */
    private void estimateMRIndependentFooting(EstimationOutput estimationOutput,
	    EstimateComputationBean estimateComputationBean) {

	EstimateComputationShape shape = estimateComputationBean.getShape();
	double barLength = shape.getFootingBarLength();
	double footingLength = shape.getFootingLength();

	// If bar length is not defined, set it.
	if (barLength == 0) {
	    barLength = footingLength
		    - (2 * ConstantsEstimation.METAL_REINFORCEMENT_MINIMUM_UNDERGROUND_COVER);
	}

	// How many bars in one footing?
	double barsPerFooting = shape.getFootingNumberOfBars();

	// Total number of bars in all footings.
	double allBars = barsPerFooting * estimateComputationBean.getQuantity();

	// Length of steel bar to buy.
	Double lengthToUse = null;
	Long intPartToUse = null;
	Double leastFraction = null;

	for (double steelBarLength : ConstantsEstimation.STEEL_BAR_LENGTHS) {
	    double rawComputedSteelBars = steelBarLength / barLength;
	    long intPart = (long) rawComputedSteelBars;
	    double fractionPart = rawComputedSteelBars - intPart;

	    // If not yet initialized, initialize now.
	    if (leastFraction == null) {
		leastFraction = fractionPart;
		intPartToUse = intPart;
		lengthToUse = steelBarLength;
	    }
	    // Else, compare.
	    // If new part is lower than least,
	    // assign it as new least.
	    else if (fractionPart < leastFraction) {
		leastFraction = fractionPart;
		intPartToUse = intPart;
		lengthToUse = steelBarLength;
	    }
	}

	// Number of bars to buy at lengthToUse length.
	double estBarsToBuy = Math.ceil(allBars / intPartToUse);

	// Put the estimated number of bars to buy to map.
	putSteelBarsToMap(estimationOutput, lengthToUse, estBarsToBuy);

	// Set the results.
	EstimateResultMRIndependentFooting resultIndependentFooting = new EstimateResultMRIndependentFooting(
		estimateComputationBean, estBarsToBuy, lengthToUse);
	estimateComputationBean.setResultMRIndependentFooting(resultIndependentFooting);
	estimateComputationBean
		.setQuantitySteelBars(estimateComputationBean.getQuantitySteelBars() + estBarsToBuy);
    }

    /**
     * Put the estimated number of bars to buy to map.
     * 
     * @param estimationOutput
     * @param lengthToUse
     * @param estBarsToBuy
     */
    private void putSteelBarsToMap(EstimationOutput estimationOutput, Double lengthToUse,
	    double estBarsToBuy) {
	Map<Double, Double> lengthToQuantityMap = estimationOutput.getSteelBarLenToQty();

	// Number of steel bars to buy, given the length of steel bar.
	// If first time to be declared.
	if (lengthToQuantityMap == null) {
	    Map<Double, Double> lenToQty = new HashMap<Double, Double>();
	    lenToQty.put(lengthToUse, estBarsToBuy);
	    estimationOutput.setSteelBarLenToQty(lenToQty);
	} else {
	    // If this length has not been registered yet,
	    // register with given quantity.
	    Double newQuantity = lengthToQuantityMap.get(lengthToUse) == null ? estBarsToBuy
		    : lengthToQuantityMap.get(lengthToUse) + estBarsToBuy;
	    lengthToQuantityMap.put(lengthToUse, newQuantity);
	    estimationOutput.setSteelBarLenToQty(lengthToQuantityMap);
	}
    }

    /**
     * Estimate steel bars and tie wires for CHB metal reinforcement.
     * 
     * @param estimationOutput
     * 
     * @param estimateComputationBean
     */
    private void estimateMRCHB(EstimationOutput estimationOutput,
	    EstimateComputationBean estimateComputationBean) {
	EstimateComputationShape shape = estimateComputationBean.getShape();
	TableMRCHBVertical mrVertical = estimateComputationBean.getMrCHBVertical();
	TableMRCHBHorizontal mrHorizontal = estimateComputationBean.getMrCHBHorizontal();

	double area = shape.getArea();
	double verticalMR = area * mrVertical.getPerSqMeter();
	double horizontalMR = area * mrHorizontal.getPerSqMeter();
	double totalMRLength = verticalMR + horizontalMR;

	// Number of steel bars to buy.
	Double lengthToUse = null;
	Double leastFraction = null;

	for (double steelBarLength : ConstantsEstimation.STEEL_BAR_LENGTHS) {
	    double rawComputedSteelBars = totalMRLength / steelBarLength;
	    long intPart = (long) rawComputedSteelBars;
	    double fractionPart = rawComputedSteelBars - intPart;

	    // If not yet initialized, initialize now.
	    if (leastFraction == null) {
		leastFraction = fractionPart;
		lengthToUse = steelBarLength;
	    }
	    // Else, compare.
	    // If new part is lower than least,
	    // assign it as new least.
	    else if (fractionPart < leastFraction) {
		leastFraction = fractionPart;
		lengthToUse = steelBarLength;
	    }
	}

	Map<Double, Double> lengthToQuantityMap = estimationOutput.getSteelBarLenToQty();

	// Number of steel bars to buy, given the length of steel bar.
	double quantity = estimateComputationBean.getQuantity();
	double estSteelBars = Math.ceil(totalMRLength / lengthToUse) * quantity;

	// If first time to be declared.
	if (lengthToQuantityMap == null) {
	    Map<Double, Double> lenToQty = new HashMap<Double, Double>();
	    lenToQty.put(lengthToUse, estSteelBars);
	    estimationOutput.setSteelBarLenToQty(lenToQty);
	} else {
	    // If this length has not been registered yet,
	    // register with given quantity.
	    Double newQuantity = lengthToQuantityMap.get(lengthToUse) == null ? estSteelBars
		    : lengthToQuantityMap.get(lengthToUse) + estSteelBars;
	    lengthToQuantityMap.put(lengthToUse, newQuantity);
	    estimationOutput.setSteelBarLenToQty(lengthToQuantityMap);
	}

	TableMRCHBTieWire tieWireTable = estimateComputationBean.getMrCHBTieWire();
	double kgPerSqMeter = tieWireTable.getKgPerSqMeter();

	// Number of tie wire kilos to buy.
	double estTieWireKilos = Math.ceil(area * kgPerSqMeter) * quantity;

	// Number of tie wire rolls to buy.
	double estTieWireRolls = Math
		.ceil(estTieWireKilos / ConstantsEstimation.TIE_WIRE_ONE_ROLL_KILOGRAM);

	// Create the result bean.
	EstimateResultMRCHB resultMRCHB = new EstimateResultMRCHB(estimateComputationBean, estSteelBars,
		estTieWireKilos, estTieWireRolls, lengthToUse);

	// Set the result to the estimation object.
	estimateComputationBean.setResultMRCHB(resultMRCHB);
	estimateComputationBean
		.setQuantitySteelBars(estimateComputationBean.getQuantitySteelBars() + estSteelBars);
	estimateComputationBean.setQuantityTieWireKilos(estTieWireKilos);
	estimateComputationBean.setQuantityTieWireRolls(estTieWireRolls);
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
	double quantity = estimateComputationBean.getQuantity();
	double estCement40kg = Math.ceil(footingVolume * footingMixture.getPartCement40kg()) * quantity;
	double estCement50kg = Math.ceil(EstimateUtils.convert40kgTo50kg(estCement40kg));
	double estSand = Math.ceil(footingVolume * footingMixture.getPartSand()) * quantity;
	double estGravel = Math.ceil(footingVolume * footingMixture.getPartGravel()) * quantity;

	// Put the results.
	// Set the result map of the CHB footing estimate.
	EstimateResultMasonryCHBFooting footingResults = new EstimateResultMasonryCHBFooting(
		estimateComputationBean, estCement40kg, estGravel, estSand);
	estimateComputationBean.setResultCHBFootingEstimate(footingResults);

	// Update the quantity.
	estimateComputationBean
		.setQuantityCement40kg(estimateComputationBean.getQuantityCement40kg() + estCement40kg);
	estimateComputationBean
		.setQuantityCement50kg(estimateComputationBean.getQuantityCement50kg() + estCement50kg);
	estimateComputationBean.setQuantitySand(estimateComputationBean.getQuantitySand() + estSand);
	estimateComputationBean
		.setQuantityGravel(estimateComputationBean.getQuantityGravel() + estGravel);
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
	double quantity = estimateComputationBean.getQuantity();
	double estBags40kg = Math.ceil(volume * plasterMixture.getPartCement40kg()) * quantity;
	double estBags50kg = Math.ceil(volume * plasterMixture.getPartCement50kg()) * quantity;
	double estSand = Math.ceil(volume * plasterMixture.getPartSand()) * quantity;

	// Set the results.
	EstimateResultMasonryPlastering plasteringResults = new EstimateResultMasonryPlastering(
		estimateComputationBean, estBags40kg, estBags50kg, estSand);
	estimateComputationBean.setResultPlasteringEstimate(plasteringResults);
	estimateComputationBean
		.setQuantityCement40kg(estimateComputationBean.getQuantityCement40kg() + estBags40kg);
	estimateComputationBean
		.setQuantityCement50kg(estimateComputationBean.getQuantityCement50kg() + estBags50kg);
	estimateComputationBean.setQuantitySand(estimateComputationBean.getQuantitySand() + estSand);
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
	double quantity = estimateComputationBean.getQuantity();
	double estBags40kg = Math.ceil(area * bags40kg) * quantity;
	double estBags50kg = Math.ceil(EstimateUtils.convert40kgTo50kg(estBags40kg));
	double estSand = Math.ceil(area * sand) * quantity;

	// Set the results.
	EstimateResultMasonryCHBLaying layingResults = new EstimateResultMasonryCHBLaying(
		estimateComputationBean, estBags40kg, estSand);
	estimateComputationBean.setResultCHBLayingEstimate(layingResults);

	// Update the quantity.
	estimateComputationBean
		.setQuantityCement40kg(estimateComputationBean.getQuantityCement40kg() + estBags40kg);
	estimateComputationBean
		.setQuantityCement50kg(estimateComputationBean.getQuantityCement50kg() + estBags50kg);
	estimateComputationBean.setQuantitySand(estimateComputationBean.getQuantitySand() + estSand);
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
	double quantity = estimateComputationBean.getQuantity();
	double estCement40kg = Math.ceil(volume * cement40kg) * quantity;
	double estCement50kg = Math.ceil(volume * cement50kg) * quantity;
	double estSand = Math.ceil(volume * sand) * quantity;
	double estGravel = Math.ceil(volume * gravel) * quantity;

	// Set the results.
	EstimateResultConcrete concreteResults = new EstimateResultConcrete(estimateComputationBean,
		estCement40kg, estCement50kg, estSand, estGravel);
	estimateComputationBean.setResultConcreteEstimate(concreteResults);

	// Update the quantity.
	estimateComputationBean
		.setQuantityCement40kg(estimateComputationBean.getQuantityCement40kg() + estCement40kg);
	estimateComputationBean
		.setQuantityCement50kg(estimateComputationBean.getQuantityCement50kg() + estCement50kg);
	estimateComputationBean.setQuantitySand(estimateComputationBean.getQuantitySand() + estSand);
	estimateComputationBean
		.setQuantityGravel(estimateComputationBean.getQuantityGravel() + estGravel);
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
	double quantity = estimateComputationBean.getQuantity();
	double totalCHB = area * TableDimensionCHB.STANDARD_CHB_PER_SQ_M * quantity;

	// Results of the estimate.
	EstimateResultMasonryCHB estimateResultMasonryCHB = new EstimateResultMasonryCHB(
		estimateComputationBean, totalCHB);
	estimateComputationBean.setResultCHBEstimate(estimateResultMasonryCHB);
	estimateComputationBean.setQuantityCHB(estimateComputationBean.getQuantityCHB() + totalCHB);
    }

}
