package com.cj.personal.vo;

import lombok.Data;

@Data
public class GestureVO {

    /**
     * 签到码
     */
    private String gesture;
    /**
     * 类型，上班/下班
     */
    private String signType;

    /**
     * 其他信息
     */
    private String info;

    /**
     * 员工id
     */
    private String employeeId;
    /**
     * 门店id
     */
    private String storeId;

}
