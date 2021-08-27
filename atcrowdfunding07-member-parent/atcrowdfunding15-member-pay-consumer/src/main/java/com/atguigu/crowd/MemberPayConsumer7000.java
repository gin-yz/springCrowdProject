package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: JinSheng
 * @date: 2021/08/23 9:00 AM
 */
@EnableFeignClients
@SpringBootApplication
public class MemberPayConsumer7000 {
    public static void main(String[] args) {
        SpringApplication.run(MemberPayConsumer7000.class,args);
    }
}
