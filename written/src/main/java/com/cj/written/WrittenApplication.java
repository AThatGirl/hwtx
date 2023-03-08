package com.cj.written;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.cj.common.mapper")
@ComponentScan(basePackages = "com.cj")
public class WrittenApplication {
    public static void main(String[] args) {
        SpringApplication.run(WrittenApplication.class, args);
    }

}
