package com.cebedo.pmsys.helper;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberHelper {

    public static String commaSeparate(double number) {
	return NumberFormat.getNumberInstance(Locale.US).format(number)
		.replaceAll(",", ", ");
    }

}
