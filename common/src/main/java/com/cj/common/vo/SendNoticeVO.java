package com.cj.common.vo;

import lombok.Data;

@Data
public class SendNoticeVO {
    //发送者id
    private String senderId;

    //接收者id，如果receiverId="全体成员",就是全部收到
    private String receiverId;

    //通知内容
    private String content;

    //门店id
    private String storeId;
}
