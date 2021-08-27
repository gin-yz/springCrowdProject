package com.atguigu.crowd.exception;

/**
 * 用户未登陆但是访问需要登陆才能访问的资源
 */
public class AccessForbiddenException extends RuntimeException{
    static final long serialVersionUID = 1L;

    public AccessForbiddenException() {
        super();
    }

    public AccessForbiddenException(String message) {
        super(message);
    }

    public AccessForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessForbiddenException(Throwable cause) {
        super(cause);
    }

    protected AccessForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
