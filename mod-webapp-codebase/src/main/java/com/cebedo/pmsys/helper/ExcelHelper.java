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

    /**
     * Add a label.
     * 
     * @param label
     * @param sheet
     * @param rowIndex
     * @param color
     * @param moreValues
     * @return
     */
    public int addLabel(String label, HSSFSheet sheet, int rowIndex, IndexedColors color,
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

    /**
     * Add a label.
     * 
     * @param label
     * @param sheet
     * @param rowIndex
     * @param moreValues
     * @return
     */
    public int addLabel(String label, HSSFSheet sheet, int rowIndex, Object... moreValues) {

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

    /**
     * Add a section.
     * 
     * @param sectionName
     * @param row
     * @param sheet
     * @param rowIndex
     * @param labelValueMap
     * @return
     */
    public int addRowsFromMap(String sectionName, HSSFSheet sheet, int rowIndex,
	    Map<String, Double> labelValueMap) {
	rowIndex = addLabel(sectionName, sheet, rowIndex);
	for (String key : labelValueMap.keySet()) {
	    rowIndex = addRow(key, labelValueMap.get(key), sheet, rowIndex);
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
    public int addRowsExpenses(String sectionName, HSSFSheet sheet, int rowIndex,
	    ImmutableList<IExpense> expenses) {
	for (IExpense expense : expenses) {
	    rowIndex = addRow(expense.getName(), expense.getCost(), sheet, rowIndex,
		    StringUtils.capitalize(expense.getObjectName()));
	}
	rowIndex++;
	return rowIndex;
    }

    /**
     * Add a section.
     * 
     * @param list
     * @param row
     * @param sheet
     * @param rowIndex
     * @param subType
     * @return
     */
    public int addSectionEstimates(ImmutableList<Entry<EstimateCost, Double>> list, HSSFSheet sheet,
	    int rowIndex, int subType) {

	// Loop through all entries.
	for (Entry<EstimateCost, Double> entry : list) {

	    // Get key and value.
	    EstimateCost cost = entry.getKey();
	    double value = entry.getValue();

	    // If the sub type is computed.
	    if (subType == EstimateCostType.SUB_TYPE_DIFFERENCE
		    || subType == EstimateCostType.SUB_TYPE_ABSOLUTE) {
		rowIndex = addRow(cost.getName(), value, sheet, rowIndex,
			subType == EstimateCostType.SUB_TYPE_DIFFERENCE ? "Difference" : "Absolute",
			cost.getCostType().getLabel());
		continue;
	    }

	    // If plain data render.
	    rowIndex = addRow(cost.getName(), value, sheet, rowIndex,
		    subType == EstimateCostType.SUB_TYPE_PLANNED ? "Estimated" : "Actual",
		    cost.getCostType().getLabel());
	}
	return rowIndex;
    }

    /**
     * Add a section.
     * 
     * @param list
     * @param row
     * @param sheet
     * @param rowIndex
     * @param subType
     * @return
     */
    public int addSectionEstimates(List<EstimateCost> list, HSSFSheet sheet, int rowIndex, int subType) {

	// Add row based on parameters.
	for (EstimateCost cost : list) {
	    rowIndex = addRow(cost.getName(),
		    subType == EstimateCostType.SUB_TYPE_PLANNED ? cost.getCost() : cost.getActualCost(),
		    sheet, rowIndex,
		    subType == EstimateCostType.SUB_TYPE_PLANNED ? "Estimated" : "Actual",
		    cost.getCostType().getLabel());
	}
	return rowIndex;
    }

    /**
     * Add an Excel row.
     * 
     * @param key
     * @param value
     * @param row
     * @param sheet
     * @param rowIndex
     * @param moreValues
     * @return
     */
    public int addRow(String key, Object value, HSSFSheet sheet, int rowIndex, Object... moreValues) {
	HSSFRow row = sheet.createRow(rowIndex);
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
    public int addRowsExpenses(HSSFSheet sheet, int rowIndex, List<? extends AbstractExpense> expenses) {
	for (AbstractExpense expense : expenses) {
	    rowIndex = addRow(expense.getName(), expense.getCost(), sheet, rowIndex);
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
