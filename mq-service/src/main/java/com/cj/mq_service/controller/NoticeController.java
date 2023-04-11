package com.cj.mq_service.controller;

import com.cj.common.vo.ResultVO;
import com.cj.common.vo.SendNoticeVO;
import com.cj.mq_service.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mq-service/notice")
@Api(tags = "通知")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping("/sendNotice")
    @ApiOperation("发送消息通知")
    public ResultVO sendNotice(@RequestBody SendNoticeVO sendNoticeVO){
        noticeService.sendNotice(sendNoticeVO);
        return ResultVO.success();
    }

    @PostMapping("/sendBlessing")
    @ApiOperation("发送祝福语")
    public ResultVO sendBlessing(@RequestBody String params){
        noticeService.sendBlessing(params);
        return ResultVO.success();
    }

}
