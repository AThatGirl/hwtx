package com.schedule.service_schedule.entity.vo.rule;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PassengerFlowRuleVo implements Serializable {
    @ApiModelProperty(value = "规则类型")
    private String type;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty(value = "计算公式： 3.8")
    private String formula;
}
