package com.cj.manage.service.impl;

import com.cj.common.entity.Store;
import com.cj.common.mapper.StoreMapper;
import com.cj.common.vo.ResultVO;
import com.cj.manage.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreMapper storeMapper;


    @Override
    public ResultVO search(String id) {
        Store store = null;
        try {
            store = storeMapper.selectById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail();
        }
        return ResultVO.success().setData(store);
    }

    @Override
    public ResultVO changeStoreInfo(Store store) {
        try {
            storeMapper.updateById(store);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail();
        }
        return ResultVO.success();
    }
}
