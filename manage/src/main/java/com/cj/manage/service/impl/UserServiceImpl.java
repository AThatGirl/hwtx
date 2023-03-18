package com.cj.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.common.en.UserCareer;
import com.cj.common.entity.*;
import com.cj.common.mapper.*;
import com.cj.common.vo.ResultVO;
import com.cj.manage.service.UserService;
import com.cj.manage.vo.UserSearchVO;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClockInMapper clockInMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private PreferenceMapper preferenceMapper;

    @Autowired
    private SuggestMapper suggestMapper;

    @Autowired
    private WorkFormMapper workFormMapper;

    @Autowired
    private WrittenMapper writtenMapper;


    @Override
    public ResultVO search(UserSearchVO userSearchVO) {

        Page<User> page = new Page<>(userSearchVO.getPageNum(), 10);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.ne("career", UserCareer.SUPER_ADMIN).ne("career", UserCareer.ADMIN);
        //根据名字查询
        if (!StringUtils.isEmpty(userSearchVO.getName())) {
            userQueryWrapper.eq("name", userSearchVO.getName());
        }
        if (!StringUtils.isEmpty(userSearchVO.getStatus())) {
            userQueryWrapper.eq("status", userSearchVO.getStatus());
        }

        try {
            userMapper.selectPage(page, userQueryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("users", page.getRecords());
        return ResultVO.success().setData(map);
    }

    @Override
    public ResultVO changeUserInfo(User user) {
        try {
            userMapper.updateById(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail();
        }
        return ResultVO.success();
    }

    @Override
    public ResultVO deleteUser(String[] ids) {

        try {
            List<String> list = Arrays.asList(ids);
            userMapper.deleteBatchIds(Arrays.asList(ids));
            clockInMapper.delete(new QueryWrapper<ClockIn>().in("employee_id", list));
            noticeMapper.delete(new QueryWrapper<Notice>().in("sender_id", list).or().in("receiver_id", list));
            preferenceMapper.delete(new QueryWrapper<Preference>().in("employee_id", list));
            suggestMapper.delete(new QueryWrapper<Suggest>().in("employee_id", list));
            workFormMapper.delete(new QueryWrapper<WorkForm>().in("employee_id", list));
            writtenMapper.delete(new QueryWrapper<Written>().in("employee_id", list));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail();
        }
        return ResultVO.success();
    }

    @Override
    public ResultVO examine(String id, Integer pass) {
        try {
            User user = userMapper.selectById(id);
            user.setPass(pass);
            userMapper.updateById(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail();
        }
        return ResultVO.success();
    }


}
