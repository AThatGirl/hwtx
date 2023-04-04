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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService {


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
                sendToEmail(senderEmail, receiverEmail, sendNoticeVO.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送到邮箱
    public void sendToEmail(String senderEmail, String receiverEmail, String content) {
        JavaMailSenderImpl mailSender = senderConfig.getSender(senderEmail);

        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(senderEmail);
        //邮件接收人
        message.setTo(receiverEmail);
        //邮件主题
        message.setSubject("通知邮件");
        //邮件内容
        message.setText(content);
        //发送邮件
        mailSender.send(message);
    }


}
