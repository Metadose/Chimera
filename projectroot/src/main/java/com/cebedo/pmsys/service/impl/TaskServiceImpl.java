package com.cebedo.pmsys.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cebedo.pmsys.dao.MilestoneDAO;
import com.cebedo.pmsys.dao.ProjectDAO;
import com.cebedo.pmsys.dao.StaffDAO;
import com.cebedo.pmsys.dao.TaskDAO;
import com.cebedo.pmsys.enums.AuditAction;
import com.cebedo.pmsys.helper.AuthHelper;
import com.cebedo.pmsys.helper.ExcelHelper;
import com.cebedo.pmsys.helper.MessageHelper;
import com.cebedo.pmsys.model.Company;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.model.assignment.TaskStaffAssignment;
import com.cebedo.pmsys.service.TaskService;
import com.cebedo.pmsys.token.AuthenticationToken;
import com.cebedo.pmsys.ui.AlertBoxGenerator;

@Service
public class TaskServiceImpl implements TaskService {

    private AuthHelper authHelper = new AuthHelper();
    private MessageHelper messageHelper = new MessageHelper();
    private ExcelHelper excelHelper = new ExcelHelper();

    private static final int EXCEL_COLUMN_DATE_START = 1;
    private static final int EXCEL_COLUMN_DURATION = 2;
    private static final int EXCEL_COLUMN_MILESTONE = 3;
    private static final int EXCEL_COLUMN_TITLE = 4;
    private static final int EXCEL_COLUMN_CONTENT = 5;

    private TaskDAO taskDAO;
    private ProjectDAO projectDAO;
    private StaffDAO staffDAO;
    private MilestoneDAO milestoneDAO;

    @Autowired
    @Qualifier(value = "milestoneDAO")
    public void setMilestoneDAO(MilestoneDAO milestoneDAO) {
	this.milestoneDAO = milestoneDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
	this.projectDAO = projectDAO;
    }

    public void setStaffDAO(StaffDAO staffDAO) {
	this.staffDAO = staffDAO;
    }

    public void setTaskDAO(TaskDAO taskDAO) {
	this.taskDAO = taskDAO;
    }

    @Override
    @Transactional
    public void createMassTasks(List<Task> tasks) {

	// Security check.
	if (tasks.size() > 0 && !this.authHelper.isActionAuthorized(tasks.get(0))) {
	    Task sampleTask = tasks.get(0);
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, sampleTask.getId());
	    return;
	}

	// Log.
	this.messageHelper.send(AuditAction.CREATE_MASS, Task.OBJECT_NAME);

