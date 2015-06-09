package com.cebedo.pmsys.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatUtils {

    public static String commaSeparate(double number) {
	return NumberFormat.getNumberInstance(Locale.US).format(number)
		.replaceAll(",", ", ");
    }

}
