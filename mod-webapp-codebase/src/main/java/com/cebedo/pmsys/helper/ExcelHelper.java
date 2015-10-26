package com.cebedo.pmsys.helper;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import com.cebedo.pmsys.bean.GeneratorExcel;
import com.cebedo.pmsys.domain.AbstractExpense;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.domain.IExpense;
import com.cebedo.pmsys.enums.EstimateCostType;
import com.google.common.collect.ImmutableList;

public class ExcelHelper {

    /**
     * Add a list of estimates.
     * 
     * @param list
     * @param subType
     * @param xlsGen
     * @param sheetName
     */
    public void addEstimates(GeneratorExcel xlsGen, String sheetName, List<EstimateCost> list,
	    int subType) {

	for (EstimateCost cost : list) {

	    // Data.
	    double costValue = subType == EstimateCostType.SUB_TYPE_PLANNED ? cost.getCost()
		    : cost.getActualCost();
	    String phase = subType == EstimateCostType.SUB_TYPE_PLANNED ? "Estimated" : "Actual";

	    // Render
	    xlsGen.addRow(sheetName, cost.getName(), costValue, phase, cost.getCostType().getLabel());
	}
    }

    /**
     * Add a list of estimates
     * 
     * @param xlsGen
     * @param sheetName
     * @param list
     * @param subType
     */
    public void addEstimates(GeneratorExcel xlsGen, String sheetName,
	    ImmutableList<Entry<EstimateCost, Double>> list, int subType) {

	// Loop through all entries.
	for (Entry<EstimateCost, Double> entry : list) {

	    // Data.
	    EstimateCost cost = entry.getKey();
	    String costName = cost.getName();
	    double costValue = entry.getValue();
	    String subTypeText = "";
	    String costTypeLabel = cost.getCostType().getLabel();

	    // If the sub type is computed.
	    if (subType == EstimateCostType.SUB_TYPE_DIFFERENCE
		    || subType == EstimateCostType.SUB_TYPE_ABSOLUTE) {
		subTypeText = subType == EstimateCostType.SUB_TYPE_DIFFERENCE ? "Difference"
			: "Absolute";
	    }
	    // If plain data render.
	    else {

		subTypeText = subType == EstimateCostType.SUB_TYPE_PLANNED ? "Estimated" : "Actual";
	    }
	    xlsGen.addRow(sheetName, costName, costValue, subTypeText, costTypeLabel);
	}
    }

    /**
     * Create a section for the list of expenses.
     * 
     * @param xlsGen
     * @param sheetName
     * @param expenses
     */
    public void addExpenses(GeneratorExcel xlsGen, String sheetName,
	    List<? extends AbstractExpense> expenses) {
	for (AbstractExpense expense : expenses) {
	    xlsGen.addRow(sheetName, expense.getName(), expense.getCost());
	}
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
    public void addExpenses(GeneratorExcel xlsGen, String sheetName, ImmutableList<IExpense> expenses) {
	for (IExpense expense : expenses) {
	    xlsGen.addRow(sheetName, expense.getName(), expense.getCost());
	}
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
