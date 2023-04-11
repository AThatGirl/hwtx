package com.cj.care.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

public class DataCheckUtils {

    public static final String NOT_HOLIDAY = "不是节日";

    public static String isHoliday(Date date) {
        int year = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        HashMap<LocalDate, String> holidays = new HashMap<>();
        holidays.put(LocalDate.of(year, 1, 1), "新年");
        holidays.put(LocalDate.of(year, 2, 14), "情人节");
        holidays.put(LocalDate.of(year, 4, 1), "愚人节");
        holidays.put(LocalDate.of(year, 4, 4), "清明节");
        holidays.put(LocalDate.of(year, 5, 1), "劳动节");
        holidays.put(LocalDate.of(year, 6, 14), "端午节");
        holidays.put(LocalDate.of(year, 6, 1), "儿童节");
        holidays.put(LocalDate.of(year, 9, 10), "教师节");
        holidays.put(LocalDate.of(year, 10, 1), "国庆节");
        holidays.put(LocalDate.of(year, 12, 25), "圣诞节");
        holidays.put(LocalDate.of(year, 4, 10), "测试节");

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return holidays.getOrDefault(localDate, NOT_HOLIDAY);
    }

    public static boolean isBirthday(String date) {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 将参数日期转换为LocalDate类型
        LocalDate targetDate = LocalDate.parse(date);
        // 判断月份和日是否相同
        return today.getMonth() == targetDate.getMonth() && today.getDayOfMonth() == targetDate.getDayOfMonth();
    }

}
