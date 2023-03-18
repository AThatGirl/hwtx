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

    public static final String FANOUT_EXCHANGE_NAME = "notice.fanout";
    public static final String FANOUT_QUEUE_NAME = "fanout.queue";

    //声明交换机
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE_NAME);
    }
    //声明队列
    @Bean
    public Queue fanoutQueue(){
        return new Queue(FANOUT_QUEUE_NAME);
    }

    @Bean
    public Binding fanoutBinding(Queue fanoutQueue, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutQueue).to(fanoutExchange);
    }


}
