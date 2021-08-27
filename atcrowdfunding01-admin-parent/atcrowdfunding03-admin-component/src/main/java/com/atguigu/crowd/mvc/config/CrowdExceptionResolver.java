package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.exception.AccessForbiddenException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ControllerAdvice注解就是对Controller进行AOP增强
 * ControllerAdvice注解主要使用场景为:
 * 全局异常处理，全局数据绑定，全局数据预处理
 * 具体看笔记
 */
@ControllerAdvice
public class CrowdExceptionResolver {

    //抽取的通用异常处理方法
    public static ModelAndView commonResolve(String viewName, //返回视图名
                                       Exception exception,//异常
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {
        //判断是不是Ajax请求
        boolean judgeRequestType = CrowdUtil.judgeRequestType(request);
        if (judgeRequestType) {
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());//空指针异常没有消息
            //返回为json，使用Gson工具
            Gson gson = new Gson();
            String responseJson = gson.toJson(resultEntity);
            response.getWriter().write(responseJson);
            //因为已经向response中写入了返回数据，返回null的话，springMVC会自动判断
            return null;
        }
        //如果不是Ajax请求，那么要返回视图
        ModelAndView modelAndView = new ModelAndView();
        //将异常设置返回
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
        //返回的试图名(jsp)
        modelAndView.setViewName(viewName);
        return modelAndView;
    }

    /**
     * 登陆异常处理
     */
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(
            LoginFailedException exception,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String viewName = "admin-login";
//        String viewName = "system-error";
        return commonResolve(viewName, exception, request, response);
    }

    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView resolveMathException(
            ArithmeticException exception,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        String viewName = "system-error";

        return commonResolve(viewName, exception, request, response);
    }

    //全局的NullPointerException捕获
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(
            NullPointerException exception,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String viewName = "system-error";

        return commonResolve(viewName, exception, request, response);
    }

    //经测试，@ControllerAdvice拦截的是全局的异常，因为在拦截器中抛出的异常也可以顺利拦截
    @ExceptionHandler(value = AccessForbiddenException.class)
    public ModelAndView resolveAccessForbiddenException(
            AccessForbiddenException exception,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String viewName = "admin-login";
        return commonResolve(viewName, exception, request, response);
    }

    //增加管理员用户时，若用户名一致
    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(
            LoginAcctAlreadyInUseException exception,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        String viewName = "admin-add";

        return commonResolve(viewName, exception, request, response);
    }

}
