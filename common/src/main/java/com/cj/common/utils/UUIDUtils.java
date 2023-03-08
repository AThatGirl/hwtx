package com.cj.common.utils;

import java.util.UUID;

/**
 * uuid工具类
 *
 * @author 杰瑞
 * @date 2023/01/10
 */
public class UUIDUtils {

    public static String getId(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}
