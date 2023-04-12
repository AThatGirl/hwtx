package com.schedule.service_schedule.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShiftDto {
    @ApiModelProperty(value = "创建的哪一天班次的时间")
    private String date;
    @ApiModelProperty(value = "班次开始时间")
    private String startTime;

    @ApiModelProperty(value = "班次结束时间")
    private String endTime;

    @ApiModelProperty(value = "需要人数")
    private Integer peopleNum;
    @ApiModelProperty(value = "班次时间")
    private double shiftTime;
}
