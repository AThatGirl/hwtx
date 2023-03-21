package com.cj.sms.controller;

import com.cj.common.vo.ResultVO;
import com.cj.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms/sms")
@Api(tags = "短信服务")
public class SmsController {


    @Autowired
    private SmsService smsService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @GetMapping(value = "/send/{phone}")
    @ApiOperation("发送短信")
    public ResultVO code(@PathVariable String phone) {
        return smsService.send(phone, redisTemplate);
    }

    @GetMapping("/getNoteByPhone/{phone}")
    @ApiOperation("获取短信验证码")
    public ResultVO getNoteByPhone(@PathVariable String phone){
        return smsService.getNoteByPhone(phone, redisTemplate);
    }

}
