package com.cj.mq_consumer.listener;

import com.cj.mq_consumer.service.NoticeService;
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

    @Autowired
    private NoticeService noticeService;

    public static final String STORE_QUEUE_NAME = "fanout.store";
    public static final String NOTICE_QUEUE_NAME = "fanout.notice";
    public static final String BLESSING_QUEUE_NAME = "simple.blessing";


    /**
     * 每次增加一个员工或者删除，就会更新门店信息
     * @param msg
     */
    @RabbitListener(queues = STORE_QUEUE_NAME)
    public void listenStoreQueue(String msg){
        //msg 为发送的id
        storeService.updateStoreUserNum(msg);
        log.info("门店信息更新成功！");
    }

    /**
     * 发送通知到员工的邮箱
     * @param msg
     */
    @RabbitListener(queues = NOTICE_QUEUE_NAME)
    public void listenNoticeQueue(String msg){
        //msg为json对象
        noticeService.sendNotice(msg);
        log.info("发送通知成功！");
    }

    @RabbitListener(queues = BLESSING_QUEUE_NAME)
    public void listenBlessingQueue(String msg){
        noticeService.sendBlessing(msg);
        log.info("消费祝福消息成功");
    }




}
