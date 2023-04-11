package com.cj.care.vo;

import lombok.Data;

@Data
public class CareGetVO {

    /**
     * 受关怀的员工姓名
     */
    private String username;
    /**
     * 门店id
     */
    private String storeId;
    /**
     * 页码
     */
    private String page;
}
