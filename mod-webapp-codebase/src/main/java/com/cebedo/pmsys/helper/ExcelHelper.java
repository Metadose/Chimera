package com.cebedo.pmsys.helper;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.cebedo.pmsys.domain.AbstractExpense;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.domain.IExpense;
import com.cebedo.pmsys.enums.EstimateCostType;
import com.google.common.collect.ImmutableList;

public class ExcelHelper {

    public int addSectionLabel(String label, HSSFSheet sheet, int rowIndex, IndexedColors color,
	    Object... moreValues) {
	// Create the style.
	HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
	style.setFillForegroundColor(color.getIndex());
	style.setFillPattern(CellStyle.SOLID_FOREGROUND);

	// Create the label cell.
	HSSFRow row = sheet.createRow(rowIndex);
	row.createCell(0).setCellStyle(style);
	row.getCell(0).setCellValue(label);
	int cellIndex = 1;
	for (Object additionalVal : moreValues) {
	    row.createCell(cellIndex).setCellValue(String.valueOf(additionalVal));
	    cellIndex++;
	}
	rowIndex++;
	return rowIndex;
    }

    public int addSectionLabel(String label, HSSFSheet sheet, int rowIndex, Object... moreValues) {
	// Create the style.
	HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
	style.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
	style.setFillPattern(CellStyle.SOLID_FOREGROUND);

	// Create the label cell.
	HSSFRow row = sheet.createRow(rowIndex);
	row.createCell(0).setCellStyle(style);
	row.getCell(0).setCellValue(label);
	int cellIndex = 1;
	for (Object additionalVal : moreValues) {
	    row.createCell(cellIndex).setCellValue(String.valueOf(additionalVal));
	    cellIndex++;
	}
	rowIndex++;
	return rowIndex;
    }

    public int addSectionFromMap(String sectionName, HSSFRow row, HSSFSheet sheet, int rowIndex,
	    Map<String, Double> labelValueMap) {
	rowIndex = addSectionLabel(sectionName, sheet, rowIndex);
	for (String key : labelValueMap.keySet()) {
	    row = sheet.createRow(rowIndex);
	    row.createCell(0).setCellValue(key);
	    row.createCell(1).setCellValue(labelValueMap.get(key));
	    rowIndex++;
	}
	rowIndex++;
	return rowIndex;
    }

    /**
     * Create a section for the list of expenses.
     * 
     * @param sectionName
     * @param row
     * @param sheet
     * @param rowIndex
     * @param expenses
     * @return
     */
    public int addSectionExpenses(String sectionName, HSSFRow row, HSSFSheet sheet, int rowIndex,
	    ImmutableList<IExpense> expenses) {
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue(sectionName);
	rowIndex++;
	for (IExpense expense : expenses) {
	    row = sheet.createRow(rowIndex);
	    row.createCell(1).setCellValue(expense.getName());
	    row.createCell(2).setCellValue(expense.getCost());
	    row.createCell(3).setCellValue(StringUtils.capitalize(expense.getObjectName()));
	    rowIndex++;
	}
	rowIndex++;
	return rowIndex;
    }

    public int addSectionEstimates(ImmutableList<Entry<EstimateCost, Double>> list, HSSFRow row,
	    HSSFSheet sheet, int rowIndex, int subType) {
	for (Entry<EstimateCost, Double> entry : list) {
	    EstimateCost cost = entry.getKey();
	    double value = entry.getValue();
	    if (subType == EstimateCostType.SUB_TYPE_DIFFERENCE
		    || subType == EstimateCostType.SUB_TYPE_ABSOLUTE) {
		rowIndex = addRow(cost.getName(), value, row, sheet, rowIndex,
			subType == EstimateCostType.SUB_TYPE_DIFFERENCE ? "Difference" : "Absolute",
			cost.getCostType().getLabel());
		continue;
	    }
	    rowIndex = addRow(cost.getName(), value, row, sheet, rowIndex,
		    subType == EstimateCostType.SUB_TYPE_PLANNED ? "Estimated" : "Actual",
		    cost.getCostType().getLabel());
	}
	return rowIndex;
    }

    public int addSectionEstimates(List<EstimateCost> list, HSSFRow row, HSSFSheet sheet, int rowIndex,
	    int subType) {
	for (EstimateCost cost : list) {
	    rowIndex = addRow(cost.getName(),
		    subType == EstimateCostType.SUB_TYPE_PLANNED ? cost.getCost() : cost.getActualCost(),
		    row, sheet, rowIndex,
		    subType == EstimateCostType.SUB_TYPE_PLANNED ? "Estimated" : "Actual",
		    cost.getCostType().getLabel());
	}
	return rowIndex;
    }

    public int addRow(String key, Object value, HSSFRow row, HSSFSheet sheet, int rowIndex,
	    Object... moreValues) {
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue(key);
	row.createCell(1).setCellValue(String.valueOf(value));

	int cellIndex = 2;
	for (Object additionalVal : moreValues) {
	    row.createCell(cellIndex).setCellValue(String.valueOf(additionalVal));
	    cellIndex++;
	}
	rowIndex++;
	return rowIndex;
    }

    /**
     * Create a section for the list of expenses.
     * 
     * @param sectionName
     * @param row
     * @param sheet
     * @param rowIndex
     * @param expenses
     * @return
     */
    public int addSectionExpenses(String sectionName, HSSFRow row, HSSFSheet sheet, int rowIndex,
	    List<? extends AbstractExpense> expenses) {
	row = sheet.createRow(rowIndex);
	row.createCell(0).setCellValue(sectionName);
	rowIndex++;
	for (AbstractExpense expense : expenses) {
	    row = sheet.createRow(rowIndex);
	    row.createCell(1).setCellValue(expense.getName());
	    row.createCell(2).setCellValue(expense.getCost());
	    rowIndex++;
	}
	rowIndex++;
	return rowIndex;
    }

    /**
     * Get value as the expected object to avoid exceptions.
     * 
     * @param cell
     * @return
     */
    public Object getValueAsExpected(HSSFWorkbook workbook, Cell cell) {

	try {

	    // Evaluate the cell.
	    // Get the value of the cell.
	    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	    CellValue cellValue = evaluator.evaluate(cell);
	    if (cellValue == null) {
		return null;
	    }

	    // Handle each case.
	    switch (cellValue.getCellType()) {

	    case Cell.CELL_TYPE_NUMERIC:
		return cellValue.getNumberValue();

	    case Cell.CELL_TYPE_STRING:
		return StringUtils.trim(cellValue.getStringValue());

	    case Cell.CELL_TYPE_BOOLEAN:
		return cellValue.getBooleanValue();

	    case Cell.CELL_TYPE_BLANK:
		return null;

	    case Cell.CELL_TYPE_ERROR:
		return null;

	    case Cell.CELL_TYPE_FORMULA:
		// CELL_TYPE_FORMULA will never happen
		// since it's already evaluated.
		return null;
	    }
	    return null;
	} catch (Exception e) {
	    return null;
	}
    }

}
