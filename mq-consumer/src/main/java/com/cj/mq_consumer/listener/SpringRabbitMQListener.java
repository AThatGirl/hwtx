package com.cj.mq_consumer.listener;

import com.cj.mq_consumer.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听消息
 */
@Slf4j
@Component
public class SpringRabbitMQListener {

    @Autowired
    private StoreService storeService;

    public static final String FANOUT_EXCHANGE_NAME = "notice.fanout";
    public static final String FANOUT_QUEUE_NAME = "fanout.queue";


    @RabbitListener(queues = FANOUT_QUEUE_NAME)
    public void listenFanoutQueue(String msg){
        //msg 为发送的id
        storeService.updateStoreUserNum(msg);
        log.info("消费者处理消息成功！");
    }





}
