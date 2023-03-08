package com.cj.manage.service;

import com.cj.common.vo.ResultVO;

/**
 * 请假条管理
 *
 * @author 杰瑞
 */
public interface WrittenService {

    /**
     * 搜索请假信息
     *
     * @param name  请假人姓名
     * @param status 请假状态
     * @return {@link ResultVO}
     */
    ResultVO search(String name, String status);



}
