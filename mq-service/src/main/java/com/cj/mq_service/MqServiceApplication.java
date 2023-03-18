package com.cj.mq_service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.cj.common.mapper")
@ComponentScan(basePackages = {"com.cj"})
@EnableDiscoveryClient
public class MqServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MqServiceApplication.class, args);
    }

}
