package com.cj.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Written implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String AUDIT_STATUS = "审核中";
    public static final String PASS_STATUS = "通过";
    public static final String REJECT_STATUS = "拒绝";

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String reason;

    private String startTime;

    private String endTime;

    private String status;

    private String createTime;

    private String updateTime;

    private String employeeId;

    private String storeId;


}
