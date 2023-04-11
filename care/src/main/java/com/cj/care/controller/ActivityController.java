package com.cj.care.controller;

import com.cj.care.service.ActivityService;
import com.cj.common.entity.CommunityActivity;
import com.cj.common.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/care/care")
@Api("社区活动")
public class ActivityController {

    @Autowired
    private ActivityService activityService;


    @GetMapping("/getActivityInfo/{storeId}/{page}")
    @ApiOperation("获取该门店的活动信息")
    public ResultVO getActivityInfo(@PathVariable("storeId") String storeId, @PathVariable("page") String page) {
        return activityService.getActivityInfo(storeId, page);
    }

    @GetMapping("/getActivityImages/{storeId}")
    @ApiOperation("获取门店轮播图")
    public ResultVO getActivityImages(@PathVariable("storeId") String storeId){
        return activityService.getActivityImages(storeId);
    }

    @PostMapping("/addActivityInfo")
    @ApiOperation("添加活动信息")
    public ResultVO addActivityInfo(@RequestBody CommunityActivity communityActivity){
        return activityService.addActivityInfo(communityActivity);
    }
    @PostMapping("/delActivityInfo")
    @ApiOperation("删除活动信息")
    public ResultVO delActivityInfo(@RequestBody String[] ids){
        return activityService.delActivityInfo(ids);
    }
    @PostMapping("/addActivityImages")
    @ApiOperation("添加轮播图")
    public ResultVO addActivityImages(@RequestParam("file") MultipartFile file,@RequestParam("relativeDescription") String relativeDescription,@RequestParam("storeId") String storeId){
        return activityService.addActivityImages(file, relativeDescription, storeId);
    }



}
