package com.cj.care.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.care.service.ActivityService;
import com.cj.care.utils.OSSClientFileUtils;
import com.cj.common.en.CommonError;
import com.cj.common.entity.CommunityActivity;
import com.cj.common.entity.ImageActivity;
import com.cj.common.exception.ClassException;
import com.cj.common.mapper.CommunityActivityMapper;
import com.cj.common.mapper.ImageActivityMapper;
import com.cj.common.utils.DateUtils;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private CommunityActivityMapper communityActivityMapper;

    @Autowired
    private ImageActivityMapper imageActivityMapper;

    @Autowired
    private OSSClientFileUtils ossClientFileUtils;

    @Override
    public ResultVO getActivityInfo(String storeId, String page) {

        Page<CommunityActivity> pageObj = new Page<>(StringUtils.isEmpty(page) ? 1 : Integer.parseInt(page), 10);
        communityActivityMapper.selectPage(pageObj, new QueryWrapper<CommunityActivity>().eq("store_id", storeId));
        Map<String, Object> map = new HashMap<>();
        map.put("total", pageObj.getTotal());
        map.put("data", pageObj.getRecords());
        return ResultVO.success().setData(map);
    }

    @Override
    public ResultVO getActivityImages(String storeId) {

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 格式化当前时间
        String nowStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 构造查询条件
        QueryWrapper<ImageActivity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("*")
                .le("create_time", nowStr)
                .orderByDesc("create_time")
                .last("limit 3");
        // 执行查询
        List<ImageActivity> imageActivities = imageActivityMapper.selectList(queryWrapper);
        return ResultVO.success().setData(imageActivities);
    }

    @Override
    public ResultVO addActivityInfo(CommunityActivity communityActivity) {
        communityActivity.setId(UUIDUtils.getId());
        communityActivity.setPublishTime(DateUtils.getNowDate());
        int res = communityActivityMapper.insert(communityActivity);
        if (res < 0) {
            ClassException.cast(CommonError.INSERT_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    public ResultVO delActivityInfo(String[] ids) {
        int res = communityActivityMapper.deleteBatchIds(Arrays.asList(ids));
        if (res < 0) {
            ClassException.cast(CommonError.DELETE_ERROR);
        }
        return ResultVO.success();
    }

    @Override
    public ResultVO addActivityImages(MultipartFile file, String relativeDescription, String storeId) {

        String fileUpload = null;
        try {
            ossClientFileUtils.initClient();
            fileUpload = ossClientFileUtils.uploadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVO.fail();
        }finally {
            ossClientFileUtils.closeClient();
        }
        if (StringUtils.isEmpty(fileUpload)) {
            return ResultVO.fail();
        }
        ImageActivity imageActivity = new ImageActivity();
        imageActivity.setId(UUIDUtils.getId());
        imageActivity.setImageUrl(fileUpload);
        imageActivity.setCreateTime(DateUtils.getNowDate());
        imageActivity.setRelativeDescription(relativeDescription);
        imageActivity.setStoreId(storeId);
        int res = imageActivityMapper.insert(imageActivity);
        if (res < 0) {
            ClassException.cast(CommonError.INSERT_ERROR);
        }
        return ResultVO.success();
    }


}
