package com.cebedo.pmsys.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cebedo.pmsys.constants.RegistryErrorCodes;
import com.cebedo.pmsys.constants.RegistryResponseMessage;
import com.cebedo.pmsys.domain.Material;

public class MaterialValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return Material.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	Material targetObj = (Material) target;

	// TODO Check the material object for more validations.
	// Can only choose one unit of measure for each material.
	int unitCount = 0;
	unitCount = targetObj.getUnitLength() == null ? unitCount : unitCount + 1;
	unitCount = targetObj.getUnitMass() == null ? unitCount : unitCount + 1;
	unitCount = targetObj.getUnitVolume() == null ? unitCount : unitCount + 1;
	if (unitCount > 1) {
	    errors.reject(RegistryErrorCodes.MATERIAL_UNIT,
		    RegistryResponseMessage.ERROR_PROJECT_MATERIAL_MORE_THAN_ONE_UNIT);
	}
    }

}
