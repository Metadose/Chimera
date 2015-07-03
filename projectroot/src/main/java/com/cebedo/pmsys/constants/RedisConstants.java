package com.cebedo.pmsys.constants;

public abstract class RedisConstants {

    /**
     * Redis objects.
     */
    public static final String OBJECT_CONVERSATION = "conversation";
    public static final String OBJECT_ATTENDANCE = "attendance";
    public static final String OBJECT_MESSAGE = "message";
    public static final String OBJECT_NOTIFICATION = "notification";
    public static final String OBJECT_PAYROLL = "payroll";
    public static final String OBJECT_MATERIAL = "material";
    public static final String OBJECT_SHAPE = "shape";
    public static final String OBJECT_DELIVERY = "delivery";
    public static final String OBJECT_ESTIMATE = "estimate";
    public static final String OBJECT_PROJECT_AUX = "projectAux";
    public static final String OBJECT_PULL_OUT = "pullout";
    public static final String OBJECT_UNIT = "unit";
    public static final String OBJECT_PLASTER_MIXTURE = "plastermixture";
    public static final String OBJECT_CHB = "chb";
    public static final String OBJECT_BLOCK_LAYING_MIXTURE = "blocklayingmixture";
    public static final String OBJECT_BLOCK_LAYING_MIXTURE_DISPLAY = "block laying mixture";
    public static final String OBJECT_ESTIMATION_ALLOWANCE = "estimationallowance";
    public static final String OBJECT_ESTIMATION_ALLOWANCE_DISPLAY = "estimation allowance";
    public static final String OBJECT_MATERIAL_CATEGORY = "materialcategory";

    public static final String OBJECT_MASONRY_CHB_ESTIMATION_SUMMARY = "masonrychbestimationsummary";
    public static final String OBJECT_MASONRY_CHB_ESTIMATION_SUMMARY_DISPLAY = "masonry (CHB) estimation summary";

    public static final String OBJECT_CONCRETE_ESTIMATION_SUMMARY = "concreteestimationsummary";
    public static final String OBJECT_CONCRETE_ESTIMATION_SUMMARY_DISPLAY = "concrete estimation summary";
    public static final String OBJECT_CONCRETE_PROPORTION = "concreteproportion";
    public static final String OBJECT_CONCRETE_PROPORTION_DISPLAY = "concrete proportion";
    public static final String OBJECT_MATERIAL_CATEGORY_DISPLAY = "material category";

    /**
     * Redis objects Edit and List pages.
     */
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
    public static final String JSP_CHB_LIST = OBJECT_CHB + "/chbList";
    public static final String JSP_CHB_EDIT = OBJECT_CHB + "/chbEdit";
    public static final String JSP_PLASTER_MIXTURE_LIST = OBJECT_PLASTER_MIXTURE
	    + "/plasterMixtureList";
    public static final String JSP_PLASTER_MIXTURE_EDIT = OBJECT_PLASTER_MIXTURE
	    + "/plasterMixtureEdit";
    public static final String JSP_BLOCK_LAYING_MIXTURE_LIST = OBJECT_BLOCK_LAYING_MIXTURE
	    + "/blockLayingMixtureList";
    public static final String JSP_BLOCK_LAYING_MIXTURE_EDIT = OBJECT_BLOCK_LAYING_MIXTURE
	    + "/blockLayingMixtureEdit";
    public static final String JSP_UNIT_LIST = OBJECT_UNIT + "/unitList";
    public static final String JSP_UNIT_EDIT = OBJECT_UNIT + "/unitEdit";
    public static final String JSP_ESTIMATION_ALLOWANCE_LIST = OBJECT_ESTIMATION_ALLOWANCE
	    + "/estimationAllowanceList";
    public static final String JSP_ESTIMATION_ALLOWANCE_EDIT = OBJECT_ESTIMATION_ALLOWANCE
	    + "/estimationAllowanceEdit";
    public static final String JSP_ESTIMATE_EDIT = OBJECT_ESTIMATE
	    + "/estimateEdit";
    public static final String JSP_MATERIAL_CATEGORY_LIST = OBJECT_MATERIAL_CATEGORY
	    + "/materialCategoryList";
    public static final String JSP_MATERIAL_CATEGORY_EDIT = OBJECT_MATERIAL_CATEGORY
	    + "/materialCategoryEdit";
    public static final String JSP_CONCRETE_PROPORTION_LIST = OBJECT_CONCRETE_PROPORTION
	    + "/concreteProportionList";
    public static final String JSP_CONCRETE_PROPORTION_EDIT = OBJECT_CONCRETE_PROPORTION
	    + "/concreteProportionEdit";
}
