package com.cj.mq_service.service;

import com.cj.common.vo.ResultVO;
import com.cj.common.vo.SendNoticeVO;

/**
 * 通知服务
 */
public interface NoticeService {


    /**
     * 给一个人发送消息
     * @param sendNoticeVO 请求vo
     * @return {@link ResultVO}
     */
    void sendNotice(SendNoticeVO sendNoticeVO);


}
