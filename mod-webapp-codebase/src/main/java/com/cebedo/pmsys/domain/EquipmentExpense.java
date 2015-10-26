package com.cebedo.pmsys.domain;

import com.cebedo.pmsys.constants.ConstantsRedis;
import com.cebedo.pmsys.constants.RegistryRedisKeys;
import com.cebedo.pmsys.model.Project;

public class EquipmentExpense extends Expense implements IDomainObject {

    private static final long serialVersionUID = -7653621455415987526L;

    public EquipmentExpense() {
	;
    }

    public EquipmentExpense(Project proj) {
	setProject(proj);
	setCompany(proj.getCompany());
    }

    @Override
    public String getKey() {
	return String.format(RegistryRedisKeys.KEY_EQUIPMENT_EXPENSE, super.company.getId(),
		super.project.getId(), super.uuid);
    }

    public static String constructPattern(Project proj) {
	return String.format(RegistryRedisKeys.KEY_EQUIPMENT_EXPENSE, proj.getCompany().getId(),
		proj.getId(), "*");
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof EquipmentExpense ? ((EquipmentExpense) obj).getKey().equals(getKey())
		: false;
    }

    @Override
    public int hashCode() {
	return getKey().hashCode();
    }

    @Override
    public String getObjectName() {
	return ConstantsRedis.DISPLAY_EQUIPMENT_EXPENSE;
    }

}
