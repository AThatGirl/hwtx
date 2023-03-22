package com.cj.mq_consumer.service;

/**
 * 门店服务
 */
public interface StoreService {

    /**
     * 更新门店人数
     * @param storeId 门店id
     */
    void updateStoreUserNum(String storeId);

}
