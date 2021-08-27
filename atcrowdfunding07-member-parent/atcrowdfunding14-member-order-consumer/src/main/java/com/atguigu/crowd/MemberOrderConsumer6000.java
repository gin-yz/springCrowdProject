package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: JinSheng
 * @date: 2021/08/22 3:44 PM
 */
@EnableFeignClients
@SpringBootApplication
public class MemberOrderConsumer6000 {
    public static void main(String[] args) {
        SpringApplication.run(MemberOrderConsumer6000.class, args);
    }
}
