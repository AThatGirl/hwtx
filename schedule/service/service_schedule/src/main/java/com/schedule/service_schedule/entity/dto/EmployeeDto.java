package com.schedule.service_schedule.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class EmployeeDto {
    @ApiModelProperty("员工id")
    private String id;
    @ApiModelProperty(value = "员工名字")
    private String name;
    @ApiModelProperty(value = "职位")
    private String position;
    @ApiModelProperty(value = "偏好（json的String值）")
    private String preference;

    @ApiModelProperty(value = "存放处理好的工作星期和时间偏好值,当size为0代表没有要求")
    private Map<String, EmployeePreferenceDto> preferenceVoMap=new HashMap<>();

    private double weekWorkTime;

    @ApiModelProperty(value = "7日工作时间")
    private List<Double> dayWorkTime=new ArrayList<>();

    @ApiModelProperty(value = "日工作限制，0没有限制")
    private double dayWorkTimeLimit;
    @ApiModelProperty(value = "周工作限制，0没有限制")
    private double weekWorkTimeLimit;

    @ApiModelProperty(value = "用于判断是否可以被选择排班")
    private boolean flag;

}
