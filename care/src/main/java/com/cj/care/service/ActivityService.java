package com.cj.care.service;

import com.cj.common.entity.CommunityActivity;
import com.cj.common.vo.ResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 社区活动
 */
public interface ActivityService {


    /**
     * 获取活动记录
     * @param storeId
     * @param page
     * @return
     */
    ResultVO getActivityInfo(String storeId, String page);

    /**
     * 获取轮播图
     * @param storeId
     * @return
     */
    ResultVO getActivityImages(String storeId);


    /**
     * 添加活动信息
     * @param communityActivity
     * @return
     */
    ResultVO addActivityInfo(CommunityActivity communityActivity);

    /**
     * 删除活动信息
     * @param ids
     * @return
     */
    ResultVO delActivityInfo(String[] ids);

    /**
     * 添加活动图片
     * @param file
     * @param relativeDescription
     * @param storeId
     * @return
     */
    ResultVO addActivityImages(MultipartFile file, String relativeDescription, String storeId);

}
