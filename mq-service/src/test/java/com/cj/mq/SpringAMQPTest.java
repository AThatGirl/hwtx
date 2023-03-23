package com.cj.mq;

import com.cj.mq_service.config.FanoutConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAMQPTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testQueue(){
        String queueName = FanoutConfig.STORE_QUEUE_NAME;
        String message = "hello, spring amqp";
        rabbitTemplate.convertAndSend(queueName, message);
    }

}
