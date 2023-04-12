package com.schedule.service_schedule.entity.vo.rule;

import com.schedule.service_schedule.entity.dto.RunDateTimeDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BusinessRuleVo implements Serializable {
    @ApiModelProperty(value = "规则类型")
    private String type;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty(value = "门店营业时间规则")
    private List<RunDateTimeDto> runDateTimeDtoList;


}
