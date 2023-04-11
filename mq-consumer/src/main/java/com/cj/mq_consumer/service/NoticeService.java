package com.cj.mq_consumer.service;


/**
 * 通知服务
 */
public interface NoticeService {


    /**
     * 发送通知服务
     */
    void sendNotice(String msg);

    /**
     * 发送祝福
     * @param msg
     */
    void sendBlessing(String msg);

}
