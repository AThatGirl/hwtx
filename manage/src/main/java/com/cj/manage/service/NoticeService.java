package com.cj.manage.service;

import com.cj.common.vo.ResultVO;
import com.cj.common.vo.SendNoticeVO;
import com.cj.manage.vo.NoticeVO;

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


    /**
     * 获取所有通知
     * @param noticeVO noticeVO
     * @return {@link ResultVO}
     */
    ResultVO getNoticeForPage(NoticeVO noticeVO);

    /**
     * 删除消息
     * @param ids id数组
     * @return {@link ResultVO}
     */
    ResultVO deleteNotice(String[] ids);


    /**
     * 已读消息
     * @param id 消息id
     * @return {@link ResultVO}
     */
    ResultVO readNotice(String id);

}
