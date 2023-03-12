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
     * 状态
     */
    private String status;

    /**
     * 页数
     */
    private Integer pageNum;

}
