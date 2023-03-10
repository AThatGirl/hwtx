package com.cj.manage.service;

import com.cj.common.vo.ResultVO;
import com.cj.common.vo.WrittenSearchVO;

/**
 * 请假条管理
 *
 * @author 杰瑞
 */
public interface WrittenService {

    /**
     * 搜索请假信息
     *
     * @param writtenSearchVO 请假条
     * @return {@link ResultVO}
     */
    ResultVO search(WrittenSearchVO writtenSearchVO);





}
