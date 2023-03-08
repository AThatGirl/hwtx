package com.cj.personal.service;

import com.cj.common.vo.ResultVO;
import com.cj.personal.vo.PlatPunch;

/**
 * 签到
 *
 * @author 杰瑞
 * @date 2023/03/07
 */
public interface ClockInService {


    /*
        活动定位打卡功能 */
    ResultVO getPunch(PlatPunch platPunch);

}
