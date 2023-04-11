package com.cj.mq_service.service;

import com.cj.common.vo.SendNoticeVO;

/**
 * 通知服务
 */
public interface NoticeService {


    /**
     * 给一个人发送消息
     * @param sendNoticeVO 请求vo
     */
    void sendNotice(SendNoticeVO sendNoticeVO);

    /**
     * 发送祝福
     * @param param
     */
     void sendBlessing(String param);

}
