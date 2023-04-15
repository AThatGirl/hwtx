package com.schedule.service_schedule.utils;

import com.schedule.service_base.handler.exceptionHandler.ScheduleException;
import com.schedule.service_schedule.entity.dto.EmployeePreferenceDto;
import com.schedule.service_schedule.entity.dto.RunDateTimeDto;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.List;

@Slf4j
public class TimeUtil {
    //将string的时间转换成LoclalTime：格式 hh:mm:ss
    public static LocalTime stringTimeToLocalTime(String time){
        LocalTime localTime=null;
        try {
            String[] time2 = time.split(":");
            log.info(time2[0]+"  "+time2[1]);
            if(time2[0].length()==1){
                if (time2[0].substring(0, 1) != "0") {
                    time = "0" + time;
                    //System.out.println(time);
                    log.info(time);
                }
            }
            localTime = LocalTime.parse(time);
        }catch (Exception e){
            throw new ScheduleException(20001,"String时间转换成LocalTime失败:"+e.getMessage());
        }
        return localTime;
    }
    /**
     * 计算一个字符串时间减去一个实型的时间比如：8：00的一个半小时前  8：00 ，1.5
     * @param time 字符串时间
     * @param num  实型例如：一个半小时后1.5  一个半小时前-1.5
     * @return String 字符串时间
     */
    public static String CalculateTime(String time,double num){
        LocalTime localTime=null;
        localTime=stringTimeToLocalTime(time);
        localTime = localTime.plusMinutes((long) (num * 60));
        return localTime.toString();
    }
    //获取runDateTimeVo星期的每天工作时间范围
    public static void setTime(List<RunDateTimeDto> weekList, String s) {
        String[] timeString=s.split("-");
        if(!timeString[0].contains(":")||!timeString[1].contains(":")){
            throw new ScheduleException(20001,"要处理的时间格式不对，需要的格式为：hh:mm");
        }
        //给星期配置时间范围
        for(RunDateTimeDto runDateTimeDto :weekList){
            runDateTimeDto.setStartTime(timeString[0]);
            runDateTimeDto.setEndTime(timeString[1]);
        }
    }

    //获取runDateTimeVo星期的每天工作时间范围
    public static void preferenceSetTime(List<EmployeePreferenceDto> weekList, String s) {
        String[] timeString=s.split("-");
        if(!timeString[0].contains(":")||!timeString[1].contains(":")){
            throw new ScheduleException(20001,"要处理的时间格式不对，需要的格式为：hh:mm");
        }
        //给星期配置时间范围
        for(EmployeePreferenceDto preferenceVo:weekList){
            preferenceVo.setStartTime(timeString[0]);
            preferenceVo.setEndTime(timeString[1]);
        }
    }

    //进行两时间比较-1 0 1 表示 小于 等于 大于
    public static int compareToStringTime(String time1,String time2){
        LocalTime localTime1=TimeUtil.stringTimeToLocalTime(time1);
        LocalTime localTime2=TimeUtil.stringTimeToLocalTime(time2);
        return localTime1.compareTo(localTime2);
    }
    public static double calculateStringTimeSub(String time1,String time2){
        LocalTime localTime1=TimeUtil.stringTimeToLocalTime(time1);
        LocalTime localTime2=TimeUtil.stringTimeToLocalTime(time2);
        double subTime=0;
        if(localTime1.compareTo(localTime2)==1){
            subTime= localTime1.toSecondOfDay()-localTime2.toSecondOfDay();
            subTime=subTime/60.0/60.0;
            if(subTime>=20){
                subTime=24*60*60.0-localTime1.toSecondOfDay()+localTime2.toSecondOfDay();
                subTime=subTime/60.0/60.0;
            }

        }else{
            subTime= localTime2.toSecondOfDay()-localTime1.toSecondOfDay();
            subTime=subTime/60.0/60.0;
        }
        return subTime;
    }
}
