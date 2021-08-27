package com.atguigu.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//@EnableDiscoveryClient //在这里不用写
@SpringBootApplication
// 扫描MyBatis的Mapper接口所在的包
@MapperScan("com.atguigu.crowd.mapper")
public class MemberMysqlProvider2000 {
    public static void main(String[] args) {
        SpringApplication.run(MemberMysqlProvider2000.class,args);
    }
}
