package com.cj.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.common.entity.User;
import com.cj.common.mapper.UserMapper;
import com.cj.common.utils.CheckUtils;
import com.cj.common.utils.Md5Utils;
import com.cj.common.vo.ResultVO;
import com.cj.manage.service.LoginService;
import com.cj.manage.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResultVO loginForPassword(String phone, String password) {
        //根据手机号和密码查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone).eq("password", Md5Utils.md5(password));
        User user = userMapper.selectOne(queryWrapper);
        //查不到，管理员不存在
        if (user == null) {
            logger.info("手机号或密码错误");
            return ResultVO.fail().setMessage("手机号或密码错误");
        } else {
            //查看是否通过审核
            ResultVO resultVO = new ResultVO();
            if (!CheckUtils.checkPass(user.getPass(), resultVO)){
                return resultVO;
            }
            //密码正确，登录验证成功，则需要生成令牌token（token就是按照特定规则生成的字符串）
            String token = JwtUtils.getToken(user);
            return ResultVO.success().setMessage(token).setData(user);
        }
    }

    @Override
    public ResultVO loginForNote(String phone, String note) {

        //根据手机号查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            return ResultVO.fail().setMessage("用户不存在");
        }
        //查看是否通过审核
        ResultVO resultVO = new ResultVO();
        if (!CheckUtils.checkPass(user.getPass(), resultVO)){
            return resultVO;
        }
        //验证码正确，登录验证成功，则需要生成令牌token（token就是按照特定规则生成的字符串）
        String token = JwtUtils.getToken(user);
        return ResultVO.success().setMessage(token).setData(user);
    }


}
