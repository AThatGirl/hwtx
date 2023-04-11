package com.cj.mq_consumer.utils;

import com.cj.common.exception.ClassException;
import com.cj.mq_consumer.config.MailSenderConfig;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.StringUtils;

public class SendEmailUtils {

    public static String commonNamed = "亲爱的";
    public static String blessingContent = "祝您生日快乐！";
    public static String holidayContent = "祝您生日快乐！";


    //发送到邮箱
    public static void sendToEmail(String senderEmail, String receiverEmail, String content, String type, MailSenderConfig senderConfig) {
        JavaMailSenderImpl mailSender = senderConfig.getSender(senderEmail);
        //默认的邮箱
        String username = mailSender.getUsername();
        if (StringUtils.isEmpty(username)){
            throw new ClassException("祝福发送异常");
        }
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(senderEmail != null ? senderEmail : username);
        //邮件接收人
        message.setTo(receiverEmail);
        //邮件主题
        message.setSubject(type);
        //邮件内容
        message.setText(content);
        //发送邮件
        mailSender.send(message);
    }



}
