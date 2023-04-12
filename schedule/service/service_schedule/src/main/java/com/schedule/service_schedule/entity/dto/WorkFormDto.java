package com.schedule.service_schedule.entity.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author tpt
 * @since 2023-02-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WorkForm对象", description="")
public class WorkFormDto  {
    private String id;

    @ApiModelProperty(value = "排班日期")
    private String date;

    @ApiModelProperty(value = "班次开始时间")
    private String startTime;

    @ApiModelProperty(value = "班次结束时间")
    private String endTime;

    @ApiModelProperty(value = "允许职位")
    private String allowCareer;

    @ApiModelProperty(value = "员工id")
    private String employeeId;

    private Double shiftTime;

    private String week;

}
