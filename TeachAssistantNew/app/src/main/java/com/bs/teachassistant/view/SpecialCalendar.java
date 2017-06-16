package com.bs.teachassistant.view;

import java.util.Calendar;

/**
 * 闰年月算法
 * <p>
 * Created by limh on 2017/4/8.
 */
public class SpecialCalendar {

    private static int daysOfMonth = 0; // 某月的天数
    private static int dayOfWeek = 0; // 具体某一天是星期几

    // 判断是否为闰年
    public static boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 100 != 0 && year % 4 == 0) {
            return true;
        }
        return false;
    }

    // 得到某月有多少天数
    public static int getDaysOfMonth(boolean isLeapyear, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysOfMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysOfMonth = 30;
                break;
            case 2:
                if (isLeapyear) {
                    daysOfMonth = 29;
                } else {
                    daysOfMonth = 28;
                }

        }
        return daysOfMonth;
    }

    // 指定某年中的某月的第一天是星期几
    public static int getWeekdayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return dayOfWeek;
    }

    // 指定某年中的某月的第一天是星期几
    public static int getWeekdayOfMonth(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return dayOfWeek;
    }

    // 指定某年中的某月的第一天是星期几
    public static int getWeekdayOfMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

}
