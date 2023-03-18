package com.cj.mq_service.service;

import com.cj.common.vo.ResultVO;
import com.cj.mq_service.vo.SendOneVO;

/**
 * 通知服务
 */
public interface NoticeService {


    /**
     * 给一个人发送消息
     * @param sendOneVO 请求vo
     * @return {@link ResultVO}
     */
    ResultVO sendToEmployee(SendOneVO sendOneVO);


}
