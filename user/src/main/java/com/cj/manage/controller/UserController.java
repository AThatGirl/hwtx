package com.cj.manage.controller;


import com.cj.common.utils.JsonUtils;
import com.cj.common.vo.ResultVO;
import com.cj.manage.feign.SmsFeignClient;
import com.cj.manage.service.LoginService;
import com.cj.manage.service.RegisterService;
import com.cj.manage.vo.RegisterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/user")
@Api(tags = "用户登陆注册")
public class UserController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private SmsFeignClient smsFeignClient;

    @PostMapping("/loginForPassword")
    @ApiOperation("密码登录")
    public ResultVO loginForPassword(@RequestBody String params){
        String phone = JsonUtils.parseRequestBody(params, "phone");
        String password = JsonUtils.parseRequestBody(params, "password");
        return loginService.loginForPassword(phone, password);
    }

    @PostMapping("/loginForNote")
    @ApiOperation("短信登录")
    public ResultVO loginForNote(@RequestBody String params){
        String phone = JsonUtils.parseRequestBody(params, "phone");
        String note = JsonUtils.parseRequestBody(params, "note");
        ResultVO noteResult = smsFeignClient.getNoteByPhone(phone);
        if (noteResult.getData() == null || !noteResult.getData().equals(note)){
            return ResultVO.fail().setMessage("验证码过期或者不存在");
        }
        redisTemplate.delete(phone);
        return loginService.loginForNote(phone, note);
    }

    @PostMapping("/userRegister")
    @ApiOperation("注册")
    public ResultVO userRegister(@RequestBody RegisterVO registerVO){
        ResultVO noteResult = smsFeignClient.getNoteByPhone(registerVO.getPhone());
        if (noteResult.getData() == null || !noteResult.getData().equals(registerVO.getNote())){
            return ResultVO.fail().setMessage("验证码过期或者不存在");
        }
        redisTemplate.delete(registerVO.getPhone());
        return registerService.userRegister(registerVO);
    }


}
