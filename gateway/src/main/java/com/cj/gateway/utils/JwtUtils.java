package com.cj.gateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {
    //过期时间
    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    //密钥
    public static final String APP_SECRET = "zhumeng123123";

    /**
     * 得到jwt令牌
     *
     * @param id   id
     * @param name 名字
     * @return {@link String}
     */
    public static String getJwtToken(String id, String name) {
        String jwtToken = Jwts.builder()
                // 设置头信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // 设置分类
                .setSubject("classA33")
                // 设置发布和过期时间
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                // 设置token主体部分
                .claim("id", id)
                .claim("name", name)
                // 编码
                .signWith(SignatureAlgorithm.ES256, APP_SECRET)
                .compact();
        return jwtToken;
    }

    /**
     * 判断token是否有效
     *
     * @param jwtToken jwt令牌
     * @return boolean
     */
    public static boolean checkToken(String jwtToken) {
        if (jwtToken == null || jwtToken.trim().isEmpty()) {
            return false;
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
            //String id = claimsJws.getBody().get("id", String.class);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 校验JWT Token是否过期，token为一个字符串
     *
     * @param token 令牌
     * @return boolean
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(APP_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            Date expireDate = claims.getExpiration();
            return expireDate.before(new Date());
        } catch (Exception e) {
            return true; // 如果解析Token出现异常，视为Token过期
        }
    }

    public static Object getMemberByJwtToken(String jwtToken, String param) {
        if (!checkToken(jwtToken)){
            return "";
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return claims.get(param);

    }

}
