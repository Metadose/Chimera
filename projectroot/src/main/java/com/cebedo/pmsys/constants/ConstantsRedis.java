package com.cebedo.pmsys.constants;

import com.cebedo.pmsys.model.Project;

public abstract class ConstantsRedis {

    // Redis objects.
    public static final String OBJECT_ATTENDANCE = "attendance";
    public static final String OBJECT_PAYROLL = "payroll";
    public static final String OBJECT_MATERIAL = "material";
    public static final String OBJECT_SHAPE = "shape";
    public static final String OBJECT_DELIVERY = "delivery";
    public static final String OBJECT_ESTIMATE = "estimate";
    public static final String OBJECT_ESTIMATION_OUTPUT = "estimationoutput";
    public static final String OBJECT_PROJECT_AUX = "projectAux";
    public static final String OBJECT_PULL_OUT = "pullout";
    public static final String OBJECT_MASONRY_CHB_ESTIMATION_SUMMARY = "masonrychbestimationsummary";
    public static final String OBJECT_CONCRETE_ESTIMATION_SUMMARY = "concreteestimationsummary";

    // Redis objects Edit and List pages.
    public static final String JSP_ESTIMATION_OUTPUT_EDIT = Project.OBJECT_NAME
	    + "/estimationOutputEdit";
    public static final String JSP_DELIVERY_EDIT = Project.OBJECT_NAME + "/deliveryEdit";
    public static final String JSP_PAYROLL_EDIT = Project.OBJECT_NAME + "/payrollEdit";
    public static final String JSP_MATERIAL_EDIT = Project.OBJECT_NAME + "/materialEdit";
    public static final String JSP_MATERIAL_PULLOUT = Project.OBJECT_NAME + "/materialPulloutEdit";

}
