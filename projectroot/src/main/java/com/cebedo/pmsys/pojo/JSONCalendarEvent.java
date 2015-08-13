package com.cebedo.pmsys.pojo;

public class JSONCalendarEvent {

    private String id;
    private String title;
    private String start;
    private String end;
    private String className;
    private String borderColor;
    private String attendanceStatus;
    private String attendanceWage;

    public String getAttendanceStatus() {
	return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
	this.attendanceStatus = attendanceStatus;
    }

    public String getAttendanceWage() {
	return attendanceWage;
    }

    public void setAttendanceWage(String attendanceWage) {
	this.attendanceWage = attendanceWage;
    }

    public String getBorderColor() {
	return borderColor;
    }

    public void setBorderColor(String borderColor) {
	this.borderColor = borderColor;
    }

    public String getClassName() {
	return className;
    }

    public void setClassName(String className) {
	this.className = className;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getStart() {
	return start;
    }

    public void setStart(String start) {
	this.start = start;
    }

    public String getEnd() {
	return end;
    }

    public void setEnd(String end) {
	this.end = end;
    }

}
