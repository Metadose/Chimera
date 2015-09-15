package com.cebedo.pmsys.helper;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class ExcelHelper {

    /**
     * Get value as the expected object to avoid exceptions.
     * 
     * @param cell
     * @return
     */
    public Object getValueAsExpected(HSSFWorkbook workbook, Cell cell) {

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
    }

}
