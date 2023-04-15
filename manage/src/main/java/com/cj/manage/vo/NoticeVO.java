package com.cj.manage.vo;

import lombok.Data;

@Data
public class NoticeVO {


    //页码
    private String page;

    //搜索某个时间之后的历史记录
    private String time;

    //消息状态
    private String status;

    //门店id
    private String storeId;

}
