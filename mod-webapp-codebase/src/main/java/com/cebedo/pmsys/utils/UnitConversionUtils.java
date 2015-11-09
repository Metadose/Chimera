package com.cebedo.pmsys.utils;

import java.math.BigDecimal;

import com.cebedo.pmsys.enums.UnitLength;

public class UnitConversionUtils {

    /**
     * Convert the value to meter.
     * 
     * @param lengthUnit
     * @param value
     * @return
     */
    public static BigDecimal convertToMeter(UnitLength lengthUnit, BigDecimal value) {

	// If it's already in meter, return.
	if (lengthUnit == UnitLength.METER) {
	    return value;
	}
	double meterConvert = lengthUnit.getConversionToMeter();
	double convertedValue = meterConvert * value.doubleValue();
	value = new BigDecimal(convertedValue);
	return value;
    }

    public static double convertToMeter(UnitLength lengthUnit, double value) {
	return convertToMeter(lengthUnit, new BigDecimal(value)).doubleValue();
    }
}
