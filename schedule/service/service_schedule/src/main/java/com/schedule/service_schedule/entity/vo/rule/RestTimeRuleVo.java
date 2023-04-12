package com.schedule.service_schedule.entity.vo.rule;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RestTimeRuleVo {
    @ApiModelProperty(value = "规则类型")
    private String type;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty(value = "休息时间：0.5 1 小时为单位")
    private Double count;
}
