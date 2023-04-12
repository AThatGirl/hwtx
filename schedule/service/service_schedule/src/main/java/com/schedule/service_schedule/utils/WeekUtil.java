package com.schedule.service_schedule.utils;

import com.schedule.service_base.handler.exceptionHandler.ScheduleException;
import com.schedule.service_schedule.entity.dto.EmployeePreferenceDto;
import com.schedule.service_schedule.entity.dto.RunDateTimeDto;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.*;

@Slf4j
public class WeekUtil {
    public static final String Monday="Mon";
    public static final String Tuesday="Tue";
    public static final String Wednesday="Wed";
    public static final String Thursday="Thu";
    public static final String Friday="Fri";
    public static final String Saturday="Sat";
    public static final String Sunday="Sun";
    public static final List<String> Week=new ArrayList<>();
    static {
        Week.add(Monday);
        Week.add(Tuesday);
        Week.add(Wednesday);
        Week.add(Thursday);
        Week.add(Friday);
        Week.add(Saturday);
        Week.add(Sunday);
    }
    //获得星期几通过日期
    public static String dealDateToStringWeek(String date){
        LocalDate localDate=LocalDate.parse(DateUtil.dealDateFormat(date));
        String week=localDate.getDayOfWeek().toString();
        week=week.substring(0,3).toLowerCase();
        String s= week.substring(0,1);
        week=week.substring(1,3);
        s=s.toUpperCase();
        week=s+week;
        return week;
    }

    //将String类型的星期处理成runDateTimeVo类
    public static List<RunDateTimeDto> getWeek(String value){
        List<RunDateTimeDto> list=new ArrayList<>();
        //需要判断是否有不连续的星期使用,隔开的
        /**
         * Mon-Fri   Mon,Fri-Sun
         */
        String[] weeks=value.split(",");
        for(String s:weeks){
            //处理连续的星期
            if(s.contains("-")){
                String[] strings=s.split("-");
                //log.info(strings[0]);
                boolean flag=false;
                for(int i=0;i<7;i++){
                    if(Objects.equals(strings[0], WeekUtil.Week.get(i))){
                        flag=true;
                    }
                    //log.info(Boolean.toString(flag));
                    if(flag){
                        RunDateTimeDto runDateTimeDto =new RunDateTimeDto();
                        runDateTimeDto.setWeek(Week.get(i));
                        list.add(runDateTimeDto);
                        //log.info(runDateTimeVo.getWeek());
                    }
                    if(strings[1].equals(WeekUtil.Week.get(i))){
                        break;
                    }
                }
            }else{
                RunDateTimeDto runDateTimeDto =new RunDateTimeDto();
                runDateTimeDto.setWeek(s);
                list.add(runDateTimeDto);
            }
        }
        //list.forEach(System.out::println);
        return list;
    }

    //将具体runDateTime的星期转换为JsonString值
    public static String getStringJSONWeek(List<RunDateTimeDto> weekList){
        String JSONString="";
        //用来存放归类完的星期
        List<List<RunDateTimeDto>> lists=new ArrayList<>();
        //将星期归类
        for(RunDateTimeDto runDateTimeDto :weekList){

            Boolean flag=true;
            for(List<RunDateTimeDto> list:lists){
                //判断开始时间和结束时间是否相同，相同的放到一个集合，flag是用来判断是否需要创建新的集合
                if (list.get(0).getStartTime().equals(runDateTimeDto.getStartTime())&&
                        list.get(0).getEndTime().equals(runDateTimeDto.getEndTime())){
                    list.add(runDateTimeDto);
                    flag=false;
                }
            }
            //如果flag为真就是需要创建一个新的集合并将当前的runDateTimeVo放入进去
            if(flag){
                List<RunDateTimeDto> list=new ArrayList<>();
                list.add(runDateTimeDto);
                lists.add(list);
            }
        }
        //将归类完的集合转换成String形式
        /**
         * Mou-Fri&9-21;Sta-Sun&10-22
         */
        for (List<RunDateTimeDto> list:lists){
            String s="";
            //处理星期
            //分成两种情况：只有一个   有多个
            if(list.size()!=1){
                Collections.sort(list);
                //判断是否全部连续，不全部连续分成多个部分
                int i=0;
                String startWeek="";
                String endWeek="";
                for(String week:WeekUtil.Week){
                    if(week.equals(list.get(i).getWeek())){
                        if(startWeek.equals("")){
                            startWeek=week;
                            endWeek=week;
                        }else{
                            endWeek=week;
                        }
                        i++;
                    }else{
                        if(!startWeek.equals("")){
                            if(!startWeek.equals(endWeek)){
                                s+=startWeek+"-"+endWeek+",";
                            }else {
                                s+=startWeek+",";
                            }
                            startWeek=endWeek="";
                        }
                    }
                    if(i==list.size()){
                        break;
                    }
                }
                if(!startWeek.equals("")){
                    if(!startWeek.equals(endWeek)){
                        s+=startWeek+"-"+endWeek;
                    }else {
                        s+=startWeek;
                    }
                }
                s+="&";

            }else{
                s+=list.get(0).getWeek()+"&";
            }
            if (!list.get(0).getStartTime().contains(":")||
                    !list.get(0).getEndTime().contains(":")){
                throw new ScheduleException(20001,"要处理的时间格式不对，需要的格式为：hh:mm");
            }
            //处理时间
            s+=list.get(0).getStartTime()+"-"+list.get(0).getEndTime();
            if(lists.indexOf(list)!=lists.size()-1){
                s+=";";
            }
            JSONString+=s;
        }
        return JSONString;
    }

