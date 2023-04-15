package com.cj.manage.service.impl;

import com.cj.common.en.CommonError;
import com.cj.common.entity.Store;
import com.cj.common.exception.ClassException;
import com.cj.common.mapper.StoreMapper;
import com.cj.common.mapper.UserMapper;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.ResultVO;
import com.cj.manage.feign.RuleFeign;
import com.cj.manage.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RuleFeign ruleFeign;

    @Override
    public ResultVO search(String id) {
        Store store = storeMapper.selectById(id);
        return ResultVO.success().setData(store);
    }

    @Override
    public ResultVO searchAllStore() {
        //分页，每页一个门店信息
        List<Store> stores = storeMapper.selectList(null);
        return ResultVO.success().setData(stores);
    }


    @Override
    public ResultVO changeStoreInfo(Store store) {
        int res = storeMapper.updateById(store);
        if (res < 0) {
            ClassException.cast(CommonError.UPDATE_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    @Transactional
    public ResultVO addStore(Store store) {
        store.setId(UUIDUtils.getId());
        store.setEmployeeNum(0);
        storeMapper.insert(store);
        ruleFeign.insertScheduleRule(store.getId());
        return ResultVO.success();
    }
}
