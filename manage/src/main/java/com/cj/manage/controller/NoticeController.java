package com.cj.manage.controller;

import com.cj.common.vo.ResultVO;
import com.cj.common.vo.SendNoticeVO;
import com.cj.manage.service.NoticeService;
import com.cj.manage.vo.NoticeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manage/notice")
@Api(tags = "通知信息管理", value = "通知信息管理")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping("/sendNotice")
    @ApiOperation("查询门店信息")
    public ResultVO sendNotice(@RequestBody SendNoticeVO sendNoticeVO) {
        return noticeService.sendNotice(sendNoticeVO);
    }

    @PostMapping("/getNoticeForPage")
    @ApiOperation("获取所有通知")
    public ResultVO getNoticeForPage(@RequestBody NoticeVO noticeVO) {
        return noticeService.getNoticeForPage(noticeVO);
    }

    @PostMapping("/readNotice")
    @ApiOperation("已读消息")
    public ResultVO readNotice(@RequestBody String id) {
        return noticeService.readNotice(id);
    }


}
