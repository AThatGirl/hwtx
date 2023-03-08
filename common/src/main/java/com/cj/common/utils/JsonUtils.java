package com.cj.common.utils;


import com.alibaba.fastjson.JSONObject;

/**
 * json工具类
 *
 * @author 杰瑞
 * @date 2023/02/23
 */
public class JsonUtils {

    public static String parseRequestBody(String json, String paramName){
        JSONObject jsonObject = JSONObject.parseObject(json);
        return jsonObject.getString(paramName);
    }




}
