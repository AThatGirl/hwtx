package com.cj.manage.service.impl;

import com.cj.common.vo.ResultVO;
import com.cj.common.vo.SendNoticeVO;
import com.cj.manage.feign.MqServiceFeignClient;
import com.cj.manage.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private MqServiceFeignClient mqServiceFeignClient;

    @Override
    public ResultVO sendNotice(SendNoticeVO sendNoticeVO) {
        return mqServiceFeignClient.sendNotice(sendNoticeVO);
    }


}
