package com.schedule.service_schedule.entity.vo.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ShiftLimitRuleVo implements Serializable {
    @ApiModelProperty(value = "规则类型")
    private String type;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "公式： <=2 小于2小时的班次的个数为")
    private String formula;
}
