package com.cebedo.pmsys.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

public class GeneratorExcel {

    private HSSFWorkbook workbook = new HSSFWorkbook();
    private Map<String, Integer> rowIndexMap = new HashMap<String, Integer>();

    public GeneratorExcel() {
	;
    }

    /**
     * Add a new plain row.
     * 
     * @param sheetName
     * @param values
     */
    public void addRow(String sheetName, Object... values) {
	HSSFSheet sheet = getSheet(sheetName);
	Integer rowIndex = getRowIndex(sheetName);
	HSSFRow row = sheet.createRow(rowIndex);
	int cellIndex = 0;
	for (Object value : values) {
	    row.createCell(cellIndex).setCellValue(String.valueOf(value));
	    cellIndex++;
	}
	rowIndex++;
	saveIndex(sheetName, rowIndex);
    }

    /**
     * Add a colored row.
     * 
     * @param sheetName
     * @param color
     * @param values
     * @return
     */
    public void addRow(String sheetName, IndexedColors color, Object... values) {

	// Create the style.
	HSSFCellStyle style = this.workbook.createCellStyle();
	style.setFillForegroundColor(color.getIndex());
	style.setFillPattern(CellStyle.SOLID_FOREGROUND);

	// If sheet or index is not initialized,
	// create it.
	HSSFSheet sheet = getSheet(sheetName);
	Integer rowIndex = getRowIndex(sheetName);

	// Create the label cell.
	HSSFRow row = sheet.createRow(rowIndex);
	int cellIndex = 0;
	for (Object val : values) {
	    row.createCell(cellIndex).setCellStyle(style);
	    row.getCell(cellIndex).setCellValue(String.valueOf(val));
	    cellIndex++;
	}
	rowIndex++;
	saveIndex(sheetName, rowIndex);
    }

    /**
     * Save the last state of the index.
     * 
     * @param sheetName
     * @param rowIndex
     */
    private void saveIndex(String sheetName, Integer rowIndex) {
	this.rowIndexMap.put(sheetName, rowIndex);
    }

    /**
     * Get the last state of the index.
     * 
     * @param sheetName
     * @return
     */
    private Integer getRowIndex(String sheetName) {
	Integer rowIndex = this.rowIndexMap.get(sheetName);
	if (rowIndex == null) {
	    rowIndex = 0;
	    this.rowIndexMap.put(sheetName, 0);
	}
	return rowIndex;
    }

    /**
     * Get the sheet. If null, create new.
     * 
     * @param sheetName
     * @return
     */
    private HSSFSheet getSheet(String sheetName) {
	HSSFSheet sheet = this.workbook.getSheet(sheetName);
	if (sheet == null) {
	    sheet = this.workbook.createSheet(sheetName);
	}
	return sheet;
    }

}
