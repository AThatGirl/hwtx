package com.cj.manage.controller;

import com.cj.common.vo.ResultVO;
import com.cj.manage.service.WrittenService;
import com.cj.common.vo.WrittenSearchVO;
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
@RequestMapping("/manage/written")
@Api(value = "请假条", tags =  "请假条")
public class WrittenController {

    public static final Integer DEFAULT_PAGE_NUM = 1;

    @Autowired
    private WrittenService writtenService;

    @GetMapping("/search")
    @ApiOperation(value = "根据名称或状态查询")
    public ResultVO search(@ModelAttribute WrittenSearchVO writtenSearchVO){
        if (writtenSearchVO.getPageNum() == null){
            writtenSearchVO.setPageNum(DEFAULT_PAGE_NUM);
        }
        return writtenService.search(writtenSearchVO);
    }

    @PostMapping("/examine")
    @ApiOperation(value = "审批请假条")
    public ResultVO examine(@RequestParam("id") String id, @RequestParam("status") String status){
        return writtenService.examine(id, status);
    }


}

