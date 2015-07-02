package com.cebedo.pmsys.constants;

public class RedisKeyRegistry {

    public static final String KEY_ATTENDANCE = "company.fk:%s:staff.fk:%s:attendance:date:%s:status:%s";

    public static final String KEY_CONCRETE_PROPORTION = "company.fk:%s:concreteproportion:%s";

    public static final String KEY_CHB = "company.fk:%s:chb:%s";

    public static final String KEY_ESTIMATION_ALLOWANCE = "company.fk:%sestimationallowance:%s";

    public static final String KEY_BLOCK_LAYING_MIXTURE = "company.fk:%sblocklayingmixture:%s";

    public static final String KEY_MATERIAL_CATEGORY = "company.fk:%s:materialcategory:%s";

    public static final String KEY_UNITS = "company.fk:%s:units:%s";

    public static final String KEY_ESTIMATE = "company.fk:%s:project.fk:%s:estimate:%s";

    public static final String KEY_MASONRY_CHB_ESTIMATION_SUMMARY = "company.fk:%s:project.fk:%s:masonrychbestimationsummary:%s";

    public static final String KEY_CONCRETE_ESTIMATION_SUMMARY = "company.fk:%s:project.fk:%s:concreteestimationsummary:%s";

    public static final String KEY_PROJECT_AUX = "company.fk:%s:project:%s";

    /**
     * 2nd-level dependents.
     */
    public static final String KEY_PROJECT_PAYROLL = "company.fk:%s:project.fk:%s:payroll:%s";
    public static final String KEY_SHAPE = "company.fk:%s:shape:%s";

    /**
     * Order of dependency. 2nd, 3rd, 4th.
     */
    public static final String KEY_DELIVERY = "company.fk:%s:project.fk:%s:delivery:%s";
    public static final String KEY_MATERIAL = "company.fk:%s:project.fk:%s:delivery.fk:%s:material:%s";
    public static final String KEY_PULL_OUT = "company.fk:%s:project.fk:%s:delivery.fk:%s:material.fk:%s:pullout:%s";
}
