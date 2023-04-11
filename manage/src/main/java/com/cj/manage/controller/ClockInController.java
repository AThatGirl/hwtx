package com.cj.manage.controller;


import com.cj.common.vo.ResultVO;
import com.cj.manage.service.ClockInService;
import com.cj.manage.vo.ClockInSearchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/manage/clockIn")
@Api(tags = "打卡管理", value = "打卡管理")
public class ClockInController {

    @Autowired
    private ClockInService clockInService;

    @PostMapping("/getClockIn")
    @ApiOperation("获得考勤信息")
    public ResultVO getClockIn(@RequestBody ClockInSearchVO clockInSearchVO) {
        return clockInService.getClockIn(clockInSearchVO);
    }


    @PostMapping("/changeClockInType")
    @ApiOperation("修改打卡类型")
    public ResultVO changeClockInType(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        String signType = map.get("signType");
        return clockInService.changeClockInType(id, signType);
    }

    @PostMapping("/deleteClockIn")
    @ApiOperation("删除打卡记录")
    public ResultVO deleteClockIn(@RequestBody String[] ids) {
        return clockInService.deleteClockIn(ids);
    }

    @PostMapping("/publishGesture")
    @ApiOperation("发布手势签到")
    public ResultVO publishGesture(@RequestBody Map<String, String> map){
        String gesture = map.get("gesture");
        return clockInService.publishGesture(gesture);
    }

}
