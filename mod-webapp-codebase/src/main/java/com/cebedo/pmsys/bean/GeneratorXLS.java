package com.cebedo.pmsys.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

public class GeneratorXLS {

    private HSSFWorkbook workbook = new HSSFWorkbook();
    private Map<String, HSSFSheet> sheetMap = new HashMap<String, HSSFSheet>();
    private Map<String, Integer> rowIndexMap = new HashMap<String, Integer>();

    public GeneratorXLS() {
	;
    }

    /**
     * Add a colored row.
     * 
     * @param sheetName
     * @param color
     * @param values
     * @return
     */
    public int addRowColored(String sheetName, IndexedColors color, Object... values) {

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
	save(sheetName, sheet, rowIndex);

	return rowIndex;
    }

    private void save(String sheetName, HSSFSheet sheet, Integer rowIndex) {
	this.sheetMap.put(sheetName, sheet);
	this.rowIndexMap.put(sheetName, rowIndex);
    }

    private Integer getRowIndex(String sheetName) {
	Integer rowIndex = this.rowIndexMap.get(sheetName);
	if (rowIndex == null) {
	    rowIndex = 0;
	    this.rowIndexMap.put(sheetName, 0);
	}
	return rowIndex;
    }

    private HSSFSheet getSheet(String sheetName) {
	HSSFSheet sheet = this.sheetMap.get(sheetName);
	if (sheet == null) {
	    sheet = this.workbook.createSheet(sheetName);
	    this.sheetMap.put(sheetName, sheet);
	}
	return sheet;
    }

}
