package com.cj.written.vo;

import lombok.Data;

/**
 * 请假
 *
 * @author 杰瑞
 * @date 2023/03/06
 */
@Data
public class WrittenVO {

    private String id;

    private String reason;

    private String startTime;

    private String endTime;

    private String storeId;


}
