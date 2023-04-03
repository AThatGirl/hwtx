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
public class ClockIn implements Serializable {

    private static final long serialVersionUID = 1L;

    public static String SIGN_UP = "上班";
    public static String SIGN_DOWN = "下班";

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String signTime;

    private String signType;

    private String info;

    private String employeeId;

    private String storeId;

}
