package com.cj.manage.service;

import com.cj.common.vo.ResultVO;

/**
 * 登录服务接口
 *
 * @author 杰瑞
 * @date 2023/02/23
 */
public interface LoginService {

    /**
     * 用密码登录
     *
     * @param phone    电话
     * @param password 密码
     * @return {@link ResultVO}
     */
    ResultVO loginForPassword(String phone, String password);


    /**
     * 用短信登录
     *
     * @param phone 电话
     * @param note  验证码
     * @return {@link ResultVO}
     */
    ResultVO loginForNote(String phone, String note);



}
