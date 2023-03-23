package com.cj.manage.service;

import com.cj.common.vo.ResultVO;
import com.cj.common.vo.SendNoticeVO;

/**
 * 通知管理
 */
public interface NoticeService {

    /**
     * 发送通知
     *
     * @return {@link ResultVO}
     */
    ResultVO sendNotice(SendNoticeVO sendNoticeVO);


}
