package com.atguigu.crowd.filter;


import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.util.AccessPassResources;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author: JinSheng
 * @date: 2021/08/16 3:46 PM
 */
@Component
public class CrowdAccessFilter extends ZuulFilter {

    @Value("${zuul.routes.crowd-project.path}")
    private String crowdProjectUrl;

    /**
     *  // 将"/project/**"转换成"/project"
     */
    @PostConstruct
    private void processPrefixUrl(){
        int endIndex = crowdProjectUrl.indexOf("/", 1);
        crowdProjectUrl = crowdProjectUrl.substring(0, endIndex);
    }

    /**
     * 去掉前缀，比如"/project/images/1.jpg"=》"/images/1.jpg"
     * @param rawUrl 原url
     * @return 比如如果前缀有"/project"，将"/project"前缀去掉，没有的话就返回原
     */
    private String getRealUrl(String rawUrl) {
        boolean judge = rawUrl.startsWith(crowdProjectUrl);
        if (judge) {
            return rawUrl.substring(crowdProjectUrl.length());
        }
        return rawUrl;
    }

    @Override
    public String filterType() {
        // 这里返回“pre”意思是在目标微服务前执行过滤
        return "pre";
    }

    /**
     * 执行顺序
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 收否需要过滤，如果需要过滤，则执行下面的run方法，否则，比如为静态资源(css,js)直接放行，不需要做登陆检查
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        // 1.获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();

        // 2.通过RequestContext对象获取当前请求对象（框架底层是借助ThreadLocal从当前线程上获取事先绑定的Request对象）
        HttpServletRequest request = requestContext.getRequest();

        // 3.获取servletPath值
        String servletPath = request.getServletPath();

        // 4.根据servletPath判断当前请求是否对应可以直接放行的特定功能
        boolean containsResult = AccessPassResources.PASS_RES_SET.contains(servletPath);

        if (containsResult) {

            // 5.如果当前请求是可以直接放行的特定功能请求则返回false放行
            return false;
        }

        if(servletPath.startsWith("/pay/notify") || servletPath.startsWith("/pay/return")){
            return false;
        }

        //去掉前缀，比如"/project/images/1.jpg"=》"/images/1.jpg"
        servletPath = getRealUrl(servletPath);

        // 5.判断当前请求是否为静态资源
        // 工具方法返回true：说明当前请求是静态资源请求，取反为false表示放行不做登录检查
        // 工具方法返回false：说明当前请求不是可以放行的特定请求也不是静态资源，取反为true表示需要做登录检查
        return !AccessPassResources.judgeCurrentServletPathWetherStaticResource(servletPath);
    }

    /**
     * 如果未登陆，则请求转发到登陆页面，否则说明没问题，直接返回null，相当于正常进行请求
     */
    @Override
    public Object run() throws ZuulException {
        //获取context对象
        RequestContext requestContext = RequestContext.getCurrentContext();

        //拿到session
        HttpServletRequest request = requestContext.getRequest();
        HttpSession session = request.getSession();

        //判断，若session为空，直接转发请求
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        if (loginMember == null) {
            //将提示消息存入Session域,因为是跨tomcat，所以只能存到session中
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
            //拿到response
            HttpServletResponse response = requestContext.getResponse();
            //转发到登陆页面,注意这个也是跨tomcat
            try {
                response.sendRedirect("/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
