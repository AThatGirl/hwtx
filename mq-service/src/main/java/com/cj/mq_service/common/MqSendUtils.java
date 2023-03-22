package com.cj.mq_service.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

/**
 * 发送消息
 */
@Slf4j
public class MqSendUtils {

    public static CorrelationData callbackUtil(String exchangeName, String routingKey, String msg, RabbitTemplate rabbitTemplate){
        //消息ID
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //准备ConfirmCallback
        correlationData.getFuture().addCallback(
                //成功的回调
                confirm -> {
                    //判断结果
                    if (confirm.isAck()) {
                        log.debug("消息成功投递到交换机");
                    } else {
                        log.error("消息投递到交换机失败！消息ID：{}", correlationData.getId());
                    }
                },
                //失败的回调
                throwable -> {
                    log.error("消息发送失败！", throwable);
                    //重新发送
                    rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, correlationData);
                });
        return correlationData;
    }


}
