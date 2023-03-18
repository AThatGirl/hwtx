package com.cj.mq_service.controller;

import com.cj.common.vo.ResultVO;
import com.cj.mq_service.service.NoticeService;
import com.cj.mq_service.vo.SendOneVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/mq-service/notice")
@Api(tags = "通知")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/sendToEmployee")
    @ApiOperation("发送消息通知")
    public ResultVO sendToEmployee(@ModelAttribute SendOneVO sendOneVO){
        return noticeService.sendToEmployee(sendOneVO);
    }


}
