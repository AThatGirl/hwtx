package com.cj.personal.controller;


import com.cj.common.entity.User;
import com.cj.common.vo.ResultVO;
import com.cj.personal.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Api(tags = "个人信息")
@RequestMapping("/personal/message")
public class MessageController {

    @Autowired
    private MessageService messageService;


    @GetMapping("/getMessageById/{id}")
    @ApiOperation("获取个人信息")
    public ResultVO getMessageById(@PathVariable("id") String id){
        return messageService.getMessageById(id);
    }

    @PostMapping("/updateMessage")
    @ApiOperation("更新个人信息")
    public ResultVO updateMessage(@RequestBody User user){
        return messageService.updateMessage(user);
    }

}
