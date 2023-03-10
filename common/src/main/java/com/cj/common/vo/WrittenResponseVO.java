package com.cj.common.vo;

import lombok.Data;

/**
 * 请假条返回数据类
 */
@Data
public class WrittenResponseVO {
    private String id;

    private String reason;

    private String startTime;

    private String endTime;

    private String status;

    private String createTime;

    private String updateTime;

    private String employeeId;

    private String name;

    private String career;


}
