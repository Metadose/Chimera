package com.cebedo.pmsys.constants;

public class RedisKeyRegistry {

    public static final String KEY_ATTENDANCE = "company.fk:%s:staff.fk:%s:attendance:date:%s:status:%s";

    public static final String KEY_ESTIMATION_OUTPUT = "company.fk:%s:project.fk:%s:estimation.output:%s";

    public static final String KEY_MASONRY_CHB_ESTIMATION_SUMMARY = "company.fk:%s:project.fk:%s:masonrychbestimationsummary:%s";

    public static final String KEY_CONCRETE_ESTIMATION_SUMMARY = "company.fk:%s:project.fk:%s:concreteestimationsummary:%s";

    public static final String KEY_PROJECT_AUX = "company.fk:%s:project:%s";

    /**
     * 2nd-level dependents.
     */
    public static final String KEY_PROJECT_PAYROLL = "company.fk:%s:project.fk:%s:payroll:%s";

    /**
     * Order of dependency. 2nd, 3rd, 4th.
     */
    public static final String KEY_DELIVERY = "company.fk:%s:project.fk:%s:delivery:%s";
    public static final String KEY_MATERIAL = "company.fk:%s:project.fk:%s:delivery.fk:%s:material:%s";
    public static final String KEY_PULL_OUT = "company.fk:%s:project.fk:%s:delivery.fk:%s:material.fk:%s:pullout:%s";
}
