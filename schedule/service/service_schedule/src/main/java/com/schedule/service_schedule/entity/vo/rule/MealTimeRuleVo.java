package com.schedule.service_schedule.entity.vo.rule;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MealTimeRuleVo implements Serializable {
    @ApiModelProperty(value = "规则类型")
    private String type;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty(value = "吃饭时间")
    private String time;

    @ApiModelProperty(value = "休息时间：0.5 1 小时")
    private Double count;
}
