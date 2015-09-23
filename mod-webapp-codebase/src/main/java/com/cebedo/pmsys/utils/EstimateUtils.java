package com.cebedo.pmsys.utils;

public class EstimateUtils {

    public static double convert40kgTo50kg(double value40kg) {
	return value40kg - (value40kg * 0.2);
    }

}
