package com.cj.manage.utils;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateCheckUtils {

    public static boolean isWithinWeek(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(dateString, formatter);
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusWeeks(1);
        return date.toLocalDate().isAfter(weekAgo) || date.toLocalDate().isEqual(weekAgo)
                && date.toLocalDate().isBefore(today) || date.toLocalDate().isEqual(today);
    }

    public static boolean isWithinOneDay(String dateString1, String dateString2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(dateString1.substring(0, 10), formatter);
        LocalDate date2 = LocalDate.parse(dateString2.substring(0, 10), formatter);
        return date2.isEqual(date1.minusDays(1));
    }

    public static int getDaysBetween(String dateString1, String dateString2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(dateString1.substring(0, 10), formatter);
        LocalDate date2 = LocalDate.parse(dateString2.substring(0, 10), formatter);
        return Math.abs((int) ChronoUnit.DAYS.between(date1, date2));
    }

    public static String getTodayDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        return today.format(formatter);
    }
}
