package com.schedule.service_schedule.entity.vo.rule;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NoPassengerFlowRuleVo implements Serializable {
    @ApiModelProperty(value = "规则类型")
    private String type;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty("没有客流量时默认人数")
    private Integer count;
}
