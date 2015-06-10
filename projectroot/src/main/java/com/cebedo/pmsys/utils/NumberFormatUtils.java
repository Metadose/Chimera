package com.cebedo.pmsys.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatUtils {

    public static String commaSeparate(double number) {
	return NumberFormat.getNumberInstance(Locale.US).format(number)
		.replaceAll(",", ", ");
    }

    public static NumberFormat getCurrencyFormatter() {
	NumberFormat df = NumberFormat.getCurrencyInstance();
	DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	dfs.setCurrencySymbol("&#8369;");
	dfs.setGroupingSeparator('.');
	dfs.setMonetaryDecimalSeparator('.');
	((DecimalFormat) df).setDecimalFormatSymbols(dfs);
	return df;
    }

}