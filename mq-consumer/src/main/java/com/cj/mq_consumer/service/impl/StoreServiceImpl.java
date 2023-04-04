package com.cj.mq_consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.common.en.UserCareer;
import com.cj.common.entity.Store;
import com.cj.common.entity.User;
import com.cj.common.mapper.StoreMapper;
import com.cj.common.mapper.UserMapper;
import com.cj.mq_consumer.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void updateStoreUserNum(String storeId) {
        Store store = storeMapper.selectById(storeId);
        //防止消息发送错误，消息一直消费不了
        if (store == null){
            return;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("career", UserCareer.SUPER_ADMIN.getCareer()).ne("career", UserCareer.ADMIN.getCareer());
        Integer sum = userMapper.selectCount(queryWrapper);
        store.setEmployeeNum(sum);
        storeMapper.updateById(store);
    }



}
