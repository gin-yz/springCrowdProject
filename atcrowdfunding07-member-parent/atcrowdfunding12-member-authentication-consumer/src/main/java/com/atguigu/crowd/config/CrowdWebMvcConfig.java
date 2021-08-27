package com.atguigu.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** 这个配置文件相当于前面项目的spring-web-mvc.xml，只不过在springBoot中是采用java代码的方式配置
 * @author: JinSheng
 * @date: 2021/08/13 10:34 AM
 */
@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    /**
     * 相当于xml配置的<mvc:view-controller path="" view-name=""/>
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 浏览器访问的地址
        String urlPath = "/auth/member/to/reg/page";

        // 目标视图的名称，将来拼接“prefix: classpath:/templates/”、“suffix: .html”前后缀
        String viewName = "member-reg";

        // 添加view-controller
        registry.addViewController(urlPath).setViewName(viewName);
        registry.addViewController("/auth/member/to/login/page").setViewName("member-login");
        registry.addViewController("/auth/member/to/center/page").setViewName("member-center");
        registry.addViewController("/member/my/crowd").setViewName("member-crowd");

    }
}
