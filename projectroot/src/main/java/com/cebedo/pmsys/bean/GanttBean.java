package com.cebedo.pmsys.bean;

import com.cebedo.pmsys.enums.ButtonElement;
import com.cebedo.pmsys.enums.TaskStatus;
import com.cebedo.pmsys.helper.DateHelper;
import com.cebedo.pmsys.model.Milestone;
import com.cebedo.pmsys.model.Project;
import com.cebedo.pmsys.model.Staff;
import com.cebedo.pmsys.model.Task;

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
	setColor(ButtonElement.DEFAULT_HOVER.backgroundColor());
	setTextColor(ButtonElement.DEFAULT_HOVER.color());
    }

    public GanttBean(Project proj) {
	setId(Project.OBJECT_NAME + "-" + proj.getId());
	setText(proj.getName());
	setOpen(true);
	setDuration(0);
	setType("Project");
	setColor(ButtonElement.DEFAULT.backgroundColor());
	setTextColor(ButtonElement.DEFAULT.color());
    }

    public GanttBean(Task task, GanttBean parent) {
	setId(Task.OBJECT_NAME + "-" + task.getId());
	setStatus(task.getStatus());
	setText(task.getTitle());
	setContent(task.getContent());
	setStart_date(DateHelper.formatDate(task.getDateStart(), "dd-MM-yyyy"));
	setOpen(true);
	setDuration(task.getDuration());
	setParent(parent.getId());
	setType("Task");

	// Set color based on task status.
	TaskStatus taskStatus = TaskStatus.of(task.getStatus());
	ButtonElement btnElem = ButtonElement.of(taskStatus.css());
	setColor(btnElem.backgroundColor());
	setTextColor(btnElem.color());
    }

    public GanttBean(Staff staff) {
	setId(Staff.OBJECT_NAME + "-" + staff.getId());
	setText(staff.getFullName());
	setOpen(true);
	setDuration(0);
	setType("Staff");
	setColor(ButtonElement.DEFAULT.backgroundColor());
	setTextColor(ButtonElement.DEFAULT.color());
    }

    public GanttBean(Task task, String parent) {
	setId(Task.OBJECT_NAME + "-" + task.getId());
	setStatus(task.getStatus());
	setText(task.getTitle());
	setContent(task.getContent());
	setStart_date(DateHelper.formatDate(task.getDateStart(), "dd-MM-yyyy"));
	setOpen(true);
	setDuration(task.getDuration());
	setParent(parent);
	setType("Task");

	// Set color based on task status.
	TaskStatus taskStatus = TaskStatus.of(task.getStatus());
	ButtonElement btnElem = ButtonElement.of(taskStatus.css());
	setColor(btnElem.backgroundColor());
	setTextColor(btnElem.color());
    }

    public GanttBean(Project proj, GanttBean parent) {
	setId(Project.OBJECT_NAME + "-" + proj.getId());
	setText(proj.getName());
	setOpen(true);
	setDuration(0);
	setType("Project");
	setParent(parent.getId());
	setColor(ButtonElement.DEFAULT.backgroundColor());
	setTextColor(ButtonElement.DEFAULT.color());
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
}
