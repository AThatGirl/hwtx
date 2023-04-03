package com.cj.personal.vo;

import lombok.Data;

/**
 * 位置打卡
 *
 * @author 杰瑞
 * @date 2023/01/31
 */
//由前端传递 用户所在位置经纬度
@Data

public class PlatPunch {

    public static final String LONGITUDES = "106.235321";
    public static final String LATITUDES = "37.432579";

    /*经度*/
    private String longitude;
    /*纬度*/
    private String latitude;
    //员工id
    private String employeeId;
    //打卡类型
    private String signType;
    //其他信息
    private String info;
    //门店id
    private String storeId;


}
