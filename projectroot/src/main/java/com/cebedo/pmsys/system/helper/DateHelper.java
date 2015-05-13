package com.cebedo.pmsys.system.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

	/**
	 * Format a date.
	 * 
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

}
