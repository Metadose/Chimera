package com.cebedo.pmsys.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.WordUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.cebedo.pmsys.constants.RegistryExcel;
import com.cebedo.pmsys.domain.AbstractExpense;
import com.cebedo.pmsys.domain.Delivery;
import com.cebedo.pmsys.domain.EquipmentExpense;
import com.cebedo.pmsys.domain.EstimateCost;
import com.cebedo.pmsys.domain.Expense;
import com.cebedo.pmsys.domain.IExpense;
import com.cebedo.pmsys.domain.ProjectPayroll;
import com.cebedo.pmsys.enums.AttendanceStatus;
import com.cebedo.pmsys.enums.EstimateCostType;
import com.cebedo.pmsys.enums.SortOrder;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.Task.TaskSubType;
import com.google.common.collect.ImmutableList;

public class GeneratorExcel {

    private HSSFWorkbook workbook = new HSSFWorkbook();
    private Map<String, Integer> rowIndexMap = new HashMap<String, Integer>();

    // Styles.
    private HSSFCellStyle styleYellowFill;
    private HSSFCellStyle styleSeaGreenFill;

    public GeneratorExcel() {
	this.styleYellowFill = this.workbook.createCellStyle();
	this.styleYellowFill.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
	this.styleYellowFill.setFillPattern(CellStyle.SOLID_FOREGROUND);

	this.styleSeaGreenFill = this.workbook.createCellStyle();
	this.styleSeaGreenFill.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
	this.styleSeaGreenFill.setFillPattern(CellStyle.SOLID_FOREGROUND);
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
	    String strVal = String.valueOf(value);
	    row.createCell(cellIndex).setCellValue(strVal);
	    cellIndex++;
	}
	rowIndex++;
	saveIndex(sheetName, rowIndex);
    }

    public void addRowEmpty(String sheetName) {
	Integer rowIndex = getRowIndex(sheetName) + 1;
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

	HSSFCellStyle style = color == IndexedColors.YELLOW ? this.styleYellowFill
		: this.styleSeaGreenFill;

	// If sheet or index is not initialized,
	// create it.
	Integer rowIndex = getRowIndex(sheetName);
	rowIndex = rowIndex == 0 ? 0 : rowIndex + 1;

	// Create the label cell.
	HSSFSheet sheet = getSheet(sheetName);
	HSSFRow row = sheet.createRow(rowIndex);
	int cellIndex = 0;
	boolean firstRun = true;
	for (Object val : values) {
	    if (firstRun) {
		row.createCell(cellIndex).setCellStyle(style);
		firstRun = false;
	    } else {
		row.createCell(cellIndex);
	    }
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

    public HSSFWorkbook getWorkbook() {
	return workbook;
    }

    public void fixColumnSizes() {
	for (int sheetIndex = 0; sheetIndex < this.workbook.getNumberOfSheets(); sheetIndex++) {
	    HSSFSheet wbSheet = this.workbook.getSheetAt(sheetIndex);
	    wbSheet.setDefaultColumnWidth(20);
	    wbSheet.autoSizeColumn(0);
	    wbSheet.setZoom(85, 100);
	}
    }

    /**
     * Add attendance entries.
     * 
     * @param sheetName
     * @param statisticsStaff
     * @param max
     * @param order
     */
    public void addStatisticsAttendanceEntries(String sheetName, StatisticsStaff statisticsStaff,
	    int max, SortOrder order) {
	addRow(sheetName, IndexedColors.SEA_GREEN, AttendanceStatus.ABSENT.label());
	addRowsStaffAttendanceCount(sheetName, statisticsStaff, AttendanceStatus.ABSENT, max, order);

	addRow(sheetName, IndexedColors.SEA_GREEN, AttendanceStatus.OVERTIME.label());
	addRowsStaffAttendanceCount(sheetName, statisticsStaff, AttendanceStatus.OVERTIME, max, order);

	addRow(sheetName, IndexedColors.SEA_GREEN, AttendanceStatus.LATE.label());
	addRowsStaffAttendanceCount(sheetName, statisticsStaff, AttendanceStatus.LATE, max, order);

	addRow(sheetName, IndexedColors.SEA_GREEN, AttendanceStatus.HALFDAY.label());
	addRowsStaffAttendanceCount(sheetName, statisticsStaff, AttendanceStatus.HALFDAY, max, order);

	addRow(sheetName, IndexedColors.SEA_GREEN, AttendanceStatus.LEAVE.label());
	addRowsStaffAttendanceCount(sheetName, statisticsStaff, AttendanceStatus.LEAVE, max, order);
    }

    /**
     * Add attendance of staff and count.
     * 
     * @param sheetName
     * @param statisticsStaff
     * @param attendanceStatus
     * @param max
     * @param order
     */
    private void addRowsStaffAttendanceCount(String sheetName, StatisticsStaff statisticsStaff,
	    AttendanceStatus attendanceStatus, int max, SortOrder order) {
	ImmutableList<Entry<Staff, Integer>> entries = statisticsStaff
		.getSortedAttendance(attendanceStatus, max, order);
	if (entries.size() == 0) {
	    addRow(sheetName, "(None)", "");
	}
	for (Entry<Staff, Integer> entry : entries) {
	    addRow(sheetName, entry.getKey().getFullName(), entry.getValue());
	}
    }

    /**
     * Add estimates.
     * 
     * @param sheetName
     * @param list
     * @param subType
     */
    public void addRowsEstimateCosts(String sheetName, List<EstimateCost> list, int subType) {
	for (EstimateCost cost : list) {

	    // Data.
	    double costValue = subType == EstimateCostType.SUB_TYPE_PLANNED ? cost.getCost()
		    : cost.getActualCost();
	    String phase = subType == EstimateCostType.SUB_TYPE_PLANNED ? "Estimated" : "Actual";

	    // Render
	    addRow(sheetName, cost.getName(), costValue, phase, cost.getCostType().getLabel());
	}
    }

    public void addStatisticsEstimatesComputed(String sheetName, StatisticsEstimateCost statEstimates,
	    int subType, SortOrder order, Integer limit) {
	String adjective = order == SortOrder.DESCENDING ? "Top" : "Bottom";
	String subTypeText = subType == EstimateCostType.SUB_TYPE_ABSOLUTE ? "ABSOLUTE DIFFERENCE"
		: "DIFFERENCE";

	// Data.
	ImmutableList<Entry<EstimateCost, Double>> direct = subType == EstimateCostType.SUB_TYPE_ABSOLUTE
		? statEstimates.getSortedAbsDiffDirect(limit, order)
		: statEstimates.getSortedDifferencesDirect(limit, order);

	ImmutableList<Entry<EstimateCost, Double>> indirect = subType == EstimateCostType.SUB_TYPE_ABSOLUTE
		? statEstimates.getSortedAbsDiffIndirect(limit, order)
		: statEstimates.getSortedDifferencesIndirect(limit, order);

	ImmutableList<Entry<EstimateCost, Double>> overall = subType == EstimateCostType.SUB_TYPE_ABSOLUTE
		? statEstimates.getSortedAbsDiffOverall(limit, order)
		: statEstimates.getSortedDifferencesOverall(limit, order);

	// Render.
	addRow(sheetName, IndexedColors.SEA_GREEN,
		String.format("Direct", adjective, WordUtils.capitalizeFully(subTypeText)));
	addRowHeader(sheetName, IndexedColors.YELLOW, "Name", WordUtils.capitalizeFully(subTypeText),
		"Cost Type");
	addRowsEstimateCostsComputed(sheetName, direct);

	addRow(sheetName, IndexedColors.SEA_GREEN,
		String.format("Indirect", adjective, WordUtils.capitalizeFully(subTypeText)));
	addRowHeader(sheetName, IndexedColors.YELLOW, "Name", WordUtils.capitalizeFully(subTypeText),
		"Cost Type");
	addRowsEstimateCostsComputed(sheetName, indirect);

	addRow(sheetName, IndexedColors.SEA_GREEN,
		String.format("Overall", adjective, WordUtils.capitalizeFully(subTypeText)));
	addRowHeader(sheetName, IndexedColors.YELLOW, "Name", WordUtils.capitalizeFully(subTypeText),
		"Cost Type");
	addRowsEstimateCostsComputed(sheetName, overall);
    }

    /**
     * Add a list of estimates
     * 
     * @param xlsGen
     * @param sheetName
     * @param list
     */
    private void addRowsEstimateCostsComputed(String sheetName,
	    ImmutableList<Entry<EstimateCost, Double>> list) {
	for (Entry<EstimateCost, Double> entry : list) {
	    EstimateCost cost = entry.getKey();
	    String costName = cost.getName();
	    double costValue = entry.getValue();
	    String costTypeLabel = cost.getCostType().getLabel();
	    addRow(sheetName, costName, costValue, costTypeLabel);
	}
    }

    /**
     * Add estimate entries.
     * 
     * @param sheetName
     * @param statEstimates
     * @param order
     * @param limit
     */
    public void addStatisticsEstimatesEntries(String sheetName, Project proj,
	    StatisticsEstimateCost statEstimates, SortOrder order, Integer limit) {

	// Direct planned.
	addRowHeader(sheetName, IndexedColors.YELLOW, "Name", "Cost", "Type", "Cost Type");
	addRowsEstimateCosts(sheetName, statEstimates.getSortedPlannedDirect(order, limit),
		EstimateCostType.SUB_TYPE_PLANNED);
	addRowEmpty(sheetName);

	// Indirect planned.
	addRowHeader(sheetName, IndexedColors.YELLOW, "Name", "Cost", "Type", "Cost Type");
	addRowsEstimateCosts(sheetName, statEstimates.getSortedPlannedIndirect(order, limit),
		EstimateCostType.SUB_TYPE_PLANNED);
	addRowEmpty(sheetName);

	if (proj.isCompleted()) {
	    // Direct actual.
	    addRowHeader(sheetName, IndexedColors.YELLOW, "Name", "Cost", "Type", "Cost Type");
	    addRowsEstimateCosts(sheetName, statEstimates.getSortedActualDirect(order, limit),
		    EstimateCostType.SUB_TYPE_ACTUAL);
	    addRowEmpty(sheetName);

	    // Indirect actual.
	    addRowHeader(sheetName, IndexedColors.YELLOW, "Name", "Cost", "Type", "Cost Type");
	    addRowsEstimateCosts(sheetName, statEstimates.getSortedActualIndirect(order, limit),
		    EstimateCostType.SUB_TYPE_ACTUAL);
	}
    }

    /**
     * Create a section for the list of expenses.
     * 
     * @param xlsGen
     * @param sheetName
     * @param expenses
     */
    public void addRowsExpenses(String sheetName, List<? extends AbstractExpense> expenses) {
	for (AbstractExpense expense : expenses) {
	    addRow(sheetName, expense.getName(), expense.getCost());
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
    public void addRowsExpenses(String sheetName, ImmutableList<IExpense> expenses) {
	for (IExpense expense : expenses) {
	    addRow(sheetName, expense.getName(), expense.getCost());
	}
    }

    public void addStatisticsExpensesDescriptive(String sheetName, StatisticsProject statisticsProj,
	    int descriptiveType) {
	String adjective = descriptiveType == StatisticsProject.DESCRIPTIVE_MAX ? "Maximum"
		: descriptiveType == StatisticsProject.DESCRIPTIVE_MIN ? "Minimum" : "";
	String superlative = descriptiveType == StatisticsProject.DESCRIPTIVE_MAX ? "greatest"
		: descriptiveType == StatisticsProject.DESCRIPTIVE_MIN ? "least" : "";

	// Data.
	List<ProjectPayroll> payroll = statisticsProj.getMaxPayrolls();
	List<Delivery> deliveries = statisticsProj.getMaxDelivery();
	List<EquipmentExpense> equips = statisticsProj.getMaxEquipment();
	List<Expense> others = statisticsProj.getMaxOtherExpenses();

	// Render.
	addRow(sheetName, IndexedColors.SEA_GREEN, adjective,
		String.format(RegistryExcel.DYNAMIC_EXPENSE_VALUE, superlative));
	addRowsExpenses(sheetName, payroll);
	addRowsExpenses(sheetName, deliveries);
	addRowsExpenses(sheetName, equips);
	addRowsExpenses(sheetName, others);
    }

    /**
     * Add expenses entries.
     * 
     * @param sheetName
     * @param limit
     * @param order
     * @param statisticsProj
     */
    public void addStatisticsExpensesPlain(String sheetName, Integer limit, SortOrder order,
	    StatisticsProject statisticsProj) {
	String adjective = order == SortOrder.DESCENDING ? "Top" : "Bottom";
	String superlative = order == SortOrder.DESCENDING ? "Most" : "Least";

	// Data.
	ImmutableList<ProjectPayroll> payrolls = statisticsProj.getLimitedSortedByCostPayrolls(limit,
		order);
	ImmutableList<Delivery> deliveries = statisticsProj.getLimitedSortedByCostDeliveries(limit,
		order);
	ImmutableList<EquipmentExpense> equips = statisticsProj.getLimitedSortedByCostEquipment(limit,
		order);
	ImmutableList<Expense> others = statisticsProj.getLimitedSortedByCostOtherExpenses(limit, order);
	ImmutableList<IExpense> projAll = statisticsProj.getLimitedSortedByCostProject(limit, order);

	// Render.
	addRow(sheetName, IndexedColors.YELLOW,
		String.format("%s %s %s Expensive", adjective, limit, superlative));

	addRow(sheetName, IndexedColors.SEA_GREEN, "Payrolls");
	addRowsExpenses(sheetName, payrolls);

	addRow(sheetName, IndexedColors.SEA_GREEN, "Deliveries");
	addRowsExpenses(sheetName, deliveries);

	addRow(sheetName, IndexedColors.SEA_GREEN, "Equipment");
	addRowsExpenses(sheetName, equips);

	addRow(sheetName, IndexedColors.SEA_GREEN, "Other Expenses");
	addRowsExpenses(sheetName, others);

	addRow(sheetName, IndexedColors.SEA_GREEN, "Overall Project");
	addRowsExpenses(sheetName, projAll);
    }

    public void addRowsTasks(String sheetName, List<Task> list, TaskSubType type) {
	for (Task task : list) {

	    // Get names from the staff set.
	    Set<Staff> staff = task.getStaff();
	    List<String> names = new ArrayList<String>();
	    for (Staff member : staff) {
		names.add(member.getFullName());
	    }

	    // Get the value based on the sub type.
	    double value = 0;
	    if (type == TaskSubType.ESTIMATED) {
		value = task.getDuration();
	    } else if (type == TaskSubType.ACTUAL) {
		value = task.getActualDuration();
	    } else if (type == TaskSubType.DIFFERENCE) {
		value = task.getDuration() - task.getActualDuration();
	    } else if (type == TaskSubType.ABSOLUTE) {
		value = Math.abs(task.getDuration() - task.getActualDuration());
	    }
	    if (names.size() > 0) {
		addRow(sheetName, task.getTitle(), value, type.getLabel(), names);
	    } else {
		addRow(sheetName, task.getTitle(), value, type.getLabel());
	    }
	}
    }

    public void addRowsTaskCount(String sheetName, Map<TaskStatus, Integer> taskStatusCountMap,
	    int tasksPopulation) {
	for (TaskStatus status : taskStatusCountMap.keySet()) {
	    double percent = ((double) taskStatusCountMap.get(status) / (double) tasksPopulation) * 100;
	    addRow(sheetName, status.label(), taskStatusCountMap.get(status), percent + "%");
	}
    }

    public void addRowHeader(String sheetName, IndexedColors color, Object... values) {

	HSSFCellStyle style = color == IndexedColors.YELLOW ? this.styleYellowFill
		: this.styleSeaGreenFill;

	// If sheet or index is not initialized,
	// create it.
	Integer rowIndex = getRowIndex(sheetName);

	// Create the label cell.
	HSSFSheet sheet = getSheet(sheetName);
	HSSFRow row = sheet.createRow(rowIndex);
	int cellIndex = 0;
	for (Object val : values) {
	    String valStr = String.valueOf(val);
	    row.createCell(cellIndex).setCellStyle(style);
	    row.getCell(cellIndex).setCellValue(valStr);
	    cellIndex++;
	}
	rowIndex++;
	saveIndex(sheetName, rowIndex);
    }

}
