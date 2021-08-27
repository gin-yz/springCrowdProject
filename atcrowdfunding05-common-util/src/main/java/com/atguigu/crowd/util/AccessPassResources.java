package com.atguigu.crowd.util;

import com.atguigu.crowd.constant.CrowdConstant;

import java.util.HashSet;
import java.util.Set;

/**
 * 在后面分布式springBoot中判断是否被过滤执行的请求
 *
 * @author: JinSheng
 * @date: 2021/08/16 4:53 PM
 */
public class AccessPassResources {
    public static final Set<String> PASS_RES_SET = new HashSet<>(7);

    static {
        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/member/to/reg/page");
        PASS_RES_SET.add("/auth/member/to/login/page");
        PASS_RES_SET.add("/auth/member/logout");
        PASS_RES_SET.add("/auth/member/do/login");
        PASS_RES_SET.add("/auth/do/member/register");
        PASS_RES_SET.add("/auth/member/send/short/message.json");
    }

    public static final Set<String> STATIC_RES_SET = new HashSet<>(9);

    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
        STATIC_RES_SET.add("images");
    }

    /**
     * 判读某一静态资源是否不需要登陆才能使用
     *
     * @param servletPath 请求的地址，注意这个地址是绝对路径
     * @return true(在资源内不需要登陆就可以使用)或false(需要登陆)
     */
    public static boolean judgeCurrentServletPathWetherStaticResource(String servletPath) {
        // 1.排除字符串无效的情况
        if(servletPath == null || servletPath.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        // 2.根据“/”拆分ServletPath字符串
        String[] split = servletPath.split("/");

        // 3.考虑到第一个斜杠左边经过拆分后得到一个空字符串是数组的第一个元素，所以需要使用下标1取第二个元素
        String firstLevelPath = split[1];

        // 4.判断是否在集合中
        return STATIC_RES_SET.contains(firstLevelPath);
    }

}
