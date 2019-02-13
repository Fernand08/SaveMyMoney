package com.demo.savemymoney.common.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    public static boolean isMonth(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (cal.get(Calendar.MONTH) + 1) == month;
    }

    public static boolean isYear(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) == month;
    }

    public static long getMillisUntil(Date date) {
        return date.getTime() - new Date().getTime();
    }
}
