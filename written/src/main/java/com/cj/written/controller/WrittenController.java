package com.cj.written.controller;


import com.cj.common.vo.ResultVO;
import com.cj.written.service.WrittenService;
import com.cj.written.vo.WrittenVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/written")
@Api(tags = "请假条", value = "请假条")
public class WrittenController {

    @Autowired
    private WrittenService writtenService;


    @PostMapping("/writeWritten")
    @ApiOperation("写请假条")
    public ResultVO writeWritten(@RequestBody WrittenVO writtenVO) {
        return writtenService.writeWritten(writtenVO);
    }

    @PostMapping("/deleteWritten")
    @ApiOperation("删除请假条")
    public ResultVO deleteWritten(@RequestBody String[] ids){
        return writtenService.deleteWritten(ids);
    }

    @GetMapping("/getWrittenById/{id}")
    @ApiOperation("通过用户id获取请假条")
    public ResultVO getWrittenById(@PathVariable String id){
        return writtenService.getWrittenById(id);
    }

}
