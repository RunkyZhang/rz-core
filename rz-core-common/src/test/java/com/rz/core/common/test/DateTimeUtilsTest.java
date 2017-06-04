package com.rz.core.common.test;

import java.util.Date;

import com.rz.core.utils.DateTimeUtils;

public class DateTimeUtilsTest {

    public static void main(String[] args) {
        DateTimeUtilsTest dateTimeUtilsTest = new DateTimeUtilsTest();
        dateTimeUtilsTest.test();

        System.out.println("End DateTimeUtilsTest...");
    }

    private void test() {
        Date date = DateTimeUtils.getNow();
        
        this.show(DateTimeUtils.getMinDate());
        this.show(DateTimeUtils.getMaxDate());

        this.show(date);
        this.show(DateTimeUtils.toYear(date));
        this.show(DateTimeUtils.toMonth(date));
        this.show(DateTimeUtils.toDay(date));
        this.show(DateTimeUtils.getToday());

        date = DateTimeUtils.toDate(2016, 1, 22, 12, 11, 10, 900);
        this.show(date);
        this.show(DateTimeUtils.addYear(date, 100));
        this.show(DateTimeUtils.addMonth(date, 100));
        this.show(DateTimeUtils.addDay(date, 100));
        this.show(DateTimeUtils.addHour(date, 100));
        this.show(DateTimeUtils.addMinute(date, 100));
        this.show(DateTimeUtils.addSecond(date, 100));
        this.show(DateTimeUtils.addMillisecond(date, 100));
    }

    private void show(Date date) {
        String value = "";
        value += DateTimeUtils.getYear(date) + " ";
        value += DateTimeUtils.getMonth(date) + " ";
        value += DateTimeUtils.getDay(date) + " ";
        value += DateTimeUtils.getHour(date) + " ";
        value += DateTimeUtils.getMinute(date) + " ";
        value += DateTimeUtils.getSecond(date) + " ";
        value += DateTimeUtils.getMillisecond(date) + " ";
        System.out.println(value);
        System.out.println(DateTimeUtils.toString(date, DateTimeUtils.DATE_FORMAT3));
        System.out.println(DateTimeUtils.dayOfWeek(date));
        System.out.println(DateTimeUtils.dayOfYear(date));
        System.out.println(date.getTime());
        System.out.println("--------------------------------------------");
    }
}
