package com.cj.mq_service.service.impl;

import com.cj.common.entity.Notice;
import com.cj.common.mapper.NoticeMapper;
import com.cj.common.utils.DateUtils;
import com.cj.common.utils.UUIDUtils;
import com.cj.common.vo.ResultVO;
import com.cj.mq_service.config.FanoutConfig;
import com.cj.mq_service.service.NoticeService;
import com.cj.mq_service.vo.SendOneVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService {


    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResultVO sendToEmployee(SendOneVO sendOneVO) {
        Notice notice = new Notice();
        notice.setId(UUIDUtils.getId());
        notice.setSenderId(sendOneVO.getSendId());
        notice.setReceiverId(sendOneVO.getReceiveId());
        notice.setContent(sendOneVO.getContent());
        notice.setStatus(Notice.UNREAD_STATUS);
        notice.setCreateTime(DateUtils.getNowDate());
        try {
            noticeMapper.insert(notice);
        }catch (Exception e){
            e.printStackTrace();
            return ResultVO.fail();
        }
        String queueName = FanoutConfig.FANOUT_QUEUE_NAME;
        String message = sendOneVO.getContent();
        rabbitTemplate.convertAndSend(queueName, "", message);
        return ResultVO.success();
    }
}
