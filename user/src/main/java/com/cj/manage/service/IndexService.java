package com.cj.manage.service;

import com.cj.common.vo.ResultVO;

/**
 * 首页
 */
public interface IndexService {


    /**
     * 获取首页相关数量
     * @return
     */
    ResultVO indexNum();


    /**
     * 本周签到人数
     * @return
     */
    ResultVO getClockInNumWeek(String store);


    /**
     * 获取男女比例
     * @return
     */
    ResultVO getSexRatio(String storeId);
}
