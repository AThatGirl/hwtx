package com.cj.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.common.en.CommonError;
import com.cj.common.en.UserCareer;
import com.cj.common.entity.*;
import com.cj.common.exception.ClassException;
import com.cj.common.mapper.*;
import com.cj.common.vo.ResultVO;
import com.cj.manage.service.UserService;
import com.cj.manage.vo.UserSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

        Page<User> page = new Page<>(userSearchVO.getPage(), 10);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.ne("career", UserCareer.SUPER_ADMIN.getCareer());
        //根据名字查询
        if (!StringUtils.isEmpty(userSearchVO.getName())) {
            userQueryWrapper.eq("name", userSearchVO.getName());
        }
        if (!StringUtils.isEmpty(userSearchVO.getStatus())) {
            userQueryWrapper.eq("status", userSearchVO.getStatus());
        }
        userQueryWrapper.eq("store_id", userSearchVO.getStoreId());
        userMapper.selectPage(page, userQueryWrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("users", page.getRecords());
        return ResultVO.success().setData(map);

    }

    @Override
    public ResultVO changeUserInfo(User user) {
        int res = userMapper.updateById(user);
        if (res < 0) {
            ClassException.cast(CommonError.UPDATE_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    @Transactional
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
            return ResultVO.fail().setMessage("删除异常");
        }
        return ResultVO.success();
    }

    @Override
    public ResultVO examine(String id, Integer pass) {
        User user = userMapper.selectById(id);
        if (User.NO_PASS.equals(pass)) {
            int del = userMapper.deleteById(user.getId());
            if (del < 0) {
                ClassException.cast(CommonError.DELETE_ERROR);
            }
        }else {
            user.setPass(pass);
            int res = userMapper.updateById(user);
            if (res < 0) {
                ClassException.cast(CommonError.UPDATE_ERROR);
            }
        }

        return ResultVO.success();
    }

    @Override
    public ResultVO searchAllUser(String storeId) {
        List<User> users = userMapper.selectList(new QueryWrapper<User>().eq("store_id", storeId));
        return ResultVO.success().setData(users);
    }


}
