package com.cj.personal.controller;


import com.cj.common.entity.ClockIn;
import com.cj.common.utils.DateUtils;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.ResultVO;

import com.cj.personal.service.ClockInService;
import com.cj.personal.vo.PlatPunch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jerry
 * @since 2023-01-10
 */
@RestController
@RequestMapping("/personal/clock-in")
@Api(tags = "打卡功能")
public class ClockInController {

    @Autowired
    private ClockInService clockInService;

    @PostMapping("/localPunch")
    @ApiOperation("地理位置打卡")
    public ResultVO localPunch(@RequestBody PlatPunch platPunch){

        return ResultVO.success().setMessage("打卡成功");
    }


}


