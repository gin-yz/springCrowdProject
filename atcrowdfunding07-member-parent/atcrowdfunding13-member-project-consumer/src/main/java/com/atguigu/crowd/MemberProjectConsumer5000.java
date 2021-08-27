package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: JinSheng
 * @date: 2021/08/17 7:43 PM
 */
@SpringBootApplication
@EnableFeignClients
public class MemberProjectConsumer5000 {
    public static void main(String[] args) {
        SpringApplication.run(MemberProjectConsumer5000.class, args);
    }
}
