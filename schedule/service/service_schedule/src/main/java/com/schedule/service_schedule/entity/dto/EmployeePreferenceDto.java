package com.schedule.service_schedule.entity.dto;

import com.schedule.service_schedule.utils.WeekUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EmployeePreferenceDto implements Comparable<EmployeePreferenceDto>{
    @ApiModelProperty(value = "星期")
    private String week;

    @ApiModelProperty(value = "时间")
    private String startTime;
    private String endTime;



    static Map<String,Integer> weekMap=new HashMap<>();
    static {
        int i=0;
        for(String s: WeekUtil.Week){
            weekMap.put(s,i++);
        }
    }

    @Override
    public int compareTo(EmployeePreferenceDto o) {
        return weekMap.get(this.getWeek())-weekMap.get(o.getWeek());
    }
}
