package com.cj.manage.vo;

import lombok.Data;

/**
 * 条件查询打卡信息
 */
@Data
public class ClockInSearchVO {

    /**
     * 门店id
     */
    private String id;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 状态
     */
    private String status;

    /**
     * 具体打卡日期
     */
    private String date;


}
