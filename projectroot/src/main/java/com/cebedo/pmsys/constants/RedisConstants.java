package com.cebedo.pmsys.constants;

public abstract class RedisConstants {

    public static final String OBJECT_CONVERSATION = "conversation";
    public static final String OBJECT_ATTENDANCE = "attendance";
    public static final String OBJECT_MESSAGE = "message";
    public static final String OBJECT_NOTIFICATION = "notification";
    public static final String OBJECT_PAYROLL = "payroll";
    public static final String OBJECT_MATERIAL = "material";
    public static final String OBJECT_FORMULA = "formula";
    public static final String OBJECT_DELIVERY = "delivery";
    public static final String OBJECT_PROJECT_AUX = "projectAux";
    public static final String OBJECT_PULL_OUT = "pullOut";

    public static final String JSP_DELIVERY_EDIT = OBJECT_DELIVERY
	    + "/deliveryEdit";
    public static final String JSP_ATTENDANCE_EDIT = OBJECT_ATTENDANCE
	    + "/attendanceEdit";
    public static final String JSP_PAYROLL_EDIT = OBJECT_PAYROLL
	    + "/payrollEdit";
    public static final String JSP_MATERIAL_EDIT = OBJECT_MATERIAL
	    + "/materialEdit";
    public static final String JSP_MATERIAL_PULLOUT = OBJECT_MATERIAL
	    + "/materialPullout";
    public static final String JSP_ATTENDANCE_LIST = OBJECT_ATTENDANCE
	    + "/attendanceList";

}
