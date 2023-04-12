package com.schedule.service_schedule.entity.vo.rule;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WorkHourRuleVo implements Serializable {
    @ApiModelProperty(value = "规则类型")
    private String type;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    private Double weekWorkTime;

    private Double dayWorkTime;

    @ApiModelProperty(value = "班次时间范围")
    private String shiftTimeRange;

    @ApiModelProperty(value = "最大连续工作时间")
    private Double maxWorkTime;
}
