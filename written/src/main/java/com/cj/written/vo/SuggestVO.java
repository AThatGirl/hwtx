package com.cj.written.vo;

import lombok.Data;

/**
 * 建议
 *
 * @author 杰瑞
 * @date 2023/03/07
 */
@Data
public class SuggestVO {

    /**
     * 员工id
     */
    private String id;

    /**
     * 建议内容
     */
    private String content;

    /**
     * 门店id
     */
    private String storeId;


}
