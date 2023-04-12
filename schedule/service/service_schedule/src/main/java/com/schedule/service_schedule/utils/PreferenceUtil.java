package com.schedule.service_schedule.utils;

import com.schedule.service_schedule.entity.dto.EmployeePreferenceDto;
import com.schedule.service_schedule.entity.vo.client.Preference;
import io.swagger.models.auth.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class PreferenceUtil {
    /**
     *   Mon&8:00-18:00;Tue~Sta&8:00-18:00,19:00-22:00
     1.	工作日偏好规则：周３到周６.缺省为全部
     默认：all
     2.	工作时间偏好规则：上午８点到下午６点。缺省为全部
     默认: all
     3.	班次时长偏好规则：每天时长不超过多久。如：４小时。缺省为0不限制；每周最多工作多久等。如：２０小时。缺省为0不限制
     JSON：{"weekWorkTime":"20","dayWorkTime":"4"}
     JSON：{"week":"Tue~Sta"}
     JSON：{"time":""}
     JSON: {"workTime":"all","weekWorkTime":"0","dayWorkTime":"0"}
     */
    public static final String PreferenceJSONString="{\"workTime\":\"all\",\"weekWorkTime\":\"0\",\"dayWorkTime\":\"0\"}";

    /**
     * 将JSONString处理成EmployeePreferenceVo
     * @param JSONString JSON的String值
     * @return List<EmployeePreferenceVo>
     */
    public static List<EmployeePreferenceDto> dealJSONStringToEmployeePreference(String JSONString){
        List<EmployeePreferenceDto> preferenceVoList=new ArrayList<>();
        if(JSONString.equals("all")){
            return preferenceVoList;
        }
        String[] strings=JSONString.split(";");
        //log.info(strings[0]);
        for(String str:strings){
            /**
             * {'runTime':'Mou-Fri&9-21;Sta-Sun&10-22'}
             */
            String[] strings1=str.split("&");
            //处理星期
            //log.error(strings1[0]);
            List<EmployeePreferenceDto> weekList=WeekUtil.preferenceGetWeek(strings1[0]);
            //weekList.forEach(System.out::println);
            //处理时间
            TimeUtil.preferenceSetTime(weekList,strings1[1]);
            preferenceVoList.addAll(weekList);
        }
        //排序按照星期
        Collections.sort(preferenceVoList);

        return preferenceVoList;
    }

    //处理偏好
    public static List<EmployeePreferenceDto> dealPreference(Preference preference){
         List<EmployeePreferenceDto> preferenceDtoList=new ArrayList<>();
         String []week=preference.getRangeTime().split(",");
         List<Integer> weekList=new ArrayList<>();
         for(String s:week){
             Integer integer=new Integer(s);
             weekList.add(integer);
         }
         for(Integer integer:weekList){
             EmployeePreferenceDto preferenceDto=new EmployeePreferenceDto();
             preferenceDto.setStartTime(preference.getStartTime());
             preferenceDto.setEndTime(preference.getEndTime());
             preferenceDto.setWeek(WeekUtil.Week.get(integer-1));
             preferenceDtoList.add(preferenceDto);
         }
         Collections.sort(preferenceDtoList);
         return preferenceDtoList;
    }
}
