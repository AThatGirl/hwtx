package com.schedule.service_schedule.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.schedule.service_schedule.entity.vo.client.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WorkForm对象", description="")
public class WorkForm implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "UUID生成id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "排班日期")
    private LocalDate date;

    @ApiModelProperty(value = "班次开始时间")
    private LocalTime startTime;

    @ApiModelProperty(value = "班次结束时间")
    private LocalTime endTime;

    @ApiModelProperty(value = "允许职位")
    private String allowCareer;

    @ApiModelProperty(value = "员工id")
    private String employeeId;

    private String storeId;

    @TableField(exist = false)
    private User user;

    @TableField(fill = FieldFill.INSERT,value = "created_at")
    private Date createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE,value = "updated_at")
    private Date updatedAt;
}

