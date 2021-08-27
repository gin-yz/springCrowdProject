package com.atguigu.crowd.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
//@Component //在Controller类(如MemberHandler)上添加@EnableConfigurationProperties(ShortMessageProperties.class)
@ConfigurationProperties(prefix = "short.message")
public class ShortMessageProperties {

    private String host;
    private String path;
    private String method;
    private String appCode;
    private String sign;
    private String skin;

}
