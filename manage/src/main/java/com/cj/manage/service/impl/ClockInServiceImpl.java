package com.cj.manage.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.common.en.CommonError;
import com.cj.common.entity.ClockIn;
import com.cj.common.entity.User;
import com.cj.common.exception.ClassException;
import com.cj.common.mapper.ClockInMapper;
import com.cj.common.mapper.UserMapper;
import com.cj.common.vo.ResultVO;
import com.cj.manage.service.ClockInService;
import com.cj.manage.vo.ClockInSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClockInServiceImpl implements ClockInService {

    @Autowired
    private ClockInMapper clockInMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResultVO getClockIn(ClockInSearchVO clockInSearchVO) {
        QueryWrapper<ClockIn> queryWrapper = new QueryWrapper<>();
        Page<ClockIn> clockInPage = new Page<>(Integer.parseInt(clockInSearchVO.getPage()), 10);
        queryWrapper.eq("store_id", clockInSearchVO.getStoreId());
        if (!StringUtils.isEmpty(clockInSearchVO.getName())) {
            //通过姓名获取id
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("name", clockInSearchVO.getName());
            List<User> users = userMapper.selectList(userQueryWrapper);
            //放入
            queryWrapper.in("employee_id", users.stream().map(User::getId).collect(Collectors.toList()));
        }
        if (!StringUtils.isEmpty(clockInSearchVO.getSignType())) {
            queryWrapper.eq("sign_type", clockInSearchVO.getSignType());
        }
        if (!StringUtils.isEmpty(clockInSearchVO.getSignTime())) {
            queryWrapper.eq("sign_time", clockInSearchVO.getSignTime());
        }
        clockInMapper.selectPage(clockInPage, queryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("clockIns", clockInPage.getRecords());
        map.put("total", clockInPage.getTotal());
        return ResultVO.success().setData(map);
    }

    @Override
    public ResultVO changeClockInType(String id, String type) {
        ClockIn clockIn = clockInMapper.selectById(id);
        clockIn.setSignType(type);
        int res = clockInMapper.updateById(clockIn);
        if (res < 0) {
            ClassException.cast(CommonError.UPDATE_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    public ResultVO deleteClockIn(String[] ids) {
        int res = clockInMapper.deleteBatchIds(Arrays.asList(ids));
        if (res < 0) {
            ClassException.cast(CommonError.UPDATE_ERROR);
        }
        return ResultVO.success();
    }
}
