package com.cj.care;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.cj.common.mapper")
@ComponentScan(basePackages = {"com.cj"})
public class CareApplication {
    public static void main(String[] args) {
        SpringApplication.run(CareApplication.class, args);

    }
}
