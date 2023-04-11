package com.cj.mq_consumer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cj.common.entity.Notice;
import com.cj.common.mapper.NoticeMapper;
import com.cj.common.mapper.UserMapper;
import com.cj.common.utils.DateUtils;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.SendNoticeVO;
import com.cj.mq_consumer.config.MailSenderConfig;
import com.cj.mq_consumer.service.NoticeService;
import com.cj.mq_consumer.utils.SendEmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {

    public static String emailNotice = "通知邮件";


    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailSenderConfig senderConfig;


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
            if (!sendNoticeVO.getReceiverId().equals(Notice.ALL_USER)) {
                String senderEmail = userMapper.selectById(sendNoticeVO.getSenderId()).getEmail();
                String receiverEmail = userMapper.selectById(sendNoticeVO.getReceiverId()).getEmail();
                //发送邮箱
                SendEmailUtils.sendToEmail(senderEmail, receiverEmail, sendNoticeVO.getContent(), emailNotice, senderConfig);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("邮箱发送失败，消息为{}", msg);
        }
    }

    @Override
    public void sendBlessing(String msg) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            String senderEmail = jsonObject.getString("senderEmail");
            String receiverEmail = jsonObject.getString("receiverEmail");
            String content = jsonObject.getString("content");
            String type = "祝福信息";
            SendEmailUtils.sendToEmail(senderEmail, receiverEmail, content, type, senderConfig);
            log.info("接收祝福人的邮箱为{}", receiverEmail);
        }catch (Exception e){
            //防止邮箱不存在，导致消息无法消费
            e.printStackTrace();
            log.info("邮箱发送失败，消息为{}", msg);
        }
    }
}
