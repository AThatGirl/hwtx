package com.cj.mq_consumer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cj.common.entity.Notice;
import com.cj.common.mapper.NoticeMapper;
import com.cj.common.utils.DateUtils;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.SendNoticeVO;
import com.cj.mq_consumer.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService {


    @Autowired
    private NoticeMapper noticeMapper;


    @Override
    public void sendNotice(String msg) {
        //解析json
        try {
            SendNoticeVO sendNoticeVO = JSONObject.parseObject(msg, SendNoticeVO.class);
            //设置属性
            Notice notice = new Notice();
            notice.setId(UUIDUtils.getId());
            notice.setSenderId(sendNoticeVO.getSenderId());
            notice.setReceiverId(sendNoticeVO.getReceiverId());
            notice.setContent(sendNoticeVO.getContent());
            notice.setStatus(Notice.UNREAD_STATUS);
            notice.setCreateTime(DateUtils.getNowDate());
            notice.setStoreId(sendNoticeVO.getStoreId());
            noticeMapper.insert(notice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
