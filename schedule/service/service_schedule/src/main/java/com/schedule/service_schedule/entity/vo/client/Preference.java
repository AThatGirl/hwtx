package com.schedule.service_schedule.entity.vo.client;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author jerry
 * @since 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Preference implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String rangeTime;

    private String startTime;

    private String endTime;
    @ApiModelProperty(value = "日工作限制，0没有限制")
    private String timeLength;

    private String lunchTime;

    private String dinnerTime;

    private String employeeId;

    @ApiModelProperty(value = "周工作限制，0没有限制")
    private String weekTime;

    private String storeId;

}
