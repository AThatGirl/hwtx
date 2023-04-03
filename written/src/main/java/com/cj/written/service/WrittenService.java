package com.cj.written.service;

import com.cj.common.vo.ResultVO;
import com.cj.written.vo.WrittenUpdateVO;
import com.cj.written.vo.WrittenVO;

/**
 * 请假条服务
 *
 * @author 杰瑞
 * @date 2023/03/06
 */
public interface WrittenService {

    /**
     * 写请假条
     *
     * @param writtenVO 写签证官
     * @return {@link ResultVO}
     */
    ResultVO writeWritten(WrittenVO writtenVO);

    /**
     * 删除请假条
     *
     * @param ids ids
     * @return {@link ResultVO}
     */
    ResultVO deleteWritten(String[] ids);


    /**
     * 通过id获取请假条，分页
     *
     * @param id id
     * @param page 页码
     * @return {@link ResultVO}
     */
    ResultVO getWrittenById(String id, String page);

    /**
     *  修改请假条
     * @param writtenUpdateVO 修改请假条VO
     * @return {@link ResultVO}
     */
    ResultVO updateWrittenById(WrittenUpdateVO writtenUpdateVO);

}
