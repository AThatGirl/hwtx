package com.cj.mq_consumer.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 监听消息
 */
@Component
public class SpringRabbitMQListener {

    public static final String FANOUT_EXCHANGE_NAME = "notice.fanout";
    public static final String FANOUT_QUEUE_NAME = "fanout.queue";


    @RabbitListener(queues = FANOUT_QUEUE_NAME)
    public void listenFanoutQueue(String msg){
        System.out.println(msg);
    }





}
