package com.cebedo.pmsys.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

	/**
	 * Reference:
	 * http://stackoverflow.com/questions/2689379/how-to-get-a-list-of
	 * -dates-between-two-dates-in-java
	 * 
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	public static List<Date> getDatesBetweenDates(Date startdate, Date enddate) {
		List<Date> dates = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startdate);
		while (calendar.getTime().before(enddate)) {
			Date result = calendar.getTime();
			dates.add(result);
			calendar.add(Calendar.DATE, 1);
		}
		dates.add(enddate);
		return dates;
	}

	public static int getDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}

}
