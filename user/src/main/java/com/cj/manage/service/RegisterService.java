package com.cj.manage.service;

import com.cj.common.entity.User;
import com.cj.common.vo.ResultVO;
import com.cj.manage.vo.RegisterVO;

/**
 * 注册服务
 *
 * @author 杰瑞
 * @date 2023/03/05
 */
public interface RegisterService {

    /**
     * 用户注册
     *
     * @param user 用户
     * @return {@link ResultVO}
     */
    ResultVO userRegister(RegisterVO registerVO);

}
