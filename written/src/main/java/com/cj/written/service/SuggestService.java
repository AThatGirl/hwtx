package com.cj.written.service;

import com.cj.common.vo.ResultVO;
import com.cj.written.vo.SuggestVO;

/**
 * 建议
 *
 * @author 杰瑞
 * @date 2023/03/07
 */
public interface SuggestService {

    /**
     * 通过员工id获取建议
     *
     * @param id id
     * @return {@link ResultVO}
     */
    ResultVO getSuggestById(String id);


    /**
     * 提交建议
     *
     * @param suggestVO 建议签证官
     * @return {@link ResultVO}
     */
    ResultVO submitSuggest(SuggestVO suggestVO);


    /**
     * 删除建议通过id
     *
     * @param id id
     * @return {@link ResultVO}
     */
    ResultVO deleteSuggestById(String id);


}
