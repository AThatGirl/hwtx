package com.schedule.service_schedule.entity.vo.rule;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EndRuleVo {
    @ApiModelProperty(value = "规则类型")
    private String type;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty(value = "关店打扫时间：0.5 1 小时")
    private Double after;

    @ApiModelProperty("默认加上的人数")
    private Integer count;

    @ApiModelProperty("计算公式: size/80")
    private String formula;
}
