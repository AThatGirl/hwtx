package com.cj.mq_service.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Fanout消息队列
 */
@Configuration
public class FanoutConfig {
    //store
    public static final String STORE_EXCHANGE_NAME = "store.fanout";
    public static final String STORE_QUEUE_NAME = "fanout.store";

    //notice
    public static final String NOTICE_EXCHANGE_NAME = "notice.fanout";
    public static final String NOTICE_QUEUE_NAME = "fanout.notice";
    
    

    //声明交换机
    @Bean
    public FanoutExchange storeExchange(){
        return new FanoutExchange(STORE_EXCHANGE_NAME);
    }
    //声明队列
    @Bean
    public Queue storeQueue(){
        return new Queue(STORE_QUEUE_NAME);
    }
    @Bean
    public Binding storeBinding(Queue storeQueue, FanoutExchange storeExchange){
        return BindingBuilder.bind(storeQueue).to(storeExchange);
    }

    //声明交换机
    @Bean
    public FanoutExchange noticeExchange(){
        return new FanoutExchange(NOTICE_EXCHANGE_NAME);
    }
    //声明队列
    @Bean
    public Queue noticeQueue(){
        return new Queue(NOTICE_QUEUE_NAME);
    }
    @Bean
    public Binding noticeBinding(Queue noticeQueue, FanoutExchange noticeExchange){
        return BindingBuilder.bind(noticeQueue).to(noticeExchange);
    }


}
