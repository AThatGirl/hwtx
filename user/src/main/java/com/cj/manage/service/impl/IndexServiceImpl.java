package com.cj.manage.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.common.entity.User;
import com.cj.common.entity.Written;
import com.cj.common.mapper.UserMapper;
import com.cj.common.mapper.WrittenMapper;
import com.cj.common.vo.ResultVO;
import com.cj.manage.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WrittenMapper writtenMapper;

    @Override
    public ResultVO indexNum() {
        int userNum = userMapper.selectList(null).size();
        int userPassingNum = userMapper.selectList(new QueryWrapper<User>().eq("pass", User.PASSING)).size();
        int writtenPassingNum = writtenMapper.selectList(new QueryWrapper<Written>().eq("status", Written.AUDIT_STATUS)).size();
        Map<String, Integer> map = new HashMap<>();
        map.put("userNum", userNum);
        map.put("userPassingNum", userPassingNum);
        map.put("writtenPassingNum", writtenPassingNum);
        return ResultVO.success().setData(map);
    }



}
