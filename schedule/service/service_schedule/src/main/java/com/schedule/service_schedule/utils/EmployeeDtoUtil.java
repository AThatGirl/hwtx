package com.schedule.service_schedule.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.schedule.service_schedule.entity.dto.EmployeeDto;
import com.schedule.service_schedule.entity.dto.EmployeePreferenceDto;
import com.schedule.service_schedule.entity.vo.client.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmployeeDtoUtil {
    /**
     * {"workTime":"all","weekWorkTime":"0","dayWorkTime":"0"}
     * {"weekWorkTime":"40","dayWorkTime":"8","shiftTimeRange":"2-4","maxWorkTime":"4"}
     * @param employeeDtoList
     */
    public static void dealEmployeeDto(List<EmployeeDto> employeeDtoList,String workTimeRuleStringJSON){
        JSONObject workTimeRuleJson=JSONObject.parseObject(workTimeRuleStringJSON);
        double weekWorkTimeLimit=workTimeRuleJson.getDouble("weekWorkTime");
        double dayWorkTimeLimit=workTimeRuleJson.getDouble("dayWorkTime");
        for(EmployeeDto employeeDto:employeeDtoList){
            JSONObject preferenceJson=JSONObject.parseObject(employeeDto.getPreference());
            //周工作限制
            if(preferenceJson.getDouble("weekWorkTime")==0){
                employeeDto.setWeekWorkTimeLimit(weekWorkTimeLimit);
            }else{
                employeeDto.setWeekWorkTimeLimit(preferenceJson.getDouble("weekWorkTime"));
            }
            //天工作限制
            if(preferenceJson.getDouble("dayWorkTime")==0){
                employeeDto.setDayWorkTimeLimit(dayWorkTimeLimit);
            }else{
                employeeDto.setDayWorkTimeLimit(preferenceJson.getDouble("dayWorkTime"));
            }
            //工作的日期
            if(!preferenceJson.getString("workTime").equals("all")){
                Map<String, EmployeePreferenceDto> preferenceDtoMap=employeeDto.getPreferenceVoMap();
                List<EmployeePreferenceDto> list=PreferenceUtil.dealJSONStringToEmployeePreference(preferenceJson.getString("workTime"));
                for(EmployeePreferenceDto preferenceDto:list){
                    preferenceDtoMap.put(preferenceDto.getWeek(),preferenceDto);
                }
            }
            //七天的工作置零
            List<Double> doubleList=employeeDto.getDayWorkTime();
            for(int i=0;i<7;i++){
                doubleList.add(0.0);
            }
        }
    }

    public static List<EmployeeDto> dealEmployee(List<User> userList,String workTimeRuleStringJSON){
        List<EmployeeDto> employeeDtoList=new ArrayList<>();
        JSONObject workTimeRuleJson= JSON.parseObject(workTimeRuleStringJSON);
        double weekWorkTimeLimit=workTimeRuleJson.getDouble("weekWorkTime");
        double dayWorkTimeLimit=workTimeRuleJson.getDouble("dayWorkTime");
        for(User user:userList){
            EmployeeDto employeeDto=new EmployeeDto();
            employeeDto.setPosition(user.getCareer());
            employeeDto.setId(user.getId());
            if(Integer.parseInt(user.getPreference().getTimeLength())==0){
                employeeDto.setDayWorkTimeLimit(dayWorkTimeLimit);
            }else{
                employeeDto.setDayWorkTimeLimit(Integer.parseInt(user.getPreference().getTimeLength()));
            }

            if(Integer.parseInt(user.getPreference().getWeekTime())==0){
                employeeDto.setWeekWorkTimeLimit(weekWorkTimeLimit);
            }else {
                employeeDto.setWeekWorkTimeLimit(Integer.parseInt(user.getPreference().getWeekTime()));
            }

            //七天的工作置零
            List<Double> doubleList=employeeDto.getDayWorkTime();
            for(int i=0;i<7;i++){
                doubleList.add(0.0);
            }

            //工作的日期
            List<EmployeePreferenceDto> list=PreferenceUtil.dealPreference(user.getPreference());
            Map<String, EmployeePreferenceDto> preferenceDtoMap=employeeDto.getPreferenceVoMap();
            for(EmployeePreferenceDto preferenceDto:list){
                preferenceDtoMap.put(preferenceDto.getWeek(),preferenceDto);
            }
            employeeDtoList.add(employeeDto);

        }
        return employeeDtoList;
    }

}
