package com.cj.mq_service.service;


/**
 * 门店服务
 */
public interface StoreService {

    /**
     * 发送更新门店消息
     * @param msg 消息
     */
    void updateStore(String msg);


}
