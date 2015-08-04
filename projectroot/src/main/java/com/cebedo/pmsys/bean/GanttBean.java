package com.cebedo.pmsys.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cebedo.pmsys.enums.GanttElement;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;
import com.cebedo.pmsys.utils.DateUtils;

public class GanttBean {
    private String id;
    private int status;
    private String text;
    private String content;
    private String start_date;
    private String type;
    private String color;
    private String textColor;
    private boolean open;
    private int duration;
    private String parent;
    private String assignedTeams;
    private String assignedStaff;

    public GanttBean() {
	;
    }

    public GanttBean(Milestone milestone, GanttBean parent) {
	setId(Milestone.OBJECT_NAME + "-" + milestone.getId());
	setText(milestone.getName());
	setOpen(true);
	setDuration(0);
	setParent(parent.getId());
	setType("Milestone");
	setColor(GanttElement.MILESTONE.backgroundColor());
	setTextColor(GanttElement.MILESTONE.color());
    }

    public GanttBean(Project proj) {
	setId(Project.OBJECT_NAME + "-" + proj.getId());
	setText(proj.getName());
	setOpen(true);
	setDuration(0);
	setType("Project");
	setColor(GanttElement.PROJECT.backgroundColor());
	setTextColor(GanttElement.PROJECT.color());
    }

    public GanttBean(Task task, GanttBean parent) {
	setId(Task.OBJECT_NAME + "-" + task.getId());
	setStatus(task.getStatus());
	setText(task.getTitle());
	setContent(task.getContent());
	setStart_date(DateUtils.formatDate(task.getDateStart(), "dd-MM-yyyy"));
	setOpen(true);
	setDuration(task.getDuration());
	setParent(parent.getId());
	setType("Task");

	// Assigned staff.
	if (task.getStaff() != null && !task.getStaff().isEmpty()) {
	    List<String> staffMembers = new ArrayList<String>();
	    for (Staff staff : task.getStaff()) {
		String name = staff.getFullName().isEmpty() ? (staff.getUser().getUsername() + " (Unnamed Staff)")
			: staff.getFullName();
		staffMembers.add(name);
	    }
	    setAssignedStaff(StringUtils.join(staffMembers, ", "));
	}

	// Set color based on task status.
	TaskStatus taskStatus = TaskStatus.of(task.getStatus());
	GanttElement ganttElem = GanttElement.of(taskStatus.css());
	setColor(ganttElem.backgroundColor());
	setTextColor(ganttElem.color());
    }

    public GanttBean(Staff staff) {
	setId(Staff.OBJECT_NAME + "-" + staff.getId());
	setText(staff.getFullName());
	setOpen(true);
	setDuration(0);
	setType("Staff");
	setColor(GanttElement.STAFF.backgroundColor());
	setTextColor(GanttElement.STAFF.color());
    }

    public GanttBean(Task task, String parent) {
	setId(Task.OBJECT_NAME + "-" + task.getId());
	setStatus(task.getStatus());
	setText(task.getTitle());
	setContent(task.getContent());
	setStart_date(DateUtils.formatDate(task.getDateStart(), "dd-MM-yyyy"));
	setOpen(true);
	setDuration(task.getDuration());
	setParent(parent);
	setType("Task");

	// Assigned staff.
	if (task.getStaff() != null && !task.getStaff().isEmpty()) {
	    List<String> staffMembers = new ArrayList<String>();
	    for (Staff staff : task.getStaff()) {
		String name = staff.getFullName().isEmpty() ? (staff.getUser().getUsername() + " (Unnamed Staff)")
			: staff.getFullName();
		staffMembers.add(name);
	    }
	    setAssignedStaff(StringUtils.join(staffMembers, ", "));
	}

	// Set color based on task status.
	TaskStatus taskStatus = TaskStatus.of(task.getStatus());
	GanttElement ganttElem = GanttElement.of(taskStatus.css());
	setColor(ganttElem.backgroundColor());
	setTextColor(ganttElem.color());
    }

    public GanttBean(Project proj, GanttBean parent) {
	setId(Project.OBJECT_NAME + "-" + proj.getId());
	setText(proj.getName());
	setOpen(true);
	setDuration(0);
	setType("Project");
	setParent(parent.getId());
	setColor(GanttElement.PROJECT.backgroundColor());
	setTextColor(GanttElement.PROJECT.color());
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public String getStart_date() {
	return start_date;
    }

    public void setStart_date(String start_date) {
	this.start_date = start_date;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public boolean isOpen() {
	return open;
    }

    public void setOpen(boolean open) {
	this.open = open;
    }

    public int getDuration() {
	return duration;
    }

    public void setDuration(int duration) {
	this.duration = duration;
    }

    public String getParent() {
	return parent;
    }

    public void setParent(String parent) {
	this.parent = parent;
    }

    public String getColor() {
	return color;
    }

    public void setColor(String color) {
	this.color = color;
    }

    public String getTextColor() {
	return textColor;
    }

    public void setTextColor(String textColor) {
	this.textColor = textColor;
    }

    public String getAssignedTeams() {
	return assignedTeams;
    }

    public void setAssignedTeams(String assignedTeams) {
	this.assignedTeams = assignedTeams;
    }

    public String getAssignedStaff() {
	return assignedStaff;
    }

    public void setAssignedStaff(String assignedStaff) {
	this.assignedStaff = assignedStaff;
    }
}
