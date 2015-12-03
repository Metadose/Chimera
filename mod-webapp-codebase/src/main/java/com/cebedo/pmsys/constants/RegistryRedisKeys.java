package com.cebedo.pmsys.constants;

public class RegistryRedisKeys {

    /**
     * Auxiliaries.
     */
    public static final String KEY_AUX_PROJECT = "company.fk:%s:project:%s";

    public static final String KEY_AUX_USER = "company.fk:%s:user:%s";

    /**
     * Company > Project > Staff.
     */
    public static final String KEY_ATTENDANCE = "company.fk:%s:project.fk:%s:staff.fk:%s:attendance:date:%s:status:%s";

    /**
     * Company > Project.
     */
    public static final String KEY_ESTIMATION_OUTPUT = "company.fk:%s:project.fk:%s:estimation.output:%s";

    public static final String KEY_PROJECT_PAYROLL = "company.fk:%s:project.fk:%s:payroll:%s";

    public static final String KEY_ESTIMATED_COST = "company.fk:%s:project.fk:%s:cost:%s";

    public static final String KEY_EXPENSE = "company.fk:%s:project.fk:%s:expense:%s";

    public static final String KEY_EQUIPMENT_EXPENSE = "company.fk:%s:project.fk:%s:equipmentexpense:%s";

    /**
     * Inventory.
     */
    public static final String KEY_DELIVERY = "company.fk:%s:project.fk:%s:delivery:%s";

    public static final String KEY_MATERIAL = "company.fk:%s:project.fk:%s:delivery.fk:%s:material:%s";

    public static final String KEY_PULL_OUT = "company.fk:%s:project.fk:%s:delivery.fk:%s:material.fk:%s:pullout:%s";

}
