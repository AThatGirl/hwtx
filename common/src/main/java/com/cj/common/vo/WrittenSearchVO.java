package com.cj.common.vo;

import lombok.Data;

/**
 *  请假条信息查询vo
 *
 * @author jerry
 */
@Data
public class WrittenSearchVO {

    /**
     * 姓名
     */
    private String name;

    /**
     * 审核状态
     */
    private String status;

    /**
     * 页数
     */
    private int pageNum;


}
