package com.cj.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.common.entity.User;
import com.cj.common.entity.Written;
import com.cj.common.mapper.UserMapper;
import com.cj.common.mapper.WrittenMapper;
import com.cj.common.vo.ResultVO;
import com.cj.manage.service.WrittenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WrittenServiceImpl implements WrittenService {

    @Autowired
    private WrittenMapper writtenMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResultVO search(String name, String status) {
        //通过姓名或者查找请假条信息
        Page<Written> page = new Page<>(0, 10);
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        QueryWrapper<Written> writtenWrapper = new QueryWrapper<>();
        //条件
        if (name != null && name.length() > 0) {
            userWrapper.eq("name",name);
            writtenWrapper.eq("employee_id", userMapper.selectOne(userWrapper).getId());
        }
        if (status!= null && status.length() > 0) {
            writtenWrapper.eq("status", status);
        }
        writtenMapper.selectPage(page, writtenWrapper);
        //map存储当前页数，数据总数和查询的数据
        Map<String, Object> map = new HashMap<>();
        map.put("current", page.getCurrent());
        map.put("total", page.getTotal());
        map.put("items", page.getRecords());
        return ResultVO.success().setData(map);
    }
}
