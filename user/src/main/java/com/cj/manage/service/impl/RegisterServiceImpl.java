package com.cj.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.common.en.CommonError;
import com.cj.common.en.UserCareer;
import com.cj.common.entity.User;
import com.cj.common.exception.ClassException;
import com.cj.common.mapper.UserMapper;
import com.cj.common.utils.Md5Utils;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.ResultVO;
import com.cj.manage.feign.MqServiceFeignClient;
import com.cj.manage.service.RegisterService;
import com.cj.manage.vo.RegisterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MqServiceFeignClient mqServiceFeignClient;

    @Override
    public ResultVO userRegister(RegisterVO registerVO) {
        User user = new User();
        //手机号是否被注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", registerVO.getPhone());
        User existUser = userMapper.selectOne(queryWrapper);
        if (existUser != null) {
            return ResultVO.fail().setMessage("该手机号已经注册");
        }
        //短信验证
//        String note = redisTemplate.opsForValue().get(registerVO.getPhone());
//        if (note == null || ! note.equals(registerVO.getNote())) {
//            return ResultVO.fail().setMessage("验证码错误或不存在");
//        }
        //判断是否是超级管理员注册
        if (UserCareer.fromCareer(registerVO.getCareer()) == UserCareer.SUPER_ADMIN) {
            user.setPass(User.YES_PASS);
        } else {
            //其他
            user.setPass(User.PASSING);
        }
        user.setId(UUIDUtils.getId());
        user.setName(registerVO.getName());
        user.setAge(registerVO.getAge());
        user.setSex(registerVO.getSex());
        user.setBirthday(registerVO.getBirthday());
        user.setPhone(registerVO.getPhone());
        user.setPassword(Md5Utils.md5(registerVO.getPassword()));
        user.setAvatar("default.png");
        user.setCareer(registerVO.getCareer());
        user.setEmail(registerVO.getEmail());
        user.setStoreId(registerVO.getStoreId());
        int res = userMapper.insert(user);
        if (res < 0) {
            ClassException.cast(CommonError.INSERT_ERROR);
        }
        //发送消息
        mqServiceFeignClient.updateStore(user.getStoreId());
        return ResultVO.success().setMessage("注册成功").setData(user);
    }
}
