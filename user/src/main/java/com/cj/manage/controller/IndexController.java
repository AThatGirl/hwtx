package com.cj.manage.controller;


import com.cj.common.vo.ResultVO;
import com.cj.manage.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/index")
@Api(tags = "首页信息")
public class IndexController {


    @Autowired
    private IndexService indexService;

    @GetMapping("/indexNum")
    @ApiOperation("首页数据")
    public ResultVO indexNum(){
        return indexService.indexNum();
    }

    @GetMapping("/getClockInNumWeek/{storeId}")
    @ApiOperation("本周签到人数")
    public ResultVO getClockInNumWeek(@PathVariable String storeId){
        return indexService.getClockInNumWeek(storeId);
    }

    @GetMapping("/getSexRatio/{storeId}")
    @ApiOperation("获取男女比例")
    public ResultVO getSexRatio(@PathVariable String storeId){
        return indexService.getSexRatio(storeId);
    }

}
