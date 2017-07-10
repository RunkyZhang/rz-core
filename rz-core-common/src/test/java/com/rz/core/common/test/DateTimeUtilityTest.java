package com.rz.core.common.test;

import java.util.Date;

import com.rz.core.utils.DateTimeUtility;

public class DateTimeUtilityTest {

    public static void main(String[] args) {
        DateTimeUtilityTest dateTimeUtilityTest = new DateTimeUtilityTest();
        dateTimeUtilityTest.test();

        for(int i = 0; i < 356; i++) {
            System.out.println(DateTimeUtility.fristDayOfWeek(DateTimeUtility.addDay(new Date(), i)));
        }

        System.out.println("End DateTimeUtilsTest...");
    }

    private void test() {
        Date date = DateTimeUtility.getNow();
        
        this.show(DateTimeUtility.getMinDate());
        this.show(DateTimeUtility.getMaxDate());

        this.show(date);
        this.show(DateTimeUtility.toYear(date));
        this.show(DateTimeUtility.toMonth(date));
        this.show(DateTimeUtility.toDay(date));
        this.show(DateTimeUtility.getToday());

        date = DateTimeUtility.toDate(2016, 1, 22, 12, 11, 10, 900);
        this.show(date);
        this.show(DateTimeUtility.addYear(date, 100));
        this.show(DateTimeUtility.addMonth(date, 100));
        this.show(DateTimeUtility.addDay(date, 100));
        this.show(DateTimeUtility.addHour(date, 100));
        this.show(DateTimeUtility.addMinute(date, 100));
        this.show(DateTimeUtility.addSecond(date, 100));
        this.show(DateTimeUtility.addMillisecond(date, 100));
    }

    private void show(Date date) {
        String value = "";
        value += DateTimeUtility.getYear(date) + " ";
        value += DateTimeUtility.getMonth(date) + " ";
        value += DateTimeUtility.getDay(date) + " ";
        value += DateTimeUtility.getHour(date) + " ";
        value += DateTimeUtility.getMinute(date) + " ";
        value += DateTimeUtility.getSecond(date) + " ";
        value += DateTimeUtility.getMillisecond(date) + " ";
        System.out.println(value);
        System.out.println(DateTimeUtility.toString(date, DateTimeUtility.DATE_FORMAT3));
        System.out.println(DateTimeUtility.dayOfWeek(date));
        System.out.println(DateTimeUtility.dayOfYear(date));
        System.out.println(date.getTime());
        System.out.println("--------------------------------------------");
    }
}
