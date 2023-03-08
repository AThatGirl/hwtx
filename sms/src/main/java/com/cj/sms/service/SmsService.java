package com.cj.sms.service;

import com.cj.common.vo.ResultVO;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 短信服务
 *
 * @author 杰瑞
 * @date 2023/01/16
 */
public interface SmsService {

    /**
     * 发送短信验证码
     *
     * @param phone         电话
     * @param redisTemplate 复述,模板
     * @return {@link ResultVO}
     */
    ResultVO send(String phone, RedisTemplate<String, String> redisTemplate);


    /**
     * 通过电话得到验证码
     *
     * @param phone 电话
     * @return {@link ResultVO}
     */
    ResultVO getNoteByPhone(String phone, RedisTemplate<String, String> redisTemplate);

}
