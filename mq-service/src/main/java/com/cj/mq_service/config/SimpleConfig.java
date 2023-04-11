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
public class SimpleConfig {
    public static final String BLESSING_QUEUE_NAME = "simple.blessing";


    //声明队列
    @Bean
    public Queue blessingQueue(){
        return new Queue(BLESSING_QUEUE_NAME);
    }



}
