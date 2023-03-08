package com.cj.sms.utils;

import org.springframework.data.redis.core.RedisTemplate;

public class CodeUtils {

    //检查redis中是否存在
    public static boolean checkCode(String phone, RedisTemplate<String, String> redisTemplate){
        String code = redisTemplate.opsForValue().get(phone);
        return code != null && !"".equals(code);
    }

    //删除redis中的phone --- code
    public static boolean deleteCode(String phone, RedisTemplate<String, String> redisTemplate){
        try {
            Boolean delete = redisTemplate.delete(phone);
            if(delete != null && delete){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
