package com.cj.mq_service.service.impl;

import com.cj.mq_service.common.MqSendUtils;
import com.cj.mq_service.config.FanoutConfig;
import com.cj.mq_service.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void updateStore(String msg) {
        String exchangeName = FanoutConfig.FANOUT_EXCHANGE_NAME;
        String routingKey = "";
        //消息回调
        CorrelationData correlationData = MqSendUtils.callbackUtil(exchangeName, routingKey, msg, rabbitTemplate);
        //发送消息
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, correlationData);
        log.info("发送成功！");
    }




}
