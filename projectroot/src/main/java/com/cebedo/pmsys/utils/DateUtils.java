package com.cebedo.pmsys.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateUtils {

    public static Date convertStringToDate(String dateString) throws ParseException {
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date date = formatter.parse(dateString);
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.getTime();
    }

    /**
     * Reference:
     * http://stackoverflow.com/questions/3083781/start-and-end-date-of
     * -a-current-month
     * 
     * @return
     */
    public static Date getCurrentMonthDateEnd() {
	Calendar calendar = getCalendarForNow();
	calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	setTimeToEndofDay(calendar);
	Date end = calendar.getTime();
	return end;
    }

    /**
     * Reference:
     * http://stackoverflow.com/questions/3083781/start-and-end-date-of
     * -a-current-month
     * 
     * @return
     */
    public static Date getCurrentMonthDateStart() {
	Calendar calendar = getCalendarForNow();
	calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
	setTimeToBeginningOfDay(calendar);
	Date begining = calendar.getTime();
	return begining;
    }

    private static Calendar getCalendarForNow() {
	Calendar calendar = GregorianCalendar.getInstance();
	calendar.setTime(new Date());
	return calendar;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
	calendar.set(Calendar.HOUR_OF_DAY, 23);
	calendar.set(Calendar.MINUTE, 59);
	calendar.set(Calendar.SECOND, 59);
	calendar.set(Calendar.MILLISECOND, 999);
    }

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

    public static String formatDate(Date date) {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
	return formatter.format(date);
    }

    /**
     * Add days to date.<br>
     * Reference:
     * http://stackoverflow.com/questions/428918/how-can-i-increment-a
     * -date-by-one-day-in-java
     * 
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.add(Calendar.DATE, days); // minus number would decrement the days
	return cal.getTime();
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
