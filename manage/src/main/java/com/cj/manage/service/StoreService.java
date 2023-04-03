package com.cj.manage.service;

import com.cj.common.entity.Store;
import com.cj.common.vo.ResultVO;

/**
 * 门店管理
 */
public interface StoreService {


    /**
     * 获取门店信息
     * @param id 门店id
     * @return {@link ResultVO}
     */
    ResultVO search(String id);


    /**
     * 获取所有的门店信息
     * @return {@link ResultVO}
     */
    ResultVO searchAllStore();

    /**
     * 修改门店信息
     * @param store 门店
     * @return {@link ResultVO}
     */
    ResultVO changeStoreInfo(Store store);


}
