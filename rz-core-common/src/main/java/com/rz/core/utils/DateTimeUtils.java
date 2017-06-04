package com.rz.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
    /*
     * yyyy-MM-dd
     */
    public final static String DATE_FORMAT1 = "yyyy-MM-dd";
    /*
     * yyyy-MM-dd HH:mm:ss
     */
    public final static String DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";
    /*
     * yyyy-MM-dd HH:mm:ss SSS
     */
    public final static String DATE_FORMAT3 = "yyyy-MM-dd HH:mm:ss SSS";
    
    public static Date toDate(int year, int month, int day) {
        return DateTimeUtils.toDate(year, month, day, 0, 0, 0, 0);
    }

    public static Date toDate(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, millisecond);

        return calendar.getTime();
    }

    public static String toString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        return simpleDateFormat.format(date);
    }

    public static Date toYear(Date date) {
        return DateTimeUtils.toDate(DateTimeUtils.getYear(date), 1, 1);
    }

    public static Date toMonth(Date date) {
        return DateTimeUtils.toDate(DateTimeUtils.getYear(date), DateTimeUtils.getMonth(date), 1);
    }

    public static Date toDay(Date date) {
        return DateTimeUtils.toDate(DateTimeUtils.getYear(date), DateTimeUtils.getMonth(date), DateTimeUtils.getDay(date));
    }

    public static Date getNow() {
        return new Date();
    }

    public static Date getToday() {
        Date now = DateTimeUtils.getNow();

        return DateTimeUtils.toDate(DateTimeUtils.getYear(now), DateTimeUtils.getMonth(now), DateTimeUtils.getDay(now));
    }
    
    public static Date getMinDate() {
        return DateTimeUtils.toDate(1, 1, 1);
    }

    public static Date getMaxDate() {
        return DateTimeUtils.toDate(9999, 12, 31, 23, 59, 59, 999);
    }
    
    public static double totalDays(Date date1, Date date2) {
        double difference = date1.getTime() - date2.getTime();

        return difference / (1000 * 60 * 60 * 24);
    }

    public static double totalHours(Date date1, Date date2) {
        double difference = date1.getTime() - date2.getTime();

        return difference / (1000 * 60 * 60);
    }

    public static double totalMinutes(Date date1, Date date2) {
        double difference = date1.getTime() - date2.getTime();

        return difference / (1000 * 60);
    }

    public static double totalSeconds(Date date1, Date date2) {
        double difference = date1.getTime() - date2.getTime();

        return difference / 1000;
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DATE);
    }

    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.SECOND);
    }

    public static int getMillisecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MILLISECOND);
    }

    public static Date addYear(Date date, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);

        return calendar.getTime();
    }

    public static Date addMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);

        return calendar.getTime();
    }

    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);

        return calendar.getTime();
    }

    public static Date addHour(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);

        return calendar.getTime();
    }

    public static Date addMinute(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);

        return calendar.getTime();
    }

    public static Date addSecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);

        return calendar.getTime();
    }

    public static Date addMillisecond(Date date, int millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, millisecond);

        return calendar.getTime();
    }
    
    public static int dayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static int dayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
