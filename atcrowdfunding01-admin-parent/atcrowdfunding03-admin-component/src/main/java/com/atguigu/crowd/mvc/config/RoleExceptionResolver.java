package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.exception.AddRoleAlreadyExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class RoleExceptionResolver {

    private Logger logger = LoggerFactory.getLogger(RoleExceptionResolver.class);

    @ExceptionHandler(value = AddRoleAlreadyExistException.class)
    public void handleAddRoleAlreadyExistException(
            AddRoleAlreadyExistException exception,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        CrowdExceptionResolver.commonResolve(null,exception,request,response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ModelAndView securityAccessDeniedExceptionHandler(
            AccessDeniedException exception,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String viewName = "system-error";
        return CrowdExceptionResolver.commonResolve(viewName,exception,request,response);
    }
}
