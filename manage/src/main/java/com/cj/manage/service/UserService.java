package com.cj.manage.service;

import com.cj.common.entity.User;
import com.cj.common.vo.ResultVO;
import com.cj.manage.vo.UserSearchVO;

/**
 * 员工管理
 *
 */
public interface UserService {


    /**
     * 查找员工信息
     * @param userSearchVO 传入数据
     * @return {@link ResultVO}
     */
    ResultVO search(UserSearchVO userSearchVO);

    /**
     *修改信息
     * @param user 员工
     * @return {@link ResultVO}
     */
    ResultVO changeUserInfo(User user);


    /**
     * 删除信息
     * @param ids id数组
     * @return {@link ResultVO}
     */
    ResultVO deleteUser(String[] ids);

    /**
     *
     * 审核
     * @param id 用户id
     * @param pass 用户状态
     * @return {@link ResultVO}
     */
    ResultVO examine(String id, Integer pass);


}
