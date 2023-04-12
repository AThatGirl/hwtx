package com.schedule.service_schedule.entity.vo.rule;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PrepareRuleVo {
    @ApiModelProperty(value = "规则类型")
    private String type;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty(value = "开店准备时间：-0.5 -1 小时")
    private Double front;

    @ApiModelProperty(value = "计算公式：size/100")
    private String formula;

}
