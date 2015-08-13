package com.cebedo.pmsys.utils;

import java.math.BigDecimal;

import com.cebedo.pmsys.enums.CommonLengthUnit;

public class UnitConversionUtils {

    /**
     * Convert the value to meter.
     * 
     * @param lengthUnit
     * @param value
     * @return
     */
    public static BigDecimal convertToMeter(CommonLengthUnit lengthUnit, BigDecimal value) {

	// If it's already in meter, return.
	if (lengthUnit == CommonLengthUnit.METER) {
	    return value;
	}
	double meterConvert = lengthUnit.conversionToMeter();
	double convertedValue = meterConvert * value.doubleValue();
	value = new BigDecimal(convertedValue);
	return value;
    }

    public static double convertToMeter(CommonLengthUnit lengthUnit, double value) {
	return convertToMeter(lengthUnit, new BigDecimal(value)).doubleValue();
    }
}
