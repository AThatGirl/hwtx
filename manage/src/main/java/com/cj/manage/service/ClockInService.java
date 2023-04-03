package com.cj.manage.service;

import com.cj.common.vo.ResultVO;
import com.cj.manage.vo.ClockInSearchVO;

/**
 * 考勤管理
 */
public interface ClockInService {

    /**
     * 条件查询打卡信息
     * @param clockInSearchVO 查询条件
     * @return {@link ResultVO}
     */
    ResultVO getClockIn(ClockInSearchVO clockInSearchVO);


    /**
     * 修改打卡类型
     * @param id id
     * @param type 类型
     * @return {@link ResultVO}
     */
    ResultVO changeClockInType(String id, String type);


    /**
     * 删除打卡记录
     * @param ids id数组
     * @return {@link ResultVO}
     */
    ResultVO deleteClockIn(String[] ids);


}
