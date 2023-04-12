package com.schedule.service_schedule.utils;

import com.schedule.service_base.handler.exceptionHandler.ScheduleException;
import org.joda.time.LocalDate;

public class DateUtil {
    public static String dealDateFormat(String date){
        if (date.contains("/")){
            String [] strings=date.split("/");
            if(strings[1].length()<2){
                strings[1]="0"+strings[1];
            }
            if(strings[2].length()<2){
                strings[2]="0"+strings[2];
            }
            date=strings[0]+"-"+strings[1]+"-"+strings[2];
        }else if(date.contains("-")){
            String [] strings=date.split("-");
            if(strings[1].length()<2){
                strings[1]="0"+strings[1];
            }
            if(strings[2].length()<2){
                strings[2]="0"+strings[2];
            }
            date=strings[0]+"-"+strings[1]+"-"+strings[2];
            return date;
        }else {
            throw new ScheduleException(20001,"日期格式不是用/或-间隔，无法转换格式");
        }
        return date;
    }
}
