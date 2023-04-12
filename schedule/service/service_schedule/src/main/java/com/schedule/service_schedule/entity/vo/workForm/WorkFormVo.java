package com.schedule.service_schedule.entity.vo.workForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class WorkFormVo implements Serializable {
    private String storeId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String employeeId;
    @ApiModelProperty(value = "允许职位")
    private String allowCareer;
}
