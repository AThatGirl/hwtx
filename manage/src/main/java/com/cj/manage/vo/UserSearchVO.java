package com.cj.manage.vo;

import lombok.Data;

/**
 * 搜索员工信息
 */
@Data
public class UserSearchVO {

    /**
     * 姓名
     */
    private String name;


    /**
     * 页数
     */
    private Integer page;

    /**
     * 状态
     */
    private String status;

    /**
     * 门店id
     */
    private String storeId;

}
