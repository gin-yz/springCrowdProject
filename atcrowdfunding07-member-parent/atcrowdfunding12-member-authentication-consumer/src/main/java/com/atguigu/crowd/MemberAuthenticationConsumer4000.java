package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class MemberAuthenticationConsumer4000 {
    public static void main(String[] args) {
        SpringApplication.run(MemberAuthenticationConsumer4000.class,args);
    }
}