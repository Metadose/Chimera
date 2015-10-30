package com.cebedo.pmsys.utils;

import java.math.BigDecimal;

import com.cebedo.pmsys.enums.UnitCommonLength;

public class UnitConversionUtils {

    /**
     * Convert the value to meter.
     * 
     * @param lengthUnit
     * @param value
     * @return
     */
    public static BigDecimal convertToMeter(UnitCommonLength lengthUnit, BigDecimal value) {

	// If it's already in meter, return.
	if (lengthUnit == UnitCommonLength.METER) {
	    return value;
	}
	double meterConvert = lengthUnit.getConversionToMeter();
	double convertedValue = meterConvert * value.doubleValue();
	value = new BigDecimal(convertedValue);
	return value;
    }

    public static double convertToMeter(UnitCommonLength lengthUnit, double value) {
	return convertToMeter(lengthUnit, new BigDecimal(value)).doubleValue();
    }
}