	for (Task task : tasks) {
	    this.taskDAO.create(task);
	}
    }

    @Override
    @Transactional
    public List<Task> convertExcelToTaskList(MultipartFile multipartFile, Project project) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return new ArrayList<Task>();
	}

	// Log.
	this.messageHelper.send(AuditAction.CONVERT_FILE, Project.OBJECT_NAME, project.getId(),
		MultipartFile.class.getName());

	try {

	    // Create Workbook instance holding reference to .xls file
	    // Get first/desired sheet from the workbook.
	    HSSFWorkbook workbook = new HSSFWorkbook(multipartFile.getInputStream());
	    HSSFSheet sheet = workbook.getSheetAt(0);

	    // Iterate through each rows one by one.
	    Iterator<Row> rowIterator = sheet.iterator();

	    // Construct estimate containers.
	    List<Task> taskList = new ArrayList<Task>();
	    List<Milestone> milestoneList = new ArrayList<Milestone>();
	    while (rowIterator.hasNext()) {

		Row row = rowIterator.next();
		int rowCountDisplay = row.getRowNum() + 1;

		// Skip first line.
		if (rowCountDisplay <= 1) {
		    continue;
		}

		// For each row, iterate through all the columns
		Iterator<Cell> cellIterator = row.cellIterator();

		// Every row, is a Staff object.
		Company company = project.getCompany();
		Task task = new Task(company, project);

		while (cellIterator.hasNext()) {

		    // Cell in this row and column.
		    Cell cell = cellIterator.next();
		    int colCountDisplay = cell.getColumnIndex() + 1;

		    switch (colCountDisplay) {

		    case EXCEL_COLUMN_DATE_START:
			task.setDateStart(cell.getDateCellValue());
			continue;

		    case EXCEL_COLUMN_DURATION:
			Double duration = (Double) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			task.setDuration(duration);
			continue;

		    case EXCEL_COLUMN_MILESTONE:
			// Get the name from the Excel.
			String milestoneName = (String) (this.excelHelper.getValueAsExpected(workbook,
				cell) == null ? "" : this.excelHelper.getValueAsExpected(workbook, cell));

			// If this milestone has already been created earlier,
			// use that milestone.
			Milestone milestone = null;
			for (Milestone mStone : milestoneList) {
			    if (mStone.getName().equals(milestoneName)) {
				milestone = mStone;
				break;
			    }
			}

			// If we didn't find any matching milestone.
			// Commit to database.
			if (milestone == null) {
			    milestone = new Milestone(company, project, milestoneName);
			    this.milestoneDAO.create(milestone);
			    milestoneList.add(milestone);
			}

			// Set the object.
			task.setMilestone(milestone);
			continue;

		    case EXCEL_COLUMN_TITLE:
			String title = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			task.setTitle(title);
			continue;

		    case EXCEL_COLUMN_CONTENT:
			String content = (String) (this.excelHelper.getValueAsExpected(workbook, cell) == null ? ""
				: this.excelHelper.getValueAsExpected(workbook, cell));
			task.setContent(content);
			continue;

		    }
		}

		taskList.add(task);
	    }
	    return taskList;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return new ArrayList<Task>();
    }

    /**
     * Create a new task.
     */
    @CacheEvict(value = Project.OBJECT_NAME + ":getByIDWithAllCollections", key = "#task.getProject().getId()", condition = "#task.getProject() != null")
    @Override
    @Transactional
    public String create(Task task) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Company authCompany = auth.getCompany();
	task.setCompany(authCompany);

	// Do service.
	this.taskDAO.create(task);

	// Log.
	this.messageHelper.send(AuditAction.CREATE, Task.OBJECT_NAME, task.getId());

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateCreate(Task.OBJECT_NAME, task.getTitle());
    }

    /**
     * Get an object given id.
     */
    @Override
    @Transactional
    public Task getByID(long id) {
	Task task = this.taskDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(task)) {
	    this.messageHelper.unauthorized(Task.OBJECT_NAME, task.getId());
	    return new Task();
	}

	// Log.
	this.messageHelper.send(AuditAction.GET, Task.OBJECT_NAME, task.getId());

	// Return obj.
	return task;
    }

    /**
     * A task object has many relationships to other objects. Merging this with
     * an existing object would be safer than Updating the existing one.
     */
    @Override
    @Transactional
    public String update(Task task) {

	// Security check.
	if (!this.authHelper.isActionAuthorized(task)) {
	    this.messageHelper.unauthorized(Task.OBJECT_NAME, task.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.UPDATE, Task.OBJECT_NAME, task.getId());

	// Do service.
	this.taskDAO.update(task);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUpdate(Task.OBJECT_NAME, task.getTitle());
    }

    /**
     * Delete a task.
     */
    @Override
    @Transactional
    public String delete(long id) {
	Task task = this.taskDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(task)) {
	    this.messageHelper.unauthorized(Task.OBJECT_NAME, task.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.DELETE, Task.OBJECT_NAME, task.getId());

	// Do service.
	this.taskDAO.delete(id);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateDelete(Task.OBJECT_NAME, task.getTitle());
    }

    /**
     * List all tasks.
     */
    @Override
    @Transactional
    public List<Task> list() {
	AuthenticationToken token = this.authHelper.getAuth();

	// Log.
	this.messageHelper.send(AuditAction.LIST, Task.OBJECT_NAME);

	if (token.isSuperAdmin()) {

	    // Return list.
	    return this.taskDAO.list(null);
	}

	// Log warn.
	Company company = token.getCompany();

	// Return list.
	return this.taskDAO.list(company.getId());
    }

    /**
     * List all tasks with all collections.
     */
    @Override
    @Transactional
    public List<Task> listWithAllCollections() {
	AuthenticationToken token = this.authHelper.getAuth();

	// Log.
	this.messageHelper.send(AuditAction.LIST, Task.OBJECT_NAME);

	if (token.isSuperAdmin()) {

	    // Return list.
	    return this.taskDAO.listWithAllCollections(null);
	}

	// Log info non-super admin.
	Company company = token.getCompany();

	// Return list.
	return this.taskDAO.listWithAllCollections(company.getId());
    }

    /**
     * Set the task to the status specified.
     */
    @Override
    @Transactional
    public String mark(long taskID, int status) {
	// Get the task.
	Task task = this.taskDAO.getByID(taskID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(task)) {
	    this.messageHelper.unauthorized(Task.OBJECT_NAME, task.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.UPDATE, Task.OBJECT_NAME, task.getId());

	// Do service.
	task.setStatus(status);
	this.taskDAO.update(task);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateMarkAs(Task.OBJECT_NAME, task.getTitle());
    }

    /**
     * Assign a task under a staff.
     */
    @Override
    @Transactional
    public String assignStaffTask(long taskID, long staffID) {
	Task task = this.taskDAO.getByID(taskID);
	Staff staff = this.staffDAO.getByID(staffID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(staff)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.ASSIGN, Staff.OBJECT_NAME, staff.getId(), Task.OBJECT_NAME,
		task.getId());

	// Do service.
	TaskStaffAssignment taskStaffAssign = new TaskStaffAssignment();
	taskStaffAssign.setTaskID(taskID);
	taskStaffAssign.setStaffID(staffID);
	this.taskDAO.assignStaffTask(taskStaffAssign);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateAssign(Staff.OBJECT_NAME, staff.getFullName());
    }

    /**
     * Get object with all collections.
     */
    @Override
    @Transactional
    public Task getByIDWithAllCollections(long id) {
	Task task = this.taskDAO.getByIDWithAllCollections(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(task)) {
	    this.messageHelper.unauthorized(Task.OBJECT_NAME, task.getId());
	    return new Task();
	}

	// Log.
	this.messageHelper.send(AuditAction.GET, Task.OBJECT_NAME, task.getId());

	// Return obj.
	return task;
    }

    /**
     * Unassign a staff from a task. TODO Pass actual objects rather than ID's
     * only.
     */
    @Override
    @Transactional
    public String unassignStaffTask(long taskID, long staffID) {
	Task task = this.taskDAO.getByID(taskID);
	Staff staff = this.staffDAO.getByID(staffID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(staff)) {
	    this.messageHelper.unauthorized(Staff.OBJECT_NAME, staff.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.UNASSIGN, Staff.OBJECT_NAME, staff.getId(),
		Task.OBJECT_NAME, task.getId());

	// Do service.
	this.taskDAO.unassignStaffTask(taskID, staffID);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUnassign(Staff.OBJECT_NAME, staff.getFullName());
    }

    /**
     * Unassign all staff linked to a task.
     */
    @Override
    @Transactional
    public String unassignAllStaffUnderTask(long id) {
	Task task = this.taskDAO.getByID(id);

	// Security check.
	if (!this.authHelper.isActionAuthorized(task)) {
	    this.messageHelper.unauthorized(Task.OBJECT_NAME, task.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.UNASSIGN_ALL, Task.OBJECT_NAME, task.getId(),
		Staff.OBJECT_NAME);

	// Do service.
	this.taskDAO.unassignAllStaffTasks(id);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUnassignAll(Staff.OBJECT_NAME);
    }

    /**
     * Delete all tasks given a project.
     */
    @Override
    @Transactional
    public String deleteAllTasksByProject(long projectID) {
	Project project = this.projectDAO.getByID(projectID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.DELETE_ALL, Project.OBJECT_NAME, project.getId(),
		Task.OBJECT_NAME);

	// Do service.
	this.taskDAO.deleteAllTasksByProject(projectID);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateDeleteAll(Task.OBJECT_NAME);
    }

    /**
     * Create a task with a linked project.
     */
    @Override
    @Transactional
    public String createWithProject(Task task, long projectID) {
	AuthenticationToken auth = this.authHelper.getAuth();
	Project proj = this.projectDAO.getByID(projectID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(proj)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, proj.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Do service.
	task.setProject(proj);
	Company authCompany = auth.getCompany();
	task.setCompany(authCompany);
	this.taskDAO.create(task);

	// Log.
	this.messageHelper.send(AuditAction.CREATE, Project.OBJECT_NAME, proj.getId(), Task.OBJECT_NAME,
		task.getId());

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateCreate(Task.OBJECT_NAME, task.getTitle());
    }

    /**
     * Update a task.
     */
    @Override
    @Transactional
    public String merge(Task task) {
	Task oldTask = this.taskDAO.getByIDWithAllCollections(task.getId());

	// Prepare object.
	task.setCompany(oldTask.getCompany());
	if (task.getProject() == null) {
	    task.setProject(oldTask.getProject());
	}
	if (task.getStaff() == null) {
	    task.setStaff(oldTask.getStaff());
	}

	// Security check.
	if (!this.authHelper.isActionAuthorized(task)) {
	    this.messageHelper.unauthorized(Task.OBJECT_NAME, task.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.MERGE, Task.OBJECT_NAME, task.getId());

	// Do service.
	this.taskDAO.merge(task);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUpdate(Task.OBJECT_NAME, task.getTitle());
    }

    /**
     * Get task title given an id.
     */
    @Override
    @Transactional
    public String getTitleByID(long taskID) {
	return this.taskDAO.getTitleByID(taskID);
    }

    /**
     * Unassign all tasks in a project.
     */
    @Override
    @Transactional
    public String unassignAllTasksByProject(long projectID) {
	Project project = this.projectDAO.getByID(projectID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.UNASSIGN_ALL, Project.OBJECT_NAME, project.getId(),
		Task.OBJECT_NAME);

	// Do service.
	this.taskDAO.unassignAllTasksByProject(project);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUnassignAll(Task.OBJECT_NAME);
    }

    /**
     * Unassign a task in a project.
     */
    @Override
    @Transactional
    public String unassignTaskByProject(long taskID, long projectID) {

	Project project = this.projectDAO.getByID(projectID);
	Task task = this.taskDAO.getByID(taskID);

	// Security check.
	if (!this.authHelper.isActionAuthorized(project)) {
	    this.messageHelper.unauthorized(Project.OBJECT_NAME, project.getId());
	    return AlertBoxGenerator.ERROR;
	}

	// Log.
	this.messageHelper.send(AuditAction.UNASSIGN, Project.OBJECT_NAME, project.getId(),
		Task.OBJECT_NAME, task.getId());

	// Do service.
	this.taskDAO.unassignTaskByProject(taskID, project);

	// Return success.
	return AlertBoxGenerator.SUCCESS.generateUnassign(Task.OBJECT_NAME, task.getTitle());
    }
}
