package com.cj.manage.utils;

import com.cj.common.entity.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    public static String getToken(User user){
        //使用jwt规则生成token字符串
        JwtBuilder builder = Jwts.builder();

        Map<String, Object> map = new HashMap<>();
        map.put("manage", user);
        String token = builder.setSubject(user.getName())//主题，就是token中的数据
                .setIssuedAt(new Date())//设置token生成的时间
                .setId(user.getId())//设置用户id为token id
                .setClaims(map)//map中存放用户的角色，权限信息
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))//设置token过期时间
                .signWith(SignatureAlgorithm.HS256, "zhumeng123")//设置加密方式和加密密码
                .compact();
        return token;
    }
}
