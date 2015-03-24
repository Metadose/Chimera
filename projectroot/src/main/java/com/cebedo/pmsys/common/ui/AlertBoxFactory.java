package com.cebedo.pmsys.common.ui;

import com.cebedo.pmsys.common.SystemConstants;

public class AlertBoxFactory {

	public static AlertBoxFactory SUCCESS = new AlertBoxFactory(
			SystemConstants.UI_STATUS_SUCCESS);

	private final String CONFIG_ALERT_STATUS = "ALERT_STATUS";
	private final String CONFIG_ALERT_HEADER = "ALERT_HEADER";
	private final String CONFIG_ALERT_MESSAGE = "ALERT_MESSAGE";
	private final String CONFIG_ALERT_ICON = "ALERT_ICON";

	private final String TEMPLATE = "<div class=\"alert alert-"
			+ CONFIG_ALERT_STATUS
			+ " alert-dismissable\"><i class=\"fa fa-"
			+ CONFIG_ALERT_ICON
			+ "\"></i><button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button><h4>"
			+ CONFIG_ALERT_HEADER + "</h4>" + CONFIG_ALERT_MESSAGE + "</div>";

	private String status;
	private String icon;
	private String header;
	private String message;

	public AlertBoxFactory(String stat) {
		setStatus(stat);
	}

	public AlertBoxFactory(String stat, String msg) {
		setStatus(stat);
		setMessage(msg);
	}

	public AlertBoxFactory() {
		;
	}

	public String generateHTML() {
		String output = TEMPLATE.replace(CONFIG_ALERT_STATUS, getStatus());
		output = output.replace(CONFIG_ALERT_ICON, getIcon());
		output = output.replace(CONFIG_ALERT_HEADER, getHeader());
		output = output.replace(CONFIG_ALERT_MESSAGE, getMessage());
		return output;
	}

	public String getStatus() {
		if (status == null) {
			return "info";
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIcon() {
		if (icon != null) {
			return icon;
		} else if (this.status.equals(SystemConstants.UI_STATUS_INFO)) {
			setIcon("info");
			return icon;
		} else if (this.status.equals(SystemConstants.UI_STATUS_DANGER)) {
			setIcon("ban");
			return icon;
		} else if (this.status.equals(SystemConstants.UI_STATUS_WARNING)) {
			setIcon("warning");
			return icon;
		} else if (this.status.equals(SystemConstants.UI_STATUS_SUCCESS)) {
			setIcon("check");
			return icon;
		}
		return "info";
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHeader() {
		if (header != null) {
			return header;
		} else if (this.status.equals(SystemConstants.UI_STATUS_INFO)) {
			setHeader("Notice");
			return header;
		} else if (this.status.equals(SystemConstants.UI_STATUS_DANGER)) {
			setHeader("Error!");
			return header;
		} else if (this.status.equals(SystemConstants.UI_STATUS_WARNING)) {
			setHeader("Warning");
			return header;
		} else if (this.status.equals(SystemConstants.UI_STATUS_SUCCESS)) {
			setHeader("Success!");
			return header;
		}
		return "Notification";
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getMessage() {
		if (message == null) {
			return "We hope you're having a great time!";
		}
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
