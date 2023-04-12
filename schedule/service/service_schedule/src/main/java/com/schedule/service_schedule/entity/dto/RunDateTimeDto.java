package com.schedule.service_schedule.entity.dto;

import com.schedule.service_schedule.utils.WeekUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@ApiModel(value = "营业时间类")
@Data
public class RunDateTimeDto implements Comparable<RunDateTimeDto>{
    @ApiModelProperty(value = "星期")
    String week;

    String startTime;

    String endTime;
    static Map<String,Integer> weekMap=new HashMap<>();
    static {
        int i=0;
        for(String s: WeekUtil.Week){
            weekMap.put(s,i++);
        }
    }
    @Override
    public int compareTo(RunDateTimeDto o) {
        return weekMap.get(this.getWeek())-weekMap.get(o.getWeek());
    }
}
