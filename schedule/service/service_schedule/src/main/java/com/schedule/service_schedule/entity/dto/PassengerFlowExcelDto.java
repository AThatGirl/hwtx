package com.schedule.service_schedule.entity.dto;


import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(description = "客流量预测数据",value = "peopleExcelVo对象")
public class PassengerFlowExcelDto {

    @ApiModelProperty(value = "门店id")
    private String id;

    @ApiModelProperty(value = "日期")
    @DateTimeFormat(value = "yyyy-MM-dd")
    private String date;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(value = "HH:mm")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(value = "HH:mm")
    private String endTime;

    @ApiModelProperty(value = "客流量")
    private Double numPeople;
}
