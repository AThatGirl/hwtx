package com.cj.personal.service;


import com.cj.common.entity.Preference;
import com.cj.common.vo.ResultVO;

/**
 * 偏好
 */
public interface PreferService {

    /**
     * 获得偏好信息
     * @param id 用户id
     * @return {@link ResultVO}
     */
    ResultVO getPrefer(String id);


    /**
     * 修改偏好信息
     * @param preference 偏好
     * @return {@link ResultVO}
     */
    ResultVO changePrefer(Preference preference);

    /**
     * 添加偏好
     * @param preference 偏好
     * @return {@link ResultVO}
     */
    ResultVO addPrefer(Preference preference);

}
