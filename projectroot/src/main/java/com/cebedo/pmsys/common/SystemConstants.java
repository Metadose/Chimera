package com.cebedo.pmsys.common;

import com.cebedo.pmsys.project.model.Project;

public abstract class SystemConstants {

	public static final String CONTROLLER_REDIRECT = "redirect:/";

	public static final String ORIGIN = "origin";
	public static final String ORIGIN_ID = "originID";

	public static final String ATTR_ACTION = "action";

	public static final String REQUEST_ROOT = "/";
	public static final String REQUEST_LIST = "list";
	public static final String REQUEST_EDIT = "edit";
	public static final String REQUEST_DELETE = "delete";
	public static final String REQUEST_CREATE = "create";
	public static final String REQUEST_UPDATE = "update";
	public static final String REQUEST_MARK = "mark";
	public static final String REQUEST_DOWNLOAD = "download";
	public static final String REQUEST_ASSIGN = "assign";

	public static final String REQUEST_UPLOAD_FILE = "upload/file";
	public static final String REQUEST_UPLOAD_FILE_TO_PROJECT = "upload/file/project";
	public static final String REQUEST_UPLOAD_TO_PROJECT = "upload/project";
	public static final String REQUEST_UPLOAD_TO_PROJECT_PROFILE = "upload/project/profile";
	public static final String REQUEST_UPLOAD_TO_STAFF_PROFILE = "upload/staff/profile";

	public static final String PROJECT_PROFILE = "project/profile";
	public static final String STAFF_PROFILE = "staff/profile";

	public static final String REQUEST_DISPLAY_PROJECT_PROFILE = "display/project/profile";
	public static final String REQUEST_DISPLAY_STAFF_PROFILE = "display/staff/profile";
	public static final String REQUEST_DISPLAY = "display";

	public static final String FROM = "from";
	public static final String FROM_PROJECT = FROM + "/" + Project.OBJECT_NAME;

	public static final String REQUEST_ASSIGN_PROJECT = "assign/project";
	public static final String REQUEST_UNASSIGN_PROJECT = "unassign/project";
	public static final String REQUEST_UNASSIGN_PROJECT_ALL = "unassign/project/all";
	public static final String REQUEST_UPDATE_ASSIGNED_PROJECT_FIELD = "update/assigned/project";

	public static final String ACTION_CREATE = "Create";
	public static final String ACTION_EDIT = "Edit";
	public static final String ACTION_UPDATE = "Update";
	public static final String ACTION_DELETE = "Delete";
	public static final String ACTION_LIST = "List";
	public static final String ACTION_ASSIGN = "Assign";
}
