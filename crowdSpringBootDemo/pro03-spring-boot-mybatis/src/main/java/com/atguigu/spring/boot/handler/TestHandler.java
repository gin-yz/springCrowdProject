package com.atguigu.spring.boot.handler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestHandler {

    @GetMapping("/hello")
    public String testMapping(){
        return "hello world!";
    }
}
