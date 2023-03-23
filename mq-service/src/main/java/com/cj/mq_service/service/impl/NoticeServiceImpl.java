package com.cj.mq_service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cj.mq_service.common.MqSendUtils;
import com.cj.mq_service.config.FanoutConfig;
import com.cj.mq_service.service.NoticeService;
import com.cj.common.vo.SendNoticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendNotice(SendNoticeVO sendNoticeVO) {

        String exchangeName = FanoutConfig.NOTICE_EXCHANGE_NAME;
        String routingKey = "";
        //对象转json
        String msg = JSONObject.toJSONString(sendNoticeVO);
        //消息回调
        CorrelationData correlationData = MqSendUtils.callbackUtil(exchangeName, routingKey, msg, rabbitTemplate);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, correlationData);
        log.info("发送成功！");
    }
}