    //将String类型的星期处理成类
    public static List<EmployeePreferenceDto> preferenceGetWeek(String value){
        List<EmployeePreferenceDto> list=new ArrayList<>();
        //需要判断是否有不连续的星期使用,隔开的
        /**
         * Mon-Fri   Mon,Fri-Sun
         */
        String[] weeks=value.split(",");
        for(String s:weeks){
            //处理连续的星期
            if(s.contains("-")){
                String[] strings=s.split("-");
                //log.info(strings[0]);
                boolean flag=false;
                for(int i=0;i<7;i++){
                    if(Objects.equals(strings[0], Week.get(i))){
                        flag=true;
                    }
                    //log.info(Boolean.toString(flag));
                    if(flag){
                        EmployeePreferenceDto preferenceVo=new EmployeePreferenceDto();
                        preferenceVo.setWeek(Week.get(i));
                        list.add(preferenceVo);
                        //log.info(runDateTimeVo.getWeek());
                    }
                    if(strings[1].equals(Week.get(i))){
                        break;
                    }
                }
            }else{
                EmployeePreferenceDto preferenceVo=new EmployeePreferenceDto();
                preferenceVo.setWeek(s);
                list.add(preferenceVo);
            }
        }
        list.forEach(System.out::println);
        return list;
    }

    //将具体的星期转换为JsonString值
    public static String preferencegetStringJSONWeek(List<EmployeePreferenceDto> weekList){
        String JSONString="";
        //用来存放归类完的星期
        List<List<EmployeePreferenceDto>> lists=new ArrayList<>();
        //将星期归类
        for(EmployeePreferenceDto preferenceVo:weekList){

            Boolean flag=true;
            for(List<EmployeePreferenceDto> list:lists){
                //判断开始时间和结束时间是否相同，相同的放到一个集合，flag是用来判断是否需要创建新的集合
                if (list.get(0).getStartTime().equals(preferenceVo.getStartTime())&&
                        list.get(0).getEndTime().equals(preferenceVo.getEndTime())){
                    list.add(preferenceVo);
                    flag=false;
                }
            }
            //如果flag为真就是需要创建一个新的集合并将当前的runDateTimeVo放入进去
            if(flag){
                List<EmployeePreferenceDto> list=new ArrayList<>();
                list.add(preferenceVo);
                lists.add(list);
            }
        }
        //将归类完的集合转换成String形式
        /**
         * Mou-Fri&9:00-21:00;Sta-Sun&10:00-22:00
         */
        for (List<EmployeePreferenceDto> list:lists){
            String s="";
            //处理星期
            //分成两种情况：只有一个   有多个
            if(list.size()!=1){
                Collections.sort(list);
                s+=list.get(0).getWeek()+"-"+list.get(list.size()-1).getWeek()+"&";
            }else{
                s+=list.get(0).getWeek()+"&";
            }
            if (!list.get(0).getStartTime().contains(":")||
                    !list.get(0).getEndTime().contains(":")){
                throw new ScheduleException(20001,"要处理的时间格式不对，需要的格式为：hh:mm");
            }
            //处理时间
            s+=list.get(0).getStartTime()+"-"+list.get(0).getEndTime();
            if(lists.indexOf(list)!=lists.size()-1){
                s+=";";
            }
            JSONString+=s;
        }
        return JSONString;
    }

}
