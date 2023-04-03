package com.cj.written.vo;

import lombok.Data;

/**
 * 修改请假条
 *
 * @author 杰瑞
 * @date 2023/03/06
 */
@Data
public class WrittenUpdateVO {

    private String id;

    private String reason;

    private String startTime;

    private String endTime;


}
