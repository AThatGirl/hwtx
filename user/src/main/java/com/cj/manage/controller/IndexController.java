package com.cj.manage.controller;


import com.cj.common.vo.ResultVO;
import com.cj.manage.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

}
