package com.cj.manage.service.impl;

import com.cj.common.en.CommonError;
import com.cj.common.entity.Store;
import com.cj.common.exception.ClassException;
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
        Store store = storeMapper.selectById(id);
        return ResultVO.success().setData(store);
    }

    @Override
    public ResultVO changeStoreInfo(Store store) {
        int res = storeMapper.updateById(store);
        if (res < 0){
            ClassException.cast(CommonError.UPDATE_ERROR);
        }
        return ResultVO.success();
    }
}
