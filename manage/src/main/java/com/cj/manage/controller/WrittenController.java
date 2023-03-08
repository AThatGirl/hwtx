package com.cj.manage.controller;

import com.cj.common.vo.ResultVO;
import com.cj.manage.service.WrittenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 请假条
 *
 *  @author 杰瑞
 */
@RestController
@CrossOrigin
@RequestMapping("/writtenManage")
@Api(value = "请假条", tags =  "请假条")
public class WrittenController {

    @Autowired
    private WrittenService writtenService;

    @GetMapping("/search/{name}/{status}")
    @ApiOperation(value = "根据名称查询")
    public ResultVO search(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "status",required = false) String status){
        return writtenService.search(name, status);
    }



}

