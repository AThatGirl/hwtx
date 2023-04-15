package com.cj.personal.service;

import com.cj.common.entity.User;
import com.cj.common.vo.ResultVO;

import java.util.List;

/**
 * 个人信息
 *
 * @author 杰瑞
 * @date 2023/03/07
 */
public interface MessageService {


    /**
     * 通过id获取个人信息
     *
     * @param id id
     * @return {@link ResultVO}
     */
    ResultVO getMessageById(String id);

    /**
     * 修改个人信息
     *
     * @param user 用户
     * @return {@link ResultVO}
     */
    ResultVO updateMessage(User user);

    List<User> getAllMessage(String storeId);
}
